package com.bric.colorpicker.demo;

import com.bric.colorpicker.ColorPicker;

public class DemoPicker extends ColorPicker {

    public DemoPicker() {
        super(true, false);

        setOpacityVisible(false);
        setHSBControlsVisible(false);
        setRGBControlsVisible(false);
        setHexControlsVisible(false);
        setPreviewSwatchVisible(false);
    }
}
