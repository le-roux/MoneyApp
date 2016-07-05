package com.le_roux.sylvain.money.Data;

import com.le_roux.sylvain.money.Utils.Logger;

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
    private double value;
    private String payee;
    private String description;
    private String category;
    private Calendar date;
    private boolean validated;

    // Keys used for storage
    private static final String PAYEE = "operation.payee";
    private static final String VALUE = "operation.value";
    private static final String CATEGORY = "operation.category";
    private static final String YEAR = "operation.date.year";
    private static final String MONTH = "operation.date.month";
    private static final String DAY = "operation.date.day";
    private static final String HOUR = "operation.date.hour";
    private static final String MINUTE = "operation.date.hour";
    private static final String DESCRIPTION = "operation.description";
    private static final String VALIDATED = "operation.validated";

    /*
     *  Constructors
     */
    public Operation () {
        this(0.0, "", "", "");
    }

    public Operation(double value, String payee) {
        this(value, payee, "", "");
    }

    public Operation(String jsonString) {
        this.value = 0.0;
        this.payee = "";
        this.description = "";
        this.category = "";
        this.date = GregorianCalendar.getInstance();
        this.validated = false;
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
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
        } catch (JSONException e) {
            Logger.d("Error when reading an operation string version");
        }
    }

    public Operation(JSONObject jsonObject) {
        this.value = 0.0;
        this.payee = "";
        this.description = "";
        this.category = "";
        this.date = GregorianCalendar.getInstance();
        this.validated = false;
        try {
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
        } catch (JSONException e) {
            Logger.d("Error when reading an operation string version");
        }
    }

    public Operation(double value, String payee, String category, String description) {
        this.value = value;
        this.payee = payee;
        this.category = category;
        this.description = description;
        this.date = GregorianCalendar.getInstance();
        this.validated = false;
    }

    /*
     *  Getters and Setters
     */
    public void setValue(double value) {
        this.value = value;
    }

    public double getValue() {
        return this.value;
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

    /*
     *  Storage methods
     */
    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        try {
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
}
