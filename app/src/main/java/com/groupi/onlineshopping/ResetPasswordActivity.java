package com.groupi.onlineshopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.groupi.onlineshopping.commonAll.Prevalent;

import org.jetbrains.annotations.NotNull;

public class ResetPasswordActivity extends AppCompatActivity {

    private String check ="";
    private EditText pasPhone;
    private Button btnRest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);


        pasPhone =(EditText)findViewById(R.id.forgot_password_number);
        btnRest=(Button)findViewById(R.id.forgot_password_send_btn);

        btnRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {



                veryfyUser();



            }
        });



    }

    private void veryfyUser()
    {
        String phone = pasPhone.getText().toString();

        if(!phone.equals(""))
        {


            final   DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(phone);

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange( DataSnapshot snapshot)
                {

                    if (snapshot.exists() )
                    {

                        String mPhone = snapshot.child("cell").getValue().toString();

                        AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this);
                        builder.setTitle("New Password");
                        final EditText newPassword = new EditText(ResetPasswordActivity.this);
                        newPassword.setHint("Enter new password");
                        builder.setView(newPassword);


                        builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                if(!newPassword.getText().toString().equals(""))
                                {
                                    ref.child("password").setValue(newPassword.getText ().toString())
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task)
                                                {
                                                    if(task.isSuccessful())
                                                    {
                                                        Toast.makeText(ResetPasswordActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();

                                                        Intent intent = new Intent(ResetPasswordActivity.this,LoginActivity.class);
                                                        startActivity(intent);
                                                    }
                                                }
                                            });
                                }
                            }
                        });

                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                  dialogInterface.cancel();
                            }
                        });

                        builder.show();

                    }
                    else
                    {
                        Toast.makeText(ResetPasswordActivity.this,"The user phone number not exist ",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }
        else
        {
            Toast.makeText(this, "Enter you phone number to reset your password", Toast.LENGTH_SHORT).show();
        }

    }
}