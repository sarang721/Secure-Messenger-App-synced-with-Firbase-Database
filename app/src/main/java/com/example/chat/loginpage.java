package com.example.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class loginpage extends AppCompatActivity {
    TextView phone, pass;
    TextView sign,create;
    FirebaseAuth auth;
    ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_loginpage);

        phone = findViewById(R.id.em);
        pass = findViewById(R.id.pas);
        create=findViewById(R.id.createa);
        auth=FirebaseAuth.getInstance();
        loadingbar=new ProgressDialog(this);


        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Register.class));
            }
        });


    }

    public void userlog(View view) {
        final String phonee = phone.getText().toString().trim();
        final String pass1 = pass.getText().toString().trim();
        if(TextUtils.isEmpty(phonee))
        {
            phone.setError("Phone Number is required");
            return ;
        }
        if(TextUtils.isEmpty(pass1))
        {
            pass.setError("Password is required");
            return ;

        }
        loadingbar.setTitle("Log IN");
        loadingbar.setMessage("Logging in please wait...");
        loadingbar.show();

        auth.signInWithEmailAndPassword(phonee,pass1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    loadingbar.dismiss();
                    Toast.makeText(loginpage.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                    Intent mainIntent=new Intent(loginpage.this,MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(mainIntent);
                    finish();
                }
                else
                {
                    loadingbar.dismiss();
                    Toast.makeText(loginpage.this, "Error !"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


        Query checkuser = FirebaseDatabase.getInstance().getReference().orderByChild("phone").equalTo(phonee);
        checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    phone.setError(null);
                    String passwordd = dataSnapshot.child(phonee).child("password").getValue(String.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}