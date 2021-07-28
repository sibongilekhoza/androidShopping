package com.groupi.onlineshopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.groupi.onlineshopping.admin.AdminCategoryActivity;
import com.groupi.onlineshopping.commonAll.Prevalent;
import com.groupi.onlineshopping.model.Users;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

   private EditText inpuntNumber;
   private EditText inputPassword;
   private Button loginBtn;
   private TextView adminLink;
    private TextView notAdminLink;
    private TextView forgotPassword;


   private ProgressDialog loadingBar;
   private String parentDbName ="Users";



    private com.rey.material.widget.CheckBox chkBoxRememberMe;;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginBtn = (Button)findViewById(R.id.login_btn);
        inpuntNumber = (EditText) findViewById(R.id.login_cellnumber_input);
        inputPassword =(EditText) findViewById(R.id.login_password_input);
        adminLink=(TextView)findViewById(R.id.admin_panel_link);
        notAdminLink=(TextView)findViewById(R.id.not_admin_panel_link);
        forgotPassword=(TextView) findViewById(R.id.forgot_password_link);



        loadingBar = new ProgressDialog(this);

        chkBoxRememberMe = (CheckBox) findViewById(R.id.remeber_me_chk);
        Paper.init(this);




        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                loginUser();
                        
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(LoginActivity.this,ResetPasswordActivity.class);
                startActivity(intent);
            }
        });

        adminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loginBtn.setText("Login Admin");
                adminLink.setVisibility(View.INVISIBLE);
                notAdminLink.setVisibility(View.VISIBLE);
                parentDbName = "Admins";

            }
        });

        notAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginBtn.setText("Login");
                adminLink.setVisibility(View.VISIBLE);
                notAdminLink.setVisibility(View.INVISIBLE);
                parentDbName = "Users ";
            }
        });


    }

    private void loginUser()
    {

        String cell = inpuntNumber.getText().toString();
        String password = inputPassword.getText().toString();
        if(TextUtils.isEmpty(cell))
          {
             Toast.makeText(this,"Cell number can not be empty",Toast.LENGTH_SHORT).show();
         }
          else if(TextUtils.isEmpty(password))
            {
              Toast.makeText(this,"Password can not be empty",Toast.LENGTH_SHORT).show();
            }
       else
        {
            loadingBar.setTitle("Login");
            loadingBar.setMessage("Checking credentials before logging in");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            allowAccessToAccount(cell,password);
        }


    }

    private void allowAccessToAccount(String cell, String password) {

        if(chkBoxRememberMe.isChecked())
        {
            Paper.book().write(Prevalent.userPhoneKey,cell);
            Paper.book().write(Prevalent.UserPasswordKey,password);

        }

        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (snapshot.child(parentDbName).child(cell).exists())
                {
                    Users usersData = snapshot.child(parentDbName).child(cell).getValue(Users.class);

                    if(usersData.getCell().equals(cell))
                    {

                        if(usersData.getPassword().equals(password))
                        {
                            if(parentDbName.equals("Users"))
                            {
                                Toast.makeText(LoginActivity.this,"Logged in successfully",Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(LoginActivity.this,DashboardActivity.class);
                                Prevalent.currentOnlineUser = usersData;
                                startActivity(intent);
                            }
                            else if(parentDbName.equals("Admins"))
                            {
                                loadingBar.dismiss();
                                Toast.makeText(LoginActivity.this,"Logged in successfully",Toast.LENGTH_SHORT).show();


                                Intent intent = new Intent(LoginActivity.this, AdminCategoryActivity.class);
                                startActivity(intent);
                            }

                        }
                        else
                        {
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this,"Incorrect password",Toast.LENGTH_SHORT).show();

                        }
                    }


                }
                else
                {

                    loadingBar.dismiss();
                    Toast.makeText(LoginActivity.this,"Account with this "+cell+" number does not exist",Toast.LENGTH_SHORT).show();

                    Toast.makeText(LoginActivity.this,"You need to create an account",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}