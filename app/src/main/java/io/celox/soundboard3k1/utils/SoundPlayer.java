package io.celox.soundboard3k1.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

public class SoundPlayer {
    private static final String TAG = "SoundPlayer";
    private MediaPlayer mediaPlayer;
    private Context context;

    public SoundPlayer(Context context) {
        this.context = context;
    }

    public void playSound(String folderName, String fileName) {
        try {
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                mediaPlayer.release();
            }

            mediaPlayer = new MediaPlayer();
            AssetFileDescriptor afd = context.getAssets().openFd(folderName + "/" + fileName);
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            
            mediaPlayer.prepare();
            mediaPlayer.start();
            
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                    mediaPlayer = null;
                }
            });
        } catch (IOException e) {
            Log.e(TAG, "Error playing sound: " + e.getMessage());
        }
    }

    public void release() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}