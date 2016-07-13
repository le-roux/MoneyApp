package com.le_roux.sylvain.money.Dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

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
    public void initCustomViews() {

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
