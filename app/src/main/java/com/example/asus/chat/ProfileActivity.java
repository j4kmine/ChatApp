package com.example.asus.chat;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import static android.os.Build.TAGS;

public class ProfileActivity extends AppCompatActivity {
    private TextView mDisplay,mStatus,mTotal;
    private ImageView profil_img;
    private Button btn_friend,btn_decline;
    private DatabaseReference mDatabaseref;
    private ProgressDialog progressDialog;
    private String mCurrent_state;
    private FirebaseUser mCurrent_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        final String userid = getIntent().getStringExtra("user_id");
        profil_img = (ImageView)findViewById(R.id.img_profile);
        mDisplay =(TextView)findViewById(R.id.txtDisplay);
        mStatus =(TextView)findViewById(R.id.txtStatus);
        mTotal =(TextView)findViewById(R.id.total);
        btn_friend=(Button)findViewById(R.id.send_Friend);
        btn_decline=(Button)findViewById(R.id.decline_request);

        mCurrent_user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseref = FirebaseDatabase.getInstance().getReference().child("user").child(userid);
        mCurrent_state = "not_friends";
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        mDatabaseref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String display_name = dataSnapshot.child("name").getValue().toString();
                String status_user = dataSnapshot.child("status").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                Log.d(TAGS,"inilah si jali jali"+display_name);
 //               mDisplay.setText(display_name);
//                mStatus.setText(status);
//                Picasso.with(ProfileActivity.this).load(image).placeholder(R.drawable.profile).into(profil_img);
//                if(mCurrent_user.getUid().equals(userid)){
//                    btn_decline.setEnabled(false);
//                    btn_decline.setVisibility(View.INVISIBLE);
//                    btn_friend.setEnabled(false);
//                    btn_friend.setVisibility(View.INVISIBLE);
//                }
                progressDialog.dismiss();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
