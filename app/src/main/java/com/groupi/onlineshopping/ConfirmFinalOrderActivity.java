package com.groupi.onlineshopping;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.groupi.onlineshopping.commonAll.Prevalent;
import com.groupi.onlineshopping.model.Products;
import com.groupi.onlineshopping.model.Users;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrderActivity extends AppCompatActivity {


    private Button confirmOderBtn,backToCartBtn;
    private EditText nameEdittext,phoneEditText,addressEditText,townEditText;
    private TextView itemsQuantiy;
    private TextView amountToPay;
    String overAllPrice = "";
    String qauntity="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        overAllPrice =getIntent().getStringExtra("Total Price");

        qauntity =getIntent().getStringExtra("Quantity products");
        confirmOderBtn = (Button)findViewById(R.id.confirm_final_order_btn);
        backToCartBtn = (Button)findViewById(R.id.back_to_cart_btn);
        nameEdittext=(EditText)findViewById(R.id.shipment_name);
        phoneEditText=(EditText)findViewById(R.id.shipment_phone);
        addressEditText=(EditText)findViewById(R.id.shipment_address);
        townEditText =(EditText)findViewById(R.id.shipment_town);

        itemsQuantiy=(TextView)findViewById(R.id.number_of_products_to_by);
        amountToPay=(TextView)findViewById(R.id.amount_to_pay);






        backToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ConfirmFinalOrderActivity.this,CartActivity.class);
                startActivity(intent);


            }
        });


        confirmOderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                 checkEmpty();
            }
        });




    }

    @Override
    protected void onStart() {
        super.onStart();



//        userNameTextView.setText(Prevalent.currentOnlineUser.getName());
        nameEdittext.setText(Prevalent.currentOnlineUser.getName());
        phoneEditText.setText(Prevalent.currentOnlineUser.getCell());
        addressEditText.setText(Prevalent.currentOnlineUser.getAddress());
        itemsQuantiy.setText("Number of items purchased: "+ qauntity);
        amountToPay.setText("Total amount to pay: R" +overAllPrice);








    }

    private void checkEmpty()
    {
        if(TextUtils.isEmpty(nameEdittext.getText().toString()))
        {
            Toast.makeText(this,"Name can not be empty",Toast.LENGTH_SHORT);
        }
        else if(TextUtils.isEmpty(phoneEditText.getText().toString()))
        {
            Toast.makeText(this,"Phone can not be empty",Toast.LENGTH_SHORT);
        }
         else if(TextUtils.isEmpty(addressEditText.getText().toString()))
        {
            Toast.makeText(this,"Address can not be empty",Toast.LENGTH_SHORT);
        }
         else if(TextUtils.isEmpty(townEditText.getText().toString()))
        {
            Toast.makeText(this,"Name can not be empty",Toast.LENGTH_SHORT);
        }

         else
             {
              confirmOder();
        }
    }

    private void confirmOder()
    {

       final String saveCurrentTime,savecurrentDate;

        Calendar calForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
        savecurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentDate.format(calForDate.getTime());

        final DatabaseReference odersRef = FirebaseDatabase.getInstance().getReference().child("Oders")
                .child(Prevalent.currentOnlineUser.getCell());
        HashMap<String,Object> ordersMap = new HashMap<>();

        ordersMap.put("totalAmount",overAllPrice);
        ordersMap.put("name",nameEdittext.getText().toString());
        ordersMap.put("cell",phoneEditText.getText().toString());
        ordersMap.put("address",addressEditText.getText().toString());
        ordersMap.put("town",townEditText.getText().toString());
        ordersMap.put("date",savecurrentDate);
        ordersMap.put("time",saveCurrentTime);
        ordersMap.put("state","Oder received");

        odersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull  Task<Void> task)
            {
                if(task.isSuccessful())
                {
                    FirebaseDatabase.getInstance().getReference().child("Cart List")
                            .child("User view").child(Prevalent.currentOnlineUser.getCell()).removeValue()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(ConfirmFinalOrderActivity.this,"Your oder was placed successfully",Toast.LENGTH_LONG).show();


                                Intent intent = new Intent(ConfirmFinalOrderActivity.this,DashboardActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();




                            }

                        }
                    });
                }
            }
        });






    }


}