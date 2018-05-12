package com.example.asus.chat;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StatusActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private TextInputLayout StatusSaved;
    private Button btnStatus;
    private DatabaseReference mDatabaseRference;
    private FirebaseUser mCurrentuser;
    private ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        mCurrentuser = FirebaseAuth.getInstance().getCurrentUser();
        String user_id = mCurrentuser.getUid();
        mDatabaseRference = FirebaseDatabase.getInstance().getReference().child("user").child(user_id);
        mProgressDialog = new ProgressDialog(this);
        mToolbar =(Toolbar)findViewById(R.id.app_bar_status);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String status = getIntent().getStringExtra("status_value");
        StatusSaved =(TextInputLayout)findViewById(R.id.StatusSaved);
        btnStatus =(Button)findViewById(R.id.btnSavedstatus);
        StatusSaved.getEditText().setText(status);
        btnStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressDialog.setTitle("Loading Status");
                mProgressDialog.setMessage("Please Wait");
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.show();

                String status = StatusSaved.getEditText().getText().toString();
                mDatabaseRference.child("status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            mProgressDialog.dismiss();
                        }else{
                            mProgressDialog.hide();
                            Toast.makeText(getApplicationContext(),"There Was Some Error",Toast.LENGTH_LONG);
                        }
                    }
                });
            }
        });



    }
}
