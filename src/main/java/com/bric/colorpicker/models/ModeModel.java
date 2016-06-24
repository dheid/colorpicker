package com.bric.colorpicker.models;

import com.bric.colorpicker.ColorPickerMode;
import com.bric.colorpicker.listeners.ModeListener;

import java.util.ArrayList;
import java.util.Collection;

public class ModeModel {

    private final Collection<ModeListener> listeners = new ArrayList<>(3);
    private ColorPickerMode mode = ColorPickerMode.BRIGHTNESS;

    public boolean addListener(ModeListener modeListener) {
        return listeners.add(modeListener);
    }

    public ColorPickerMode getMode() {
        return this.mode;
    }

    public void setMode(ColorPickerMode mode) {
        this.mode = mode;
        for (ModeListener listener : listeners) {
            listener.modeChanged(this);
        }
    }
}
