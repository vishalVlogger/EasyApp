package com.appdroid.ssbtproject.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.appdroid.ssbtproject.Adapter.MyProductAdepter;
import com.appdroid.ssbtproject.Adapter.ProductsAdepter;
import com.appdroid.ssbtproject.Adapter.ProductsPaginationAdepter;
import com.appdroid.ssbtproject.Holder.ProductHolder;
import com.appdroid.ssbtproject.R;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ListingProducts extends AppCompatActivity {

    private static final String TAG = "listing_products";
    RecyclerView recyclerView;
    FirestorePagingOptions<ProductHolder> options;
    ProductsAdepter productsPaginationAdepter;
    LinearLayout sort;
    ImageView imgBack;
    String flag,intentFlag;
    BottomSheetDialog bottomSheetDialog;
    TextView lowtohigh,hightolow,peroff,txt_for_no_data;

    MyProductAdepter  productAdepter;
    List<ProductHolder> list;

    private boolean isScrolling = false;
    private boolean isLastItemReached = false;

    DocumentSnapshot lastVisible;
    RelativeLayout noDataLayout;
    private int limit = 10;
    LottieAnimationView progressBar;

    public static Boolean isScrollingForNew = true;
    public static int previoustotal;
    int page =1;
    int currentItems, totalItems,scrollOutItems;

    GridLayoutManager gridLayoutManager;
    Query nextQuery,searchNextQuery;

    boolean nowFistTimeScrolling = true;
    boolean highToLowScrolling = false;
    boolean perVisSearch = false;
    LottieAnimationView centerProgressBar;

    ProductsPaginationAdepter paginationAdepter;
    Dialog pDialog;
    EditText searchBar;
    ImageView search_voice_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_products);
        previoustotal = 0;
        flag = getIntent().getStringExtra("flag");

        Log.d(TAG, "onCreate:stsrfseswewewewewds "+flag);
        noDataLayout  = findViewById(R.id.noDataLayout);
        sort = findViewById(R.id.sort);
        intentFlag = getIntent().getAction();

        progressBar = findViewById(R.id.progressBar);
        centerProgressBar = findViewById(R.id.centerProgressBar);
        search_voice_btn = findViewById(R.id.search_voice_btn);

        list = new ArrayList<>();

        searchBar = findViewById(R.id.searchBar);
        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //    Toast.makeText(MainActivity.this, ""+searchBar.getText().toString(), Toast.LENGTH_SHORT).show();
                    centerProgressBar.setVisibility(View.VISIBLE);
                    list.clear();
                    productAdepter.notifyDataSetChanged();
                    flag = searchBar.getText().toString().toLowerCase();
                    uploadSearchQuery(flag);
                    intentFlag = "fromSearch";
                    getSearchResultWithNewStyle();

                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    return true;
                }
                return false;
            }
        });

        search_voice_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSpeechInput();
            }
        });

        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.sorting_bottom_sheat_item);
        lowtohigh = bottomSheetDialog.findViewById(R.id.lowtohigh);
        hightolow = bottomSheetDialog.findViewById(R.id.hightolow);
        peroff = bottomSheetDialog.findViewById(R.id.peroff);


        pDialog = new Dialog(ListingProducts.this);
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.setCancelable(false);

        recyclerView = findViewById(R.id.productsList);
        recyclerView.hasFixedSize();
        gridLayoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(gridLayoutManager);
      //  recyclerView.setLayoutManager(new LinearLayoutManager(this));

        imgBack = findViewById(R.id.img_back);

        productAdepter = new MyProductAdepter(list,ListingProducts.this);
        recyclerView.setAdapter(productAdepter);

        if (getIntent().getAction().equals("fromSearch")){
            Log.d(TAG, "onCreate: from search");
             // getSearchResult();
            searchBar.setText(flag);
            flag = flag.toLowerCase();
            getSearchResultWithNewStyle();
        }else {

            //getDataForPaginationModerl();

           getFirstTimeDataWithNewStyle();
             //getFistTimeData();
        }

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.show();
            }
        });

        hightolow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query query = null;
                pDialog.show();
                if (intentFlag.equals("fromSearch")){
                    query = FirebaseFirestore.getInstance().collection("Product")
                            .orderBy("packPrice", Query.Direction.DESCENDING)
                            .whereArrayContains("keyword",flag).limit(limit);
                }else {
                    query = FirebaseFirestore.getInstance().collection("Product")
                            .orderBy("packPrice", Query.Direction.DESCENDING)
                            .whereEqualTo("category",flag).limit(limit);
                }
                getHighToLowDataWithNewStyle(query);
                //getHighToLowData(query);
            }
        });

        peroff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query query = null;
                pDialog.show();
                if (intentFlag.equals("fromSearch")){
                    flag = flag.toLowerCase();
                    query = FirebaseFirestore.getInstance().collection("Product")
                            .orderBy("discountPer", Query.Direction.DESCENDING)
                            .whereArrayContains("keyword",flag).limit(limit);
                    Log.d("chetan", "onClick:from search "+flag);
                }else {
                    query = FirebaseFirestore.getInstance().collection("Product")
                            .orderBy("discountPer", Query.Direction.DESCENDING)
                            .whereEqualTo("category",flag).limit(limit);
                    Log.d("chetan", "onClick:call "+flag);
                }
                getPerOffDataWithNewStyle(query);

                //getPerOffData(query);
            }
        });
        
        lowtohigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query query = null;

                pDialog.show();

                if (getIntent().getAction().equals("fromSearch")){
                    Log.d(TAG, "onCreate: from search");
                    flag = flag.toLowerCase();
                    getSearchResultWithNewStyle();
                }else {
                    getFirstTimeDataWithNewStyle();

                }
                bottomSheetDialog.dismiss();

            }
        });



        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){

                    //   isScrolling = true;

                }

            }
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                currentItems = gridLayoutManager.getChildCount();
                totalItems = gridLayoutManager.getItemCount();
                scrollOutItems = gridLayoutManager.findFirstVisibleItemPosition();

                Log.d("appdroidssssssssss", "current itek "+currentItems+" total Item "+totalItems+" scrollout "+scrollOutItems +" is scro "+isScrollingForNew +" previas totoal "+previoustotal);
                if (isScrollingForNew) {
                    if (totalItems > previoustotal) {

                        previoustotal = totalItems;
                        page++;

                        isScrollingForNew = false;

                    }

                }

                if (!isScrollingForNew && (currentItems+scrollOutItems) == totalItems){

                    Log.d("currentItem", "onScrolled: "+currentItems);
                    Log.d("scrollOutItems", "onScrolled: "+scrollOutItems);
                    Log.d("totalItems", "onScrolled: "+totalItems);

                    isScrollingForNew = true;
                    progressBar.setVisibility(View.VISIBLE);

                    Log.d(TAG, "onScrolled:sagggg  scroll firstTime "+nowFistTimeScrolling+" highToLowScrolling "+highToLowScrolling+" perVisSearch "+perVisSearch);

                    if (nowFistTimeScrolling){
                        Log.d(TAG, "onScrolled:sagggg  scroll firstTime");

                        if (getIntent().getAction().equals("fromSearch")){
                            searchNextQuery = FirebaseFirestore.getInstance().collection("Product")
                                    .orderBy("packPrice", Query.Direction.ASCENDING)
                                    .whereArrayContains("keyword",flag)
                                    .startAfter(lastVisible).limit(limit);

                        }else {
                            nextQuery = FirebaseFirestore.getInstance().collection("Product")
                                    .orderBy("packPrice", Query.Direction.ASCENDING).whereEqualTo("category",flag)
                                    .startAfter(lastVisible).limit(limit);
                         }

                    }else if (highToLowScrolling){

                        Log.d(TAG, "onScrolled:sagggg  scroll highTolow");

                        if (intentFlag.equals("fromSearch")){
                            searchNextQuery = FirebaseFirestore.getInstance().collection("Product")
                                    .orderBy("packPrice", Query.Direction.DESCENDING)
                                    .whereArrayContains("keyword",flag).startAfter(lastVisible).limit(limit);
                        }else {
                            nextQuery = FirebaseFirestore.getInstance().collection("Product")
                                    .orderBy("packPrice", Query.Direction.DESCENDING)
                                    .whereEqualTo("category",flag).startAfter(lastVisible).limit(limit);
                        }
                    }else if (perVisSearch){
                        if (intentFlag.equals("fromSearch")){
                            searchNextQuery = FirebaseFirestore.getInstance().collection("Product")
                                    .orderBy("discountPer", Query.Direction.DESCENDING)
                                    .whereArrayContains("keyword",flag).startAfter(lastVisible).limit(limit);
                        }else {
                            nextQuery = FirebaseFirestore.getInstance().collection("Product")
                                    .orderBy("discountPer", Query.Direction.DESCENDING)
                                    .whereEqualTo("category",flag).startAfter(lastVisible).limit(limit);
                        }
                    }

                    if (intentFlag.equals("fromSearch")){
                        featchDataForSearchResult(searchNextQuery);
                    }else {
                        featchData(nextQuery);
                    }


                }

            }
        });

    }

