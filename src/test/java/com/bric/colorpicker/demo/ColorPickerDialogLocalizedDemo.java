package com.bric.colorpicker.demo;

import com.bric.colorpicker.ColorPickerDialog;

import java.awt.Color;
import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;

public class ColorPickerDialogLocalizedDemo {

    public static void main(String[] args) throws InvocationTargetException, InterruptedException {
        EventQueue.invokeAndWait(() -> {
            ColorPickerDialog.showDialog(null, Color.GREEN, Locale.FRENCH);
        });
    }

}