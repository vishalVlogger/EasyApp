package com.appdroid.ssbtproject.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.appdroid.ssbtproject.Database.CardProductHolder;
import com.appdroid.ssbtproject.Database.UserPostsRoomDatabase;
import com.appdroid.ssbtproject.Holder.OrderProductHolder;
import com.appdroid.ssbtproject.Holder.ProductHolder;
import com.appdroid.ssbtproject.Holder.UserHolder;
import com.appdroid.ssbtproject.Notification.APIService;
import com.appdroid.ssbtproject.Notification.Client;
import com.appdroid.ssbtproject.Notification.Data;
import com.appdroid.ssbtproject.Notification.Responce;
import com.appdroid.ssbtproject.Notification.Sender;
import com.appdroid.ssbtproject.Notification.Token;
import com.appdroid.ssbtproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Response;

public class CheckoutActivity extends AppCompatActivity implements PaymentResultListener {

    private static final String TAG = "CheckOutActivity";
    TextView title,grandTotal,txt_addressCheck;
    Dialog completeProfileDialog,updateAddressDialog,progressDialog;
    Intent intent;
    String totalAmount,totalMRPamount;
    LinearLayout updateAddress;
    ImageView closeDialog;

    TextView edUpdateAddressFromDialog,totlaAmountPrice,discount;
    Button updateBTNfromDilog,complete_your_profileBTN;

    List<CardProductHolder> productsListFromCard;
    List<OrderProductHolder> orderProductHolderList;
    UserHolder userHolder;

    RadioButton razorpay,cod;
    Button confirmOrder;
    ImageView img_back;
    Dialog placeOrderConfurmationDialog;
    Checkout checkout;

    APIService apiService;
    SharedPreferences sharedPreferences;
    Random rand;
    EditText noteForOrder;
    int n;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        rand = new Random();
        n = rand.nextInt(100000);

        apiService = Client.getRetrofit("https://fcm.googleapis.com/").create(APIService.class);

        intent = getIntent();
        totalAmount =  intent.getStringExtra("totalAmount");
        totalMRPamount = intent.getStringExtra("totlaMrpAmount");


        completeProfileDialog = new Dialog(this);
        completeProfileDialog.setContentView(R.layout.complete_your_profile);
        completeProfileDialog.setCancelable(false);
        complete_your_profileBTN = completeProfileDialog.findViewById(R.id.complete_your_profileBTN);

        updateAddressDialog = new Dialog(this);
        updateAddressDialog.setContentView(R.layout.update_address_dialog);
        updateAddressDialog.setCancelable(true);

        progressDialog = new Dialog(this);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setCancelable(false);

        noteForOrder = findViewById(R.id.noteForOrder);


        edUpdateAddressFromDialog = updateAddressDialog.findViewById(R.id.Address);
        updateBTNfromDilog  = updateAddressDialog.findViewById(R.id.update);



        closeDialog = completeProfileDialog.findViewById(R.id.closeDialog);

        img_back = findViewById(R.id.img_back);
        title = findViewById(R.id.txt_filter);
        grandTotal = findViewById(R.id.grandTotal);
        txt_addressCheck = findViewById(R.id.txt_addressCheck);
        updateAddress = findViewById(R.id.updateAddress);

        totlaAmountPrice = findViewById(R.id.totlaAmountPrice);
        discount = findViewById(R.id.discount);


        totlaAmountPrice.setText("₹"+totalMRPamount);
        discount.setText("- ₹"+(Integer.parseInt(totalMRPamount) - Integer.parseInt(totalAmount)));

        cod = findViewById(R.id.cod);
        razorpay = findViewById(R.id.razorpay);
        confirmOrder = findViewById(R.id.confirmOrder);


        productsListFromCard = new ArrayList<>();
        orderProductHolderList = new ArrayList<>();

        sharedPreferences = getSharedPreferences("pay",MODE_PRIVATE);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



