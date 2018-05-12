package com.example.asus.chat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private android.support.v7.widget.Toolbar mToolbars;
    private TextInputLayout mLoginEmail;
    private TextInputLayout mLoginPassword;
    private ProgressDialog mProgressDialog;
    private Button mLoginButton;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        mToolbars = (android.support.v7.widget.Toolbar)findViewById(R.id.login_toolbar);
        setSupportActionBar(mToolbars);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       mLoginEmail=(TextInputLayout)findViewById(R.id.LoginEmail);
       mLoginPassword =(TextInputLayout)findViewById(R.id.LoginPassword);
       mProgressDialog = new ProgressDialog(this);
       mLoginButton = (Button)findViewById(R.id.btnLogin);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email = mLoginEmail.getEditText().getText().toString();
                String Password = mLoginPassword.getEditText().getText().toString();

               if(! TextUtils.isEmpty(Email) || !TextUtils.isEmpty(Password)){
                   mProgressDialog.setTitle("Login");
                   mProgressDialog.setMessage("Please Wait Checking Credentials");
                    mProgressDialog.setCanceledOnTouchOutside(false);
                    mProgressDialog.show();
                    loginUser(Email,Password);
               }
            }
        });


    }

    private void loginUser(String email, String password) {

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    mProgressDialog.dismiss();
                    Intent mainIntent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                }else{
                    mProgressDialog.hide();
                    Toast.makeText(LoginActivity.this,"canot sign in",Toast.LENGTH_LONG);
                }
            }
        });
    }
}
