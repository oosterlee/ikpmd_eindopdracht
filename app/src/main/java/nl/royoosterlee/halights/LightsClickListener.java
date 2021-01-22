package nl.royoosterlee.halights;

import android.view.View;

public interface LightsClickListener {
    void onClick(View view, int position);
    void onSwitch(View compoundButton, int position);
}
