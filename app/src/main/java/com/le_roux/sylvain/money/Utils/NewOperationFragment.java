package com.le_roux.sylvain.money.Utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.le_roux.sylvain.money.Data.Operation;
import com.le_roux.sylvain.money.Interfaces.AccountContainer;
import com.le_roux.sylvain.money.Interfaces.DateViewContainer;
import com.le_roux.sylvain.money.R;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Sylvain LE ROUX on 07/07/2016.
 */
public class NewOperationFragment extends DialogFragment implements DateViewContainer {

    private DateView dateView = null;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ViewGroup layout = (ViewGroup)inflater.inflate(R.layout.new_operation_fragment, null);
        this.dateView = (DateView)layout.findViewById(R.id.date);
        this.dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.show(getFragmentManager(), "Date picker");
            }
        });
        builder.setView(layout)
                .setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton(R.string.Create, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO check the validity of all the fields
                        // Get the views
                        DateView dateView =(DateView)layout.findViewById(R.id.date);
                        RadioGroup radioGroup = (RadioGroup)layout.findViewById(R.id.Type);
                        EditText payeeView = (EditText)layout.findViewById(R.id.payee);
                        EditText categoryView = (EditText)layout.findViewById(R.id.category);
                        EditText valueView = (EditText)layout.findViewById(R.id.value);
                        EditText descriptionView = (EditText)layout.findViewById(R.id.description);

                        // Create the operation object
                        Operation operation = new Operation();
                        // TODO allow to change the date
                        Calendar calendar = GregorianCalendar.getInstance();
                        calendar.set(Calendar.DAY_OF_MONTH, dateView.getDay());
                        calendar.set(Calendar.MONTH, dateView.getMonth());
                        calendar.set(Calendar.YEAR, dateView.getYear());
                        operation.setDate(calendar);

                        operation.setPayee(payeeView.getText().toString());
                        operation.setCategory(categoryView.getText().toString());
                        double value = Double.parseDouble(valueView.getText().toString());
                        if (radioGroup.getCheckedRadioButtonId() == R.id.Debit)
                            if (value > 0)
                                value = -value;
                        else
                            if(value < 0)
                                value = -value;
                        operation.setValue(value);
                        operation.setDescription(descriptionView.getText().toString());
                        ((AccountContainer)getActivity()).getAccount().addOperation(operation);
                        // TODO update the list view
                    }
                });
        return builder.create();
    }

    @Override
    public DateView getDateView() {
        return this.dateView;
    }
}
