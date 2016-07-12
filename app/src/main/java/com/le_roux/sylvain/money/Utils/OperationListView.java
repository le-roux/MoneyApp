package com.le_roux.sylvain.money.Utils;

import android.content.Context;
import android.database.Cursor;
import android.util.AttributeSet;
import android.widget.ListView;

import com.le_roux.sylvain.money.Data.Account;

/**
 * Created by Sylvain LE ROUX on 11/07/2016.
 */
public class OperationListView extends ListView {

    /*
     *  Constructors
     */
    public OperationListView(Context context) {
        super(context);
    }

    public OperationListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OperationListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /*
     *  Other functions
     */
    public void update() {

    }
}
