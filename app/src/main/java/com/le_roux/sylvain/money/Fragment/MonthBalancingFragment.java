package com.le_roux.sylvain.money.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.le_roux.sylvain.money.Interfaces.DebtDisplayer;
import com.le_roux.sylvain.money.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Sylvain LE ROUX on 14/07/2016.
 */
public class MonthBalancingFragment extends Fragment {

    private int month;
    private int year;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup layout = (ViewGroup)inflater.inflate(R.layout.month_balancing_fragment, container, false);
        Calendar calendar = GregorianCalendar.getInstance();

        // Month
        Spinner monthSpinner = (Spinner)layout.findViewById(R.id.monthSpinner);
        ArrayAdapter monthAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.Month, android.R.layout.simple_spinner_item);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthAdapter);
        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                month = position;
                ((DebtDisplayer)getActivity()).displayDebtsForMonth(month, year);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        monthSpinner.setSelection(calendar.get(Calendar.MONTH));

        // Year
        Spinner yearSpinner = (Spinner)layout.findViewById(R.id.yearSpinner);
        ArrayList<Integer> yearList = new ArrayList<>();
        for (int i = 0; i < 3000; i++) {
            yearList.add(i);
        }
        ArrayAdapter yearAdapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, yearList);
        yearSpinner.setAdapter(yearAdapter);
        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                year = position;
                ((DebtDisplayer)getActivity()).displayDebtsForMonth(month, year);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        yearSpinner.setSelection(calendar.get(Calendar.YEAR));

        return layout;
    }
}
