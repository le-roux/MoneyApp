package com.le_roux.sylvain.money.Utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.le_roux.sylvain.money.Adapter.OperationAdapter;
import com.le_roux.sylvain.money.Adapter.SimpleOperationAdapter;
import com.le_roux.sylvain.money.Data.Account;
import com.le_roux.sylvain.money.Data.Operation;
import com.le_roux.sylvain.money.Dialog.NewOperationFragment;
import com.le_roux.sylvain.money.Dialog.NewSimpleOperationFragment;
import com.le_roux.sylvain.money.Interfaces.FragmentContainer;

/**
 * Created by Sylvain LE ROUX on 12/07/2016.
 */

/**
 * This class is responsible for controlling the link between the account and the related view (OperationsListView).
 * In the MVC pattern, it plays the role of the controller.
 * It's also here that are the different query methods for different display (by year, by category, by payee, ...).
 */
public class OperationListController {

    private Account account; // Model
    private ListView view; // View
    private Cursor cursor;
    private OperationAdapter adapter;
    private AppCompatActivity activity;

    private static String[] columns = {
            OperationContract.Table._ID,
            OperationContract.Table.COLUMN_NAME_ACCOUNT,
            OperationContract.Table.COLUMN_NAME_DAY,
            OperationContract.Table.COLUMN_NAME_MONTH,
            OperationContract.Table.COLUMN_NAME_YEAR,
            OperationContract.Table.COLUMN_NAME_PAYEE,
            OperationContract.Table.COLUMN_NAME_VALUE,
            OperationContract.Table.COLUMN_NAME_CATEGORY,
            OperationContract.Table.COLUMN_NAME_DESCRIPTION,
            OperationContract.Table.COLUMN_NAME_VALIDATED,
            OperationContract.Table.COLUMN_NAME_SHARED};

    /*
     *  Constructors
     */
    public OperationListController(Account account, ListView view, final AppCompatActivity activity) {
        this.account = account;
        this.view = view;
        this.activity = activity;
        this.cursor = null;

        this.view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    ((FragmentContainer)activity).setFragment(new NewSimpleOperationFragment());
                    cursor.moveToFirst();
                    cursor.move(position - 1);
                    Bundle info = new Bundle();
                    info.putInt(DateView.YEAR, cursor.getInt(cursor.getColumnIndexOrThrow(OperationContract.Table.COLUMN_NAME_YEAR)));
                    info.putInt(DateView.MONTH, cursor.getInt(cursor.getColumnIndexOrThrow(OperationContract.Table.COLUMN_NAME_MONTH)) + 1);
                    info.putInt(DateView.DAY, cursor.getInt(cursor.getColumnIndexOrThrow(OperationContract.Table.COLUMN_NAME_DAY)));
                    info.putString(Operation.PAYEE, cursor.getString(cursor.getColumnIndexOrThrow(OperationContract.Table.COLUMN_NAME_PAYEE)));
                    info.putString(Operation.CATEGORY, cursor.getString(cursor.getColumnIndexOrThrow(OperationContract.Table.COLUMN_NAME_CATEGORY)));
                    info.putDouble(Operation.VALUE, cursor.getDouble(cursor.getColumnIndexOrThrow(OperationContract.Table.COLUMN_NAME_VALUE)));
                    info.putString(Operation.DESCRIPTION, cursor.getString(cursor.getColumnIndexOrThrow(OperationContract.Table.COLUMN_NAME_DESCRIPTION)));
                    info.putInt(NewOperationFragment.STATUS, cursor.getInt(cursor.getColumnIndexOrThrow(OperationContract.Table._ID)));
                    ((FragmentContainer) activity).getFragment().setArguments(info);
                    ((FragmentContainer) activity).getFragment().show(activity.getSupportFragmentManager(), "New operation");
                }
            }
        });
    }

    /*
     *  Getters and Setters
     */
    public void setAccount(Account account) {
        this.account = account;
    }

    public Account getAccount() {
        return this.account;
    }

    public OperationAdapter getAdapter() {
        return this.adapter;
    }

    /*
     *  Other functions
     */

    /**
     * Get all the operations done on the account during the specified year and display them.
     * @param year the year we are interested in
     */
    public boolean displayOperationsForYear(int year) {
        if (this.account == null)
            return false;
        this.cursor = getOperationsForYear(year);
        this.adapter = new SimpleOperationAdapter(this.activity, this.cursor, 0);
        this.view.setAdapter(this.adapter);
        return true;
    }

    public Cursor getOperationsForYear(int year) {
        AccountOpenHelper helper = this.account.getDatabaseHelper();
        String where = OperationContract.Table.COLUMN_NAME_ACCOUNT + " LIKE ? AND " +
                OperationContract.Table.COLUMN_NAME_YEAR + " LIKE ? ";
        String[] args = {"", ""};
        String order = OperationContract.Table.COLUMN_NAME_YEAR + ", "
                + OperationContract.Table.COLUMN_NAME_MONTH + ", "
                + OperationContract.Table.COLUMN_NAME_DAY;
        if (this.account != null)
            args[0] = this.account.getName();
        else
            args[0] = "*";
        args[1] = String.valueOf(year);
        return helper.queryOrdered(columns, where, args, order);
    }




}
