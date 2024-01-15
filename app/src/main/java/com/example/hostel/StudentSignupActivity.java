package com.example.hostel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashSet;
import java.util.Set;

public class StudentSignupActivity extends AppCompatActivity {

    private EditText nameEditText, emailEditText, regNoEditText, usernameEditText, passwordEditText, hostelEditText;
    private Button nextButton;

    private final Set<String> allowedHostels = new HashSet<String>() {{
        add("A");
        add("B");
        add("C");
        add("D");
        add("a");
        add("b");
        add("c");
        add("d");
    }};

    private DatabaseReference studentRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_signup);

        // Initialize Firebase Realtime Database reference
        studentRef = FirebaseDatabase.getInstance().getReference("studentData");

        nameEditText = findViewById(R.id.editTextNameStudent);
        emailEditText = findViewById(R.id.editTextEmailStudent);
        regNoEditText = findViewById(R.id.RegNo);
        usernameEditText = findViewById(R.id.editTextUsernameStudent);
        passwordEditText = findViewById(R.id.editTextPasswordStudent);
        hostelEditText = findViewById(R.id.editTextHostelStudent);
        nextButton = findViewById(R.id.btnNextStudent);

        // Set click listener for the "Next" button
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidHostel()) {
                    if (storeStudentDetails()) {
                        startActivity(new Intent(StudentSignupActivity.this, StudentLoginActivity.class));
                        finish();
                    } else {
                        Toast.makeText(StudentSignupActivity.this, "Failed to add credentials. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(StudentSignupActivity.this, "Enter a valid hostel (A/B/C/D/a/b/c/d)", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isValidHostel() {
        String hostel = hostelEditText.getText().toString().trim().toLowerCase();
        return allowedHostels.contains(hostel);
    }

    private boolean storeStudentDetails() {
        String name = nameEditText.getText().toString().trim();
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String registrationNumber = regNoEditText.getText().toString().trim();
        String hostel = hostelEditText.getText().toString().trim();

        // Create a unique key for the new student
        String studentId = studentRef.push().getKey();

        // Create a StudentModel object
        StudentModel studentModel = new StudentModel(username, name, email, registrationNumber, password, hostel);

        // Store the new student data in Firebase Realtime Database
        studentRef.child(studentId).setValue(studentModel);

        return true;  // Modify this as needed based on your requirements
    }
}
