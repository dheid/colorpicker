package com.bric.colorpicker.demo;

import com.bric.colorpicker.ColorPicker;
import com.bric.colorpicker.ColorPickerMode;

import javax.swing.JComboBox;

public class ModeComboBox extends JComboBox<String> {
    
    public ModeComboBox() {
        addItem("Hue");
        addItem("Saturation");
        addItem("Brightness");
        addItem("Red");
        addItem("Green");
        addItem("Blue");

        setSelectedIndex(2);
    }

    public void setPicker(ColorPicker picker) {
        addActionListener(e -> {
            picker.setMode(getMode());
        });
    }
    
    public ColorPickerMode getMode() {
        int selectedIndex = getSelectedIndex();
        if (selectedIndex == 0) {
            return ColorPickerMode.HUE;
        } else if (selectedIndex == 1) {
            return ColorPickerMode.SATURATION;
        } else if (selectedIndex == 2) {
            return ColorPickerMode.BRIGHTNESS;
        } else if (selectedIndex == 3) {
            return ColorPickerMode.RED;
        } else if (selectedIndex == 4) {
            return ColorPickerMode.GREEN;
        } else if (selectedIndex == 5) {
            return ColorPickerMode.BLUE;
        } else {
            throw new IllegalStateException("Selected index " + selectedIndex + " not supported");
        }
    }

    public void setMode(ColorPickerMode mode) {
        if(mode.equals(ColorPickerMode.HUE)) {
            setSelectedIndex(0);
        } else if(mode.equals(ColorPickerMode.SATURATION)) {
            setSelectedIndex(1);
        } else if(mode.equals(ColorPickerMode.BRIGHTNESS)) {
            setSelectedIndex(2);
        } else if(mode.equals(ColorPickerMode.RED)) {
            setSelectedIndex(3);
        } else if(mode.equals(ColorPickerMode.GREEN)) {
            setSelectedIndex(4);
        } else if(mode.equals(ColorPickerMode.BLUE)) {
            setSelectedIndex(5);
        }
    }
}
