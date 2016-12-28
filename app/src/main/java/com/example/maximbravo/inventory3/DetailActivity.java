package com.example.maximbravo.inventory3;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.maximbravo.inventory3.data.ProductContract;
import com.example.maximbravo.inventory3.data.ProductContract.ProductEntry;
import com.example.maximbravo.inventory3.data.ProductDbHelper;
import com.example.maximbravo.inventory3.data.ProductDbManiplator;

/**
 * Created by Maxim Bravo on 12/24/2016.
 */

public class DetailActivity extends AppCompatActivity {
    private EditText nameEditText;
    private EditText quantityEditText;
    private EditText priceEditText;
    private EditText emailEditText;

    private String name;
    private int quantity;
    private double price;
    private String email;

    private Button incrementButton;
    private Button decrementButton;
    private int position;
    private boolean editmode = false;
    private ProductDbHelper mDbHelper;
    private int id;
    private ProductDbManiplator mDbManipulator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        nameEditText = (EditText) findViewById(R.id.edit_product_name);
        quantityEditText = (EditText) findViewById(R.id.edit_product_quantity);
        priceEditText = (EditText) findViewById(R.id.edit_product_price);
        emailEditText = (EditText) findViewById(R.id.edit_supplier);

        quantity = Integer.parseInt(quantityEditText.getText().toString());

        incrementButton = (Button) findViewById(R.id.increment_button);
        decrementButton = (Button) findViewById(R.id.decrement_button);

        mDbHelper = new ProductDbHelper(this);
        mDbManipulator = new ProductDbManiplator(this);

        Intent intent = getIntent();
        String idandposition = intent.getStringExtra("idandposition");
        if(idandposition != null) {
            String[] idcommapostion = idandposition.split(",");
            id = Integer.parseInt(idcommapostion[0]);
            position = Integer.parseInt(idcommapostion[1]);

            editmode = true;
            populatePageWith(id);

        }

    }

    public void populatePageWith(int id){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

// Define a projection that specifies which columns from the database
// you will actually use after this query.
        String[] projection = {
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductEntry.COLUMN_PRODUCT_QUANTITY,
                ProductEntry.COLUMN_PRODUCT_EMAIL
        };

// Filter results WHERE "title" = 'My Title'
        String selection = ProductEntry._ID + " = ?";
        String[] selectionArgs = { "" + id };



        Cursor cursor = db.query(
                ProductEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );
        cursor.moveToFirst();
        cursor.move(position);
        String itemName = cursor.getString(cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_PRODUCT_NAME));
        int itemquantity = cursor.getInt(cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_PRODUCT_QUANTITY));
        double itemprice = cursor.getDouble(cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_PRODUCT_PRICE));
        String email = cursor.getString(cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_PRODUCT_EMAIL));

        nameEditText.setText(itemName);
        quantityEditText.setText(""+itemquantity);
        priceEditText.setText(""+itemprice);
        emailEditText.setText(email);
        cursor.close();
    }
    public void increment(View view){
        quantity = Integer.parseInt(quantityEditText.getText().toString());
        quantityEditText.setText("" + (quantity+1));
    }
    public void decrement(View view){
        quantity = Integer.parseInt(quantityEditText.getText().toString());
        if(quantity == 0){
            Toast.makeText(this, "Sorry the minnimum quantity is 0.", Toast.LENGTH_SHORT);
        } else {
            quantityEditText.setText("" + (quantity-1));
        }
    }

    public void saveProduct(){
        boolean savePet = true;
        price = -1;
        name = nameEditText.getText().toString();
        if(quantityEditText.getText().toString().length() > 9){
            Toast.makeText(this, "Please have a smaller quantity", Toast.LENGTH_SHORT).show();
            return;
        }
        quantity = Integer.parseInt(quantityEditText.getText().toString());
        if(priceEditText.getText().toString().length() >= 1) {
            price = Double.parseDouble(priceEditText.getText().toString());
        }

        email = emailEditText.getText().toString();
        if(name.length() < 1){
            Toast.makeText(this, "You must supply a name", Toast.LENGTH_SHORT).show();
            savePet = false;
        }
        if(email.length() < 7){
            Toast.makeText(this, "You must supply a real email", Toast.LENGTH_SHORT).show();
            savePet = false;
        }
        if(price == -1){
            Toast.makeText(this, "You must supply a price", Toast.LENGTH_SHORT).show();
            savePet = false;
        }
        if(savePet){
            if(!editmode) {
                mDbManipulator.insertProductWithValues(name, quantity, price, email);
            } else {
                updateRow();
            }
            Intent backToParent = new Intent(DetailActivity.this, MainActivity.class);
            startActivity(backToParent);
        }

    }
    public void orderMore(View view){
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, email);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Order More " + name);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
    public void updateRow(){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME, nameEditText.getText().toString());
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, Integer.parseInt(quantityEditText.getText().toString()));
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE, Double.parseDouble(priceEditText.getText().toString()));
        values.put(ProductEntry.COLUMN_PRODUCT_EMAIL, emailEditText.getText().toString());

        String selection = ProductEntry._ID + " = ?";
        String[] selectionArgs = { ""+(id) };

        int count = db.update(
                ProductEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int itemId = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (itemId == R.id.action_add_product) {
            Toast.makeText(this, "You clicked the check menu item.", Toast.LENGTH_LONG).show();
            saveProduct();
            return true;
        }
        if(itemId == R.id.action_delete_product){
            Toast.makeText(this, "You have clicked the delete menu item.", Toast.LENGTH_SHORT).show();
            if(editmode){
                int input = id;
                mDbManipulator.deleteProductAt(input);
            }
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
