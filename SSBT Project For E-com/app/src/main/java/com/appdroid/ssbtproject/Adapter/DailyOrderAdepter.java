package com.appdroid.ssbtproject.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appdroid.ssbtproject.Database.UserPostsRoomDatabase;
import com.appdroid.ssbtproject.Holder.OrderProductHolder;
import com.appdroid.ssbtproject.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.makeramen.roundedimageview.RoundedImageView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class DailyOrderAdepter extends RecyclerView.Adapter<DailyOrderAdepter.ViewHolder>{
    Context context;
    List<OrderProductHolder> list;

    public DailyOrderAdepter(Context context, List<OrderProductHolder> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(context).inflate(R.layout.item_listing,parent,false);
        return new DailyOrderAdepter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderProductHolder model  = list.get(position);


        holder.pack.setText(model.getProductPack());
        holder.productTitle.setText(model.getProductTitle());
        holder.productPrice.setText("₹"+model.getProductPrice());

        long finalAmount = (model.getProductQty()*(Integer.parseInt(model.getProductPrice())));
        holder.totalAmount.setText("₹"+finalAmount);
        holder.productContity.setText(""+model.getProductQty());

        Glide.with(context).load(model.getProductImage()).into(holder.productImage);

        holder.deleteFromDailyOrderList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.remove_item_from_daily_order_list_confurmaion_dialog);
                Button yesBTN,noBTN;

                yesBTN = dialog.findViewById(R.id.yes);
                noBTN = dialog.findViewById(R.id.no);

                dialog.show();
                yesBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                UserPostsRoomDatabase.getInstance(context).postsDao()
                                        .deleteDailyOrderProduct(UserPostsRoomDatabase.getInstance(context).postsDao()
                                                .findByDailyOrderProductName(model.getProductTitle()));
                            }
                        }).start();
                        deleteProductFromList(position);
                    }
                });

                noBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        });
    }

    private void deleteProductFromList(int position) {
        List<OrderProductHolder> newList = new ArrayList<>();
        newList.addAll(list);
        newList.remove(position);

        if (list.size()>1){

            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String strDate = dateFormat.format(new Date());

            HashMap<String,Object> hashMap = new HashMap<>();
            hashMap.put("date",strDate);
            hashMap.put("userID",FirebaseAuth.getInstance().getUid());
            hashMap.put("productsList",newList);

            Task<Void> firestore = FirebaseFirestore.getInstance().collection("DailyOrders")
                    .document(FirebaseAuth.getInstance().getUid())
                    .update(hashMap);
            firestore.addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        notifyItemDelete(position);
                    }
                }
            });
        }else {
            Task<Void> firestore  = FirebaseFirestore.getInstance().collection("DailyOrders")
                    .document(FirebaseAuth.getInstance().getUid())
                    .delete();

            firestore.addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        notifyItemDelete(position);
                    }
                }
            });
        }


    }

    private void notifyItemDelete(int position) {
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,list.size());

        if (list.size()==0){
          //  SavedPostsActivity.noPostLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView productImage;
        TextView productTitle,productPrice,pack,productContity,totalAmount;
        ImageView deleteFromDailyOrderList;
        long finalAmount;
        public ViewHolder(@NonNull View view) {
            super(view);
            productImage = view.findViewById(R.id.img_product);
            productPrice = view.findViewById(R.id.txt_price);
            productTitle = view.findViewById(R.id.txt_Title);
            pack = view.findViewById(R.id.pack);
            deleteFromDailyOrderList = view.findViewById(R.id.deleteFromDailyOrderList);
            productContity = view.findViewById(R.id.productContity);
            totalAmount = view.findViewById(R.id.totalAmount);
        }
    }
}
