package com.bric.colorpicker.options;

import java.util.Locale;

import com.bric.colorpicker.ColorPickerMode;
import com.bric.colorpicker.models.ColorModel;

public class GreenOption extends Option {

    public GreenOption() {
        this(null);
    }

    public GreenOption(Locale locale) {
        super("greenLabel", ColorPickerMode.GREEN, locale);
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
