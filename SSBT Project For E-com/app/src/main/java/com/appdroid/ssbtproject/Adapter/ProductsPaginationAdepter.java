package com.appdroid.ssbtproject.Adapter;


import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appdroid.ssbtproject.Holder.SliderHolder;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;

import org.jetbrains.annotations.NotNull;

public class ProductsPaginationAdepter extends FirestorePagingAdapter<SliderHolder,ProductsPaginationAdepter.ViewHolder> {


    /**
     * Construct a new FirestorePagingAdapter from the given {@link FirestorePagingOptions}.
     *
     * @param options
     */
    public ProductsPaginationAdepter(@NonNull @NotNull FirestorePagingOptions<SliderHolder> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull ProductsPaginationAdepter.ViewHolder holder, int position, @NonNull @NotNull SliderHolder model) {

    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return null;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
        }
    }
}
