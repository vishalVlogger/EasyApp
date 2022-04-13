package com.appdroid.ssbtproject.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appdroid.ssbtproject.Activity.CheckoutActivity;
import com.appdroid.ssbtproject.Activity.LoginActivity;
import com.appdroid.ssbtproject.Adapter.CardAdepter;
import com.appdroid.ssbtproject.Database.CardProductHolder;
import com.appdroid.ssbtproject.Database.ProductViewModel;
import com.appdroid.ssbtproject.Database.UserPostsRoomDatabase;
import com.appdroid.ssbtproject.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class CartFragment extends Fragment {
    public static  LinearLayout cardEmptyLayout;
    List<CardProductHolder> productsListFromCard;
    CardAdepter cardAdepter;
    RecyclerView recyclerView;

    public static TextView totalAmount;
    Button checkoutBTN;
    int checkOutAmount = 0;
    int mrpAmount = 0;

    int checkOutAmountForNEWmethod = 0;
    int mrpAmountForNEWmethod = 0;

    RelativeLayout priceBar;
    ProductViewModel productViewModel;

    public static void reload(FragmentActivity activity) {

        CartFragment fragment = (CartFragment) activity.getSupportFragmentManager().findFragmentByTag("card_fragment");

        if (fragment !=null){
            try {
                activity.getSupportFragmentManager().beginTransaction().detach(fragment).attach(fragment).commit();
            }catch (Exception e){
                Log.d("FFFFFFFFDD", "reload: "+e.getLocalizedMessage());
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        cardEmptyLayout = view.findViewById(R.id.cardEmptyLayout);
        recyclerView = view.findViewById(R.id.cardRecyclerView);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        priceBar = view.findViewById(R.id.priceBar);

        totalAmount = view.findViewById(R.id.totalAmount);
        checkoutBTN = view.findViewById(R.id.checkoutBTN);

        productViewModel  = ViewModelProviders.of(this).get(ProductViewModel.class);
        productViewModel.getCardsProductsCounts().observe(getViewLifecycleOwner(), new Observer<List<CardProductHolder>>() {
            @Override
            public void onChanged(List<CardProductHolder> cardProductHolders) {


                checkOutAmountForNEWmethod = 0;
                mrpAmountForNEWmethod = 0 ;

                for (CardProductHolder holder : cardProductHolders){
                    checkOutAmountForNEWmethod = checkOutAmountForNEWmethod + + ( holder.getNumber_of_packs() * holder.getProductPrice());
                    mrpAmountForNEWmethod = mrpAmountForNEWmethod  + (holder.getNumber_of_packs() * holder.getProductMrpPrice());
                }

                Log.d("SAFGGGGGF", "totla value: "+checkOutAmountForNEWmethod);
                Log.d("SAFGGGGGF", "totla MRP : "+mrpAmountForNEWmethod);

                long finalSum = checkOutAmountForNEWmethod;

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (finalSum == 0){
                            priceBar.setVisibility(View.GONE);
                        }
                        totalAmount.setText(""+ finalSum);
                    }
                });
            }
        });

        checkoutBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FirebaseAuth.getInstance().getCurrentUser() != null){
                    Intent intent = new Intent(getContext(), CheckoutActivity.class);
                    intent.putExtra("totalAmount",totalAmount.getText().toString());
                    intent.putExtra("totlaMrpAmount",String.valueOf(mrpAmountForNEWmethod));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else {
                    showPicodeBox();
                }
            }
        });

        //getCheckOutAmount();
        getCardProducts();
        return view;
    }

    private void showPicodeBox() {
        Dialog dialog =  new Dialog(getContext());
        dialog.setContentView(R.layout.submit_pincode);
        dialog.setCancelable(false);
        EditText pincode = dialog.findViewById(R.id.pincode);
        Button btn  = dialog.findViewById(R.id.btn);
        ImageView closeDialog  = dialog.findViewById(R.id.closeDialog);

        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!pincode.getText().toString().isEmpty()) {
                    if (pincode.getText().toString().length() == 6){
                        checkPincodeAvailbility(pincode.getText().toString());
                        dialog.dismiss();
                    }else {
                        Toast.makeText(getContext(), "Enter 6-digit Pincode.", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(getContext(), "Enter 6-digit Pincode.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }

    private void checkPincodeAvailbility(String pinCode) {

        DocumentReference firestore = FirebaseFirestore.getInstance()
                .collection("Content").document("Categories");

        firestore.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                List<String> list = (List<String>) documentSnapshot.get("pinCode");

                if (list.contains(pinCode)){
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("pincode",MODE_PRIVATE).edit();
                    editor.putString("code",pinCode);
                    editor.apply();
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(getContext(), getString(R.string.notAvailble), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void getCardProducts() {

        checkOutAmount  = 0;
        mrpAmount = 0;
        Thread  thread  = new Thread(new Runnable() {
            @Override
            public void run() {
                productsListFromCard = UserPostsRoomDatabase.getInstance(getContext())
                        .postsDao()
                        .getAllCardProductsList();
                Collections.reverse(productsListFromCard);


                Log.d("DDDDDDDDD", "run inside run : "+productsListFromCard.size());

                for (CardProductHolder cardProductHolder : productsListFromCard){
                    checkOutAmount = checkOutAmount + ( cardProductHolder.getNumber_of_packs() * cardProductHolder.getProductPrice());
                    mrpAmount = mrpAmount + (cardProductHolder.getNumber_of_packs() * cardProductHolder.getProductMrpPrice());
                }

                cardAdepter = new CardAdepter(getContext(),productsListFromCard,getActivity());


                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        recyclerView.setAdapter(cardAdepter);
                        // Stuff that updates the UI
                   //     totalAmount.setText(""+checkOutAmount);
                        Log.d("chetan_girnare", "run: "+checkOutAmount);
                        if (productsListFromCard.size()==0){
                            cardEmptyLayout.setVisibility(View.VISIBLE);
                            priceBar.setVisibility(View.GONE);
                        }else {
                            cardEmptyLayout.setVisibility(View.GONE);
                            priceBar.setVisibility(View.VISIBLE);
                        }
                    }
                });

                cardAdepter.notifyDataSetChanged();
            }
        });
        thread.start();
    }
}
