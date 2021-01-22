package nl.royoosterlee.halights.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import java.util.Map;

import nl.royoosterlee.halights.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    private SettingsViewModel settingsViewModel;

//    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        setPreferencesFromResource(R.xml.root_preferences, rootKey);
//        settingsViewModel =
//                ViewModelProviders.of(this).get(SettingsViewModel.class);
//        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
//        final TextView textView = root.findViewById(R.id.text_notifications);
//        settingsViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
//        return root;
//    }
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }

    @Override
    public void onStart() {
        super.onStart();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        System.out.println("PREFERENCES");

        Map<String, ?> preferences = sharedPreferences.getAll();

        for (Map.Entry<String, ?> entry : preferences.entrySet()) {
            System.out.println(entry.getKey() + "/" + entry.getValue());
        }

        System.out.println("[SettingsFragment2] onStart");
    }
}