package com.le_roux.sylvain.money.Data;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.le_roux.sylvain.money.Utils.Logger;
import com.le_roux.sylvain.money.Utils.OperationContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Sylvain LE ROUX on 05/07/2016.
 */

/**
 * This class represents an operation on an account : either a deposit or a withdrawal.
 * If it's a deposit, value > 0, otherwise value < 0;
 */
public class Operation {
    private String accountName;
    private double value;
    private String payee;
    private String description;
    private String category;
    private Calendar date;
    private boolean validated;
    private boolean shared;
    private long id;

    // Keys used for storage
    public static final String ACCOUNT = "operation.account";
    public static final String PAYEE = "operation.payee";
    public static final String VALUE = "operation.value";
    public static final String CATEGORY = "operation.category";
    public static final String YEAR = "operation.date.year";
    public static final String MONTH = "operation.date.month";
    public static final String DAY = "operation.date.day";
    public static final String HOUR = "operation.date.hour";
    public static final String MINUTE = "operation.date.hour";
    public static final String DESCRIPTION = "operation.description";
    public static final String VALIDATED = "operation.validated";
    public static final String SHARED = "operation.shared";

    /*
     *  Constructors
     */
    public Operation () {
        this(0.0, "", "", "", "");
    }

    public Operation(double value, String payee) {
        this(value, payee, "", "", "");
    }

    public Operation(String jsonString) {
        this.accountName = "";
        this.value = 0.0;
        this.payee = "";
        this.description = "";
        this.category = "";
        this.date = GregorianCalendar.getInstance();
        this.validated = false;
        this.shared = false;
        this.id = -1;
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            this.accountName = jsonObject.getString(ACCOUNT);
            this.value = jsonObject.getDouble(VALUE);
            this.payee = jsonObject.getString(PAYEE);
            this.description = jsonObject.getString(DESCRIPTION);
            this.category = jsonObject.getString(CATEGORY);
            this.date.set(Calendar.YEAR, jsonObject.getInt(YEAR));
            this.date.set(Calendar.MONTH, jsonObject.getInt(MONTH));
            this.date.set(Calendar.DAY_OF_MONTH, jsonObject.getInt(DAY));
            this.date.set(Calendar.HOUR_OF_DAY, jsonObject.getInt(HOUR));
            this.date.set(Calendar.MINUTE, jsonObject.getInt(MINUTE));
            this.validated = jsonObject.getBoolean(VALIDATED);
            this.shared = jsonObject.getBoolean(SHARED);
        } catch (JSONException e) {
            Logger.d("Error when reading an operation string version");
        }
    }

    public Operation(JSONObject jsonObject) {
        this.accountName = "";
        this.value = 0.0;
        this.payee = "";
        this.description = "";
        this.category = "";
        this.date = GregorianCalendar.getInstance();
        this.validated = false;
        this.shared = false;
        this.id = -1;
        try {
            this.accountName = jsonObject.getString(ACCOUNT);
            this.value = jsonObject.getDouble(VALUE);
            this.payee = jsonObject.getString(PAYEE);
            this.description = jsonObject.getString(DESCRIPTION);
            this.category = jsonObject.getString(CATEGORY);
            this.date.set(Calendar.YEAR, jsonObject.getInt(YEAR));
            this.date.set(Calendar.MONTH, jsonObject.getInt(MONTH));
            this.date.set(Calendar.DAY_OF_MONTH, jsonObject.getInt(DAY));
            this.date.set(Calendar.HOUR_OF_DAY, jsonObject.getInt(HOUR));
            this.date.set(Calendar.MINUTE, jsonObject.getInt(MINUTE));
            this.validated = jsonObject.getBoolean(VALIDATED);
            this.shared = jsonObject.getBoolean(SHARED);
        } catch (JSONException e) {
            Logger.d("Error when reading an operation string version");
        }
    }

    public Operation(double value, String payee, String category, String description, String accountName) {
        this.value = value;
        this.payee = payee;
        this.category = category;
        this.description = description;
        this.date = GregorianCalendar.getInstance();
        this.validated = false;
        this.shared = false;
        this.id = -1;
        this.accountName = accountName;
    }

    /*
     *  Getters and Setters
     */

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountName() {
        return this.accountName;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getValue() {
        return this.value;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public String getPayee() {
        return this.payee;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return this.category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public Calendar getDate() {
        return this.date;
    }

    public void toggleValidation() {
        this.validated = !this.validated;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }

    public boolean isShared() {
        return this.shared;
    }

    /*
     *  Storage methods
     */
    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ACCOUNT, this.accountName);
            jsonObject.put(PAYEE, this.payee);
            jsonObject.put(VALUE, this.value);
            jsonObject.put(CATEGORY, this.category);
            jsonObject.put(YEAR, this.date.get(Calendar.YEAR));
            jsonObject.put(MONTH, this.date.get(Calendar.MONTH));
            jsonObject.put(DAY, this.date.get(Calendar.DAY_OF_MONTH));
            jsonObject.put(HOUR, this.date.get(Calendar.HOUR_OF_DAY));
            jsonObject.put(MINUTE, this.date.get(Calendar.MINUTE));
            jsonObject.put(DESCRIPTION, this.description);
            jsonObject.put(VALIDATED, this.validated);
            jsonObject.put(SHARED, this.shared);
            return jsonObject;
        } catch(JSONException e) {
            Logger.d("Error when converting operation to string");
        }
        return null;
    }

    public String toString() {
        JSONObject jsonObject = this.toJSONObject();
        if (jsonObject == null)
            return null;
        return jsonObject.toString();
    }

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(OperationContract.Table.COLUMN_NAME_ACCOUNT, this.accountName);
        values.put(OperationContract.Table.COLUMN_NAME_PAYEE, this.payee);
        values.put(OperationContract.Table.COLUMN_NAME_VALUE, this.value);
        values.put(OperationContract.Table.COLUMN_NAME_CATEGORY, this.category);
        values.put(OperationContract.Table.COLUMN_NAME_DESCRIPTION, this.description);
        values.put(OperationContract.Table.COLUMN_NAME_VALIDATED, this.validated);
        values.put(OperationContract.Table.COLUMN_NAME_SHARED, this.shared);
        values.put(OperationContract.Table.COLUMN_NAME_DAY, this.date.get(Calendar.DAY_OF_MONTH));
        values.put(OperationContract.Table.COLUMN_NAME_MONTH, this.date.get(Calendar.MONTH));
        values.put(OperationContract.Table.COLUMN_NAME_YEAR, this.date.get(Calendar.YEAR));
        return values;
    }
}
