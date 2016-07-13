package com.le_roux.sylvain.money.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.le_roux.sylvain.money.R;
import com.le_roux.sylvain.money.Utils.DateView;
import com.le_roux.sylvain.money.Utils.OperationContract;

/**
 * Created by Sylvain LE ROUX on 07/07/2016.
 */
public class OperationAdapter extends CursorAdapter {
    public OperationAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    public OperationAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup layout = (ViewGroup)inflater.inflate(R.layout.item_operation, null);
        bindView(layout, context, cursor);
        return layout;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Get the views
        DateView dateView = (DateView)view.findViewById(R.id.date);
        TextView payee = (TextView)view.findViewById(R.id.payee);
        TextView category = (TextView)view.findViewById(R.id.category);
        TextView valueView = (TextView)view.findViewById(R.id.value);
        TextView validated = (TextView)view.findViewById(R.id.validated);
        TextView shared = (TextView)view.findViewById(R.id.shared);

        // Fill the views
        dateView.setDay(cursor.getInt(cursor.getColumnIndexOrThrow(OperationContract.Table.COLUMN_NAME_DAY)));
        dateView.setMonth(cursor.getInt(cursor.getColumnIndexOrThrow(OperationContract.Table.COLUMN_NAME_MONTH)) + 1);
        dateView.setYear(cursor.getInt(cursor.getColumnIndexOrThrow(OperationContract.Table.COLUMN_NAME_YEAR)));

        payee.setText(cursor.getString(cursor.getColumnIndexOrThrow(OperationContract.Table.COLUMN_NAME_PAYEE)));
        category.setText(cursor.getString(cursor.getColumnIndexOrThrow(OperationContract.Table.COLUMN_NAME_CATEGORY)));
        double value = cursor.getDouble(cursor.getColumnIndexOrThrow(OperationContract.Table.COLUMN_NAME_VALUE));
        valueView.setText(String.valueOf(value));
        if (value >= 0)
            valueView.setBackgroundResource(R.color.green);
        else
            valueView.setBackgroundResource(R.color.red);

        int v = cursor.getInt(cursor.getColumnIndexOrThrow(OperationContract.Table.COLUMN_NAME_VALIDATED));
        if (v == 1)
            validated.setText("V");
        else
            validated.setText(" ");

        int s = cursor.getInt(cursor.getColumnIndexOrThrow(OperationContract.Table.COLUMN_NAME_SHARED));
        if (s == 1)
            shared.setText(R.string.S);
        else
            shared.setText(" ");
    }
}
