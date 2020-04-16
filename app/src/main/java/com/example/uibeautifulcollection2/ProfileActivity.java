package com.example.uibeautifulcollection2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.uibeautifulcollection2.check.Upload;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvInfo;
    private ImageView imgProfile;
    private EditText edtName;
    private Button btnSave;
    private Uri uriProfileImage;
    private ProgressBar progressBarProfile;
    private static int CHOOSE_IMAGE = 101;
    private String profileImageUrl;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private StorageTask upLoadTask;
    private Uri mUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        init();
    }

    private void init() {
        tvInfo = (TextView)findViewById(R.id.tv_cameras);
        imgProfile = (ImageView)findViewById(R.id.img_cameras);
        edtName = (EditText)findViewById(R.id.edt_name_profile);
        btnSave = (Button)findViewById(R.id.btn_save);
        progressBarProfile =(ProgressBar)findViewById(R.id.progress_profile);
        Sprite fading = new FadingCircle();
        progressBarProfile.setIndeterminateDrawable(fading);
        imgProfile.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        databaseReference = FirebaseDatabase.getInstance().getReference("uploads");
        //loadUserInformation();
    }
    @Override
    protected void onStart() {
        super.onStart();
        if(firebaseAuth.getCurrentUser()==null){
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }
//    private void uploadImageToFirebaseStorage() {
//        final StorageReference profileImageRef = FirebaseStorage.getInstance().getReference("profilepics/"+System
//        .currentTimeMillis()+".jpg");
//        if(uriProfileImage!=null){
//            progressBarProfile.setVisibility(View.VISIBLE);
//            profileImageRef.putFile(uriProfileImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    progressBarProfile.setVisibility(View.GONE);
//                    profileImageRef.putFile(uriProfileImage).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//                        @Override
//                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                            if (!task.isSuccessful()) {
//                                throw task.getException();
//                            }
//                            return profileImageRef.getDownloadUrl();
//                        }
//                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Uri> task) {
//                            if (task.isSuccessful()) {
//                                Uri downloadUri = task.getResult();
//                            } else {
//                                Toast.makeText(ProfileActivity.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    progressBarProfile.setVisibility(View.GONE);
//                }
//            });
//        }
//    }
    private void saveUserInformation() {
        String displayName = edtName.getText().toString();
//        if (displayName.isEmpty()){
//            edtName.setError("Field can not be empty");
//            edtName.requestFocus();
//            return;
//        }
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user!=null && profileImageUrl!=null){
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .setPhotoUri(Uri.parse(profileImageUrl))
                    .build();
            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(ProfileActivity.this,"Profile Updated",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
//    private void uploadImageToFirebaseStorage() {
//        final StorageReference profileImageRef = FirebaseStorage.getInstance().getReference("profilepics/"+System
//        .currentTimeMillis()+".jpg");
//        if(uriProfileImage!=null) {
//            progressBarProfile.setVisibility(View.VISIBLE);
//            profileImageRef.putFile(uriProfileImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    progressBarProfile.setVisibility(View.GONE);
//                    profileImageUrl = taskSnapshot.getStorage().getDownloadUrl().toString();
//
//                }
//            })
//            .addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    progressBarProfile.setVisibility(View.GONE);
//                    Toast.makeText(ProfileActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
//                }
//            });
//        }
//    }


    private void findImageChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Profile Image"), CHOOSE_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CHOOSE_IMAGE && resultCode == RESULT_OK && data !=null && data.getData()!=null){
            uriProfileImage = data.getData();
//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriProfileImage);
//                imgProfile.setImageBitmap(bitmap);
//                uploadImageToFirebaseStorage();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            mUri = data.getData();
            Picasso.get().load(mUri).into(imgProfile);
            uploadToDatabase();
        }
    }
    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void loadUserInformation() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user!=null){
            if(user.getPhotoUrl()!=null){
                //String photoUrl = user.getPhotoUrl().toString();
                Glide.with(this).
                        load(user.getPhotoUrl().toString())
                        .into(imgProfile);
            }
        }
        if(user.getDisplayName()!=null){
            edtName.setText(user.getDisplayName());
        }
    }
    private void uploadToDatabase(){
//        String displayName = edtName.getText().toString();
//        if (displayName.isEmpty()){
//            edtName.setError("Field can not be empty");
//            edtName.requestFocus();
//            return;
//        }
        if (mUri!=null){
            progressBarProfile.setVisibility(View.VISIBLE);
            StorageReference fileReference = storageReference.child(
                    System.currentTimeMillis()+"."+getFileExtension(mUri));
            upLoadTask = fileReference.putFile(mUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressBarProfile.setProgress(0);
                        }
                    },500);
                    Toast.makeText(ProfileActivity.this,"Upload Successful",Toast.LENGTH_LONG).show();
                    Upload upload = new Upload(edtName.getText().toString().trim(),
                            taskSnapshot.getStorage().getDownloadUrl().toString());
                    String uploadId = databaseReference.push().getKey();
                    databaseReference.child(uploadId).setValue(upload);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProfileActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                }
            });
        }else {
            Toast.makeText(this,"No file selected",Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_save:
                saveUserInformation();
                break;
            case R.id.img_cameras:
                String displayName = edtName.getText().toString();
                if (displayName.isEmpty()){
                    edtName.setError("Field can not be empty");
                    edtName.requestFocus();
                    return;
                }
                findImageChooser();
                break;
        }
    }
}
