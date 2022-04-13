package com.appdroid.ssbtproject.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appdroid.ssbtproject.Adapter.MyProductAdepter;
import com.appdroid.ssbtproject.Adapter.ProductsAdepter;
import com.appdroid.ssbtproject.Database.CardProductHolder;
import com.appdroid.ssbtproject.Database.CheckOutAmount;
import com.appdroid.ssbtproject.Database.ProductViewModel;
import com.appdroid.ssbtproject.Database.UserPostsRoomDatabase;
import com.appdroid.ssbtproject.Holder.PackHolder;
import com.appdroid.ssbtproject.Holder.ProductHolder;
import com.appdroid.ssbtproject.R;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductDetails extends AppCompatActivity {

    private static final String TAG = "appdroidTech";
    ImageView productImage,img_back;
    TextView productTitle,productPrice,productPacks,txt_addItem,txt_count,numberOfItemsInCard,productDetails,perOff,mrp_price_in_detail;
    RelativeLayout li_cart;
    LinearLayout li_sub,li_add,similerProductLayout,suggestedProductLayout;
    ProductHolder productHolder;
    PopupMenu popupMenu;
    int cardItemsCount = 0;


    RecyclerView recyclerView,recyclerSimilerProduct,recyclerSuggestedProduct;
    FirestorePagingOptions<ProductHolder> vegetaiblesOptions;
    ProductsAdepter vegetablesAdepter;

    ProductViewModel productViewModel;

    MyProductAdepter productAdepter,similerProductsAdepter,suggestedProductsAdepter;
    List<ProductHolder> list,similerProductList,suggestedProductsList;

    private boolean isScrolling = false;
    private boolean isLastItemReached = false;

    private boolean isScrollingForSimilerProduct = false;
    private boolean isLastItemReachedForSimilerProduct = false;

    private boolean isScrollingForSuggestedProduct = false;
    private boolean isLastItemReachedForSuggestedProduct = false;


    DocumentSnapshot lastVisible,lastVisibleForSimilerProducts,lastVisibleForSuProducts;
    private int limit = 4;

    FrameLayout cardBTN,addBTNframe;
    TextView seeMoreProducts;
    TextView txtOutOfStock;
    ImageView shereBTN,imgFav;
    Dialog dialog;

    LinearLayout favLay;
    String flag = "emp";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        productHolder = (ProductHolder) getIntent().getSerializableExtra("all");

        favLay = findViewById(R.id.favLay);
        imgFav = findViewById(R.id.imgFav);

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.progress_dialog);

        shereBTN = findViewById(R.id.shereBTN);

        recyclerView = findViewById(R.id.recyclerSelling);
        recyclerView.hasFixedSize();
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager1);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerSimilerProduct = findViewById(R.id.recyclerSimilerProduct);
        recyclerSimilerProduct.hasFixedSize();
        RecyclerView.LayoutManager layoutManager12 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerSimilerProduct.setLayoutManager(layoutManager12);
        recyclerSimilerProduct.setItemAnimator(new DefaultItemAnimator());


        recyclerSuggestedProduct = findViewById(R.id.recyclerSuggestedProduct);
        recyclerSuggestedProduct.hasFixedSize();
        RecyclerView.LayoutManager suggestedProductRecylerManger = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerSuggestedProduct.setLayoutManager(suggestedProductRecylerManger);
        recyclerSuggestedProduct.setItemAnimator(new DefaultItemAnimator());

        productImage = findViewById(R.id.productImage);
        img_back = findViewById(R.id.img_back);
        similerProductLayout = findViewById(R.id.similerProductLayout);

        suggestedProductLayout = findViewById(R.id.suggestedProductLayout);

        productTitle = findViewById(R.id.productTitle);
        productPrice = findViewById(R.id.productPrice);
        productPacks = findViewById(R.id.productPacks);
        numberOfItemsInCard = findViewById(R.id.numberOfItemsInCard);
        productDetails = findViewById(R.id.productDetails);
        mrp_price_in_detail = findViewById(R.id.mrp_price_in_detail);

        seeMoreProducts = findViewById(R.id.seeMoreProducts);
        txtOutOfStock = findViewById(R.id.txtOutOfStock);
        addBTNframe = findViewById(R.id.addBTNframe);

        perOff = findViewById(R.id.perOff);

        cardBTN = findViewById(R.id.cardBTN);

        productViewModel  = ViewModelProviders.of(this).get(ProductViewModel.class);
        productViewModel.getCardsProductsCounts().observe(this, new Observer<List<CardProductHolder>>() {
            @Override
            public void onChanged(List<CardProductHolder> cardProductHolders) {
                numberOfItemsInCard.setText(""+cardProductHolders.size());
            }
        });

        list = new ArrayList<>();
        similerProductList = new ArrayList<>();
        suggestedProductsList = new ArrayList<>();

        productAdepter = new MyProductAdepter(list,ProductDetails.this);
        recyclerView.setAdapter(productAdepter);

        similerProductsAdepter = new MyProductAdepter(similerProductList,ProductDetails.this);
        recyclerSimilerProduct.setAdapter(similerProductsAdepter);

        suggestedProductsAdepter = new MyProductAdepter(suggestedProductsList,ProductDetails.this);
        recyclerSuggestedProduct.setAdapter(suggestedProductsAdepter);

        txt_addItem = findViewById(R.id.txt_addItem);
        li_cart = findViewById(R.id.li_cart);
        li_sub = findViewById(R.id.li_sub);
        li_add = findViewById(R.id.li_add);
        txt_count = findViewById(R.id.txt_count);

        productTitle.setText(productHolder.getTitle());
        Glide.with(this).load(productHolder.getImage()).into(productImage);

        productPacks.setText(productHolder.getSellUnit().get(0).getPack());
        productPrice.setText(""+productHolder.getSellUnit().get(0).getPrice());
        mrp_price_in_detail.setText(""+productHolder.getSellUnit().get(0).getMrp());
        perOff.setText(""+(int)discountPercentage(Integer.parseInt(productHolder.getSellUnit().get(0).getPrice()),Integer.parseInt(productHolder.getSellUnit().get(0).getMrp()))+"%OFF");

        productDetails.setText(productHolder.getDescription());
        popupMenu = new PopupMenu(ProductDetails.this,productPacks);

        shereBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                ganerateShareLinkForWhatsApp(productHolder,"all");
            }
        });

        for (int i=0;i<productHolder.getSellUnit().size();i++){
            popupMenu.getMenu().add(Menu.NONE,i,i,productHolder.getSellUnit().get(i).getPack());
        }
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();

                PackHolder packHolder1 = productHolder.getSellUnit().get(id);
                productPrice.setText(packHolder1.getPrice());
                productPacks.setText(packHolder1.getPack());
                txt_count.setText("1");
                perOff.setText(""+(int)discountPercentage(Integer.parseInt(packHolder1.getPrice()),Integer.parseInt(packHolder1.getMrp()))+"%OFF");

                mrp_price_in_detail.setText(packHolder1.getMrp());

                if (productHolder.getUnit().equals("kg")){
                    if (packHolder1.getPack().contains("gm")){
                        int packValueInGram = Integer.parseInt(packHolder1.getPack().replaceAll("[^0-9]", ""));

                        if (productHolder.getQuntity() < packValueInGram){
                          //  productTitle.setText("Out Of Stock");
                            txtOutOfStock.setVisibility(View.VISIBLE);
                            addBTNframe.setVisibility(View.GONE);
                        }else {
                            productTitle.setText(productHolder.getTitle());
                            txtOutOfStock.setVisibility(View.GONE);
                            addBTNframe.setVisibility(View.VISIBLE);
                        }
                    }else if (packHolder1.getPack().contains("kg")){
                        int packValueInKG = Integer.parseInt(packHolder1.getPack().replaceAll("[^0-9]", ""));
                        packValueInKG = (packValueInKG*1000);

                        if (productHolder.getQuntity() < packValueInKG){
                           // productTitle.setText("Out Of Stock");
                            txtOutOfStock.setVisibility(View.VISIBLE);
                            addBTNframe.setVisibility(View.GONE);
                        }else {
                            productTitle.setText(productHolder.getTitle());
                            txtOutOfStock.setVisibility(View.GONE);
                            addBTNframe.setVisibility(View.VISIBLE);
                        }

                    }
                }else if (productHolder.getUnit().equals("liter")){

                    if (packHolder1.getPack().contains("ml")){
                        int packValueInMl = Integer.parseInt(packHolder1.getPack().replaceAll("[^0-9]", ""));

                        if (productHolder.getQuntity() < packValueInMl){
                           // productTitle.setText("Out Of Stock");
                            txtOutOfStock.setVisibility(View.VISIBLE);
                            addBTNframe.setVisibility(View.GONE);
                            Toast.makeText(ProductDetails.this, "Product is out of Stock", Toast.LENGTH_SHORT).show();
                        }else {
                            productTitle.setText(productHolder.getTitle());
                            txtOutOfStock.setVisibility(View.GONE);
                            addBTNframe.setVisibility(View.VISIBLE);

                        }
                    }else if (packHolder1.getPack().contains("liter")){
                        int packValueInLiterToMl = Integer.parseInt(packHolder1.getPack().replaceAll("[^0-9]", ""));
                        packValueInLiterToMl = (packValueInLiterToMl*1000);

                        if (productHolder.getQuntity()< packValueInLiterToMl){
                       //     productTitle.setText("Out Of Stock");
                            txtOutOfStock.setVisibility(View.VISIBLE);
                            addBTNframe.setVisibility(View.GONE);
                            Toast.makeText(ProductDetails.this, "Product is out of Stock", Toast.LENGTH_SHORT).show();
                        }else {
                            txtOutOfStock.setVisibility(View.GONE);
                            addBTNframe.setVisibility(View.VISIBLE);
                            productTitle.setText(productHolder.getTitle());

                        }
                    }
                }else if (productHolder.getUnit().equals("pcs")) {
                    if (packHolder1.getPack().contains("pcs")) {
                        int packValueInPices = Integer.parseInt(packHolder1.getPack().replaceAll("[^0-9]", ""));
                        Log.d(TAG, "onBindViewHolder: " + packValueInPices);
                        if (productHolder.getQuntity() < packValueInPices) {
                            Log.d(TAG, "onBindViewHolder: out of stock " + productHolder.getTitle());
                           // productTitle.setText("Out Of Stock");
                            addBTNframe.setVisibility(View.GONE);
                            txtOutOfStock.setVisibility(View.VISIBLE);
                        }else {
                            txtOutOfStock.setVisibility(View.GONE);
                            addBTNframe.setVisibility(View.VISIBLE);
                            productTitle.setText(productHolder.getTitle());

                        }
                    }
                }else if (productHolder.getUnit().equals("dozen")) {
                    if (packHolder1.getPack().contains("dozen") && !packHolder1.getPack().contains("half-dozen")) {
                        int packValueInDozen = Integer.parseInt(packHolder1.getPack().replaceAll("[^0-9]", ""));
                        int packValueInDozenToPices  = packValueInDozen*12;
                        Log.d(TAG, "onBindViewHolder: " + packValueInDozenToPices);
                        if (productHolder.getQuntity() < packValueInDozenToPices) {
                            Log.d(TAG, "onBindViewHolder: out of stock " + productHolder.getTitle());
                            //productTitle.setText("Out Of Stock");
                            txtOutOfStock.setVisibility(View.VISIBLE);
                            addBTNframe.setVisibility(View.GONE);

                        }else {
                            txtOutOfStock.setVisibility(View.GONE);
                            addBTNframe.setVisibility(View.VISIBLE);
                            productTitle.setText(productHolder.getTitle());

                        }
                    }else if (packHolder1.getPack().contains("half-dozen")) {
                        int packValueInDozenToPices  = 6;
                        Log.d(TAG, "onBindViewHolder: " + packValueInDozenToPices);
                        if (productHolder.getQuntity() < packValueInDozenToPices) {
                            Log.d(TAG, "onBindViewHolder: out of stock " + productHolder.getTitle());
                            //productTitle.setText("Out Of Stock");
                            txtOutOfStock.setVisibility(View.VISIBLE);
                            addBTNframe.setVisibility(View.GONE);
                        }else {
                            txtOutOfStock.setVisibility(View.GONE);
                            addBTNframe.setVisibility(View.VISIBLE);
                            productTitle.setText(productHolder.getTitle());
                        }
                    }
                }

                deleteProductFromCard(productHolder,id);
                return false;
            }
        });

        productPacks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupMenu.show();
            }
        });

        txt_addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!txtOutOfStock.getText().toString().equals("Out Of Stock")){

                li_cart.setVisibility(View.VISIBLE);
                txt_addItem.setVisibility(View.GONE);
             //   numberOfItemsInCard.setText(""+(++cardItemsCount));

                CardProductHolder cardProductHolder = new CardProductHolder();

                cardProductHolder.setProduct_doc_id(productHolder.getDocId());

                for (int i=0;i<productHolder.getSellUnit().size();i++){
                    if (productPacks.getText().toString().equals(productHolder.getSellUnit().get(i).getPack())){
                        cardProductHolder.setPack_id(i);
                        cardProductHolder.setProductPrice(Integer.parseInt(productHolder.getSellUnit().get(i).getPrice()));
                        cardProductHolder.setProductMrpPrice(Integer.parseInt(productHolder.getSellUnit().get(i).getMrp()));
                    }
                }

                if (txt_count.getVisibility() == View.INVISIBLE){
                    cardProductHolder.setNumber_of_packs(1);
                    txt_count.setVisibility(View.VISIBLE);
                }else {
                    cardProductHolder.setNumber_of_packs(Integer.parseInt(txt_count.getText().toString()));
                }


                AddProductToBucket insertDownloadingTask = new AddProductToBucket();
                insertDownloadingTask.execute(cardProductHolder);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        boolean b = UserPostsRoomDatabase.getInstance(ProductDetails.this)
                                .postsDao()
                                .isCheckOutPriceAvailble("1");
                        Log.d(TAG, "runkkkkkkkkkkkkk: "+b);

                        if (!b){

                            CheckOutAmount checkOutAmount = new CheckOutAmount();

                            checkOutAmount.setFinalAmount(Integer.parseInt(productPrice.getText().toString()));
                            checkOutAmount.setId("1");

                            UserPostsRoomDatabase.getInstance(ProductDetails.this)
                                    .postsDao()
                                    .addCheckOutValue(checkOutAmount);
                        }else {

                            UserPostsRoomDatabase.getInstance(ProductDetails.this)
                                    .postsDao()
                                    .updateCheckOutValue(Integer.parseInt(productPrice.getText().toString()),"1");

                        }

                    }
                }).start();

             }else {
                    Toast.makeText(ProductDetails.this, "Product is out of Stock", Toast.LENGTH_SHORT).show();
                }
            }
        });

        li_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int  packAgeID = 0;
                for (int i=0;i<productHolder.getSellUnit().size();i++){
                    if (productPacks.getText().toString().equals(productHolder.getSellUnit().get(i).getPack())){
                        packAgeID = i;
                    }
                }

                PackHolder packHolder1 = productHolder.getSellUnit().get(packAgeID);
                int count = Integer.parseInt(String.valueOf(txt_count.getText()));
                count++;
                if (productHolder.getUnit().equals("kg")){

                    if (packHolder1.getPack().contains("gm")){
                        int packValueInGram = Integer.parseInt(packHolder1.getPack().replaceAll("[^0-9]", ""));
                        packValueInGram = packValueInGram*(count);

                        if (productHolder.getQuntity() < packValueInGram){
                         //   productTitle.setText("Out Of Stock");
                            txtOutOfStock.setVisibility(View.VISIBLE);
                            Toast.makeText(ProductDetails.this, "Product is out of Stock", Toast.LENGTH_SHORT).show();
                        }else {
                            productTitle.setText(productHolder.getTitle());
                            txtOutOfStock.setVisibility(View.GONE);
                            updateCountOfProductsInBuket(productHolder);
                        }
                    }else if (packHolder1.getPack().contains("kg")){
                        int packValueInKGToGram = Integer.parseInt(packHolder1.getPack().replaceAll("[^0-9]", ""));
                        packValueInKGToGram = (packValueInKGToGram*1000);
                        packValueInKGToGram = (packValueInKGToGram*count);
                        if (productHolder.getQuntity()< packValueInKGToGram){
                            //productTitle.setText("Out Of Stock");
                            txtOutOfStock.setVisibility(View.VISIBLE);
                            Toast.makeText(ProductDetails.this, "Product is out of Stock", Toast.LENGTH_SHORT).show();
                        }else {
                            productTitle.setText(productHolder.getTitle());
                            txtOutOfStock.setVisibility(View.GONE);
                            updateCountOfProductsInBuket(productHolder);
                        }

                    }

                }else if (productHolder.getUnit().equals("liter")){

                    if (packHolder1.getPack().contains("ml")){
                        int packValueInMl = Integer.parseInt(packHolder1.getPack().replaceAll("[^0-9]", ""));
                        packValueInMl = packValueInMl*(count);

                        if (productHolder.getQuntity() < packValueInMl){
                            //productTitle.setText("Out Of Stock");
                            txtOutOfStock.setVisibility(View.VISIBLE);
                            Toast.makeText(ProductDetails.this, "Product is out of Stock", Toast.LENGTH_SHORT).show();
                        }else {
                            productTitle.setText(productHolder.getTitle());
                            txtOutOfStock.setVisibility(View.GONE);
                            updateCountOfProductsInBuket(productHolder);
                        }
                    }else if (packHolder1.getPack().contains("liter")){
                        int packValueInLiterToMl = Integer.parseInt(packHolder1.getPack().replaceAll("[^0-9]", ""));
                        packValueInLiterToMl = (packValueInLiterToMl*1000);
                        packValueInLiterToMl = (packValueInLiterToMl*count);
                        if (productHolder.getQuntity()< packValueInLiterToMl){
                            //productTitle.setText("Out Of Stock");
                            txtOutOfStock.setVisibility(View.VISIBLE);
                            Toast.makeText(ProductDetails.this, "Product is out of Stock", Toast.LENGTH_SHORT).show();
                        }else {
                            productTitle.setText(productHolder.getTitle());
                            txtOutOfStock.setVisibility(View.GONE);
                            updateCountOfProductsInBuket(productHolder);
                        }
                    }
                }else if (productHolder.getUnit().equals("pcs")){
                    if (packHolder1.getPack().contains("pcs")){
                        int packValueInMl = Integer.parseInt(packHolder1.getPack().replaceAll("[^0-9]", ""));
                        packValueInMl = packValueInMl*(count);

                        if (productHolder.getQuntity() < packValueInMl){
                           // productTitle.setText("Out Of Stock");
                            txtOutOfStock.setVisibility(View.VISIBLE);
                            Toast.makeText(ProductDetails.this, "Product is out of Stock", Toast.LENGTH_SHORT).show();
                        }else {
                            productTitle.setText(productHolder.getTitle());
                            txtOutOfStock.setVisibility(View.GONE);
                            updateCountOfProductsInBuket(productHolder);
                        }
                    }
                }else if (productHolder.getUnit().equals("dozen")) {
                    if (packHolder1.getPack().contains("dozen") && !packHolder1.getPack().contains("half-dozen")) {
                        int packValueInDozen = Integer.parseInt(packHolder1.getPack().replaceAll("[^0-9]", ""));
                        int packValueInDozenToPices  = (packValueInDozen*12)*count;
                        Log.d(TAG, "onBindViewHolderdozen: " + packValueInDozenToPices);
                        if (productHolder.getQuntity() < packValueInDozenToPices) {
                            Log.d(TAG, "onBindViewHolder: out of stock " + productHolder.getTitle());
                            //productTitle.setText("Out Of Stock");
                            txtOutOfStock.setVisibility(View.VISIBLE);
                        }else {
                            productTitle.setText(productHolder.getTitle());
                            txtOutOfStock.setVisibility(View.GONE);
                            updateCountOfProductsInBuket(productHolder);
                        }
                    }else if (packHolder1.getPack().contains("half-dozen")) {
                        int packValueInDozenToPices  = 6;
                        packValueInDozenToPices = packValueInDozenToPices*count;
                        Log.d(TAG, "onBindViewHolderdozen: " + packValueInDozenToPices);
                        if (productHolder.getQuntity() < packValueInDozenToPices) {
                            Log.d(TAG, "onBindViewHolder: out of stock " + productHolder.getTitle());
                          //  productTitle.setText("Out Of Stock");
                            txtOutOfStock.setVisibility(View.VISIBLE);
                        }else {
                            productTitle.setText(productHolder.getTitle());
                            txtOutOfStock.setVisibility(View.GONE);
                            updateCountOfProductsInBuket(productHolder);
                        }
                    }
                }


            }
        });

        li_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(String.valueOf(txt_count.getText()));
                if (count > 1) {
                    count--;
                    txt_count.setText(String.valueOf(count));

                    int finalCount = count;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            UserPostsRoomDatabase.getInstance(ProductDetails.this)
                                    .postsDao()
                                    .updateProductNumbers(finalCount,productHolder.getDocId());
                        }
                    }).start();




                } else {
                    txt_addItem.setVisibility(View.VISIBLE);
                    li_cart.setVisibility(View.GONE);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            UserPostsRoomDatabase.getInstance(ProductDetails.this).postsDao()
                                    .deleteProductFromCard(UserPostsRoomDatabase.getInstance(ProductDetails.this).postsDao()
                                            .findByProductId(productHolder.getDocId()));
                        }
                    }).start();

                }
                productTitle.setText(productHolder.getTitle());
                txtOutOfStock.setVisibility(View.GONE);// out of stock reverse karaychay ithe
            }
        });

        seeMoreProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetails.this, ListingProducts.class);
                intent.setAction("list");
                intent.putExtra("flag",productHolder.getCategory());
                startActivity(intent);
            }
        });

        cardBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetails.this,Dashboard.class);
                intent.setAction("fromProductDetail");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean b = UserPostsRoomDatabase.getInstance(ProductDetails.this)
                        .postsDao()
                        .isProductAvailble(productHolder.getDocId());

                  cardItemsCount = UserPostsRoomDatabase.getInstance(ProductDetails.this)
                        .postsDao()
                        .getAllCardProductsList().size();

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                          //  numberOfItemsInCard.setText(""+cardItemsCount);
                    }
                });

                Log.d(TAG, "run: "+cardItemsCount);
                if (b){

                    CardProductHolder cardProductHolder = UserPostsRoomDatabase.getInstance(ProductDetails.this)
                            .postsDao()
                            .findByProductId(productHolder.getDocId());


                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            txt_count.setVisibility(View.VISIBLE);
                            li_cart.setVisibility(View.VISIBLE);
                            txt_addItem.setVisibility(View.GONE);

                            productPacks.setText(productHolder.getSellUnit().get(cardProductHolder.getPack_id()).getPack());
                            productPrice.setText(productHolder.getSellUnit().get(cardProductHolder.getPack_id()).getPrice());

                            txt_count.setText(String.valueOf(cardProductHolder.getNumber_of_packs()));
                        }
                    });

                }else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            productPacks.setText(productHolder.getSellUnit().get(0).getPack());
                            productPrice.setText(productHolder.getSellUnit().get(0).getPrice());
                        }
                    });


                }

                Log.d(TAG, "run: "+b);
            }
        }).start();

        if (productHolder.getQuntity() != null){
            if (Float.isNaN(productHolder.getQuntity())){
                addBTNframe.setVisibility(View.GONE);
                txtOutOfStock.setVisibility(View.VISIBLE);
            }else {
                checkAvailbility();
            }

        }else {
            addBTNframe.setVisibility(View.GONE);
            txtOutOfStock.setVisibility(View.VISIBLE);
        }


        favLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                if (auth.getCurrentUser() != null) {
                    if (flag.equals("AV")) {
                        RemoveFromMyList();
                    } else {
                        addToMyList();
                    }

                } else {

                    Toast.makeText(ProductDetails.this, "Login to save favourite.", Toast.LENGTH_SHORT).show();

                    Dialog dialog = new Dialog(ProductDetails.this);
                    dialog.setContentView(R.layout.login_confurmaion_dialog);
                    TextView txt = dialog.findViewById(R.id.txt);
                    Button button = dialog.findViewById(R.id.loginBTN);

                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            startActivity(new Intent(ProductDetails.this,LoginActivity.class));
                        }
                    });

                    txt.setText("You need to log in first for adding product in favorite list");
                    dialog.show();
                }
            }
        });

        getFistTimeData();

        getSimilerProductsData();
        getSuggetedProducts();
        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            getMovieIsAddedToFav();
        }

    }

    private void getSuggetedProducts() {
//        Log.d(TAG, "getSuggetedProducts: "+productHolder.getSuggestTags().size());

        if (productHolder.getSuggestTags() != null && productHolder.getSuggestTags().size()>0){
            Query query = FirebaseFirestore.getInstance().collection("Product").whereArrayContainsAny("suggestTags",productHolder.getSuggestTags()).limit(limit);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot snapshot : task.getResult()) {
                            ProductHolder holder = snapshot.toObject(ProductHolder.class);
                            holder.setDocId(snapshot.getId());
                            if ((holder.getVisibility() == null || !holder.getVisibility().equals("hide")) && !holder.getTitle().equals(productHolder.getTitle())) {
                                suggestedProductsList.add(holder);
                                Log.d(TAG, "onComplete:sdsdsfsd "+holder.getTitle());
                            }
                        }
                        if (task.getResult().size() > 0) {
                              suggestedProductsAdepter.notifyDataSetChanged();
                              lastVisibleForSuProducts = task.getResult().getDocuments().get(task.getResult().size() - 1);
                        } else {
                             suggestedProductLayout.setVisibility(View.GONE);
                        }

                        RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener(){
                            @Override
                            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                                super.onScrollStateChanged(recyclerView, newState);
                                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                                    isScrollingForSuggestedProduct = true;
                                }
                            }

                            @Override
                            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                super.onScrolled(recyclerView, dx, dy);

                                LinearLayoutManager linearLayoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
                                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                                int visibleItemCount = linearLayoutManager.getChildCount();
                                int totalItemCount = linearLayoutManager.getItemCount();

                                Log.d(TAG, "first visible "+firstVisibleItemPosition+" visible item count "+visibleItemCount +" total  "+totalItemCount +" is scroll "+isScrollingForSimilerProduct+" reach last "+isLastItemReachedForSimilerProduct);

                                if (isScrollingForSuggestedProduct && (firstVisibleItemPosition + visibleItemCount == totalItemCount) && !isLastItemReachedForSuggestedProduct) {
                                    isScrollingForSuggestedProduct = false;

                                    Query nextQuery = null;

                                    nextQuery = FirebaseFirestore.getInstance().collection("Product")
                                            .whereArrayContainsAny("suggestTags",productHolder.getSuggestTags())
                                            .startAfter(lastVisibleForSuProducts).limit(limit);



                                    nextQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> t) {
                                            if (t.isSuccessful()) {
                                                for (DocumentSnapshot d : t.getResult()) {
                                                    ProductHolder productModel = d.toObject(ProductHolder.class);
                                                    productModel.setDocId(d.getId());
                                                    if ((productModel.getVisibility() == null || !productModel.getVisibility().equals("hide") ) && !productModel.getTitle().equals(productHolder.getTitle())){
                                                        suggestedProductsList.add(productModel);
                                                    }
                                                }
                                                suggestedProductsAdepter.notifyDataSetChanged();

                                                if (t.getResult().size() == 0){
                                                    Log.d("lastvisible", "onComplete: post samplya "+t.getResult().size());

                                                }else {
                                                    Log.d("lastvisible", "onComplete: post samplya "+t.getResult().size());

                                                    lastVisibleForSuProducts = t.getResult().getDocuments().get(t.getResult().size() - 1);
                                                    if (t.getResult().size() < limit) {
                                                        isLastItemReachedForSuggestedProduct = true;
                                                    }
                                                }
                                            }else {

                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("DDDDDDDDDDDDDDDDAAAAAAA", "onFailure: "+e.getLocalizedMessage());
                                        }
                                    });
                                }


                            }
                        };

                        recyclerSuggestedProduct.addOnScrollListener(onScrollListener);


                    }
                }
            });
        }else {
            suggestedProductLayout.setVisibility(View.GONE);
        }



    }

    private void getSimilerProductsData() {

        similerProductList.clear();
        Log.d(TAG, "getSimilerProductsData: "+productHolder.getSimilarTags());

        if (productHolder.getSimilarTags() != null){
            if (!productHolder.getSimilarTags().equals("")){
                Query query = FirebaseFirestore.getInstance().collection("Product").whereEqualTo("similarTags",productHolder.getSimilarTags()).limit(limit);
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (DocumentSnapshot snapshot : task.getResult()) {
                                ProductHolder holder = snapshot.toObject(ProductHolder.class);
                                holder.setDocId(snapshot.getId());
                                Log.d(TAG, "onCompleteAAAAAAA: this product "+holder.getTitle()+" outside product "+productHolder.getTitle());
                                if ((holder.getVisibility() == null || !holder.getVisibility().equals("hide")) && !holder.getTitle().equals(productHolder.getTitle())){
                                    similerProductList.add(holder);
                                }
                            }
                            if (task.getResult().size()>0){
                                similerProductsAdepter.notifyDataSetChanged();
                                lastVisibleForSimilerProducts = task.getResult().getDocuments().get(task.getResult().size() - 1);
                            }else {
                                similerProductLayout.setVisibility(View.GONE);
                            }
                            Log.d("lastvisibleaaaaasssssss", "lastvisibleaaaaasssssss: "+task.getResult().size());



                            RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener(){
                                @Override
                                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                                    super.onScrollStateChanged(recyclerView, newState);
                                    if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                                        isScrollingForSimilerProduct = true;
                                    }
                                }

                                @Override
                                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                    super.onScrolled(recyclerView, dx, dy);

                                    LinearLayoutManager linearLayoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
                                    int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                                    int visibleItemCount = linearLayoutManager.getChildCount();
                                    int totalItemCount = linearLayoutManager.getItemCount();

                                    Log.d(TAG, "first visible "+firstVisibleItemPosition+" visible item count "+visibleItemCount +" total  "+totalItemCount +" is scroll "+isScrollingForSimilerProduct+" reach last "+isLastItemReachedForSimilerProduct);

                                    if (isScrollingForSimilerProduct && (firstVisibleItemPosition + visibleItemCount == totalItemCount) && !isLastItemReachedForSimilerProduct) {
                                        isScrollingForSimilerProduct = false;

                                        Query nextQuery = null;

                                        nextQuery = FirebaseFirestore.getInstance().collection("Product")
                                                .whereEqualTo("similarTags",productHolder.getSimilarTags())
                                                .startAfter(lastVisibleForSimilerProducts).limit(limit);



                                        nextQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> t) {
                                                if (t.isSuccessful()) {
                                                    for (DocumentSnapshot d : t.getResult()) {
                                                        ProductHolder productModel = d.toObject(ProductHolder.class);
                                                        productModel.setDocId(d.getId());
                                                        if (productModel.getVisibility() == null || !productModel.getVisibility().equals("hide")){
                                                            similerProductList.add(productModel);
                                                        }
                                                    }
                                                    similerProductsAdepter.notifyDataSetChanged();

                                                    if (t.getResult().size() == 0){
                                                        Log.d("lastvisible", "onComplete: post samplya "+t.getResult().size());

                                                    }else {
                                                        Log.d("lastvisible", "onComplete: post samplya "+t.getResult().size());

                                                        lastVisibleForSimilerProducts = t.getResult().getDocuments().get(t.getResult().size() - 1);
                                                        if (t.getResult().size() < limit) {
                                                            isLastItemReachedForSimilerProduct = true;
                                                        }
                                                    }
                                                }else {

                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d("DDDDDDDDDDDDDDDDAAAAAAA", "onFailure: "+e.getLocalizedMessage());
                                            }
                                        });
                                    }


                                }
                            };

                            recyclerSimilerProduct.addOnScrollListener(onScrollListener);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Log.d(TAG, "onFailuressssssaaa: "+e.getLocalizedMessage());
                    }
                });
            }else {
                similerProductLayout.setVisibility(View.GONE);
            }
        }else {
            similerProductLayout.setVisibility(View.GONE);
        }




    }

    private void getMovieIsAddedToFav() {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("UsersWishList").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MyLishHolder myLishHolder = snapshot.getValue(MyLishHolder.class);
                    if (myLishHolder.getDocId().equals(productHolder.getDocId())) {
                        Drawable drawable = getResources().getDrawable(R.drawable.ic_heart);
                        imgFav.setImageDrawable(drawable);
                        flag = "AV";
                    } else {

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void addToMyList() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("pName",productHolder.getTitle());
        hashMap.put("docId",productHolder.getDocId());

        database.child("UsersWishList").child(auth.getCurrentUser().getUid()).push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ProductDetails.this, "Product added to your list..", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProductDetails.this, "Failed..", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void RemoveFromMyList() {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        com.google.firebase.database.Query query = database.child("UsersWishList").child(auth.getCurrentUser().getUid()).orderByChild("docId").equalTo(productHolder.getDocId());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot appleSnapshot : snapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                    Drawable drawable = getResources().getDrawable(R.drawable.ic_heart_gary);
                    imgFav.setImageDrawable(drawable);
                    flag = "emp";
                    Toast.makeText(ProductDetails.this, "Remove from you'r list..", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void ganerateShareLinkForWhatsApp(ProductHolder model, String w) {

        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://ssbtproject.page.link/"+model.getDocId()+"/fromDandagezStore"))
                .setDomainUriPrefix("https://ssbtproject.page.link")

                        // Open links with this app on Android
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder(getPackageName())
                            .setMinimumVersion(1)
                            .setFallbackUrl(Uri.parse("https://play.google.com/store/apps/details?id=com.appdroid.ssbtproject"))
                            .build())



                .setNavigationInfoParameters(new DynamicLink.NavigationInfoParameters.Builder()
                        .setForcedRedirectEnabled(true)
                        .build())

                .setSocialMetaTagParameters(
                        new DynamicLink.SocialMetaTagParameters.Builder()
                                .setTitle(model.getTitle())
                                .setImageUrl(Uri.parse(model.getImage()))
                                .setDescription(model.getTitle())
                                .build())


                .buildDynamicLink();

        Uri dynamicLinkUri = dynamicLink.getUri();

        Log.d(TAG, "ganerateShareLinkForWhatsApp: "+dynamicLinkUri);
        //Log.d("JJJJJJJJJJJ", "GanerateDynamicLink: preview link "+dynamicLinkUri);
        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLongLink(dynamicLinkUri)
                .buildShortDynamicLink();
/*
           Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLongLink(Uri.parse("https://jalgaonlive.page.link/?link="+newsHolder.getLink()+"/&apn=com.techdrift.jalgaonlive"))
                .buildShortDynamicLink();*/

    /*    Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLongLink(Uri.parse("https://mahanewslive.page.link/?docid="+"/"+model.getDocId()+))
                .buildShortDynamicLink();*/


        shortLinkTask.addOnCompleteListener(new OnCompleteListener<ShortDynamicLink>() {
            @Override
            public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                if (task.isSuccessful()){
                    Uri shortLink = task.getResult().getShortLink();
                    Uri flowchartLink = task.getResult().getPreviewLink();
                    Log.d("JJJJJJJJJJJ", "GanerateDynamicLink: short link "+shortLink);
                    Log.d("JJJJJJJJJJJ", "GanerateDynamicLink: preview link "+flowchartLink);

                    dialog.dismiss();

                    String shareBody = String.valueOf(shortLink);
                    Log.d(TAG, "onCompleteeeeeeeeee: "+shareBody);
                    String shareNews = "*Check this out : "+model.getTitle()+"*"+"\n" +
                            "\n" +
                            "*Price At Just : "+productPrice.getText().toString()+"Rs*\n\n "+shareBody+"\n" +
                            "\n";

                    if (w.equals("all")){
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_SUBJECT, "SS");
                        intent.putExtra(Intent.EXTRA_TEXT, shareNews);
                        startActivity(Intent.createChooser(intent, "Share"));
                    }else if (w.equals("w")){
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, shareNews);
                        sendIntent.setType("text/plain");
                        sendIntent.setPackage("com.whatsapp");
                        startActivity(sendIntent);
                    }else if (w.equals("t")){
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://telegram.me/share/url?url="+shareNews));
                        startActivity(intent);
                    }

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Log.d("JJJJJJJJJJJ", "error "+e.getLocalizedMessage());
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
                UserPostsRoomDatabase.getInstance(ProductDetails.this)
                        .postsDao()
                        .updateProductNumbers(finalCount,model.getDocId());

            }
        }).start();

    }

    private void checkAvailbility() {
        {
            PackHolder packHolder = productHolder.getSellUnit().get(0);
            if (productHolder.getUnit().equals("kg")) {
                if (packHolder.getPack().contains("gm")) {
                    int packValueInGram = Integer.parseInt(packHolder.getPack().replaceAll("[^0-9]", ""));
                    Log.d(TAG, "onBindViewHolder: " + packValueInGram);
                    if (productHolder.getQuntity() < packValueInGram) {
                        Log.d(TAG, "onBindViewHolder: out of stock " + productHolder.getTitle());
                        //productTitle.setText("Out Of Stock");
                        addBTNframe.setVisibility(View.GONE);
                        txtOutOfStock.setVisibility(View.VISIBLE);
                    }
                } else if (packHolder.getPack().contains("kg")) {
                    int packValueInKG = Integer.parseInt(packHolder.getPack().replaceAll("[^0-9]", ""));
                    packValueInKG = (packValueInKG * 1000);
                    Log.d(TAG, "onBindViewHolder:kg " + packValueInKG);
                    if (productHolder.getQuntity() < packValueInKG) {
                       // productTitle.setText("Out Of Stock");
                        addBTNframe.setVisibility(View.GONE);
                        txtOutOfStock.setVisibility(View.VISIBLE);
                    }
                }
            } else if (productHolder.getUnit().equals("liter")) {
                if (packHolder.getPack().contains("ml")) {
                    int packValueInMl = Integer.parseInt(packHolder.getPack().replaceAll("[^0-9]", ""));
                    Log.d(TAG, "onBindViewHolder: " + packValueInMl);
                    if (productHolder.getQuntity() < packValueInMl) {
                        Log.d(TAG, "onBindViewHolder: out of stock " + productHolder.getTitle());
                       // productTitle.setText("Out Of Stock");
                        addBTNframe.setVisibility(View.GONE);
                        txtOutOfStock.setVisibility(View.VISIBLE);
                    }
                } else if (packHolder.getPack().contains("liter")) {
                    int packValueInLiterToMl = Integer.parseInt(packHolder.getPack().replaceAll("[^0-9]", ""));
                    packValueInLiterToMl = (packValueInLiterToMl * 1000);
                    Log.d(TAG, "onBindViewHolder:ML " + packValueInLiterToMl);
                    if (productHolder.getQuntity() < packValueInLiterToMl) {
                       // productTitle.setText("Out Of Stock");
                        addBTNframe.setVisibility(View.GONE);
                        txtOutOfStock.setVisibility(View.VISIBLE);
                    }
                }
            } else if (productHolder.getUnit().equals("pcs")) {
                if (packHolder.getPack().contains("pcs")) {
                    int packValueInPices = Integer.parseInt(packHolder.getPack().replaceAll("[^0-9]", ""));
                    Log.d(TAG, "onBindViewHolder: " + packValueInPices);
                    if (productHolder.getQuntity() < packValueInPices) {
                        Log.d(TAG, "onBindViewHolder: out of stock " + productHolder.getTitle());
                       // productTitle.setText("Out Of Stock");
                        addBTNframe.setVisibility(View.GONE);
                        txtOutOfStock.setVisibility(View.VISIBLE);
                    }
                }
            } else if (productHolder.getUnit().equals("dozen")) {
                if (packHolder.getPack().contains("dozen") && !packHolder.getPack().contains("half-dozen")) {
                    int packValueInDozen = Integer.parseInt(packHolder.getPack().replaceAll("[^0-9]", ""));
                    int packValueInDozenToPices  = packValueInDozen*12;
                    Log.d(TAG, "onBindViewHolder: " + packValueInDozenToPices);
                    if (productHolder.getQuntity() < packValueInDozenToPices) {
                        Log.d(TAG, "onBindViewHolder: out of stock " + productHolder.getTitle());
                    //    productTitle.setText("Out Of Stock");
                        addBTNframe.setVisibility(View.GONE);
                        txtOutOfStock.setVisibility(View.VISIBLE);
                    }
                }else if (packHolder.getPack().contains("half-dozen")) {
                    int packValueInDozenToPices  = 6;
                    Log.d(TAG, "onBindViewHolder: " + packValueInDozenToPices);
                    if (productHolder.getQuntity() < packValueInDozenToPices) {
                        Log.d(TAG, "onBindViewHolder: out of stock " + productHolder.getTitle());
                       // productTitle.setText("Out Of Stock");
                        addBTNframe.setVisibility(View.GONE);
                        txtOutOfStock.setVisibility(View.VISIBLE);
                    }
                }
            }

            // pices and dusen work remain
        }
    }

    static float discountPercentage(int SellingPrice, int MarketPrice) {
        // Calculating discount
        float discount = (float) MarketPrice - SellingPrice;

        // Calculating discount percentage
        float disPercent = (discount / MarketPrice) * 100;

        return disPercent;
    }

    private void getFistTimeData(){
        Query query = FirebaseFirestore.getInstance().collection("Product").whereEqualTo("category",productHolder.getCategory()).limit(limit);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (DocumentSnapshot snapshot : task.getResult()) {
                        ProductHolder holder = snapshot.toObject(ProductHolder.class);
                        holder.setDocId(snapshot.getId());
                        if (holder.getVisibility() == null || !holder.getVisibility().equals("hide")){
                            list.add(holder);
                        }

                    }
                    productAdepter.notifyDataSetChanged();
                    lastVisible = task.getResult().getDocuments().get(task.getResult().size() - 1);
                    Log.d("lastvisible", "onComplete: "+task.getResult().size());



                    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener(){
                        @Override
                        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                            super.onScrollStateChanged(recyclerView, newState);
                            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                                isScrolling = true;
                            }
                        }

                        @Override
                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            LinearLayoutManager linearLayoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
                            int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                            int visibleItemCount = linearLayoutManager.getChildCount();
                            int totalItemCount = linearLayoutManager.getItemCount();

                            Log.d(TAG, "first visible "+firstVisibleItemPosition+" visible item count "+visibleItemCount +" total  "+totalItemCount +" is scroll "+isScrolling+" reach last "+isLastItemReached);

                            if (isScrolling && (firstVisibleItemPosition + visibleItemCount == totalItemCount) && !isLastItemReached) {
                                isScrolling = false;

                                Query nextQuery = null;

                                 nextQuery = FirebaseFirestore.getInstance().collection("Product")
                                         .whereEqualTo("category",productHolder.getCategory()).startAfter(lastVisible).limit(limit);

                                nextQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> t) {
                                        if (t.isSuccessful()) {
                                            for (DocumentSnapshot d : t.getResult()) {
                                                ProductHolder productModel = d.toObject(ProductHolder.class);
                                                productModel.setDocId(d.getId());
                                                if (productModel.getVisibility() == null || !productModel.getVisibility().equals("hide")){
                                                    list.add(productModel);
                                                }
                                            }
                                            productAdepter.notifyDataSetChanged();

                                            if (t.getResult().size() == 0){
                                                Log.d("lastvisible", "onComplete: post samplya "+t.getResult().size());

                                            }else {
                                                Log.d("lastvisible", "onComplete: post samplya "+t.getResult().size());

                                                lastVisible = t.getResult().getDocuments().get(t.getResult().size() - 1);
                                                if (t.getResult().size() < limit) {
                                                    isLastItemReached = true;
                                                }
                                            }
                                        }else {

                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("DDDDDDDDDDDDDDDDAAAAAAA", "onFailure: "+e.getLocalizedMessage());
                                    }
                                });
                            }


                        }
                    };

                    recyclerView.addOnScrollListener(onScrollListener);
                }
            }
        });
    }


    public void deleteProductFromCard(ProductHolder model, int packID) {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                // Insert Data
                boolean b = UserPostsRoomDatabase.getInstance(ProductDetails.this)
                        .postsDao()
                        .isProductAvailble(model.getDocId());
                Log.d(TAG, "run: "+b);

                if (b){
                    CardProductHolder cardProductHolder = UserPostsRoomDatabase.getInstance(ProductDetails.this)
                            .postsDao()
                            .findByProductId(model.getDocId());

                    if (packID != cardProductHolder.getPack_id()){
                        UserPostsRoomDatabase.getInstance(ProductDetails.this).postsDao()
                                .deleteProductFromCard(cardProductHolder);

                        runOnUiThread(new Runnable() {

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

    public class AddProductToBucket extends AsyncTask<CardProductHolder,Void,Void> {
        @Override
        protected Void doInBackground(CardProductHolder... cardProduct) {
            UserPostsRoomDatabase.getInstance(ProductDetails.this)
                    .postsDao()
                    .addProductToCard(cardProduct[0]);

            return null;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}