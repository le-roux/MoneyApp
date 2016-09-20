package com.le_roux.sylvain.money.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.le_roux.sylvain.money.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sylvain LE ROUX on 20/09/2016.
 */

public class SpendingsAdapter extends BaseAdapter{

    private Context context;
    private HashMap<String, Double> spendings;
    private ArrayList<String> categories;
    private double totalSpending;

    public SpendingsAdapter(Context context, ArrayList<String> categories, HashMap<String, Double> spendings) {
        this.context = context;
        this.categories = categories;
        this.spendings = spendings;
        totalSpending = 0;
    }

    @Override
    public int getCount() {
        return spendings.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_spendings, null);
            totalSpending = 0;
            for (Double value : spendings.values())
                totalSpending += value;
        }

        // Get the fields
        TextView categoryField = (TextView)convertView.findViewById(R.id.category);
        TextView valueField = (TextView)convertView.findViewById(R.id.value);
        TextView shareField = (TextView)convertView.findViewById(R.id.marketShare);

        // Fill the fields
        String categoryName = this.categories.get(position);
        categoryField.setText(categoryName);
        double value = this.spendings.get(categoryName);
        valueField.setText(String.valueOf(value) + '€');
        shareField.setText(String.valueOf(value / totalSpending) + '%');
        return null;
    }
}
