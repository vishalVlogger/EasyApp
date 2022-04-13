package com.appdroid.ssbtproject.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.appdroid.ssbtproject.Database.CardProductHolder;
import com.appdroid.ssbtproject.Database.UserPostsRoomDatabase;
import com.appdroid.ssbtproject.Fragment.CartFragment;
import com.appdroid.ssbtproject.Holder.PackHolder;
import com.appdroid.ssbtproject.Holder.ProductHolder;
import com.appdroid.ssbtproject.R;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;

public class CardAdepter extends RecyclerView.Adapter<CardAdepter.ViewHolder> {
    Context context;
    List<CardProductHolder> list;
    List<ProductHolder> productHolders;
    FragmentActivity activity;
    public CardAdepter(Context context, List<CardProductHolder> list, FragmentActivity activity) {
        this.context = context;
        this.list = list;
        productHolders = new ArrayList<>();
        this.activity = activity;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selling, parent,false);


        return new CardAdepter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        CardProductHolder cardProductHolder = list.get(position);

        holder.getProducts(cardProductHolder,position);

        holder.li_cart.setVisibility(View.VISIBLE);
        holder.txt_addItem.setVisibility(View.GONE);

        holder.txt_count.setVisibility(View.VISIBLE);
        holder.txt_count.setText(String.valueOf(cardProductHolder.getNumber_of_packs()));

        holder.li_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // holder.loadingDialog.show();
                int count = Integer.parseInt(String.valueOf(holder.txt_count.getText()));
                count++;


                //holder.txt_count.setText(String.valueOf(count));

                int  packAgeID = 0;
                for (int i=0;i<holder.productHolder.getSellUnit().size();i++){
                    if (holder.packAgeItems.getText().toString().equals(holder.productHolder.getSellUnit().get(i).getPack())){
                        packAgeID = i;
                    }
                }

                PackHolder packHolder1 = holder.productHolder.getSellUnit().get(packAgeID);

