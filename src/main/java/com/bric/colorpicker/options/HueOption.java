package com.bric.colorpicker.options;

import com.bric.colorpicker.ColorPickerMode;
import com.bric.colorpicker.models.ColorModel;

public class HueOption extends Option {

    public HueOption() {
        super(STRINGS.getObject("hueLabel").toString(), ColorPickerMode.HUE);
    }

    @Override
    public void doColorChanged(ColorModel colorModel) {
        setValue(Math.round(colorModel.getHue() * getMax()));
    }

    @Override
    public void update(ColorModel colorModel) {
        colorModel.setHue(getValue() / (float) getMax());
    }

}
