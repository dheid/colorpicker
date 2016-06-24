package com.bric.colorpicker.options;

import com.bric.colorpicker.ColorPickerMode;
import com.bric.colorpicker.models.ColorModel;

public class GreenOption extends Option {

    public GreenOption() {
        super(STRINGS.getObject("greenLabel").toString(), ColorPickerMode.GREEN);
    }

    @Override
    public void doColorChanged(ColorModel colorModel) {
        setValue(colorModel.getGreen());
    }

    @Override
    public void update(ColorModel colorModel) {
        colorModel.setGreen(getValue());
    }

}
