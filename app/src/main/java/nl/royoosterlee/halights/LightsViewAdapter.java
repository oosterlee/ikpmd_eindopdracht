package nl.royoosterlee.halights;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LightsViewAdapter extends RecyclerView.Adapter<LightsViewAdapter.LightsViewHolder> {
    private List<LightsView> lightsViewList;
    private Context context;
    private LightsClickListener lightsClickListener;

    public LightsViewAdapter(List<LightsView> lightsViews, Context context) {
        this.lightsViewList = lightsViews;
        this.context = context;
    }

    @NonNull
    @Override
    public LightsViewAdapter.LightsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new LightsViewHolder(inflater.from(context).inflate(R.layout.recycler_view_item, parent, false));
    }

    @Override
    public void onBindViewHolder(LightsViewHolder holder, int position) {
        System.out.println("[onBindViewHolder]");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && lightsViewList.get(position).getIcon() != null) {
//            holder.icon.setImageIcon(lightsViewList.get(position).getIcon());
        } else {
//            holder.icon.setImageResource(R.drawable.ic_lightbulb_outline_black_24dp);
        }

        holder.title.setText(lightsViewList.get(position).getTitle());
        holder.subtitle.setText(lightsViewList.get(position).getSubtitle());
        holder.onoff.setChecked(lightsViewList.get(position).isOn());

        lightsClickListener.onSwitch(holder.onoff, position);
    }

    @Override
    public int getItemCount() {
        return lightsViewList.size();
    }

    public void setLightsClickListener(LightsClickListener lightsClickListener) {
        this.lightsClickListener = lightsClickListener;
    }

    public class LightsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
        ImageView icon;
        TextView title, subtitle;
        Switch onoff;

        public LightsViewHolder(View view) {
            super(view);
//            icon = view.findViewById(R.id.recycler_item_icon);
            title = view.findViewById(R.id.recycler_item_title);
            subtitle = view.findViewById(R.id.recycler_item_subtitle);
            onoff = view.findViewById(R.id.recycler_item_switch);
            view.setOnClickListener(this);
            onoff.setOnCheckedChangeListener(this);
        }

        @Override
        public void onClick(View view) {
            if (lightsClickListener != null) {
                lightsClickListener.onClick(view, getAdapterPosition());
            }
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (lightsClickListener != null) {
                lightsClickListener.onSwitch(compoundButton, getAdapterPosition());
            }
        }
    }
}
