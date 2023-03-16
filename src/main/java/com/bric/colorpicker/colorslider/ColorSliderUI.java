/*
 * @(#)ColorPickerSliderUI.java
 *
 * $Date$
 *
 * Copyright (c) 2011 by Jeremy Wood.
 * All rights reserved.
 *
 * The copyright of this software is owned by Jeremy Wood.
 * You may not use, copy or modify this software, except in
 * accordance with the license agreement you entered into with
 * Jeremy Wood. For details see accompanying license terms.
 *
 * This software is probably, but not necessarily, discussed here:
 * https://javagraphics.java.net/
 *
 * That site should also contain the most recent official version
 * of this software.  (See the SVN repository for more details.)
 */
package com.bric.colorpicker.colorslider;

import com.bric.colorpicker.ColorPicker;
import com.bric.colorpicker.ColorPickerMode;
import com.bric.colorpicker.ColorPickerPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.TexturePaint;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import javax.swing.JSlider;
import javax.swing.event.MouseInputAdapter;
import javax.swing.plaf.basic.BasicSliderUI;
import lombok.NonNull;

/**
 * This is a SliderUI designed specifically for the {@code ColorPicker}.
 */
public class ColorSliderUI extends BasicSliderUI {

    private final ColorPicker colorPicker;

    /**
     * Half of the height of the arrow
     */
    private static final int ARROW_HALF = 8;

    private final int[] intArray = new int[Toolkit.getDefaultToolkit().getScreenSize().height];

    private final BufferedImage image =
        new BufferedImage(1, intArray.length, BufferedImage.TYPE_INT_RGB);

    /**
     * This overrides the default behavior for this color slider and sets the thumb to where the
     * user clicked. From a design standpoint, users probably don't want to scroll through several
     * colors to get where they clicked: they simply want the color they selected.
     */
    private MouseInputAdapter mouseInputAdapter;

    public ColorSliderUI(@NonNull JSlider slider, @NonNull ColorPicker colorPicker) {
        super(slider);
        this.colorPicker = colorPicker;
        ColorPickerPanel colorPanel = colorPicker.getColorPanel();
        colorPanel.addComponentListener(new ResizeListener());
    }

    @Override
    public void paintThumb(Graphics g) {
        int y = thumbRect.y + thumbRect.height / 2;
        Polygon polygon = new Polygon();
        polygon.addPoint(0, y - ARROW_HALF);
        polygon.addPoint(ARROW_HALF, y);
        polygon.addPoint(0, y + ARROW_HALF);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.black);
        g2.fill(polygon);
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(1));
        g2.draw(polygon);
    }

    @Override
    protected void calculateThumbSize() {
        super.calculateThumbSize();
        thumbRect.height += 4;
        thumbRect.y -= 2;
    }

    @Override
    protected void calculateTrackRect() {
        super.calculateTrackRect();
        ColorPickerPanel cp = colorPicker.getColorPanel();
        int size = Math.min(ColorPickerPanel.MAX_SIZE, Math.min(cp.getWidth(), cp.getHeight()));
        int max = slider.getHeight() - ARROW_HALF * 2 - 2;
        if (size > max) {
            size = max;
        }
        trackRect.y = slider.getHeight() / 2 - size / 2;
        trackRect.height = size;
    }

    @Override
    public synchronized void paintTrack(Graphics g) {
        ColorPickerMode mode = colorPicker.getMode();
        if (mode == ColorPickerMode.HUE
            || mode == ColorPickerMode.BRIGHTNESS
            || mode == ColorPickerMode.SATURATION) {
            float[] hsb = colorPicker.getHSB();
            if (mode == ColorPickerMode.HUE) {
                for (int y = 0; y < trackRect.height; y++) {
                    float hue = (float) y / trackRect.height;
                    intArray[y] = Color.HSBtoRGB(hue, 1, 1);
                }
            } else if (mode == ColorPickerMode.SATURATION) {
                for (int y = 0; y < trackRect.height; y++) {
                    float sat = 1 - (float) y / trackRect.height;
                    intArray[y] = Color.HSBtoRGB(hsb[0], sat, hsb[2]);
                }
            } else {
                for (int y = 0; y < trackRect.height; y++) {
                    float bri = 1 - (float) y / trackRect.height;
                    intArray[y] = Color.HSBtoRGB(hsb[0], hsb[1], bri);
                }
            }
        } else {
            int[] rgb = colorPicker.getRGB();
            if (mode == ColorPickerMode.RED) {
                for (int y = 0; y < trackRect.height; y++) {
                    int red = 255 - (int) (y * 255 / trackRect.height + 0.49);
                    intArray[y] = (red << 16) + (rgb[1] << 8) + rgb[2];
                }
            } else if (mode == ColorPickerMode.GREEN) {
                for (int y = 0; y < trackRect.height; y++) {
                    int green = 255 - (int) (y * 255 / trackRect.height + 0.49);
                    intArray[y] = (rgb[0] << 16) + (green << 8) + rgb[2];
                }
            } else if (mode == ColorPickerMode.BLUE) {
                for (int y = 0; y < trackRect.height; y++) {
                    int blue = 255 - (int) (y * 255 / trackRect.height + 0.49);
                    intArray[y] = (rgb[0] << 16) + (rgb[1] << 8) + blue;
                }
            }
        }
        Graphics2D g2 = (Graphics2D) g;
        Rectangle r = new Rectangle(6, trackRect.y, 14, trackRect.height);

        Painter painter = new Painter();
        painter.setGraphics2D(g2);

        if (slider.hasFocus()) {
            painter.paintFocus(r, 3);
        }

        WritableRaster raster = image.getRaster();
        raster.setDataElements(0, 0, 1, trackRect.height, intArray);
        Paint p = new TexturePaint(image, new Rectangle(0, trackRect.y, 1, image.getHeight()));
        g2.setPaint(p);
        g2.fillRect(r.x, r.y, r.width, r.height);

        painter.drawBevel(r);
    }

    @Override
    public void paintFocus(Graphics g) {
    }

    @Override
    protected void installListeners(JSlider slider) {
        super.installListeners(slider);
        slider.removeMouseListener(trackListener);
        slider.removeMouseMotionListener(trackListener);
        if (mouseInputAdapter == null) {
            mouseInputAdapter = new SliderMouseListener(this, slider);
            slider.addMouseListener(mouseInputAdapter);
            slider.addMouseMotionListener(mouseInputAdapter);
        }
        slider.setOpaque(false);
    }

    @Override
    protected void uninstallListeners(JSlider slider) {
        super.uninstallListeners(slider);
        if (mouseInputAdapter != null) {
            slider.removeMouseListener(mouseInputAdapter);
            slider.removeMouseMotionListener(mouseInputAdapter);
        }
    }


    private class ResizeListener extends ComponentAdapter {

        ResizeListener() {
        }

        @Override
        public void componentResized(ComponentEvent e) {
            if (focusRect != null) {
                calculateGeometry();
                slider.repaint();
            }
        }
    }

}
