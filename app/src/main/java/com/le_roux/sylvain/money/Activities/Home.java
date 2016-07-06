package com.le_roux.sylvain.money.Activities;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.le_roux.sylvain.money.Data.Account;
import com.le_roux.sylvain.money.R;
import com.le_roux.sylvain.money.Utils.AccountOpenHelper;
import com.le_roux.sylvain.money.Utils.Logger;
import com.le_roux.sylvain.money.Utils.OperationContract;
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

        AccountOpenHelper databaseHelper = new AccountOpenHelper(this);
        SQLiteDatabase dbWrite = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(OperationContract.Table.COLUMN_NAME_PAYEE, "foo");
        values.put(OperationContract.Table.COLUMN_NAME_VALUE, 5);
        values.put(OperationContract.Table.COLUMN_NAME_DESCRIPTION, "");
        values.put(OperationContract.Table.COLUMN_NAME_VALIDATED, 0);
        values.put(OperationContract.Table.COLUMN_NAME_DAY, 20);
        long newRowId;
        newRowId = dbWrite.insert(OperationContract.Table.TABLE_NAME,
                OperationContract.Table.COLUMN_NAME_ENTRY_ID, values);

        SQLiteDatabase dbRead = databaseHelper.getReadableDatabase();
        String[] projection = {OperationContract.Table.COLUMN_NAME_VALUE};
        Cursor c = dbRead.query(true, OperationContract.Table.TABLE_NAME, projection, null, null, null, null, null, null);
        c.moveToFirst();
        this.balanceAccount.setName("Balance");
        this.balanceAccount.setValue(c.getDouble(c.getColumnIndexOrThrow(OperationContract.Table.COLUMN_NAME_VALUE)));
    }
}
