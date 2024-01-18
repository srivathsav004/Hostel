package com.example.hostel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
public class StudentLogoutFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_logout_confirmation_a, container, false);

        // Handle the logout confirmation buttons click
        Button btnYes = view.findViewById(R.id.btnYes);
        Button btnNo = view.findViewById(R.id.btnNo);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("LogoutConfirmation", "Yes button clicked");
                Intent intent = new Intent(requireActivity(), StudentLoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                requireActivity().finish();
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement the logic to dismiss the fragment
                dismissFragment();
            }
        });

        return view;
    }

    // Dismiss the fragment and pop the back stack
    private void dismissFragment() {
        // Use popBackStack() without the tag to remove the last fragment
        requireActivity().getSupportFragmentManager().popBackStack();
    }
}
