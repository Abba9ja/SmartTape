package com.example.abba9ja.smarttape;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class About extends AppCompatActivity {
    ImageButton fbfl, googlefl, insfl;
    private ProgressDialog pDialog;
    public  Boolean pp=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("About");


        fbfl = (ImageButton) findViewById(R.id.flfb);
        googlefl = (ImageButton) findViewById(R.id.flgoogle);
        insfl = (ImageButton) findViewById(R.id.flins);

        fbfl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog();
                try {
                    String facebookScheme = "fb://profile/100003760700709";
                    Intent facebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebookScheme));
                    startActivity(facebookIntent);
                }catch (ActivityNotFoundException e){
                    Intent facebookIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.facebook.com/mshehubello2"));
                    startActivity(facebookIntent);
                }

            }
        });

        googlefl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog();
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setClassName("com.google.android.apps.plus",
                            "com.google.android.apps.plus.phone.UrlGatewayActivity");
                    intent.putExtra("customAppUri", "101876982071790049264");
                    startActivity(intent);
                }catch (ActivityNotFoundException e){
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/101876982071790049264")));
                }

            }
        });

        insfl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog();
                Uri uri = Uri.parse("http://instagram.com/_u/abba9ja");
                Intent insta = new Intent(Intent.ACTION_VIEW, uri);
                insta.setPackage("com.instagram.android");
                try {
                    startActivity(insta);
                }catch (ActivityNotFoundException e){
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/abba9ja")));
                }


            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();
        if (pp){
            pDialog.dismiss();
            pp=false;
        }
    }

    public void progressDialog() {
        pDialog = new ProgressDialog(About.this);
        pDialog.setMessage("Loading ...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
        pp=true;
    }
}
