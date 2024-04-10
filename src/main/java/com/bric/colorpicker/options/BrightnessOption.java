package com.bric.colorpicker.options;

import java.util.Locale;

import com.bric.colorpicker.ColorPickerMode;
import com.bric.colorpicker.models.ColorModel;

public class BrightnessOption extends Option {

    public BrightnessOption() {
        this(null);
    }

    public BrightnessOption(Locale locale) {
        super("brightnessLabel", ColorPickerMode.BRIGHTNESS, locale);
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
