package com.le_roux.sylvain.money.Utils;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.widget.ListView;

import com.le_roux.sylvain.money.Adapter.SpendingsAdapter;
import com.le_roux.sylvain.money.Data.Account;

import java.util.Calendar;
import java.util.HashMap;
import java.util.StringTokenizer;

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

    public HashMap<String, Double> computeSpendings(Cursor cursor) {
        cursor.moveToFirst();
        HashMap<String, Double> spendings = new HashMap<>();
        String category;
        double value;
        Logger.d("compute");
        while (!cursor.isAfterLast()) {
            Logger.d("while");
            category = cursor.getString(cursor.getColumnIndex(OperationContract.Table.COLUMN_NAME_CATEGORY));
            value = cursor.getDouble(cursor.getColumnIndex(OperationContract.Table.COLUMN_NAME_VALUE));
            if (value < 0) {
                if (spendings.containsKey(category)) {
                    spendings.put(category, spendings.get(category) - value);
                } else {
                    spendings.put(category, -value);
                }
            }
            cursor.moveToNext();
        }
        return spendings;
    }

    public void displaySpendingsForYear(int year) {
        AccountOpenHelper helper = this.account.getDatabaseHelper();
        String where = OperationContract.Table.COLUMN_NAME_ACCOUNT + " LIKE ? AND "
                + OperationContract.Table.COLUMN_NAME_YEAR + " LIKE ?";
        String args[] = {this.account.getName(), String.valueOf(year)};

        Cursor cursor = helper.query(this.COLUMNS, where, args);
        HashMap<String, Double> spendings = this.computeSpendings(cursor);

        this.adapter = new SpendingsAdapter(this.context, Account.getCategoriesList(), spendings);
        this.spendingsView.setAdapter(this.adapter);
    }

    public void displaySpendingsForPeriod(Calendar startDate, Calendar endDate) {
        AccountOpenHelper helper = this.account.getDatabaseHelper();
        String where = OperationContract.Table.COLUMN_NAME_ACCOUNT + " LIKE ? AND "
                + OperationContract.Table.COLUMN_NAME_YEAR + " BETWEEN ? AND ? AND "
                + OperationContract.Table.COLUMN_NAME_MONTH + " BETWEEN ? AND ? AND "
                + OperationContract.Table.COLUMN_NAME_DAY + " BETWEEN ? AND ?";
        String args[] = {this.account.getName(),
                String.valueOf(startDate.get(Calendar.YEAR)),
                String.valueOf(endDate.get(Calendar.YEAR)),
                String.valueOf(startDate.get(Calendar.MONTH) - 1),
                String.valueOf(endDate.get(Calendar.MONTH) - 1),
                String.valueOf(startDate.get(Calendar.DAY_OF_MONTH)),
                String.valueOf(endDate.get(Calendar.DAY_OF_MONTH))};

        Logger.d("where: " + where);
        Cursor cursor = helper.query(this.COLUMNS, where, args);
        Logger.d("cursor: " + cursor);
        HashMap<String, Double> spendings = this.computeSpendings(cursor);

        this.adapter = new SpendingsAdapter(this.context, Account.getCategoriesList(), spendings);
        this.spendingsView.setAdapter(this.adapter);
    }
}
