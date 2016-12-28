package com.example.maximbravo.inventory3.data;

import android.provider.BaseColumns;

/**
 * Created by Maxim Bravo on 12/22/2016.
 */

public class ProductContract {
    public static class ProductEntry implements BaseColumns {
        public static final String TABLE_NAME = "store";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_PRODUCT_NAME = "name";
        public static final String COLUMN_PRODUCT_QUANTITY = "quantity";
        public static final String COLUMN_PRODUCT_PRICE = "price";
        public static final String COLUMN_PRODUCT_EMAIL = "email";
    }
}
