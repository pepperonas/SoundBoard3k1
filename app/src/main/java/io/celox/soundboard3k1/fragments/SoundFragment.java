package io.celox.soundboard3k1.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.celox.soundboard3k1.R;
import io.celox.soundboard3k1.adapters.SoundAdapter;
import io.celox.soundboard3k1.models.SoundItem;

public class SoundFragment extends Fragment {

    private static final String ARG_FOLDER_PATH = "folder_path";
    private static final String ARG_CATEGORY_NAME = "category_name";

    private String folderPath;
    private String categoryName;
    private RecyclerView recyclerView;
    private SoundAdapter soundAdapter;

    public static SoundFragment newInstance(String folderPath, String categoryName) {
        SoundFragment fragment = new SoundFragment();
        Bundle args = new Bundle();
        args.putString(ARG_FOLDER_PATH, folderPath);
        args.putString(ARG_CATEGORY_NAME, categoryName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            folderPath = getArguments().getString(ARG_FOLDER_PATH);
            categoryName = getArguments().getString(ARG_CATEGORY_NAME);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sound, container, false);
        
        recyclerView = view.findViewById(R.id.sound_recycler_view);
        setupRecyclerView();
        loadSounds();
        
        return view;
    }

    private void setupRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        
        soundAdapter = new SoundAdapter(getContext(), new ArrayList<>());
        recyclerView.setAdapter(soundAdapter);
    }

    private void loadSounds() {
        List<SoundItem> soundItems = new ArrayList<>();
        
        try {
            String[] files = getContext().getAssets().list(folderPath);
            for (String file : files) {
                if (file.endsWith(".mp3") || file.endsWith(".wav")) {
                    String displayName = formatFileName(file);
                    soundItems.add(new SoundItem(file, displayName, folderPath));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        soundAdapter.updateSounds(soundItems);
    }

    private String formatFileName(String fileName) {
        String name = fileName.substring(0, fileName.lastIndexOf('.'));
        name = name.replace("_", " ");
        name = name.replace("-", " ");
        
        String[] words = name.split(" ");
        StringBuilder formatted = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                formatted.append(word.substring(0, 1).toUpperCase())
                        .append(word.substring(1).toLowerCase())
                        .append(" ");
            }
        }
        
        return formatted.toString().trim();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (soundAdapter != null) {
            soundAdapter.release();
        }
    }
}