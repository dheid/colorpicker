package com.bric.colorpicker.options;

import com.bric.colorpicker.ColorPickerMode;
import com.bric.colorpicker.models.ColorModel;

public class SaturationOption extends Option {

    public SaturationOption() {
        super(STRINGS.getObject("saturationLabel").toString(), ColorPickerMode.SATURATION);
    }

    @Override
    public void doColorChanged(ColorModel colorModel) {
        setValue(Math.round(colorModel.getSaturation() * getMax()));
    }

    @Override
    public void update(ColorModel colorModel) {
        colorModel.setSaturation(getValue() / (float) getMax());
    }

}
