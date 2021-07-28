package com.groupi.onlineshopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.groupi.onlineshopping.commonAll.Prevalent;
import com.groupi.onlineshopping.model.Users;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Button joinNowBtn;
    private Button loginbtn;
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        joinNowBtn=(Button) findViewById(R.id.main_join_now_btn);
        loginbtn=(Button) findViewById(R.id.main_log_btn);
        loadingBar = new ProgressDialog(this);

        Paper.init(this);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);

            }
        });

        joinNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
               Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
               startActivity(intent);

            }
        });

        //when you checked remember me and you didn't logout
        String userphoneKey= Paper.book().read(Prevalent.userPhoneKey);
        String userpassword= Paper.book().read(Prevalent.UserPasswordKey);

        if(userphoneKey != "" && userpassword != "")
        {
            if(!TextUtils.isEmpty(userphoneKey) && !TextUtils.isEmpty(userpassword))
            {
                allowAccess(userphoneKey,userpassword);


                loadingBar.setTitle("Already Logged in");
                loadingBar.setMessage("Please wait");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

            }

        }



    }

    private void allowAccess(final String cell, final String password)
    {

        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (snapshot.child("Users").child(cell).exists())
                {
                    Users usersData = snapshot.child("Users").child(cell).getValue(Users.class);

                    if(usersData.getCell().equals(cell))
                    {

                        if(usersData.getPassword().equals(password))
                        {
                            Toast.makeText(MainActivity.this,"Logged in successfully",Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                            Intent intent = new Intent(MainActivity.this,DashboardActivity.class);
                            Prevalent.currentOnlineUser = usersData;
                            startActivity(intent);

                        }
                        else
                        {
                            loadingBar.dismiss();
                            Toast.makeText(MainActivity.this,"Incorrect password",Toast.LENGTH_SHORT).show();

                        }
                    }


                }
                else
                {

                    Toast.makeText(MainActivity.this,"Account with this "+cell+" number does not exist",Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(MainActivity.this,"You need to create an account",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

}