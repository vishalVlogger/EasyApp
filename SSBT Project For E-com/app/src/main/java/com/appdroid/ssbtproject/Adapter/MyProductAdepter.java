package com.appdroid.ssbtproject.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.appdroid.ssbtproject.Activity.LoginActivity;
import com.appdroid.ssbtproject.Activity.ProductDetails;
import com.appdroid.ssbtproject.Database.CardProductHolder;
import com.appdroid.ssbtproject.Database.CheckOutAmount;
import com.appdroid.ssbtproject.Database.UserPostsRoomDatabase;
import com.appdroid.ssbtproject.Holder.DailyOrderHolder;
import com.appdroid.ssbtproject.Holder.OrderProductHolder;
import com.appdroid.ssbtproject.Holder.PackHolder;
import com.appdroid.ssbtproject.Holder.ProductHolder;
import com.appdroid.ssbtproject.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MyProductAdepter extends RecyclerView.Adapter<MyProductAdepter.ViewHolder> {

    List<ProductHolder> list;
    Context context;
    private static final String TAG = "appdroidTech";
    public String isFromFavList = "not";

    public MyProductAdepter(List<ProductHolder> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selling, parent,false);
        return new MyProductAdepter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ProductHolder model = list.get(position);

        PackHolder packHolder = model.getSellUnit().get(0);

        holder.txt_Title.setText(model.getTitle());

        Log.d(TAG, "onBindViewHolder:ssssssss " + Float.isNaN(model.getQuntity()) +" productTitle "+model.getTitle());

        {

            Log.d(TAG, "onBindViewHolder:ssssssss " + model.getQuntity() +" productTitle "+model.getTitle());
            if (model.getUnit().equals("kg")) {
                if (packHolder.getPack().contains("gm")) {
                    int packValueInGram = Integer.parseInt(packHolder.getPack().replaceAll("[^0-9]", ""));
                    Log.d(TAG, "onBindViewHolder: " + packValueInGram);

                    Log.d(TAG, "onBindViewHoldersssssssss " + model.getQuntity() +" product : "+model.getTitle());

                    if (model.getQuntity() == null){
                        holder.txtOutOfStock.setVisibility(View.VISIBLE);
                        holder.addBTNframe.setVisibility(View.GONE);
                    }else {

                        if (Float.isNaN(model.getQuntity())){
                            holder.txtOutOfStock.setVisibility(View.VISIBLE);
                            holder.addBTNframe.setVisibility(View.GONE);
                        }else {
                            if (model.getQuntity() < packValueInGram) {
                                Log.d(TAG, "onBindViewHolder: out of stock " + model.getTitle());
                                holder.txtOutOfStock.setVisibility(View.VISIBLE);
                                holder.addBTNframe.setVisibility(View.GONE);
                                //      holder.txt_Title.setText("Out Of Stock");
                            }else {
                                holder.txtOutOfStock.setVisibility(View.GONE);
                                holder.addBTNframe.setVisibility(View.VISIBLE);
                            }
                        }
                    }


                } else if (packHolder.getPack().contains("kg")) {
                    int packValueInKG = Integer.parseInt(packHolder.getPack().replaceAll("[^0-9]", ""));
                    packValueInKG = (packValueInKG * 1000);
                    Log.d(TAG, "onBindViewHolder:kg " + packValueInKG);
                    if (model.getQuntity() != null){

                        if (Float.isNaN(model.getQuntity())){
                            holder.txtOutOfStock.setVisibility(View.VISIBLE);
                            holder.addBTNframe.setVisibility(View.GONE);
                        }else {
                            if (model.getQuntity() < packValueInKG) {
                                holder.txtOutOfStock.setVisibility(View.VISIBLE);
                                holder.addBTNframe.setVisibility(View.GONE);
                            }else {
                                holder.txtOutOfStock.setVisibility(View.GONE);
                                holder.addBTNframe.setVisibility(View.VISIBLE);
                            }
                        }

                    }else {
                        holder.txtOutOfStock.setVisibility(View.VISIBLE);
                        holder.addBTNframe.setVisibility(View.GONE);
                    }
                }
            } else if (model.getUnit().equals("liter")) {
                if (packHolder.getPack().contains("ml")) {
                    int packValueInMl = Integer.parseInt(packHolder.getPack().replaceAll("[^0-9]", ""));
                    Log.d(TAG, "onBindViewHolder: " + packValueInMl);
                    if (model.getQuntity() == null) {
                        holder.txtOutOfStock.setVisibility(View.VISIBLE);
                        holder.addBTNframe.setVisibility(View.GONE);
                    }else {
                        if (Float.isNaN(model.getQuntity())){
                            holder.txtOutOfStock.setVisibility(View.VISIBLE);
                            holder.addBTNframe.setVisibility(View.GONE);
                        }else {
                            if (model.getQuntity() < packValueInMl) {
                                Log.d(TAG, "onBindViewHolder: out of stock " + model.getTitle());
                                holder.txtOutOfStock.setVisibility(View.VISIBLE);
                                holder.addBTNframe.setVisibility(View.GONE);
                            } else {
                                holder.txtOutOfStock.setVisibility(View.GONE);
                                holder.addBTNframe.setVisibility(View.VISIBLE);
                            }
                        }

                    }


                } else if (packHolder.getPack().contains("liter")) {
                    int packValueInLiterToMl = Integer.parseInt(packHolder.getPack().replaceAll("[^0-9]", ""));
                    packValueInLiterToMl = (packValueInLiterToMl * 1000);
                    Log.d(TAG, "onBindViewHolder:ML " + packValueInLiterToMl);
                    if (model.getQuntity() != null) {
                        if (Float.isNaN(model.getQuntity())){
                            holder.txtOutOfStock.setVisibility(View.VISIBLE);
                            holder.addBTNframe.setVisibility(View.GONE);
                        }else {
                            if (model.getQuntity() < packValueInLiterToMl) {
                                holder.txtOutOfStock.setVisibility(View.VISIBLE);
                                holder.addBTNframe.setVisibility(View.GONE);
                            } else {
                                holder.txtOutOfStock.setVisibility(View.GONE);
                                holder.addBTNframe.setVisibility(View.VISIBLE);
                            }
                        }
                    }else{
                        holder.txtOutOfStock.setVisibility(View.VISIBLE);
                        holder.addBTNframe.setVisibility(View.GONE);
                    }
                }
            } else if (model.getUnit().equals("pcs")) {


                if (packHolder.getPack().contains("pcs")) {
                    int packValueInPices = Integer.parseInt(packHolder.getPack().replaceAll("[^0-9]", ""));
                    Log.d(TAG, "onBindViewHolder: oaAaaaaa" + model.getTitle() +" quntity "+model.getQuntity());
                    if (model.getQuntity() == null) {
                        Log.d(TAG, "onBindViewHolder: oaAaaaaa inside null -------"+model.getTitle());

                        holder.txtOutOfStock.setVisibility(View.VISIBLE);
                        holder.addBTNframe.setVisibility(View.GONE);
                    }else {

                        if (Float.isNaN(model.getQuntity())){
                            holder.txtOutOfStock.setVisibility(View.VISIBLE);
                            holder.addBTNframe.setVisibility(View.GONE);
                        }else {
                            if (model.getQuntity() < packValueInPices) {
                                Log.d(TAG, "onBindViewHolder: out of stock " + model.getTitle());
                                holder.txtOutOfStock.setVisibility(View.VISIBLE);
                                holder.addBTNframe.setVisibility(View.GONE);
                            } else {
                                holder.txtOutOfStock.setVisibility(View.GONE);
                                holder.addBTNframe.setVisibility(View.VISIBLE);
                            }
                        }

                    }
                }
            } else if (model.getUnit().equals("dozen")) {
                if (packHolder.getPack().contains("dozen") && !packHolder.getPack().contains("half-dozen")) {
                    int packValueInDozen = Integer.parseInt(packHolder.getPack().replaceAll("[^0-9]", ""));
                    int packValueInDozenToPices  = packValueInDozen*12;
                    if (model.getQuntity() != null ) {
                        if (Float.isNaN(model.getQuntity())){
                            holder.txtOutOfStock.setVisibility(View.VISIBLE);
                            holder.addBTNframe.setVisibility(View.GONE);
                        }else {
                            if (model.getQuntity() < packValueInDozenToPices) {
                                Log.d(TAG, "onBindViewHolder: out of stock " + model.getTitle());
                                holder.txtOutOfStock.setVisibility(View.VISIBLE);
                                holder.addBTNframe.setVisibility(View.GONE);
                            }else {
                                holder.txtOutOfStock.setVisibility(View.GONE);
                                holder.addBTNframe.setVisibility(View.VISIBLE);
                            }
                        }

                    }else {
                        holder.txtOutOfStock.setVisibility(View.VISIBLE);
                        holder.addBTNframe.setVisibility(View.GONE);
                    }

                }else if (packHolder.getPack().contains("half-dozen")) {
                    int packValueInDozenToPices  = 6;
                    if (model.getQuntity() != null) {

                        if (Float.isNaN(model.getQuntity())){
                            holder.txtOutOfStock.setVisibility(View.VISIBLE);
                            holder.addBTNframe.setVisibility(View.GONE);
                        }else {
                            if (model.getQuntity() < packValueInDozenToPices) {
                                Log.d(TAG, "onBindViewHolder: out of stock " + model.getTitle());
                                holder.txtOutOfStock.setVisibility(View.VISIBLE);
                                holder.addBTNframe.setVisibility(View.GONE);
                            }else {
                                holder.txtOutOfStock.setVisibility(View.GONE);
                                holder.addBTNframe.setVisibility(View.VISIBLE);
                            }
                        }

                    }else {
                        holder.txtOutOfStock.setVisibility(View.VISIBLE);
                        holder.addBTNframe.setVisibility(View.GONE);
                    }
                }
            }

            // pices and dusen work remain
        }



        holder.productItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductDetails.class);
                intent.putExtra("all",model);
                context.startActivity(intent);
            }
        });

        Glide.with(context)
                .load(model.getImage())
                .placeholder(R.drawable.ic_dandage_placeholder)
                .into(holder.img_product);

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean b = UserPostsRoomDatabase.getInstance(context)
                        .postsDao()
                        .isProductAvailble(model.getDocId());

                if (b){

                    CardProductHolder cardProductHolder = UserPostsRoomDatabase.getInstance(context)
                            .postsDao()
                            .findByProductId(model.getDocId());


                    ((Activity)context).runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            holder.txt_count.setVisibility(View.VISIBLE);
                            holder.li_cart.setVisibility(View.VISIBLE);
                            holder.txt_addItem.setVisibility(View.GONE);

                            holder.packAgeItems.setText(model.getSellUnit().get(cardProductHolder.getPack_id()).getPack());
                            holder.txt_price.setText(model.getSellUnit().get(cardProductHolder.getPack_id()).getPrice());
                            holder.perOff.setText(""+(int)holder.discountPercentage(Integer.parseInt(model.getSellUnit().get(cardProductHolder.getPack_id()).getPrice()),Integer.parseInt(model.getSellUnit().get(cardProductHolder.getPack_id()).getMrp()))+"%OFF");

                            holder.mrp.setText(model.getSellUnit().get(cardProductHolder.getPack_id()).getMrp());

                            holder.txt_count.setText(String.valueOf(cardProductHolder.getNumber_of_packs()));
                        }
                    });

                }else {
                    ((Activity)context).runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            holder.txt_addItem.setVisibility(View.VISIBLE);
                            holder.li_cart.setVisibility(View.GONE);

                            holder.packAgeItems.setText(packHolder.getPack());
                            holder.txt_price.setText(packHolder.getPrice());

                            Log.d("DDDDDDDAAAAS", "run: "+model.getTitle());

                            holder.perOff.setText(""+(int)holder.discountPercentage(Integer.parseInt(packHolder.getPrice()),Integer.parseInt(packHolder.getMrp()))+"%OFF");
                            holder.mrp.setText(packHolder.getMrp());
                        }
                    });


                }

                Log.d(TAG, "run: "+b);
            }
        }).start();



        holder.txt_addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!holder.txtOutOfStock.getText().toString().equals("Out Of Stock")){

                holder.li_cart.setVisibility(View.VISIBLE);
                holder.txt_addItem.setVisibility(View.GONE);
                CardProductHolder cardProductHolder = new CardProductHolder();

                cardProductHolder.setProduct_doc_id(model.getDocId());

                for (int i=0;i<model.getSellUnit().size();i++){
                    if (holder.packAgeItems.getText().toString().equals(model.getSellUnit().get(i).getPack())){
                        cardProductHolder.setPack_id(i);
                        cardProductHolder.setProductPrice(Integer.parseInt(model.getSellUnit().get(i).getPrice()));
                        cardProductHolder.setProductMrpPrice(Integer.parseInt(model.getSellUnit().get(i).getMrp()));

                    }
                }

                if (holder.txt_count.getVisibility() == View.INVISIBLE){
                    cardProductHolder.setNumber_of_packs(1);
                    holder.txt_count.setVisibility(View.VISIBLE);
                }else {
                    cardProductHolder.setNumber_of_packs(Integer.parseInt(holder.txt_count.getText().toString()));
                }


                MyProductAdepter.AddProductToBucket insertDownloadingTask = new MyProductAdepter.AddProductToBucket();
                insertDownloadingTask.execute(cardProductHolder);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        boolean b = UserPostsRoomDatabase.getInstance(context)
                                .postsDao()
                                .isCheckOutPriceAvailble("1");
                        Log.d(TAG, "runkkkkkkkkkkkkk: "+b);

                        if (!b){

                            CheckOutAmount checkOutAmount = new CheckOutAmount();

                            checkOutAmount.setFinalAmount(Integer.parseInt(holder.txt_price.getText().toString()));
                            checkOutAmount.setId("1");

                            UserPostsRoomDatabase.getInstance(context)
                                    .postsDao()
                                    .addCheckOutValue(checkOutAmount);
                        }else {

                            UserPostsRoomDatabase.getInstance(context)
                                    .postsDao()
                                    .updateCheckOutValue(Integer.parseInt(holder.txt_price.getText().toString()),"1");

                        }

                    }
                   }).start();

                }else {
                    Toast.makeText(context, "Product is out of Stock", Toast.LENGTH_SHORT).show();
                }

            }
        });

        holder.li_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int  packAgeID = 0;
                for (int i=0;i<model.getSellUnit().size();i++){
                    if (holder.packAgeItems.getText().toString().equals(model.getSellUnit().get(i).getPack())){
                        packAgeID = i;
                    }
                }

                PackHolder packHolder1 = model.getSellUnit().get(packAgeID);
                int count = Integer.parseInt(String.valueOf(holder.txt_count.getText()));
                count++;
                if (model.getUnit().equals("kg")){

                    if (packHolder1.getPack().contains("gm")){
                        int packValueInGram = Integer.parseInt(packHolder1.getPack().replaceAll("[^0-9]", ""));
                        packValueInGram = packValueInGram*(count);

                        if (model.getQuntity() != null) {
                            if (model.getQuntity() < packValueInGram){
                                //  holder.txt_Title.setText("Out Of Stock");
                                holder.addBTNframe.setVisibility(View.GONE);
                                holder.txtOutOfStock.setVisibility(View.VISIBLE);
                                Toast.makeText(context, "Product is out of Stock", Toast.LENGTH_SHORT).show();
                            }else {
                                holder.txt_Title.setText(model.getTitle());
                                holder.txtOutOfStock.setVisibility(View.GONE);
                                holder.updateCountOfProductsInBuket(model);
                            }
                        }else {
                            holder.addBTNframe.setVisibility(View.GONE);
                            holder.txtOutOfStock.setVisibility(View.VISIBLE);
                        }

                    }else if (packHolder1.getPack().contains("kg")){
                        int packValueInKGToGram = Integer.parseInt(packHolder1.getPack().replaceAll("[^0-9]", ""));
                        packValueInKGToGram = (packValueInKGToGram*1000);
                            packValueInKGToGram = (packValueInKGToGram*count);
                        if (model.getQuntity() != null) {
                            if (model.getQuntity()< packValueInKGToGram){
                                //holder.txt_Title.setText("Out Of Stock");
                                holder.addBTNframe.setVisibility(View.GONE);
                                holder.txtOutOfStock.setVisibility(View.VISIBLE);
                                Toast.makeText(context, "Product is out of Stock", Toast.LENGTH_SHORT).show();
                            }else {

                                holder.addBTNframe.setVisibility(View.VISIBLE);
                                holder.txtOutOfStock.setVisibility(View.GONE);
                                holder.updateCountOfProductsInBuket(model);
                            }

                        }else {
                            holder.addBTNframe.setVisibility(View.GONE);
                            holder.txtOutOfStock.setVisibility(View.VISIBLE);
                        }

                    }

                }else if (model.getUnit().equals("liter")){

                    if (packHolder1.getPack().contains("ml")){
                        int packValueInMl = Integer.parseInt(packHolder1.getPack().replaceAll("[^0-9]", ""));
                        packValueInMl = packValueInMl*(count);

                        if (model.getQuntity() != null) {
                            if (model.getQuntity() < packValueInMl){
                                holder.addBTNframe.setVisibility(View.GONE);
                                holder.txtOutOfStock.setVisibility(View.VISIBLE);
                                Toast.makeText(context, "Product is out of Stock", Toast.LENGTH_SHORT).show();
                            }else {
                                holder.txt_Title.setText(model.getTitle());
                                holder.txtOutOfStock.setVisibility(View.GONE);
                                holder.updateCountOfProductsInBuket(model);
                            }
                        }else {
                            holder.addBTNframe.setVisibility(View.GONE);
                            holder.txtOutOfStock.setVisibility(View.VISIBLE);
                        }

                    }else if (packHolder1.getPack().contains("liter")){
                        int packValueInLiterToMl = Integer.parseInt(packHolder1.getPack().replaceAll("[^0-9]", ""));
                        packValueInLiterToMl = (packValueInLiterToMl*1000);
                        packValueInLiterToMl = (packValueInLiterToMl*count);

                        if (model.getQuntity() != null) {
                            if (model.getQuntity()< packValueInLiterToMl){
                                holder.addBTNframe.setVisibility(View.GONE);
                                holder.txtOutOfStock.setVisibility(View.VISIBLE);
                                Toast.makeText(context, "Product is out of Stock", Toast.LENGTH_SHORT).show();
                            }else {
                                holder.txt_Title.setText(model.getTitle());
                                holder.txtOutOfStock.setVisibility(View.GONE);
                                holder.updateCountOfProductsInBuket(model);
                            }
                        }else {
                            holder.addBTNframe.setVisibility(View.GONE);
                            holder.txtOutOfStock.setVisibility(View.VISIBLE);
                        }


                    }
                }else if (model.getUnit().equals("pcs")){
                    if (packHolder1.getPack().contains("pcs")){
                        int packValueInMl = Integer.parseInt(packHolder1.getPack().replaceAll("[^0-9]", ""));
                        packValueInMl = packValueInMl*(count);

                        if (model.getQuntity() != null) {
                            if (model.getQuntity() < packValueInMl){
                                holder.txtOutOfStock.setVisibility(View.VISIBLE);
                                holder.addBTNframe.setVisibility(View.GONE);
                                Toast.makeText(context, "Product is out of Stock", Toast.LENGTH_SHORT).show();
                            }else {
                                holder.txt_Title.setText(model.getTitle());
                                holder.txtOutOfStock.setVisibility(View.GONE);
                                holder.updateCountOfProductsInBuket(model);
                            }
                        }else {
                            holder.txtOutOfStock.setVisibility(View.VISIBLE);
                            holder.addBTNframe.setVisibility(View.GONE);
                        }

                    }
                }else if (model.getUnit().equals("dozen")) {
                    if (packHolder1.getPack().contains("dozen") && !packHolder1.getPack().contains("half-dozen")) {
                        int packValueInDozen = Integer.parseInt(packHolder1.getPack().replaceAll("[^0-9]", ""));
                        int packValueInDozenToPices  = (packValueInDozen*12)*count;
                        Log.d(TAG, "onBindViewHolderdozen: " + packValueInDozenToPices);
                        if (model.getQuntity() != null) {
                            if (model.getQuntity() < packValueInDozenToPices) {
                                Log.d(TAG, "onBindViewHolder: out of stock " + model.getTitle());
                                holder.txtOutOfStock.setVisibility(View.VISIBLE);
                                holder.addBTNframe.setVisibility(View.GONE);
                            }else {
                                holder.txt_Title.setText(model.getTitle());
                                holder.txtOutOfStock.setVisibility(View.GONE);
                                holder.updateCountOfProductsInBuket(model);
                            }
                        }else {
                            holder.txtOutOfStock.setVisibility(View.VISIBLE);
                            holder.addBTNframe.setVisibility(View.GONE);
                        }

                    }else if (packHolder1.getPack().contains("half-dozen")) {
                        int packValueInDozenToPices  = 6;
                        packValueInDozenToPices = packValueInDozenToPices*count;
                        Log.d(TAG, "onBindViewHolderdozen: " + packValueInDozenToPices);
                        if (model.getQuntity() != null) {
                            if (model.getQuntity() < packValueInDozenToPices) {
                                Log.d(TAG, "onBindViewHolder: out of stock " + model.getTitle());
                                holder.txtOutOfStock.setVisibility(View.VISIBLE);
                                holder.addBTNframe.setVisibility(View.GONE);
                            }else {
                                holder.txt_Title.setText(model.getTitle());
                                holder.txtOutOfStock.setVisibility(View.GONE);
                                holder.updateCountOfProductsInBuket(model);
                            }
                        }else {
                            holder.txtOutOfStock.setVisibility(View.VISIBLE);
                            holder.addBTNframe.setVisibility(View.GONE);
                        }

                    }
                }
            }
        });

        holder.li_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(String.valueOf(holder.txt_count.getText()));
                if (count > 1) {
                    count--;
                    holder.txt_count.setText(String.valueOf(count));

                    int finalCount = count;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            UserPostsRoomDatabase.getInstance(context)
                                    .postsDao()
                                    .updateProductNumbers(finalCount,model.getDocId());
                        }
                    }).start();




                } else {
                    holder.txt_addItem.setVisibility(View.VISIBLE);
                    holder.li_cart.setVisibility(View.GONE);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            UserPostsRoomDatabase.getInstance(context).postsDao()
                                    .deleteProductFromCard(UserPostsRoomDatabase.getInstance(context).postsDao()
                                            .findByProductId(model.getDocId()));
                        }
                    }).start();

                }
                holder.txt_Title.setText(model.getTitle());
                holder.txtOutOfStock.setVisibility(View.GONE);// out of stock reverse karaychay ithe
            }
        });

        holder.popupMenu.getMenu().clear();

        for (int i=0;i<model.getSellUnit().size();i++){
            holder.popupMenu.getMenu().add(Menu.NONE,i,i,model.getSellUnit().get(i).getPack());
        }

        holder.packAgeItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.popupMenu.show();
            }
        });

        holder.popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                int id = item.getItemId();

                PackHolder packHolder1 = model.getSellUnit().get(id);
                holder.txt_price.setText(packHolder1.getPrice());
                holder.packAgeItems.setText(packHolder1.getPack());
                holder.perOff.setText(""+(int)holder.discountPercentage(Integer.parseInt(packHolder1.getPrice()),Integer.parseInt(packHolder1.getMrp()))+"%OFF");

                holder.mrp.setText(packHolder1.getMrp());

                if (model.getUnit().equals("kg")){
                    if (packHolder1.getPack().contains("gm")){
                        int packValueInGram = Integer.parseInt(packHolder1.getPack().replaceAll("[^0-9]", ""));


                        if (model.getQuntity() != null) {
                            if (model.getQuntity() < packValueInGram){
                                holder.txtOutOfStock.setVisibility(View.VISIBLE);
                                holder.addBTNframe.setVisibility(View.GONE);
                            }else {
                                holder.txt_Title.setText(model.getTitle());
                                holder.txtOutOfStock.setVisibility(View.GONE);
                                holder.addBTNframe.setVisibility(View.VISIBLE);
                            }
                        }else {
                            holder.txtOutOfStock.setVisibility(View.VISIBLE);
                            holder.addBTNframe.setVisibility(View.GONE);
                        }

                    }else if (packHolder1.getPack().contains("kg")){
                        int packValueInKG = Integer.parseInt(packHolder1.getPack().replaceAll("[^0-9]", ""));
                        packValueInKG = (packValueInKG*1000);
                        if (model.getQuntity() != null) {
                            if (model.getQuntity() < packValueInKG){
                                //   holder.txt_Title.setText("Out Of Stock");
                                holder.txtOutOfStock.setVisibility(View.VISIBLE);
                                holder.addBTNframe.setVisibility(View.GONE);
                            }else {
                                holder.txt_Title.setText(model.getTitle());
                                holder.txtOutOfStock.setVisibility(View.GONE);
                                holder.addBTNframe.setVisibility(View.VISIBLE);
                            }
                        }else {
                            holder.txtOutOfStock.setVisibility(View.VISIBLE);
                            holder.addBTNframe.setVisibility(View.GONE);
                        }


                    }
                }else if (model.getUnit().equals("liter")){

                    if (packHolder1.getPack().contains("ml")){
                        int packValueInMl = Integer.parseInt(packHolder1.getPack().replaceAll("[^0-9]", ""));

                        if (model.getQuntity() != null) {
                            if (model.getQuntity() < packValueInMl){
                                holder.txtOutOfStock.setVisibility(View.VISIBLE);
                                holder.addBTNframe.setVisibility(View.GONE);
                                Toast.makeText(context, "Product is out of Stock", Toast.LENGTH_SHORT).show();
                            }else {
                                holder.txt_Title.setText(model.getTitle());
                                holder.txtOutOfStock.setVisibility(View.GONE);
                                holder.addBTNframe.setVisibility(View.VISIBLE);
                            }
                        }else {
                            holder.txtOutOfStock.setVisibility(View.VISIBLE);
                            holder.addBTNframe.setVisibility(View.GONE);
                        }

                    }else if (packHolder1.getPack().contains("liter")){
                        int packValueInLiterToMl = Integer.parseInt(packHolder1.getPack().replaceAll("[^0-9]", ""));
                        packValueInLiterToMl = (packValueInLiterToMl*1000);

                        if (model.getQuntity() != null) {
                            if (model.getQuntity()< packValueInLiterToMl){
                                holder.txtOutOfStock.setVisibility(View.VISIBLE);
                                holder.addBTNframe.setVisibility(View.GONE);
                                Toast.makeText(context, "Product is out of Stock", Toast.LENGTH_SHORT).show();
                            }else {
                                holder.txt_Title.setText(model.getTitle());
                                holder.txtOutOfStock.setVisibility(View.GONE);
                                holder.addBTNframe.setVisibility(View.VISIBLE);
                            }
                        }else {
                            holder.txtOutOfStock.setVisibility(View.VISIBLE);
                            holder.addBTNframe.setVisibility(View.GONE);
                        }

                    }
                }else if (model.getUnit().equals("pcs")) {
                    if (packHolder1.getPack().contains("pcs")) {
                        int packValueInPices = Integer.parseInt(packHolder1.getPack().replaceAll("[^0-9]", ""));
                        Log.d(TAG, "onBindViewHolder: " + packValueInPices);
                        if (model.getQuntity() != null) {
                            if (model.getQuntity() < packValueInPices) {
                                Log.d(TAG, "onBindViewHolder: out of stock " + model.getTitle());
                                holder.txtOutOfStock.setVisibility(View.VISIBLE);
                                holder.addBTNframe.setVisibility(View.GONE);
                            }else {
                                holder.txt_Title.setText(model.getTitle());
                                holder.txtOutOfStock.setVisibility(View.GONE);
                                holder.addBTNframe.setVisibility(View.VISIBLE);
                            }
                        }else {
                            holder.txtOutOfStock.setVisibility(View.VISIBLE);
                            holder.addBTNframe.setVisibility(View.GONE);
                        }

                    }
                }else if (model.getUnit().equals("dozen")) {
                    if (packHolder1.getPack().contains("dozen") && !packHolder1.getPack().contains("half-dozen")) {
                        int packValueInDozen = Integer.parseInt(packHolder1.getPack().replaceAll("[^0-9]", ""));
                        int packValueInDozenToPices  = packValueInDozen*12;
                        Log.d(TAG, "onBindViewHolder: " + packValueInDozenToPices);
                        if (model.getQuntity() != null) {
                            if (model.getQuntity() < packValueInDozenToPices) {
                                Log.d(TAG, "onBindViewHolder: out of stock " + model.getTitle());
                                holder.txtOutOfStock.setVisibility(View.VISIBLE);
                                holder.addBTNframe.setVisibility(View.GONE);
                            }else {
                                holder.txt_Title.setText(model.getTitle());
                                holder.txtOutOfStock.setVisibility(View.GONE);
                                holder.addBTNframe.setVisibility(View.VISIBLE);
                            }
                        }else {
                            holder.txtOutOfStock.setVisibility(View.VISIBLE);
                            holder.addBTNframe.setVisibility(View.GONE);
                        }

                    }else if (packHolder1.getPack().contains("half-dozen")) {
                        int packValueInDozenToPices  = 6;
                        Log.d(TAG, "onBindViewHolder: " + packValueInDozenToPices);
                        if (model.getQuntity() != null) {
                            if (model.getQuntity() < packValueInDozenToPices) {
                                Log.d(TAG, "onBindViewHolder: out of stock " + model.getTitle());
                                holder.txtOutOfStock.setVisibility(View.VISIBLE);
                                holder.addBTNframe.setVisibility(View.GONE);
                            }else {
                                holder.txt_Title.setText(model.getTitle());
                                holder.txtOutOfStock.setVisibility(View.GONE);
                                holder.addBTNframe.setVisibility(View.VISIBLE);
                            }
                        }else {
                            holder.txtOutOfStock.setVisibility(View.VISIBLE);
                            holder.addBTNframe.setVisibility(View.GONE);
                        }

                    }
                }



                holder.deleteProductFromCard(model,id);
                return false;
            }
        });


        if (isFromFavList.equals("yes")){
            holder.deleteProductFromFavList.setVisibility(View.VISIBLE);
        }else {
            holder.deleteProductFromFavList.setVisibility(View.GONE);
        }
        holder.deleteProductFromFavList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                if (auth.getCurrentUser() != null) {
                    RemoveFromMyList(model);
                }
            }
        });
    }

    private void loginConfurmationDialog() {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.login_confurmaion_dialog);
        Button loginBTN = dialog.findViewById(R.id.loginBTN);

        dialog.show();
        loginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent  intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);
            }
        });
    }

    private void makeConfurmation(ProductHolder model, ViewHolder holder) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.daily_order_confurmation);
        Button yesBTN,noBTN;

        yesBTN = dialog.findViewById(R.id.yesBTN);
        noBTN = dialog.findViewById(R.id.noBTN);

        yesBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.txt_addItem.setVisibility(View.VISIBLE);
                holder.li_cart.setVisibility(View.GONE);

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                       CardProductHolder cardProductHolder =  UserPostsRoomDatabase.getInstance(context).postsDao()
                                .findByProductId(model.getDocId());

                        if (cardProductHolder != null){
                            UserPostsRoomDatabase.getInstance(context).postsDao()
                                    .deleteProductFromCard(cardProductHolder);
                        }

                    }
                }).start();

                AddProductForDailyOrder(model,dialog,holder);
            }
        });

        noBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void AddProductForDailyOrder(ProductHolder model, Dialog dialog, ViewHolder holder) {
        DocumentReference firestore  = FirebaseFirestore.getInstance().collection("DailyOrders")
                .document(FirebaseAuth.getInstance().getUid());

        firestore.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                OrderProductHolder orderProductHolder;

                    if (documentSnapshot.exists()) {


                        DailyOrderHolder holder1 = documentSnapshot.toObject(DailyOrderHolder.class);
                        List<OrderProductHolder> productsList = new ArrayList<>();

                        productsList.addAll(holder1.getProductsList());
                        Log.d(TAG, "onSuccess:exist");

                         orderProductHolder = new OrderProductHolder();

                        orderProductHolder.setProductTitle(model.getTitle());
                        orderProductHolder.setProductQty (Integer.parseInt(String.valueOf(holder.txt_count.getText())));
                        orderProductHolder.setProductPrice(holder.txt_price.getText().toString());
                        orderProductHolder.setProductPack(holder.packAgeItems.getText().toString());
                        orderProductHolder.setProductImage(model.getImage());
                        orderProductHolder.setProductDocId(model.getDocId());

                        productsList.add(orderProductHolder);
                        holder.txt_count.setText("1");


                        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        String strDate = dateFormat.format(new Date());

                        HashMap<String,Object> hashMap  = new HashMap<>();
                        hashMap.put("userID",FirebaseAuth.getInstance().getCurrentUser().getUid());
                        hashMap.put("productsList",productsList);
                        hashMap.put("date",strDate);
                        hashMap.put("mobileNumber",FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());   // ha number change karaycha aahe
                        hashMap.put("status","ordered");

                        DocumentReference firestore1  = FirebaseFirestore.getInstance().collection("DailyOrders")
                                .document(FirebaseAuth.getInstance().getUid());

                        firestore1.update(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "onSuccess: ");
                          //      holder.dailytOrderBTN.setVisibility(View.GONE);
                                dialog.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: "+e.getLocalizedMessage());
                            }
                        });

                    }else {

                        Log.d(TAG, "onSuccess: not exist");
                        List<OrderProductHolder> productsList = new ArrayList<>();

                         orderProductHolder = new OrderProductHolder();

                        orderProductHolder.setProductTitle(model.getTitle());
                        orderProductHolder.setProductImage(model.getImage());
                        orderProductHolder.setProductQty (Integer.parseInt(String.valueOf(holder.txt_count.getText())));
                        orderProductHolder.setProductPrice(holder.txt_price.getText().toString());
                        orderProductHolder.setProductPack(holder.packAgeItems.getText().toString());
                        orderProductHolder.setProductDocId(model.getDocId());
                        productsList.add(orderProductHolder);

                        holder.txt_count.setText("1");

                        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        String strDate = dateFormat.format(new Date());

                        HashMap<String,Object> hashMap  = new HashMap<>();
                        hashMap.put("userID",FirebaseAuth.getInstance().getCurrentUser().getUid());
                        hashMap.put("productsList",productsList);
                        hashMap.put("date",strDate);
                        hashMap.put("mobileNumber",FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());   // itehe pn




                       DocumentReference firestore1 = FirebaseFirestore.getInstance().collection("DailyOrders")
                               .document(FirebaseAuth.getInstance().getUid());


                       firestore1.set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                           @Override
                           public void onSuccess(Void aVoid) {
                               Log.d(TAG, "onSuccess: ");
                       //        holder.dailytOrderBTN.setVisibility(View.GONE);
                               dialog.dismiss();
                           }
                       }).addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {
                               Log.d(TAG, "onFailure: "+e.getLocalizedMessage());
                           }
                       });

                    }

                MyProductAdepter.AddDailyOrderProductToBucket insertDownloadingTask = new MyProductAdepter.AddDailyOrderProductToBucket();
                insertDownloadingTask.execute(orderProductHolder);
            }
        });
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

    public void setList(List<ProductHolder> list) {
        this.list = list;
    }

    public void setIsFromeFavList(String yes) {
        this.isFromFavList = yes;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_Title,txt_price,txt_addItem,txt_count,packAgeItems,mrp,perOff,txtOutOfStock;
        RelativeLayout li_cart;
        LinearLayout li_sub,li_add;
        LinearLayout productItem;
        ImageView img_product;
        PopupMenu popupMenu;
  //    Button dailytOrderBTN;

        Button deleteProductFromFavList;
        FrameLayout addBTNframe;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_product = itemView.findViewById(R.id.img_product);
            addBTNframe = itemView.findViewById(R.id.addBTNframe);

            txt_Title = itemView.findViewById(R.id.txt_Title);
            txt_price = itemView.findViewById(R.id.txt_price);

            productItem = itemView.findViewById(R.id.itemLayout);
            txt_addItem = itemView.findViewById(R.id.txt_addItem);
            txt_count = itemView.findViewById(R.id.txt_count);
            li_cart = itemView.findViewById(R.id.li_cart);
            li_sub = itemView.findViewById(R.id.li_sub);
            li_add = itemView.findViewById(R.id.li_add);
            packAgeItems = itemView.findViewById(R.id.packAgeItems);
            mrp = itemView.findViewById(R.id.mrp);
            perOff = itemView.findViewById(R.id.perOff);
            popupMenu = new PopupMenu(context,packAgeItems);
            txtOutOfStock = itemView.findViewById(R.id.txtOutOfStock);

            deleteProductFromFavList = itemView.findViewById(R.id.deleteProductFromFavList);
           // dailytOrderBTN = itemView.findViewById(R.id.dailytOrderBTN);
        }

        float discountPercentage(int SellingPrice, int MarketPrice) {
            // Calculating discount
            float discount = (float) MarketPrice - SellingPrice;

            // Calculating discount percentage
            float disPercent = (discount / MarketPrice) * 100;

            return disPercent;
        }

        public void deleteProductFromCard(ProductHolder model, int packID) {

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    // Insert Data
                    boolean b = UserPostsRoomDatabase.getInstance(context)
                            .postsDao()
                            .isProductAvailble(model.getDocId());
                    Log.d(TAG, "run: "+b);

                    if (b){
                        CardProductHolder cardProductHolder = UserPostsRoomDatabase.getInstance(context)
                                .postsDao()
                                .findByProductId(model.getDocId());

                        if (packID != cardProductHolder.getPack_id()){
                            UserPostsRoomDatabase.getInstance(context).postsDao()
                                    .deleteProductFromCard(cardProductHolder);

                            ((Activity)context).runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    li_cart.setVisibility(View.GONE);
                                    txt_count.setText("1");
                                    txt_addItem.setVisibility(View.VISIBLE);
                                }
                            });


                        }
                    }
                }
            });



        }

        public void updateCountOfProductsInBuket(ProductHolder model) {
            int count = Integer.parseInt(String.valueOf(txt_count.getText()));
            count++;
            txt_count.setText(String.valueOf(count));

            int finalCount = count;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    UserPostsRoomDatabase.getInstance(context)
                            .postsDao()
                            .updateProductNumbers(finalCount,model.getDocId());

                }
            }).start();

        }
    }

    private void RemoveFromMyList(ProductHolder model) {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        com.google.firebase.database.Query query = database.child("UsersWishList").child(auth.getCurrentUser().getUid()).orderByChild("docId").equalTo(model.getDocId());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot appleSnapshot : snapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                 /*   Drawable drawable = getResources().getDrawable(R.drawable.ic_heart_gary);
                    imgFav.setImageDrawable(drawable);
                    flag = "emp";*/
                    Toast.makeText(context, "Remove from you'r list..", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    public class AddProductToBucket extends AsyncTask<CardProductHolder,Void,Void> {
        @Override
        protected Void doInBackground(CardProductHolder... cardProduct) {
            UserPostsRoomDatabase.getInstance(context)
                    .postsDao()
                    .addProductToCard(cardProduct[0]);

            return null;
        }
    }

    public class AddDailyOrderProductToBucket extends AsyncTask<OrderProductHolder,Void,Void> {
        @Override
        protected Void doInBackground(OrderProductHolder... cardProduct) {
            UserPostsRoomDatabase.getInstance(context)
                    .postsDao()
                    .addDailyOrderProduct(cardProduct[0]);

            return null;
        }
    }

}
