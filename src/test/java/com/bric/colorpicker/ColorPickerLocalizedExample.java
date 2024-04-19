package com.bric.colorpicker;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.Color;
import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;

public class ColorPickerLocalizedExample {

    public static void main(String[] args) throws InvocationTargetException, InterruptedException {
        EventQueue.invokeAndWait(() -> {
            JFrame frame = new JFrame("ColorPicker Example");
            frame.setSize(600, 400);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            ColorPicker colorPicker = new ColorPicker(true, true, Locale.FRENCH);
            colorPicker.setColor(Color.BLUE);
            colorPicker.addColorListener(colorModel -> System.out.println(colorModel.getColor()));
            frame.add(colorPicker);

            frame.setVisible(true);
        });
    }

}
