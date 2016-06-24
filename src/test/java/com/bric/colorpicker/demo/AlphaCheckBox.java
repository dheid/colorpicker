package com.bric.colorpicker.demo;

import com.bric.colorpicker.ColorPicker;

import javax.swing.JCheckBox;

public class AlphaCheckBox extends JCheckBox implements PickerModificator {

    public AlphaCheckBox() {
        setText("Include Alpha");
    }

    @Override
    public void setPicker(ColorPicker picker) {
        addActionListener(e -> picker.setOpacityVisible(isSelected()));
    }
}
