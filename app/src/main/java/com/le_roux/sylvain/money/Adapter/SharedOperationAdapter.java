package com.le_roux.sylvain.money.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.le_roux.sylvain.money.Data.Account;
import com.le_roux.sylvain.money.R;
import com.le_roux.sylvain.money.Utils.OperationContract;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sylvain LE ROUX on 13/07/2016.
 */
public class SharedOperationAdapter extends OperationAdapter {

    private Context context;

    public SharedOperationAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        this.context = context;
    }

    public SharedOperationAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        this.context = context;
    }

    @Override
    protected ViewGroup getLayout(LayoutInflater inflater) {
        return (ViewGroup)inflater.inflate(R.layout.item_shared_operation, null);
    }

    @Override
    protected void bindCustomView(View view, Cursor cursor) {
        TextView accountNameView = (TextView)view.findViewById(R.id.accountName);
        String accountName = cursor.getString(cursor.getColumnIndexOrThrow(OperationContract.Table.COLUMN_NAME_ACCOUNT));
        accountNameView.setText(accountName);
        try {
            JSONObject accountJSON = new JSONObject(PreferenceManager.getDefaultSharedPreferences(this.context).getString(accountName, null));
            Account account = new Account(accountJSON, null, this.context);
            accountNameView.setBackgroundColor(account.getColor());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
