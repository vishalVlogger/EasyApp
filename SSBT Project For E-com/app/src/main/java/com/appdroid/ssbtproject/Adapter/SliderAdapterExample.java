package com.appdroid.ssbtproject.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appdroid.ssbtproject.Activity.VideoPlayer;
import com.appdroid.ssbtproject.Holder.SliderHolder;
import com.appdroid.ssbtproject.R;
import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapterExample extends SliderViewAdapter<SliderAdapterExample.SliderAdapterVH> {

    private Context context;
    private List<SliderHolder> list = new ArrayList<>();

    public SliderAdapterExample(Context context, List<SliderHolder> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH holder, final int position) {

        SliderHolder sliderItem = list.get(position);

        Glide.with(context).load(sliderItem.getThumb()).into(holder.slide);
        holder.headline.setText(sliderItem.getTitle());

        if (sliderItem.getFlag().equals("Video")){
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
            public void onClick(View v) {
                if (sliderItem.getFlag().equals("Video")){
                    Intent intent = new Intent(context, VideoPlayer.class);
                    intent.putExtra("link",sliderItem.getLink());
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return list.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        ImageView slide;
        TextView headline;
        RelativeLayout bottomOpec,videoLayout;
        public SliderAdapterVH(View itemView) {
            super(itemView);
            slide = itemView.findViewById(R.id.slideImage);
            headline = itemView.findViewById(R.id.headlines);
            bottomOpec = itemView.findViewById(R.id.bottomOpec);
            videoLayout = itemView.findViewById(R.id.videoLayout);
            //this.itemView = itemView;
        }
    }

}
