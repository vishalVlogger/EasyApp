package com.appdroid.ssbtproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appdroid.ssbtproject.Holder.GroceryModel;
import com.appdroid.ssbtproject.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.ViewHolder> {

    Context context;
    ArrayList<GroceryModel> models;

    public FavouriteAdapter(Context context, ArrayList<GroceryModel> models) {
        this.context = context;
        this.models = models;
    }

    @NonNull
    @Override
    public FavouriteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favourites, parent,false);
        return new FavouriteAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FavouriteAdapter.ViewHolder holder, final int position) {

        holder.img_product.setImageResource(models.get(position).getImg_grocery());
        holder.txt_ltr.setText(models.get(position).getTxt_grocery());

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_product;
        TextView txt_ltr;

        public ViewHolder(View itemView) {
            super(itemView);

            img_product = itemView.findViewById(R.id.img_product);
            txt_ltr = itemView.findViewById(R.id.txt_ltr);
        }
    }
}
