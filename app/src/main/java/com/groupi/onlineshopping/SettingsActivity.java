package com.groupi.onlineshopping;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.groupi.onlineshopping.commonAll.Prevalent;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

   private CircleImageView profileImage;
   private EditText fullNameEditText,userCellEditText,addressEditText;
   private TextView profile,closeTextBtn,saveTextBtn;

   private Uri uri;
   private String myUri ="";
   private StorageReference storageProfilePictureRef;
   private String checker ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        profileImage = (CircleImageView) findViewById(R.id.setting_profile_image);
        fullNameEditText = (EditText) findViewById(R.id.settings_full_name);
        userCellEditText = (EditText) findViewById(R.id.settings_cell_number);
        addressEditText = (EditText) findViewById(R.id.setting_address);

        profile = (TextView)findViewById(R.id.profile_image_change_btn);
        closeTextBtn = (TextView)findViewById(R.id.close_settings_btn);
        saveTextBtn = (TextView)findViewById(R.id.update_account_settings_btn);

       userInfoDisplay(profileImage,fullNameEditText,userCellEditText,addressEditText);


       closeTextBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view)
           {
               finish();
           }
       });

       saveTextBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {


                  userInforSaved();
                  updateOnlyUserInfo();


           }
       });


    }

    private void updateOnlyUserInfo() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

        HashMap<String,Object> usermap = new HashMap<>();
        usermap.put("name",fullNameEditText.getText().toString());
        usermap.put("address",addressEditText.getText().toString());
        usermap.put("cell",userCellEditText.getText().toString());

        ref.child(Prevalent.currentOnlineUser.getCell()).updateChildren(usermap);

        startActivity(new Intent(SettingsActivity.this,DashboardActivity.class));
        Toast.makeText(SettingsActivity.this, "Profile info updated successfully", Toast.LENGTH_SHORT).show();
        finish();

    }




    private void userInforSaved()
    {

        if(TextUtils.isEmpty(fullNameEditText.getText().toString()))
        {
            Toast.makeText(this,"Enter name to update",Toast.LENGTH_SHORT).show();
        }
        else  if(TextUtils.isEmpty(userCellEditText.getText().toString()))
        {
            Toast.makeText(this,"Enter cell number to update",Toast.LENGTH_SHORT).show();
        }
        else  if(TextUtils.isEmpty(addressEditText.getText().toString()))
        {
            Toast.makeText(this,"Enter address to update",Toast.LENGTH_SHORT).show();
        }
    }

    private void userInfoDisplay(CircleImageView profileImage, EditText fullNameEditText, EditText userCellEditText, EditText addressEditText)
    {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getCell());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot snapshot)
            {

                if(snapshot.exists())
                {
                    if(snapshot.child("image").exists())
                    {
                        String image = snapshot.child("image").getValue().toString();
                        String name = snapshot.child("name").getValue().toString();
                        String cell = snapshot.child("cell").getValue().toString();
                        String address = snapshot.child("address").getValue().toString();

                        Picasso.get().load(image).into(profileImage);
                        fullNameEditText.setText(name);
                        userCellEditText.setText(cell);
                        addressEditText.setText(address);



                    }

                }


            }

            @Override
            public void onCancelled( DatabaseError error) {

            }
        });
    }
}