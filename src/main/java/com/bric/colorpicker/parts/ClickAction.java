package com.bric.colorpicker.parts;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import lombok.RequiredArgsConstructor;

/**
 * This action calls {@code button.doClick()}.
 */
@RequiredArgsConstructor
public class ClickAction extends AbstractAction {

    private final JButton button;

    @Override
    public void actionPerformed(ActionEvent e) {
        button.doClick();
    }
}
