package com.appdroid.ssbt_delivery_boy.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appdroid.ssbt_delivery_boy.Activity.Activity.OrderDetaild;
import com.appdroid.ssbt_delivery_boy.R;
import com.appdroid.ssbt_delivery_boy.holder.OrderHolder;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    Context context;
    List<OrderHolder> list;
    Date today;

    public OrderAdapter() {
    }

    public OrderAdapter(Context context, List<OrderHolder> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.oder_list,parent,false);
        today = new Date();
        return  new OrderAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, int position) {

        OrderHolder model = list.get(position);
        holder.orderdUserName.setText(model.getUserInfo().getName());
        holder.orderId.setText("Order ID: "+model.getOrderId());
        //holder.date.setText(""+model.getAssignDate());

        Date date = model.getAssignDate();
        String mainDate = DateFormat.getDateInstance().format(date);

        final long holderTime = model.getAssignDate().getTime();
        long todayTime = today.getTime();
        long difference = todayTime - holderTime;

        int numOfDay = (int) (difference/(1000*60*60*24));
        int min = (int) (difference/(1000*60));
        int hours = (int) (difference/(1000*60*60));

        if (min>=60){
            if (hours >= 24){
                if (numOfDay>=2){
                    holder.date.setText(mainDate);
                }else {
                    holder.date.setText(numOfDay+" day ago");
                }
            }else {
                holder.date.setText(hours+" hour ago");
            }
        }else if (min == 0){
            holder.date.setText("a seconds ago");
        }else if (min<10){
            holder.date.setText(min+" minutes ago");
        }

        holder.orderdUserName.setText(model.getUserInfo().getName());
        holder.deliveryAddress.setText(model.getUserInfo().getAddress());
        holder.userContactNumber.setText(model.getUserInfo().getNo());

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OrderDetaild.class);
                intent.putExtra("all",model);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderId,orderdUserName,date,userContactNumber,deliveryAddress;
        LinearLayout relativeLayout;
        public ViewHolder(@NonNull View view) {
            super(view);
            orderId = view.findViewById(R.id.orderId);
            orderdUserName = view.findViewById(R.id.orderdUserName);
            date = view.findViewById(R.id.date);
            relativeLayout = view.findViewById(R.id.layout);
            deliveryAddress = view.findViewById(R.id.deliveryAddress);
            userContactNumber = view.findViewById(R.id.userContactNumber);
        }
    }
}
