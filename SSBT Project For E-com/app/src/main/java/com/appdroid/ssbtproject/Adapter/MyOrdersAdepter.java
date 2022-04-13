package com.appdroid.ssbtproject.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appdroid.ssbtproject.Activity.OrderdProductList;
import com.appdroid.ssbtproject.Holder.OrderHolder;
import com.appdroid.ssbtproject.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MyOrdersAdepter extends RecyclerView.Adapter<MyOrdersAdepter.ViewHolder> {
    List<OrderHolder> list;
    Context context;

    public MyOrdersAdepter(List<OrderHolder> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.item_orders,parent,false);
       return new MyOrdersAdepter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            OrderHolder orderHolder = list.get(position);


            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, OrderdProductList.class);
                    intent.putExtra("all",orderHolder);
                    context.startActivity(intent);
                }
            });

            holder.txt_orderNo.setText(orderHolder.getOrderId());
       //     holder.txt_status.setText(orderHolder.getStatus());
            holder.txt_price.setText("| ₹"+orderHolder.getOrderTotal());
            holder.txt_item_count.setText("Total Items: "+orderHolder.getProductDetails().size());
      //      holder.txt_date_time.setText(getDateWithConvertion(orderHolder.getOrderDate()) + "| "+orderHolder.getOrderDate().getHours()+":"+orderHolder.getOrderDate().getMinutes());
            holder.txt_date_time.setText(getDateWithConvertion(orderHolder.getOrderDate()));

            if (orderHolder.getStatus().equals("ordered")){
                Drawable drawable = context.getResources().getDrawable(R.drawable.rectangle_left_cure_blue);
                holder.status_img_bg.setImageDrawable(drawable);
                holder.txt_status.setText("Order Placed Successfully.");
                holder.txt_status.setTextColor(context.getResources().getColor(R.color.placeOrderColor));

            }else if (orderHolder.getStatus().equals("cancelled")){
                Drawable drawable = context.getResources().getDrawable(R.drawable.rectangle_left_cure_red);
                holder.status_img_bg.setImageDrawable(drawable);
                holder.txt_status.setText("Order is Cancelled.");
                holder.txt_status.setTextColor(context.getResources().getColor(R.color.orderCansel));
            }else if (orderHolder.getStatus().equals("delivered")){
                Drawable drawable = context.getResources().getDrawable(R.drawable.rectangle_left_cure_green);
                holder.status_img_bg.setImageDrawable(drawable);
                holder.txt_status.setText("Order Delivered.");
                holder.txt_status.setTextColor(context.getResources().getColor(R.color.orderCompletedColor));
            }else if (orderHolder.getStatus().equals("delivery")){
                Drawable drawable = context.getResources().getDrawable(R.drawable.rectangle_left_cure_orange);
                holder.status_img_bg.setImageDrawable(drawable);
                holder.txt_status.setText("Order out for delivery coming very soon..");
                holder.txt_status.setTextColor(context.getResources().getColor(R.color.outForDelivery));
            }


    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private String getDateWithConvertion(Date orderDate) {
        String pattern = "EEEE, MMMM d, yyyy 'at' h:m a";
        DateFormat df = new SimpleDateFormat(pattern);
      //  Date today = Calendar.getInstance().getTime();

        String todayAsString = df.format(orderDate);

        return todayAsString;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_orderNo,txt_status,txt_item_count,txt_price;
        TextView txt_date_time;
        ImageView status_img_bg;
        LinearLayout layout;
        public ViewHolder(@NonNull View view) {
            super(view);
            layout = view.findViewById(R.id.layout);
            txt_status = view.findViewById(R.id.txt_way);
            txt_orderNo = view.findViewById(R.id.txt_orderNo);
            txt_item_count = view.findViewById(R.id.txt_item_count);
            txt_price = view.findViewById(R.id.txt_dollar);
            txt_date_time = view.findViewById(R.id.txt_date_time);
            status_img_bg = view.findViewById(R.id.img_bg);
        }
    }
}
