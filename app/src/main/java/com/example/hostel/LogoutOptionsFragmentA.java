package com.example.hostel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class LogoutOptionsFragmentA extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_logout_options_a, container, false);

        // Handle the logout button click
        Button btnLogout = view.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutConfirmationFragment();
            }
        });

        return view;
    }

    private void showLogoutConfirmationFragment() {
        LogoutConfirmationFragmentA fragment = new LogoutConfirmationFragmentA();
        fragment.setTargetFragment(this, 0); // Set the target fragment to handle onActivityResult
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, fragment, fragment.getClass().getSimpleName())
                .addToBackStack(null)
                .commit();
    }
}
