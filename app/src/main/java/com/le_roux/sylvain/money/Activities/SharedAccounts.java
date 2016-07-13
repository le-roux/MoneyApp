package com.le_roux.sylvain.money.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.le_roux.sylvain.money.Dialog.NewOperationFragment;
import com.le_roux.sylvain.money.Dialog.NewSharedOperationFragment;
import com.le_roux.sylvain.money.Interfaces.DateViewContainer;
import com.le_roux.sylvain.money.Interfaces.FragmentContainer;
import com.le_roux.sylvain.money.Interfaces.Updatable;
import com.le_roux.sylvain.money.R;
import com.le_roux.sylvain.money.Utils.DateView;
import com.le_roux.sylvain.money.Utils.SharedOperationsListController;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class SharedAccounts extends AppCompatActivity implements Updatable, DateViewContainer, FragmentContainer {

    private NewOperationFragment fragment;
    private SharedOperationsListController controller;

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
        this.controller = new SharedOperationsListController(operationsListView, this);
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View header = inflater.inflate(R.layout.header_shared_operation, null);
        operationsListView.addHeaderView(header);
        this.controller.displayOperationsForYear(GregorianCalendar.getInstance().get(Calendar.YEAR));

    }

    @Override
    public void update() {
        this.controller.displayOperationsForYear(GregorianCalendar.getInstance().get(Calendar.YEAR));
    }

    @Override
    public DateView getDateView() {
        return fragment.getDateView();
    }

    @Override
    public NewOperationFragment getFragment() {
        return this.fragment;
    }

    @Override
    public void setFragment(NewOperationFragment fragment) {
        this.fragment = fragment;
    }
}
