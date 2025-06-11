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
            String assetPath = video.getPath();
            String originalFileName = assetPath.substring(assetPath.lastIndexOf('/') + 1);
            
            // Replace spaces and special characters for WhatsApp compatibility
            String safeFileName = originalFileName.replaceAll("[^a-zA-Z0-9.-]", "_");
            
            // Use cache directory - works best for all Android versions
            File cacheDir = new File(context.getCacheDir(), "videos");
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            
            File outputFile = new File(cacheDir, safeFileName);
            
            // Always copy fresh to ensure file is accessible
            try (InputStream inputStream = context.getAssets().open(assetPath);
                 FileOutputStream outputStream = new FileOutputStream(outputFile)) {
                
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
                outputStream.flush();
                outputStream.close();
                inputStream.close();
            }
            
            // Create URI using FileProvider
            Uri videoUri = FileProvider.getUriForFile(context,
                    context.getPackageName() + ".fileprovider",
                    outputFile);
            
            // Share via Intent with explicit WhatsApp handling
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("video/*");
            shareIntent.putExtra(Intent.EXTRA_STREAM, videoUri);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            
            // Start chooser
            context.startActivity(Intent.createChooser(shareIntent, "Video teilen"));
            
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Fehler beim Kopieren der Video-Datei", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Fehler beim Teilen des Videos", Toast.LENGTH_SHORT).show();
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