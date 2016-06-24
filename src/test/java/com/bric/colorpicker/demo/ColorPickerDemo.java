package com.bric.colorpicker.demo;

import com.bric.colorpicker.ColorPicker;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.lang.reflect.InvocationTargetException;

public class ColorPickerDemo {

    public static void main(final String... args) throws InvocationTargetException, InterruptedException {

        EventQueue.invokeAndWait(() -> {
            DemoPicker picker = new DemoPicker();
            picker.setPreferredSize(new Dimension(400,400));

            ColorPickerFrame colorPickerFrame = new ColorPickerFrame(picker);

            PaletteWindow paletteWindow = new PaletteWindow(colorPickerFrame);
            paletteWindow.setPicker(picker);

            picker.addPropertyChangeListener(ColorPicker.MODE_PROPERTY, evt -> {
                paletteWindow.switchMode(picker.getMode());
            });

            colorPickerFrame.addComponentListener(new WindowAttachingListener(colorPickerFrame, paletteWindow));

            colorPickerFrame.setVisible(true);
            paletteWindow.setVisible(true);
        });

    }

    private static class WindowAttachingListener extends ComponentAdapter {
        private final ColorPickerFrame colorPickerFrame;
        private final PaletteWindow paletteWindow;

        public WindowAttachingListener(ColorPickerFrame colorPickerFrame, PaletteWindow paletteWindow) {
            this.colorPickerFrame = colorPickerFrame;
            this.paletteWindow = paletteWindow;
        }

        @Override
        public void componentMoved(ComponentEvent e) {
            Point p = colorPickerFrame.getLocation();
            paletteWindow.attachTo(p);
        }
    }
}
