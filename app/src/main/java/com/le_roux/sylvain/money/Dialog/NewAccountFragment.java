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
import android.widget.EditText;

import com.le_roux.sylvain.money.Data.Account;
import com.le_roux.sylvain.money.Data.Operation;
import com.le_roux.sylvain.money.Interfaces.AccountContainer;
import com.le_roux.sylvain.money.Interfaces.Updatable;
import com.le_roux.sylvain.money.R;

/**
 * Created by Sylvain LE ROUX on 07/07/2016.
 */
public class NewAccountFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ViewGroup layout = (ViewGroup)inflater.inflate(R.layout.new_account_fragment, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(layout)
                .setCancelable(false)
                .setPositiveButton(R.string.Create, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText accountNameField = (EditText)layout.findViewById(R.id.name);
                        EditText initialBalanceField = (EditText)layout.findViewById(R.id.initialValue);
                        String accountName = accountNameField.getText().toString();
                        accountName = accountName.replaceAll(" ", "_");
                        //TODO check that this name doesn't already exists
                        Account account = new Account(accountName, PreferenceManager.getDefaultSharedPreferences(getActivity()), getActivity());
                        account.setTable(getActivity());
                        ((AccountContainer)getActivity()).setAccount(account);
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(Account.CURRENT_ACCOUNT, account.getName());
                        editor.putString(accountName, account.toString());
                        editor.apply();

                        String initialBalanceString = initialBalanceField.getText().toString();
                        if (!initialBalanceString.equals("")) {
                            double initialBalance = Double.parseDouble(initialBalanceString);
                            Operation operation = new Operation();
                            operation.setPayee(accountName);
                            operation.setValue(initialBalance);
                            operation.setCategory(getString(R.string.InitialValue));
                            operation.setDescription(getString(R.string.InitialValue));
                            account.addOperation(operation);
                        }
                        ((Updatable)getActivity()).update();
                    }
                });
        return builder.create();
    }
}
