package com.bric.colorpicker.options;

import java.util.Locale;

import com.bric.colorpicker.ColorPickerMode;
import com.bric.colorpicker.models.ColorModel;

public class SaturationOption extends Option {

    public SaturationOption() {
        this(null);
    }

    public SaturationOption(Locale locale) {
        super("saturationLabel", ColorPickerMode.SATURATION, locale);
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
