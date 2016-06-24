package com.bric.colorpicker.parts;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import java.awt.event.ActionEvent;

/**
 * This action calls {@code button.doClick()}.
 */
public class ClickAction extends AbstractAction {

    private final JButton button;

    public ClickAction(JButton button) {
        this.button = button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        button.doClick();
    }
}
