package com.bric.colorpicker;

import javax.swing.JFrame;
import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;

public class ColorPickerExample {

    public static void main(String[] args) throws InvocationTargetException, InterruptedException {
        EventQueue.invokeAndWait(() -> {
            JFrame frame = new JFrame("ColorPicker Example");
            frame.setSize(600, 400);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            ColorPicker colorPicker = new ColorPicker(true, true);
            colorPicker.addColorListener(colorModel -> System.out.println(colorModel.getColor()));
            frame.add(colorPicker);

            frame.setVisible(true);
        });
    }

}
