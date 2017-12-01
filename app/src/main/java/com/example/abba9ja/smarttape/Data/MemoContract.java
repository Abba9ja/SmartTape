package com.example.abba9ja.smarttape.Data;

/**
 * Created by Abba9ja on 9/21/2017.
 */

import android.net.Uri;
import android.provider.BaseColumns;


public class MemoContract {

    // The authority, which is how your code knows which Content Provider to access
    public static final String AUTHORITY = "com.example.abba9ja.smarttape";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Define the possible paths for accessing data in this contract
    // This is the path for the "memo" directory
    public static final String PATH_MEMO = "memo";

    /* MemoEntry is an inner class that defines the contents of the memo table */
    public static final class MemoEntry implements BaseColumns {

        // MemoEntry content URI = base content URI + path
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MEMO).build();


        // Memo table and column names
        public static final String TABLE_NAME = "memo";

        // Since MemoEntry implements the interface "BaseColumns", it has an automatically produced
        // "_ID" column in addition to the two below
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_NAME = "name";


    }
}