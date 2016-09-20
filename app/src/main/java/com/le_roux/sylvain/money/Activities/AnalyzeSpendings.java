package com.le_roux.sylvain.money.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.le_roux.sylvain.money.Data.Account;
import com.le_roux.sylvain.money.Dialog.DatePickerFragment;
import com.le_roux.sylvain.money.Interfaces.DateViewContainer;
import com.le_roux.sylvain.money.R;
import com.le_roux.sylvain.money.Utils.DateView;
import com.le_roux.sylvain.money.Utils.SpendingsController;

public class AnalyzeSpendings extends AppCompatActivity implements DateViewContainer {

    private static final int START_DATE_VIEW = 0;
    private static final int END_DATE_VIEW = 1;
    private DateView dateViews[] = new DateView[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze_spendings);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Account account = Account.getCurrentAccount(sharedPreferences, this);

        // Set the toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        // Get the views
        ListView spendings = (ListView)findViewById(R.id.spendings);
        dateViews[START_DATE_VIEW] = (DateView)findViewById(R.id.startDate);
        dateViews[END_DATE_VIEW] = (DateView)findViewById(R.id.endDate);

        // Add listeners to select the dates
        dateViews[START_DATE_VIEW].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePickerFragment = new DatePickerFragment();
                Bundle info = new Bundle();
                info.putInt(DateView.DAY, dateViews[START_DATE_VIEW].getDay());
                info.putInt(DateView.MONTH, dateViews[START_DATE_VIEW].getMonth() - 1);
                info.putInt(DateView.YEAR, dateViews[START_DATE_VIEW].getYear());
                info.putInt(DateView.ID, START_DATE_VIEW);
                datePickerFragment.setArguments(info);
                datePickerFragment.show(getSupportFragmentManager(), "Date picker");
            }
        });

        dateViews[END_DATE_VIEW].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePickerFragment = new DatePickerFragment();
                Bundle info = new Bundle();
                info.putInt(DateView.DAY, dateViews[END_DATE_VIEW].getDay());
                info.putInt(DateView.MONTH, dateViews[END_DATE_VIEW].getMonth() - 1);
                info.putInt(DateView.YEAR, dateViews[END_DATE_VIEW].getYear());
                info.putInt(DateView.ID, END_DATE_VIEW);
                datePickerFragment.setArguments(info);
                datePickerFragment.show(getSupportFragmentManager(), "Date picker");
            }
        });

        // Fill the list
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        spendings.addHeaderView(inflater.inflate(R.layout.header_spendings, null));
        SpendingsController controller = new SpendingsController(this, account, spendings);
        controller.displaySpendingsForYear(2016);
    }


    @Override
    public DateView getDateView() {
        return getDateView(START_DATE_VIEW);
    }

    @Override
    public DateView getDateView(int id) {
        if (id < 2)
            return dateViews[id];
        else
            return null;
    }
}
