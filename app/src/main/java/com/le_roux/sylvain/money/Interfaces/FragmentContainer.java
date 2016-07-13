package com.le_roux.sylvain.money.Interfaces;

import com.le_roux.sylvain.money.Dialog.NewOperationFragment;

/**
 * Created by Sylvain LE ROUX on 13/07/2016.
 */
public interface FragmentContainer {
    NewOperationFragment getFragment();
    void setFragment(NewOperationFragment fragment);
}
