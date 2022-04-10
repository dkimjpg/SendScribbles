package com.example.sendscribbles;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.w3c.dom.Text;

public class SignUpActivity extends AppCompatActivity {
    public final static String TAG = "SignUpActivity";
    private TextInputLayout etSignUpUser;
    private TextInputLayout etSignUpPassword;
    private TextInputLayout etSignUpConPass;
    private Button btnSignUp;
    private Button btnGoToSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etSignUpUser = findViewById(R.id.et_SignUpUsername);
        etSignUpPassword = findViewById(R.id.et_SignUpPassword);
        etSignUpConPass = findViewById(R.id.et_SignUpConPass);
        btnSignUp = findViewById(R.id.btnSignUpConfirm);
        btnGoToSignIn = findViewById(R.id.btnGoToLogin);



        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Clear Errors
                etSignUpUser.setError(null);
                etSignUpPassword.setError(null);
                etSignUpConPass.setError(null);

                String user = etSignUpUser.getEditText().getText().toString();
                String pass = etSignUpPassword.getEditText().getText().toString();

                // Form Validation
                if(user.equals("")){
                    etSignUpUser.setError("Username cannot be empty");
                    return;
                }
                else if (pass.equals("")){
                    etSignUpPassword.setError("Password cannot be empty");
                    return;
                }

                if(!pass.equals(etSignUpConPass.getEditText().getText().toString())){
                    etSignUpConPass.setError("Password Do Not Match");
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