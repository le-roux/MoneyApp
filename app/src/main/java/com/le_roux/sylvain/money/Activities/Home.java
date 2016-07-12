package com.le_roux.sylvain.money.Activities;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.le_roux.sylvain.money.Adapter.OperationAdapter;
import com.le_roux.sylvain.money.Data.Account;
import com.le_roux.sylvain.money.Data.Operation;
import com.le_roux.sylvain.money.Interfaces.AccountContainer;
import com.le_roux.sylvain.money.Interfaces.DateViewContainer;
import com.le_roux.sylvain.money.Interfaces.Updatable;
import com.le_roux.sylvain.money.R;
import com.le_roux.sylvain.money.Utils.AccountOpenHelper;
import com.le_roux.sylvain.money.Utils.DateView;
import com.le_roux.sylvain.money.Utils.Logger;
import com.le_roux.sylvain.money.Utils.NewAccountFragment;
import com.le_roux.sylvain.money.Utils.NewOperationFragment;
import com.le_roux.sylvain.money.Utils.OperationContract;
import com.le_roux.sylvain.money.Utils.OperationListView;
import com.le_roux.sylvain.money.Utils.PriceView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Home extends AppCompatActivity implements AccountContainer, DateViewContainer, Updatable {

    private Account account;

    private Button coursesButton;
    private Button newOperationButton;
    private OperationListView operationsListView;
    private PriceView balanceAccount;
    private DateViewContainer container;
    private OperationAdapter adapter;
    private SharedPreferences sharedPreferences;

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
            if (this.account != null)
                this.balanceAccount.setValue(this.account.getBalance());
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
        this.account = account;
        this.account.setTable(this);
        AccountOpenHelper databaseHelper = new AccountOpenHelper(this, this.account.getName());

        SQLiteDatabase dbRead = databaseHelper.getReadableDatabase();
        String[] selection = {OperationContract.Table._ID,
                OperationContract.Table.COLUMN_NAME_DAY,
                OperationContract.Table.COLUMN_NAME_MONTH,
                OperationContract.Table.COLUMN_NAME_YEAR,
                OperationContract.Table.COLUMN_NAME_PAYEE,
                OperationContract.Table.COLUMN_NAME_VALUE,
                OperationContract.Table.COLUMN_NAME_CATEGORY,
                OperationContract.Table.COLUMN_NAME_DESCRIPTION,
                OperationContract.Table.COLUMN_NAME_VALIDATED};
        Cursor c = dbRead.query(true, this.account.getName(), selection, null, null, null, null, null, null);
        this.adapter = new OperationAdapter(this, c, 0);
        this.operationsListView.setCursor(c);
        this.operationsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    DialogFragment fragment = new NewOperationFragment();
                    Cursor cursor = operationsListView.getCursor();
                    cursor.moveToFirst();
                    cursor.move(position - 1);
                    Bundle info = new Bundle();
                    info.putInt(DateView.YEAR, cursor.getInt(cursor.getColumnIndexOrThrow(OperationContract.Table.COLUMN_NAME_YEAR)));
                    info.putInt(DateView.MONTH, cursor.getInt(cursor.getColumnIndexOrThrow(OperationContract.Table.COLUMN_NAME_MONTH)));
                    info.putInt(DateView.DAY, cursor.getInt(cursor.getColumnIndexOrThrow(OperationContract.Table.COLUMN_NAME_DAY)));
                    info.putString(Operation.PAYEE, cursor.getString(cursor.getColumnIndexOrThrow(OperationContract.Table.COLUMN_NAME_PAYEE)));
                    info.putString(Operation.CATEGORY, cursor.getString(cursor.getColumnIndexOrThrow(OperationContract.Table.COLUMN_NAME_CATEGORY)));
                    info.putDouble(Operation.VALUE, cursor.getInt(cursor.getColumnIndexOrThrow(OperationContract.Table.COLUMN_NAME_VALUE)));
                    info.putString(Operation.DESCRIPTION, cursor.getString(cursor.getColumnIndexOrThrow(OperationContract.Table.COLUMN_NAME_DESCRIPTION)));
                    info.putInt(NewOperationFragment.STATUS, cursor.getInt(cursor.getColumnIndexOrThrow(OperationContract.Table._ID)));
                    fragment.setArguments(info);
                    fragment.show(getSupportFragmentManager(), "New operation");
                }
            }
        });
        this.operationsListView.setAdapter(this.adapter);
    }

    @Override
    public Account getAccount() {
        return this.account;
    }

    @Override
    public OperationAdapter getOperationAdapter() {
        return this.adapter;
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
        if (this.balanceAccount != null)
            if (this.account != null)
                this.balanceAccount.setValue(this.account.getBalance());
            else
                this.balanceAccount.setValue(0);
    }
}
