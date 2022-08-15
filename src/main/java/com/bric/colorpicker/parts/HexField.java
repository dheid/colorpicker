package com.bric.colorpicker.parts;

import com.bric.colorpicker.listeners.ColorListener;
import com.bric.colorpicker.listeners.ColorListenerWrapper;
import com.bric.colorpicker.models.ColorModel;
import javax.swing.JTextField;

public class HexField extends JTextField implements ColorListener {

    private final ColorListenerWrapper colorListenerWrapper;

    public HexField() {
        super(5);
        colorListenerWrapper = ColorListenerWrapper.withListener(colorModel -> {
            int r = colorModel.getRed();
            int g = colorModel.getGreen();
            int b = colorModel.getBlue();
            setText(String.format("%02x%02x%02x", r, g, b));
        });
    }

    @Override
    public void colorChanged(ColorModel colorModel) {
        colorListenerWrapper.colorChanged(colorModel);
    }

    public void aboutToChangeColor() {
        colorListenerWrapper.aboutToChangeValue();
    }
}
