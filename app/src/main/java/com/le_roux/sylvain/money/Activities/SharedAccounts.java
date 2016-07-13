package com.le_roux.sylvain.money.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.le_roux.sylvain.money.Dialog.NewOperationFragment;
import com.le_roux.sylvain.money.Dialog.NewSharedOperationFragment;
import com.le_roux.sylvain.money.Interfaces.DateViewContainer;
import com.le_roux.sylvain.money.Interfaces.Updatable;
import com.le_roux.sylvain.money.R;
import com.le_roux.sylvain.money.Utils.DateView;

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
    }

    @Override
    public void update() {

    }

    @Override
    public DateView getDateView() {
        return fragment.getDateView();
    }
}
