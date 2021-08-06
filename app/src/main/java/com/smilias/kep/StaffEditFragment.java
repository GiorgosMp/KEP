package com.smilias.kep;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smilias.kep.databinding.FragmentStaffEditBinding;
import com.smilias.kep.model.Citizen;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;


public class StaffEditFragment extends Fragment {


    private FragmentStaffEditBinding binding;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private Citizen citizen;
    private String searchedUserUID;
    private SignUpViewModel viewModel;

    public StaffEditFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStaffEditBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        viewModel = ViewModelProviders.of(this).get(SignUpViewModel.class);


        binding.btnSearchUser.setOnClickListener(v -> {
            searchUser();
        });

        binding.btnEditUser.setOnClickListener(v -> {
            editUser();
        });


        return view;
    }

    private void editUser() {
        citizen.setFirstName(binding.editFirstName.getText().toString().toUpperCase());
        citizen.setLastName(binding.editLastName.getText().toString().toUpperCase());
        citizen.setFatherName(binding.editFatherName.getText().toString().toUpperCase());
        citizen.setMotherName(binding.editMotherName.getText().toString().toUpperCase());
        citizen.setId(binding.editIdNumber.getText().toString());
        citizen.setBirthDate(binding.editTextDate.getText().toString());
        citizen.setAmka(binding.editAMKAnumber.getText().toString());
        citizen.setTaxNumber(binding.editTaxNumber.getText().toString());
        citizen.setAddress(binding.editAddress.getText().toString().toUpperCase());
        correctCredits();

    }

    private void correctCredits() {
        String result = viewModel.editUserCorrectCredits(citizen);

        if (result.equals("go")) {
            myRef.setValue(citizen);
            myRef.child("UID").setValue(searchedUserUID);
        } else {
            switch (result) {
                case "firstName":
                    binding.editFirstName.setError("Name is required");
                    binding.editFirstName.requestFocus();
                    break;
                case "lastName":
                    binding.editLastName.setError("Lastname is required");
                    binding.editLastName.requestFocus();
                    break;
                case "fatherName":
                    binding.editFatherName.setError("Father's name is required");
                    binding.editFatherName.requestFocus();
                    break;
                case "motherName":
                    binding.editMotherName.setError("Mother's name is required");
                    binding.editMotherName.requestFocus();
                    break;
                case "birthDate":
                    binding.editTextDate.setError("Correct birth date is required");
                    binding.editTextDate.requestFocus();
                    break;
                case "id":
                    binding.editIdNumber.setError("Id is required");
                    binding.editIdNumber.requestFocus();
                    break;
                case "amka":
                    binding.editAMKAnumber.setError("Correct AMKA is required");
                    binding.editAMKAnumber.requestFocus();
                    break;
                case "taxNumber":
                    binding.editTaxNumber.setError("Correct TAX number is required");
                    binding.editTaxNumber.requestFocus();
                    break;
                case "address":
                    binding.editAddress.setError("Address is required");
                    binding.editAddress.requestFocus();
                    break;
                default:
                    break;
            }
        }
    }

    private void searchUser() {
        textViewsSetVisibilityFalse();
        String usernameToSearch = null;
        try {
            usernameToSearch = binding.editTextSearchUser.getText().toString().toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users/" + usernameToSearch);
        createCitizen();
    }

    private void createCitizen() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                citizen = dataSnapshot.getValue(Citizen.class);
                searchedUserUID = dataSnapshot.child("UID").getValue(String.class);
                try {
                    if (citizen.getFirstName() != null) {
                        textViewsSetVisibilityTrue();
                        textViewsSetText();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

    private void textViewsSetText() {
        binding.editFirstName.setText(citizen.getFirstName());
        binding.editLastName.setText(citizen.getLastName());
        binding.editFatherName.setText(citizen.getFatherName());
        binding.editMotherName.setText(citizen.getMotherName());
        binding.editIdNumber.setText(citizen.getId());
        binding.editTextDate.setText(citizen.getBirthDate());
        binding.editAMKAnumber.setText(citizen.getAmka());
        binding.editTaxNumber.setText(citizen.getTaxNumber());
        binding.editAddress.setText(citizen.getAddress());
    }

    private void textViewsSetVisibilityTrue() {
        binding.editFirstName.setVisibility(View.VISIBLE);
        binding.editLastName.setVisibility(View.VISIBLE);
        binding.editFatherName.setVisibility(View.VISIBLE);
        binding.editMotherName.setVisibility(View.VISIBLE);
        binding.editIdNumber.setVisibility(View.VISIBLE);
        binding.editTextDate.setVisibility(View.VISIBLE);
        binding.editAMKAnumber.setVisibility(View.VISIBLE);
        binding.editTaxNumber.setVisibility(View.VISIBLE);
        binding.editAddress.setVisibility(View.VISIBLE);
        binding.btnEditUser.setVisibility(View.VISIBLE);

        binding.txtFirstName.setVisibility(View.VISIBLE);
        binding.txtLastName.setVisibility(View.VISIBLE);
        binding.txtFatherName.setVisibility(View.VISIBLE);
        binding.txtMotherName.setVisibility(View.VISIBLE);
        binding.txtID.setVisibility(View.VISIBLE);
        binding.txtDate.setVisibility(View.VISIBLE);
        binding.txtAMKA.setVisibility(View.VISIBLE);
        binding.txtTaxNumber.setVisibility(View.VISIBLE);
        binding.txtAddress.setVisibility(View.VISIBLE);
    }

    private void textViewsSetVisibilityFalse() {
        binding.editFirstName.setVisibility(View.GONE);
        binding.editLastName.setVisibility(View.GONE);
        binding.editFatherName.setVisibility(View.GONE);
        binding.editMotherName.setVisibility(View.GONE);
        binding.editIdNumber.setVisibility(View.GONE);
        binding.editTextDate.setVisibility(View.GONE);
        binding.editAMKAnumber.setVisibility(View.GONE);
        binding.editTaxNumber.setVisibility(View.GONE);
        binding.editAddress.setVisibility(View.GONE);

        binding.btnEditUser.setVisibility(View.GONE);
        binding.txtFirstName.setVisibility(View.GONE);
        binding.txtLastName.setVisibility(View.GONE);
        binding.txtFatherName.setVisibility(View.GONE);
        binding.txtMotherName.setVisibility(View.GONE);
        binding.txtID.setVisibility(View.GONE);
        binding.txtDate.setVisibility(View.GONE);
        binding.txtAMKA.setVisibility(View.GONE);
        binding.txtTaxNumber.setVisibility(View.GONE);
        binding.txtAddress.setVisibility(View.GONE);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}