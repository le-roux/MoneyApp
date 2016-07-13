package com.le_roux.sylvain.money.Utils;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.le_roux.sylvain.money.Adapter.OperationAdapter;
import com.le_roux.sylvain.money.Adapter.SharedOperationAdapter;
import com.le_roux.sylvain.money.Data.Account;
import com.le_roux.sylvain.money.Data.Operation;
import com.le_roux.sylvain.money.Dialog.NewOperationFragment;
import com.le_roux.sylvain.money.Dialog.NewSharedOperationFragment;
import com.le_roux.sylvain.money.Dialog.NewSimpleOperationFragment;
import com.le_roux.sylvain.money.Interfaces.FragmentContainer;

/**
 * Created by Sylvain LE ROUX on 13/07/2016.
 */
public class SharedOperationsListController {
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
    public SharedOperationsListController(ListView view, final AppCompatActivity activity) {
        this.view = view;
        this.activity = activity;
        this.cursor = null;

        this.view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    ((FragmentContainer)activity).setFragment(new NewSharedOperationFragment());
                    cursor.moveToFirst();
                    cursor.move(position - 1);
                    Bundle info = new Bundle();
                    info.putString(Operation.ACCOUNT, cursor.getString(cursor.getColumnIndexOrThrow(OperationContract.Table.COLUMN_NAME_ACCOUNT)));
                    info.putInt(DateView.YEAR, cursor.getInt(cursor.getColumnIndexOrThrow(OperationContract.Table.COLUMN_NAME_YEAR)));
                    info.putInt(DateView.MONTH, cursor.getInt(cursor.getColumnIndexOrThrow(OperationContract.Table.COLUMN_NAME_MONTH)));
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
        this.cursor = getOperationsForYear(year);
        this.adapter = new SharedOperationAdapter(this.activity, this.cursor, 0);
        this.view.setAdapter(this.adapter);
        return true;
    }

    public Cursor getOperationsForYear(int year) {
        AccountOpenHelper helper = new AccountOpenHelper(this.activity);
        helper.createTable();
        String where = OperationContract.Table.COLUMN_NAME_SHARED + " LIKE ? AND " +
                OperationContract.Table.COLUMN_NAME_YEAR + " LIKE ? ";
        String[] args = {String.valueOf(1), String.valueOf(year)};
        return helper.query(columns, where, args);
    }



}
