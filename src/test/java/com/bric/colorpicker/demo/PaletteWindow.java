package com.bric.colorpicker.demo;

import com.bric.colorpicker.ColorPicker;
import com.bric.colorpicker.ColorPickerMode;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.AbstractButton;
import javax.swing.JWindow;


public class PaletteWindow extends JWindow {

    public static final int HORIZONTAL_DISTANCE = 10;
    private final ModeComboBox comboBox = new ModeComboBox();
    private final Collection<PickerModificator> pickerModificators = new ArrayList<>(1);

    public void setPicker(ColorPicker picker) {
        comboBox.setPicker(picker);
        for (PickerModificator pickerModificator : pickerModificators) {
            pickerModificator.setPicker(picker);
        }
    }

    public PaletteWindow(Window owner) {
        super(owner);

        setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 0;
        c.insets = new Insets(5, 5, 5, 5);
        c.anchor = GridBagConstraints.WEST;
        add(comboBox, c);

        ActionListener packListener = e -> pack();

        add(new AlphaCheckBox(), c, packListener);
        add(new HsbCheckBox(), c, packListener);
        add(new RgbCheckBox(), c, packListener);
        add(new ModeControlsCheckBox().selected(), c, packListener);

        pack();
        setLocationRelativeTo(null);
    }

    private void add(AbstractButton button, GridBagConstraints c, ActionListener listener) {
        c.gridy++;
        button.addActionListener(listener);
        add(button, c);
        if (button instanceof PickerModificator) {
            pickerModificators.add((PickerModificator) button);
        }
    }

    public void switchMode(ColorPickerMode mode) {
        comboBox.setMode(mode);
    }

    public void setVisible(boolean visible) {
        super.setVisible(visible);
    }

    public void attachTo(Point p) {
        int newX = p.x - getWidth() - HORIZONTAL_DISTANCE;
        int newY = p.y;
        setLocation(newX, newY);
    }
}
