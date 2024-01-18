package com.example.hostel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StudentDataA extends AppCompatActivity {

    private LinearLayout studentsLayout;
    private DatabaseReference studentsReference;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_data_a);

        studentsLayout = findViewById(R.id.noticesLayout);
        studentsReference = FirebaseDatabase.getInstance().getReference("studentA"); // Change to "studentA" node

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Students...");
        progressDialog.setCancelable(false);

        loadStudents();
    }

    private void loadStudents() {
        progressDialog.show();

        studentsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                studentsLayout.removeAllViews();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    StudentModel student = snapshot.getValue(StudentModel.class);
                    if (student != null) {
                        addStudentToLayout(student);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                Toast.makeText(StudentDataA.this, "Error loading students", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addStudentToLayout(StudentModel student) {
        View studentView = LayoutInflater.from(this).inflate(R.layout.studentdata, null);
        ImageView profileImage = studentView.findViewById(R.id.profileImage);
        TextView nameTextView = studentView.findViewById(R.id.nameTextView);
        TextView roomNumberTextView = studentView.findViewById(R.id.roomNumberTextView);
        TextView emailTextView = studentView.findViewById(R.id.emailTextView);
        TextView usernameTextView = studentView.findViewById(R.id.usernameTextView);
        TextView departmentTextView = studentView.findViewById(R.id.departmentTextView);

        nameTextView.setText("Name: "+student.getName());
        roomNumberTextView.setText("Room: " + student.getRoomNumber());
        emailTextView.setText("Email: " + student.getEmail());
        usernameTextView.setText("Username: " + student.getUsername());
        departmentTextView.setText("Department: " + student.getDepartment());

        // Load profile image using Glide (replace with your logic)
        Glide.with(this)
                .load(student.getProfileImageUrl()) // Replace with the actual field in your Student class
                .placeholder(R.drawable.person) // Placeholder image if profile image is not available
                .into(profileImage);

        studentsLayout.addView(studentView);
    }
}
