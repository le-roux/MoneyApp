package com.le_roux.sylvain.money.Activities;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.le_roux.sylvain.money.Data.Account;
import com.le_roux.sylvain.money.Interfaces.AccountContainer;
import com.le_roux.sylvain.money.R;
import com.le_roux.sylvain.money.Utils.AccountOpenHelper;
import com.le_roux.sylvain.money.Utils.Logger;
import com.le_roux.sylvain.money.Utils.NewAccountFragment;
import com.le_roux.sylvain.money.Utils.OperationContract;
import com.le_roux.sylvain.money.Utils.PriceView;

import org.json.JSONException;
import org.json.JSONObject;

public class Home extends AppCompatActivity implements AccountContainer {

    private Account account;

    private Button coursesButton;
    private ListView operationsListView;
    private PriceView balanceAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        this.coursesButton = (Button)findViewById(R.id.coursesButton);
        this.operationsListView = (ListView)findViewById(R.id.operationsListView);
        this.balanceAccount = (PriceView)findViewById(R.id.balanceAccount);

        if (this.balanceAccount != null)
            this.balanceAccount.setName(getString(R.string.Solde));

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPreferences.contains(Account.CURRENT_ACCOUNT)) {
            try {
                this.setAccount(new Account(new JSONObject(sharedPreferences.getString(Account.CURRENT_ACCOUNT, ""))));
            } catch (JSONException e) {
                Logger.d("Error when recreating account JSON from string");
            }
        }
        else {
            DialogFragment fragment = new NewAccountFragment();
            fragment.show(getSupportFragmentManager(), "New account");
        }
    }

    @Override
    public void setAccount(Account account) {
        this.account = account;
        AccountOpenHelper databaseHelper = new AccountOpenHelper(this, this.account.getName());
        SQLiteDatabase dbWrite = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(OperationContract.Table.COLUMN_NAME_PAYEE, this.account.getName());
        values.put(OperationContract.Table.COLUMN_NAME_VALUE, 10);
        values.put(OperationContract.Table.COLUMN_NAME_CATEGORY, "food");
        values.put(OperationContract.Table.COLUMN_NAME_DESCRIPTION, "");
        values.put(OperationContract.Table.COLUMN_NAME_VALIDATED, 0);
        values.put(OperationContract.Table.COLUMN_NAME_DAY, 20);
        values.put(OperationContract.Table.COLUMN_NAME_MONTH, 5);
        values.put(OperationContract.Table.COLUMN_NAME_YEAR, 2016);
        long newRowId;
        newRowId = dbWrite.insert(this.account.getName(),
                null, values);

        SQLiteDatabase dbRead = databaseHelper.getReadableDatabase();
        String[] selection = {OperationContract.Table._ID,
                OperationContract.Table.COLUMN_NAME_DAY,
                OperationContract.Table.COLUMN_NAME_MONTH,
                OperationContract.Table.COLUMN_NAME_YEAR,
                OperationContract.Table.COLUMN_NAME_PAYEE,
                OperationContract.Table.COLUMN_NAME_VALUE,
                OperationContract.Table.COLUMN_NAME_CATEGORY,
                OperationContract.Table.COLUMN_NAME_VALIDATED};
        Cursor c = dbRead.query(true, this.account.getName(), selection, null, null, null, null, null, null);

        int[] to = {R.id.day, R.id.month, R.id.year, R.id.payee, R.id.value, R.id.category, R.id.validated};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.item_operation, c, selection, to, 0);
        this.operationsListView.setAdapter(adapter);
    }

    @Override
    public Account getAccount() {
        return this.account;
    }
}
