package com.example.hostel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminLoginActivity extends AppCompatActivity {

    private TextView adminSignupTextView;
    private Button btnLogin;
    private EditText usernameEditText, passwordEditText;

    private DatabaseReference adminRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        adminSignupTextView = findViewById(R.id.AdminSignup);
        btnLogin = findViewById(R.id.btnLogin);
        usernameEditText = findViewById(R.id.editTextUsername);
        passwordEditText = findViewById(R.id.editTextPassword);

        adminRef = FirebaseDatabase.getInstance().getReference("adminData");

        adminSignupTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signupIntent = new Intent(AdminLoginActivity.this, AdminSignupActivity.class);
                signupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(signupIntent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (isValidInput(username, password)) {
                    checkCredentials(username, password);
                } else {
                    Toast.makeText(AdminLoginActivity.this, "Please enter valid credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isValidInput(String username, String password) {
        return !username.isEmpty() && !password.isEmpty();
    }

    private void checkCredentials(final String username, final String password) {
        adminRef.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot adminSnapshot : dataSnapshot.getChildren()) {
                        AdminModel adminModel = adminSnapshot.getValue(AdminModel.class);
                        if (adminModel != null && adminModel.password.equals(password)) {
                            redirectToRespectiveAdminPage(username, adminModel.hostel);
                            return;
                        }
                    }
                }
                Toast.makeText(AdminLoginActivity.this, "Invalid credentials. Please try again.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AdminLoginActivity.this, "Error checking credentials", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void redirectToRespectiveAdminPage(String username, String hostel) {
        Class<?> adminPageClass = getAdminPageClassForHostel(hostel);

        if (adminPageClass != null) {
            Intent adminPageIntent = new Intent(AdminLoginActivity.this, adminPageClass);
            adminPageIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(adminPageIntent);
        } else {
            Toast.makeText(AdminLoginActivity.this, "Invalid hostel value: " + hostel, Toast.LENGTH_SHORT).show();
        }
    }

    private Class<?> getAdminPageClassForHostel(String hostel) {
        switch (hostel) {
            case "A":
                return AdminPageA.class;
            case "B":
                return AdminPageB.class;
            case "C":
                return AdminPageC.class;
            case "D":
                return AdminPageD.class;
            default:
                return null;
        }
    }
}
