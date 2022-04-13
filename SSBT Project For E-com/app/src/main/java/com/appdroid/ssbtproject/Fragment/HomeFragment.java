package com.appdroid.ssbtproject.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appdroid.ssbtproject.Activity.ListingProducts;
import com.appdroid.ssbtproject.Adapter.LablesAdepter;
import com.appdroid.ssbtproject.Adapter.MyProductAdepter;
import com.appdroid.ssbtproject.Adapter.ProductsAdepter;
import com.appdroid.ssbtproject.Adapter.SliderAdapterExample;
import com.appdroid.ssbtproject.Adapter.SliderAdepter;
import com.appdroid.ssbtproject.Holder.LabelHolder;
import com.appdroid.ssbtproject.Holder.ProductHolder;
import com.appdroid.ssbtproject.Holder.SliderHolder;
import com.appdroid.ssbtproject.R;
import com.appdroid.ssbtproject.Utilities.InternetConnection;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFragment extends Fragment {

    public static String TAG = "dandgezzz";
    LinearLayout networkLayout;
    Button btnNetWork;

    RecyclerView vegetablesRecyelerView;
    FirestorePagingOptions<ProductHolder> vegetaiblesOptions;
    ProductsAdepter vegetablesAdepter;

    MyProductAdepter myProductAdepterForVegetaibles,dairyProductsAdepter, groceryAdapter,handcraftAdepter;
    List<ProductHolder> vegitablesHolderList;

    DocumentSnapshot lastVisibleOfVegitable;
    private int limit = 8;
    private boolean isScrollingVegitableScroler = false;
    private boolean isLastItemReachedVegitableScroler = false;

    LinearLayoutManager sliderLayoutManager;
    SliderAdepter sliderAdepter;
    FirestorePagingOptions<SliderHolder> SliderOptions;

    TextView vegetablesViewMore;
    //ViewPager2 viewPager2;

    Handler handler;

    RecyclerView dairyProductsRecylerView;
    LinearLayoutManager managerForDairyProducts;
    List<ProductHolder> dairyProductsList;

    RecyclerView groceryRecylerView;
    LinearLayoutManager managerForGrocery;
    List<ProductHolder> groceryList;

    TextView dairyProductSeeAll, groceryViewMore,handcraftsViewMore;

    LinearLayout fruits,vegetables,grocery,dairyProducts,other,otherProducts,handcrafts;
    ImageView homeScennCard;

    SliderView sliderView;
    SliderAdapterExample adapter;
    List<SliderHolder> list;

    RecyclerView recyclerLabel;
    List<LabelHolder> labelHolderList;
    LablesAdepter lablesAdepter;
    TextView[] dots;
    LinearLayout indecatersLayout;

    ShimmerFrameLayout shimmer_view_container,shimmer_view_container1Grocery
            ,shimmer_view_container2Vegitables,shimmer_view_container3DairyProducts,shimmer_view_container4All,shimmer_view_containerHandCrafts;
    LinearLayout mainLayout;

    ImageView groceryMore, vegetablesMore, dairyMore;


    RecyclerView handCraftRecylerView;
    LinearLayoutManager managerForHandCraftProducts;
    List<ProductHolder> handcraftProductsList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        shimmer_view_container = view.findViewById(R.id.shimmer_view_container);
        shimmer_view_container1Grocery = view.findViewById(R.id.shimmer_view_container1Grocery);
        shimmer_view_container2Vegitables = view.findViewById(R.id.shimmer_view_container2);
        shimmer_view_container3DairyProducts = view.findViewById(R.id.shimmer_view_container3DairyProducts);
        shimmer_view_container4All = view.findViewById(R.id.shimmer_view_container4All);
        shimmer_view_containerHandCrafts = view.findViewById(R.id.shimmer_view_containerHandCrafts);

        mainLayout = view.findViewById(R.id.mainLayout);

        mainLayout.setVisibility(View.GONE);

        shimmer_view_container.startShimmer();
        shimmer_view_container1Grocery.startShimmer();
        shimmer_view_container2Vegitables.startShimmer();
        shimmer_view_container3DairyProducts.startShimmer();
        shimmer_view_containerHandCrafts.startShimmer();

        recyclerLabel = view.findViewById(R.id.recyclerLabel);
        recyclerLabel.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerLabel.hasFixedSize();
        labelHolderList = new ArrayList<>();

        //indecatersLayout = view.findViewById(R.id.indecatersLayout);
        list = new ArrayList<>();
        sliderView = view.findViewById(R.id.imageSlider);
        sliderView.setSliderAdapter(new SliderAdapterExample(getContext(), list));
        Log.d("SSSSSSAFFFFF", "onSuccess: sssssA before ");

        {
            handcrafts = view.findViewById(R.id.handcrafts);
            fruits = view.findViewById(R.id.fruits);
            groceryMore = view.findViewById(R.id.groceryMore);
            vegetablesMore = view.findViewById(R.id.vegetablesMore);
            dairyMore = view.findViewById(R.id.dairyMore);
            vegetables = view.findViewById(R.id.vegetables);
            grocery = view.findViewById(R.id.grocery);
            dairyProducts = view.findViewById(R.id.dairyProducts);
            other = view.findViewById(R.id.other);
            homeScennCard = view.findViewById(R.id.homeScennCard);
            groceryViewMore = view.findViewById(R.id.groceryViewMore);
            otherProducts = view.findViewById(R.id.otherProducts);

            handcraftsViewMore = view.findViewById(R.id.handcraftsViewMore);

           // viewPager2 = view.findViewById(R.id.viewPager2);
            //sliderLayoutManager = new LinearLayoutManager(getContext());
       //     handler = new Handler();

            vegitablesHolderList = new ArrayList<>();
            myProductAdepterForVegetaibles = new MyProductAdepter(vegitablesHolderList, getContext());

            vegetablesViewMore = view.findViewById(R.id.vegetablesViewMore);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);


            handCraftRecylerView = view.findViewById(R.id.handcraftsRecylerView);
            managerForHandCraftProducts = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            handCraftRecylerView.setLayoutManager(managerForHandCraftProducts);
            handCraftRecylerView.hasFixedSize();
            handCraftRecylerView.setItemAnimator(new DefaultItemAnimator());

            handcraftProductsList = new ArrayList<>();
            handcraftAdepter = new MyProductAdepter(handcraftProductsList, getContext());
            handCraftRecylerView.setAdapter(handcraftAdepter);



            dairyProductsRecylerView = view.findViewById(R.id.dairyProductsRecylerView);
            managerForDairyProducts = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            dairyProductsRecylerView.setLayoutManager(managerForDairyProducts);
            dairyProductsRecylerView.setItemAnimator(new DefaultItemAnimator());

            dairyProductsList = new ArrayList<>();
            dairyProductsAdepter = new MyProductAdepter(dairyProductsList, getContext());
            dairyProductsRecylerView.setAdapter(dairyProductsAdepter);

            groceryRecylerView = view.findViewById(R.id.groceryRecylerView);
            managerForGrocery = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            groceryRecylerView.setLayoutManager(managerForGrocery);
            groceryRecylerView.setItemAnimator(new DefaultItemAnimator());

            groceryList = new ArrayList<>();
            groceryAdapter = new MyProductAdepter(groceryList, getContext());
            groceryRecylerView.setAdapter(groceryAdapter);

            networkLayout = view.findViewById(R.id.li_network);
            btnNetWork = view.findViewById(R.id.btnNetWork);

            dairyProductSeeAll = view.findViewById(R.id.dairyProductSeeAll);

            vegetablesRecyelerView = view.findViewById(R.id.vegetablesRecylerView);
            RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            vegetablesRecyelerView.setLayoutManager(layoutManager1);
            vegetablesRecyelerView.setItemAnimator(new DefaultItemAnimator());

            vegetablesRecyelerView.setAdapter(myProductAdepterForVegetaibles);

            getSliderData();
            //getGroceryWithMyPagination();
           // getVegitablesWithMyPagination();
          //  getDairyProudctsData();
          //  getDataForRecylerLabels();
            checkConnetion();
        }

        otherProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ListingProducts.class);
                intent.setAction("list");
                intent.putExtra("flag", "Other Product");
                startActivity(intent);
            }
        });
        {

            handcrafts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ListingProducts.class);
                    intent.setAction("list");
                    intent.putExtra("flag", "handcraft");
                    startActivity(intent);
                }
            });


            fruits.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ListingProducts.class);
                    intent.setAction("list");
                    intent.putExtra("flag", "Fruits");
                    startActivity(intent);
                }
            });
            vegetables.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ListingProducts.class);
                    intent.setAction("list");
                    intent.putExtra("flag", "Vegitable");
                    startActivity(intent);
                }
            });

            groceryViewMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ListingProducts.class);
                    intent.setAction("list");
                    intent.putExtra("flag", "Grocery");
                    startActivity(intent);
                }
            });

            handcraftsViewMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ListingProducts.class);
                    intent.setAction("list");
                    intent.putExtra("flag", "handcraft");
                    startActivity(intent);
                }
            });

            groceryMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ListingProducts.class);
                    intent.setAction("list");
                    intent.putExtra("flag", "Grocery");
                    startActivity(intent);
                }
            });
            
            grocery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ListingProducts.class);
                    intent.setAction("list");
                    intent.putExtra("flag", "Grocery");
                    startActivity(intent);
                }
            });

            other.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ListingProducts.class);
                    intent.setAction("list");
                    intent.putExtra("flag", "Meat Products");
                    startActivity(intent);
                }
            });

            dairyProducts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ListingProducts.class);
                    intent.setAction("list");
                    intent.putExtra("flag", "Dairy Product");
                    startActivity(intent);
                }
            });

            dairyMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ListingProducts.class);
                    intent.setAction("list");
                    intent.putExtra("flag", "Dairy Product");
                    startActivity(intent);
                }
            });

            dairyProductSeeAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ListingProducts.class);
                    intent.setAction("list");
                    intent.putExtra("flag", "Dairy Product");
                    startActivity(intent);
                }
            });

            vegetablesMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), ListingProducts.class);
                    intent.setAction("list");
                    intent.putExtra("flag", "Vegitable");
                    startActivity(intent);
                }
            });

            vegetablesViewMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), ListingProducts.class);
                    intent.setAction("list");
                    intent.putExtra("flag", "Vegitable");
                    startActivity(intent);
                }
            });

            btnNetWork.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkConnetion();
                }
            });
        }
        return view;
    }

    private void getDataForRecylerLabels() {
            Query firestore = FirebaseFirestore.getInstance()
                    .collection("Labels").orderBy("date", Query.Direction.DESCENDING);

            firestore.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                    Log.d("SSSSSSAFFFFF", "onSuccess: sssssA in");
                    labelHolderList.clear();
                    for (DocumentSnapshot snapshot :queryDocumentSnapshots.getDocuments()){
                        LabelHolder labelHolder = snapshot.toObject(LabelHolder.class);
                        labelHolderList.add(labelHolder);
                    }
                    Log.d("SSSSSSAFFFFF", "onSuccess: sssssA out");

                    lablesAdepter = new LablesAdepter(labelHolderList,getContext());
                    recyclerLabel.setAdapter(lablesAdepter);
                    shimmer_view_container4All.setVisibility(View.GONE);
                    recyclerLabel.setVisibility(View.VISIBLE);
                }
            });
    }

    private void getDairyProudctsData() {

        Query query = FirebaseFirestore.getInstance().collection("Product")
                .orderBy("date", Query.Direction.DESCENDING)
                .whereEqualTo("category","Dairy Product").whereEqualTo("homeVisible","show")
                .limit(limit);

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                dairyProductsList.clear();
                if (value.getDocuments() != null) {
                    for (DocumentSnapshot document : value.getDocuments()) {
                        ProductHolder productModel = document.toObject(ProductHolder.class);
                        productModel.setDocId(document.getId());
                        dairyProductsList.add(productModel);
                    }
                    getDataForRecylerLabels();
                    shimmer_view_container3DairyProducts.setVisibility(View.GONE);
                    dairyProductsRecylerView.setVisibility(View.VISIBLE);
                    dairyMore.setVisibility(View.VISIBLE);
                    dairyProductsAdepter.notifyDataSetChanged();
                }

            }
        });
    }


    private void getSliderData() {

        CollectionReference firestore = FirebaseFirestore.getInstance().collection("Slider");
        firestore.orderBy("date", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot snapshot:queryDocumentSnapshots.getDocuments()){
                    SliderHolder holder = snapshot.toObject(SliderHolder.class);
                    list.add(holder);
                }

                getHandCraftsData();
             //   getGroceryWithMyPagination();
                shimmer_view_container.stopShimmer();
                shimmer_view_container.setVisibility(View.GONE);
                mainLayout.setVisibility(View.VISIBLE);

                adapter = new SliderAdapterExample(getContext(),list);
                sliderView.setSliderAdapter(adapter);
            }
        });
        /*firestore.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                dots = new TextView[queryDocumentSnapshots.size()];
                for (int i=0;i<queryDocumentSnapshots.size();i++) {
                    dots[i] = new TextView(getContext());
                    dots[i].setText(Html.fromHtml("&#9679"));
                    dots[i].setTextSize(18);
                    indecatersLayout.addView(dots[i]);
                }
            }
        });*/

      /*  PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(6)
                .setPageSize(6)
                .build();

        SliderOptions = new  FirestorePagingOptions.Builder<SliderHolder>()
                .setQuery(firestore, config, new SnapshotParser<SliderHolder>() {
                    @NonNull
                    @Override
                    public SliderHolder parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                        SliderHolder holder = snapshot.toObject(SliderHolder.class);
                        holder.setDocID(snapshot.getId());


                        return holder;
                    }
                }).build();

        sliderAdepter = new SliderAdepter( SliderOptions,getContext());
       // viewPager2.setAdapter(sliderAdepter);*/

    }

    private void getHandCraftsData() {

        Query query = FirebaseFirestore.getInstance().collection("Product")
                .orderBy("date", Query.Direction.DESCENDING)
                .whereEqualTo("category","handcraft").whereEqualTo("homeVisible","show").limit(limit);



        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.d("SSSSSSSS", "Listen failed.", error);
                    return;
                }


                if (value.getDocuments() != null){
                    handcraftProductsList.clear();
                    for (DocumentSnapshot document : value.getDocuments()) {
                        ProductHolder productModel = document.toObject(ProductHolder.class);
                        productModel.setDocId(document.getId());
                        handcraftProductsList.add(productModel);
                        Log.d("testingggggg", "Liste"+productModel.getTitle());
                    }
                    shimmer_view_containerHandCrafts.stopShimmer();
                    shimmer_view_containerHandCrafts.setVisibility(View.GONE);
                    handCraftRecylerView.setVisibility(View.VISIBLE);

                    handcraftsViewMore.setVisibility(View.VISIBLE);
                    getGroceryWithMyPagination();
                 //   getVegitablesWithMyPagination();


                    handcraftAdepter.notifyDataSetChanged();
                }



            }
        });
    }

    private void getGroceryWithMyPagination() {

        Query query = FirebaseFirestore.getInstance().collection("Product")
                .orderBy("date", Query.Direction.DESCENDING)
                .whereEqualTo("category","Grocery").whereEqualTo("homeVisible","show").limit(limit);



        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w("SSSSSSSS", "Listen failed.", error);
                    return;
                }


                if (value.getDocuments() != null){
                    groceryList.clear();
                    for (DocumentSnapshot document : value.getDocuments()) {
                        ProductHolder productModel = document.toObject(ProductHolder.class);
                        productModel.setDocId(document.getId());
                        groceryList.add(productModel);
                    }
                    shimmer_view_container1Grocery.stopShimmer();
                    shimmer_view_container1Grocery.setVisibility(View.GONE);
                    groceryRecylerView.setVisibility(View.VISIBLE);

                    groceryMore.setVisibility(View.VISIBLE);
                    getVegitablesWithMyPagination();


                    groceryAdapter.notifyDataSetChanged();
                }



            }
        });
    }

    private void getVegitablesWithMyPagination() {

        Query query = FirebaseFirestore.getInstance().collection("Product")
               .orderBy("date", Query.Direction.DESCENDING)
                .whereEqualTo("category","Vegitable").whereEqualTo("homeVisible","show").limit(limit);

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                vegitablesHolderList.clear();
                if (value.getDocuments() != null) {
                    for (DocumentSnapshot document : value.getDocuments()) {
                        ProductHolder productModel = document.toObject(ProductHolder.class);
                        productModel.setDocId(document.getId());
                        vegitablesHolderList.add(productModel);
                    }
                    getDairyProudctsData();

                    shimmer_view_container2Vegitables.stopShimmer();
                    shimmer_view_container2Vegitables.setVisibility(View.GONE);
                    vegetablesRecyelerView.setVisibility(View.VISIBLE);
                    vegetablesMore.setVisibility(View.VISIBLE);
                    myProductAdepterForVegetaibles.notifyDataSetChanged();
                }
            }
        });
    }

    private void checkConnetion() {
        if (InternetConnection.checkConnection(getContext())) {
            networkLayout.setVisibility(View.GONE);
        } else {

            networkLayout.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
      //  vegetablesAdepter.startListening();

        Log.d(TAG, "onStart: ");

        //sliderAdepter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        //sliderAdepter.stopListening();
        Log.d(TAG, "onStop: ");
       // vegetablesAdepter.stopListening();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");/*
        if (vegetablesAdepter != null ){
            vegetablesAdepter.refresh();
        }*/

        if (myProductAdepterForVegetaibles != null){
            myProductAdepterForVegetaibles.notifyDataSetChanged();
        }
        if (dairyProductsAdepter != null){
            dairyProductsAdepter.notifyDataSetChanged();
        }
        if (groceryAdapter != null){
            groceryAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach: ");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach: ");
    }
}