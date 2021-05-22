package com.example.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

/**
 *
 */

public class Register extends AppCompatActivity {

    EditText name, phone, password, email;
    Button submit;
    DatabaseReference ref;
    FirebaseAuth fAuth;
    TextView textView,register;
    ProgressDialog loadingbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        submit = findViewById(R.id.button);
        register=findViewById(R.id.registered); 
        fAuth = FirebaseAuth.getInstance();
        loadingbar=new ProgressDialog(this);
        ref=FirebaseDatabase.getInstance().getReference();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),loginpage.class));
            }
        });

        if (fAuth.getCurrentUser() != null) {
            Intent mainIntent=new Intent(Register.this,MainActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mainIntent);
            finish();
        }


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String pass = password.getText().toString().trim();
                final String email1 = email.getText().toString().trim();

                if (TextUtils.isEmpty(email1)) {
                    email.setError("Email required");
                    return;
                }

                if (TextUtils.isEmpty(pass)) {
                    password.setError("Password is required");
                    return;
                }

                loadingbar.setTitle("Sign In");
                loadingbar.setMessage("Please Wait...");
                loadingbar.show();


                fAuth.createUserWithEmailAndPassword(email1,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loadingbar.dismiss();
                            String userid=fAuth.getCurrentUser().getUid();
                            ref.child("users").child(userid).setValue("null");
                            Toast.makeText(Register.this, "Succesfully created account", Toast.LENGTH_SHORT).show();
                            Intent mainIntent=new Intent(Register.this,MainActivity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(mainIntent);
                            finish();

                        } else {
                            loadingbar.dismiss();
                            Toast.makeText(Register.this, "Error"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });













            }
        });

    }
}