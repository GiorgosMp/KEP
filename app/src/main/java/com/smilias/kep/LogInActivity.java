package com.smilias.kep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.smilias.kep.databinding.ActivityLogInBinding;
import com.smilias.kep.model.UserDetails;

import java.io.IOException;

public class LogInActivity extends AppCompatActivity {
    private static final String TAG = "";
    private ActivityLogInBinding binding;
    private FirebaseAuth mAuth;
    public static String username;
    private String email;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private DatabaseReference myRef2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLogInBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference("Users");
        email = "";

        binding.btnToSignUp.setOnClickListener(view1 -> {
            Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
            startActivity(intent);

        });

        binding.btnLogIn.setOnClickListener(view1 -> {
            username = binding.editTextTextPersonName.getText().toString().toUpperCase();
            myRef2 = database.getReference("OnlineStaff/" + username);
            myRef2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String comparedUser = dataSnapshot.child("UID").getValue(String.class);
                    myRef = FirebaseDatabase.getInstance().getReference("Users/" + username);
                    if (comparedUser != null) {
                        isStaff();
                    } else {
                        isNotStaff();
                    }

                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                }
            });

        });
        binding.btnForgetPassword.setOnClickListener(view1 -> {
            Intent intent = new Intent(LogInActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });
    }

    private void isNotStaff() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                email = dataSnapshot.child("email").getValue(String.class);
                try {
                    mAuth.signInWithEmailAndPassword(email,
                            binding.editTextTextPassword.getText().toString())
                            .addOnCompleteListener(LogInActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithEmail:success");
                                        Toast.makeText(LogInActivity.this, "Logged in", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                                        finish();
                                        startActivity(intent);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(LogInActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } catch (Throwable e) {
                    e.printStackTrace();
                    Toast.makeText(LogInActivity.this, "Wrong username.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

    private void isStaff() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                email = dataSnapshot.child("email").getValue(String.class);
                try {
                    mAuth.signInWithEmailAndPassword(email,
                            binding.editTextTextPassword.getText().toString())
                            .addOnCompleteListener(LogInActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithEmail:success");
                                        Toast.makeText(LogInActivity.this, "Logged in", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LogInActivity.this, StaffMainActivity.class);
                                        finish();
                                        startActivity(intent);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(LogInActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } catch (Throwable e) {
                    e.printStackTrace();
                    Toast.makeText(LogInActivity.this, "Wrong username.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

}