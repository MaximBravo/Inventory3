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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maximbravo.inventory3.data.ProductContract.ProductEntry;
import com.example.maximbravo.inventory3.data.ProductDbHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private TextView output;
    private ProductDbHelper mDbHelper;
    private ProductCursorAdapter mCursorAdapter;
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
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                //showDummyProducts();
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("id", -1);
                startActivity(intent);
            }
        });

        //initialize output textView
        output = (TextView) findViewById(R.id.output);

        //initialize dbhelper
        mDbHelper = new ProductDbHelper(getApplicationContext());

        ListView productListView = (ListView) findViewById(R.id.list);



        mCursorAdapter = new ProductCursorAdapter(this, null);
        productListView.setAdapter(mCursorAdapter);

        productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
               Toast.makeText(MainActivity.this, "You clicked on the " + id + " element", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("idandposition", ""+ id + "," + position);

                startActivity(intent);
            }
        });
        showDummyProducts();



    }

    public void insertDummyProduct(){
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        //fake values
        String name = "Jumbo Minnion";
        int quantity = 5;
        double price = 15.45;
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, name);
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, quantity);
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, price);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(ProductEntry.TABLE_NAME, null, values);

        showDummyProducts();
    }
    public void deleteTable(){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete(ProductEntry.TABLE_NAME, null, null);
        showDummyProducts();
    }
    public void showDummyProducts(){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                ProductEntry._ID,
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_QUANTITY,
                ProductEntry.COLUMN_PRODUCT_PRICE
        };

//        // Filter results WHERE "title" = 'My Title'
//        String selection = ProductEntry.COLUMN_NAME_TITLE + " = ?";
//        String[] selectionArgs = { "My Title" };

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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
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
