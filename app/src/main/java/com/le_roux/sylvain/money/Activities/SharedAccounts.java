package com.le_roux.sylvain.money.Activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.le_roux.sylvain.money.Dialog.NewOperationFragment;
import com.le_roux.sylvain.money.Dialog.NewSharedOperationFragment;
import com.le_roux.sylvain.money.Fragment.MonthBalancingFragment;
import com.le_roux.sylvain.money.Fragment.YearBalancingFragment;
import com.le_roux.sylvain.money.Interfaces.DateViewContainer;
import com.le_roux.sylvain.money.Interfaces.FragmentContainer;
import com.le_roux.sylvain.money.Interfaces.Updatable;
import com.le_roux.sylvain.money.R;
import com.le_roux.sylvain.money.Utils.DateView;
import com.le_roux.sylvain.money.Utils.Logger;
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

        RadioGroup balancingTimeGroup = (RadioGroup)findViewById(R.id.TimeSpanGroup);
        final FragmentManager manager = getFragmentManager();
        if (balancingTimeGroup != null) {
            balancingTimeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId == R.id.monthButton) {
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.replace(R.id.balancingLayout, new MonthBalancingFragment());
                        transaction.addToBackStack(null);
                        transaction.commit();
                    } else {
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.replace(R.id.balancingLayout, new YearBalancingFragment());
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                }
            });
        }
        RadioButton monthButton = (RadioButton)findViewById(R.id.monthButton);
        RadioButton yearButton = (RadioButton)findViewById(R.id.yearButton);

        if (monthButton != null)
            monthButton.setChecked(true);
        RelativeLayout balancingLayout = (RelativeLayout)findViewById(R.id.balancingLayout);
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
