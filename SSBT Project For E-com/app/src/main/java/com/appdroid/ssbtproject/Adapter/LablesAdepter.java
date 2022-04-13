package com.appdroid.ssbtproject.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appdroid.ssbtproject.Activity.ListingProducts;
import com.appdroid.ssbtproject.Holder.LabelHolder;
import com.appdroid.ssbtproject.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class LablesAdepter extends RecyclerView.Adapter<LablesAdepter.ViewHolder> {
    List<LabelHolder> list;
    Context context;

    public LablesAdepter(List<LabelHolder> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.item_label,parent,false);
       return new LablesAdepter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LabelHolder  labelHolder = list.get(position);
        holder.txt_Product.setText(labelHolder.getTitle());
        Glide.with(context).load(labelHolder.getImage()).into(holder.backImage);
        holder.shopNowBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ListingProducts.class);
                intent.setAction("list");
                intent.putExtra("flag", labelHolder.getFlag());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView backImage;
        TextView txt_Product;
        Button shopNowBTN;
        public ViewHolder(@NonNull View view) {
            super(view);
            backImage = view.findViewById(R.id.backImage);
            txt_Product = view.findViewById(R.id.txt_Product);
            shopNowBTN = view.findViewById(R.id.shopNowBTN);
        }
    }
}
