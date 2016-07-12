package com.le_roux.sylvain.money.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;

import com.le_roux.sylvain.money.Data.Account;
import com.le_roux.sylvain.money.R;

/**
 * Created by Sylvain LE ROUX on 12/07/2016.
 */
public class NewCategoryFragment extends DialogFragment{
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ViewGroup layout = (ViewGroup)inflater.inflate(R.layout.new_category_fragment, null);
        builder.setView(layout)
                .setCancelable(false)
                .setNegativeButton(R.string.Cancel, null)
                .setPositiveButton(R.string.Create, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText categoryView = (EditText)layout.findViewById(R.id.categoryName);
                        String category = categoryView.getText().toString();
                        Account.getCategoriesList().add(category);
                        // TODO what happens if category already exists
                        Account.saveCategories(PreferenceManager.getDefaultSharedPreferences(getActivity()));
                    }
                });
        return builder.create();
    }
}
