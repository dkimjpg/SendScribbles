package com.example.sendscribbles;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private TextInputLayout etUser;
    private TextInputLayout etPass;
    private Button btnSignIn;
    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(ParseUser.getCurrentUser() != null){
            goToMainActivity();
        }

        etUser = findViewById(R.id.et_Username);
        etPass = findViewById(R.id.et_Password);
        btnSignIn = findViewById(R.id.btn_signin);
        btnSignUp = findViewById(R.id.btn_signup);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG,"Login Button on Click");
                String username = etUser.getEditText().getText().toString();
                String password = etPass.getEditText().getText().toString();
                loginUser(username,password);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToNewUserActivity();
            }
        });


    }

    public void loginUser(String user, String pass){
        ParseUser.logInInBackground(user, pass, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e != null){
                    Log.e(TAG, "Issue with login",e);
                    etPass.setErrorEnabled(true);
                    etPass.setError("Invalid Username or Password");
                    return;
                }
                goToMainActivity();
                Toast.makeText(LoginActivity.this,"Successfully Logged In",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void goToNewUserActivity(){
        Intent i = new Intent(this,SignUpActivity.class);
        startActivity(i);
        finish();
    }

    private void goToMainActivity() {
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
        finish();
    }
}