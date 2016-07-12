package com.le_roux.sylvain.money.Utils;

import android.content.Context;
import android.database.Cursor;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Sylvain LE ROUX on 11/07/2016.
 */
public class OperationListView extends ListView {

    private Cursor cursor;

    public OperationListView(Context context) {
        super(context);
    }

    public OperationListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OperationListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    public Cursor getCursor() {
        return this.cursor;
    }
}
