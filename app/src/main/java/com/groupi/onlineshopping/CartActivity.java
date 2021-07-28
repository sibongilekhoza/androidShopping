package com.groupi.onlineshopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.groupi.onlineshopping.commonAll.Prevalent;
import com.groupi.onlineshopping.model.Cart;
import com.groupi.onlineshopping.viewHolder.CartViewHolder;

import org.jetbrains.annotations.NotNull;

public class CartActivity extends AppCompatActivity {

    private Button checkoutbtn,continueShopingBtn;
    private TextView txttotalAmount,textMessage;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private int overallPrice =0;
    int quanty =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        checkoutbtn=(Button)findViewById(R.id.checkout_btn);
        continueShopingBtn=(Button)findViewById(R.id.continue_shopping_btn);
        txttotalAmount=(TextView)findViewById(R.id.total_price);
        textMessage=(TextView)findViewById(R.id.msg1);
        recyclerView =(RecyclerView) findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);



        continueShopingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(CartActivity.this,DashboardActivity.class);
                startActivity(intent);


            }
        });

        checkoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                txttotalAmount.setText("Total price is=R"+overallPrice);
                Intent intent = new Intent(CartActivity.this,ConfirmFinalOrderActivity.class);
                intent.putExtra("Total Price",String.valueOf(overallPrice));
                intent.putExtra("Quantity products ",String.valueOf(quanty));
                startActivity(intent);
                finish();

            }
        });

    }

    @Override
    protected void onStart()
    {
        super.onStart();

        checkOrderState();

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        FirebaseRecyclerOptions<Cart> options =
                new  FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef.child("User view")
                .child(Prevalent.currentOnlineUser.getCell())
                        .child("Products"),Cart.class).build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull  CartViewHolder holder, int position, @NonNull  Cart model) {

                String text=model.getPrice();

                String price =text.substring(1);

                int productPrices = Integer.valueOf(price);
                 quanty = model.getQuantity();
                int oneTypeProductPrice =productPrices*quanty;

                   holder.txtProductQuantity.setText("Quantity ="+model.getQuantity());
                  holder.txtProductPrice.setText("Price ="+model.getPrice());
                holder.txtProductName.setText("product name = "+model.getName());
                holder.txtProductTotalAmount.setText("Total Amount =R" +oneTypeProductPrice);

                  overallPrice =overallPrice + oneTypeProductPrice;




                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        CharSequence options[]= new CharSequence[]
                                {
                                        "Edit",
                                        "Remove"
                                };
                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Cart options");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                if(i==0)
                                {
                                    Intent intent = new Intent(CartActivity.this,ProductDetailsActivity.class);
                                    intent.putExtra("pid",model.getPid());
                                    startActivity(intent);
                                }
                                if(i==1)
                                {
                                    cartListRef.child("User view").child(Prevalent.currentOnlineUser.getCell())
                                    .child("Products").child(model.getPid()).removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if(task.isSuccessful())
                                            {
                                                Toast.makeText(CartActivity.this,"Item Removed from the cart",Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(CartActivity.this,DashboardActivity.class);
                                                startActivity(intent);
                                            }

                                        }
                                    });
                                }

                            }
                        });

                          builder.show();
                    }
                });

            }

            @NonNull

            @Override
            public CartViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout,parent,false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
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
                   String username = snapshot.child("name").getValue().toString();
                   if(shoppingState.equals("shipped"))
                   {
                       txttotalAmount.setText("Dear "+ username+"\n Order has been received successfully.and has been shipped to you");
                       recyclerView.setVisibility(View.GONE);

                       textMessage.setVisibility(View.VISIBLE);
                       textMessage.setText("Congratulation your order has been approved successfully. your order will be sent via the address your provided");
                       checkoutbtn.setVisibility(View.GONE);
                       Toast.makeText(CartActivity.this,"You can purchase more product",Toast.LENGTH_SHORT).show();


                   }
                   else if (shoppingState.equals("Oder received"))
                   {
                       txttotalAmount.setText("Dear "+ username+"\n Order has not yet been confirm ");
                       recyclerView.setVisibility(View.GONE);

                       textMessage.setVisibility(View.VISIBLE);
                       checkoutbtn.setVisibility(View.GONE);
                       Toast.makeText(CartActivity.this,"You can purchase more product",Toast.LENGTH_SHORT).show();
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