package nl.royoosterlee.halights.ui.home;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import nl.royoosterlee.halights.LightsClickListener;
import nl.royoosterlee.halights.LightsView;
import nl.royoosterlee.halights.LightsViewAdapter;
import nl.royoosterlee.halights.MainActivity;
import nl.royoosterlee.halights.R;

public class HomeViewModel extends ViewModel {

    public HomeViewModel() {
        System.out.println("HomeViewModel created!");
    }
}