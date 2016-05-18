package com.emergencyapp;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.sourcey.materiallogindemo.R;

import java.util.Timer;
import java.util.TimerTask;

public class UserArea extends Activity {
private Button btnFire,btnHospital,btnPolice;

    private Button btnFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);
        btnFire= (Button)findViewById(R.id.btnFirefight);
        btnHospital=(Button)findViewById(R.id.btnHospital);
        btnPolice=(Button)findViewById(R.id.btnPolice);

        btnFire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });
        btnPolice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();

            }
        });
        btnHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();

            }
        });

    }


    public void sendRequest() {
        ParseObject Request = new ParseObject("Request");
        Request.put("Patient", ParseUser.getCurrentUser().getObjectId());
        Request.put("name", ParseUser.getCurrentUser().get("name"));
        Request.saveInBackground();

        final ProgressDialog progressDialog = new ProgressDialog(UserArea.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Contacting Respondent...");
        progressDialog.show();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {

                        // After calling complete
                        progressDialog.dismiss();

                        AlertDialog.Builder builder = new AlertDialog.Builder(UserArea.this);
//                                builder.setTitle("Auto-closing Dialog");
                        builder.setMessage("Success! Respondent will arrive shortly.");
                        builder.setCancelable(true);

                        final AlertDialog dlg = builder.create();

                        dlg.show();

                        final Timer t = new Timer();
                        t.schedule(new TimerTask() {
                            public void run() {
                                dlg.dismiss(); // when the task active then close the dialog
                                t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report

                                Intent intent = new Intent(UserArea.this, MapsActivity.class);
                                startActivity(intent);
                            }
                        }, 2000); // after 2 second (or 2000 miliseconds), the task will be active.

                    }
                }, 3000);


        // Push to Respondents
        ParsePush push = new ParsePush();
        push.setChannel("Respondent");
        push.setMessage("This is a sample app for push notification for respondent.");
        push.sendInBackground();
        // Push Respondents end
    }


}
