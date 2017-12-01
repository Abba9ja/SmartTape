package com.example.abba9ja.smarttape;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class Feedback extends AppCompatActivity {
    Button btnSubmit;
    EditText etDescription, etSubject;
    CheckBox chk1, chk2, chk3, chk4, chk5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Feed Back");
        initializeVar();



        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subject = etSubject.getText().toString();
                String description = etDescription.getText().toString();

                String mailSubject = "PopCorn Movies Feedback on " + subject;
                String chkString, chkString2, chkString3, chkString4, chkString5;
                chkString = "";
                chkString2 = "";
                chkString3 = "";
                chkString4 = "";
                chkString5 = "";
                if (description.length() >0){
                    if (chk1.isChecked()){
                        chkString = "LOADING DATA SLOWLY, ";
                    }
                    if(chk2.isChecked()){
                        chkString2 = "FOCUS NOT WORKING, ";
                    }
                    if(chk3.isChecked()){
                        chkString3 = "NEED MORE GUIDE, ";
                    }
                    if (chk4.isChecked()){
                        chkString4 = "HAVING PROBLEM SHARING, ";
                    }
                    if (chk5.isChecked()) {
                        chkString5 += "APP CRASHING, ";
                    }

                    String emailAddress = "abba9ja@gmail.com";
                    String message = chkString + chkString2 + chkString3 + chkString4 + chkString5 + description;
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "abba9ja@gmail.com" });
                    intent.putExtra(Intent.EXTRA_SUBJECT, mailSubject);
                    intent.putExtra(android.content.Intent.EXTRA_TEXT, message);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(Intent.createChooser(intent, "Send mail to Developer:"));
                    }else{
                        Toast.makeText(Feedback.this, "No email app found", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(Feedback.this, "Description required!", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void initializeVar() {
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        etDescription = (EditText) findViewById(R.id.etDescription);
        etSubject = (EditText) findViewById(R.id.subject);
        chk1 = (CheckBox) findViewById(R.id.loadinSlow);
        chk2 = (CheckBox) findViewById(R.id.focus);
        chk3 = (CheckBox) findViewById(R.id.needMore);
        chk4 = (CheckBox) findViewById(R.id.prblmShare);
        chk5 = (CheckBox) findViewById(R.id.appshutdown);

    }

}
