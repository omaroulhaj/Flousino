package com.example.flousino;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText mEmail;
    private EditText mPass;
    private Button btnLogin;
    private TextView mForget;
    private TextView mSignup;
    private ProgressBar mBar;
    // Firebase ...
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mAuth=FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser()!=null)
        {
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finish();
        }
        mBar= new ProgressBar(this);
        login();
    }
    private void login()
    {
        mEmail=findViewById(R.id.email_login);
        mPass=findViewById(R.id.password_login);
        btnLogin=findViewById(R.id.btn_login);
        mForget=findViewById(R.id.forget_password);
        mSignup=findViewById(R.id.signup_register);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String pass = mPass.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is required");
                    mEmail.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(pass)) {
                    mPass.setError("Password is required");
                    mPass.requestFocus();
                    return;
                }
                mBar.setVisibility(View.VISIBLE);
                mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            mBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(),"Login Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                            finish();
                        }
                        else {
                            mBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(),"Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        // Registration button click listener
        mSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
            }
        });
        mForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ResetActivity.class));
            }
        });
    }
}