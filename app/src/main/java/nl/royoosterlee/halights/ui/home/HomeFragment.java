package nl.royoosterlee.halights.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import nl.royoosterlee.halights.Change_Light;
import nl.royoosterlee.halights.LightsClickListener;
import nl.royoosterlee.halights.LightsView;
import nl.royoosterlee.halights.LightsViewAdapter;
import nl.royoosterlee.halights.R;

import static android.graphics.Color.argb;

public class HomeFragment extends Fragment implements LightsClickListener, ValueEventListener {

    LightsViewAdapter lightsViewAdapter;
    List<LightsView> lightsViewList = new ArrayList<>();
    Switch lightSwitch;

    DatabaseReference ikpmd;

    RecyclerView lightsView;

    @SuppressLint("ResourceType")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        db = FirebaseDatabase.getInstance().getReference();
//        ikpmd = db.child("ikpmd");
        FirebaseApp fbApp = FirebaseApp.getInstance("halights");
        FirebaseDatabase database = FirebaseDatabase.getInstance(fbApp);
        ikpmd = database.getReference("ikpmd");
        ikpmd.addValueEventListener(this);

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        final RecyclerView recyclerView = root.findViewById(R.id.lights_recycler_view);
        lightSwitch = root.findViewById(R.id.recycler_item_switch);

        setupLightsView(recyclerView);

//        lightSwitch.getThumbDrawable().setColorFilter(ContextCompat.getColor(requireActivity(), Color.parseColor("#3F51B5")), PorterDuff.Mode.MULTIPLY);

        return root;
    }

    private void setupLightsView(RecyclerView lightsView) {
        lightsViewAdapter = new LightsViewAdapter(lightsViewList, getContext());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        System.out.println(lightsView);
        System.out.println(lightsView);
        lightsView.setLayoutManager(layoutManager);
        lightsView.setItemAnimator(new DefaultItemAnimator());
        lightsView.setAdapter(lightsViewAdapter);
        lightsViewAdapter.setLightsClickListener(this);

        this.lightsView = lightsView;
    }

    @Override
    public void onClick(View view, int position) {
        System.out.println("[lights] onClick!!!");
//        String title = lightsViewList.get(position).getTitle();
        LightsView listItem = lightsViewList.get(position);
//        mText.setText(listItem.getTitle());

        Intent intent = new Intent(getContext(), Change_Light.class);
        JSONObject obj = new JSONObject();
        try {
            obj.put("id", listItem.getId());
            obj.put("position", position);
            obj.put("name", listItem.getTitle());
            obj.put("subtitle", listItem.getSubtitle());
            obj.put("color", listItem.getColor());
            obj.put("on", listItem.isOn());
            obj.put("brightness", listItem.getBrightness());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("()()()()()()()()()<<");
        System.out.println(listItem.getBrightness());
        System.out.println(obj.toString());
        intent.putExtra("light", obj.toString());
        startActivity(intent);
    }

    @Override
    public void onSwitch(View compoundButton, int position) {
        System.out.println("[lights] onSwitch1!!");
        int lightColor = lightsViewList.get(position).getColor();
//        mText.setText(title);
        Switch aSwitch = (Switch)compoundButton;
        boolean checked = aSwitch.isChecked();

        View view = (View) compoundButton.getParent();

        System.out.println(view);

        System.out.println(String.format("KEY:: %s", ikpmd.getKey()));

        if (checked) {
            aSwitch.getTrackDrawable().setColorFilter(Color.parseColor("#373737"), PorterDuff.Mode.LIGHTEN);
            GradientDrawable gradient = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[] {lightColor, lightColor});
            gradient.setCornerRadius(40);
            view.setBackground(gradient);
        } else {
            aSwitch.getTrackDrawable().setColorFilter(Color.parseColor("#2a2a2a"), PorterDuff.Mode.DARKEN);
            GradientDrawable gradient = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[] {Color.parseColor("#424242"), Color.parseColor("#424242")});
            gradient.setCornerRadius(40);
            view.setBackground(gradient);
        }

        int id = lightsViewList.get(position).getId();
        ikpmd.child(String.valueOf(id)).child("on").setValue(checked);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        Iterable<DataSnapshot> it = snapshot.getChildren();

        lightsViewList.clear();

        int lvSize = lightsViewList.size();
        for (DataSnapshot lv : it) {
            int index = Integer.parseInt(lv.getKey());
            System.out.println("ITERABLE VALUE");
            if (index > lvSize-1) {
                lightsViewList.add(lv.getValue(LightsView.class));
            } else {
                lightsViewList.set(index, lv.getValue(LightsView.class));
            }

//            System.out.println(lv.getValue(LightsView.class));
        }

        lightsViewAdapter.notifyDataSetChanged();




        System.out.println("VALUEUEEEE");
        System.out.println(lightsView);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        System.out.println("Failed to read value");
        System.out.println(error.toException());
    }
}