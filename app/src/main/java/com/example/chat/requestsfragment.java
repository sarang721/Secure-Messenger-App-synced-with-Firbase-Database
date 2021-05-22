package com.example.chat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link requestsfragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class requestsfragment extends Fragment {

    View Requestfragmentview;
    private RecyclerView myreqlist;
    private DatabaseReference reqref,userref;
    private FirebaseAuth auth;
    String userid;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public requestsfragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment requestsfragment.
     */
    // TODO: Rename and change types and number of parameters
    public static requestsfragment newInstance(String param1, String param2) {
        requestsfragment fragment = new requestsfragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Requestfragmentview= inflater.inflate(R.layout.fragment_requestsfragment, container, false);
        myreqlist=Requestfragmentview.findViewById(R.id.chat_request_list);
        reqref= FirebaseDatabase.getInstance().getReference().child("Requests");
        auth=FirebaseAuth.getInstance();
        userid=auth.getCurrentUser().getUid();
        userref=FirebaseDatabase.getInstance().getReference().child("users");
        myreqlist.setLayoutManager(new LinearLayoutManager(getContext()));



        return Requestfragmentview;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<post> options=new FirebaseRecyclerOptions.Builder<post>()
                .setQuery(reqref.child(userid),post.class)
                .build();

        FirebaseRecyclerAdapter<post,requestviewholder> adapter=new FirebaseRecyclerAdapter<post, requestviewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final requestviewholder holder, int i, @NonNull post post) {
                holder.itemView.findViewById(R.id.requestaccept).setVisibility(View.VISIBLE);
                holder.itemView.findViewById(R.id.requestcancel).setVisibility(View.VISIBLE);

                final String listuser_id=getRef(i).getKey();
                DatabaseReference getyperef=getRef(i).child("requesttype").getRef();
                getyperef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            String type=dataSnapshot.getValue().toString();
                            if(type.equals("received"))
                            {
                                userref.child(listuser_id).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.hasChild("name"))
                                        {
                                            String name1=dataSnapshot.child("name").getValue().toString();
                                            String username1=dataSnapshot.child("username").getValue().toString();

                                            holder.name.setText(name1);
                                            holder.username.setText(username1);
                                        }


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @NonNull
            @Override
            public requestviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.post, parent, false);

                requestviewholder holder=new requestviewholder(view);
                return holder;
            }
        };

        myreqlist.setAdapter(adapter);
        adapter.startListening();
    }
    public static class  requestviewholder extends  RecyclerView.ViewHolder
    {
        TextView name,username;
        Button accept,cancel;
        public requestviewholder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.nameoffriend);
            username=itemView.findViewById(R.id.username1);
            accept=itemView.findViewById(R.id.requestaccept);
            cancel=itemView.findViewById(R.id.requestcancel);

        }
    }
}