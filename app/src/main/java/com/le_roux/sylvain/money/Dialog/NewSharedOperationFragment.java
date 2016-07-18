package com.le_roux.sylvain.money.Dialog;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.le_roux.sylvain.money.Data.Account;
import com.le_roux.sylvain.money.Data.Operation;
import com.le_roux.sylvain.money.Interfaces.Updatable;
import com.le_roux.sylvain.money.R;
import com.le_roux.sylvain.money.Utils.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;

/**
 * Created by Sylvain LE ROUX on 13/07/2016.
 */
public class NewSharedOperationFragment extends NewOperationFragment {

    private Spinner accountSpinner;
    private ArrayAdapter<String> accountAdapter;

    @Override
    public ViewGroup getLayoutView(LayoutInflater inflater) {
        return (ViewGroup)inflater.inflate(R.layout.new_shared_operation_fragment, null);
    }

    @Override
    public void initCustomViews(ViewGroup layout, Bundle info) {
        Button newAccount = (Button)layout.findViewById(R.id.addAccount);
        this.accountSpinner = (Spinner)layout.findViewById(R.id.account);

        Account.retrieveAccounts(PreferenceManager.getDefaultSharedPreferences(getActivity()));
        this.accountAdapter = new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, Account.getAccountsList());
        this.accountSpinner.setAdapter(this.accountAdapter);

        if (info != null) {
            String accountName = info.getString(Account.ACCOUNT);
            int index = Account.getAccountsList().indexOf(accountName);
            if (index == -1) {
                if (accountName != null && !accountName.equals("")) {
                    Account.getAccountsList().add(accountName);
                    Collections.sort(Account.getAccountsList());
                    Account.saveAccounts(PreferenceManager.getDefaultSharedPreferences(getActivity()));
                    this.accountAdapter.notifyDataSetChanged();
                    this.accountSpinner.setSelection(Account.getAccountsList().size() - 1);
                }
            } else
                this.accountSpinner.setSelection(index);
        }

        newAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment fragment = new NewAccountFragment();
                Bundle args = new Bundle();
                args.putBoolean(NewAccountFragment.CURRENT, false);
                fragment.setArguments(args);
                fragment.show(getFragmentManager(), "New account");
            }
        });

    }

    @Override
    public NewOperationFragment getInstance() {
        return new NewSharedOperationFragment();
    }

    @Override
    public Bundle setSpecificInfo() {
        Bundle info = new Bundle();
        int accountIndex = this.accountSpinner.getSelectedItemPosition();
        Account.retrieveAccounts(PreferenceManager.getDefaultSharedPreferences(getActivity()));
        String accountName = Account.getAccountsList().get(accountIndex);
        info.putString(Account.ACCOUNT, accountName);
        return info;
    }

    @Override
    public void editOperation(Operation operation) {
        operation.setAccountName((String)this.accountSpinner.getSelectedItem());
        operation.setShared(true);
    }

    @Override
    public void saveOperation(Operation operation, int id) {
        String accountName = (String)this.accountSpinner.getSelectedItem();
        if (accountName!= null && !accountName.equals("")) {
            String accountString = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(accountName, null);
            JSONObject jsonAccount = null;
            try {
                jsonAccount = new JSONObject(accountString);
            } catch (JSONException e) {
                Logger.d("Error when creating account in NewSharedOperationFragment");
            }

            Account account = new Account(jsonAccount, PreferenceManager.getDefaultSharedPreferences(getActivity()), getActivity());
            if (id == -1) // New operation
                account.addOperation(operation);
            else
                account.updateOperation(id, operation);
            ((Updatable)getActivity()).update();
        }
    }

    @Override
    public boolean updateSpinner(int spinnerId, Object newItem) {
        if (!super.updateSpinner(spinnerId, newItem)) {
            if (spinnerId == ACCOUNT_SPINNER) {
                int position = Account.getAccountsList().indexOf(newItem);
                this.accountAdapter.notifyDataSetChanged();
                this.accountSpinner.setSelection(position);
                return true;
            }
            return false;
        }
        return true;
    }
}
