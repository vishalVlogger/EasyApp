package com.appdroid.ssbtproject.Fragment;

import android.app.Dialog;
import android.content.Intent;
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

import com.appdroid.ssbtproject.Activity.AddYourDetails;
import com.appdroid.ssbtproject.Activity.EditProfile;
import com.appdroid.ssbtproject.Activity.LoginActivity;
import com.appdroid.ssbtproject.Activity.MyOrders;
import com.appdroid.ssbtproject.Activity.RequestProduct;
import com.appdroid.ssbtproject.Activity.SettingActivity;
import com.appdroid.ssbtproject.Holder.UserHolder;
import com.appdroid.ssbtproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.HashMap;

public class ProfileFragment extends Fragment {

    View view;
    LinearLayout loginLayout,profile_edit_layout,myOrders,changeContactNumber,manageAddress, settingLayout,requastProduct;
    RelativeLayout not_login_layout, completeProfileLayout;

    Button loginBTN, logout, yes, no;

    TextView userName,userEmail,userNumber;
    UserHolder userHolder;
    Dialog dialog;

    BottomSheetDialog sheetDialog;

    EditText address;
    Button updateBTN,complete_your_profileBTN;
    ImageView closeDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        loginLayout = view.findViewById(R.id.loginLayout);
        completeProfileLayout = view.findViewById(R.id.completeProfileLayout);
        settingLayout = view.findViewById(R.id.settingLayout);
        not_login_layout = view.findViewById(R.id.not_login_layout);
        logout = view.findViewById(R.id.logout);
        myOrders = view.findViewById(R.id.myOrders);
        requastProduct = view.findViewById(R.id.requastProduct);
        loginBTN = view.findViewById(R.id.loginBTN);

        changeContactNumber = view.findViewById(R.id.changeContactNumber);

        profile_edit_layout = view.findViewById(R.id.profile_edit_layout);

        userName = view.findViewById(R.id.userName);
        userEmail = view.findViewById(R.id.userEmail);
        userNumber = view.findViewById(R.id.userNumber);
        manageAddress = view.findViewById(R.id.manageAddress);
        complete_your_profileBTN = view.findViewById(R.id.complete_your_profileBTN);


        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.update_address_dialog);
        dialog.setCancelable(true);
        closeDialog = dialog.findViewById(R.id.closeDialog);

        address = dialog.findViewById(R.id.Address);
        updateBTN = dialog.findViewById(R.id.update);

        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        if (FirebaseAuth.getInstance().getCurrentUser() == null){
            not_login_layout.setVisibility(View.VISIBLE);
            loginLayout.setVisibility(View.GONE);
        }else {
            not_login_layout.setVisibility(View.GONE);
            loginLayout.setVisibility(View.VISIBLE);
            getUserInfo();
        }

        sheetDialog = new BottomSheetDialog(getContext());
        sheetDialog.setContentView(R.layout.popup_logout);
        sheetDialog.setCancelable(true);
        yes = sheetDialog.findViewById(R.id.yes);
        no = sheetDialog.findViewById(R.id.no);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sheetDialog.show();
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                sheetDialog.dismiss();

                ProfileFragment fragment = (ProfileFragment) getActivity().getSupportFragmentManager().findFragmentByTag("profileFragment");
                Log.d("FFFFFFFFDD", "reload: "+fragment.getTag());
                if (fragment != null){
                    try {
                        getActivity().getSupportFragmentManager().beginTransaction().detach(fragment).attach(fragment).commit();
                    }catch (Exception e){
                        Log.d("FFFFFFFFDD", "reload: "+e.getLocalizedMessage());
                    }
                }


            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sheetDialog.dismiss();
            }
        });

        complete_your_profileBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),AddYourDetails.class);
                startActivity(intent);
            }
        });

        loginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        profile_edit_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),EditProfile.class);
                intent.putExtra("all",userHolder);
                startActivity(intent);
            }
        });

        myOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), MyOrders.class));
            }
        });

        changeContactNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),LoginActivity.class));
            }
        });

        manageAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        updateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!address.getText().toString().isEmpty()){
                    updateAddress();
                }else {
                    Toast.makeText(getContext(), "Please Enter Address", Toast.LENGTH_SHORT).show();
                }
            }
        });

        settingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SettingActivity.class));
            }
        });


        requastProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(getContext(), RequestProduct.class);
                    intent.putExtra("all",userHolder);
                    startActivity(intent);
            }
        });


        checkIsUserRegister();
        return view;
    }

    private void updateAddress() {
        HashMap<String,Object> hashMap  = new HashMap<>();
        hashMap.put("address",address.getText().toString());

        DocumentReference firestore = FirebaseFirestore.getInstance().collection("userInfo").document(userHolder.getDocID());
        firestore.update(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getContext(), "Updated.", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });
    }

    private void getUserInfo() {

        Query query =  FirebaseFirestore.getInstance()
                .collection("userInfo")
                .whereEqualTo("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentSnapshot snapshot : value.getDocuments()){
                        userHolder = snapshot.toObject(UserHolder.class);
                        userHolder.setDocID(snapshot.getId());

                        userName.setText(userHolder.getName());
                        userEmail.setText(userHolder.getEmail());
                        userNumber.setText(userHolder.getNo());
                        address.setText(userHolder.getAddress());
                    }
            }
        });

    }

    private void checkIsUserRegister() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null){
            completeProfileLayout.setVisibility(View.GONE);
            loginLayout.setVisibility(View.GONE);
            not_login_layout.setVisibility(View.VISIBLE);
        }else {
            Query query = FirebaseFirestore.getInstance()
                    .collection("userInfo")
                    .whereEqualTo("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());

            query.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (value.getDocuments().size() > 0) {
                        completeProfileLayout.setVisibility(View.GONE);
                        loginLayout.setVisibility(View.VISIBLE);
                        not_login_layout.setVisibility(View.GONE);
                    } else {
                        //completeProfileDialog.show();

                        completeProfileLayout.setVisibility(View.VISIBLE);
                        loginLayout.setVisibility(View.GONE);
                        not_login_layout.setVisibility(View.GONE);

                    /*Intent intent = new Intent(CheckoutActivity.this,AddYourDetails.class);
                    startActivity(intent);*/
                    }
                }
            });
        }
    }

}
