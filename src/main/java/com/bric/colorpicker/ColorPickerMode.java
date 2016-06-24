package com.bric.colorpicker;

public enum ColorPickerMode {
    HUE(360), BRIGHTNESS(100), SATURATION(100), RED(255), GREEN(255), BLUE(255), ALPHA(255);

    private int max;

    ColorPickerMode(int max) {
        this.max = max;
    }

    public int getMax() {
        return max;
    }
}
