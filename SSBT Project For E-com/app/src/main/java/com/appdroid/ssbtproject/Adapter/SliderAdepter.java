package com.appdroid.ssbtproject.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appdroid.ssbtproject.Activity.VideoPlayer;
import com.appdroid.ssbtproject.Holder.SliderHolder;
import com.appdroid.ssbtproject.R;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;

public class SliderAdepter extends FirestorePagingAdapter<SliderHolder,SliderAdepter.ViewHolder> {
    Context context;

    public SliderAdepter(@NonNull FirestorePagingOptions<SliderHolder> options, Context context) {
        super(options);
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.slider_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

  /*  @Override
    public long getItemId(int position) {
        return  position;
    }*/

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull SliderHolder model) {
        Glide.with(context).load(model.getThumb()).into(holder.slide);
        holder.headline.setText(model.getTitle());

        if (model.getFlag().equals("Video")){
            holder.videoLayout.setVisibility(View.VISIBLE);
            holder.slide.setAlpha(0.5f);
           // holder.bottomOpec.setVisibility(View.GONE);
        }else {
            holder.slide.setAlpha(1f);
            holder.videoLayout.setVisibility(View.GONE);
          //  holder.bottomOpec.setVisibility(View.VISIBLE);

        }
        holder.slide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (model.getFlag().equals("Video")){
                    Intent  intent = new Intent(context, VideoPlayer.class);
                    intent.putExtra("link",model.getLink());
                    context.startActivity(intent);
                }
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView slide;
        TextView headline;
        RelativeLayout bottomOpec,videoLayout;
        public ViewHolder(@NonNull View view) {
            super(view);
            slide = itemView.findViewById(R.id.slideImage);
            headline = itemView.findViewById(R.id.headlines);
            bottomOpec = itemView.findViewById(R.id.bottomOpec);
            videoLayout = itemView.findViewById(R.id.videoLayout);
        }
    }
}
