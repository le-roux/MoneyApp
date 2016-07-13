package com.le_roux.sylvain.money.Activities;

import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.le_roux.sylvain.money.Data.Account;
import com.le_roux.sylvain.money.Data.Operation;
import com.le_roux.sylvain.money.Dialog.NewOperationFragment;
import com.le_roux.sylvain.money.Dialog.NewSharedOperationFragment;
import com.le_roux.sylvain.money.Interfaces.DateViewContainer;
import com.le_roux.sylvain.money.Interfaces.Updatable;
import com.le_roux.sylvain.money.R;
import com.le_roux.sylvain.money.Utils.DateView;
import com.le_roux.sylvain.money.Utils.OperationContract;
import com.le_roux.sylvain.money.Utils.OperationListController;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class SharedAccounts extends AppCompatActivity implements Updatable, DateViewContainer {

    private NewOperationFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_accounts);

        Button addOperationButton = (Button)findViewById(R.id.addOperation);
        if (addOperationButton != null) {
            addOperationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragment = new NewSharedOperationFragment();
                    fragment.show(getSupportFragmentManager(), "New shared operation");
                }
            });
        }

        /*ListView operationsListView = (ListView)findViewById(R.id.operationsListView);
        OperationListController controller = new OperationListController(null, operationsListView, this);
        ArrayList<Operation> operationsList = new ArrayList<>();
        Account.retrieveAccounts(PreferenceManager.getDefaultSharedPreferences(this));
        for (String accountName : Account.getAccountsList()) {
            Account account = new Account(accountName, PreferenceManager.getDefaultSharedPreferences(this), this);
            controller.setAccount(account);
            Cursor cursor = controller.getOperationsForYear(GregorianCalendar.getInstance().get(Calendar.YEAR));
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int shared = cursor.getInt(cursor.getColumnIndexOrThrow(OperationContract.Table.COLUMN_NAME_SHARED));
                if (shared == 1) {
                    double value = cursor.getDouble(cursor.getColumnIndexOrThrow(OperationContract.Table.COLUMN_NAME_VALUE));
                    String payee = cursor.getString(cursor.getColumnIndexOrThrow(OperationContract.Table.COLUMN_NAME_PAYEE));
                    String category = cursor.getString(cursor.getColumnIndexOrThrow(OperationContract.Table.COLUMN_NAME_CATEGORY));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow(OperationContract.Table.COLUMN_NAME_DESCRIPTION));
                    Operation operation = new Operation(value, payee, category, description);
                    operation.setShared(true);
                    operationsList.add(operation);
                }
            }
        }
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.item_shared_operation, operationsList);
        operationsListView.setAdapter(adapter);*/
    }

    @Override
    public void update() {

    }

    @Override
    public DateView getDateView() {
        return fragment.getDateView();
    }
}
