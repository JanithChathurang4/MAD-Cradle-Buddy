package com.example.infantcareaidingmobileapplication;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MonthlyPhoto extends AppCompatActivity {

    private static final int img_request = 1;
    private  Button choosB,uploadB,galleryB;
    private EditText monthNum;
    private ImageView imgD;
    private ProgressBar hprogbar;
   // private TextView name;
    private Uri mImageUri;
    private StorageReference storeref;
    private DatabaseReference dbref;
    private StorageTask mUploadTask;
    private ArrayList<Uploader> muploads;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_photo);
     //   name = findViewById(R.id.text_v_name);
        choosB = findViewById(R.id.chooseP);
        uploadB = findViewById(R.id.upload);
        galleryB = findViewById(R.id.Gallery);
        monthNum = findViewById(R.id.MonthNum);
        imgD = findViewById(R.id.ViewChoosedone);
        hprogbar = findViewById(R.id.progressBar2);

        storeref = FirebaseStorage.getInstance().getReference(FirebaseAuth.getInstance().getCurrentUser().getUid());; /*FirebaseStorage.getInstance().getReference(("Photo1/")+FirebaseAuth.getInstance().getCurrentUser().getUid());*/
        dbref = FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().getCurrentUser().getUid());; /*FirebaseDatabase.getInstance().getReference(("Photo1/")+FirebaseAuth.getInstance().getCurrentUser().getUid());*/

        choosB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilechooser();
            }
        });
        uploadB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mUploadTask != null && mUploadTask.isInProgress()){
                    Toast.makeText(MonthlyPhoto.this,"Upload in progress",Toast.LENGTH_SHORT).show();
                }
                uploadphoto();}
        });
        galleryB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagesActivity();
            }
        });
    }
    private void openFilechooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,img_request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == img_request && resultCode== RESULT_OK && data != null && data.getData() != null){
            mImageUri = data.getData();

            Picasso.get().load(mImageUri).into(imgD);

        }
    }

    private String getFileExtention( Uri uri1){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri1));
    }
    private void uploadphoto(){
          if (mImageUri != null){
            StorageReference fileRef = storeref.child(System.currentTimeMillis()+"."+getFileExtention(mImageUri));
            mUploadTask=fileRef.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    hprogbar.setProgress(0);
                                }
                            }, 500);
                            Toast.makeText(MonthlyPhoto.this,"Upload successful",Toast.LENGTH_LONG).show();
                            /*Uploader bu = new Uploader(monthNum.getText().toString().trim(),
                                    taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
                            String uploadId = dbref.push().getKey();
                            dbref.child(uploadId).setValue(bu);*/
                            Task<Uri>uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful());
                            Uri downloadUri = uriTask.getResult();
                            Uploader upload = new Uploader(monthNum.getText().toString().trim(),downloadUri.toString());
                            String uploadID = dbref.push().getKey();
                            dbref.child(uploadID).setValue(upload);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MonthlyPhoto.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            hprogbar.setProgress((int)progress);
                        }
                    });
       /* if (mImageUri != null) {
            final StorageReference fileReference = storeref.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(System.currentTimeMillis()+"."+getFileExtention(mImageUri));

            fileReference.putFile(mImageUri).continueWithTask(
                    new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {

                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                            if (!task.isSuccessful()) {
                                throw task.getException(); }
                            return fileReference.getDownloadUrl();
                        } })

                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) { Uri downloadUri = task.getResult();
                                BabyUser bbu = new BabyUser(monthNum.getText().toString().trim(), downloadUri.toString());
                                dbref.push().setValue(bbu);
                                Toast.makeText(MonthlyPhoto.this, "Upload successful", Toast.LENGTH_LONG).show();
                            }
                            else { Toast.makeText(MonthlyPhoto.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    })*/


        }else {
            Toast.makeText(this,"No photo selected",Toast.LENGTH_SHORT).show();
        }
    }
    private void openImagesActivity(){
        Intent intent =new Intent(this,ImagesActivity.class);
        startActivity(intent);
    }
}