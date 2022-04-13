package com.appdroid.ssbt_delivery_boy.Activity.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appdroid.ssbt_delivery_boy.R;
import com.appdroid.ssbt_delivery_boy.adapter.OrderdProductsAdeoter;
import com.appdroid.ssbt_delivery_boy.holder.DailyOrderHolder;
import com.appdroid.ssbt_delivery_boy.holder.OrderProductHolder;
import com.appdroid.ssbt_delivery_boy.holder.ProductHolder;
import com.appdroid.ssbt_delivery_boy.holder.UserHolder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;

public class DailyOrderDetailActivity extends AppCompatActivity {
    DailyOrderHolder dailyOrderHolder;
    private static final String TAG = "AAAAAAAAAAAAA";
    TextView userName,userContactNumber,userAddress,totalBill;
    RecyclerView recyclerView;
    OrderdProductsAdeoter orderdProductsAdepter;
    Button deliveryCompleted,cancel;

    ImageView imgBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_order_detail);
        dailyOrderHolder = (DailyOrderHolder) getIntent().getSerializableExtra("all");

        userAddress = findViewById(R.id.userAddress);
        userContactNumber = findViewById(R.id.userContactNumber);
        userName = findViewById(R.id.userName);
        totalBill = findViewById(R.id.totalBill);

        imgBack = findViewById(R.id.img_back);

        deliveryCompleted = findViewById(R.id.deliveryCompleted);
        cancel = findViewById(R.id.cancel);

        recyclerView = findViewById(R.id.productsList);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        orderdProductsAdepter = new OrderdProductsAdeoter(dailyOrderHolder.getProductsList(),this);
        recyclerView.setAdapter(orderdProductsAdepter);


        getUserInfo(dailyOrderHolder);

     //   totalBill.setText("Total Amount : "+orderHolder.getOrderTotal());

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        deliveryCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(DailyOrderDetailActivity.this);
                dialog.setContentView(R.layout.delivery_confurmation);
                Button yesBTN = dialog.findViewById(R.id.yesBTN);
                Button noBTN = dialog.findViewById(R.id.noBTN);

                dialog.show();

                yesBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        updateOrderStatus(dailyOrderHolder,dialog);
                        //
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

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(DailyOrderDetailActivity.this);
                dialog.setContentView(R.layout.order_cancel_confurmation);
                Button yesBTN = dialog.findViewById(R.id.yesBTN);
                Button noBTN = dialog.findViewById(R.id.noBTN);



                yesBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                        Dialog reasonDialog = new Dialog(DailyOrderDetailActivity.this);
                        reasonDialog.setContentView(R.layout.order_cancel_reason_dialog);
                        EditText reason = reasonDialog.findViewById(R.id.reason);
                        Button submitBTN = reasonDialog.findViewById(R.id.submitBTN);

                        reasonDialog.show();

                        submitBTN.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!reason.getText().toString().isEmpty()){
                                    reasonDialog.dismiss();
                                    //updateCanceletionReson(orderHolder,reason.getText().toString(),reasonDialog);
                                }else {
                                    Toast.makeText(DailyOrderDetailActivity.this, "Enter Valid Reason...", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });



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
        });

    }
    private void updateOrderStatus(DailyOrderHolder orderHolder, Dialog dialog) {
        DocumentReference firestore = FirebaseFirestore.getInstance().collection("DailyOrders")
                .document(orderHolder.getDocId());

        Log.d(TAG, "updateOrderStatus: "+orderHolder.getDocId());

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("status","delivered");
        firestore.update(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    dialog.dismiss();
                    updateProductsStock(orderHolder.getProductsList());
                }
            }
        });
    }
    private void updateProductsStock(List<OrderProductHolder> orderProductHolderList) {
        for (OrderProductHolder orderProductHolder : orderProductHolderList){

            if (orderProductHolder.getProductPack().contains("gm")){

                int packValueInGram = Integer.parseInt((orderProductHolder.getProductPack().replaceAll("[^0-9]", "")));
                int finalValue = packValueInGram * (orderProductHolder.getProductQty());
                Log.d(TAG, "onBindViewHolder: "+finalValue +" product Name "+orderProductHolder.getProductTitle());

                updateProductStockValues(orderProductHolder,finalValue);

            }else if (orderProductHolder.getProductPack().contains("kg")){

                int packValueInKG = Integer.parseInt(orderProductHolder.getProductPack().replaceAll("[^0-9]", ""));
                packValueInKG = (packValueInKG*1000);
                int finalValue = packValueInKG * (orderProductHolder.getProductQty());
                updateProductStockValues(orderProductHolder,finalValue);
                Log.d(TAG, "onBindViewHolder:kg "+finalValue+" product Name "+orderProductHolder.getProductTitle());

            }else if (orderProductHolder.getProductPack().contains("ml")){
                int packValueInMl = Integer.parseInt(orderProductHolder.getProductPack().replaceAll("[^0-9]", ""));
                int finalValue = packValueInMl*(orderProductHolder.getProductQty());
                updateProductStockValues(orderProductHolder,finalValue);
            }else if (orderProductHolder.getProductPack().contains("liter")){
                int packValueInLiter = Integer.parseInt(orderProductHolder.getProductPack().replaceAll("[^0-9]", ""));
                int packInML = packValueInLiter*1000;
                int finalValue = packInML*(orderProductHolder.getProductQty());
                updateProductStockValues(orderProductHolder,finalValue);
            }
        }  // pisc and dusen work remain
    }
    private void updateProductStockValues(OrderProductHolder orderProductHolder, int finalValue) {

        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Product")
                .document(orderProductHolder.getProductDocId());

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                ProductHolder productHolder = documentSnapshot.toObject(ProductHolder.class);
                long updatedStock = (productHolder.getQuntity() - finalValue);
                Log.d(TAG, "product name  : "+productHolder.getTitle()+" product quntity : "+productHolder.getQuntity() +" final updated Stock "+updatedStock);

                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("quntity",updatedStock);

                DocumentReference reference = FirebaseFirestore.getInstance().collection("Product")
                        .document(orderProductHolder.getProductDocId());

                reference.update(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: final stock updated .....");
                    }
                });
            }
        });

    }
    public void getUserInfo(DailyOrderHolder model) {
        CollectionReference firestore = FirebaseFirestore.getInstance().collection("userInfo");
        firestore.whereEqualTo("userId",model.getUserID()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                    UserHolder userHolder = snapshot.toObject(UserHolder.class);
                    userName.setText(userHolder.getName());
                    userAddress.setText(userHolder.getAddress());
                    userContactNumber.setText(userHolder.getNo());
                }
            }
        });
    }
}