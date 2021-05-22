package com.example.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class settings extends AppCompatActivity {

    EditText name,username;
    DatabaseReference ref;
    FirebaseAuth auth;
    Button update;
    Toolbar mtoolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        name=findViewById(R.id.settingname);

        username=findViewById(R.id.settingusername);
        update=findViewById(R.id.button12);
        ref= FirebaseDatabase.getInstance().getReference();
        auth=FirebaseAuth.getInstance();


        alreadydetailsfilled();


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name1=name.getText().toString().toUpperCase();
                String username1=username.getText().toString();
                String userid=auth.getCurrentUser().getUid();

                if(TextUtils.isEmpty(name1))
                {
                    name.setError("This is required");
                    return;
                }
                if(TextUtils.isEmpty(username1))
                {
                    username.setError("This is required");
                    return;
                }
                    HashMap<String,String> map=new HashMap<>();
                    map.put("name",name1);
                    map.put("username",username1);
                    map.put("userid",userid);
                    ref.child("users").child(userid).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    startActivity(new Intent(settings.this,MainActivity.class));
                                    Toast.makeText(settings.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    String eror=task.getException().toString();
                                    Toast.makeText(settings.this, "Error"+eror, Toast.LENGTH_SHORT).show();
                                }

                        }
                    });
                    String s=name1.toUpperCase();
                    ref.child("usernames").child(s).setValue(username1).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {

                            }
                        }
                    });

            }
        });
    }



    private void alreadydetailsfilled() {
        String user=auth.getCurrentUser().getUid();
        ref.child("users").child(user).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if((dataSnapshot.exists()) && (dataSnapshot.hasChild("name")) && (dataSnapshot.hasChild("username")))
                {
                    String retname=dataSnapshot.child("name").getValue().toString();
                    String retusername=dataSnapshot.child("username").getValue().toString();
                    name.setText(retname);
                    username.setText(retusername);

                }
                else
                {
                    Toast.makeText(settings.this, "Please enter your name and email", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}