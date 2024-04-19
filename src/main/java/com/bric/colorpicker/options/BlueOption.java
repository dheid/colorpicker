package com.bric.colorpicker.options;

import java.util.Locale;

import com.bric.colorpicker.ColorPickerMode;
import com.bric.colorpicker.models.ColorModel;

public class BlueOption extends Option {

    public BlueOption() {
        this(null);
    }

    public BlueOption(Locale locale) {
        super("blueLabel", ColorPickerMode.BLUE, locale);
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