/*    private void getDataForPaginationModerl() {


        Log.d(TAG, "getDataForPaginationModerl: ");
        Query firestore  =  FirebaseFirestore.getInstance().collection("Product")
                .orderBy("packPrice", Query.Direction.ASCENDING).whereEqualTo("category",flag).limit(limit);


      *//*  PagingConfig config = new PagingConfig(*//**//* page size *//**//* 5, *//**//* prefetchDistance *//**//* 2,
                *//**//* enablePlaceHolders *//**//* false);

        FirestorePagingOptions<ProductHolder> options = new FirestorePagingOptions.Builder<ProductHolder>()
                .setLifecycleOwner(this)
                .setQuery(firestore, config, new SnapshotParser<ProductHolder>() {
                            @NonNull
                            @NotNull
                            @Override
                            public ProductHolder parseSnapshot(@NonNull @NotNull DocumentSnapshot snapshot) {
                                ProductHolder productHolder = snapshot.toObject(ProductHolder.class);
                                Log.d(TAG, "parseSnapshot: "+productHolder.getTitle());
                                productHolder.setDocId(snapshot.getId());
                                return productHolder;
                            }
                        }
                )
                .build();

            paginationAdepter = new ProductsPaginationAdepter(options);*//*


          //
    }*/
    public void getSpeechInput() {

        Locale locale = new Locale("mar_IN");
    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, locale);

    if (intent.resolveActivity(ListingProducts.this.getPackageManager()) != null) {
        startActivityForResult(intent, 10);
    } else {
        Toast.makeText(ListingProducts.this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
    }
}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    // searchBar.setText(result.get(0));
                    uploadSearchQuery(result.get(0));
                    searchBar.setText(result.get(0));

                    centerProgressBar.setVisibility(View.VISIBLE);
                    list.clear();
                    productAdepter.notifyDataSetChanged();
                    flag = result.get(0).toLowerCase();

                    intentFlag = "fromSearch";
                    getSearchResultWithNewStyle();

                }
                break;
        }
    }

    private void uploadSearchQuery(String toString) {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        HashMap<String ,Object> hashMap = new HashMap<>();
        if (firebaseUser != null){
            hashMap.put("userID",FirebaseAuth.getInstance().getCurrentUser().getUid());
        }else {
            hashMap.put("userID","notLogin");
        }
        hashMap.put("date",new Timestamp(new Date()));
        hashMap.put("tag",toString);
        CollectionReference firebaseFirestore = FirebaseFirestore.getInstance().collection("SerchQuerys");
        firebaseFirestore.add(hashMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    Log.d("DDDDDDDASDGADF", "onComplete: save "+toString);
                }
            }
        });

    }
    private void getFirstTimeDataWithNewStyle() {
        list.clear();

         nowFistTimeScrolling = true;
         highToLowScrolling = false;
         perVisSearch = false;
         previoustotal = 0;

        Query firestore  =  FirebaseFirestore.getInstance().collection("Product")
                .orderBy("packPrice", Query.Direction.ASCENDING).whereEqualTo("category",flag).limit(limit);

        firestore.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (DocumentSnapshot snapshot : task.getResult()) {
                        ProductHolder holder = snapshot.toObject(ProductHolder.class);
                        holder.setDocId(snapshot.getId());
                        if (holder.getVisibility() == null || !holder.getVisibility().equals("hide")){
                            list.add(holder);
                        }
                    }
                    centerProgressBar.setVisibility(View.GONE);
                    productAdepter.notifyDataSetChanged();
                    lastVisible = task.getResult().getDocuments().get(task.getResult().size() - 1);
                    if (pDialog.isShowing()){
                        pDialog.dismiss();
                    }
                    Log.d("lastvisible", "onComplete: "+task.getResult().size());
                }
            }
        });
    }

    public void featchData(Query nextQuery){

        if (nextQuery != null){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "onScrolled:ssssssssss after ");

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

                                progressBar.setVisibility(View.GONE);
                                if (t.getResult().size() != 0){
                                    Log.d("lastvisible", "onComplete: post samplya "+t.getResult().size());
                                    lastVisible = t.getResult().getDocuments().get(t.getResult().size() - 1);
                                    if (t.getResult().size() < limit) {
                                        isLastItemReached = true;
                                    }
                                }else {
                                    Log.d("lastvisible", "onComplete: post samplya "+t.getResult().size());
                                }
                                productAdepter.notifyDataSetChanged();
                                progressBar.setVisibility(View.GONE);


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
            },1000);
        }


    }

    public void featchDataForSearchResult(Query next){
        if (next != null){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    next.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
                                progressBar.setVisibility(View.GONE);
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
            },1000);
        }
    }

    private void getPerOffData(Query query) {
        list.clear();
        isLastItemReached = false;
        final RecyclerView.OnScrollListener[] onScrollListener = new RecyclerView.OnScrollListener[1];
        bottomSheetDialog.dismiss();
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (DocumentSnapshot snapshot : task.getResult()) {
                        ProductHolder holder = snapshot.toObject(ProductHolder.class);
                        holder.setDocId(snapshot.getId());
                        list.add(holder);
                    }
                    productAdepter.notifyDataSetChanged();
                    lastVisible = task.getResult().getDocuments().get(task.getResult().size() - 1);
                    Log.d("lastvisible", "onComplete: from per off "+task.getResult().size());



                     onScrollListener[0] = new RecyclerView.OnScrollListener(){
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
                                progressBar.setVisibility(View.VISIBLE);
                                Query nextQuery = null;


                                if (intentFlag.equals("fromSearch")){
                                    nextQuery = FirebaseFirestore.getInstance().collection("Product")
                                            .orderBy("discountPer", Query.Direction.DESCENDING)
                                            .whereArrayContains("keyword",flag).startAfter(lastVisible).limit(limit);
                                }else {
                                    nextQuery = FirebaseFirestore.getInstance().collection("Product")
                                            .orderBy("discountPer", Query.Direction.DESCENDING)
                                            .whereEqualTo("category",flag).startAfter(lastVisible).limit(limit);
                                }

                                nextQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> t) {
                                        if (t.isSuccessful()) {
                                            for (DocumentSnapshot d : t.getResult()) {
                                                ProductHolder productModel = d.toObject(ProductHolder.class);
                                                productModel.setDocId(d.getId());
                                                list.add(productModel);
                                            }
                                            productAdepter.notifyDataSetChanged();
                                            progressBar.setVisibility(View.GONE);
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



                }
                recyclerView.addOnScrollListener(onScrollListener[0]);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: "+e.getLocalizedMessage());
            }
        });

    }

    private void getPerOffDataWithNewStyle(Query query) {
        list.clear();
        isLastItemReached = false;
        previoustotal = 0;
        nowFistTimeScrolling = false;
        highToLowScrolling = false;

        perVisSearch = true;

        bottomSheetDialog.dismiss();
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
                    Log.d(TAG, "onComplete: "+pDialog.isShowing());

                    if (pDialog.isShowing()){
                        pDialog.dismiss();
                    }
                    Log.d("lastvisible", "onComplete: from per off "+task.getResult().size());

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: "+e.getLocalizedMessage());
            }
        });

    }

    private void getLowToHighData(Query query) {
        list.clear();
        isLastItemReached = false;
        final RecyclerView.OnScrollListener[] onScrollListener = new RecyclerView.OnScrollListener[1];
        bottomSheetDialog.dismiss();
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (DocumentSnapshot snapshot : task.getResult()) {
                        ProductHolder holder = snapshot.toObject(ProductHolder.class);
                        holder.setDocId(snapshot.getId());

                        Log.d("chetanA", "onComplete: "+holder.getPackPrice());

                        list.add(holder);
                    }
                    productAdepter.notifyDataSetChanged();
                    lastVisible = task.getResult().getDocuments().get(task.getResult().size() - 1);
                    Log.d("lastvisible", "onComplete: "+task.getResult().size());



                 onScrollListener[0] = new RecyclerView.OnScrollListener(){
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


                                if (intentFlag.equals("fromSearch")){
                                    nextQuery = FirebaseFirestore.getInstance().collection("Product")
                                            .orderBy("packPrice", Query.Direction.ASCENDING)
                                            .whereArrayContains("keyword",flag).startAfter(lastVisible).limit(limit);
                                }else {
                                    nextQuery = FirebaseFirestore.getInstance().collection("Product")
                                            .orderBy("packPrice", Query.Direction.ASCENDING)
                                            .whereEqualTo("category",flag).startAfter(lastVisible).limit(limit);
                                }



                                nextQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> t) {
                                        if (t.isSuccessful()) {
                                            for (DocumentSnapshot d : t.getResult()) {
                                                ProductHolder productModel = d.toObject(ProductHolder.class);
                                                productModel.setDocId(d.getId());
                                                Log.d("chetanA", "onComplete: "+productModel.getPackPrice());
                                                list.add(productModel);
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


                }
                recyclerView.addOnScrollListener(onScrollListener[0]);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: "+e.getLocalizedMessage());
            }
        });

    }

    private void getSearchResult() {
        list.clear();
        Query firestore  =  FirebaseFirestore.getInstance().collection("Product")
                .orderBy("packPrice", Query.Direction.ASCENDING).whereArrayContains("keyword",flag).limit(limit);

        final RecyclerView.OnScrollListener[] onScrollListener = new RecyclerView.OnScrollListener[1];
        firestore.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (DocumentSnapshot snapshot : task.getResult()) {
                        ProductHolder holder = snapshot.toObject(ProductHolder.class);
                        holder.setDocId(snapshot.getId());
                        list.add(holder);
                    }
                    productAdepter.notifyDataSetChanged();
                    Log.d("lastvisible", "onComplete: "+task.getResult().size());
                    if (task.getResult().size()>0){
                        noDataLayout.setVisibility(View.GONE);
                        sort.setVisibility(View.VISIBLE);
                        lastVisible = task.getResult().getDocuments().get(task.getResult().size() - 1);
                    }else {

                        noDataLayout.setVisibility(View.VISIBLE);
                        sort.setVisibility(View.GONE);
                    }





                    onScrollListener[0] = new RecyclerView.OnScrollListener(){
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

                                progressBar.setVisibility(View.VISIBLE);



                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Query nextQuery = null;

                                        nextQuery = FirebaseFirestore.getInstance().collection("Product")
                                                .orderBy("packPrice", Query.Direction.ASCENDING)
                                                .whereArrayContains("keyword",flag)
                                                .startAfter(lastVisible).limit(limit);

                                        nextQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> t) {
                                                if (t.isSuccessful()) {
                                                    for (DocumentSnapshot d : t.getResult()) {
                                                        ProductHolder productModel = d.toObject(ProductHolder.class);
                                                        productModel.setDocId(d.getId());
                                                        list.add(productModel);
                                                    }
                                                    productAdepter.notifyDataSetChanged();
                                                    progressBar.setVisibility(View.GONE);
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
                                },2000);


                            }


                        }
                    };


                }
                recyclerView.addOnScrollListener(onScrollListener[0]);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: d"+e.getLocalizedMessage());
            }
        });
    }

    private void getSearchResultWithNewStyle() {
        list.clear();

        nowFistTimeScrolling = true;
        highToLowScrolling = false;
        perVisSearch = false;
        previoustotal = 0;



        Query firestore  =  FirebaseFirestore.getInstance().collection("Product")
                .orderBy("packPrice", Query.Direction.ASCENDING).whereArrayContains("keyword",flag).limit(limit);

        final RecyclerView.OnScrollListener[] onScrollListener = new RecyclerView.OnScrollListener[1];
        firestore.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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

                    if (pDialog.isShowing()){
                        pDialog.dismiss();
                    }
                    Log.d("lastvisible", "onComplete:is pd showing "+pDialog.isShowing());
                    if (task.getResult().size()>0){
                        noDataLayout.setVisibility(View.GONE);
                        centerProgressBar.setVisibility(View.GONE);
                        sort.setVisibility(View.VISIBLE);
                        lastVisible = task.getResult().getDocuments().get(task.getResult().size() - 1);
                    }else {
                        noDataLayout.setVisibility(View.VISIBLE);
                        sort.setVisibility(View.GONE);
                        centerProgressBar.setVisibility(View.GONE);
                    }
/*                    onScrollListener[0] = new RecyclerView.OnScrollListener(){
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

                                progressBar.setVisibility(View.VISIBLE);



                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Query nextQuery = null;

                                        nextQuery = FirebaseFirestore.getInstance().collection("Product")
                                                .orderBy("packPrice", Query.Direction.ASCENDING)
                                                .whereArrayContains("keyword",flag)
                                                .startAfter(lastVisible).limit(limit);

                                        nextQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> t) {
                                                if (t.isSuccessful()) {
                                                    for (DocumentSnapshot d : t.getResult()) {
                                                        ProductHolder productModel = d.toObject(ProductHolder.class);
                                                        productModel.setDocId(d.getId());
                                                        list.add(productModel);
                                                    }
                                                    productAdepter.notifyDataSetChanged();
                                                    progressBar.setVisibility(View.GONE);
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
                                },2000);


                            }


                        }
                    };*/
                }
              //  recyclerView.addOnScrollListener(onScrollListener[0]);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: d"+e.getLocalizedMessage());
            }
        });
    }

    private void getHighToLowData(Query query) {
        Log.d(TAG, "getHighToLowData: calll");
        list.clear();
        isLastItemReached = false;
        bottomSheetDialog.dismiss();
        final RecyclerView.OnScrollListener[] onScrollListener = new RecyclerView.OnScrollListener[1];
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (DocumentSnapshot snapshot : task.getResult()) {
                        ProductHolder holder = snapshot.toObject(ProductHolder.class);
                        holder.setDocId(snapshot.getId());
                        list.add(holder);
                    }
                    productAdepter.notifyDataSetChanged();
                    lastVisible = task.getResult().getDocuments().get(task.getResult().size() - 1);
                    Log.d("lastvisible", "onComplete: "+task.getResult().size());



                    onScrollListener[0] = new RecyclerView.OnScrollListener(){
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


                                progressBar.setVisibility(View.VISIBLE);

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Query nextQuery = null;
                                        if (intentFlag.equals("fromSearch")){
                                            nextQuery = FirebaseFirestore.getInstance().collection("Product")
                                                    .orderBy("packPrice", Query.Direction.DESCENDING)
                                                    .whereArrayContains("keyword",flag).startAfter(lastVisible).limit(limit);
                                        }else {
                                            nextQuery = FirebaseFirestore.getInstance().collection("Product")
                                                    .orderBy("packPrice", Query.Direction.DESCENDING)
                                                    .whereEqualTo("category",flag).startAfter(lastVisible).limit(limit);
                                        }

                                        nextQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> t) {
                                                if (t.isSuccessful()) {
                                                    for (DocumentSnapshot d : t.getResult()) {
                                                        ProductHolder productModel = d.toObject(ProductHolder.class);
                                                        productModel.setDocId(d.getId());
                                                        list.add(productModel);
                                                    }
                                                    productAdepter.notifyDataSetChanged();
                                                    progressBar.setVisibility(View.GONE);
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
                                },2000);


                            }


                        }
                    };


                }
                recyclerView.addOnScrollListener(onScrollListener[0]);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: "+e.getLocalizedMessage());
            }
        });

    }

    private void getHighToLowDataWithNewStyle(Query query) {
        Log.d(TAG, "getHighToLowData: calll");
        list.clear();
        previoustotal = 0;
        isLastItemReached = false;
        highToLowScrolling = true;
        nowFistTimeScrolling = false;

        bottomSheetDialog.dismiss();
        final RecyclerView.OnScrollListener[] onScrollListener = new RecyclerView.OnScrollListener[1];
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
                    if (pDialog.isShowing()){
                        pDialog.dismiss();
                    }
                    Log.d("lastvisible", "onComplete: "+task.getResult().size());



                /*    onScrollListener[0] = new RecyclerView.OnScrollListener(){
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


                                progressBar.setVisibility(View.VISIBLE);

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Query nextQuery = null;
                                        if (intentFlag.equals("fromSearch")){
                                            nextQuery = FirebaseFirestore.getInstance().collection("Product")
                                                    .orderBy("packPrice", Query.Direction.DESCENDING)
                                                    .whereArrayContains("keyword",flag).startAfter(lastVisible).limit(limit);
                                        }else {
                                            nextQuery = FirebaseFirestore.getInstance().collection("Product")
                                                    .orderBy("packPrice", Query.Direction.DESCENDING)
                                                    .whereEqualTo("category",flag).startAfter(lastVisible).limit(limit);
                                        }

                                        nextQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> t) {
                                                if (t.isSuccessful()) {
                                                    for (DocumentSnapshot d : t.getResult()) {
                                                        ProductHolder productModel = d.toObject(ProductHolder.class);
                                                        productModel.setDocId(d.getId());
                                                        list.add(productModel);
                                                    }
                                                    productAdepter.notifyDataSetChanged();
                                                    progressBar.setVisibility(View.GONE);
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
                                },2000);


                            }


                        }
                    };*/


                }
