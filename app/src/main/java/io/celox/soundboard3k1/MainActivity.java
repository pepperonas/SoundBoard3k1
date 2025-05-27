package io.celox.soundboard3k1;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.celox.soundboard3k1.databinding.ActivityMainBinding;
import io.celox.soundboard3k1.fragments.SoundFragment;
import io.celox.soundboard3k1.models.SoundCategory;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private List<SoundCategory> soundCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (binding.toolbar != null) {
            setSupportActionBar(binding.toolbar);
        }

        loadSoundCategories();
        setupBottomNavigation();
        
        if (savedInstanceState == null && !soundCategories.isEmpty()) {
            loadFragment(soundCategories.get(0));
        }
    }

    private void loadSoundCategories() {
        soundCategories = new ArrayList<>();
        try {
            String[] folders = getAssets().list("");
            for (String folder : folders) {
                String[] files = getAssets().list(folder);
                if (files != null && files.length > 0 && (files[0].endsWith(".mp3") || files[0].endsWith(".wav"))) {
                    String displayName = formatFolderName(folder);
                    soundCategories.add(new SoundCategory(folder, displayName, folder));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String formatFolderName(String folderName) {
        String formatted = folderName.replace("_", " ");
        return formatted.substring(0, 1).toUpperCase(Locale.ROOT) + formatted.substring(1);
    }

    private void setupBottomNavigation() {
        Menu menu = binding.navView.getMenu();
        menu.clear();
        
        // BottomNavigationView supports max 5 items
        int maxItems = Math.min(soundCategories.size(), 5);
        
        for (int i = 0; i < maxItems; i++) {
            SoundCategory category = soundCategories.get(i);
            MenuItem menuItem = menu.add(Menu.NONE, i, Menu.NONE, category.getDisplayName());
            menuItem.setIcon(R.drawable.ic_dashboard_black_24dp);
        }

        binding.navView.setOnItemSelectedListener(item -> {
            int position = item.getItemId();
            if (position < soundCategories.size()) {
                loadFragment(soundCategories.get(position));
                return true;
            }
            return false;
        });
    }

    private void loadFragment(SoundCategory category) {
        Fragment fragment = SoundFragment.newInstance(category.getFolderPath(), category.getDisplayName());
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment_activity_main, fragment);
        transaction.commit();
        
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(category.getDisplayName());
        }
    }
}