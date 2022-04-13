package com.appdroid.ssbtproject.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appdroid.ssbtproject.Holder.OrderProductHolder;
import com.appdroid.ssbtproject.R;

import java.util.List;

public class OrderdProductsAdeoter extends RecyclerView.Adapter<OrderdProductsAdeoter.ViewHolder> {
    List<OrderProductHolder> list;
    Context context;

    public OrderdProductsAdeoter(List<OrderProductHolder> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.oders_product_item,parent,false);
        return new OrderdProductsAdeoter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        OrderProductHolder orderProductHolder = list.get(position);
        holder.productName.setText(orderProductHolder.getProductTitle()+" ("+orderProductHolder.getProductPack()+")");
        holder.txt_price.setText(orderProductHolder.getProductPrice());
        holder.productPackQuntity.setText(""+orderProductHolder.getProductQty());
        int price = Integer.parseInt(orderProductHolder.getProductPrice());
        int totalAmount = (price * orderProductHolder.getProductQty());
        holder.totalAmount.setText(""+totalAmount);

        if ((position % 2) == 0) {
            holder.row.setBackgroundResource(R.color.lightBlue);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
       TextView productName,totalAmount,productPackQuntity,txt_price;
       LinearLayout row;
        public ViewHolder(@NonNull View view) {
            super(view);
            row = view.findViewById(R.id.row);
            productName = view.findViewById(R.id.product_name);
            totalAmount = view.findViewById(R.id.amount);
            productPackQuntity = view.findViewById(R.id.quntity);
            txt_price = view.findViewById(R.id.price);
        }
    }
}
