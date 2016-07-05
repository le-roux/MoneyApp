package com.le_roux.sylvain.money.Data;

import com.le_roux.sylvain.money.Utils.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Sylvain LE ROUX on 05/07/2016.
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
    private static final String DATE = "operation.date";
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
            //this.date = jsonObject.get(DATE);
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
     *  Other methods
     */
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(PAYEE, this.payee);
            jsonObject.put(VALUE, this.value);
            jsonObject.put(CATEGORY, this.category);
            jsonObject.put(DATE, this.date);
            jsonObject.put(DESCRIPTION, this.description);
            jsonObject.put(VALIDATED, this.validated);
            return jsonObject.toString();
        } catch(JSONException e) {
            Logger.d("Error when converting operation to string");
        }
        return null;
    }
}
