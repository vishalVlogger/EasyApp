package com.appdroid.ssbtproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appdroid.ssbtproject.Holder.ReviewHolder;
import com.appdroid.ssbtproject.Holder.UserHolder;
import com.appdroid.ssbtproject.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class ReviewsAdepter extends RecyclerView.Adapter<ReviewsAdepter.ViewHolder> {

    Context context;
    List<ReviewHolder> list;

    public ReviewsAdepter(Context context, List<ReviewHolder> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.review_item,parent,false);
        return new ReviewsAdepter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ReviewHolder  reviewHolder = list.get(position);

            holder.getUserProfile(reviewHolder.getUserId());
            holder.review.setText(reviewHolder.getReview());
            holder.stars.setText("Rating : "+reviewHolder.getRatting());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView userName,review,stars;
        public ViewHolder(@NonNull View view) {
            super(view);
            userName = view.findViewById(R.id.userName);
            review = view.findViewById(R.id.review);
            stars = view.findViewById(R.id.stars);
        }

        public void getUserProfile(String userId) {
            Query firestore = FirebaseFirestore.getInstance().collection("userInfo")
                    .whereEqualTo("userId",userId).limit(1);
            firestore.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                               UserHolder userHolder = snapshot.toObject(UserHolder.class);
                               userName.setText(userHolder.getName());
                        }
                }
            });
        }
    }
}
