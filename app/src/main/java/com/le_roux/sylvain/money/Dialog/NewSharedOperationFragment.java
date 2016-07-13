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
import com.le_roux.sylvain.money.R;

/**
 * Created by Sylvain LE ROUX on 13/07/2016.
 */
public class NewSharedOperationFragment extends NewOperationFragment {
    @Override
    public ViewGroup getLayoutView(LayoutInflater inflater) {
        return (ViewGroup)inflater.inflate(R.layout.new_shared_operation_fragment, null);
    }

    @Override
    public void initCustomViews(ViewGroup layout) {
        Button newAccount = (Button)layout.findViewById(R.id.addAccount);
        Spinner accountSpinner = (Spinner)layout.findViewById(R.id.account);

        Account.retrieveAccounts(PreferenceManager.getDefaultSharedPreferences(getActivity()));
        ArrayAdapter<String> accountAdapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, Account.getAccountsList());
        accountSpinner.setAdapter(accountAdapter);

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
        return null;
    }
}
