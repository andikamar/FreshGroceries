package com.dika.freshgroceries.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dika.freshgroceries.model.Constants;
import com.dika.freshgroceries.model.Grocery;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private Context ctx;

    public DatabaseHandler(Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
        this.ctx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_GROCERY_TABLE = "CREATE TABLE " + Constants.TABLE_NAME + "("
                + Constants.KEY_ID + " INTEGER PRIMARY KEY," + Constants.KEY_GROCERY_ITEM + " TEXT,"
                + Constants.KEY_QTY_NUMBER + " TEXT,"
                + Constants.KEY_DATE_NAME + " LONG," + Constants.KEY_PRICE
                + " TEXT, "+ Constants.KEY_RATING + " INTEGER, "+ Constants.KEY_IMAGE + " INTEGER);";

        db.execSQL(CREATE_GROCERY_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);

        onCreate(db);

    }

    //Add Grocery
    public void addGrocery(Grocery grocery) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_GROCERY_ITEM, grocery.getName());
        values.put(Constants.KEY_QTY_NUMBER, grocery.getQuantity());
        values.put(Constants.KEY_DATE_NAME, Constants.getToday());
        values.put(Constants.KEY_PRICE, grocery.getPrice());
        values.put(Constants.KEY_RATING, grocery.getRating());
        values.put(Constants.KEY_IMAGE, grocery.getImage());

        //Insert the row
        db.insert(Constants.TABLE_NAME, null, values);
    }



    //Get all Groceries
    public List<Grocery> getAllGroceries() {
        SQLiteDatabase db = this.getReadableDatabase();

        List<Grocery> groceryList = new ArrayList<>();

        Cursor cursor = db.query(Constants.TABLE_NAME, new String[]{
                Constants.KEY_ID, Constants.KEY_GROCERY_ITEM, Constants.KEY_QTY_NUMBER,
                Constants.KEY_DATE_NAME, Constants.KEY_PRICE, Constants.KEY_RATING, Constants.KEY_IMAGE}, null, null, null, null, Constants.KEY_DATE_NAME + " DESC");

        if (cursor.moveToFirst()) {
            do {
                Grocery grocery = new Grocery();
                grocery.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
                grocery.setName(cursor.getString(cursor.getColumnIndex(Constants.KEY_GROCERY_ITEM)));
                grocery.setQuantity(cursor.getInt(cursor.getColumnIndex(Constants.KEY_QTY_NUMBER)));
                grocery.setPrice(cursor.getDouble(cursor.getColumnIndex(Constants.KEY_PRICE)));
                grocery.setDateItemAdded(cursor.getString(cursor.getColumnIndex(Constants.KEY_DATE_NAME)));
                grocery.setRating(cursor.getInt(cursor.getColumnIndex(Constants.KEY_RATING)));
                grocery.setImage(cursor.getInt(cursor.getColumnIndex(Constants.KEY_IMAGE)));

                // Add to the groceryList
                groceryList.add(grocery);

            } while (cursor.moveToNext());
        }

        return groceryList;
    }


    //Updated Grocery
    public void updateGrocery(Grocery grocery) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_GROCERY_ITEM, grocery.getName());
        values.put(Constants.KEY_QTY_NUMBER, grocery.getQuantity());
        values.put(Constants.KEY_DATE_NAME, System.currentTimeMillis());//get system time
        values.put(Constants.KEY_PRICE, grocery.getPrice());
        values.put(Constants.KEY_RATING, grocery.getRating());
        values.put(Constants.KEY_IMAGE, grocery.getImage());


        //update row
        db.update(Constants.TABLE_NAME, values, Constants.KEY_ID + "=?", new String[]{String.valueOf(grocery.getId())});
    }


    //Delete Grocery
    public void deleteGrocery(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Constants.TABLE_NAME, Constants.KEY_ID + " = ?",
                new String[]{String.valueOf(id)});

        db.close();

    }
}
