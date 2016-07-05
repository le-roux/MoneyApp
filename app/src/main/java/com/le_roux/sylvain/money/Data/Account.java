package com.le_roux.sylvain.money.Data;

import com.le_roux.sylvain.money.Utils.Logger;

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
    private ArrayList<Operation> operationsList;
    public static ArrayList<String> categoriesList = new ArrayList<>();

    // Keys used for storage
    private static final String NAME = "account.name";
    private static final String BALANCE = "account.balance";
    private static final String OPERATIONS_LIST = "account.operationsList";

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
}
