package com.appdroid.ssbtproject.Adapter;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.appdroid.ssbtproject.Database.CardProductHolder;
import com.appdroid.ssbtproject.Database.UserPostsRoomDatabase;
import com.appdroid.ssbtproject.Holder.ProductHolder;
import com.appdroid.ssbtproject.R;

public class ProductsAdepter// extends FirestorePagingAdapter<ProductHolder,ProductsAdepter.ViewHolder>
{

    private static final String TAG = "appdroidTech";
    Context context ;

    /*public ProductsAdepter(@NonNull FirestorePagingOptions<ProductHolder> options, Context context) {
        super(options);
        this.context = context;
    }*/

   /* @Override
    protected void onBindViewHolder(@NonNull ProductsAdepter.ViewHolder holder, int position, @NonNull ProductHolder model) {

        PackHolder packHolder = model.getSellUnit().get(0);

        holder.txt_Title.setText(model.getTitle());
        if (model.getUnit().equals("kg")){
            if (packHolder.getPack().contains("gm")){
                int packValueInGram = Integer.parseInt(packHolder.getPack().replaceAll("[^0-9]", ""));
                Log.d(TAG, "onBindViewHolder: "+packValueInGram);
                if (model.getQuntity() < packValueInGram){
                    Log.d(TAG, "onBindViewHolder: out of stock "+model.getTitle());
                        holder.txt_Title.setText("Out Of Stock");
                }
            }
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

                          holder.txt_count.setText(String.valueOf(cardProductHolder.getNumber_of_packs()));
                      }
                  });

              }else {
                  ((Activity)context).runOnUiThread(new Runnable() {

                      @Override
                      public void run() {
                          holder.packAgeItems.setText(packHolder.getPack());
                          holder.txt_price.setText(packHolder.getPrice());
                      }
                  });


              }

                Log.d(TAG, "run: "+b);
            }
        }).start();

        holder.txt_addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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


                AddProductToBucket insertDownloadingTask = new AddProductToBucket();
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


                //Log.d(TAG, "onClick: document Id : "+cardProductHolder.getProduct_doc_id()+" pack no "+cardProductHolder.getPack_id()+" number of pack "+ cardProductHolder.getNumber_of_packs());

            }
        });

        holder.li_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(String.valueOf(holder.txt_count.getText()));
                count++;
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
            }
        });



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

                holder.txt_price.setText(""+packHolder1.getPrice());
                holder.packAgeItems.setText(packHolder1.getPack());



                holder.deleteProductFromCard(model,id);
                return false;
            }
        });
    }

    @NonNull
    @Override
    public ProductsAdepter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selling, parent,false);
        return new ProductsAdepter.ViewHolder(view);
    }*/

    public class AddProductToBucket extends AsyncTask<CardProductHolder,Void,Void> {
        @Override
        protected Void doInBackground(CardProductHolder... cardProduct) {
            UserPostsRoomDatabase.getInstance(context)
                    .postsDao()
                    .addProductToCard(cardProduct[0]);

            return null;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_Title,txt_price,txt_addItem,txt_count,packAgeItems,perOff;
        LinearLayout li_cart,li_sub,li_add;
        ImageView img_product;
        PopupMenu popupMenu;
        LinearLayout productItem;

        long productQuantityInGram;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img_product = itemView.findViewById(R.id.img_product);

            txt_Title = itemView.findViewById(R.id.txt_Title);
            txt_price = itemView.findViewById(R.id.txt_price);

            productItem = itemView.findViewById(R.id.itemLayout);
            txt_addItem = itemView.findViewById(R.id.txt_addItem);
            txt_count = itemView.findViewById(R.id.txt_count);
            li_cart = itemView.findViewById(R.id.li_cart);
            li_sub = itemView.findViewById(R.id.li_sub);
            li_add = itemView.findViewById(R.id.li_add);
            packAgeItems = itemView.findViewById(R.id.packAgeItems);
            perOff = itemView.findViewById(R.id.perOff);
            popupMenu = new PopupMenu(context,packAgeItems);



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
                                    txt_addItem.setVisibility(View.VISIBLE);
                                }
                            });


                        }
                    }
                }
            });



        }
    }
}