/*                recyclerView.addOnScrollListener(onScrollListener[0]);*/
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: "+e.getLocalizedMessage());
            }
        });

    }

    private void getFistTimeData(){

        list.clear();
       Query firestore  =  FirebaseFirestore.getInstance().collection("Product")
               .orderBy("packPrice", Query.Direction.ASCENDING).whereEqualTo("category",flag).limit(limit);
        final RecyclerView.OnScrollListener[] onScrollListener = new RecyclerView.OnScrollListener[1];
       firestore.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
           @Override
           public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {

                        for (DocumentSnapshot snapshot : task.getResult()) {
                            ProductHolder holder = snapshot.toObject(ProductHolder.class);
                            holder.setDocId(snapshot.getId());
                            list.add(holder);
                        }
                        productAdepter.notifyDataSetChanged();
                        lastVisible = task.getResult().getDocuments().get(task.getResult().size() - 1);
                        Log.d("lastvisible", "onComplete: "+task.getResult().size());



                         onScrollListener[0] = new RecyclerView.OnScrollListener(){
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

                                Log.d(TAG, "firstvisible "+firstVisibleItemPosition+" visible item count "+visibleItemCount +" total  "+totalItemCount +" is scroll "+isScrolling+" reach last "+isLastItemReached);



                                if (isScrolling && (firstVisibleItemPosition + visibleItemCount == totalItemCount) && !isLastItemReached) {

                                    progressBar.setVisibility(View.VISIBLE);
                                    isScrolling = false;

                                    Log.d(TAG, "onScrolled:ssssssssss before ");
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Log.d(TAG, "onScrolled:ssssssssss after ");
                                            Query nextQuery = null;

                                            nextQuery = FirebaseFirestore.getInstance().collection("Product")
                                                    .orderBy("packPrice", Query.Direction.ASCENDING).whereEqualTo("category",flag)
                                                    .startAfter(lastVisible).limit(limit);

                                            nextQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> t) {
                                                    if (t.isSuccessful()) {
                                                        for (DocumentSnapshot d : t.getResult()) {
                                                            ProductHolder productModel = d.toObject(ProductHolder.class);
                                                            productModel.setDocId(d.getId());
                                                            list.add(productModel);
                                                        }

                                                        progressBar.setVisibility(View.GONE);
                                                        if (t.getResult().size() != 0){
                                                            Log.d("lastvisible", "onComplete: post samplya "+t.getResult().size());
                                                            lastVisible = t.getResult().getDocuments().get(t.getResult().size() - 1);
                                                            if (t.getResult().size() < limit) {
                                                                isLastItemReached = true;
                                                            }
                                                        }else {
                                                            Log.d("lastvisible", "onComplete: post samplya "+t.getResult().size());
                                                        }
                                                        productAdepter.notifyDataSetChanged();
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
                                    },2000);



                                }


                            }
                        };


                    }
               recyclerView.addOnScrollListener(onScrollListener[0]);
           }
       });
    }

    @Override
    protected void onResume() {
        super.onResume();
        productAdepter.notifyDataSetChanged();
    }


}




