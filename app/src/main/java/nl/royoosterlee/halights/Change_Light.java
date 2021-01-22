package nl.royoosterlee.halights;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toolbar;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import top.defaults.colorpicker.ColorObserver;
import top.defaults.colorpicker.ColorPickerView;

public class Change_Light extends AppCompatActivity implements ValueEventListener {
    androidx.appcompat.widget.Toolbar toolbar;
    SeekBar seekBar;

    DatabaseReference ikpmd;
    DatabaseReference light;
    Runnable postNewColor;
    Runnable setNewTitle;

    int color;
    int brightness;

    String title;

    boolean colorChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change__light);

        FirebaseApp fbApp = FirebaseApp.getInstance("halights");
        FirebaseDatabase database = FirebaseDatabase.getInstance(fbApp);
        ikpmd = database.getReference("ikpmd");

        toolbar = findViewById(R.id.cl_toolbar);

        seekBar = findViewById(R.id.brightness_slider);

        postNewColor = new Runnable() {
            @Override
            public void run() {
                System.out.println("postNewColor");

                light.child("color").setValue(color);
                light.child("brightness").setValue(brightness);
            }
        };

        setNewTitle = new Runnable() {
            @Override
            public void run() {
                light.child("title").setValue(title);
            }
        };


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        JSONObject lightData = null;
        try {
            System.out.println("-=-=-=-=-=-=");
            System.out.println((String) extras.get("light"));
            lightData = new JSONObject((String) extras.get("light"));
            light = ikpmd.child(String.valueOf(lightData.get("id")));
            light.addValueEventListener(this);

            changeTitle(lightData.getString("name"));
            title = lightData.getString("name");
            changeSwitch(lightData.getBoolean("on"));
            int clr = lightData.getInt("color");
            brightness = lightData.getInt("brightness");
            System.out.println("BRIGHTNESS: " + brightness);
            changeBrightnessSlider(brightness);
            changeColorPickerColor(clr);
            changeSliderColor(clr);
            changeToolbarColor(clr);
            color = clr;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onStart() {
        super.onStart();

//        toolbar.setBackground();

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(postNewColor, 250);



        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Clicked!!!");
                onBackPressed();
                finish();
            }
        });

        ColorPickerView colorPicker = findViewById(R.id.colorPicker);
        colorPicker.subscribe(new ColorObserver() {
            @Override
            public void onColor(int clr, boolean fromUser, boolean shouldPropagate) {
                color = clr;
                changeColorAll(color);
                handler.removeCallbacks(postNewColor);
                handler.postDelayed(postNewColor, 250);
            }
        });

        SeekBar slider = findViewById(R.id.brightness_slider);
        slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (colorChanged) {
                    colorChanged = false;
                    return;
                }
                brightness = seekBar.getProgress();
                handler.removeCallbacks(postNewColor);
                handler.postDelayed(postNewColor, 250);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final Switch aSwitch = findViewById(R.id.cl_switch);
        final boolean[] checkedChange = {false};
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                light.child("on").setValue(b);
                aSwitch.setChecked(b);
            }
        });

        final EditText editText = findViewById(R.id.cl_lightName);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                System.out.println("beforeTextChanged");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                System.out.println("onTextChanged");
                title = String.valueOf(editText.getText());
                handler.removeCallbacks(setNewTitle);
                handler.postDelayed(setNewTitle, 5000);
            }

            @Override
            public void afterTextChanged(Editable editable) {
//                light.child("title").setValue(editText.getText());
            }
        });

    }

    private void changeColorAll(int color) {
        changeToolbarColor(color);
        changeSliderColor(color);
    }

    private void changeToolbarColor(int color) {
        System.out.println("changeToolbarColor");
        GradientDrawable gradient = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[] {color, color});
        toolbar.setBackground(gradient);
    }

    private void changeSliderColor(int color) {
        System.out.println("changeSliderColor");
        GradientDrawable gradient = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[] {Color.GRAY, color, Color.WHITE});
        seekBar.setProgressDrawable(gradient);
    }

    private void changeTitle(String title) {
        System.out.println("changeTitle");
        EditText editText = findViewById(R.id.cl_lightName);
        editText.setText(title);
    }

    private void changeSwitch(boolean state) {
        System.out.println("changeSwitch");
        Switch aSwitch = findViewById(R.id.cl_switch);
        aSwitch.setChecked(state);

        if (!state) {
            seekBar.getThumb().setAlpha(0);
            seekBar.getBackground().setAlpha(0);
            GradientDrawable gradient = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[] {Color.GRAY, Color.GRAY});
            seekBar.setProgressDrawable(gradient);
            changeToolbarColor(Color.parseColor("#4c4c4c"));
        } else {
            seekBar.getThumb().setAlpha(255);
            changeSliderColor(color);
            changeToolbarColor(color);
        }
    }

    private void changeColorPickerColor(int color) {
        System.out.println("changeColorPickerColor " + color);
        ColorPickerView colorPicker = findViewById(R.id.colorPicker);

        colorPicker.setInitialColor(color);
        colorPicker.reset();
    }

    private void changeBrightnessSlider(int val) {
        SeekBar slider = findViewById(R.id.brightness_slider);
        slider.setProgress(val);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        LightsView lv = snapshot.getValue(LightsView.class);
        colorChanged = true;
        color = lv.getColor();
        brightness = lv.getBrightness();

        changeToolbarColor(color);
        changeSliderColor(color);
        changeColorPickerColor(color);

        changeTitle(lv.getTitle());

        changeBrightnessSlider(brightness);

        changeSwitch(lv.isOn());

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}