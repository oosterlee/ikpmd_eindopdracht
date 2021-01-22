package nl.royoosterlee.halights;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;

public class LightsView {
    private Icon icon;
    private String title, subtitle;
    private boolean on;
    private int id;
    private int color;
    private int brightness;

    public LightsView(String title, String subtitle, boolean on, int id, Icon icon) {
        this.title = title;
        this.subtitle = subtitle;
        this.on = on;
        this.icon = icon;
        this.id = id;
        this.color = Color.parseColor("#F6CD8B");
    }

    public LightsView(String title, String subtitle, boolean on, int id) {
        this.title = title;
        this.subtitle = subtitle;
        this.on = on;
        this.icon = null;
        this.id = id;
        this.color = Color.parseColor("#F6CD8B");
    }

    public LightsView(String title, String subtitle, boolean on, int id, int color) {
        this.title = title;
        this.subtitle = subtitle;
        this.on = on;
        this.id = id;
        this.color = color;
    }

    public LightsView() {
        System.out.println("[LightsView] no argument constructor!");
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBrightness() {
        return brightness;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }
}
