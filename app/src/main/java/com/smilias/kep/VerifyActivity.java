package com.smilias.kep;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class VerifyActivity extends AppCompatActivity {
    private static final String TAG = "VerifyActivity";
    private FirebaseAuth mAuth;

    public void verify(View View) {
        FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(VerifyActivity.this, "getString(R.string.vemail_sent)", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Email sent.");
                            Intent intent = new Intent(VerifyActivity.this, LogInActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }

    public void already(View view) {
        Intent intent = new Intent(VerifyActivity.this, LogInActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        mAuth = FirebaseAuth.getInstance();
    }
}