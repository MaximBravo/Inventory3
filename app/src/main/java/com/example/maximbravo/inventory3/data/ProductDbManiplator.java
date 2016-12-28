package com.example.maximbravo.inventory3.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Maxim Bravo on 12/24/2016.
 */

public class ProductDbManiplator {
    private ProductDbHelper mDbHelper;
    private SQLiteDatabase writableDb;
    private SQLiteDatabase readableDb;
    public ProductDbManiplator(Context context){
        mDbHelper = new ProductDbHelper(context);
        writableDb = mDbHelper.getWritableDatabase();
        readableDb = mDbHelper.getReadableDatabase();
    }
    public void insertProduct(ContentValues values){
        long newRowId = writableDb.insert(ProductContract.ProductEntry.TABLE_NAME, null, values);
    }

    public void insertProductWithValues(String name, int quantity, double price, String email){
        ContentValues values = new ContentValues();
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME, name);
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, quantity);
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE, price);
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_EMAIL, email);
        long newRowId = writableDb.insert(ProductContract.ProductEntry.TABLE_NAME, null, values);
    }

    public void deleteProductAt(int position){
        // Define 'where' part of query.
        String selection = ProductContract.ProductEntry._ID + " = ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { ""+position };
        // Issue SQL statement.
        writableDb.delete(ProductContract.ProductEntry.TABLE_NAME, selection, selectionArgs);
    }
}
