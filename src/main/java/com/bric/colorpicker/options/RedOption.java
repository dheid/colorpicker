package com.bric.colorpicker.options;

import java.util.Locale;

import com.bric.colorpicker.ColorPickerMode;
import com.bric.colorpicker.models.ColorModel;

public class RedOption extends Option {

    public RedOption() {
        this(null);
    }

    public RedOption(Locale locale) {
        super("redLabel", ColorPickerMode.RED, locale);
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
