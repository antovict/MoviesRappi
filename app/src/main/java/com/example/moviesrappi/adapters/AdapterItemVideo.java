package com.example.moviesrappi.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviesrappi.R;
import com.example.moviesrappi.YouTubeActivity;
import com.example.moviesrappi.cache.SingletonCache;
import com.example.moviesrappi.models.Video;
import com.example.moviesrappi.models.Videos;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.ArrayList;

public class AdapterItemVideo extends RecyclerView.Adapter<AdapterItemVideo.ItemVideoHolder>{
    ArrayList<Video> listVideos;
    Videos videos;
    Context mContext;


    public AdapterItemVideo(Context ctx, Videos videos){
        this.videos = videos;
        this.listVideos = videos.getResults();
        mContext = ctx;
        SingletonCache.initInstance();
    }

    @NonNull
    @Override
    public ItemVideoHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_video, viewGroup, false);
        ItemVideoHolder pvh = new ItemVideoHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final ItemVideoHolder viewHolder, int i) {
        viewHolder.txtTitle.setText(listVideos.get(i).getName());
        viewHolder.txtTitle.setSelected(true);
        viewHolder.content.setBackgroundColor(mContext.getResources().getColor(R.color.transparent));
        viewHolder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=(Integer) v.getTag();
                Video video= listVideos.get(position);
                switch (v.getId())
                {
                    case R.id.content:
                        SingletonCache.viewVideo = video;
                        Intent myIntent = new Intent(mContext, YouTubeActivity.class);
                        myIntent.putExtra("VIDEO_ID", video.getKey()); //Optional parameters
                        mContext.startActivity(myIntent);
                        break;
                }
            }
        });
        viewHolder.content.setTag(i);
    }

    @Override
    public int getItemCount() {
        return listVideos.size();
    }

    public static class ItemVideoHolder extends RecyclerView.ViewHolder {
        TextView txtTitle;
        LinearLayout content;

        ItemVideoHolder(View convertView) {
            super(convertView);
            txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
            content = (LinearLayout) convertView.findViewById(R.id.content);
        }
    }
}


