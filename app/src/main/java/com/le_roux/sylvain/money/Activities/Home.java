package com.le_roux.sylvain.money.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.le_roux.sylvain.money.Data.Account;
import com.le_roux.sylvain.money.R;
import com.le_roux.sylvain.money.Utils.Logger;
import com.le_roux.sylvain.money.Utils.PriceView;

import org.json.JSONException;
import org.json.JSONObject;

public class Home extends AppCompatActivity {

    private Account account;

    private Button coursesButton;
    private PriceView balanceAccount;

    private SharedPreferences sharedPreferences;

    private static final String ACCOUNT = "home.account";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        JSONObject jsonAccount;
        try {
            jsonAccount = new JSONObject(this.sharedPreferences.getString(ACCOUNT, ""));
            this.account = new Account(jsonAccount);
        } catch (JSONException e) {
            Logger.d("Error when retrieving account JSON in Home activity");
            this.account = new Account("default");
        }

        this.coursesButton = (Button)findViewById(R.id.coursesButton);
        this.balanceAccount = (PriceView)findViewById(R.id.balanceAccount);
    }
}
