package com.groupi.onlineshopping.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.groupi.onlineshopping.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class AdminMaintainEditProductsActivity extends AppCompatActivity {


    private Button applyChanges,deleteProduct_btn;
    private EditText editName,editPrice,editDescription;
    private ImageView imageView;
    private String productId;
    private DatabaseReference productRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_edit_products);

        productId= getIntent().getStringExtra("pid") ;
        productRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productId);

        applyChanges = (Button)findViewById(R.id.edit_products_btn_v);
        deleteProduct_btn = (Button)findViewById(R.id.delete_products_btn_v);
        editName = (EditText)findViewById(R.id.edit_product_name_v);
        editPrice = (EditText)findViewById(R.id.edit_product_price_v);
        editDescription= (EditText)findViewById(R.id.edit_product_description_v);
        imageView= (ImageView)findViewById(R.id.edit_product_image_v);
        
        displayProductInfor();

        applyChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                applyingChanges();
            }
        });

        deleteProduct_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                 deleteThisProduct();
                 productRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                     @Override
                     public void onComplete(Task<Void> task)
                     {
                         Toast.makeText(AdminMaintainEditProductsActivity.this,"Product was deleted successfully.",Toast.LENGTH_SHORT).show();

                         Intent intent = new Intent(AdminMaintainEditProductsActivity.this,AdminCategoryActivity.class);
                         startActivity(intent);
                         finish();

                     }
                 });
            }
        });

    }

    private void deleteThisProduct()
    {
    }

    private void applyingChanges()
    {

        String name = editName.getText().toString();
        String price = editPrice.getText().toString();
        String description = editName.getText().toString();
        if(name.equals(""))
        {
            Toast.makeText(this,"Write product name before you can save",Toast.LENGTH_SHORT);

        }
        else if(price.equals(""))
        {
            Toast.makeText(this,"Write product price before you can save",Toast.LENGTH_SHORT).show();

        }
        else if(description.equals(""))
        {
            Toast.makeText(this,"Write product description before you can save",Toast.LENGTH_SHORT).show();

        }
        else
        {
            HashMap<String,Object> productMap = new HashMap<>();

            productMap.put("pid",productId);
            productMap.put("description",description);
            //productMap.put("image",dowloadImageUrl);
            productMap.put("price",price);
            productMap.put("name",name);

            productRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(AdminMaintainEditProductsActivity.this,"product successfully updated",Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(AdminMaintainEditProductsActivity.this,AdminCategoryActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }
            });
        }


    }

    private void displayProductInfor()
    {

        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot snapshot)
            {
                   if(snapshot.exists())
                   {
                      String pName =snapshot.child("name").getValue().toString();
                       String pPrice =snapshot.child("price").getValue().toString();
                       String pDescription =snapshot.child("description").getValue().toString();
                       String pImage =snapshot.child("image").getValue().toString();

                       editName.setText(pName);
                       editPrice.setText(pPrice);
                       editDescription.setText(pDescription);
                       Picasso.get().load(pImage).into(imageView);

                   }
            }

            @Override
            public void onCancelled(DatabaseError error)
            {

            }
        });
    }
}