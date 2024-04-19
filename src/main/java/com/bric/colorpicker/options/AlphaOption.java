package com.bric.colorpicker.options;

import com.bric.colorpicker.ColorPickerMode;
import com.bric.colorpicker.models.ColorModel;

import javax.swing.ButtonGroup;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.util.Locale;

public class AlphaOption extends Option {

    public AlphaOption() {
        this(null);
    }

    public AlphaOption(Locale locale) {
        super("alphaLabel", ColorPickerMode.ALPHA, locale);
    }

    @Override
    public void addTo(Container container, GridBagConstraints constraints, ButtonGroup buttonGroup) {
        constraints.gridy++;
        constraints.gridx = 0;
        constraints.anchor = GridBagConstraints.LINE_END;
        constraints.fill = GridBagConstraints.NONE;
        container.add(getLabel(), constraints);

        constraints.gridx++;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        container.add(getSpinner(), constraints);
    }

    @Override
    public void update(ColorModel colorModel) {
        colorModel.setAlpha(getValue());
    }

    @Override
    public void doColorChanged(ColorModel colorModel) {
        setValue(colorModel.getAlpha());
    }

}
