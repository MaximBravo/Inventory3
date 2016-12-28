package com.example.maximbravo.inventory3.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.maximbravo.inventory3.data.ProductContract.ProductEntry;

/**
 * Created by Maxim Bravo on 12/22/2016.
 */

public class ProductDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "store.db";

    private static final String SQL_CREATE_TABLE =  "CREATE TABLE "
            + ProductEntry.TABLE_NAME + " ("
            + ProductEntry._ID + " INTEGER PRIMARY KEY, "
            + ProductEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, "
            + ProductEntry.COLUMN_PRODUCT_QUANTITY + " INTEGER NOT NULL, "
            + ProductEntry.COLUMN_PRODUCT_PRICE + " INTEGER NOT NULL, "
            + ProductEntry.COLUMN_PRODUCT_EMAIL + " TEXT NOT NULL); ";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ProductEntry.TABLE_NAME;

    public ProductDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int olderVersion, int newerVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
