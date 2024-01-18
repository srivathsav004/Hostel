package com.example.hostel;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ProgressDialog;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class NoticeUploadA extends AppCompatActivity {

    private LinearLayout noticesLayout;
    private DatabaseReference noticeAReference;
    ImageView AdminUploadNotice;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_upload);

        AdminUploadNotice = findViewById(R.id.AdminUploadNotice);
        AdminUploadNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoticeUploadA.this, AdminNoticeActivity.class);
                startActivity(intent);
            }
        });

        noticesLayout = findViewById(R.id.noticesLayout);
        noticeAReference = FirebaseDatabase.getInstance().getReference("NoticeA");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Notices...");
        progressDialog.setCancelable(false);

        loadNotices();
    }

    private void loadNotices() {
        progressDialog.show(); // Show progress dialog
        noticeAReference.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressDialog.dismiss(); // Dismiss progress dialog
                noticesLayout.removeAllViews();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    NoticeA notice = snapshot.getValue(NoticeA.class);
                    if (notice != null) {
                        addNoticeToLayout(notice, snapshot.getKey());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss(); // Dismiss progress dialog on error
                // Handle the error
            }
        });
    }

    private void addNoticeToLayout(NoticeA notice, String noticeId) {
        View noticeView = LayoutInflater.from(this).inflate(R.layout.item_notice, null);
        ImageView noticeImage = noticeView.findViewById(R.id.noticeImage);
        TextView noticeTitle = noticeView.findViewById(R.id.noticeTitle);
        TextView noticeDateTime = noticeView.findViewById(R.id.noticeDateTime); // Add this line
        ImageView deleteIcon = noticeView.findViewById(R.id.deleteIcon);

        noticeTitle.setText(notice.getTitle());
        noticeDateTime.setText("Uploaded on: " + notice.getDateTime()); // Assuming getDateTime() is the method in NoticeA class to retrieve date and time
        Glide.with(this)
                .load(notice.getImageUrl())
                .into(noticeImage);

        // Set onClickListener for delete icon
        deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Call method to delete the notice
                deleteNotice(noticeId);
            }
        });

        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, getResources().getDimensionPixelSize(R.dimen.notice_spacing), 0, 0);
        noticeView.setLayoutParams(layoutParams);

        noticesLayout.addView(noticeView);
    }


    private void deleteNotice(String noticeId) {
        // Retrieve the notice data before deletion
        noticeAReference.child(noticeId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                NoticeA notice = dataSnapshot.getValue(NoticeA.class);
                if (notice != null) {
                    showDeleteConfirmationDialog(notice, noticeId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
            }
        });
    }

    private void showDeleteConfirmationDialog(NoticeA notice, String noticeId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Notice");
        builder.setMessage("Are you sure you want to delete this notice?");
        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
            // User confirmed, proceed with deletion
            deleteNoticeData(noticeId, notice.getImageUrl());
        });
        builder.setNegativeButton("No", (dialogInterface, i) -> {
            // User canceled, do nothing
        });
        builder.show();
    }

    private void deleteNoticeData(String noticeId, String imageUrl) {
        // Delete data from Firebase Realtime Database
        noticeAReference.child(noticeId).removeValue();

        // Implement logic to delete the image if needed
        deleteImage(imageUrl);
        recreate();

        // Show success toast
        Toast.makeText(NoticeUploadA.this, "Notice deleted successfully", Toast.LENGTH_SHORT).show();
    }

    private void deleteImage(String imageUrl) {
        // Implement logic to delete the image.
        // This will depend on how the images are stored. If they are stored in Firebase Storage, you can use StorageReference to delete the image.
        // Here is an example assuming the images are stored in Firebase Storage:

        // Get the reference to the image in Firebase Storage
        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl);

        // Delete the image
        storageReference.delete().addOnSuccessListener(aVoid -> {
            // Image deleted successfully
            Log.d("NoticeUploadA", "Image deleted successfully");
        }).addOnFailureListener(e -> {
            // Error deleting the image
            Log.e("NoticeUploadA", "Error deleting image", e);
        });
    }


}
