package com.example.shepherdxx.c_player.radio;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;


/**
 * Created by Shepherdxx on 05.01.2018.
 */

public class Contract {

    public static final String CONTENT_AUTHORITY = "com.example.shepherdxx.c_player";
    public static final String PATH_RADIO_CHANNELS = "channel_list";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private Contract(){}

    /**
     * Inner class that defines constant values for the radio database table.
     * Each entry in the table represents a single chanel.
     */
    public static class RDB_Entry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_RADIO_CHANNELS);

        /** Name of database table for radio_list */
        public static final String TABLE_NAME = "radio_list";

        /**
         * Type: TEXT
         */
        public static final String COLUMN_NAME = "name";

        /**
         * Type: TEXT
         */
        public static final String COLUMN_URL = "address";

        /**
         * Can be null
         * Type: TEXT
         */
        public static final String COLUMN_IMAGE_URL = "image";

        /**
         * The only possible values are {@link #YEP},{@link #NOPE},
         * or {@link #DEFAULT}.
         * Type: INTEGER
         */
        public static final String COLUMN_FAVOURITE = "favourite";

        /**
         * Can be null
         * Type: TEXT
         */
        public static final String COLUMN_DESCRIPTION = "description";

        public static final int USER = 0;
        public static final int SERVER = 1;
        /**
         * По дефолту - {@link #USER}, предустановленные - {@link #SERVER}
         */
        public static final String COLUMN_SOURCE = "source";

        public static final String COLUMN_TIMESTAMP = "timestamp";

        /**
         * Possible values for the favourite column.
         */
        public static final int YEP = 1;
        public static final int NOPE = 2;
        public static final int DEFAULT = 0;

        /**
         * Returns whether or not the given value is {@link #YEP},{@link #NOPE},
         * or {@link #DEFAULT}.
         */
        public static boolean isValidValue(int favour) {
            if (favour == DEFAULT || favour == YEP || favour == NOPE) {
                return true;
            }
            return false;
        }

        /**
         * The default sort order for
         * queries containing NAME fields.
         */
        public static final String SORT_ORDER_DEFAULT =
                COLUMN_NAME + " ASC";

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of radio channels.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RADIO_CHANNELS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single channel.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RADIO_CHANNELS;
    }

}
