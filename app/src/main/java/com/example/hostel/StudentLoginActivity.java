package com.example.hostel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StudentLoginActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button btnLogin;
    private TextView studentSignupTextView;

    private DatabaseReference studentRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

        // Initialize views
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        btnLogin = findViewById(R.id.btnLogin);
        studentSignupTextView = findViewById(R.id.StudentSignup);

        studentRef = FirebaseDatabase.getInstance().getReference("studentData");

        // Set click listener for the "Login" button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the entered username and password
                final String username = editTextUsername.getText().toString().trim();
                final String password = editTextPassword.getText().toString().trim();

                // Check credentials using Firebase Realtime Database
                studentRef.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                                StudentModel studentModel = studentSnapshot.getValue(StudentModel.class);
                                if (studentModel != null && studentModel.password.equals(password)) {
                                    redirectToStudentPage(username, studentModel.hostel);
                                    return;
                                }
                            }
                        }
                        // Credentials are incorrect, show a toast or perform additional actions
                        Toast.makeText(StudentLoginActivity.this, "Invalid credentials. Please try again.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(StudentLoginActivity.this, "Error checking credentials", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // Set click listener for the "Sign Up" TextView
        studentSignupTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open Studentsignupactivity when "Sign Up" TextView is clicked
                Intent signupIntent = new Intent(StudentLoginActivity.this, StudentSignupActivity.class);

                // Clear the back stack and start the new activity
                signupIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(signupIntent);
            }
        });
    }

    // Method to redirect to the student's page based on hostel
    private void redirectToStudentPage(String username, String hostel) {
        Class<?> studentPageClass = getStudentPageClassForHostel(hostel);

        if (studentPageClass != null) {
            Intent studentPageIntent = new Intent(StudentLoginActivity.this, studentPageClass);
            studentPageIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(studentPageIntent);
        } else {
            Toast.makeText(StudentLoginActivity.this, "Invalid hostel value: " + hostel, Toast.LENGTH_SHORT).show();
        }
    }

    // Method to get the class for a given hostel
    private Class<?> getStudentPageClassForHostel(String hostel) {
        switch (hostel) {
            case "A":
                return StudentPageA.class;
            case "B":
                return StudentPageB.class;
            case "C":
                return StudentPageC.class;
            case "D":
                return StudentPageD.class;
            default:
                return null;
        }
    }
}
