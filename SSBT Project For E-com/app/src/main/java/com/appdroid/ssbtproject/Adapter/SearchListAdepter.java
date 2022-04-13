package com.appdroid.ssbtproject.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appdroid.ssbtproject.Activity.ListingProducts;
import com.appdroid.ssbtproject.R;
import com.appdroid.ssbtproject.Room.SearchQueryRoomHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SearchListAdepter extends RecyclerView.Adapter<SearchListAdepter.ViewHolder> implements Filterable {
    Context context;
    List<SearchQueryRoomHolder> list;
    public static  List<SearchQueryRoomHolder> searchListAll;


    public SearchListAdepter(Context context, List<SearchQueryRoomHolder> lists) {
        this.context = context;
        this.list = lists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_search,parent,false);
        this.searchListAll = new ArrayList<>();
        this.searchListAll.addAll(list);
        return new SearchListAdepter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
          holder.searchQuery.setText(list.get(position).getQuery());
        holder.searchItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ListingProducts.class);
                intent.setAction("fromSearch");
                intent.putExtra("flag",list.get(position).getQuery());
                context.startActivity(intent);
            }
        });

    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<SearchQueryRoomHolder> filterFils = new ArrayList<>();

            if (list != null && searchListAll != null){
                Log.d("HHHHHH", "performFiltering: "+searchListAll.size()+" "+list.size());
                if (charSequence.toString().isEmpty()){
                    filterFils.addAll(searchListAll);
                }else {
                    for (SearchQueryRoomHolder query : searchListAll){
                        if (query.getQuery().toLowerCase().contains(charSequence.toString().toLowerCase())){
                            filterFils.add(query);
                        }
                    }
                }
            }

            FilterResults  filterResults = new FilterResults();
            filterResults.values = filterFils;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            if (list != null){
                list.clear();
                list.addAll((Collection<? extends SearchQueryRoomHolder>) filterResults.values);
                notifyDataSetChanged();
            }

            Log.d("DDDDDDDAAAAAAA", "publishResults: "+list.size());
        }
    };

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }





    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView searchQuery;
        LinearLayout searchItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            searchQuery = itemView.findViewById(R.id.txt_productName);
            searchItem = itemView.findViewById(R.id.searchItem);
        }
    }
}

