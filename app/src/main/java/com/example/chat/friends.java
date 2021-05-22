package com.example.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class friends extends AppCompatActivity {
    RecyclerView myrecycler;
    DatabaseReference ref,userref;
    FirebaseAuth auth;
    String userid;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        myrecycler=findViewById(R.id.recyclerfriends);
        myrecycler.setLayoutManager(new LinearLayoutManager(this));
        auth=FirebaseAuth.getInstance();
        userid=auth.getCurrentUser().getUid();
        userref=FirebaseDatabase.getInstance().getReference().child("users");
        ref= FirebaseDatabase.getInstance().getReference().child("friends").child(userid);
        toolbar=findViewById(R.id.toolbarfriends);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Friends");


    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<post> options=new FirebaseRecyclerOptions.Builder<post>()
                .setQuery(ref,post.class)
                .build();

        FirebaseRecyclerAdapter<post,friendsviewholder> adapter=new FirebaseRecyclerAdapter<post, friendsviewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final friendsviewholder friendsviewholder, int i, @NonNull post post) {
                final String userids=getRef(i).getKey();
                userref.child(userids).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final String retname=dataSnapshot.child("name").getValue().toString();
                        friendsviewholder.name.setText(retname);
                        friendsviewholder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent profile=new Intent(friends.this,personalchats.class);
                                profile.putExtra("userid",userids);
                                profile.putExtra("retname",retname);
                                startActivity(profile);



                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @NonNull
            @Override
            public friendsviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layoutfriends, parent, false);

                return new friendsviewholder(view);
            }


        };

        myrecycler.setAdapter(adapter);
        adapter.startListening();
    }



    static public class friendsviewholder extends RecyclerView.ViewHolder
    {
        TextView name,username;

        public friendsviewholder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.nameoffriend);
            username= itemView.findViewById(R.id.username1);
        }
    }

}
