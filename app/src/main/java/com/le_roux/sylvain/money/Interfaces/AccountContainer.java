package com.le_roux.sylvain.money.Interfaces;

import android.content.SharedPreferences;
import android.widget.ListView;

import com.le_roux.sylvain.money.Adapter.OperationAdapter;
import com.le_roux.sylvain.money.Data.Account;

/**
 * Created by sylva on 07/07/2016.
 */
public interface AccountContainer {
    void setAccount(Account account);
    Account getAccount();
    OperationAdapter getOperationAdapter();
    SharedPreferences getSharedPreferences();
}
