package com.bric.colorpicker.models;

import com.bric.colorpicker.listeners.ColorListener;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;

public class ColorModel {

    private final Collection<ColorListener> listeners = new ArrayList<>(6);

    private Color color = Color.GREEN;
    private boolean changing;

    public void addListener(ColorListener changeListener) {
        listeners.add(changeListener);
    }

    private void fireColorChanged() {
        changing = true;
        for (ColorListener listener : listeners) {
            listener.colorChanged(this);
        }
        changing = false;
    }

    public boolean isChanging() {
        return changing;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        if (changing) {
            return;
        }
        this.color = color;
        fireColorChanged();
    }

    public float getHue() {
        return getHSB()[0];
    }

    public void setHue(float hue) {
        if (changing) {
            return;
        }
        float[] hsb = getHSB();
        color = Color.getHSBColor(hue, hsb[1], hsb[2]);
        fireColorChanged();
    }

    public float getSaturation() {
        return getHSB()[1];
    }

    public void setSaturation(float saturation) {
        if (changing) {
            return;
        }
        float[] hsb = getHSB();
        color = Color.getHSBColor(hsb[0], saturation, hsb[2]);
        fireColorChanged();
    }

    public float getBrightness() {
        return getHSB()[2];
    }

    public void setBrightness(float brightness) {
        if (changing) {
            return;
        }
        float[] hsb = getHSB();
        color = Color.getHSBColor(hsb[0], hsb[1], brightness);
        fireColorChanged();
    }

    public int[] getRGB() {
        return new int[]{getRed(), getGreen(), getBlue()};
    }

    public float[] getHSB() {
        return Color.RGBtoHSB(getRed(), getGreen(), getBlue(), new float[3]);
    }

    public int getAlpha() {
        return color.getAlpha();
    }

    public void setAlpha(int alpha) {
        color = new Color(getRed(), getGreen(), getBlue(), alpha);
        fireColorChanged();
    }

    public int getBlue() {
        return color.getBlue();
    }

    public void setBlue(int blue) {
        if (changing) {
            return;
        }
        color = new Color(getRed(), getGreen(), blue);
        fireColorChanged();
    }

    public int getGreen() {
        return color.getGreen();
    }

    public void setGreen(int green) {
        if (changing) {
            return;
        }
        color = new Color(getRed(), green, getBlue());
        fireColorChanged();
    }

    public int getRed() {
        return color.getRed();
    }

    public void setRed(int red) {
        if (changing) {
            return;
        }
        color = new Color(red, getGreen(), getBlue());
        fireColorChanged();
    }

}
