package com.le_roux.sylvain.money.Utils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.le_roux.sylvain.money.Interfaces.DateViewContainer;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Sylvain LE ROUX on 11/07/2016.
 */
public class DatePickerFragment extends DialogFragment{

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = GregorianCalendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        return new DatePickerDialog(getActivity(), ((DateViewContainer)getActivity()).getDateView(), year, month, day);
    }
}
