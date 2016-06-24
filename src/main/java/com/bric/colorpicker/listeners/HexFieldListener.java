package com.bric.colorpicker.listeners;

import com.bric.colorpicker.parts.HexField;
import com.bric.colorpicker.models.ColorModel;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.Color;

public class HexFieldListener implements DocumentListener {

    private ColorModel colorModel;

    private HexField hexField;

    public void setHexField(HexField hexField) {
        this.hexField = hexField;
    }

    public void setColorModel(ColorModel colorModel) {
        this.colorModel = colorModel;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        processUpdate(e);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        processUpdate(e);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        processUpdate(e);
    }

    private void processUpdate(DocumentEvent e) {
        if (colorModel.isChanging()) {
            return;
        }

        hexField.aboutToChangeColor();
        String text = hexField.getText();
        try {
            Color color = Color.decode('#' + text);
            colorModel.setColor(color);
        } catch (NumberFormatException ex) {
            // The number could not be parsed. We will be error prone here.
        }
    }
}
