package com.example.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.AbstractExecutorService;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class  personalchats extends AppCompatActivity {

    String receiverid,receivername,senderid,notdecmsg;

    Toolbar toolbar;
    DatabaseReference userref;
    FirebaseAuth auth;
    String pwdtext="sarang",encmsg="";
    EditText message;
    ImageButton sendmsgbtn;
    List<messages> messageslist=new ArrayList<>();
    LinearLayoutManager linlayoutmanager;
    messageadapter msgadapter;
    RecyclerView usermessageslist;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalchats);
        userref = FirebaseDatabase.getInstance().getReference();
        receiverid = getIntent().getExtras().get("userid").toString();
        receivername = getIntent().getExtras().get("retname").toString();
        toolbar = findViewById(R.id.toolbarchats);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(receivername);
        auth = FirebaseAuth.getInstance();
        senderid = auth.getCurrentUser().getUid();
        message = findViewById(R.id.entermessage);
        sendmsgbtn = findViewById(R.id.sendmsg);
        usermessageslist = findViewById(R.id.chatsrecyclerview);
        msgadapter=new messageadapter(messageslist);
        linlayoutmanager = new LinearLayoutManager(this);
        usermessageslist.setLayoutManager(linlayoutmanager);
        usermessageslist.setAdapter(msgadapter);



        sendmsgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = message.getText().toString();
                try {
                    encmsg=encrypt(msg,pwdtext);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (TextUtils.isEmpty(msg)) {
                    Toast.makeText(personalchats.this, "Please write some message", Toast.LENGTH_SHORT).show();
                } else {
                    DatabaseReference ref = userref.child("messages").child(senderid).child(receiverid).push();
                    String pushid = ref.getKey();
                    String senderref = "messages/" + senderid + "/" + receiverid;
                    String receiverref = "messages/" + receiverid + "/" + senderid;

                    Map msgdata = new HashMap();
                    msgdata.put("message",encmsg);
                    msgdata.put("type", "text");
                    msgdata.put("from", senderid);

                    Map msgstruct = new HashMap();
                    msgstruct.put(senderref + "/" + pushid, msgdata);
                    msgstruct.put(receiverref + "/" + pushid, msgdata);

                    userref.updateChildren(msgstruct).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(personalchats.this, "Message sent successfully", Toast.LENGTH_SHORT).show();
                                message.setText("");
                            } else {
                                Toast.makeText(personalchats.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }

            }



        });


    }

    private String encrypt(String data, String password) throws Exception {
        SecretKeySpec key=generatekey(pwdtext);
        Cipher c=Cipher.getInstance("AES/ECB/PKCS5Padding");
        c.init(Cipher.ENCRYPT_MODE,key);
        byte[] encval=c.doFinal(data.getBytes("UTF-8"));
        String encryptedval= Base64.encodeToString(encval,Base64.DEFAULT);
        return encryptedval;

    }

    private SecretKeySpec generatekey(String password) throws Exception {
        final MessageDigest digest=MessageDigest.getInstance("SHA-256");
        byte[] bytes=password.getBytes("UTF-8");
        digest.update(bytes,0,bytes.length);
        byte[] key=digest.digest();
        SecretKeySpec secretKeySpec=new SecretKeySpec(key,"AES");
        return secretKeySpec;


    }


    @Override
    protected void onStart() {
        super.onStart();
        userref.child("messages").child(senderid).child(receiverid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                messages messages=dataSnapshot.getValue(messages.class);
                messageslist.add(messages);
                msgadapter.notifyDataSetChanged();
                usermessageslist.smoothScrollToPosition(usermessageslist.getAdapter().getItemCount());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



}