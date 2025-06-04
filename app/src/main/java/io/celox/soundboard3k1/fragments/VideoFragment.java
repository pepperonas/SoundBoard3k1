package io.celox.soundboard3k1.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.celox.soundboard3k1.R;
import io.celox.soundboard3k1.adapters.VideoAdapter;
import io.celox.soundboard3k1.models.VideoItem;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VideoFragment extends Fragment {
    private RecyclerView recyclerView;
    private VideoAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        
        recyclerView = view.findViewById(R.id.recycler_view_videos);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        
        List<VideoItem> videos = loadVideosFromAssets();
        adapter = new VideoAdapter(getContext(), videos);
        recyclerView.setAdapter(adapter);
        
        return view;
    }

    private List<VideoItem> loadVideosFromAssets() {
        List<VideoItem> videos = new ArrayList<>();
        try {
            String[] videoFiles = getContext().getAssets().list("video");
            if (videoFiles != null) {
                for (String fileName : videoFiles) {
                    if (fileName.endsWith(".mp4") || fileName.endsWith(".webm") || fileName.endsWith(".mkv")) {
                        String displayName = fileName.substring(0, fileName.lastIndexOf('.'));
                        videos.add(new VideoItem(displayName, "video/" + fileName));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return videos;
    }
}