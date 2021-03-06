package com.le_roux.sylvain.money.Utils;

import android.provider.BaseColumns;

/**
 * Created by Sylvain LE ROUX on 06/07/2016.
 */
public class OperationContract {
    public OperationContract() {}

    public static abstract class Table implements BaseColumns {
        public static final String COLUMN_NAME_ACCOUNT = "account";
        public static final String COLUMN_NAME_PAYEE = "payee";
        public static final String COLUMN_NAME_VALUE = "value";
        public static final String COLUMN_NAME_CATEGORY = "category";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_VALIDATED = "validated";
        public static final String COLUMN_NAME_YEAR = "year";
        public static final String COLUMN_NAME_MONTH = "month";
        public static final String COLUMN_NAME_DAY = "day";
        public static final String COLUMN_NAME_SHARED = "shared";
    }
}
