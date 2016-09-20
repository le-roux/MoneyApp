package com.le_roux.sylvain.money.Utils;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.widget.ListView;

import com.le_roux.sylvain.money.Adapter.SpendingsAdapter;
import com.le_roux.sylvain.money.Data.Account;

import java.util.HashMap;

/**
 * Created by Sylvain LE ROUX on 20/09/2016.
 */

public class SpendingsController {
    private Account account;
    private SpendingsAdapter adapter;
    private ListView spendingsView;
    private Context context;

    private String[] COLUMNS = {
            OperationContract.Table.COLUMN_NAME_CATEGORY,
            OperationContract.Table.COLUMN_NAME_VALUE};

    public SpendingsController(Activity activity, Account account, ListView view) {
        this.context = activity;
        this.account = account;
        this.spendingsView = view;
        this.adapter = null;
    }

    public void displaySpendingsForYear(int year) {
        AccountOpenHelper helper = this.account.getDatabaseHelper();
        String where = OperationContract.Table.COLUMN_NAME_ACCOUNT + " LIKE ? AND "
                + OperationContract.Table.COLUMN_NAME_YEAR + " LIKE ?";
        String args[] = {this.account.getName(), String.valueOf(year)};
        Cursor cursor = helper.query(this.COLUMNS, where, args);
        cursor.moveToFirst();
        HashMap<String, Double> spendings = new HashMap<>();
        String category;
        double value;
        while (!cursor.isAfterLast()) {
            category = cursor.getString(cursor.getColumnIndex(OperationContract.Table.COLUMN_NAME_CATEGORY));
            value = cursor.getDouble(cursor.getColumnIndex(OperationContract.Table.COLUMN_NAME_VALUE));
            if (spendings.containsKey(category)) {
                spendings.put(category, spendings.get(category) + value);
            } else {
                spendings.put(category, value);
            }
            cursor.moveToNext();
        }

        this.adapter = new SpendingsAdapter(this.context, Account.getCategoriesList(), spendings);
        this.spendingsView.setAdapter(this.adapter);
    }
}
