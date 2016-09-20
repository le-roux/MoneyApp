package com.le_roux.sylvain.money.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.le_roux.sylvain.money.Data.Account;
import com.le_roux.sylvain.money.Data.Operation;
import com.le_roux.sylvain.money.Dialog.NewOperationFragment;
import com.le_roux.sylvain.money.Dialog.NewSimpleOperationFragment;
import com.le_roux.sylvain.money.Interfaces.AccountContainer;
import com.le_roux.sylvain.money.Interfaces.DateViewContainer;
import com.le_roux.sylvain.money.Interfaces.FragmentContainer;
import com.le_roux.sylvain.money.Interfaces.Updatable;
import com.le_roux.sylvain.money.R;
import com.le_roux.sylvain.money.Utils.DateView;
import com.le_roux.sylvain.money.Utils.OperationListController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class ManageAccount extends AppCompatActivity implements AccountContainer, Updatable, DateViewContainer, FragmentContainer {

    private OperationListController controller;
    private NewOperationFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_account);

        // Set the toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        // Get the views
        ListView operationsListView = (ListView)findViewById(R.id.operationsListView);
        final LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        operationsListView.addHeaderView(inflater.inflate(R.layout.header_operation, null));
        Button addOperationButton = (Button)findViewById(R.id.addOperation);
        Button analyseSpendingsButton=  (Button)findViewById(R.id.analyseSpending);

        // Create the account
        Account account = null;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPreferences.contains(Account.CURRENT_ACCOUNT)) {
            JSONObject accountJSON;
            try {
                accountJSON = new JSONObject(sharedPreferences.getString(sharedPreferences.getString(Account.CURRENT_ACCOUNT, null), null));
            } catch (JSONException e) {
                accountJSON = null;
            }
            account = new Account(accountJSON, sharedPreferences, this);
        }

        Account.retrieveCategories(sharedPreferences);
        Account.retrievePayees(sharedPreferences);

        this.controller = new OperationListController(account, operationsListView, this);
        this.update();

        // Add listeners on buttons
        if (addOperationButton != null) {
            addOperationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragment = new NewSimpleOperationFragment();
                    Bundle info = new Bundle();
                    Calendar c = GregorianCalendar.getInstance();
                    info.putInt(DateView.YEAR, c.get(Calendar.YEAR));
                    info.putInt(DateView.MONTH, c.get(Calendar.MONTH) + 1);
                    info.putInt(DateView.DAY, c.get(Calendar.DAY_OF_MONTH));
                    info.putString(Operation.PAYEE, "");
                    info.putString(Operation.CATEGORY, "");
                    info.putDouble(Operation.VALUE, -0);
                    info.putString(Operation.DESCRIPTION, "");
                    info.putInt(NewOperationFragment.STATUS, NewOperationFragment.NEW);
                    fragment.setArguments(info);
                    fragment.show(getSupportFragmentManager(), "New operation");
                }
            });
        }

        if (analyseSpendingsButton != null) {
            analyseSpendingsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ManageAccount.this, AnalyzeSpendings.class);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void setAccount(Account account) {
        this.controller.setAccount(account);
    }

    @Override
    public Account getAccount() {
        return this.controller.getAccount();
    }

    @Override
    public void update() {
        this.controller.displayOperationsForYear(GregorianCalendar.getInstance().get(Calendar.YEAR));
    }

    @Override
    public DateView getDateView() {
        return fragment.getDateView();
    }

    @Override
    public NewOperationFragment getFragment() {
        return this.fragment;
    }

    @Override
    public void setFragment(NewOperationFragment fragment) {
        this.fragment = fragment;
    }
}
