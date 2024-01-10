package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class signup_page extends AppCompatActivity {

    private FirebaseAuth mAuth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        mAuth = FirebaseAuth.getInstance();

        Button sign_up_btn = findViewById(R.id.signup);

//        EditText first_name = findViewById(R.id.first_name);

        EditText lg_box = findViewById(R.id.email);

        EditText pass_box = findViewById(R.id.password);

        EditText confirm_pass = findViewById(R.id.confirm_password);

        Button back_btn = findViewById(R.id.backbtnsgn);
        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String name = first_name.getText().toString();
                String email = lg_box.getText().toString();
                String password = pass_box.getText().toString();
                String confirm_password = confirm_pass.getText().toString();

                if (email.isEmpty()) {
                    Toast.makeText(signup_page.this, "Empty email",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password.isEmpty())
                {
                    Toast.makeText(signup_page.this, "Empty password",Toast.LENGTH_SHORT).show();
                    return;

                }
                if(password.equals(confirm_password) && !email.contains("@myhealth")) {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Toast.makeText(signup_page.this, "Welcome!",
                                                Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(signup_page.this, create_client_profile.class);
                                        intent.putExtra("Email", email); // Pass the email
                                        startActivity(intent);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(signup_page.this, "Bad email or password",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else{
                    if(!password.equals(confirm_password))
                        Toast.makeText(signup_page.this,"Passwords do not match",Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(signup_page.this,"you cannot use that email",Toast.LENGTH_LONG).show();
                }
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n = new Intent(signup_page.this,MainActivity.class);
                startActivity(n);
            }
        });


    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
    }


}