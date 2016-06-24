package com.bric.colorpicker.demo;

import com.bric.colorpicker.ColorPicker;

import javax.swing.AbstractButton;
import javax.swing.JCheckBox;

public class ModeControlsCheckBox extends JCheckBox implements PickerModificator {

    public ModeControlsCheckBox() {
        setText("Include Mode Controls");
    }

    @Override
    public void setPicker(ColorPicker picker) {
        addActionListener(e -> picker.setModeControlsVisible(isSelected()));
    }

    public AbstractButton selected() {
        setSelected(true);
        return this;
    }
}
