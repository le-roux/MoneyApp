package com.le_roux.sylvain.money.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Sylvain LE ROUX on 06/07/2016.
 */
public class AccountOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "accountDatabase";
    public static final String TABLE_NAME = "operationsTable";

    // Possible data types
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String REAL_TYPE = " REAL";
    private static final String COMMA_SEP = ",";

    public AccountOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void createTable() {
        String SQL_CREATE_TABLE =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( " +
                        "_id" + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        OperationContract.Table.COLUMN_NAME_ACCOUNT + TEXT_TYPE + COMMA_SEP +
                        OperationContract.Table.COLUMN_NAME_PAYEE + TEXT_TYPE + COMMA_SEP +
                        OperationContract.Table.COLUMN_NAME_VALUE + REAL_TYPE + COMMA_SEP +
                        OperationContract.Table.COLUMN_NAME_CATEGORY + TEXT_TYPE + COMMA_SEP +
                        OperationContract.Table.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                        OperationContract.Table.COLUMN_NAME_VALIDATED + INTEGER_TYPE + COMMA_SEP +
                        OperationContract.Table.COLUMN_NAME_YEAR + INTEGER_TYPE + COMMA_SEP +
                        OperationContract.Table.COLUMN_NAME_MONTH + INTEGER_TYPE + COMMA_SEP +
                        OperationContract.Table.COLUMN_NAME_DAY + INTEGER_TYPE + COMMA_SEP +
                        OperationContract.Table.COLUMN_NAME_SHARED + INTEGER_TYPE +')';
        this.getWritableDatabase().execSQL(SQL_CREATE_TABLE);
    }

    public Cursor query(String[] columns, String selection, String[] selectionArgs) {
        SQLiteDatabase dbRead = this.getReadableDatabase();
        return dbRead.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
    }

    public long insert(String nullColumn, ContentValues values) {
        SQLiteDatabase dbWrite = this.getWritableDatabase();
        return dbWrite.insert(TABLE_NAME, nullColumn, values);
    }

    public int update(ContentValues values, String whereClause, String[] whereArgs) {
        SQLiteDatabase dbWrite = this.getWritableDatabase();
        return dbWrite.update(TABLE_NAME, values, whereClause, whereArgs);
    }

    public int delete(String whereClause, String[] whereArgs) {
        SQLiteDatabase dbWrite = this.getWritableDatabase();
        return dbWrite.delete(TABLE_NAME, whereClause, whereArgs);
    }
}
