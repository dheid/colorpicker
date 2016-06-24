package com.bric.colorpicker.options;

import com.bric.colorpicker.ColorPickerMode;
import com.bric.colorpicker.models.ColorModel;

public class RedOption extends Option {

    public RedOption() {
        super(STRINGS.getObject("redLabel").toString(), ColorPickerMode.RED);
    }

    @Override
    public void doColorChanged(ColorModel colorModel) {
        setValue(colorModel.getRed());
    }

    @Override
    public void update(ColorModel colorModel) {
        colorModel.setRed(getValue());
    }

}
