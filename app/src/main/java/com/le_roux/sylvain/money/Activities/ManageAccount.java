package com.le_roux.sylvain.money.Activities;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.le_roux.sylvain.money.Adapter.OperationAdapter;
import com.le_roux.sylvain.money.Data.Account;
import com.le_roux.sylvain.money.Data.Operation;
import com.le_roux.sylvain.money.Dialog.NewOperationFragment;
import com.le_roux.sylvain.money.Interfaces.AccountContainer;
import com.le_roux.sylvain.money.Interfaces.Updatable;
import com.le_roux.sylvain.money.R;
import com.le_roux.sylvain.money.Utils.DateView;
import com.le_roux.sylvain.money.Utils.OperationListController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class ManageAccount extends AppCompatActivity implements AccountContainer, Updatable{

    private OperationListController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_account);

        // Get the views
        ListView operationsListView = (ListView)findViewById(R.id.operationsListView);
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        operationsListView.addHeaderView(inflater.inflate(R.layout.header_operation, null));
        Button addOperationButton = (Button)findViewById(R.id.addOperation);

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

        this.controller = new OperationListController(account, operationsListView, this);
        this.update();

        if (addOperationButton != null) {
            addOperationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NewOperationFragment fragment = new NewOperationFragment();
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
    public OperationAdapter getOperationAdapter() {
        return this.controller.getAdapter();
    }

    @Override
    public SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    public void update() {
        this.controller.displayOperationsForYear(GregorianCalendar.getInstance().get(Calendar.YEAR));
    }
}
