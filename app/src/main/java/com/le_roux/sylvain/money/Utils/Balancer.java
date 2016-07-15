package com.le_roux.sylvain.money.Utils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;

import com.le_roux.sylvain.money.Data.Account;

import java.util.ArrayList;

/**
 * Created by Sylvain LE ROUX on 15/07/2016.
 */
public class Balancer {

    public static ArrayList<Double> getDebtsForMonth(int year, int month, Activity parentActivity) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(parentActivity);
        Account.retrieveAccounts(sharedPreferences);
        AccountOpenHelper helper = new AccountOpenHelper(parentActivity);
        ArrayList<Double> accountsPayments = new ArrayList<>();
        ArrayList<Double> accountsDebt = new ArrayList<>();
        double totalSum = 0, average;
        for (String name : Account.getAccountsList()) {
            String[] columns = {OperationContract.Table.COLUMN_NAME_VALUE};
            StringBuilder selection = new StringBuilder();
            selection.append(OperationContract.Table.COLUMN_NAME_ACCOUNT)
                    .append(" LIKE ? AND ")
                    .append(OperationContract.Table.COLUMN_NAME_YEAR)
                    .append(" LIKE ? AND ")
                    .append(OperationContract.Table.COLUMN_NAME_SHARED)
                    .append(" LIKE 1");
            if (month >= 0 && month < 12) {
                selection.append(" AND ")
                        .append(OperationContract.Table.COLUMN_NAME_MONTH)
                        .append(" LIKE ")
                        .append(month);
            }
            String[] args = {name, String.valueOf(year)};
            Cursor cursor = helper.query(columns, selection.toString(), args);
            double sum = 0;
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                sum += cursor.getDouble(cursor.getColumnIndexOrThrow(OperationContract.Table.COLUMN_NAME_VALUE));
                cursor.moveToNext();
            }
            accountsPayments.add(sum);
            totalSum += sum;
        }

        average = totalSum / accountsPayments.size();

        for (int i = 0; i < accountsPayments.size(); i++)
            accountsDebt.add(accountsPayments.get(i) - average);

        return accountsDebt;
    }

    public static ArrayList<Double> getDebtsForYear(int year, Activity parentActivity) {
        return getDebtsForMonth(year, -1, parentActivity);
    }

}
