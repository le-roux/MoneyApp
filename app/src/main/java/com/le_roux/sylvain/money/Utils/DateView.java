package com.le_roux.sylvain.money.Utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Sylvain LE ROUX on 07/07/2016.
 */

/**
 * Allows to display a date in format dd/mm/yyyy
 */
public class DateView extends TextView {

    /*
     *  Attributes
     */
    private int day;
    private int month;
    private int year;

    /*
     *  Constructors
     */
    public DateView(Context context) {
        super(context);
        this.day = 1;
        this.month = 1;
        this.year = 1970;
    }

    public DateView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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

}
