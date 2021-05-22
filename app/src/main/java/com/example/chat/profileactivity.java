package com.example.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class profileactivity extends AppCompatActivity {
    String receiverid, userstats, senderid;
    TextView name, userid;
    Button send,ignore;
    DatabaseReference ref, reqref,acceptref;
    Toolbar toolbar;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profileactivity);
        name = findViewById(R.id.textView4);
        userid = findViewById(R.id.textView5);
        toolbar = findViewById(R.id.profiletoolbar);
        userstats = "new";
        acceptref=FirebaseDatabase.getInstance().getReference().child("friends");
        ignore=findViewById(R.id.ignorebtn);
        reqref = FirebaseDatabase.getInstance().getReference().child("Requests");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Request Friend");
        send = findViewById(R.id.button12);
        receiverid = getIntent().getExtras().get("userid").toString();
        ref = FirebaseDatabase.getInstance().getReference().child("users").child(receiverid);
        auth = FirebaseAuth.getInstance();
        senderid = auth.getCurrentUser().getUid();

        reqref.child(senderid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(receiverid)) {
                    String requesttype = dataSnapshot.child(receiverid).child("requesttype").getValue().toString();
                    if (requesttype.equals("sent")) {
                        send.setText("Cancel Request");
                        userstats="requestsent";
                        send.setBackgroundColor(Color.parseColor("#FF0000"));
                    }
                    else if(requesttype.equals("received"))
                    {
                        send.setText("Accept");
                        userstats="requestreceived";
                        ignore.setVisibility(View.VISIBLE);
                        ignore.setEnabled(true);

                        ignore.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                userstats="new";
                                reqref.child(senderid).child(receiverid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if(task.isSuccessful())
                                        {
                                            reqref.child(receiverid).child(senderid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful())
                                                    {
                                                        send.setText("Send Request");
                                                        ignore.setVisibility(View.INVISIBLE);
                                                    }

                                                }
                                            });
                                        }

                                    }
                                });

                            }
                        });

                    }
                }
                else
                {
                    acceptref.child(senderid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild(receiverid))
                            {
                                userstats="friends";
                                send.setText("Unfriend");

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String namee = dataSnapshot.child("name").getValue().toString();
                    String userids = dataSnapshot.child("username").getValue().toString();
                    name.setText(namee);
                    userid.setText(userids);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (receiverid.equals(senderid)) {
            send.setVisibility(View.INVISIBLE);
        } else {
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (userstats.equals("new")) {
                        sendrequest();
                    }
                     if(userstats.equals("requestsent"))
                    {
                        cancelrequest();

                    }
                     if(userstats.equals("requestreceived")) {
                         friends();

                     }
                     if(userstats.equals("friends"))
                     {
                         removefriend();
                     }
                }


            });


        }
    }

    private void removefriend() {
        acceptref.child(senderid).child(receiverid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    acceptref.child(receiverid).child(senderid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                send.setText("Send Request");
                                userstats="new";
                            }
                        }
                    });
                }

            }
        });

    }

    private void friends() {

        acceptref.child(senderid).child(receiverid).child("reqtype").setValue("friends").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    acceptref.child(receiverid).child(senderid).child("reqtype").setValue("friends").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                         if(task.isSuccessful())
                         {
                             reqref.child(senderid).child(receiverid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                 @Override
                                 public void onComplete(@NonNull Task<Void> task) {
                                     if(task.isSuccessful())
                                     {
                                         reqref.child(receiverid).child(senderid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                             @Override
                                             public void onComplete(@NonNull Task<Void> task) {
                                                 if(task.isSuccessful())
                                                 {
                                                     ignore.setVisibility(View.INVISIBLE);
                                                     send.setText("Unfriend");
                                                     userstats="fiends";

                                                 }

                                             }
                                         });
                                     }
                                 }
                             });


                         }
                        }
                    });
                }
            }
        });


    }

    private void sendrequest() {
        reqref.child(senderid).child(receiverid).child("requesttype").setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    reqref.child(receiverid).child(senderid).child("requesttype").setValue("received").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            send.setText("Cancel Request");
                            send.setBackgroundColor(Color.parseColor("#FF0000"));
                            userstats = "requestsent";

                        }
                    });
                }

            }
        });
    }


    private void cancelrequest()
    {
        reqref.child(senderid).child(receiverid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    reqref.child(receiverid).child(senderid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            send.setText("Send Request");
                            userstats="new";
                            send.setBackgroundColor(Color.parseColor("#3DDC84"));


                        }
                        }
                    });
                }

            }
        });

    }
}

