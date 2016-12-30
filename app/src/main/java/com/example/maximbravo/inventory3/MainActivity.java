package com.example.maximbravo.inventory3;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maximbravo.inventory3.data.ProductContract;
import com.example.maximbravo.inventory3.data.ProductContract.ProductEntry;
import com.example.maximbravo.inventory3.data.ProductDbHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private TextView output;
    private static ProductDbHelper mDbHelper;
    private static ProductCursorAdapter mCursorAdapter;
    private static int theId;
    private TextView saleButton;
    private static int thePosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("id", -1);
                startActivity(intent);
            }
        });

        output = (TextView) findViewById(R.id.output);


        mDbHelper = new ProductDbHelper(getApplicationContext());


        ListView productListView = (ListView) findViewById(R.id.list);

        View emptyView = findViewById(R.id.empty_view);
        productListView.setEmptyView(emptyView);

        saleButton = (TextView) findViewById(R.id.sale_button);
        mCursorAdapter = new ProductCursorAdapter(this, null);
        productListView.setAdapter(mCursorAdapter);

        productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "You clicked on the " + id + " element", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("idandposition", "" + id + "," + position);
                startActivity(intent);
            }
        });
        showDummyProducts();



    }
    public static void updateQuantity(int pos) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        SQLiteDatabase dbr = mDbHelper.getReadableDatabase();
        String[] projection = {
                ProductEntry._ID,
                ProductEntry.COLUMN_PRODUCT_QUANTITY
        };
        Cursor cursor = dbr.query(
                ProductEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );
        cursor.moveToFirst();
        cursor.move(pos);
        int itemquantity = cursor.getInt(cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_PRODUCT_QUANTITY));
        theId = cursor.getInt(cursor.getColumnIndexOrThrow(ProductEntry._ID));
        cursor.close();

        if(itemquantity != 0) {
            ContentValues values = new ContentValues();
            values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, itemquantity-1);
            String selection = ProductEntry._ID + " = ?";
            String[] selectionArgs = {"" + (theId)};
            int count = db.update(
                    ProductEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);
        }
        showDummyProducts();
    }
    public void insertDummyProduct(){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String name = "Jumbo Minnion";
        int quantity = 5;
        double price = 15.45;

        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, name);
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, quantity);
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, price);

        showDummyProducts();
    }
    public void deleteTable(){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete(ProductEntry.TABLE_NAME, null, null);
        showDummyProducts();
    }
    public static void showDummyProducts(){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                ProductEntry._ID,
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_QUANTITY,
                ProductEntry.COLUMN_PRODUCT_PRICE
        };

        Cursor cursor = db.query(
                ProductEntry.TABLE_NAME,                  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // The sort order
        );

        mCursorAdapter.swapCursor(cursor);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add_dummy_product) {
            Toast.makeText(this, "You clicked the Add Product menu item.", Toast.LENGTH_LONG).show();
            insertDummyProduct();
            return true;
        }
        if(id == R.id.action_delete_all_products){
            deleteTable();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
