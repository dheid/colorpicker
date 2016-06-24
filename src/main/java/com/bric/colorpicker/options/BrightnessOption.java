package com.bric.colorpicker.options;

import com.bric.colorpicker.ColorPickerMode;
import com.bric.colorpicker.models.ColorModel;

public class BrightnessOption extends Option {

    public BrightnessOption() {
        super(STRINGS.getObject("brightnessLabel").toString(), ColorPickerMode.BRIGHTNESS);
    }

    @Override
    public void doColorChanged(ColorModel colorModel) {
        setValue(Math.round(colorModel.getBrightness() * getMax()));
    }


    @Override
    public void update(ColorModel colorModel) {
        colorModel.setBrightness(getValue() / (float) getMax());
    }

}
