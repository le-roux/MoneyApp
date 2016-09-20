package com.le_roux.sylvain.money.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.widget.ListView;

import com.le_roux.sylvain.money.Data.Account;
import com.le_roux.sylvain.money.R;
import com.le_roux.sylvain.money.Utils.Logger;
import com.le_roux.sylvain.money.Utils.SpendingsController;

public class AnalyzeSpendings extends AppCompatActivity {

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

        // Fill the list
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        spendings.addHeaderView(inflater.inflate(R.layout.header_spendings, null));
        SpendingsController controller = new SpendingsController(this, account, spendings);
        controller.displaySpendingsForYear(2016);
    }


}
