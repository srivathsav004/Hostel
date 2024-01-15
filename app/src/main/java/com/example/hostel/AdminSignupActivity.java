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

public class AdminSignupActivity extends AppCompatActivity {
    private Button nextButton;
    private EditText editTextNameAdmin, editTextUsernameAdmin, editTextPasswordAdmin, editTextHostel;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_signup);

        databaseReference = FirebaseDatabase.getInstance().getReference("adminData"); // Change "adminData" to your desired Firebase database reference

        editTextNameAdmin = findViewById(R.id.editTextNameAdmin);
        editTextUsernameAdmin = findViewById(R.id.editTextUsernameAdmin);
        editTextPasswordAdmin = findViewById(R.id.editTextPasswordAdmin);
        editTextHostel = findViewById(R.id.editTextHostel);
        nextButton = findViewById(R.id.btnNextAdmin);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidHostel()) {
                    if (storeAdminDetails()) {
                        startActivity(new Intent(AdminSignupActivity.this, AdminLoginActivity.class));
                        finish();
                    } else {
                        Toast.makeText(AdminSignupActivity.this, "Failed to add credentials. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AdminSignupActivity.this, "Enter a valid hostel (A/B/C/D/a/b/c/d)", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isValidHostel() {
        String hostel = editTextHostel.getText().toString().trim();
        // Validate the hostel against your allowed hostels
        return (hostel.length() == 1 && "ABCDabcd".contains(hostel));
    }

    private boolean storeAdminDetails() {
        String name = editTextNameAdmin.getText().toString().trim();
        String username = editTextUsernameAdmin.getText().toString().trim();
        String password = editTextPasswordAdmin.getText().toString().trim();
        String hostel = editTextHostel.getText().toString().trim();

        // Create a unique key for each admin
        String adminId = databaseReference.push().getKey();

        AdminModel adminModel = new AdminModel(username, name, password, hostel);

        // Store admin details in the database
        databaseReference.child(adminId).setValue(adminModel);

        Toast.makeText(AdminSignupActivity.this, "Credentials added successfully", Toast.LENGTH_SHORT).show();

        return true;
    }
}
