package com.example.asus.chat;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class UserActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private RecyclerView mUserList;
    private DatabaseReference mDatabaseref;
    private LinearLayoutManager mLayoutManager;
    private  FirebaseRecyclerAdapter adapter;
    private FirebaseRecyclerAdapter<Users, UsersViewHolder> mPostAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        mToolbar = (Toolbar)findViewById(R.id.user_app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("All User");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDatabaseref = FirebaseDatabase.getInstance().getReference().child("user");
        mUserList = (RecyclerView)findViewById(R.id.users_list);
        mUserList.setLayoutManager(new LinearLayoutManager(UserActivity.this));
//        mLayoutManager = new LinearLayoutManager(this);
//        mUserList = (RecyclerView)findViewById(R.id.users_list);
//        mUserList.setHasFixedSize(true);
        setupAdapter();
        mUserList.setAdapter(mPostAdapter);


    }
    private void setupAdapter() {
        mPostAdapter = new FirebaseRecyclerAdapter<Users, UsersViewHolder>(
                Users.class,
                R.layout.users_single_layout,
                UsersViewHolder.class,
                mDatabaseref
        ){
            @Override
            protected void populateViewHolder(UsersViewHolder viewHolder, Users model, int position) {
                viewHolder.setDisplayName(model.getName());
                viewHolder.setDisplayStatus(model.getStatus());
                viewHolder.setUserImage(model.getThumb_image(),getApplicationContext());

                final String user_id = getRef(position).getKey();

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent ProfileIntent = new Intent(UserActivity.this,ProfileActivity.class);
                        ProfileIntent.putExtra("user_id",user_id);
                        startActivity(ProfileIntent);
                    }
                });
            }
        };

    }
//    //started when activity becoming visible to user
//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        FirebaseRecyclerAdapter<Users,UsersViewHolder>firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users, UsersViewHolder>(
//             Users.class,R.layout.users_single_layout,UsersViewHolder.class,mDatabaseref
//
//
//        ) {
//
//            @Override
//            protected void populateViewHolder(UsersViewHolder viewHolder, Users model, int position) {
//                viewHolder.setDisplayName(model.getName());
//                viewHolder.setDisplayStatus(model.getStatus());
//                viewHolder.setUserImage(model.getThumb_image(),getApplicationContext());
//
//                final String user_id = getRef(position).getKey();
//
//                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent ProfileIntent = new Intent(UserActivity.this,ProfileActivity.class);
//                        ProfileIntent.putExtra("user_id",user_id);
//                        startActivity(ProfileIntent);
//                    }
//                });
//            }
//        };
//        firebaseRecyclerAdapter.startListening();
//        mUserList.setAdapter(firebaseRecyclerAdapter);
//
//
//    }

    public  static  class UsersViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public UsersViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public  void  setDisplayName(String name){
            TextView username = (TextView)mView.findViewById(R.id.username);
            username.setText(name);
        }
        public  void  setDisplayStatus(String status){
            TextView userStatusView = (TextView)mView.findViewById(R.id.status);
            userStatusView.setText(status);
        }

        public void setUserImage(String uri, Context ctx){
            CircleImageView userViewImage = (CircleImageView)mView.findViewById(R.id.img_profile);
            Picasso.with(ctx).load(uri).placeholder(R.drawable.draws).into(userViewImage);
        }
    }
}
