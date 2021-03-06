package com.le_roux.sylvain.money.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.le_roux.sylvain.money.Data.Account;
import com.le_roux.sylvain.money.Dialog.LogInFragment;
import com.le_roux.sylvain.money.Dialog.NewAccountFragment;
import com.le_roux.sylvain.money.Interfaces.AccountContainer;
import com.le_roux.sylvain.money.Interfaces.Updatable;
import com.le_roux.sylvain.money.R;
import com.le_roux.sylvain.money.Utils.Logger;
import com.le_roux.sylvain.money.Utils.PriceView;

import org.json.JSONException;
import org.json.JSONObject;

public class Home extends AppCompatActivity implements Updatable, AccountContainer {

    private Account account;
    private Button coursesButton;
    private PriceView balanceAccount;
    private Button accountPageButton;
    private TextView accountNameView;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Set the toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        this.accountNameView = (TextView)findViewById(R.id.accountName);
        this.coursesButton = (Button)findViewById(R.id.coursesButton);
        this.accountPageButton = (Button)findViewById(R.id.accountPageButton);
        this.balanceAccount = (PriceView)findViewById(R.id.balanceAccount);

        if (this.balanceAccount != null)
            this.balanceAccount.setName(getString(R.string.Solde));

        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (!sharedPreferences.contains(Account.CURRENT_ACCOUNT)) {
            DialogFragment fragment = new NewAccountFragment();
            Bundle args = new Bundle();
            args.putBoolean(NewAccountFragment.CURRENT, true);
            fragment.setArguments(args);
            fragment.show(getSupportFragmentManager(), "New account");
        }

        this.accountPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, ManageAccount.class);
                startActivity(intent);
            }
        });

        this.coursesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, SharedAccounts.class);
                startActivity(intent);
            }
        });

        this.update();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.update();
    }

    public void setAccount(Account account) {
        this.account = account;
        this.accountNameView.setText(this.account.getName());
        this.accountNameView.setTextColor(this.account.getColor());
    }

    @Override
    public Account getAccount() {
        return this.account;
    }

    @Override
    public void update() {

        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPreferences.contains(Account.CURRENT_ACCOUNT)) {
            try {
                String accountName = sharedPreferences.getString(Account.CURRENT_ACCOUNT, "");
                this.setAccount(new Account(new JSONObject(sharedPreferences.getString(accountName, "")), this.sharedPreferences, this));
            } catch (JSONException e) {
                Logger.d("Error when recreating account JSON from string");
            }
        }

        if (this.balanceAccount != null) {
            if (this.account != null)
                this.balanceAccount.setValue(this.account.getBalance());
            else
                this.balanceAccount.setValue(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.default_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.changeAccount) : {
                DialogFragment fragment = new LogInFragment();
                fragment.show(getSupportFragmentManager(), "Change account");
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
