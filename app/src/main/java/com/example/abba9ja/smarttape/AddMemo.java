package com.example.abba9ja.smarttape;



import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.abba9ja.smarttape.Data.MemoContract;

import static android.R.id.input;

public class AddMemo extends AppCompatActivity {

    public EditText filename, description;
    public Button btnSave, btnUpdate, btnShare;
    public  String uid, des, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_memo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Memo");

        filename  = ((EditText) findViewById(R.id.etFilename));
        description = ((EditText) findViewById(R.id.etDescription));

        btnSave = (Button) findViewById(R.id.btnsave);
        btnUpdate = (Button) findViewById(R.id.btnupdate);
        btnSave = (Button) findViewById(R.id.btnsave);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra("UP_KEY")) {

               Bundle b = this.getIntent().getExtras();
               String[] eachMemoData = b.getStringArray("UP_KEY");
                uid = eachMemoData[0];
                des = eachMemoData[1];
                name = eachMemoData[2];

                filename.setText(name);
                description.setText(des);

                btnUpdate.setEnabled(true);
                btnSave.setEnabled(false);

            }
        }
    }

    public void onClickShare(View view){
        setShareIntent();
    }

    private void setShareIntent() {
        // Get access to the URI for the bitmap
        // Construct a ShareIntent with link to image
        Intent shareIntent = new Intent();
        // Construct a ShareIntent with link to image
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("*/*");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "SMART TAPE APP SHARED MEMO: " +
                "ESTIMATE OF: "+filename.getText()+" DESCRIPTION: " + description.getText());
        // Launch share menu
        startActivity(Intent.createChooser(shareIntent, "Share Estimate"));
    }

    public void onClickSave(View view) {
        // Not yet implemented
        // Check if EditText is empty, if not retrieve input and store it in a ContentValues object
        // If the EditText input is empty -> don't create an entry
        String filename = ((EditText) findViewById(R.id.etFilename)).getText().toString();
        String description = ((EditText) findViewById(R.id.etDescription)).getText().toString();
        if (filename.length() == 0 || description.length() == 0 ) {
            Toast.makeText(AddMemo.this, "Name or Description can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // Insert new task data via a ContentResolver
        // Create new empty ContentValues object
        ContentValues contentValues = new ContentValues();
        // Put the task description and selected mPriority into the ContentValues
        contentValues.put(MemoContract.MemoEntry.COLUMN_DESCRIPTION, description);
        contentValues.put(MemoContract.MemoEntry.COLUMN_NAME, filename);
        // Insert the content values via a ContentResolver
        Uri uri = getContentResolver().insert(MemoContract.MemoEntry.CONTENT_URI, contentValues);

        // Display the URI that's returned with a Toast
        // [Hint] Don't forget to call finish() to return to MainActivity after this insert is complete
        if(uri != null) {
            //Toast.makeTexetBaseContext().uri.toString(), Toast.LENGTH_LONG).show();
        }

        // Finish activity (this returns back to MainActivity)
        finish();
    }

    public void onClickUpdate(View view){
        String filename = ((EditText) findViewById(R.id.etFilename)).getText().toString();
        String description = ((EditText) findViewById(R.id.etDescription)).getText().toString();
        if (filename.length() == 0 || description.length() == 0 ) {
            Toast.makeText(AddMemo.this, "Name or Description can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues contentValues = new ContentValues();
        String mSelectionClause = MemoContract.MemoEntry._ID+ "=?";
        String[] mSelectionArgs = {uid};

        String stringId =uid;
        Uri uri = MemoContract.MemoEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(stringId).build();

        int rowUpdated = 0;

        contentValues.put(MemoContract.MemoEntry.COLUMN_DESCRIPTION, description);
        contentValues.put(MemoContract.MemoEntry.COLUMN_NAME, filename);

        rowUpdated = getContentResolver().update(uri, contentValues, mSelectionClause,mSelectionArgs);

        if (rowUpdated != 0){
            finish();
        }


    }
}
