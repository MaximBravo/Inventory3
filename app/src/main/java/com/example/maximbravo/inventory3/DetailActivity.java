package com.example.maximbravo.inventory3;

import android.content.Intent;
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

import com.example.maximbravo.inventory3.data.ProductContract.ProductEntry;
import com.example.maximbravo.inventory3.data.ProductDbManiplator;

/**
 * Created by Maxim Bravo on 12/24/2016.
 */

public class DetailActivity extends AppCompatActivity {
    private EditText nameEditText;
    private EditText quantityEditText;
    private EditText priceEditText;

    private String name;
    private int quantity;
    private double price;

    private Button incrementButton;
    private Button decrementButton;

    private ProductEntry mDbHelper;

    private ProductDbManiplator mDbManipulator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        nameEditText = (EditText) findViewById(R.id.edit_product_name);
        quantityEditText = (EditText) findViewById(R.id.edit_product_quantity);
        priceEditText = (EditText) findViewById(R.id.edit_product_price);
        quantity = Integer.parseInt(quantityEditText.getText().toString());

        incrementButton = (Button) findViewById(R.id.increment_button);
        decrementButton = (Button) findViewById(R.id.decrement_button);

        mDbManipulator = new ProductDbManiplator(this);

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
        quantity = Integer.parseInt(quantityEditText.getText().toString());
        if(priceEditText.getText().toString().length() >= 1) {
            price = Double.parseDouble(priceEditText.getText().toString());
        }

        if(name.length() < 1){
            Toast.makeText(this, "You must supply a name", Toast.LENGTH_SHORT).show();
            savePet = false;
        }
        if(price == -1){
            Toast.makeText(this, "You must supply a price", Toast.LENGTH_SHORT).show();
            savePet = false;
        }
        if(savePet){
            mDbManipulator.insertProductWithValues(name, quantity, price);
            Intent backToParent = new Intent(DetailActivity.this, MainActivity.class);
            startActivity(backToParent);
        }

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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_product) {
            Toast.makeText(this, "You clicked the check menu item.", Toast.LENGTH_LONG).show();
            saveProduct();
            return true;
        }
        if(id == R.id.action_delete_product){
            Toast.makeText(this, "You have clicked the delete menu item.", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
