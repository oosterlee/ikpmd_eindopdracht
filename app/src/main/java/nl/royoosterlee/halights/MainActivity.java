package nl.royoosterlee.halights;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Path;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "[halights]";

    private DatabaseReference ikpmd;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean networkConnected = isNetworkConnected();

        if (!networkConnected) {
            while (!networkConnected) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                networkConnected = isNetworkConnected();
                Toast.makeText(this, "You are not connected to the internet!", Toast.LENGTH_SHORT);
            }
            Toast.makeText(this, "Connected!", Toast.LENGTH_LONG);
        }


        FirebaseOptions fbOptions = new FirebaseOptions.Builder()
                .setProjectId("hsleiden-1610636860966")
                .setApplicationId("1:783277049426:web:c105bebf37b8b9f423418b")
                .setApiKey("AIzaSyA44FfPF5OdnYC2gkBLzoROiNtxBLp0QM8")
                .setDatabaseUrl("https://hsleiden-1610636860966-default-rtdb.europe-west1.firebasedatabase.app").build();

        FirebaseApp.initializeApp(this, fbOptions, "halights");

        Log.d(TAG, "onStart");

        FirebaseApp fbApp = FirebaseApp.getInstance("halights");
        FirebaseDatabase database = FirebaseDatabase.getInstance(fbApp);
        ikpmd = database.getReference("ikpmd");

        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home,
                R.id.navigation_dashboard,
                R.id.navigation_settings
        ).build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

//        setSupportActionBar();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    @Override
    public void onStart() {
        super.onStart();

        MaterialButton button = findViewById(R.id.addLightBtn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Clicked the add light button!");

                ikpmd.orderByChild("id").limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Iterable<DataSnapshot> it = snapshot.getChildren();

                        for (DataSnapshot lv : it) {
                            LightsView lightsView = lv.getValue(LightsView.class);
                            createNewLight(lightsView.getId() + 1);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }

    private void createNewLight(int id) {
        LightsView lightsView = new LightsView();
        lightsView.setId(id);
        lightsView.setBrightness(50);
        lightsView.setColor(Color.parseColor("#F6CD8B"));
        lightsView.setOn(false);
        lightsView.setTitle("Light" + id);
        lightsView.setSubtitle("ColorLight");

        ikpmd.child(String.valueOf(id)).setValue(lightsView);
    }

}
