package com.smilias.kep;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smilias.kep.databinding.ActivitySignUpBinding;
import com.smilias.kep.model.Citizen;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import static android.content.ContentValues.TAG;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignUpBinding binding;
    private SignUpViewModel viewModel;
    private String firstName;
    private String lastName;
    private String fatherName;
    private String motherName;
    private String birthDate;
    private String id;
    private String amka;
    private String taxNumber;
    private String address;
    private String email;
    private String password;
    private String username;
    private Citizen citizen;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef, myRef2;
    private List currentUsernames;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        viewModel = ViewModelProviders.of(this).get(SignUpViewModel.class);

        database = FirebaseDatabase.getInstance();
        currentUsernames = new ArrayList<>();
        usernameExists();
        mAuth = FirebaseAuth.getInstance();

        binding.btnSignUp.setOnClickListener(view1 -> btnOnclick());
    }

    private void initialisation() {
        username = binding.editUsername.getText().toString().toUpperCase();
        firstName = binding.editFirstName.getText().toString().toUpperCase();
        lastName = binding.editLastName.getText().toString().toUpperCase();
        fatherName = binding.editFatherName.getText().toString().toUpperCase();
        motherName = binding.editMotherName.getText().toString().toUpperCase();
        birthDate = binding.editTextDate.getText().toString();
        id = binding.editIdNumber.getText().toString();
        amka = binding.editAMKAnumber.getText().toString();
        taxNumber = binding.editTaxNumber.getText().toString();
        address = binding.editAddress.getText().toString().toUpperCase();
        email = binding.editEmail.getText().toString();
        password = binding.editPassword.getText().toString();
        citizen = new Citizen(username, firstName, lastName, fatherName, motherName, birthDate, id, amka, taxNumber, address, email);
    }

    private void usernameExists() {
        myRef2 = database.getReference();
        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.child("Users").getChildren()) {
                    currentUsernames.add(snapshot.getKey());
                }


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

    private void btnOnclick() {
        initialisation();
        if (currentUsernames.contains(username)) {
            binding.editUsername.setError("Username already in use");
            binding.editUsername.requestFocus();
        } else {
            String result = viewModel.signUp(citizen, password, username);
            if (result.equals("go")) allInfoReady(mAuth);
            else {
                switch (result) {
                    case "username":
                        binding.editUsername.setError("Username is required");
                        binding.editUsername.requestFocus();
                        break;
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
                    case "email":
                        binding.editEmail.setError("Correct email is required");
                        binding.editEmail.requestFocus();
                        break;
                    case "password":
                        binding.editPassword.setError("Password is required to be atleast 6 characters ");
                        binding.editPassword.requestFocus();
                        break;
                    default:
                        break;
                }
            }
        }

    }

    private void allInfoReady(FirebaseAuth mAuth) {


        mAuth.createUserWithEmailAndPassword(citizen.getEmail(), password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            user.sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "Email sent.");
                                                Toast.makeText(SignUpActivity.this, "User created.", Toast.LENGTH_SHORT).show();
                                                myRef = FirebaseDatabase.getInstance().getReference("Users");
                                                myRef.child(citizen.getUsername()).setValue(citizen);
                                                myRef.child(citizen.getUsername()).child("UID").setValue(user.getUid());
                                                Intent intent = new Intent(SignUpActivity.this, LogInActivity.class);
                                                startActivity(intent);
                                                finish();

                                            }
                                        }
                                    });


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Something happened, try again.", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
}