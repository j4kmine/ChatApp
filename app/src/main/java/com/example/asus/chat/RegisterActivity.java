package com.example.asus.chat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private TextInputLayout regEmail;
    private TextInputLayout regPassword;
    private TextInputLayout regDisplayName;
    private Button regSubmit;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDb;
    private ProgressDialog mProgressBar;
    private DatabaseReference mDatabase;
    private android.support.v7.widget.Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mToolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.register_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mProgressBar = new ProgressDialog(this);
        regEmail = (TextInputLayout)findViewById(R.id.reg_emails);
        regPassword =(TextInputLayout)findViewById(R.id.reg_passwords);
        regDisplayName =(TextInputLayout)findViewById(R.id.textInputLayout8);
        regSubmit =(Button)findViewById(R.id.reg_submit);
        mAuth = FirebaseAuth.getInstance();
        regSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String display_name = regDisplayName.getEditText().getText().toString();
                String email = regEmail.getEditText().getText().toString();
                String password = regPassword.getEditText().getText().toString();
                if(!TextUtils.isEmpty(display_name) || !TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)){
                    mProgressBar.setTitle("Registering User");
                    mProgressBar.setMessage("Please Wait !");
                    mProgressBar.setCanceledOnTouchOutside(false);
                    mProgressBar.show();
                    daftar_anggota(display_name,email,password);
                }

            }
        });
    }

    private void daftar_anggota(final String display_name, String email , String password){
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = current_user.getUid();
                    final String names = display_name;
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("user").child(uid);

                    String device_token = FirebaseInstanceId.getInstance().getToken();
                    HashMap<String, String> userMap = new HashMap<>();
                    userMap.put("name", names);
                    userMap.put("status", "Hi there I'm using Lapit Chat App.");
                    userMap.put("image", "default");
                    userMap.put("thumb_image", "default");
                    userMap.put("device_token", device_token);
                    mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                mProgressBar.dismiss();
                                Intent mainIntent = new Intent(RegisterActivity.this,MainActivity.class);
                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(mainIntent);
                                finish();
                            }
                        }
                    });

                }else{
                    mProgressBar.hide();
                    Toast.makeText(RegisterActivity.this, "You Got Some Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
