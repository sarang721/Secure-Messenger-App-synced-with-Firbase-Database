package com.example.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    Toolbar mtoolbar;
    FirebaseAuth auth;
    DatabaseReference ref;
    TabLayout mytablayout;
    ViewPager myviewPager;
    tabsaccesoradap mytabaccessor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mtoolbar=findViewById(R.id.toolbar);
        auth=FirebaseAuth.getInstance();
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("Secure Messenger app");
        ref=FirebaseDatabase.getInstance().getReference();
        myviewPager=findViewById(R.id.maintabspager);
        mytabaccessor=new tabsaccesoradap(getSupportFragmentManager());
        myviewPager.setAdapter(mytabaccessor);
        mytablayout=findViewById(R.id.maintab);
        mytablayout.setupWithViewPager(myviewPager);
        

    }

    @Override
    protected void onStart() {
        super.onStart();
        String userid=auth.getCurrentUser().getUid();
        ref.child("users").child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if((dataSnapshot.child("name").exists()) )
                {

                }

                else
                {
                    Intent settingsintent=new Intent(MainActivity.this,settings.class);
                    settingsintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(settingsintent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         super.onCreateOptionsMenu(menu);

         getMenuInflater().inflate(R.menu.options_menu,menu);

         return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
         super.onOptionsItemSelected(item);
         if(item.getItemId()==R.id.setting)
         {
                startActivity(new Intent(MainActivity.this,settings.class));

         }
         if(item.getItemId()==R.id.logout) {
             auth.signOut();
             startActivity(new Intent(MainActivity.this,loginpage.class));
         }
         if(item.getItemId()==R.id.invite)
         {
             startActivity(new Intent(MainActivity.this,postlist.class));
         }
         if(item.getItemId()==R.id.friends)
         {
             startActivity(new Intent(MainActivity.this,friends.class));
         }


         return true;
    }
}