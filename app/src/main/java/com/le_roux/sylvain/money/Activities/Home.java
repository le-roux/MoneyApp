package com.le_roux.sylvain.money.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.le_roux.sylvain.money.Adapter.OperationAdapter;
import com.le_roux.sylvain.money.Data.Account;
import com.le_roux.sylvain.money.Data.Operation;
import com.le_roux.sylvain.money.Dialog.NewAccountFragment;
import com.le_roux.sylvain.money.Dialog.NewOperationFragment;
import com.le_roux.sylvain.money.Interfaces.AccountContainer;
import com.le_roux.sylvain.money.Interfaces.DateViewContainer;
import com.le_roux.sylvain.money.Interfaces.Updatable;
import com.le_roux.sylvain.money.R;
import com.le_roux.sylvain.money.Utils.DateView;
import com.le_roux.sylvain.money.Utils.Logger;
import com.le_roux.sylvain.money.Utils.OperationListController;
import com.le_roux.sylvain.money.Utils.OperationListView;
import com.le_roux.sylvain.money.Utils.PriceView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Home extends AppCompatActivity implements AccountContainer, DateViewContainer, Updatable {

    private Button coursesButton;
    private Button newOperationButton;
    private OperationListView operationsListView;
    private PriceView balanceAccount;
    private DateViewContainer container;
    private SharedPreferences sharedPreferences;
    private OperationListController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        this.coursesButton = (Button)findViewById(R.id.coursesButton);
        this.operationsListView = (OperationListView)findViewById(R.id.operationsListView);
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
        this.operationsListView.addHeaderView(inflater.inflate(R.layout.header_operation, null));
        this.balanceAccount = (PriceView)findViewById(R.id.balanceAccount);
        this.newOperationButton = (Button)findViewById(R.id.addOperation);

        if (this.balanceAccount != null)
            this.balanceAccount.setName(getString(R.string.Solde));

        this.controller = new OperationListController(null, this.operationsListView, this);

        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPreferences.contains(Account.CURRENT_ACCOUNT)) {
            try {
                String accountName = sharedPreferences.getString(Account.CURRENT_ACCOUNT, "");
                this.setAccount(new Account(new JSONObject(sharedPreferences.getString(accountName, "")), this.sharedPreferences));
            } catch (JSONException e) {
                Logger.d("Error when recreating account JSON from string");
            }
        }
        else {
            DialogFragment fragment = new NewAccountFragment();
            fragment.show(getSupportFragmentManager(), "New account");
        }

        if (this.balanceAccount != null) {
            if (this.controller.getAccount() != null)
                this.balanceAccount.setValue(this.controller.getAccount().getBalance());
            else
                this.balanceAccount.setValue(0);
        }

        this.newOperationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewOperationFragment fragment = new NewOperationFragment();
                container = fragment;
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

    @Override
    public void setAccount(Account account) {
        this.controller.setAccount(account);
        this.controller.getAccount().setTable(this);
        this.controller.displayOperationsForYear(GregorianCalendar.getInstance().get(Calendar.YEAR));
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
        return this.sharedPreferences;
    }

    @Override
    public DateView getDateView() {
        if (container != null)
            return container.getDateView();
        return null;
    }

    @Override
    public void update() {
        this.controller.displayOperationsForYear(GregorianCalendar.getInstance().get(Calendar.YEAR));
        if (this.balanceAccount != null)
            if (this.controller.getAccount() != null)
                this.balanceAccount.setValue(this.controller.getAccount().getBalance());
            else
                this.balanceAccount.setValue(0);
    }
}
