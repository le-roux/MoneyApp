package com.le_roux.sylvain.money.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.le_roux.sylvain.money.Data.Account;
import com.le_roux.sylvain.money.R;
import com.le_roux.sylvain.money.Utils.PriceView;

import java.util.ArrayList;

/**
 * Created by Sylvain LE ROUX on 14/07/2016.
 */
public class AccountsDebtAdapter extends BaseAdapter {

    private ArrayList<Double> accountsDebt;
    private Context context;

    public AccountsDebtAdapter(Context context, ArrayList accountsDebt) {
        this.context = context;
        this.accountsDebt = accountsDebt;
    }

    public void setAccountsDebt(ArrayList<Double> accountsDebt) {
        this.accountsDebt = accountsDebt;
    }

    @Override
    public int getCount() {
        return accountsDebt.size();
    }

    @Override
    public Object getItem(int position) {
        return accountsDebt.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.accounts_debt, null);
        }

        TextView accountNameView = (TextView)convertView.findViewById(R.id.accountName);
        TextView actionView = (TextView)convertView.findViewById(R.id.action);
        PriceView debtView = (PriceView)convertView.findViewById(R.id.value);

        accountNameView.setText(Account.getAccountsList().get(position));
        if (this.accountsDebt.get(position) < 0) {
            actionView.setText(R.string.MustReceive);
            debtView.setValue(-this.accountsDebt.get(position));
        } else {
            actionView.setText(R.string.MustPay);
            debtView.setValue(this.accountsDebt.get(position));
        }

        return convertView;
    }
}
