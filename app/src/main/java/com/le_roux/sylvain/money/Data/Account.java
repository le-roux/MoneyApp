package com.le_roux.sylvain.money.Data;

import android.content.Context;
import android.content.SharedPreferences;
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
    private int color;
    private String password;
    private SharedPreferences sharedPreferences;
    private AccountOpenHelper databaseHelper;
    private Context context;
    private static ArrayList<String> categoriesList = new ArrayList<>();
    private static ArrayList<String> payeesList = new ArrayList<>();
    private static ArrayList<String> accountsList = new ArrayList<>();

    // Keys used for storage
    private static final String NAME = "account.name";
    private static final String BALANCE = "account.balance";
    private static final String COLOR = "account.color";
    private static final String PASSWORD = "account.password";

    public static final String CURRENT_ACCOUNT = "account.current";
    public static final String CATEGORIES = "account.categories";
    public static final String PAYEES = "account.payees";
    public static final String ACCOUNTS = "account.accounts";
    public static final String ACCOUNT = "account.account";

    /*
     *  Constructors
     */
    public Account(String name, int color, String password, SharedPreferences sharedPreferences, Context context) {
        this.name = name;
        this.balance = 0;
        this.color = color;
        this.password = password;
        this.sharedPreferences = sharedPreferences;
        this.context = context;
        this.setTable(this.context);
    }

    public Account(JSONObject jsonObject, SharedPreferences sharedPreferences, Context context) {
        try {
            this.name = jsonObject.getString(NAME);
            this.balance = jsonObject.getDouble(BALANCE);
            this.color = jsonObject.getInt(COLOR);
            this.password = jsonObject.getString(PASSWORD);
        } catch (JSONException e) {
            Logger.d("Error when creating account from json string");
        }
        this.sharedPreferences = sharedPreferences;
        this.context = context;
        this.setTable(this.context);
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

    public int getColor() {
        return this.color;
    }

    public String getPassword() {
        return this.password;
    }

    public AccountOpenHelper getDatabaseHelper() {
        return this.databaseHelper;
    }

    public static ArrayList<String> getCategoriesList() {
        return categoriesList;
    }

    public static ArrayList<String> getPayeesList() {
        return payeesList;
    }

    public static ArrayList<String> getAccountsList() {
        return accountsList;
    }

    /*
     * Storage methods
     */
    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(NAME, this.name);
            jsonObject.put(BALANCE, this.balance);
            jsonObject.put(COLOR, this.color);
            jsonObject.put(PASSWORD, this.password);
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

    public void save() {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString(this.name, this.toString());
        editor.apply();
    }

    public static void saveCategories(SharedPreferences sharedPreferences) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        JSONArray array = new JSONArray();
        for (String category : categoriesList) {
            array.put(category);
        }
        editor.putString(CATEGORIES, array.toString());
        editor.apply();
    }

    public static void retrieveCategories(SharedPreferences sharedPreferences) {
        try {
            if (sharedPreferences.contains(CATEGORIES)) {
                JSONArray array = new JSONArray(sharedPreferences.getString(CATEGORIES, null));
                categoriesList.clear();
                for (int i = 0; i < array.length(); i++)
                    categoriesList.add(array.getString(i));
            }
        } catch (JSONException e) {
            Logger.d("Impossible to read the categories list");
        }
    }

    public static void savePayees(SharedPreferences sharedPreferences) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        JSONArray array = new JSONArray();
        for (String category : payeesList) {
            array.put(category);
        }
        editor.putString(PAYEES, array.toString());
        editor.apply();
    }

    public static void retrievePayees(SharedPreferences sharedPreferences) {
        try {
            if (sharedPreferences.contains(PAYEES)) {
                JSONArray array = new JSONArray(sharedPreferences.getString(PAYEES, null));
                payeesList.clear();
                for (int i = 0; i < array.length(); i++)
                    payeesList.add(array.getString(i));
            }
        } catch (JSONException e) {
            Logger.d("Impossible to read the payees list");
        }
    }

    public static void saveAccounts(SharedPreferences sharedPreferences) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        JSONArray array = new JSONArray();
        for (String category : accountsList) {
            array.put(category);
        }
        editor.putString(ACCOUNTS, array.toString());
        editor.apply();
    }

    public static void retrieveAccounts(SharedPreferences sharedPreferences) {
        try {
            if (sharedPreferences.contains(ACCOUNTS)) {
                JSONArray array = new JSONArray(sharedPreferences.getString(ACCOUNTS, null));
                accountsList.clear();
                for (int i = 0; i < array.length(); i++)
                    accountsList.add(array.getString(i));
            }
        } catch (JSONException e) {
            Logger.d("Impossible to read the accounts list");
        }
    }

    /*
     *  Other functions
     */

    /**
     * Open or create a table for this account
     * @param context
     */
    private void setTable(Context context) {
        this.databaseHelper = new AccountOpenHelper(context);
        this.databaseHelper.createTable();
    }

    /**
     * Reads all the operations and compute the balance.
     * @return false is the actual balance is different from the one previously saved, true otherwise
     */
    public boolean checkBalance() {
        SQLiteDatabase db = this.databaseHelper.getReadableDatabase();
        double balance = 0.0;
        String[] projection = {OperationContract.Table.COLUMN_NAME_VALUE};
        Cursor c = db.query(false, AccountOpenHelper.TABLE_NAME, projection, null, null, null, null, null, null);
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

    public boolean addOperation(Operation operation) {
        long rowId;
        rowId = this.databaseHelper.insert(null, operation.getContentValues());
        this.balance += operation.getValue();
        this.save();
        return rowId != -1;
    }

    public boolean updateOperation(int id, Operation operation) {
        String where = OperationContract.Table._ID + " LIKE ? ";
        String[] args = {String.valueOf(id)};

        // Get previous value to update the account balance
        String[] columns = {OperationContract.Table.COLUMN_NAME_VALUE};
        Cursor cursor = this.databaseHelper.query(columns, where, args);
        cursor.moveToFirst();
        balance -= cursor.getDouble(cursor.getColumnIndexOrThrow(OperationContract.Table.COLUMN_NAME_VALUE));
        // Update the record
        int nbRows;
        nbRows = this.databaseHelper.update(operation.getContentValues(), where, args);
        balance += operation.getValue();
        this.save();
        return nbRows == 1;
    }

    public void deleteOperation(int id) {
        String where = OperationContract.Table._ID + " LIKE ? ";
        String[] args = {String.valueOf(id)};

        // Get previous value to update the account balance
        String[] columns = {OperationContract.Table.COLUMN_NAME_VALUE};
        Cursor cursor = this.databaseHelper.query(columns, where, args);
        cursor.moveToFirst();
        balance -= cursor.getDouble(cursor.getColumnIndexOrThrow(OperationContract.Table.COLUMN_NAME_VALUE));

        this.databaseHelper.delete(where, args);
        this.save();
    }
}
