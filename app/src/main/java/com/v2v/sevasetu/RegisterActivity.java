package com.v2v.sevasetu;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.v2v.sevasetu.databinding.ActivityRegisterBinding;
import com.v2v.sevasetu.LoginActivity;

public class RegisterActivity extends AppCompatActivity {

    // ViewBinding object for accessing views in activity_register.xml
    ActivityRegisterBinding binding;

    // Firebase Authentication instance
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the binding object for the register screen
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();

        // Handle Register button click
        binding.signUpButton.setOnClickListener(v -> {
            // Get email and password from input fields
            String email = binding.emailEditText.getText().toString().trim();
            String password = binding.passwordEditText.getText().toString().trim();

            // Validate email
            if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.emailEditText.setError("Enter valid email");
                return;
            }

            // Validate password
            if (TextUtils.isEmpty(password) || password.length() < 6) {
                binding.passwordEditText.setError("Password must be at least 6 characters");
                return;
            }

            // Create a new user in Firebase with email and password
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Registration success
                            Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();

                            firebaseAuth.signOut();

                            // Redirect to Login screen after registration
                            startActivity(new Intent(this, LoginActivity.class));
                            finish(); // Finish current activity so user can't go back

                        } else {
                            // Handle errors
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                // If email already exists
                                Toast.makeText(this, "Email already registered", Toast.LENGTH_SHORT).show();
                            } else {
                                // Other errors
                                Toast.makeText(this, "Registration failed: "
                                        + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        });

        // Handle "Already have account? Login" redirection
        binding.loginRedirect.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
        });
    }
}