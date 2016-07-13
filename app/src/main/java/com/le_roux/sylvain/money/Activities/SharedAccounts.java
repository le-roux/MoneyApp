package com.le_roux.sylvain.money.Activities;

import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
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

        ListView operationsListView = (ListView)findViewById(R.id.operationsListView);
        OperationListController controller = new OperationListController(null, operationsListView, this);
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View header = inflater.inflate(R.layout.header_shared_operation, null);
        operationsListView.addHeaderView(header);
        controller.displayOperationsForYear(GregorianCalendar.getInstance().get(Calendar.YEAR));

    }

    @Override
    public void update() {

    }

    @Override
    public DateView getDateView() {
        return fragment.getDateView();
    }
}
