package com.le_roux.sylvain.money.Utils;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Sylvain LE ROUX on 07/07/2016.
 */

/**
 * Allows to display a date in format dd/mm/yyyy
 */
public class DateView extends TextView implements DatePickerDialog.OnDateSetListener{

    /*
     *  Attributes
     */
    private int day;
    private int month;
    private int year;

    /*
     *  Keys related with date
     */
    public static final String DAY = "date.day";
    public static final String MONTH = "date.month";
    public static final String YEAR = "date.year";

    /*
     *  Constructors
     */
    public DateView(Context context) {
        super(context);
        Calendar calendar = GregorianCalendar.getInstance();
        this.day = calendar.get(Calendar.DAY_OF_MONTH);
        this.month = calendar.get(Calendar.MONTH) + 1;
        this.year = calendar.get(Calendar.YEAR);
        update();
    }

    public DateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Calendar calendar = GregorianCalendar.getInstance();
        this.day = calendar.get(Calendar.DAY_OF_MONTH);
        this.month = calendar.get(Calendar.MONTH) + 1;
        this.year = calendar.get(Calendar.YEAR);
        update();
    }

    public DateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Calendar calendar = GregorianCalendar.getInstance();
        this.day = calendar.get(Calendar.DAY_OF_MONTH);
        this.month = calendar.get(Calendar.MONTH) + 1;
        this.year = calendar.get(Calendar.YEAR);
        update();
    }

    /*
     *  Getters and Setters
     */
    public void setDay(int day) {
        this.day = day;
        this.update();
    }

    public int getDay() {
        return this.day;
    }

    public void setMonth(int month) {
        this.month = month;
        this.update();
    }

    public int getMonth() {
        return this.month;
    }

    public void setYear(int year) {
        this.year = year;
        this.update();
    }

    public int getYear() {
        return this.year;
    }

    /*
     *  Other functions
     */
    public void update() {
        StringBuilder builder = new StringBuilder();
        if (this.day < 10)
            builder.append('0');
        builder.append(String.valueOf(this.day))
                .append('/');
        if (this.month < 10)
            builder.append('0');
        builder.append(String.valueOf(this.month))
                .append('/')
                .append(this.year);
        this.setText(builder.toString());
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        this.setYear(year);
        this.setMonth(monthOfYear + 1);
        this.setDay(dayOfMonth);
    }
}
