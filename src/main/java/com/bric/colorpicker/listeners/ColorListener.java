package com.bric.colorpicker.listeners;

import com.bric.colorpicker.models.ColorModel;

@FunctionalInterface
public interface ColorListener {

    void colorChanged(ColorModel colorModel);

}
