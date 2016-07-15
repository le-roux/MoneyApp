package com.le_roux.sylvain.money.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.le_roux.sylvain.money.Data.Account;
import com.le_roux.sylvain.money.Interfaces.Updatable;
import com.le_roux.sylvain.money.R;

/**
 * Created by Sylvain LE ROUX on 15/07/2016.
 */
public class LogInFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup layout = (ViewGroup)inflater.inflate(R.layout.log_in_fragment, null);
        final Spinner accountsSpinner = (Spinner)layout.findViewById(R.id.accounts);
        EditText passwordField = (EditText)layout.findViewById(R.id.password);
        Account.retrieveAccounts(PreferenceManager.getDefaultSharedPreferences(getActivity()));
        ArrayAdapter accountsAdapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, Account.getAccountsList());
        accountsSpinner.setAdapter(accountsAdapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(layout)
                .setNegativeButton(R.string.Cancel, null)
                .setPositiveButton(R.string.LogIn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(Account.CURRENT_ACCOUNT, (String)accountsSpinner.getSelectedItem());
                        editor.apply();
                        ((Updatable)getActivity()).update();
                    }
                });
        return builder.create();
    }
}