                if (holder.productHolder.getUnit().equals("kg")){

                    if (packHolder1.getPack().contains("gm")){
                        int packValueInGram = Integer.parseInt(packHolder1.getPack().replaceAll("[^0-9]", ""));
                        packValueInGram = packValueInGram*(count);


                        if (holder.productHolder.getQuntity() < packValueInGram){
                            //holder.txt_Title.setText("Out Of Stock");
                            holder.txtOutOfStock.setVisibility(View.VISIBLE);
                            Toast.makeText(context, "Product is out of Stock", Toast.LENGTH_SHORT).show();
                        }else {
                            holder.txt_Title.setText(holder.productHolder.getTitle());
                            holder.txtOutOfStock.setVisibility(View.GONE);
                            holder.updateCard(count,cardProductHolder);
                        }
                    }else if (packHolder1.getPack().contains("kg")){
                        int packValueInKGToGram = Integer.parseInt(packHolder1.getPack().replaceAll("[^0-9]", ""));
                        packValueInKGToGram = (packValueInKGToGram*1000);
                        packValueInKGToGram = (packValueInKGToGram*count);
                        if (holder.productHolder.getQuntity()< packValueInKGToGram){
                            //holder.txt_Title.setText("Out Of Stock");
                            holder.txtOutOfStock.setVisibility(View.VISIBLE);
                            Toast.makeText(context, "Product is out of Stock", Toast.LENGTH_SHORT).show();
                        }else {
                            holder.txt_Title.setText(holder.productHolder.getTitle());
                            holder.txtOutOfStock.setVisibility(View.GONE);
                            holder.updateCard(count,cardProductHolder);
                        }

                    }

                }else if (holder.productHolder.getUnit().equals("liter")){

                    if (packHolder1.getPack().contains("ml")){
                        int packValueInMl = Integer.parseInt(packHolder1.getPack().replaceAll("[^0-9]", ""));
                        packValueInMl = packValueInMl*(count);

                        if (holder.productHolder.getQuntity() < packValueInMl){
                          //  holder.txt_Title.setText("Out Of Stock");
                            holder.txtOutOfStock.setVisibility(View.VISIBLE);
                            Toast.makeText(context, "Product is out of Stock", Toast.LENGTH_SHORT).show();
                        }else {
                            holder.txt_Title.setText(holder.productHolder.getTitle());
                            holder.updateCard(count,cardProductHolder);
                            holder.txtOutOfStock.setVisibility(View.GONE);
                        }
                    }else if (packHolder1.getPack().contains("liter")){
                        int packValueInLiterToMl = Integer.parseInt(packHolder1.getPack().replaceAll("[^0-9]", ""));
                        packValueInLiterToMl = (packValueInLiterToMl*1000);
                        packValueInLiterToMl = (packValueInLiterToMl*count);
                        if (holder.productHolder.getQuntity()< packValueInLiterToMl){
                           // holder.txt_Title.setText("Out Of Stock");
                            holder.txtOutOfStock.setVisibility(View.VISIBLE);
                            Toast.makeText(context, "Product is out of Stock", Toast.LENGTH_SHORT).show();
                        }else {
                            holder.txtOutOfStock.setVisibility(View.GONE);
                            holder.txt_Title.setText(holder.productHolder.getTitle());
                            holder.updateCard(count,cardProductHolder);
                        }
                    }
                }else if (holder.productHolder.getUnit().equals("pcs")){
                    if (packHolder1.getPack().contains("pcs")){
                        int packValueInMl = Integer.parseInt(packHolder1.getPack().replaceAll("[^0-9]", ""));
                        packValueInMl = packValueInMl*(count);

                        Log.d("SSSSSSSSSS", "onClick: "+holder.productHolder.getQuntity());

                        if (holder.productHolder.getQuntity() < packValueInMl){
                            //holder.txt_Title.setText("Out Of Stock");
                            holder.txtOutOfStock.setVisibility(View.VISIBLE);
                            Toast.makeText(context, "Product is out of Stock", Toast.LENGTH_SHORT).show();
                        }else {
                            holder.txt_Title.setText(holder.productHolder.getTitle());
                            holder.txtOutOfStock.setVisibility(View.GONE);
                            holder.updateCard(count,cardProductHolder);
                        }
                    }
                }else if (holder.productHolder.getUnit().equals("dozen")) {
                    if (packHolder1.getPack().contains("dozen") && !packHolder1.getPack().contains("half-dozen")) {
                        int packValueInDozen = Integer.parseInt(packHolder1.getPack().replaceAll("[^0-9]", ""));
                        int packValueInDozenToPices  = (packValueInDozen*12)*count;
                        if (holder.productHolder.getQuntity() < packValueInDozenToPices) {
                           // holder.txt_Title.setText("Out Of Stock");
                            holder.txtOutOfStock.setVisibility(View.VISIBLE);
                        }else {
                            holder.txt_Title.setText(holder.productHolder.getTitle());
                            holder.updateCard(count,cardProductHolder);
                            holder.txtOutOfStock.setVisibility(View.GONE);
                        }
                    }else if (packHolder1.getPack().contains("half-dozen")) {
                        int packValueInDozenToPices  = 6;
                        packValueInDozenToPices = packValueInDozenToPices*count;
                        if (holder.productHolder.getQuntity() < packValueInDozenToPices) {
                            holder.txtOutOfStock.setVisibility(View.VISIBLE);
                            //holder.txt_Title.setText("Out Of Stock");
                        }else {
                            holder.txt_Title.setText(holder.productHolder.getTitle());
                            holder.txtOutOfStock.setVisibility(View.GONE);
                            holder.updateCard(count,cardProductHolder);
                        }
                    }
                }

            }
        });

        holder.deleteProductFromCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.txt_addItem.setVisibility(View.VISIBLE);
                holder.li_cart.setVisibility(View.GONE);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        UserPostsRoomDatabase.getInstance(context).postsDao().deleteProductFromCard(cardProductHolder);


                        ((Activity)context).runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                //   CartFragment.totalAmount.setText(""+(Integer.parseInt(CartFragment.totalAmount.getText().toString()) - cardProductHolder.getProductPrice()));
                                //holder.loadingDialog.dismiss();
                                if (activity != null){
                                    CartFragment.reload(activity);
                                }

                            }
                        });


                    }
                }).start();


                notifyItemDeleted(position);
            }
        });

        holder.li_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //holder.loadingDialog.show();
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
                                    .updateProductNumbers(finalCount,cardProductHolder.getProduct_doc_id());

                            ((Activity)context).runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    Log.d("SSSSSSSSSSS", "run: "+(Integer.parseInt(CartFragment.totalAmount.getText().toString()) - cardProductHolder.getProductPrice()));
                                    if (activity != null){
                                        CartFragment.reload(activity);
                                    }
                                }
                            });
                        }
                    }).start();



                } else {

                    holder.txt_addItem.setVisibility(View.VISIBLE);
                    holder.li_cart.setVisibility(View.GONE);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            UserPostsRoomDatabase.getInstance(context).postsDao().deleteProductFromCard(cardProductHolder);


                            ((Activity)context).runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                 //   CartFragment.totalAmount.setText(""+(Integer.parseInt(CartFragment.totalAmount.getText().toString()) - cardProductHolder.getProductPrice()));
                                    //holder.loadingDialog.dismiss();
                                    if (activity != null){
                                        CartFragment.reload(activity);
                                    }

                                }
                            });


                        }
                    }).start();


                    notifyItemDeleted(position);
                }
            }
        });

    }

    private void notifyItemDeleted(int position) {
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,list.size());

        if (list.size()==0){
            if (context.getClass().getSimpleName().equals("Dashboard")){
                CartFragment.cardEmptyLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_Title,txt_price,txt_addItem,txt_count,packAgeItems,mrp,perOff,txtOutOfStock;
        RelativeLayout li_cart;
        LinearLayout li_sub,li_add;
        ImageView img_product;
        long productQuntity;
       // Dialog loadingDialog;
        Button deleteProductFromCard;
        ProductHolder productHolder;
        public ViewHolder(@NonNull View view) {
            super(view);
            img_product = itemView.findViewById(R.id.img_product);

           /* loadingDialog = new Dialog(context);
            loadingDialog.setCancelable(false);
            loadingDialog.setContentView(R.layout.progress_dialog);*/

            txt_Title = itemView.findViewById(R.id.txt_Title);
            txt_price = itemView.findViewById(R.id.txt_price);
            mrp = itemView.findViewById(R.id.mrp);
            perOff = itemView.findViewById(R.id.perOff);

            deleteProductFromCard = itemView.findViewById(R.id.deleteProductFromCard);
            deleteProductFromCard.setVisibility(View.VISIBLE);
            txt_addItem = itemView.findViewById(R.id.txt_addItem);
            txt_count = itemView.findViewById(R.id.txt_count);
            li_cart = itemView.findViewById(R.id.li_cart);
            li_sub = itemView.findViewById(R.id.li_sub);
            li_add = itemView.findViewById(R.id.li_add);
            packAgeItems = itemView.findViewById(R.id.packAgeItems);
            txtOutOfStock = itemView.findViewById(R.id.txtOutOfStock);

        }

        private void getProducts(CardProductHolder cardProductHolder,int position) {
            DocumentReference firestore = FirebaseFirestore.getInstance().collection("Product").document(cardProductHolder.getProduct_doc_id());

            firestore.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error != null){
                        Log.d("EEEEEE", "onEvent: "+error.getLocalizedMessage());
                        return;
                    }
                    if (value != null){
                        Log.d("DDDDDDDDDDDDD", "onEyovent: "+context.getClass().getSimpleName());
                        if (context != null){
                          productHolder = value.toObject(ProductHolder.class);



                         if (productHolder == null){
                                deleteFromCard(cardProductHolder,position);
                         }else if (Float.isNaN(productHolder.getQuntity()) || productHolder.getQuntity() == null){
                             Log.d("GFHHHHHHH", "onEvent: NAN");
                             deleteFromCard(cardProductHolder,position);
                         } else {

                             productHolders.add(productHolder);

                             PackHolder packHolder = productHolder.getSellUnit().get(cardProductHolder.getPack_id());

                             packAgeItems.setText(packHolder.getPack());
                             txt_Title.setText(productHolder.getTitle());

                             productQuntity = productHolder.getQuntity();


                             Glide.with(context.getApplicationContext())
                                     .load(productHolder.getImage())
                                     .into(img_product);


                             txt_price.setText(packHolder.getPrice());
                             mrp.setText(packHolder.getMrp());
                             perOff.setText(""+(int)discountPercentage(Integer.parseInt(packHolder.getPrice()),Integer.parseInt(packHolder.getMrp()))+"%OFF");

                             int count = Integer.parseInt(String.valueOf(txt_count.getText()));

                             int  packAgeID = 0;
                             for (int i=0;i<productHolder.getSellUnit().size();i++){
                                 if (packAgeItems.getText().toString().equals(productHolder.getSellUnit().get(i).getPack())){
                                     packAgeID = i;
                                 }
                             }

                             PackHolder packHolder1 = productHolder.getSellUnit().get(packAgeID);

                             if (productHolder.getUnit().equals("kg")){

                                 if (packHolder1.getPack().contains("gm")){
                                     int packValueInGram = Integer.parseInt(packHolder1.getPack().replaceAll("[^0-9]", ""));
                                     packValueInGram = packValueInGram*(count);


                                     if (productHolder.getQuntity() < packValueInGram){
                                         //holder.txt_Title.setText("Out Of Stock");
                                         txtOutOfStock.setVisibility(View.VISIBLE);

                                         Log.d("GFHHHHHHH", "onEvent: pack in gram from gram test "+packValueInGram+" quntity "+productHolder.getQuntity());

                                         deleteFromCard(cardProductHolder,position);
                                        // Toast.makeText(context, "Product is out of Stock", Toast.LENGTH_SHORT).show();
                                     }else {
                                         txt_Title.setText(productHolder.getTitle());
                                         txtOutOfStock.setVisibility(View.GONE);
                                     }
                                 }else if (packHolder1.getPack().contains("kg")){
                                     int packValueInKGToGram = Integer.parseInt(packHolder1.getPack().replaceAll("[^0-9]", ""));

                                     Log.d("GFHHHHHHH", "onEvent: pack in gram from KG test before multiplay "+packValueInKGToGram+" quntity "+productHolder.getQuntity());



                                     packValueInKGToGram = (packValueInKGToGram*1000);
                                     packValueInKGToGram = (packValueInKGToGram*count);
                                     if (productHolder.getQuntity()< packValueInKGToGram){
                                         //holder.txt_Title.setText("Out Of Stock");
                                         txtOutOfStock.setVisibility(View.VISIBLE);
                                         Log.d("GFHHHHHHH", "onEvent: pack in gram from KG test "+packValueInKGToGram+" quntity "+productHolder.getQuntity());

                                         deleteFromCard(cardProductHolder,position);
                                     //    Toast.makeText(context, "Product is out of Stock", Toast.LENGTH_SHORT).show();
                                     }else {
                                         txt_Title.setText(productHolder.getTitle());
                                         txtOutOfStock.setVisibility(View.GONE);

                                     }

                                 }

                             }else if (productHolder.getUnit().equals("liter")){

                                 if (packHolder1.getPack().contains("ml")){
                                     int packValueInMl = Integer.parseInt(packHolder1.getPack().replaceAll("[^0-9]", ""));
                                     packValueInMl = packValueInMl*(count);

                                     if (productHolder.getQuntity() < packValueInMl){
                                         //  holder.txt_Title.setText("Out Of Stock");
                                         txtOutOfStock.setVisibility(View.VISIBLE);
                                         deleteFromCard(cardProductHolder,position);
                                        // Toast.makeText(context, "Product is out of Stock", Toast.LENGTH_SHORT).show();
                                     }else {
                                         txt_Title.setText(productHolder.getTitle());

                                         txtOutOfStock.setVisibility(View.GONE);
                                     }
                                 }else if (packHolder1.getPack().contains("liter")){
                                     int packValueInLiterToMl = Integer.parseInt(packHolder1.getPack().replaceAll("[^0-9]", ""));
                                     packValueInLiterToMl = (packValueInLiterToMl*1000);
                                     packValueInLiterToMl = (packValueInLiterToMl*count);
                                     if (productHolder.getQuntity()< packValueInLiterToMl){
                                         // holder.txt_Title.setText("Out Of Stock");
                                         txtOutOfStock.setVisibility(View.VISIBLE);
                                         deleteFromCard(cardProductHolder,position);
                                       //  Toast.makeText(context, "Product is out of Stock", Toast.LENGTH_SHORT).show();
                                     }else {
                                         txtOutOfStock.setVisibility(View.GONE);
                                         txt_Title.setText(productHolder.getTitle());

                                     }
                                 }
                             }else if (productHolder.getUnit().equals("pcs")){
                                 if (packHolder1.getPack().contains("pcs")){
                                     int packValueInMl = Integer.parseInt(packHolder1.getPack().replaceAll("[^0-9]", ""));
                                     packValueInMl = packValueInMl*(count);

                                     Log.d("SSSSSSSSSS", "onClick: "+productHolder.getQuntity());

                                     if (productHolder.getQuntity() < packValueInMl){
                                         //holder.txt_Title.setText("Out Of Stock");
                                         txtOutOfStock.setVisibility(View.VISIBLE);
                                         deleteFromCard(cardProductHolder,position);
                                         Toast.makeText(context, "Product is out of Stock", Toast.LENGTH_SHORT).show();
                                     }else {
                                         txt_Title.setText(productHolder.getTitle());
                                         txtOutOfStock.setVisibility(View.GONE);
                                     }
                                 }
                             }else if (productHolder.getUnit().equals("dozen")) {
                                 if (packHolder1.getPack().contains("dozen") && !packHolder1.getPack().contains("half-dozen")) {
                                     int packValueInDozen = Integer.parseInt(packHolder1.getPack().replaceAll("[^0-9]", ""));
                                     int packValueInDozenToPices  = (packValueInDozen*12)*count;
                                     if (productHolder.getQuntity() < packValueInDozenToPices) {
                                         // holder.txt_Title.setText("Out Of Stock");
                                         txtOutOfStock.setVisibility(View.VISIBLE);
                                         deleteFromCard(cardProductHolder,position);
                                     }else {
                                         txt_Title.setText(productHolder.getTitle());

                                         txtOutOfStock.setVisibility(View.GONE);
                                     }
                                 }else if (packHolder1.getPack().contains("half-dozen")) {
                                     int packValueInDozenToPices  = 6;
                                     packValueInDozenToPices = packValueInDozenToPices*count;
                                     if (productHolder.getQuntity() < packValueInDozenToPices) {
                                         txtOutOfStock.setVisibility(View.VISIBLE);
                                         //holder.txt_Title.setText("Out Of Stock");
                                         deleteFromCard(cardProductHolder,position);
                                     }else {
                                         txt_Title.setText(productHolder.getTitle());
                                         txtOutOfStock.setVisibility(View.GONE);

                                     }
                                 }
                             }

                         }


                        }

                    }
                }
            });
        }

        float discountPercentage(int SellingPrice, int MarketPrice) {
            float discount = (float) MarketPrice - SellingPrice;

            float disPercent = (discount / MarketPrice) * 100;

            return disPercent;
        }

        public void deleteFromCard(CardProductHolder cardProductHolder,int position){
            txt_addItem.setVisibility(View.VISIBLE);
            li_cart.setVisibility(View.GONE);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    UserPostsRoomDatabase.getInstance(context).postsDao().deleteProductFromCard(cardProductHolder);


                    ((Activity)context).runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            //CartFragment.totalAmount.setText(""+(Integer.parseInt(CartFragment.totalAmount.getText().toString()) - cardProductHolder.getProductPrice()));
                            //holder.loadingDialog.dismiss();


                                if (activity != null){
                                    CartFragment.reload(activity);
                                }



                        }
                    });


                }
            }).start();

        }

        public void updateCard(int count, CardProductHolder cardProductHolder) {
            int finalCount = count;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    UserPostsRoomDatabase.getInstance(context)
                            .postsDao()
                            .updateProductNumbers(finalCount,cardProductHolder.getProduct_doc_id());

                    ((Activity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           // loadingDialog.dismiss();
                            if (activity != null){
                                CartFragment.reload(activity);
                            }
                        }
                    });
                }
            }).start();

        }
    }
}
