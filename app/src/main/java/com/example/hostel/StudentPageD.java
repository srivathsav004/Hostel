package com.example.hostel;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;

public class StudentPageD extends AppCompatActivity {

    ImageView logoutOptionsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_page_d);

        // Corrected: Remove the data type ImageView from this line
        logoutOptionsButton = findViewById(R.id.logoutOptionsButton);

        logoutOptionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutConfirmation();
            }
        });
    }

    private void showLogoutConfirmation() {
        LogoutConfirmationFragmentA fragment = new LogoutConfirmationFragmentA();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(android.R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
