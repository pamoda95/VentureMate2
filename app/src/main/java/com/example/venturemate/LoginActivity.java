package com.example.venturemate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.zip.Inflater;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText email, password;
    private TextView notHaveAccnt;
    private Button login_btn;
    private FirebaseAuth dbAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar ac = getSupportActionBar();
        ac.setTitle("Login");

        ac.setDisplayHomeAsUpEnabled(true);
        ac.setDisplayShowHomeEnabled(true);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        notHaveAccnt = findViewById(R.id.notHaveAccnt);
        login_btn = findViewById(R.id.login_btn);

        dbAuth = FirebaseAuth.getInstance();

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dbemail = email.getText().toString().trim();
                String dbpassword = password.getText().toString().trim();

                if(!Patterns.EMAIL_ADDRESS.matcher(dbemail).matches()){
                    email.setError("Invalid Email");
                    email.setFocusable(true);
                } else if(dbpassword.length()<6){
                    password.setError("Password Length at least 6 characters");
                    password.setFocusable(true);
                } else {
                    loginUser(dbemail, dbpassword);
                }
            }
        });

        notHaveAccnt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private void loginUser(String dbemail, String dbpassword) {
        dbAuth.signInWithEmailAndPassword(dbemail, dbpassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = dbAuth.getCurrentUser();

                            startActivity(new Intent(LoginActivity.this, CategorySelection.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

}