        razorpay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    cod.setChecked(false);
                }
            }
        });

        cod.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    razorpay.setChecked(false);
                }
            }
        });


        confirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!cod.isChecked() && !razorpay.isChecked()){
                    Toast.makeText(CheckoutActivity.this, "Please Select Payment Method.", Toast.LENGTH_SHORT).show();
                }else if (cod.isChecked() || razorpay.isChecked()){
                    makeConfurmation();
                }
            }
        });

        complete_your_profileBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckoutActivity.this,AddYourDetails.class);
                startActivity(intent);
            }
        });


        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        updateAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAddressDialog.show();
            }
        });

        updateBTNfromDilog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edUpdateAddressFromDialog.getText().toString().isEmpty()){
                    updateAddressOnly(edUpdateAddressFromDialog.getText().toString());
                }
            }
        });

        title.setText("Checkout Details");
        grandTotal.setText("₹"+totalAmount);
        checkIsUserRegister();

        getCardProducts();
    }

    private void makeConfurmation() {
        placeOrderConfurmationDialog = new Dialog(CheckoutActivity.this);
        placeOrderConfurmationDialog.setContentView(R.layout.place_order_confermation__dialog);
        Button yes,no;
        yes = placeOrderConfurmationDialog.findViewById(R.id.yes);
        no = placeOrderConfurmationDialog.findViewById(R.id.no);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cod.isChecked()){

                    progressDialog.show();
                    updateProductsStock(orderProductHolderList);
                    placeOrder("","Cash On Delivery");
                }else if (razorpay.isChecked()){
                    goWithRazerPay();
                }

            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                placeOrderConfurmationDialog.dismiss();
            }
        });

        placeOrderConfurmationDialog.show();
    }

    private void goWithRazerPay() {
         checkout =  new Checkout();
     //   "rzp_test_IpUQEKYIYuRFnu"
        checkout.setKeyID(sharedPreferences.getString("key","rzp_test_IpUQEKYIYuRFnu"));

        Log.d(TAG, "goWithRazerPay: "+sharedPreferences.getString("key","rzp_test_IpUQEKYIYuRFnu"));
        checkout.setImage(R.drawable.dandagez_logo);
        JSONObject jsonObject  = new JSONObject();
        try {
            jsonObject.put("name",""+userHolder.getName());
            //jsonObject.put("description",""+contestHolder.getContestTitle());
            jsonObject.put("currency","INR");
            jsonObject.put("amount",(Integer.parseInt(totalAmount)*100));

            jsonObject.put("prefill.email", userHolder.getEmail());
            jsonObject.put("prefill.contact",userHolder.getNo());
            //  jsonObject.put("prefill.email",userHolder.ge);


            checkout.open(CheckoutActivity.this,jsonObject);

            placeOrderConfurmationDialog.dismiss();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void placeOrder(String razerPayResponceID,String paymentMode) {


        HashMap<String,Object> hashMap = new HashMap<>();

        hashMap.put("orderDate",new Timestamp(new Date()));
        hashMap.put("orderId", "ORD"+n);
        hashMap.put("orderTotal",Integer.parseInt(totalAmount));
        hashMap.put("status","ordered");
        hashMap.put("paymentMode",paymentMode);
        hashMap.put("rpayPaymentId",razerPayResponceID);
        hashMap.put("userNo",FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
        hashMap.put("note",noteForOrder.getText().toString());
        hashMap.put("productDetails",orderProductHolderList);
        hashMap.put("userInfo",userHolder);

        CollectionReference firestore = FirebaseFirestore.getInstance().collection("Orders");

        firestore.add(hashMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()){
                    Toast.makeText(CheckoutActivity.this, "Order Placed.", Toast.LENGTH_SHORT).show();
                    placeOrderConfurmationDialog.dismiss();
                    sendNotification();
                    clearCard("ORD"+n);
                }
            }
        });

    }

    private void sendNotification() {
           DatabaseReference allToken = FirebaseDatabase.getInstance().getReference("TokenDetails");

           allToken.addListenerForSingleValueEvent(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot snapshot) {
                   for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                       Token token = dataSnapshot.getValue(Token.class);
                       Data data = new Data();

                       data.setTitle("Hey We have a new Order");
                       data.setBody("This is the body");
                       data.setIcon(R.drawable.ic_bucket_icon);
                       data.setSent(token.getToken());



                       Sender sender =new Sender(data,token.getToken(),"Hey We have a new Order","This is the body");

                       apiService.sendNotification(sender)
                               .enqueue(new retrofit2.Callback<Responce>() {
                                   @Override
                                   public void onResponse(Call<Responce> call, Response<Responce> response) {
                                       Log.d(TAG, "onResponse: notification sended");
                                   }

                                   @Override
                                   public void onFailure(Call<Responce> call, Throwable t) {

                                   }
                               });
                   }
               }

               @Override
               public void onCancelled(@NonNull DatabaseError error) {

               }
           });

    }

    private void updateProductsStock(List<OrderProductHolder> orderProductHolderList) {

        int i=0;
        for (OrderProductHolder orderProductHolder : orderProductHolderList){


                if(i++ == orderProductHolderList.size() - 1){
                    // Last iteration
                    if (orderProductHolder.getProductPack().contains("gm")){

                        int packValueInGram = Integer.parseInt((orderProductHolder.getProductPack().replaceAll("[^0-9]", "")));
                        int finalValue = packValueInGram * (orderProductHolder.getProductQty());
                        Log.d(TAG, "onBindViewHolder: "+finalValue +" product Name "+orderProductHolder.getProductTitle());

                        updateProductStockValues(orderProductHolder,finalValue,true);

                    }else if (orderProductHolder.getProductPack().contains("kg")){

                        int packValueInKG = Integer.parseInt(orderProductHolder.getProductPack().replaceAll("[^0-9]", ""));
                        packValueInKG = (packValueInKG*1000);
                        int finalValue = packValueInKG * (orderProductHolder.getProductQty());
                        updateProductStockValues(orderProductHolder,finalValue,true);
                        Log.d(TAG, "onBindViewHolder:kg "+finalValue+" product Name "+orderProductHolder.getProductTitle());

                    }else if (orderProductHolder.getProductPack().contains("ml")){
                        int packValueInMl = Integer.parseInt(orderProductHolder.getProductPack().replaceAll("[^0-9]", ""));
                        int finalValue = packValueInMl*(orderProductHolder.getProductQty());
                        updateProductStockValues(orderProductHolder,finalValue,true);
                    }else if (orderProductHolder.getProductPack().contains("liter")){
                        int packValueInLiter = Integer.parseInt(orderProductHolder.getProductPack().replaceAll("[^0-9]", ""));
                        int packInML = packValueInLiter*1000;
                        int finalValue = packInML*(orderProductHolder.getProductQty());
                        updateProductStockValues(orderProductHolder,finalValue,true);


                    }else if (orderProductHolder.getProductPack().contains("dozen") && !orderProductHolder.getProductPack().contains("half-dozen")) {
                        int packValueInDozen = Integer.parseInt(orderProductHolder.getProductPack().replaceAll("[^0-9]", ""));
                        int packValueInDozenToPices  = (packValueInDozen*12)*(orderProductHolder.getProductQty());
                        Log.d(TAG, "onBindViewHolderdozen: " + packValueInDozenToPices);
                        updateProductStockValues(orderProductHolder,packValueInDozenToPices,false);

                    }else if (orderProductHolder.getProductPack().contains("half-dozen")) {
                        int packValueInDozenToPices  = 6;
                        packValueInDozenToPices = packValueInDozenToPices*(orderProductHolder.getProductQty());
                        Log.d(TAG, "onBindViewHolderdozen: " + packValueInDozenToPices);
                        updateProductStockValues(orderProductHolder,packValueInDozenToPices,true);

                    }else if (orderProductHolder.getProductPack().contains("pcs")){
                        int packValueInMl = Integer.parseInt(orderProductHolder.getProductPack().replaceAll("[^0-9]", ""));
                        packValueInMl = packValueInMl*(orderProductHolder.getProductQty());
                        updateProductStockValues(orderProductHolder,packValueInMl,true);
                    }




                }else {
                    if (orderProductHolder.getProductPack().contains("gm")){

                        int packValueInGram = Integer.parseInt((orderProductHolder.getProductPack().replaceAll("[^0-9]", "")));
                        int finalValue = packValueInGram * (orderProductHolder.getProductQty());
                        Log.d(TAG, "onBindViewHolder: "+finalValue +" product Name "+orderProductHolder.getProductTitle());

                        updateProductStockValues(orderProductHolder,finalValue,false);

                    }else if (orderProductHolder.getProductPack().contains("kg")){

                        int packValueInKG = Integer.parseInt(orderProductHolder.getProductPack().replaceAll("[^0-9]", ""));
                        packValueInKG = (packValueInKG*1000);
                        int finalValue = packValueInKG * (orderProductHolder.getProductQty());
                        updateProductStockValues(orderProductHolder,finalValue,false);
                        Log.d(TAG, "onBindViewHolder:kg "+finalValue+" product Name "+orderProductHolder.getProductTitle());

                    }else if (orderProductHolder.getProductPack().contains("ml")){
                        int packValueInMl = Integer.parseInt(orderProductHolder.getProductPack().replaceAll("[^0-9]", ""));
                        int finalValue = packValueInMl*(orderProductHolder.getProductQty());
                        updateProductStockValues(orderProductHolder,finalValue,false);
                    }else if (orderProductHolder.getProductPack().contains("liter")){
                        int packValueInLiter = Integer.parseInt(orderProductHolder.getProductPack().replaceAll("[^0-9]", ""));
                        int packInML = packValueInLiter*1000;
                        int finalValue = packInML*(orderProductHolder.getProductQty());
                        updateProductStockValues(orderProductHolder,finalValue,false);


                    }else if (orderProductHolder.getProductPack().contains("dozen") && !orderProductHolder.getProductPack().contains("half-dozen")) {
                        int packValueInDozen = Integer.parseInt(orderProductHolder.getProductPack().replaceAll("[^0-9]", ""));
                        int packValueInDozenToPices  = (packValueInDozen*12)*(orderProductHolder.getProductQty());
                        Log.d(TAG, "onBindViewHolderdozen: " + packValueInDozenToPices);
                        updateProductStockValues(orderProductHolder,packValueInDozenToPices,false);

                    }else if (orderProductHolder.getProductPack().contains("half-dozen")) {
                        int packValueInDozenToPices  = 6;
                        packValueInDozenToPices = packValueInDozenToPices*(orderProductHolder.getProductQty());
                        Log.d(TAG, "onBindViewHolderdozen: " + packValueInDozenToPices);
                        updateProductStockValues(orderProductHolder,packValueInDozenToPices,false);

                    }else if (orderProductHolder.getProductPack().contains("pcs")){
                        int packValueInMl = Integer.parseInt(orderProductHolder.getProductPack().replaceAll("[^0-9]", ""));
                        packValueInMl = packValueInMl*(orderProductHolder.getProductQty());
                        updateProductStockValues(orderProductHolder,packValueInMl,false);
                    }
                }

            }

        }  // pisc and dusen work remain


    private void updateProductStockValues(OrderProductHolder orderProductHolder, int finalValue,boolean isLastPosition) {

        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Product")
                .document(orderProductHolder.getProductDocId());

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                ProductHolder productHolder = documentSnapshot.toObject(ProductHolder.class);
                long updatedStock = (productHolder.getQuntity()-finalValue);
                Log.d(TAG, "product name  : "+productHolder.getTitle()+" product quntity : "+productHolder.getQuntity() +" final updated Stock "+updatedStock);

                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("quntity",updatedStock);

                DocumentReference reference = FirebaseFirestore.getInstance().collection("Product")
                        .document(orderProductHolder.getProductDocId());

                reference.update(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if (isLastPosition){

                            if (progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                            Intent intent = new Intent(CheckoutActivity.this,OrderConfurm.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.putExtra("orderID","ORD"+n);
                            startActivity(intent);
                        }
                        Log.d(TAG, "onSuccess: final stock updated .....");
                    }
                });
            }
        });

    }

    private void clearCard(String s) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                    UserPostsRoomDatabase.getInstance(CheckoutActivity.this)
                            .postsDao()
                            .clearCard();

                   /* Intent intent = new Intent(CheckoutActivity.this,OrderConfurm.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("orderID",s);
                    startActivity(intent);*/
            }
        }).start();
    }


    public void getCardProducts() {


        Thread  thread  = new Thread(new Runnable() {
            @Override
            public void run() {
                productsListFromCard = UserPostsRoomDatabase.getInstance(CheckoutActivity.this)
                        .postsDao()
                        .getAllCardProductsList();
                Collections.reverse(productsListFromCard);

                HashSet holderSet = new HashSet();
                holderSet.addAll(productsListFromCard);

                productsListFromCard.clear();
                productsListFromCard.addAll(holderSet);

                
                Log.d("DDDDDDDDD", "run inside run : "+productsListFromCard.size());

                orderProductHolderList.clear();
                for (CardProductHolder cardProductHolder : productsListFromCard){
                    getProductDetails(cardProductHolder);
                }


            }
        });
        thread.start();
    }

    private void getProductDetails(CardProductHolder cardProductHolder) {
        DocumentReference firestore = FirebaseFirestore.getInstance().collection("Product").document(cardProductHolder.getProduct_doc_id());

        firestore.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null){
                    Log.d("EEEEEE", "onEvent: "+error.getLocalizedMessage());
                    return;
                }
                if (value != null){
                    ProductHolder productHolder = value.toObject(ProductHolder.class);

                    OrderProductHolder orderProductHolder = new OrderProductHolder();

                    orderProductHolder.setProductTitle(productHolder.getTitle());
                    orderProductHolder.setProductImage(productHolder.getImage());
                    orderProductHolder.setProductPack(productHolder.getSellUnit().get(cardProductHolder.getPack_id()).getPack());
                    orderProductHolder.setProductPrice(productHolder.getSellUnit().get(cardProductHolder.getPack_id()).getPrice());
                    orderProductHolder.setProductQty(cardProductHolder.getNumber_of_packs());
                    orderProductHolder.setProductDocId(cardProductHolder.getProduct_doc_id());

                    orderProductHolderList.add(orderProductHolder);
                    Log.d("DDDDDDDDDDD", "onEvent: "+orderProductHolderList.size());

                }
            }
        });
    }

    private void updateAddressOnly(String updatedText) {

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("address",updatedText);

        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("userInfo")
                .document(userHolder.getDocID());
        documentReference.update(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(CheckoutActivity.this, "Address Updated.", Toast.LENGTH_SHORT).show();
                    updateAddressDialog.dismiss();

                }
            }
        });
    }

    private void checkIsUserRegister() {

        Query query =  FirebaseFirestore.getInstance()
                .collection("userInfo")
                .whereEqualTo("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.getDocuments().size() > 0){

                        if (completeProfileDialog.isShowing()){
                            completeProfileDialog.dismiss();
                        }

                    for (DocumentSnapshot snapshot : value.getDocuments()){
                        userHolder = snapshot.toObject(UserHolder.class);
                        userHolder.setDocID(snapshot.getId());
                        txt_addressCheck.setText(userHolder.getAddress());
                        edUpdateAddressFromDialog.setText(userHolder.getAddress());
                    }
                }else{
                    completeProfileDialog.show();

                    Intent intent = new Intent(CheckoutActivity.this,AddYourDetails.class);
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    public void onPaymentSuccess(String s) {

        progressDialog.show();
        placeOrder(s,"Online");
        Log.d(TAG, "onPaymentSuccess: "+s);
        updateProductsStock(orderProductHolderList);
    }

    @Override
    public void onPaymentError(int i, String s) {
        cod.setChecked(false);
        razorpay.setChecked(false);
    }

}