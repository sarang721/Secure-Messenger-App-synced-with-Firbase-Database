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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class postlist extends AppCompatActivity  {

    private RecyclerView recyclerview;
    private DatabaseReference ref;
    private  RecyclerView findfriendrecyclerlist;
    Toolbar toolbarop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postlist);
        findfriendrecyclerlist=findViewById(R.id.recycler);
        ref=FirebaseDatabase.getInstance().getReference().child("users");
        findfriendrecyclerlist.setLayoutManager(new LinearLayoutManager(this));
        toolbarop=findViewById(R.id.toolbarinvite);
        setSupportActionBar(toolbarop);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Find Friends");


    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<post> options =
                new FirebaseRecyclerOptions.Builder<post>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("users"), post.class)
                        .build();

        FirebaseRecyclerAdapter<post,Findfriendsviewholder> adapter=new FirebaseRecyclerAdapter<post, Findfriendsviewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull Findfriendsviewholder holder, final int i, @NonNull post post) {
                holder.name.setText(post.getName());
                holder.username.setText(post.getUsername());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String userid=getRef(i).getKey();
                        Intent profile=new Intent(postlist.this,profileactivity.class);
                        profile.putExtra("userid",userid);
                        startActivity(profile);
                    }
                });
            }

            @NonNull
            @Override
            public Findfriendsviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.post, parent, false);

                return new Findfriendsviewholder(view);
            }
        };
        findfriendrecyclerlist.setAdapter(adapter);

        adapter.startListening();
    }

    public static class Findfriendsviewholder extends RecyclerView.ViewHolder {

        TextView name,username;
        public Findfriendsviewholder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.nameoffriend);
            username=itemView.findViewById(R.id.username1);
        }
    }

}