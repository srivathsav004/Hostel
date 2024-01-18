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
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class StudentLoginActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button btnLogin, back;
    private TextView studentSignupTextView;

    private DatabaseReference studentRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        btnLogin = findViewById(R.id.btnLogin);
        back = findViewById(R.id.back);
        studentSignupTextView = findViewById(R.id.StudentSignup);

        studentRef = FirebaseDatabase.getInstance().getReference();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentLoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the entered username and password
                final String username = editTextUsername.getText().toString().trim();
                final String password = editTextPassword.getText().toString().trim();
                studentRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot hostelSnapshot : dataSnapshot.getChildren()) {
                                String hostelNode = hostelSnapshot.getKey();
                                if (hostelNode != null && hostelNode.startsWith("student")) {
                                    checkCredentialsInHostel(hostelNode, username, password);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(StudentLoginActivity.this, "Error checking credentials", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        studentSignupTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signupIntent = new Intent(StudentLoginActivity.this, StudentSignupActivity.class);
                signupIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(signupIntent);
            }
        });
    }
    private void checkCredentialsInHostel(final String hostelNode, final String username, final String password) {
        studentRef.child(hostelNode).orderByChild("username").equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                                StudentModel studentModel = studentSnapshot.getValue(StudentModel.class);
                                if (studentModel != null && studentModel.password.equals(password)) {
                                    redirectToStudentPage(username, hostelNode);
                                    return;
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(StudentLoginActivity.this, "Error checking credentials", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void redirectToStudentPage(String username, String hostelNode) {
        Class<?> studentPageClass = getStudentPageClassForHostel(hostelNode);

        if (studentPageClass != null) {
            Intent studentPageIntent = new Intent(StudentLoginActivity.this, studentPageClass);
            studentPageIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(studentPageIntent);
        } else {
            Toast.makeText(StudentLoginActivity.this, "Invalid hostel value: " + hostelNode, Toast.LENGTH_SHORT).show();
        }
    }
    private Class<?> getStudentPageClassForHostel(String hostelNode) {
        switch (hostelNode) {
            case "studentA":
                return StudentPageA.class;
            case "studentB":
                return StudentPageB.class;
            case "studentC":
                return StudentPageC.class;
            case "studentD":
                return StudentPageD.class;
            default:
                return null;
        }
    }
}
