package com.le_roux.sylvain.money.Dialog;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.le_roux.sylvain.money.Data.Account;
import com.le_roux.sylvain.money.Data.Operation;
import com.le_roux.sylvain.money.Interfaces.AccountContainer;
import com.le_roux.sylvain.money.Interfaces.Updatable;
import com.le_roux.sylvain.money.R;

/**
 * Created by Sylvain LE ROUX on 13/07/2016.
 */
public class NewSimpleOperationFragment extends NewOperationFragment{

    private CheckBox sharedCheckBox;

    @Override
    public ViewGroup getLayoutView(LayoutInflater inflater) {
        return (ViewGroup)inflater.inflate(R.layout.new_operation_fragment, null);
    }

    @Override
    public void initCustomViews(ViewGroup layout, Bundle info) {
        this.sharedCheckBox = (CheckBox)layout.findViewById(R.id.shared);
        if (info != null)
            this.sharedCheckBox.setChecked(info.getBoolean(Operation.SHARED));
    }

    @Override
    public NewOperationFragment getInstance() {
        return new NewSimpleOperationFragment();
    }

    @Override
    public Bundle setSpecificInfo() {
        return new Bundle(); // No specific info to add
    }

    @Override
    public void editOperation(Operation operation) {
        String accountName = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(Account.CURRENT_ACCOUNT, null);
        operation.setAccountName(accountName);
        if (this.sharedCheckBox.isChecked())
            operation.setShared(true);
        else
            operation.setShared(false);
    }

    @Override
    public void saveOperation(Operation operation, int id) {
        if (id == 0) // New operation
            ((AccountContainer)getActivity()).getAccount().addOperation(operation);
        else {
            ((AccountContainer)getActivity()).getAccount().updateOperation(id, operation);
        }
        ((Updatable)getActivity()).update();
    }
}
