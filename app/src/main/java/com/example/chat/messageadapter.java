package com.example.chat;

import android.content.Context;
import android.content.Intent;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class messageadapter  extends RecyclerView.Adapter<messageadapter.messageviewholder> {
    private List<messages> user_messages_list;
    FirebaseAuth auth;
    DatabaseReference userref;
    String pwdtext="sarang";
    String decmsg="";
    View view;
    public messageadapter(List<messages> messageslist) {
        this.user_messages_list=messageslist;
    }


    public class messageviewholder extends RecyclerView.ViewHolder
    {
        public TextView sendermsg,receivermsg;
        public messageviewholder(@NonNull View itemView) {
            super(itemView);
            sendermsg=itemView.findViewById(R.id.sender);
            receivermsg=itemView.findViewById(R.id.receiver);

        }
    }

    @NonNull
    @Override
    public messageviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customchats, parent, false);

        auth=FirebaseAuth.getInstance();



        return new messageviewholder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull messageviewholder messageviewholder, int position) {
        String messagesenderid=auth.getCurrentUser().getUid();
        messages message=user_messages_list.get(position);
        String fromuserid=message.getFrom();
        String messagetype=message.getType();

        userref= FirebaseDatabase.getInstance().getReference().child("users").child(fromuserid);
        userref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
        if(messagetype.equals("text"))
        {
            String msg=message.getMessage();
            try {
                decmsg=decrypt(msg,pwdtext);
            } catch (Exception e) {
                e.printStackTrace();
            }

            messageviewholder.receivermsg.setVisibility(View.INVISIBLE);

            if(fromuserid.equals(messagesenderid))
            {
                messageviewholder.sendermsg.setText(decmsg);

            }
            else
            {
                messageviewholder.sendermsg.setVisibility(View.INVISIBLE);
                messageviewholder.receivermsg.setVisibility(View.VISIBLE);
                messageviewholder.receivermsg .setText(decmsg);


            }

        }
    }

    private String decrypt(String data, String password) throws Exception {
        SecretKeySpec key=generatekey(password);
        Cipher c=Cipher.getInstance("AES/ECB/PKCS5Padding");
        c.init(Cipher.DECRYPT_MODE,key);
        byte[] decodedval= Base64.decode(data,Base64.DEFAULT);
        byte[] decval=c.doFinal(decodedval);
        String decryptedval=new String(decval,"UTF-8");
        return decryptedval;


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
    public int getItemCount() {
        return user_messages_list.size();
    }





}
