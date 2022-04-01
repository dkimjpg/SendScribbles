package com.example.sendscribbles;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {
    public final static String TAG = "SignUpActivity";
    private EditText etSignUpUser;
    private EditText etSignUpPassword;
    private EditText etSignUpConPass;
    private Button btnSignUp;
    private Button btnGoToSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etSignUpUser = findViewById(R.id.etSignUpUser);
        etSignUpPassword = findViewById(R.id.etSignUpPass);
        etSignUpConPass = findViewById(R.id.etConPass);
        btnSignUp = findViewById(R.id.btnSignUpConfirm);
        btnGoToSignIn = findViewById(R.id.btnGoToLogin);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = etSignUpUser.getText().toString();
                String pass = etSignUpPassword.getText().toString();
                if(!pass.equals(etSignUpConPass.getText().toString())){
                    Toast.makeText(SignUpActivity.this, "Passwords do not Match", Toast.LENGTH_SHORT).show();
                    return;
                }
                addNewScribbleUser(user, pass);
            }
        });

        btnGoToSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoLoginActivity();
            }
        });
    }

    private void gotoLoginActivity() {
        Intent i = new Intent(this,LoginActivity.class);
        startActivity(i);
        finish();
    }

    private void goToMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private void addNewScribbleUser(String user, String pass) {
        ParseUser newUser = new ParseUser();
        newUser.setUsername(user);
        newUser.setPassword(pass);

        newUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    Log.i(TAG,"Signed Up");
                    goToMainActivity();
                }
                else{
                    Log.e(TAG,"OOPS something went wrong");
                }
            }
        });
    }
}