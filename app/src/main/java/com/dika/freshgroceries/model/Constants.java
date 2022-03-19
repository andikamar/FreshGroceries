package com.dika.freshgroceries.model;

import android.text.format.DateFormat;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Calendar;
import java.util.Date;

public class Constants {

    // DB
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "groceryListDB";
    public static final String TABLE_NAME = "groceryTBL";

    //Table columns
    public static final String KEY_ID = "id";
    public static final String KEY_GROCERY_ITEM = "grocery_item";
    public static final String KEY_QTY_NUMBER = "quantity_number";
    public static final String KEY_DATE_NAME = "date_added";
    public static final String KEY_PRICE = "grocery_price";
    public static final String KEY_RATING = "grocery_rating";
    public static final String KEY_IMAGE = "grocery_image";


    // gettoday
    public static String getToday() {
        Date date = Calendar.getInstance().getTime();
        return (String) DateFormat.format("d MMMM yyyy", date);
    }

    //format rupiah
    public static String formatRupiah(long rupiah) {
        DecimalFormat mataUangIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        mataUangIndonesia.setMinimumFractionDigits(0);
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setGroupingSeparator('.');
        mataUangIndonesia.setDecimalFormatSymbols(formatRp);
        return mataUangIndonesia.format(rupiah);
    }


}

