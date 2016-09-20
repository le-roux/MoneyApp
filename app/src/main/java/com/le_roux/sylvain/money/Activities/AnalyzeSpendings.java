package com.le_roux.sylvain.money.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.widget.ListView;

import com.le_roux.sylvain.money.R;

public class AnalyzeSpendings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze_spendings);

        // Set the toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        // Get the views
        ListView spendings = (ListView)findViewById(R.id.spendings);

        // Fill the list
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        spendings.addHeaderView(inflater.inflate(R.layout.header_spendings, null));
    }


}
