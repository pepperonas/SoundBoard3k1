package io.celox.soundboard3k1.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        
        holder.soundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundPlayer.playSound(soundItem.getFolderName(), soundItem.getFileName());
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

    static class SoundViewHolder extends RecyclerView.ViewHolder {
        Button soundButton;

        public SoundViewHolder(@NonNull View itemView) {
            super(itemView);
            soundButton = itemView.findViewById(R.id.sound_button);
        }
    }
}