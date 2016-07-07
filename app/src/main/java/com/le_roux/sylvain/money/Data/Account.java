package com.le_roux.sylvain.money.Data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.le_roux.sylvain.money.Utils.AccountOpenHelper;
import com.le_roux.sylvain.money.Utils.Logger;
import com.le_roux.sylvain.money.Utils.OperationContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sylvain LE ROUX on 05/07/2016.
 */
public class Account {

    private String name;
    private double balance;
    private AccountOpenHelper databaseHelper;
    private ArrayList<Operation> operationsList;
    // TODO stores it in a database
    public static ArrayList<String> categoriesList = new ArrayList<>();

    // Keys used for storage
    private static final String NAME = "account.name";
    private static final String BALANCE = "account.balance";
    private static final String OPERATIONS_LIST = "account.operationsList";

    public static final String CURRENT_ACCOUNT = "account.current";

    /*
     *  Constructors
     */
    public Account(String name) {
        this.name = name;
        this.balance = 0;
        this.operationsList = new ArrayList<>();
    }

    public Account(JSONObject jsonObject) {
        this.operationsList = new ArrayList<>();
        try {
            this.name = jsonObject.getString(NAME);
            this.balance = jsonObject.getDouble(BALANCE);
            JSONArray operations = jsonObject.getJSONArray(OPERATIONS_LIST);
            for (int i = 0; i < operations.length(); i++) {
                Operation operation = new Operation(operations.getJSONObject(i));
                this.operationsList.add(operation);
            }
        } catch (JSONException e) {
            Logger.d("Error when creating account from json string");
        }
    }

    /*
     * Getters and Setters
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public double getBalance() {
        return this.balance;
    }

    public boolean addOperation(Operation operation) {
        //this.operationsList.add(operation);
        SQLiteDatabase dbWrite = this.databaseHelper.getWritableDatabase();
        long rowId;
        rowId = dbWrite.insert(this.name, null, operation.getContentValues());
        return rowId != -1;
    }

    public void deleteOperation(int index) {
        this.operationsList.remove(index);
    }

    /*
     * Storage methods
     */
    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(NAME, this.name);
            jsonObject.put(BALANCE, this.balance);
            JSONArray jsonArray = new JSONArray();
            for (Operation operation : this.operationsList)
                jsonArray.put(operation.toJSONObject());
            jsonObject.put(OPERATIONS_LIST, jsonArray);
            return jsonObject;
        } catch (JSONException e) {
            Logger.d("Error when converting account to JSONObject");
        }
        return null;
    }

    public String toString() {
        JSONObject jsonObject = this.toJSONObject();
        if (jsonObject == null)
            return null;
        return jsonObject.toString();
    }

    /*
     *  Other functions
     */

    /**
     * Open or create a table for this account
     * @param context
     */
    public void setTable(Context context) {
        this.databaseHelper = new AccountOpenHelper(context, this.name);
    }

    /**
     * Reads all the operations and compute the balance.
     * @return false is the actual balance is different from the one previously saved, true otherwise
     */
    public boolean checkBalance() {
        SQLiteDatabase db = this.databaseHelper.getReadableDatabase();
        double balance = 0.0;
        String[] projection = {OperationContract.Table.COLUMN_NAME_VALUE};
        Cursor c = db.query(false, this.name, projection, null, null, null, null, null, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            balance += c.getDouble(c.getColumnIndexOrThrow(OperationContract.Table.COLUMN_NAME_VALUE));
            c.moveToNext();
        }
        if (this.balance != balance) {
            this.balance = balance;
            return false;
        }
        return true;
    }
}
