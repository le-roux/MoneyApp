package com.le_roux.sylvain.money.Utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Sylvain LE ROUX on 05/07/2016.
 */
public class PriceView extends TextView {

    // Name of the property to display
    private String name;

    // Value of the property
    private double value;

    /*
     *  Constructors
     */
    public PriceView(Context context) {
        super(context);
    }

    public PriceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PriceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /*
     *  Getters and setters
     */
    public void setName(String name) {
        this.name = name;
        this.update();
    }

    public String getName() {
        return this.name;
    }

    public void setValue(double value) {
        this.value = value;
        this.update();
    }

    public double getValue() {
        return this.value;
    }

    /*
     *  Other methods
     */
    public void update() {
        String text = this.name
                + " : "
                + String.valueOf(this.value);
        setText(text);
    }

}
