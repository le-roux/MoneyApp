package com.le_roux.sylvain.money.Activities;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.le_roux.sylvain.money.Data.Account;
import com.le_roux.sylvain.money.R;
import com.le_roux.sylvain.money.Utils.AccountOpenHelper;
import com.le_roux.sylvain.money.Utils.OperationContract;
import com.le_roux.sylvain.money.Utils.PriceView;

public class Home extends AppCompatActivity {

    private Account account;

    private Button coursesButton;
    private PriceView balanceAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        this.coursesButton = (Button)findViewById(R.id.coursesButton);
        this.balanceAccount = (PriceView)findViewById(R.id.balanceAccount);

        if (this.balanceAccount != null)
            this.balanceAccount.setName(getString(R.string.Solde));

        AccountOpenHelper databaseHelper = new AccountOpenHelper(this);
        SQLiteDatabase dbWrite = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(OperationContract.Table.COLUMN_NAME_PAYEE, "foo");
        values.put(OperationContract.Table.COLUMN_NAME_VALUE, 10);
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
        this.balanceAccount.setValue(c.getDouble(c.getColumnIndexOrThrow(OperationContract.Table.COLUMN_NAME_VALUE)));
    }
}
