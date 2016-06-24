package com.bric.colorpicker.listeners;

import javax.swing.text.JTextComponent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class SelectAllListener extends FocusAdapter {
    @Override
    public void focusGained(FocusEvent e) {
        JTextComponent selectable = (JTextComponent) e.getSource();
        selectable.selectAll();
    }
}
