package com.le_roux.sylvain.money.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.le_roux.sylvain.money.Data.Account;
import com.le_roux.sylvain.money.Data.Operation;
import com.le_roux.sylvain.money.Interfaces.AccountContainer;
import com.le_roux.sylvain.money.Interfaces.Updatable;
import com.le_roux.sylvain.money.R;
import com.le_roux.sylvain.money.Utils.Logger;

import java.util.ArrayList;

/**
 * Created by Sylvain LE ROUX on 07/07/2016.
 */
public class NewAccountFragment extends DialogFragment {

    public static final String CURRENT = "accountFragment.current";

    private int color;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ViewGroup layout = (ViewGroup)inflater.inflate(R.layout.new_account_fragment, null);

        Spinner colorsSpinner = (Spinner)layout.findViewById(R.id.color);
        final ImageView colorsViewer = (ImageView)layout.findViewById(R.id.colorViewer);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.ColorsName, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colorsSpinner.setAdapter(adapter);

        final String[] colors = getResources().getStringArray(R.array.ColorsValue);
        colorsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setColor(Color.parseColor(colors[position]));
                colorsViewer.setBackgroundColor(getColor());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(layout)
                .setCancelable(false)
                .setPositiveButton(R.string.Create, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Get the views
                        EditText accountNameField = (EditText)layout.findViewById(R.id.name);
                        EditText initialBalanceField = (EditText)layout.findViewById(R.id.initialValue);
                        EditText passwordField = (EditText)layout.findViewById(R.id.password);

                        // Prepare the local variables
                        boolean valid = true;
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        String accountName = accountNameField.getText().toString();
                        // Check if this account already exists and is valid
                        Account.retrieveAccounts(sharedPreferences);
                        if (accountName.equals("") || Account.getAccountsList().contains(accountName)) {
                            Toast.makeText(getActivity(), R.string.InvalidName, Toast.LENGTH_LONG).show();
                            valid = false; // Invalid account name
                        }
                        String password = passwordField.getText().toString();
                        if (password.equals("")) {
                            Toast.makeText(getActivity(), R.string.InvalidPassword, Toast.LENGTH_LONG).show();
                            valid = false;
                        }
                        String initialBalanceString = initialBalanceField.getText().toString();
                        if (initialBalanceString.equals("")) {
                            Toast.makeText(getActivity(), R.string.InvalidInitialValue, Toast.LENGTH_LONG).show();
                            valid = false;
                        }

                        if (valid) {
                            // Create the account
                            Account account = new Account(accountName, getColor(), password, PreferenceManager.getDefaultSharedPreferences(getActivity()), getActivity());
                            for (int i = 0; i < getActivity().getClass().getInterfaces().length; i++) {
                                if (getActivity().getClass().getInterfaces()[i].equals(AccountContainer.class)) {
                                    ((AccountContainer) getActivity()).setAccount(account);
                                    break;
                                }
                            }

                            // Save the account in Shared preferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            Bundle args = getArguments();
                            if (args != null && args.getBoolean(CURRENT))
                                editor.putString(Account.CURRENT_ACCOUNT, account.getName());
                            editor.putString(accountName, account.toString());
                            editor.apply();
                            Account.getAccountsList().add(accountName);
                            Account.saveAccounts(sharedPreferences);

                            if (!initialBalanceString.equals("")) {
                                double initialBalance = Double.parseDouble(initialBalanceString);
                                Operation operation = new Operation();
                                operation.setAccountName(accountName);
                                operation.setPayee(accountName);
                                operation.setValue(initialBalance);
                                operation.setCategory(getString(R.string.InitialValue));
                                operation.setDescription(getString(R.string.InitialValue));
                                account.addOperation(operation);
                            }
                            ((Updatable) getActivity()).update();
                        } else {
                            DialogFragment fragment = new NewAccountFragment();
                            fragment.show(getFragmentManager(), "New Account");
                        }
                    }
                });
        return builder.create();
    }

    private int getColor() {
        return this.color;
    }

    private void setColor(int color) {
        this.color = color;
    }
}
