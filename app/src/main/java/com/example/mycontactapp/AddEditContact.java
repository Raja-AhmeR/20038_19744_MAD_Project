package com.example.mycontactapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddEditContact extends AppCompatActivity {

    private ImageView profileIv;
    private EditText nameEt, phoneEt, emailEt, noteEt;
    private FloatingActionButton fab;

    // String Variables
    String name, phone, email, note;

    ActionBar actionBar;

    // Permission Constant
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 200;
    private static final int IMAGE_FROM_GALLERY_CODE_PERMISSION = 300;
    private static final int IMAGE_FROM_CAMERA_CODE_PERMISSION = 400;

    // String Array of Permissions
    private String[] cameraPermission;
    private String[] storagePermission;

    // Image Uri variable
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_contact);

        // init permissions
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        //init actionbar
        actionBar = getSupportActionBar();
        // set title
        actionBar.setTitle("Add Contact");
        // back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        // init view
        profileIv = findViewById(R.id.profileIv);
        nameEt = findViewById(R.id.nameEt);
        phoneEt = findViewById(R.id.phoneEt);
        emailEt = findViewById(R.id.emailEt);
        noteEt = findViewById(R.id.noteEt);
        fab = findViewById(R.id.fab);

        // add event handler
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

        profileIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickerDialog();
            }
        });

    }

    private void showImagePickerDialog() {
        // Option of Dialog Box
        String options[] = {"Camera", "Gallery"};

        // Alert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Set Title
        builder.setTitle("Choose An Option");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handling Item Click
                if (which == 0) {
                    // Camera Selection
                    if (!checkCameraPermission()) {
                        // Requesting Camera
                        requestStoragePermission();
                    }
                    else {
                        pickFromCamera();
                    }
                }
                else if (which == 1) {
                    // Gallery Selection
                    if (!checkStoragePermission()) {
                        // Requesting Storage
                        requestStoragePermission();
                    }
                    else {
                        pickFromGallery();
                    }
                }
            }
        }).create().show();
    }

    private void pickFromCamera() {
       //Content Value for Images
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "IMAGE_TITLE");
        values.put(MediaStore.Images.Media.DESCRIPTION, "IMAGE_DETAIL");

        // Save Uri
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        // Intent to Open Camera
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        startActivityForResult(cameraIntent, IMAGE_FROM_CAMERA_CODE_PERMISSION);
    }
    private void pickFromGallery() {
        // Intent for image from Gallery
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");  // Only Image

        startActivityForResult(galleryIntent, IMAGE_FROM_GALLERY_CODE_PERMISSION);
    }

    private void saveData() {

        // take input from user and saving in variable
        name = nameEt.getText().toString();
        phone = phoneEt.getText().toString();
        email = emailEt.getText().toString();
        note = noteEt.getText().toString();

        // Checking fields data
        if (!name.isEmpty() || !phone.isEmpty() || !email.isEmpty() || !note.isEmpty()){

        }
        else {
            Toast.makeText(getApplicationContext(), "All Fields Required..!", Toast.LENGTH_SHORT).show();
        }

    }

    // Back button click function
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    // Checking Camera Permissions
    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result & result1;
    }

    // Request for Camera Permission
    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_PERMISSION_CODE);
    }

    // Checking Storage Permissions
    private boolean checkStoragePermission() {
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result1;
    }

    // Request for Storage Permission
    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_PERMISSION_CODE);
    }

    // Handle Request Permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_PERMISSION_CODE:
                if (grantResults.length > 0) {
                    // If all Permission Allowed return true otherwise false
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (cameraAccepted && storageAccepted) {
                        // Both Permissions Accepted
                        pickFromCamera();
                    }
                    else {
                        // Permissions Not Accepted
                        Toast.makeText(getApplicationContext(), "Allow Camera & Storage Permission", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case STORAGE_PERMISSION_CODE:
                if (grantResults.length > 0) {
                    // If all Permission Allowed return true otherwise false
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (storageAccepted) {
                        // Permissions Accepted
                        pickFromGallery();
                    }
                    else {
                        // Permissions Not Accepted
                        Toast.makeText(getApplicationContext(), "Allow Storage Permission", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

   // @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            if (requestCode == IMAGE_FROM_GALLERY_CODE_PERMISSION) {
//                // Picked Image From Gallery
//                // Now Crop Image
//                CropImage.activity(data.getData())
//                        .setGuidelines(CropImageView.Guidelines.ON)
//                        .setAspectRatio(1, 1)
//                        .start(this);
//            }
//            else if (requestCode == IMAGE_FROM_CAMERA_CODE_PERMISSION) {
//                // Capture Image from Camera & Crop
//                CropImage.activity(imageUri)
//                        .setGuidelines(CropImageView.Guidelines.ON)
//                        .setAspectRatio(1, 1)
//                        .start(this);
//            }
//            else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//                // Cropped Image Recieved
//                CropImage.ActivityResult result = CropImage.getActivityResult(data);
//                imageUri = result.getUri();
//                profileIv.setImageURI(imageUri);
//            }
//            else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                // For Error Handling
//                Toast.makeText(getApplicationContext(), "Something Wrong", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

    /*
    1. Create View Object in Java File
    2. Profile Image Taking with User Permission & Crop functionality
        i. First permission from manifest file, Check & Request Permission
        ii. By Clicking profileIv to open Camera or Photo
        iii. Pick Image & Save in ImageUrl Variable
        iv. Create Activity for Crop an Image in Manifest File

    */
}