package com.le_roux.sylvain.money.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Sylvain LE ROUX on 06/07/2016.
 */
public class AccountOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "accountDatabase";

    // Possible data types
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String REAL_TYPE = " REAL";
    private static final String COMMA_SEP = ",";

    private String name;

    public AccountOpenHelper(Context context, String name) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.name = name;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /*String SQL_CREATE_TABLE =
                "CREATE TABLE " + this.name + " ( " +
                        "_id" + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        OperationContract.Table.COLUMN_NAME_PAYEE + TEXT_TYPE + COMMA_SEP +
                        OperationContract.Table.COLUMN_NAME_VALUE + REAL_TYPE + COMMA_SEP +
                        OperationContract.Table.COLUMN_NAME_CATEGORY + TEXT_TYPE + COMMA_SEP +
                        OperationContract.Table.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                        OperationContract.Table.COLUMN_NAME_VALIDATED + INTEGER_TYPE + COMMA_SEP +
                        OperationContract.Table.COLUMN_NAME_YEAR + INTEGER_TYPE + COMMA_SEP +
                        OperationContract.Table.COLUMN_NAME_MONTH + INTEGER_TYPE + COMMA_SEP +
                        OperationContract.Table.COLUMN_NAME_DAY + INTEGER_TYPE + ')';
        db.execSQL(SQL_CREATE_TABLE);*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void createTable(String name) {
        String SQL_CREATE_TABLE =
                "CREATE TABLE " + name + " ( " +
                        "_id" + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        OperationContract.Table.COLUMN_NAME_PAYEE + TEXT_TYPE + COMMA_SEP +
                        OperationContract.Table.COLUMN_NAME_VALUE + REAL_TYPE + COMMA_SEP +
                        OperationContract.Table.COLUMN_NAME_CATEGORY + TEXT_TYPE + COMMA_SEP +
                        OperationContract.Table.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                        OperationContract.Table.COLUMN_NAME_VALIDATED + INTEGER_TYPE + COMMA_SEP +
                        OperationContract.Table.COLUMN_NAME_YEAR + INTEGER_TYPE + COMMA_SEP +
                        OperationContract.Table.COLUMN_NAME_MONTH + INTEGER_TYPE + COMMA_SEP +
                        OperationContract.Table.COLUMN_NAME_DAY + INTEGER_TYPE + ')';
        this.getWritableDatabase().execSQL(SQL_CREATE_TABLE);
    }
}
