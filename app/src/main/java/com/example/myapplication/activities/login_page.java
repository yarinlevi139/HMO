package com.example.myapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login_page extends AppCompatActivity {
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mAuth = FirebaseAuth.getInstance();
        EditText lg_box = findViewById(R.id.email);
        EditText pass_box = findViewById(R.id.password);

        Button sign_up_btn = findViewById(R.id.login);

        Button back_btn = findViewById(R.id.backbtnlg);
        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = lg_box.getText().toString();
                String password = pass_box.getText().toString();
                signIn(email, password);
            }
        });
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n = new Intent(login_page.this, main_activity.class);
                startActivity(n);
            }
        });

    }

    private void signIn(String email, String password) {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(login_page.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(login_page.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(login_page.this, "Authentication successful.", Toast.LENGTH_SHORT).show();

                            // Start another activity or perform further actions
                            if (email.contains("@myhealth")) {
                                Intent intent = new Intent(login_page.this, main_page_doctor.class);
                                intent.putExtra("Email", email); // Pass the email
                                startActivity(intent);
                                finish(); // Finish LoginActivity so the user cannot go back to it using the back button
                            }
                            else
                            {
                                Intent intent = new Intent(login_page.this, main_page_client.class);
                                intent.putExtra("Email", email); // Pass the email
                                startActivity(intent);
                                finish(); // Finish LoginActivity so the user cannot go back to it using the back button
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(login_page.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
