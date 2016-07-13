package com.le_roux.sylvain.money.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.le_roux.sylvain.money.R;
import com.le_roux.sylvain.money.Utils.OperationContract;

/**
 * Created by Sylvain LE ROUX on 13/07/2016.
 */
public class SimpleOperationAdapter extends OperationAdapter {
    public SimpleOperationAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    public SimpleOperationAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    protected ViewGroup getLayout(LayoutInflater inflater) {
        return (ViewGroup)inflater.inflate(R.layout.item_operation, null);
    }

    @Override
    protected void bindCustomView(View view, Cursor cursor) {
        TextView validated = (TextView)view.findViewById(R.id.validated);
        TextView shared = (TextView)view.findViewById(R.id.shared);

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
