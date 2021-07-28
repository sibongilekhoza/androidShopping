package com.groupi.onlineshopping.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.groupi.onlineshopping.R;
import com.groupi.onlineshopping.model.Cart;
import com.groupi.onlineshopping.viewHolder.CartViewHolder;

import org.jetbrains.annotations.NotNull;

public class AdminUserProductsActivity extends AppCompatActivity {


    private RecyclerView productList;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference catsListRef;
    private String userId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_products);

        userId =getIntent().getStringExtra("uid");

        productList = findViewById(R.id.products_list);
        productList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        productList.setLayoutManager(layoutManager);


        catsListRef = FirebaseDatabase.getInstance().getReference().child("Cart List").child("Admin view")
        .child(userId).child("Products");


    }

    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions<Cart>options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(catsListRef,Cart.class).build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder>adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model)
            {
                String text=model.getPrice();

                String price =text.substring(1);

                int productPrices = Integer.valueOf(price);
                int quanty = model.getQuantity();
                int oneTypeProductPrice =productPrices*quanty;

                holder.txtProductQuantity.setText("Quantity ="+model.getQuantity());
                holder.txtProductPrice.setText("Price ="+model.getPrice());
                holder.txtProductName.setText("product name = "+model.getName());
                holder.txtProductTotalAmount.setText("Total Amount =R" +oneTypeProductPrice);
            }

            @NonNull

            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout,parent,false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;

            }
        };
        productList.setAdapter(adapter);
        adapter.startListening();
    }
}