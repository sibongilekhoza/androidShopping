package com.groupi.onlineshopping.viewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.groupi.onlineshopping.R;
import com.groupi.onlineshopping.interfaces.ItemClickListener;


public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtProductName,txtProductPrice,txtProductQuantity,txtProductTotalAmount;
    public ItemClickListener itemClickListener;

    public CartViewHolder(@NonNull  View itemView) {
        super(itemView);

        txtProductName = itemView.findViewById(R.id.product_name_cart);
        txtProductPrice = itemView.findViewById(R.id.product_price_cart);
        txtProductQuantity = itemView.findViewById(R.id.product_quantity_cart);
        txtProductTotalAmount=itemView.findViewById(R.id.product_price_total);
    }

    @Override
    public void onClick(View view)
    {
      itemClickListener.onClick(view,getAdapterPosition(),false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}

