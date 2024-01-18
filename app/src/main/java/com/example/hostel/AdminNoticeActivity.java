package com.example.hostel;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.app.ProgressDialog;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.util.Date;

public class AdminNoticeActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private EditText titleEditText;
    private Button selectButton;
    private Button uploadButton;
    private ProgressDialog progressDialog;
    private DatabaseReference noticeAReference;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_notice);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");
        titleEditText = findViewById(R.id.Title);
        selectButton = findViewById(R.id.Select);
        uploadButton = findViewById(R.id.Upload);
        noticeAReference = FirebaseDatabase.getInstance().getReference("NoticeA");
        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadFile();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
        }
    }

    private void uploadFile() {
        if (imageUri != null) {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(imageUri));

            progressDialog.show();

            UploadTask uploadTask = fileReference.putFile(imageUri);

            uploadTask.continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                return fileReference.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    String title = titleEditText.getText().toString().trim();
                    String formattedDateTime = DateFormat.format("dd/MM/yyyy hh:mm a", new Date()).toString();
                    NoticeA notice = new NoticeA(title, downloadUri.toString(), formattedDateTime);
                    noticeAReference.push().setValue(notice);
                    Toast.makeText(AdminNoticeActivity.this, "File uploaded successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AdminNoticeActivity.this, "File upload failed", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e -> {
                progressDialog.dismiss();
                e.printStackTrace();
                Toast.makeText(AdminNoticeActivity.this, "File upload failed", Toast.LENGTH_SHORT).show();
            });
        } else {
            Toast.makeText(AdminNoticeActivity.this, "Please select an image", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileExtension(Uri uri) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(this.getContentResolver().getType(uri));
    }
}
