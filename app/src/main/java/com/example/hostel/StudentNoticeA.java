package com.example.hostel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class StudentNoticeA extends AppCompatActivity {

    private LinearLayout noticesLayout;
    private DatabaseReference noticeAReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_notice_a);

        noticesLayout = findViewById(R.id.noticesLayout);
        noticeAReference = FirebaseDatabase.getInstance().getReference("NoticeA");

        loadNotices();
    }

    private void loadNotices() {
        noticeAReference.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                noticesLayout.removeAllViews();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    NoticeA notice = snapshot.getValue(NoticeA.class);
                    if (notice != null) {
                        addNoticeToLayout(notice);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
            }
        });
    }
    private void addNoticeToLayout(NoticeA notice) {
        View noticeView = LayoutInflater.from(this).inflate(R.layout.student_notice, null);
        ImageView noticeImage = noticeView.findViewById(R.id.noticeImage);
        TextView noticeTitle = noticeView.findViewById(R.id.noticeTitle);
        TextView noticeDateTime = noticeView.findViewById(R.id.noticeDateTime);

        noticeTitle.setText("Title: " + notice.getTitle());
        noticeDateTime.setText("Uploaded on: " + notice.getDateTime()); // Assuming getDateTime() is the method in NoticeA class to retrieve date and time
        Glide.with(this)
                .load(notice.getImageUrl())
                .into(noticeImage);

        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, getResources().getDimensionPixelSize(R.dimen.notice_spacing), 0, 0);
        noticeView.setLayoutParams(layoutParams);

        noticesLayout.addView(noticeView);
    }
}
