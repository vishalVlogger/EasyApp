package com.appdroid.ssbtproject.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appdroid.ssbtproject.R;

import androidx.fragment.app.Fragment;

public class FavouriteFragment extends Fragment {

    View view;
    /*Integer[] img_product = {R.drawable.rectangle_gray_box_home,R.drawable.rectangle_gray_box_home,
            R.drawable.rectangle_gray_box_home,R.drawable.rectangle_gray_box_home};
    String[] txt_ltr = {"1 Ltr","1 Ltr","1 Ltr","1 Ltr"};

    private RecyclerView recyclerView;
    private FavouriteAdapter favouriteAdapter;
    private ArrayList<GroceryModel> groceryModels;
    LinearLayout li_favourites,li_cart;*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_favourite, container, false);

        /*li_favourites = view.findViewById(R.id.li_favourites);
        li_cart = view.findViewById(R.id.li_cart);
        li_favourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                li_cart.setVisibility(View.VISIBLE);
                li_favourites.setVisibility(View.GONE);
            }
        });
        *//*-------New RecyclerView Code Here-------*//*
        recyclerView = view.findViewById(R.id.recyclerFavourites);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        groceryModels = new ArrayList<>();

        for (int i = 0; i < img_product.length; i++) {
            GroceryModel model = new GroceryModel(img_product[i],txt_ltr[i]);
            groceryModels.add(model);
        }
        favouriteAdapter = new FavouriteAdapter(getContext(), groceryModels);
        recyclerView.setAdapter(favouriteAdapter);*/
        return view;
    }
}
