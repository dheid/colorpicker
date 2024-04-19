package com.bric.colorpicker.options;

import java.util.Locale;

import com.bric.colorpicker.ColorPickerMode;
import com.bric.colorpicker.models.ColorModel;

public class HueOption extends Option {

    public HueOption() {
        this(null);
    }

    public HueOption(Locale locale) {
        super("hueLabel", ColorPickerMode.HUE, locale);
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
