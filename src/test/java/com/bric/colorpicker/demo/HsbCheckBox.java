package com.bric.colorpicker.demo;

import com.bric.colorpicker.ColorPicker;

import javax.swing.JCheckBox;

public class HsbCheckBox extends JCheckBox implements PickerModificator {

    public HsbCheckBox() {
        setText("Include HSB Values");
    }

    @Override
    public void setPicker(ColorPicker picker) {
        addActionListener(e -> picker.setHSBControlsVisible(isSelected()));
    }
}
