 package com.groupi.onlineshopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.groupi.onlineshopping.commonAll.Prevalent;
import com.groupi.onlineshopping.model.Products;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

 public class ProductDetailsActivity extends AppCompatActivity {

    private Button addToCart;
    private ImageView productImage;
    private TextView productPrice,productDescription,productName;
    private Button decrementBtn,incrementBtn;
    private TextView quantinty;
    private int totalQuantity =1;
    private String productID = "";
    private String state = "nornaml";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productID = getIntent().getStringExtra("pid");

        productImage = (ImageView)  findViewById(R.id.product_image_details);
       addToCart =(Button)findViewById(R.id.prod_details_add_to_cart_btn);
        decrementBtn = (Button)findViewById(R.id.product_decrement);
        incrementBtn= (Button)findViewById(R.id.product_increment);
        quantinty = (TextView)findViewById(R.id.product_quantity);
        productPrice = (TextView)findViewById(R.id.product_price_details);
        productDescription = (TextView)findViewById(R.id.product_description_details);
        productName = (TextView)findViewById(R.id.product_name_details);

        incrementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(totalQuantity<10)
                {
                    totalQuantity++;
                    quantinty.setText(String.valueOf(totalQuantity));
                }

            }
        });

        decrementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(totalQuantity>1)
                {
                    totalQuantity--;
                    quantinty.setText(String.valueOf(totalQuantity));
                }


            }
        });

        getProductDeatails(productID);


        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(state.equals("Orders placed")||state.equals("Order Shipped"))
                {
                    Toast.makeText(ProductDetailsActivity.this,"You can place more products once your order was confirmed",Toast.LENGTH_LONG).show();
                }
                else
                {
                    addingToCartList();
                }

            }
        });
    }

     @Override
     protected void onStart()
     {
         super.onStart();

         checkOrderState();
     }

     private void addingToCartList()
     {
         String saveCurrentTime,savecurrentDate;

         Calendar calForDate = Calendar.getInstance();

         SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
         savecurrentDate = currentDate.format(calForDate.getTime());

         SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
         saveCurrentTime = currentDate.format(calForDate.getTime());

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
         final HashMap<String,Object>cartMap = new HashMap<>();
         cartMap.put("pid",productID);
         cartMap.put("name",productName.getText().toString());
         cartMap.put("price",productPrice.getText().toString());
         cartMap.put("date",savecurrentDate);
         cartMap.put("time",saveCurrentTime);
         cartMap.put("quantity",totalQuantity);
         cartMap.put("discount","");

         cartListRef.child("User view").child(Prevalent.currentOnlineUser.getCell()).child("Products").child(productID)
         .updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
             @Override
             public void onComplete(@NonNull  Task<Void> task)
             {
                 if(task.isSuccessful())
                 {
                     cartListRef.child("Admin view").child(Prevalent.currentOnlineUser.getCell()).child("Products").child(productID)
                             .updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                         @Override
                         public void onComplete(@NonNull Task<Void> task) {
                             if(task.isSuccessful())
                             {
                                 Toast.makeText(ProductDetailsActivity.this,"Added to cart list",Toast.LENGTH_SHORT).show();
                                 Intent intent = new Intent(ProductDetailsActivity.this,DashboardActivity.class);
                                 startActivity(intent);
                             }

                         }
                     });
                 }

             }
         });


     }

     private void getProductDeatails(String productID)
     {

         DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");

         productsRef.child(productID).addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange( DataSnapshot snapshot)
             {
                 if(snapshot.exists()) {
                     Products product = snapshot.getValue(Products.class);
                     productName.setText(product.getName());
                     productDescription.setText(product.getDescription());
                     productPrice.setText("R"+product.getPrice());
                     Picasso.get().load(product.getImage()).into(productImage);
                 }

             }

             @Override
             public void onCancelled( DatabaseError error) {

             }
         });

     }


     private void checkOrderState()
     {
         DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("Oders")
                 .child(Prevalent.currentOnlineUser.getCell());
         orderRef.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot)
             {
                 if(snapshot.exists())
                 {
                     String shoppingState = snapshot.child("state").getValue().toString();

                     if(shoppingState.equals("shipped"))
                     {

                              state="Order Shipped";

                     }
                     else if (shoppingState.equals("Oder received"))
                     {
                         state="Orders placed";
                     }

                 }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError error)
             {

             }
         });
     }
 }