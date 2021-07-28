package com.groupi.onlineshopping.viewHolder;

import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.groupi.onlineshopping.R;
import com.groupi.onlineshopping.interfaces.ItemClickListener;


import org.jetbrains.annotations.NotNull;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
public TextView txtProductName,txtProductDescription,txtProductPrice;
public ImageView imageView;
public ItemClickListener listner;


    public ProductViewHolder( View itemView)
    {
        super(itemView);

        imageView = (ImageView) itemView.findViewById(R.id.product_image_v);
        txtProductName = (TextView) itemView.findViewById(R.id.product_name_v);
        txtProductDescription = (TextView) itemView.findViewById(R.id.product_description_v);
        txtProductPrice = (TextView) itemView.findViewById(R.id.product_price_v);


    }

    public void setItemClickListner(ItemClickListener listner)
    {
        this.listner = listner;
    }

    @Override
    public void onClick(View view) {
        listner.onClick(view,getAdapterPosition(),false);

    }
}
