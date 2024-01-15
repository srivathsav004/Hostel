package com.example.hostel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private RadioGroup radioGroupRoles;
    private Button buttonNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        radioGroupRoles = findViewById(R.id.radioGroupRoles);
        buttonNext = findViewById(R.id.buttonNext);

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedId = radioGroupRoles.getCheckedRadioButtonId();
                RadioButton radioButton = findViewById(selectedId);

                if (radioButton != null) {
                    String selectedRole = radioButton.getText().toString();

                    // Start the appropriate login activity based on the selected role
                    if (selectedRole.equals("Student")) {
                        Intent studentIntent = new Intent(MainActivity.this, StudentLoginActivity.class);
                        startActivity(studentIntent);
                    } else if (selectedRole.equals("Admin")) {
                        Intent adminIntent = new Intent(MainActivity.this, AdminLoginActivity.class);
                        startActivity(adminIntent);
                    }
                }
            }
        });
    }
}
