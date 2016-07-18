package com.le_roux.sylvain.money.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.le_roux.sylvain.money.Data.Account;
import com.le_roux.sylvain.money.Data.Operation;
import com.le_roux.sylvain.money.Interfaces.AccountContainer;
import com.le_roux.sylvain.money.Interfaces.DateViewContainer;
import com.le_roux.sylvain.money.Interfaces.SpinnerUpdater;
import com.le_roux.sylvain.money.Interfaces.Updatable;
import com.le_roux.sylvain.money.R;
import com.le_roux.sylvain.money.Utils.DateView;
import com.le_roux.sylvain.money.Utils.Logger;

import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;

/**
 * Created by Sylvain LE ROUX on 07/07/2016.
 */
public abstract class NewOperationFragment extends DialogFragment implements DateViewContainer, SpinnerUpdater {

    private DateView dateView = null;
    private RadioGroup radioGroup;
    private RadioButton debitButton;
    private RadioButton creditButton;
    private Spinner payeeView;
    private Spinner categoryView;
    private EditText valueView;
    private EditText descriptionView;
    private Button addPayeeButton;
    private Button addCategoryButton;

    private ArrayAdapter categoryAdapter;
    private ArrayAdapter payeeAdapter;

    public static final String STATUS = "operation.fragment.status";
    public static final int NEW = 0;
    public static final int CATEGORY_SPINNER = 0;
    public static final int PAYEE_SPINNER = 1;
    public static final int ACCOUNT_SPINNER = 2;

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ViewGroup layout = getLayoutView(inflater);

        // Get the views
        this.dateView = (DateView)layout.findViewById(R.id.date);
        this.radioGroup = (RadioGroup)layout.findViewById(R.id.Type);
        this.debitButton = (RadioButton)layout.findViewById(R.id.Debit);
        this.creditButton = (RadioButton)layout.findViewById(R.id.Credit);
        this.payeeView = (Spinner)layout.findViewById(R.id.payee);
        this.categoryView = (Spinner) layout.findViewById(R.id.category);
        this.valueView = (EditText)layout.findViewById(R.id.value);
        this.descriptionView = (EditText)layout.findViewById(R.id.description);
        this.addPayeeButton = (Button)layout.findViewById(R.id.addPayee);
        this.addCategoryButton = (Button)layout.findViewById(R.id.addCategory);

        // Local variables
        String positiveButtonText;
        final int id;

        // Initialization of the spinners
        Account.retrieveCategories(PreferenceManager.getDefaultSharedPreferences(getActivity()));
        this.categoryAdapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, Account.getCategoriesList());
        this.categoryView.setAdapter(this.categoryAdapter);
        Account.retrievePayees(PreferenceManager.getDefaultSharedPreferences(getActivity()));
        this.payeeAdapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, Account.getPayeesList());
        this.payeeView.setAdapter(payeeAdapter);

        // Set initial values
        Bundle info = getArguments();
        if (info != null) {
            // Set the date
            this.dateView.setYear(info.getInt(DateView.YEAR));
            this.dateView.setMonth(info.getInt(DateView.MONTH));
            this.dateView.setDay(info.getInt(DateView.DAY));

            // Set the payee name and add it to the list of payees if not already present
            String payeeName = info.getString(Operation.PAYEE);
            int payeeIndex = Account.getPayeesList().indexOf(payeeName);
            if (payeeIndex != -1) // Element present in the list
                this.payeeView.setSelection(payeeIndex);
            else if (payeeName != null && !payeeName.equals("")) {
                Account.getPayeesList().add(payeeName);
                Collections.sort(Account.getPayeesList());
                Account.savePayees(PreferenceManager.getDefaultSharedPreferences(getActivity()));
                this.payeeView.setSelection(Account.getPayeesList().size() - 1);
                payeeAdapter.notifyDataSetChanged();
            }

            // Set the category and add it to the list of categories if not already present
            String categoryName = info.getString(Operation.CATEGORY);
            int categoryIndex = Account.getCategoriesList().indexOf(categoryName);
            if (categoryIndex != -1) // Element present in the list
                this.categoryView.setSelection(categoryIndex);
            else if (categoryName != null && !categoryName.equals("")) {
                Account.getCategoriesList().add(categoryName);
                Collections.sort(Account.getCategoriesList());
                Account.saveCategories(PreferenceManager.getDefaultSharedPreferences(getActivity()));
                this.categoryView.setSelection(Account.getCategoriesList().size() - 1);
                categoryAdapter.notifyDataSetChanged();
            }

            // Set the type (debit/credit)
            double value = info.getDouble(Operation.VALUE);
            if (value > 0)
                this.creditButton.setChecked(true);
            else
                this.debitButton.setChecked(true);

            // Set the value
            if (value != 0)
                this.valueView.setText(String.valueOf(value));

            // Set the description
            this.descriptionView.setText(info.getString(Operation.DESCRIPTION));

            // Select the text of the positive button
            if ((id = info.getInt(STATUS)) == 0) // Operation not yet saved -> new one
                positiveButtonText = getString(R.string.Create);
            else
                positiveButtonText = getString(R.string.Update);
        } else {
            positiveButtonText = getString(R.string.Create);
            id = -1;
            this.debitButton.setChecked(true);
        }

        initCustomViews(layout, info);

        // Listeners
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

        this.addPayeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment fragment = new NewPayeeFragment();
                fragment.show(getActivity().getSupportFragmentManager(), "New category");
            }
        });

        this.addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment fragment = new NewCategoryFragment();
                fragment.show(getActivity().getSupportFragmentManager(), "New category");
            }
        });

        // Create the dialog
        builder.setView(layout)
                .setCancelable(false)
                .setNegativeButton(R.string.Cancel, null)
                .setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        boolean dataValidated = true;
                        // TODO check the validity of all the fields

                        // Get the values
                        int payeeIndex = payeeView.getSelectedItemPosition();
                        String payee = Account.getPayeesList().get(payeeIndex);
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

                            editOperation(operation);
                            saveOperation(operation, id);
                        } else {
                            DialogFragment fragment = getInstance();
                            Bundle info = setSpecificInfo();
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
        if (id != 0) { // Existing operation
            builder.setNeutralButton(R.string.Delete, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ((AccountContainer)getActivity()).getAccount().deleteOperation(id);
                    ((Updatable)getActivity()).update();
                }
            });
        }
        return builder.create();
    }

    @Override
    public DateView getDateView() {
        return this.dateView;
    }

    public abstract ViewGroup getLayoutView(LayoutInflater inflater);

    public abstract void initCustomViews(ViewGroup layout, Bundle info);

    public abstract NewOperationFragment getInstance();

    public abstract Bundle setSpecificInfo();

    public abstract void editOperation(Operation operation);

    public abstract void saveOperation(Operation operation, int id);

    @Override
    public boolean updateSpinner(int spinnerId, Object newItem) {
        switch (spinnerId) {
            case (CATEGORY_SPINNER): {
                int position = this.categoryAdapter.getPosition(newItem);
                this.categoryAdapter.notifyDataSetChanged();
                this.categoryView.setSelection(position);
                return true;
            }
            case (PAYEE_SPINNER) : {
                int position = this.payeeAdapter.getPosition(newItem);
                this.payeeAdapter.notifyDataSetChanged();
                this.payeeView.setSelection(position);
                return true;
            }
            default : return false;
        }
    }
}
