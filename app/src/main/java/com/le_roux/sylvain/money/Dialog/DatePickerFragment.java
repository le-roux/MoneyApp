package com.le_roux.sylvain.money.Dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.le_roux.sylvain.money.Interfaces.DateViewContainer;
import com.le_roux.sylvain.money.Utils.DateView;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Sylvain LE ROUX on 11/07/2016.
 */
public class DatePickerFragment extends DialogFragment{

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int day, month, year, id;
        Bundle info = getArguments();
        if (info != null) {
            day = info.getInt(DateView.DAY);
            month = info.getInt(DateView.MONTH);
            year = info.getInt(DateView.YEAR);
            if (info.containsKey(DateView.ID))
                id = info.getInt(DateView.ID);
            else id = -1;
        } else {
            final Calendar c = GregorianCalendar.getInstance();
            day = c.get(Calendar.DAY_OF_MONTH);
            month = c.get(Calendar.MONTH);
            year = c.get(Calendar.YEAR);
            id = -1;
        }
        if (id == -1)
            return new DatePickerDialog(getActivity(), ((DateViewContainer)getActivity()).getDateView(), year, month, day);
        else
            return new DatePickerDialog(getActivity(), ((DateViewContainer)getActivity()).getDateView(id), year, month, day);
    }
}
