package com.example.hostel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
public class AdminPageA extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page_a);

        // Add the LogoutOptionsFragmentA when the admin clicks on the logout button
        ImageView logoutButton = findViewById(R.id.logoutOptionsButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutOptionsFragment();
            }
        });

        ImageView notice=findViewById(R.id.notice);
        notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminPageA.this, NoticeUploadA.class);
                startActivity(intent);
            }
        });
        ImageView AdminFoodMenuA=findViewById(R.id.AdminFoodMenuA);
        AdminFoodMenuA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminPageA.this,AdminFoodMenuA.class);
                startActivity(intent);
            }
        });
        ImageView AdminGuidelinesA=findViewById(R.id.AdminGuidelinesA);
        AdminGuidelinesA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminPageA.this,AdminGuidelinesA.class);
                startActivity(intent);
            }
        });
        ImageView AdminFeeDetailsA=findViewById(R.id.AdminFeeDetailsA);
        AdminFeeDetailsA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminPageA.this,AdminFeeDetailsA.class);
                startActivity(intent);
            }
        });
    }

    private void showLogoutOptionsFragment() {
        LogoutOptionsFragmentA fragment = new LogoutOptionsFragmentA();

        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, fragment, fragment.getClass().getSimpleName())
                .addToBackStack(null)
                .commit();
    }

}