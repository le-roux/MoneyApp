package com.le_roux.sylvain.money.Dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.le_roux.sylvain.money.Data.Operation;
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
        if (this.sharedCheckBox.isChecked())
            operation.setShared(true);
        else
            operation.setShared(false);
    }
}
