package io.celox.soundboard3k1.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import io.celox.soundboard3k1.R;
import io.celox.soundboard3k1.models.SoundItem;
import io.celox.soundboard3k1.utils.SoundPlayer;

public class SoundAdapter extends RecyclerView.Adapter<SoundAdapter.SoundViewHolder> {
    
    private List<SoundItem> soundItems;
    private Context context;
    private SoundPlayer soundPlayer;

    public SoundAdapter(Context context, List<SoundItem> soundItems) {
        this.context = context;
        this.soundItems = soundItems;
        this.soundPlayer = new SoundPlayer(context);
    }

    @NonNull
    @Override
    public SoundViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sound_item, parent, false);
        return new SoundViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SoundViewHolder holder, int position) {
        SoundItem soundItem = soundItems.get(position);
        holder.soundButton.setText(soundItem.getDisplayName());
        
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundPlayer.playSound(soundItem.getFolderName(), soundItem.getFileName());
            }
        });
        
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                shareAudioFile(soundItem);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return soundItems.size();
    }

    public void updateSounds(List<SoundItem> newSounds) {
        this.soundItems = newSounds;
        notifyDataSetChanged();
    }

    public void release() {
        if (soundPlayer != null) {
            soundPlayer.release();
        }
    }
    
    private void shareAudioFile(SoundItem soundItem) {
        try {
            // Copy audio file from assets to cache directory
            String assetPath = soundItem.getFolderName() + "/" + soundItem.getFileName();
            File cacheDir = new File(context.getCacheDir(), "audio");
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            
            File outputFile = new File(cacheDir, soundItem.getFileName());
            
            try (InputStream inputStream = context.getAssets().open(assetPath);
                 FileOutputStream outputStream = new FileOutputStream(outputFile)) {
                
                byte[] buffer = new byte[1024];
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
            shareIntent.setType("audio/*");
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            
            // Start chooser
            Intent chooser = Intent.createChooser(shareIntent, "Teile Audio über...");
            context.startActivity(chooser);
            
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Fehler beim Teilen der Datei", Toast.LENGTH_SHORT).show();
        }
    }

    static class SoundViewHolder extends RecyclerView.ViewHolder {
        TextView soundButton;

        public SoundViewHolder(@NonNull View itemView) {
            super(itemView);
            soundButton = itemView.findViewById(R.id.sound_button);
        }
    }
}