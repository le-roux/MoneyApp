package com.le_roux.sylvain.money.Utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.ViewGroup;
import android.widget.EditText;

import com.le_roux.sylvain.money.Data.Account;
import com.le_roux.sylvain.money.R;

/**
 * Created by Sylvain LE ROUX on 07/07/2016.
 */
public class NewAccountFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ViewGroup layout = (ViewGroup)getResources().getLayout(R.layout.new_account_fragment);
        final EditText accountNameField = (EditText)layout.findViewById(R.id.name);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(layout)
                .setPositiveButton(R.string.Create, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String accountName = accountNameField.getText().toString();
                        Account account = new Account(accountName);
                        account.setTable(getActivity());
                    }
                });
        return builder.create();
    }
}
