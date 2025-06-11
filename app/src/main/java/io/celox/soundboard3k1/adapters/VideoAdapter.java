package io.celox.soundboard3k1.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
        
        holder.itemView.setOnLongClickListener(v -> {
            shareVideoFile(video);
            return true;
        });
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
    
    private void shareVideoFile(VideoItem video) {
        try {
            // Copy video file from assets to cache directory
            String assetPath = video.getPath();
            File cacheDir = new File(context.getCacheDir(), "shared_videos");
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            
            // Extract the actual filename from the path
            String originalFileName = assetPath.substring(assetPath.lastIndexOf('/') + 1);
            File outputFile = new File(cacheDir, originalFileName);
            
            try (InputStream inputStream = context.getAssets().open(assetPath);
                 FileOutputStream outputStream = new FileOutputStream(outputFile)) {
                
                byte[] buffer = new byte[8192];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
            }
            
            // Get URI using FileProvider
            Uri contentUri = FileProvider.getUriForFile(context,
                    context.getPackageName() + ".fileprovider",
                    outputFile);
            
            // Create share intent
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("video/mp4");
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            
            // Start chooser
            Intent chooser = Intent.createChooser(shareIntent, "Teile Video über...");
            context.startActivity(chooser);
            
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Fehler beim Teilen der Datei", Toast.LENGTH_SHORT).show();
        }
    }

    static class VideoViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        VideoViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_video_name);
        }
    }
}