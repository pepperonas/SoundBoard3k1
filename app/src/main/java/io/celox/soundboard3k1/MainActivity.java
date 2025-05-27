package io.celox.soundboard3k1;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
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

    private static final String PREFS_NAME = "theme_prefs";
    private static final String KEY_NIGHT_MODE = "night_mode";
    
    private ActivityMainBinding binding;
    private List<SoundCategory> soundCategories;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Load theme preference before super.onCreate()
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isNightMode = sharedPreferences.getBoolean(KEY_NIGHT_MODE, false);
        AppCompatDelegate.setDefaultNightMode(
            isNightMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );
        
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (binding.toolbar != null) {
            setSupportActionBar(binding.toolbar);
        }

        loadSoundCategories();
        setupBottomNavigation();
        styleBottomNavigation();
        
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
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_theme_toggle) {
            toggleTheme();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void toggleTheme() {
        boolean isNightMode = (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES);
        
        // Save the new preference
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_NIGHT_MODE, !isNightMode);
        editor.apply();
        
        // Apply the new theme
        AppCompatDelegate.setDefaultNightMode(
            isNightMode ? AppCompatDelegate.MODE_NIGHT_NO : AppCompatDelegate.MODE_NIGHT_YES
        );
    }
    
    private void styleBottomNavigation() {
        boolean isNightMode = (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES);
        
        if (!isNightMode) {
            // Light theme - set neutral gray background
            binding.navView.setBackgroundColor(Color.parseColor("#F5F5F5"));
            binding.navView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F5F5F5")));
            
            // Create color state list for icon/text
            int[][] states = new int[][] {
                new int[] { android.R.attr.state_selected },
                new int[] { -android.R.attr.state_selected }
            };
            
            int[] colors = new int[] {
                Color.parseColor("#000000"), // Selected - Black
                Color.parseColor("#757575")   // Unselected - Medium gray
            };
            
            ColorStateList colorStateList = new ColorStateList(states, colors);
            binding.navView.setItemIconTintList(colorStateList);
            binding.navView.setItemTextColor(colorStateList);
            binding.navView.setItemRippleColor(null); // Remove ripple
            binding.navView.setItemActiveIndicatorColor(null); // Remove active indicator
        }
    }
}