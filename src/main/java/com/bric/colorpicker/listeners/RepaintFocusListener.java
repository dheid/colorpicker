package com.bric.colorpicker.listeners;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RepaintFocusListener implements FocusListener {
    private final Component component;

    @Override
    public void focusGained(FocusEvent e) {
        component.repaint();
    }

    @Override
    public void focusLost(FocusEvent e) {
        component.repaint();
    }
}
