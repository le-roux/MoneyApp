package com.le_roux.sylvain.money.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Path;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.le_roux.sylvain.money.Data.Account;
import com.le_roux.sylvain.money.Data.Operation;
import com.le_roux.sylvain.money.Interfaces.AccountContainer;
import com.le_roux.sylvain.money.Interfaces.DateViewContainer;
import com.le_roux.sylvain.money.Interfaces.Updatable;
import com.le_roux.sylvain.money.R;
import com.le_roux.sylvain.money.Utils.DateView;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Sylvain LE ROUX on 07/07/2016.
 */
public class NewOperationFragment extends DialogFragment implements DateViewContainer {

    private DateView dateView = null;
    private RadioGroup radioGroup;
    private RadioButton debitButton;
    private RadioButton creditButton;
    private EditText payeeView;
    private Spinner categoryView;
    private EditText valueView;
    private EditText descriptionView;

    public static final String STATUS = "operation.fragment.status";
    public static final int NEW = 0;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ViewGroup layout = (ViewGroup)inflater.inflate(R.layout.new_operation_fragment, null);

        // Get the views
        this.dateView = (DateView)layout.findViewById(R.id.date);
        this.radioGroup = (RadioGroup)layout.findViewById(R.id.Type);
        this.debitButton = (RadioButton)layout.findViewById(R.id.Debit);
        this.creditButton = (RadioButton)layout.findViewById(R.id.Credit);
        this.payeeView = (EditText)layout.findViewById(R.id.payee);
        this.categoryView = (Spinner) layout.findViewById(R.id.category);
        this.valueView = (EditText)layout.findViewById(R.id.value);
        this.descriptionView = (EditText)layout.findViewById(R.id.description);
        String positiveButtonText;
        final int id;

        ArrayAdapter spinnerAdapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, Account.getCategoriesList());
        this.categoryView.setAdapter(spinnerAdapter);

        // Set initial values
        Bundle info = getArguments();
        if (info == null)
            return null;
        this.dateView.setYear(info.getInt(DateView.YEAR));
        this.dateView.setMonth(info.getInt(DateView.MONTH));
        this.dateView.setDay(info.getInt(DateView.DAY));
        this.payeeView.setText(info.getString(Operation.PAYEE));
        String categoryName = info.getString(Operation.CATEGORY);
        int categoryIndex = Account.getCategoriesList().indexOf(categoryName);
        if (categoryIndex != -1)
            this.categoryView.setSelection(categoryIndex);
        else if (categoryName != "") {
            Account.getCategoriesList().add(categoryName);
            Account.saveCategories(PreferenceManager.getDefaultSharedPreferences(getActivity()));
            this.categoryView.setSelection(Account.getCategoriesList().size() - 1);
        }
        double value = info.getDouble(Operation.VALUE);
        if (value > 0)
            this.creditButton.setChecked(true);
        else
            this.debitButton.setChecked(true);
        if (value != 0)
            this.valueView.setText(String.valueOf(value));
        this.descriptionView.setText(info.getString(Operation.DESCRIPTION));
        if ((id = info.getInt(STATUS)) == 0)
            positiveButtonText = getString(R.string.Create);
        else
            positiveButtonText = getString(R.string.Update);

        this.dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePickerFragment = new DatePickerFragment();
                Bundle info = new Bundle();
                info.putInt(DateView.DAY, dateView.getDay());
                info.putInt(DateView.MONTH, dateView.getMonth() - 1);
                info.putInt(DateView.YEAR, dateView.getYear());
                datePickerFragment.setArguments(info);
                datePickerFragment.show(getFragmentManager(), "Date picker");
            }
        });
        builder.setView(layout)
                .setCancelable(false)
                .setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        boolean dataValidated = true;
                        // TODO check the validity of all the fields

                        // Get the values
                        String payee = payeeView.getText().toString();
                        int categoryIndex = categoryView.getSelectedItemPosition();
                        String category = Account.getCategoriesList().get(categoryIndex);
                        String valueString = valueView.getText().toString();
                        String description = descriptionView.getText().toString();

                        // Validate data & create the operation object
                        Operation operation = new Operation();
                        Calendar calendar = GregorianCalendar.getInstance();
                        calendar.set(Calendar.DAY_OF_MONTH, dateView.getDay());
                        calendar.set(Calendar.MONTH, dateView.getMonth() - 1);
                        calendar.set(Calendar.YEAR, dateView.getYear());
                        operation.setDate(calendar);

                        if (!payee.equals(""))
                            operation.setPayee(payee);
                        else {
                            Toast.makeText(getActivity(), getString(R.string.InvalidPayee), Toast.LENGTH_LONG).show();
                            dataValidated = false;
                        }

                        if (!category.equals(""))
                            operation.setCategory(category);
                        else {
                            Toast.makeText(getActivity(), getString(R.string.InvalidCategory), Toast.LENGTH_LONG).show();
                            dataValidated = false;
                        }


                        operation.setDescription(description);

                        double value = 0;
                        try {
                            value = Double.parseDouble(valueString);
                        } catch (NumberFormatException e) {
                            Toast.makeText(getActivity(), getString(R.string.InvalidValue), Toast.LENGTH_LONG).show();
                            dataValidated = false;
                        }
                        if (dataValidated) {
                            if (radioGroup.getCheckedRadioButtonId() == R.id.Debit && value > 0)
                                value = -value;
                            else if (radioGroup.getCheckedRadioButtonId() == R.id.Credit && value < 0)
                                value = -value;
                            operation.setValue(value);

                            if (id == 0)
                                ((AccountContainer)getActivity()).getAccount().addOperation(operation);
                            else {
                                ((AccountContainer)getActivity()).getAccount().updateOperation(id, operation);
                            }
                            ((Updatable)getActivity()).update();
                            // TODO update the list view
                        } else {
                            DialogFragment fragment = new NewOperationFragment();
                            Bundle info = new Bundle();
                            info.putInt(DateView.YEAR, dateView.getYear());
                            info.putInt(DateView.MONTH, dateView.getMonth() - 1);
                            info.putInt(DateView.DAY, dateView.getDay());
                            info.putString(Operation.PAYEE, payee);
                            info.putString(Operation.CATEGORY, category);
                            info.putDouble(Operation.VALUE, value);
                            info.putString(Operation.DESCRIPTION, description);
                            fragment.setArguments(info);
                            fragment.show(getFragmentManager(), "New Operation");
                        }
                    }
                });
        return builder.create();
    }

    @Override
    public DateView getDateView() {
        return this.dateView;
    }
}
