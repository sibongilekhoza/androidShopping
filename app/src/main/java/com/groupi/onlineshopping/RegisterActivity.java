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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private Button createAccountBtn;
    private EditText inputName;
    private EditText inputCellNum;
    private EditText inputPassword;
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        createAccountBtn = (Button)findViewById(R.id.register_btn);
        inputName = (EditText) findViewById(R.id.register_name_input);
        inputCellNum = (EditText) findViewById(R.id.register_cellnumber_input);
        inputPassword =(EditText) findViewById(R.id.register_password_input);
        loadingBar = new ProgressDialog(this);


        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                createAccount();
            }
        });

    }

    private void createAccount()
    {
        String name = inputName.getText().toString();
        String cell = inputCellNum.getText().toString();
        String password = inputPassword.getText().toString();

        if(TextUtils.isEmpty(name))
        {
            Toast.makeText(this,"Name can not be empty",Toast.LENGTH_SHORT).show();
        }
       else if(TextUtils.isEmpty(cell))
        {
            Toast.makeText(this,"Cell number can not be empty",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this,"Password can not be empty",Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Registration in process");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            validateCellnumber(name,cell,password);

        }


    }

    private void validateCellnumber(String name, String cell, String password)
    {
        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();
        // checking if the user already exist or not
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {

                if(!(snapshot.child("Users").child(cell).exists()))
                {
                    HashMap<String,Object> userdataMap = new HashMap<>();
                    userdataMap.put("cell",cell);
                    userdataMap.put("name",name);
                    userdataMap.put("password",password);

                    rootRef.child("Users").child(cell).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(RegisterActivity.this,"Account has been created",Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        loadingBar.dismiss();
                                        Toast.makeText(RegisterActivity.this,"Network Error: please try again",Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                }
                else
                    {
                      Toast.makeText(RegisterActivity.this,"Cell number "+cell+" already exist",Toast.LENGTH_SHORT).show();
                      loadingBar.dismiss();

                }

            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });


    }
}