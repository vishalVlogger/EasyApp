package com.appdroid.ssbtproject.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appdroid.ssbtproject.Holder.GroceryProductModel;
import com.appdroid.ssbtproject.R;

import java.util.ArrayList;


public class GroceryAdapter extends RecyclerView.Adapter<GroceryAdapter.ViewHolder> {
    Context context;
    ArrayList<GroceryProductModel> models;

    public GroceryAdapter(Context context, ArrayList<GroceryProductModel> models) {
        this.context = context;
        this.models = models;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_veg, parent,false);
        return new GroceryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.img_circle.setImageResource(models.get(position).getImg_circle());
        holder.img_grocery.setImageResource(models.get(position).getImg_grocery());
        holder.txt_grocery.setText(models.get(position).getTxt_grocery());
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_grocery,img_circle;
        TextView txt_grocery;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_circle = itemView.findViewById(R.id.img_circle);
            img_grocery = itemView.findViewById(R.id.img_grocery);
            txt_grocery = itemView.findViewById(R.id.txt_grocery);
        }
    }
}
