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
            String fileName = assetPath.substring(assetPath.lastIndexOf('/') + 1);
            String mimeType = getMimeType(fileName);
            
            // Use cache directory for consistent sharing approach
            File cacheDir = new File(context.getCacheDir(), "shared_videos");
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            
            File cachedFile = new File(cacheDir, fileName);
            
            // Copy asset to cache directory
            try (InputStream inputStream = context.getAssets().open(assetPath);
                 FileOutputStream outputStream = new FileOutputStream(cachedFile)) {
                
                byte[] buffer = new byte[8192];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
            }
            
            // Create URI using FileProvider
            Uri videoUri = FileProvider.getUriForFile(context,
                    context.getPackageName() + ".fileprovider",
                    cachedFile);
            
            if (videoUri != null) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType(mimeType);
                shareIntent.putExtra(Intent.EXTRA_STREAM, videoUri);
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                
                Intent chooser = Intent.createChooser(shareIntent, "Teile Video über...");
                if (shareIntent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(chooser);
                } else {
                    Toast.makeText(context, "Keine App zum Teilen von Videos gefunden", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Fehler beim Erstellen der Video-URI", Toast.LENGTH_SHORT).show();
            }
            
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Fehler beim Kopieren der Video-Datei: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Unerwarteter Fehler beim Teilen: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    
    private String getMimeType(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
        switch (extension) {
            case "mp4":
                return "video/mp4";
            case "webm":
                return "video/webm";
            case "mkv":
                return "video/x-matroska";
            case "avi":
                return "video/x-msvideo";
            case "mov":
                return "video/quicktime";
            case "3gp":
                return "video/3gpp";
            default:
                return "video/*";
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