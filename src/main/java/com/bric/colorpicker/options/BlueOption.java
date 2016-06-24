package com.bric.colorpicker.options;

import com.bric.colorpicker.ColorPickerMode;
import com.bric.colorpicker.models.ColorModel;

public class BlueOption extends Option {

    public BlueOption() {
        super(STRINGS.getObject("blueLabel").toString(), ColorPickerMode.BLUE);
    }

    @Override
    public void doColorChanged(ColorModel colorModel) {
        setValue(colorModel.getBlue());
    }

    @Override
    public void update(ColorModel colorModel) {
        colorModel.setBlue(getValue());
    }

}
