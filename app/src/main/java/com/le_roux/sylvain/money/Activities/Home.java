package com.le_roux.sylvain.money.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.le_roux.sylvain.money.Adapter.OperationAdapter;
import com.le_roux.sylvain.money.Data.Account;
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

        this.accountNameView = (TextView)findViewById(R.id.accountName);
        this.coursesButton = (Button)findViewById(R.id.coursesButton);
        this.accountPageButton = (Button)findViewById(R.id.accountPageButton);
        this.balanceAccount = (PriceView)findViewById(R.id.balanceAccount);

        if (this.balanceAccount != null)
            this.balanceAccount.setName(getString(R.string.Solde));

        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPreferences.contains(Account.CURRENT_ACCOUNT)) {
            try {
                String accountName = sharedPreferences.getString(Account.CURRENT_ACCOUNT, "");
                this.setAccount(new Account(new JSONObject(sharedPreferences.getString(accountName, "")), this.sharedPreferences, this));
            } catch (JSONException e) {
                Logger.d("Error when recreating account JSON from string");
            }
        }
        else {
            DialogFragment fragment = new NewAccountFragment();
            fragment.show(getSupportFragmentManager(), "New account");
        }

        this.accountPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, ManageAccount.class);
                startActivity(intent);
            }
        });
        if (this.account != null)
            this.accountNameView.setText(this.account.getName());
        this.update();
    }

    public void setAccount(Account account) {
        this.account = account;
        this.account.setTable(this);
        this.accountNameView.setText(this.account.getName());
    }

    @Override
    public Account getAccount() {
        return this.account;
    }

    @Override
    public void update() {
        if (this.balanceAccount != null) {
            if (this.account != null)
                this.balanceAccount.setValue(this.account.getBalance());
            else
                this.balanceAccount.setValue(0);
        }
    }
}
