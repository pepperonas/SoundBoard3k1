package io.celox.soundboard3k1.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import io.celox.soundboard3k1.R;
import io.celox.soundboard3k1.models.VideoItem;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
    private Context context;
    private List<VideoItem> videos;

    public VideoAdapter(Context context, List<VideoItem> videos) {
        this.context = context;
        this.videos = videos;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.video_item, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        VideoItem video = videos.get(position);
        holder.textView.setText(video.getName());
        
        holder.itemView.setOnClickListener(v -> playVideo(video));
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    private void playVideo(VideoItem video) {
        Intent intent = new Intent(context, io.celox.soundboard3k1.VideoPlayerActivity.class);
        intent.putExtra("video_path", video.getPath());
        intent.putExtra("video_name", video.getName());
        context.startActivity(intent);
    }

    static class VideoViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        VideoViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_video_name);
        }
    }
}