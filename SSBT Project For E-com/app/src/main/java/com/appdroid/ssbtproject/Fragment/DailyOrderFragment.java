package com.appdroid.ssbtproject.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appdroid.ssbtproject.Activity.LoginActivity;
import com.appdroid.ssbtproject.Adapter.DailyOrderAdepter;
import com.appdroid.ssbtproject.Database.ProductViewModel;
import com.appdroid.ssbtproject.Holder.DailyOrderHolder;
import com.appdroid.ssbtproject.Holder.OrderProductHolder;
import com.appdroid.ssbtproject.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DailyOrderFragment extends Fragment {
    private static final String TAG = "dailyOrderFragment";
    RecyclerView recyclerView;
    List<OrderProductHolder> list;

    Button loginBTN;
    DailyOrderAdepter adepter;
    RelativeLayout not_login_layout,lowerLayout;

   public static TextView totalPrice;
    long totalAmount = 0;

    ProductViewModel productViewModel;
    //RelativeLayout relativeLayout;
   // LinearLayout lay1, lay2;
    Timer t, t1;
    private Animation animMoveRight, animMoveLeftRight;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_daily_order, container, false);
        not_login_layout = view.findViewById(R.id.not_login_layout);
        list = new ArrayList<>();

        loginBTN = view.findViewById(R.id.loginBTN);
        //relativeLayout = view.findViewById(R.id.relativeLayout);
        /*lay1 = view.findViewById(R.id.lay1);
        lay2 = view.findViewById(R.id.lay2);*/
        recyclerView =view.findViewById(R.id.dailyOrderRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adepter = new DailyOrderAdepter(getContext(),list);
        recyclerView.setAdapter(adepter);
        totalPrice = view.findViewById(R.id.totalPrice);

        lowerLayout = view.findViewById(R.id.lowerLayout);

        animMoveRight = AnimationUtils.loadAnimation(getContext(),R.anim.move_side);
        animMoveLeftRight = AnimationUtils.loadAnimation(getContext(),R.anim.move_left_right);

       // relativeLayout.setVisibility(View.VISIBLE);
        t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
               /* lay1.startAnimation(animMoveRight);
                lay1.setVisibility(View.GONE);
                lay2.setVisibility(View.VISIBLE);
                lay2.startAnimation(animMoveLeftRight);*/

                t1 = new Timer();
                t1.schedule(new TimerTask() {
                    @Override
                    public void run() {
                       // lay2.setVisibility(View.GONE);
                    //    relativeLayout.setVisibility(View.GONE);
                    }
                },5000);
            }
        },5000);

        productViewModel  = ViewModelProviders.of(this).get(ProductViewModel.class);

        productViewModel.getDailyOrdersProductsCounts().observe(getActivity(), new Observer<List<OrderProductHolder>>() {
            @Override
            public void onChanged(List<OrderProductHolder> orderProductHolders) {
                Log.d(TAG, "onChanged: "+orderProductHolders.size());
               int totalAmount = 0;
                for (OrderProductHolder orderProductHolder : orderProductHolders){
                    totalAmount = totalAmount + (Integer.parseInt(orderProductHolder.getProductPrice())*orderProductHolder.getProductQty());
                    Log.d(TAG, "onChanged: "+totalAmount);

                    totalPrice.setText(""+totalAmount+"Rs/-");
                }

            }
        });


        loginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            getDailyOrdersProducts();
            not_login_layout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }else {
            not_login_layout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            lowerLayout.setVisibility(View.GONE);
        }

        return view;
    }

    private void getDailyOrdersProducts() {
        DocumentReference firestore = FirebaseFirestore.getInstance().collection("DailyOrders")
                .document(FirebaseAuth.getInstance().getUid());

        firestore.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                DailyOrderHolder holder = documentSnapshot.toObject(DailyOrderHolder.class);
                if (holder != null){
                    list.addAll(holder.getProductsList());
                    adepter.notifyDataSetChanged();
                    for (OrderProductHolder orderProductHolder : holder.getProductsList()){
                        totalAmount = totalAmount + Integer.parseInt(orderProductHolder.getProductPrice());
                    }
                //    totalPrice.setText("Total Cost : "+totalAmount);

                }
            }
        });
    }
}