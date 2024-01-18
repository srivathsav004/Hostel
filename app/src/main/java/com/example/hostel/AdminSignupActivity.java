package com.example.hostel;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminSignupActivity extends AppCompatActivity {

    private Button nextButton;
    private EditText editTextNameAdmin, editTextUsernameAdmin, passwordEditText, confirmPasswordEditText, editTextHostel;
    private DatabaseReference databaseReference;
    private ImageView togglePassword, toggleConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_signup);

        databaseReference = FirebaseDatabase.getInstance().getReference("adminData"); // Change "adminData" to your desired Firebase database reference

        editTextNameAdmin = findViewById(R.id.editTextNameAdmin);
        editTextUsernameAdmin = findViewById(R.id.editTextUsernameAdmin);
        passwordEditText = findViewById(R.id.editTextPasswordAdmin);
        editTextHostel = findViewById(R.id.editTextHostel);
        nextButton = findViewById(R.id.btnNextAdmin);
        confirmPasswordEditText = findViewById(R.id.editTextConfirmPasswordAdmin);

        togglePassword = findViewById(R.id.togglePassword);
        toggleConfirmPassword = findViewById(R.id.togglePassword1);
        TextView visitWebsiteTextView = findViewById(R.id.VisitWebsite);
        visitWebsiteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the website when the TextView is clicked
                String websiteUrl = "https://www.ptuniv.edu.in/";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl));
                startActivity(intent);
            }
        });

        togglePassword.setTag(R.drawable.eye);
        toggleConfirmPassword.setTag(R.drawable.eye);

        togglePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility(passwordEditText, togglePassword);
            }
        });

        toggleConfirmPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleConfirmPasswordVisibility(confirmPasswordEditText, toggleConfirmPassword);
            }
        });

        passwordEditText.addTextChangedListener(new PasswordTextWatcher(togglePassword));
        confirmPasswordEditText.addTextChangedListener(new PasswordTextWatcher(toggleConfirmPassword));

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidInput() && isValidHostel()) {
                    if (arePasswordsMatching()) {
                        if (storeAdminDetails()) {
                            startActivity(new Intent(AdminSignupActivity.this, AdminLoginActivity.class));
                            finish();
                        } else {
                            Toast.makeText(AdminSignupActivity.this, "Failed to add credentials. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(AdminSignupActivity.this, "Passwords do not match. Please enter matching passwords.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AdminSignupActivity.this, "Please fill in all fields and enter a valid hostel (A/B/C/D/a/b/c/d).", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isValidInput() {
        return !editTextNameAdmin.getText().toString().trim().isEmpty() &&
                !editTextUsernameAdmin.getText().toString().trim().isEmpty() &&
                !passwordEditText.getText().toString().trim().isEmpty() &&
                !confirmPasswordEditText.getText().toString().trim().isEmpty() &&
                !editTextHostel.getText().toString().trim().isEmpty();
    }

    private boolean arePasswordsMatching() {
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
        return password.equals(confirmPassword);
    }
    @Override
    public void onBackPressed() {
        showExitConfirmationDialog();
        super.onBackPressed();
    }
    private void showExitConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to leave? Any unsaved data will be lost.");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // User clicked "Yes", finish the activity
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // User clicked "No", dismiss the dialog
                dialogInterface.dismiss();
            }
        });

        // Show the dialog
        builder.create().show();
    }

    private void togglePasswordVisibility(EditText editText, ImageView toggleButton) {
        int inputType = editText.getInputType();
        int drawableId;

        if (inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_VARIATION_PASSWORD);
            drawableId = R.drawable.eye_with_slash;
        } else {
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            drawableId = R.drawable.eye;
        }

        // Set the new drawable and update the tag
        toggleButton.setImageResource(drawableId);
        toggleButton.setTag(drawableId);

        // Move the cursor to the end of the text
        editText.setSelection(editText.getText().length());
    }

    private void toggleConfirmPasswordVisibility(EditText editText, ImageView toggleButton) {
        int inputType = editText.getInputType();
        int drawableId;

        if (inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_VARIATION_PASSWORD);
            drawableId = R.drawable.eye_with_slash;
        } else {
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            drawableId = R.drawable.eye;
        }

        // Set the new drawable and update the tag
        toggleButton.setImageResource(drawableId);
        toggleButton.setTag(drawableId);

        // Move the cursor to the end of the text
        editText.setSelection(editText.getText().length());
    }

    private boolean isValidHostel() {
        String hostel = editTextHostel.getText().toString().trim();
        // Validate the hostel against your allowed hostels
        return (hostel.length() == 1 && "ABCDabcd".contains(hostel));
    }

    private boolean storeAdminDetails() {
        String name = editTextNameAdmin.getText().toString().trim();
        String username = editTextUsernameAdmin.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String hostel = editTextHostel.getText().toString().trim();

        // Create a unique key for each admin
        String adminId = databaseReference.push().getKey();

        AdminModel adminModel = new AdminModel(username, name, password, hostel);

        // Store admin details in the database
        databaseReference.child(adminId).setValue(adminModel);

        Toast.makeText(AdminSignupActivity.this, "Credentials added successfully", Toast.LENGTH_SHORT).show();

        return true;
    }

    private class PasswordTextWatcher implements TextWatcher {

        private ImageView toggleButton;

        PasswordTextWatcher(ImageView toggleButton) {
            this.toggleButton = toggleButton;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // Show/hide toggle button based on text length
            toggleButton.setVisibility(editable.length() > 0 ? View.VISIBLE : View.GONE);
        }
    }
}
