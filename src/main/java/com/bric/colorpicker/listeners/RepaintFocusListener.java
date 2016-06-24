package com.bric.colorpicker.listeners;

import com.bric.colorpicker.ColorPickerPanel;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class RepaintFocusListener implements FocusListener {
    private final Component component;

    public RepaintFocusListener(ColorPickerPanel component) {
        this.component = component;
    }

    @Override
    public void focusGained(FocusEvent e) {
        component.repaint();
    }

    @Override
    public void focusLost(FocusEvent e) {
        component.repaint();
    }
}
