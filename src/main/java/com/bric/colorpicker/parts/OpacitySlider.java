package com.bric.colorpicker.parts;

import com.bric.colorpicker.listeners.ColorListener;
import com.bric.colorpicker.listeners.ColorListenerWrapper;
import com.bric.colorpicker.models.ColorModel;
import javax.swing.JSlider;

public class OpacitySlider extends JSlider implements ColorListener {

    private final ColorListenerWrapper colorListenerWrapper;

    public OpacitySlider() {
        super(0, 255, 255);
        colorListenerWrapper = ColorListenerWrapper.withListener(colorModel -> setValue(colorModel.getAlpha()));
    }

    @Override
    public void colorChanged(ColorModel colorModel) {
        colorListenerWrapper.colorChanged(colorModel);
    }

    public void aboutToChangeColor() {
        colorListenerWrapper.aboutToChangeValue();
    }
}

