package com.bric.colorpicker.demo;

import com.bric.colorpicker.ColorPicker;

import javax.swing.JCheckBox;

public class RgbCheckBox extends JCheckBox implements PickerModificator {

    public RgbCheckBox() {
        setText("Include RGB Values");
    }

    @Override
    public void setPicker(ColorPicker picker) {
        addActionListener(e -> picker.setRGBControlsVisible(isSelected()));
    }
}
