package io.celox.soundboard3k1.adapters;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
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
import java.io.OutputStream;
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
            
            Uri videoUri;
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Use MediaStore for Android 10+
                ContentResolver resolver = context.getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4");
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_MOVIES + "/SoundBoard3k1");
                
                videoUri = resolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues);
                
                if (videoUri != null) {
                    try (InputStream inputStream = context.getAssets().open(assetPath);
                         OutputStream outputStream = resolver.openOutputStream(videoUri)) {
                        
                        byte[] buffer = new byte[8192];
                        int length;
                        while ((length = inputStream.read(buffer)) > 0) {
                            outputStream.write(buffer, 0, length);
                        }
                    }
                }
            } else {
                // Fallback for older Android versions
                File moviesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
                File appDir = new File(moviesDir, "SoundBoard3k1");
                if (!appDir.exists()) {
                    appDir.mkdirs();
                }
                
                File outputFile = new File(appDir, fileName);
                
                try (InputStream inputStream = context.getAssets().open(assetPath);
                     FileOutputStream outputStream = new FileOutputStream(outputFile)) {
                    
                    byte[] buffer = new byte[8192];
                    int length;
                    while ((length = inputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, length);
                    }
                }
                
                videoUri = FileProvider.getUriForFile(context,
                        context.getPackageName() + ".fileprovider",
                        outputFile);
            }
            
            if (videoUri != null) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("video/mp4");
                shareIntent.putExtra(Intent.EXTRA_STREAM, videoUri);
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                
                Intent chooser = Intent.createChooser(shareIntent, "Teile Video über...");
                context.startActivity(chooser);
            } else {
                Toast.makeText(context, "Fehler beim Erstellen der Video-URI", Toast.LENGTH_SHORT).show();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Fehler: " + e.getMessage(), Toast.LENGTH_LONG).show();
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