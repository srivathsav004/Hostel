package com.example.hostel;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminFeeDetailsA extends AppCompatActivity {

    private DatabaseReference feesReference;
    private ScrollView scrollView;
    private LinearLayout studentsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_fee_details_a);

        feesReference = FirebaseDatabase.getInstance().getReference("feesA"); // Replace with your Firebase path
        scrollView = findViewById(R.id.scrollView); // Replace with your ScrollView ID
        studentsLayout = findViewById(R.id.studentsLayout); // Replace with your LinearLayout ID

        // Load existing students when the activity starts
        loadExistingStudents();
    }

    private void loadExistingStudents() {
        feesReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    clearStudentTabs();
                    for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                        String studentName = studentSnapshot.child("name").getValue(String.class);
                        String receiptUrl = studentSnapshot.child("receiptUrl").getValue(String.class);
                        addStudentTab(studentName, receiptUrl);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(AdminFeeDetailsA.this, "Error loading students", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AdminFeeDetailsA.this, "Failed to load students", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void addStudentTab(String studentName, String receiptUrl) {
        // Create a new LinearLayout for the student tab
        LinearLayout studentTab = new LinearLayout(this);
        studentTab.setOrientation(LinearLayout.HORIZONTAL);
        studentTab.setClickable(true);
        studentTab.setFocusable(true);

        // Set an onClickListener to handle the click event
        studentTab.setOnClickListener(view -> {
            // Implement logic to view the receipt for this student
            openReceipt(receiptUrl);
        });

        // Create an ImageView for the profile vector asset
        ImageView profileImage = new ImageView(this);
        profileImage.setImageResource(R.drawable.person);
        profileImage.setLayoutParams(new LinearLayout.LayoutParams(48, 48));

        // Create a TextView for the student name
        TextView studentNameTextView = new TextView(this);
        studentNameTextView.setText(studentName);

        // Add the ImageView and TextView to the student tab LinearLayout
        studentTab.addView(profileImage);
        studentTab.addView(studentNameTextView);

        // Add the student tab to the layout
        studentsLayout.addView(studentTab);
    }

    private void openReceipt(String receiptUrl) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(receiptUrl), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No PDF viewer installed", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearStudentTabs() {
        studentsLayout.removeAllViews();
    }
}
