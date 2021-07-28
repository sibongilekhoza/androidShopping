package com.groupi.onlineshopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.groupi.onlineshopping.model.Products;
import com.groupi.onlineshopping.viewHolder.ProductViewHolder;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class SearchProductsActivity extends AppCompatActivity
{
  private Button searchButton;
  private EditText inputText;
  private RecyclerView searchList;
  private String searchInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_products);

        searchButton=(Button)findViewById(R.id.search_btn);
        inputText =(EditText)findViewById(R.id.search_product_name);
        searchList = findViewById(R.id.search_list);

        searchList.setLayoutManager(new LinearLayoutManager(SearchProductsActivity.this));


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                searchInput = inputText.getText().toString();
                onStart();

            }
        });

    }

    @Override
    protected void onStart()
    {
        super.onStart();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Products");

        FirebaseRecyclerOptions<Products>options= new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(reference.orderByChild("name").startAt(searchInput),Products.class).build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model)
                    {
                        holder.txtProductName.setText(model.getName());
                        holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductPrice.setText("Price = R"+model.getPrice());

                        Picasso.get().load(model.getImage()).into(holder.imageView);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent intent = new Intent(SearchProductsActivity.this,ProductDetailsActivity.class);
                                intent.putExtra("pid",model.getPid());
                                startActivity(intent);

                            }
                        });

                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout,parent,false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };

          searchList.setAdapter(adapter);
          adapter.startListening();
    }
}