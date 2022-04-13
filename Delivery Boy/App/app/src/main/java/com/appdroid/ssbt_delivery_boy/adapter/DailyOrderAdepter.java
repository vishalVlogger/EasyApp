package com.appdroid.ssbt_delivery_boy.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appdroid.ssbt_delivery_boy.Activity.Activity.DailyOrderDetailActivity;
import com.appdroid.ssbt_delivery_boy.R;
import com.appdroid.ssbt_delivery_boy.holder.DailyOrderHolder;
import com.appdroid.ssbt_delivery_boy.holder.UserHolder;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class DailyOrderAdepter extends RecyclerView.Adapter<DailyOrderAdepter.ViewHolder> {
    Context context;
    List<DailyOrderHolder> list;
    Date today;

    public DailyOrderAdepter(Context context, List<DailyOrderHolder> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.oder_list,parent,false);
        today = new Date();
        return  new DailyOrderAdepter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DailyOrderHolder model = list.get(position);

        //holder.date.setText(model.getDate());
        holder.orderId.setVisibility(View.GONE);

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

        holder.getUserInfo(model);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DailyOrderDetailActivity.class);
                intent.putExtra("all",model);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderId,orderdUserName,date,userContactNumber,deliveryAddress;
        LinearLayout relativeLayout;
        public ViewHolder(@NonNull View view) {
            super(view);
            orderdUserName = view.findViewById(R.id.orderdUserName);
            date = view.findViewById(R.id.date);
            relativeLayout = view.findViewById(R.id.layout);
            orderId = view.findViewById(R.id.orderId);
            deliveryAddress = view.findViewById(R.id.deliveryAddress);
            userContactNumber = view.findViewById(R.id.userContactNumber);
        }

        public void getUserInfo(DailyOrderHolder model) {
            CollectionReference firestore = FirebaseFirestore.getInstance().collection("userInfo");
            firestore.whereEqualTo("userId",model.getUserID()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                       UserHolder userHolder = snapshot.toObject(UserHolder.class);
                       orderdUserName.setText(userHolder.getName());
                       deliveryAddress.setText(userHolder.getAddress());
                       userContactNumber.setText(userHolder.getNo());
                    }
                }
            });
        }
    }
}
