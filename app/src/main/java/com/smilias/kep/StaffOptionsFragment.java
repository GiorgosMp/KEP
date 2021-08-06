package com.smilias.kep;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smilias.kep.databinding.FragmentStaffOptionsBinding;

import androidx.fragment.app.Fragment;


public class StaffOptionsFragment extends Fragment {
    private FragmentStaffOptionsBinding binding;

    public StaffOptionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStaffOptionsBinding.inflate(inflater, container, false);
        binding.btnSignOut2.setOnClickListener(view -> {
            System.exit(0);
        });
        View view = binding.getRoot();
        return view;
    }
}