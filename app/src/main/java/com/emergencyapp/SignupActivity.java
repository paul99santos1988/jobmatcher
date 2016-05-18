package com.emergencyapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.sourcey.materiallogindemo.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    @InjectView(R.id.etName)
    EditText etName;
    @InjectView(R.id.etUsername)
    EditText etUsername;
    @InjectView(R.id.etAddress)
    EditText etAddress;
    @InjectView(R.id.etEmail)
    EditText etEmail;
    @InjectView(R.id.etContactNo)
    EditText etContactNo;
    @InjectView(R.id.etPassword)
    EditText etPassword;
    @InjectView(R.id.btnSignup)
    Button btnSignup;
    @InjectView(R.id.tvLoginlink)
    TextView tvLoginlink;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.inject(this);
        etName = (EditText) findViewById(R.id.etName);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etAddress = (EditText) findViewById(R.id.etAddress);
        etContactNo = (EditText) findViewById(R.id.etContactNo);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);

        btnSignup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                validate();

                if(validate()) {
                    signup();
                    //TODO: KUWANG PA OG DELAY MU DIRITSO
                }
                else
                    onRegisterFailed();
            }
        });

        tvLoginlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void signup(){
        Log.d(TAG, "Signup");
        String name = etName.getText().toString();
        long contactNo = Long.parseLong(etContactNo.getText().toString());
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String username = etUsername.getText().toString();
        String address = etAddress.getText().toString();

        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.put("name", name);
        user.put("contactNumber", contactNo);
        user.put("locationName", address);
        user.put("type", "respondent");

        // FOR PUSH NOTIFICATION
        // Associate the device with a user
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("user",ParseUser.getCurrentUser());
        installation.saveInBackground();
        // END

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.e(TAG,"ParseException is "+e);
//                    btnSignup.setEnabled(false);
                    //
                } else {
                    Log.e(TAG,"ParseException is "+e);

                }
            }
        });





            final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this, R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Creating Account...");
            progressDialog.show();

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                        }
                    }, 3000);

        }


    public boolean validate() {
        boolean valid = true;

        String name = etName.getText().toString();
        //int contactNo_int = Integer.parseInt(etContactNo.getText().toString());
        String contactNo = etContactNo.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String username = etUsername.getText().toString();
        String address = etAddress.getText().toString();

        if (username.isEmpty() || username.length() < 6) {
            etUsername.setError("at least 6 characters");
           valid = false;
        }
        if (address.isEmpty() || address.length() < 10) {
            etAddress.setError("Invalid address");
            valid = false;
        }
        if (name.isEmpty() || name.length() < 10) {
            etName.setError("enter your full name");
            valid = false;
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("enter a valid email address");
            valid = false;
        }
        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            etPassword.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        }
        if (contactNo.length() < 11 || contactNo.length() > 11 || contactNo.isEmpty()) {
            etContactNo.setError("enter valid phone number (11 digits)");
            valid = false;
        }
        return valid;

    }
    public void onRegisterFailed() {
        Toast.makeText(getBaseContext(), "Invalid Credentials", Toast.LENGTH_LONG).show();
        btnSignup.setEnabled(true);
    }


}
