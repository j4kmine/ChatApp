package com.example.asus.chat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class SettingsActivity extends AppCompatActivity {
    private DatabaseReference mUserDatabaseref;
    private FirebaseUser mUserDatabase;
    private TextView txtStatus;
    private TextView txtDisplayName;
    private Button btnChangeImage;
    private Button btnChangeStatus;
    private CircleImageView profilImg;
    private StorageReference mStorageRef;
    private ProgressDialog mProgressDialog;
    public static  final  int GALLERY_PICK = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_PICK && resultCode == RESULT_OK){
            Uri ImageUri = data.getData();
            CropImage.activity(ImageUri)
                    .setAspectRatio(1,1)
                    .start(this);

        }
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                mProgressDialog = new ProgressDialog(SettingsActivity.this);
                mProgressDialog.setTitle("Uploading Image...");
                mProgressDialog.setMessage("Please Wait Uploading Image");
                mProgressDialog.setCanceledOnTouchOutside(true);
                mProgressDialog.show();

                Uri resultUri = result.getUri();
                final byte[] thumb_byte;
                final File thumb_file_path = new File(resultUri.getPath());
                try {
                    Bitmap thumb_bitmap = new Compressor(this)
                                          .setMaxHeight(200)
                                            .setMaxWidth(200)
                                            .setQuality(75)
                                            .compressToBitmap(thumb_file_path);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    thumb_bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
                    thumb_byte= baos.toByteArray();
                    String current_id = mUserDatabase.getUid();
                    StorageReference riversRef = mStorageRef.child("profile_images").child(current_id+".jpg");
                    final StorageReference thumbs = mStorageRef.child("profile_images").child("thumbs").child(current_id+".jpg");
                    riversRef.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()){
                                final String download_url = task.getResult().getDownloadUrl().toString();
                                UploadTask uploadTask = thumbs.putBytes(thumb_byte);
                                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {
                                        String thumb_download_url = thumb_task.getResult().getDownloadUrl().toString();
                                        if(thumb_task.isSuccessful()){
                                            Map update_Hasmap = new HashMap();
                                            update_Hasmap.put("image",download_url);
                                            update_Hasmap.put("thumb_image",thumb_download_url);

                                            mUserDatabaseref.updateChildren(update_Hasmap).addOnCompleteListener(new OnCompleteListener() {
                                                @Override
                                                public void onComplete(@NonNull Task task) {
                                                    if(task.isSuccessful()){
                                                        mProgressDialog.dismiss();
                                                        Toast.makeText(SettingsActivity.this,"Working",Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });
                                        }

                                    }
                                });
//                                mUserDatabaseref.child("image").setValue(download_url).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        mProgressDialog.dismiss();
//                                        Toast.makeText(SettingsActivity.this,"Working",Toast.LENGTH_LONG).show();
//                                    }
//                                });

                            }else{
                                mProgressDialog.dismiss();
                                Toast.makeText(SettingsActivity.this,"Error",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){

                Exception error = result.getError();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        profilImg = (CircleImageView)findViewById(R.id.circleImageView);
        txtDisplayName= (TextView)findViewById(R.id.txtDisplay);
        txtStatus=(TextView)findViewById(R.id.txtStatus);
        btnChangeImage = (Button)findViewById(R.id.btnImage);
        btnChangeStatus=(Button)findViewById(R.id.btnStatus);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mUserDatabase= FirebaseAuth.getInstance().getCurrentUser();
        String current_id = mUserDatabase.getUid();
        btnChangeStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String status_value = txtStatus.getText().toString();
                Intent status_intent = new Intent(SettingsActivity.this,StatusActivity.class);
                status_intent.putExtra("status_value",status_value);
                startActivity(status_intent);
            }
        });
        btnChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK);

                /*
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(SettingsActivity.this);
                        */
            }
        });
        mUserDatabaseref = FirebaseDatabase.getInstance().getReference().child("user").child(current_id);
        mUserDatabaseref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();
                txtDisplayName.setText(name);
                txtStatus.setText(status);
                if(! image.equals("default")){
                    Picasso.with(SettingsActivity.this).load(image).into(profilImg);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(20);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }
}
