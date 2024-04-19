/*
 * @(#)ColorPickerPanel.java
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
package com.bric.colorpicker;

import static com.bric.colorpicker.ColorPickerMode.BLUE;
import static com.bric.colorpicker.ColorPickerMode.BRIGHTNESS;
import static com.bric.colorpicker.ColorPickerMode.GREEN;
import static com.bric.colorpicker.ColorPickerMode.HUE;
import static com.bric.colorpicker.ColorPickerMode.RED;
import static com.bric.colorpicker.ColorPickerMode.SATURATION;

import com.bric.colorpicker.colorslider.Painter;
import com.bric.colorpicker.listeners.ColorListener;
import com.bric.colorpicker.listeners.ColorListenerWrapper;
import com.bric.colorpicker.listeners.ModeListener;
import com.bric.colorpicker.listeners.RepaintFocusListener;
import com.bric.colorpicker.models.ColorModel;
import com.bric.colorpicker.models.ModeModel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import lombok.extern.slf4j.Slf4j;

/**
 * This is the large graphic element in the {@code ColorPicker}
 * that depicts a wide range of colors.
 * <P>This panel can operate in 6 different modes.  In each mode a different
 * property is held constant: hue, saturation, brightness, red, green, or blue.
 * (Each property is identified with a constant in the {@code ColorPicker} class,
 * such as: {@code ColorPicker.HUE} or {@code ColorPicker.GREEN}.)
 * <P>In saturation and brightness mode, a wheel is used.  Although it doesn't
 * use as many pixels as a square does: it is a very aesthetic models since the hue can
 * wrap around in a complete circle.  (Also, on top of looks, this is how most
 * people learn to think the color spectrum, so it has that advantage, too).
 * In all other modes a square is used.
 * <P>The user can click in this panel to select a new color.  The selected color is
 * highlighted with a circle drawn around it.  Also once this
 * target has the keyboard focus, the user can use the arrow keys to
 * traverse the available colors.
 * <P>Note this target is public and exists independently of the
 * {@code ColorPicker} class.  The only way this class is dependent
 * on the {@code ColorPicker} class is when the constants for the modes
 * are used.
 * <P>The graphic in this panel will be based on either the width or
 * the height of this target: depending on which is smaller.
 *
 * @see ColorPicker
 * @see ColorPickerDialog
 */
@Slf4j
public class ColorPickerPanel extends JPanel implements ColorListener, ModeListener {
    /**
     * The maximum size the graphic will be.  No matter
     * how big the panel becomes, the graphic will not exceed
     * this length.
     * <P>(This is enforced because only 1 BufferedImage is used
     * to render the graphic.  This image is created once at a fixed
     * size and is never replaced.)
     */
    public static final int MAX_SIZE = 325;

    /**
     * This controls how the colors are displayed.
     */
    private ColorPickerMode mode = BRIGHTNESS;

    /**
     * The point used to indicate the selected color.
     */
    private Point point = new Point(0, 0);

    private final Collection<ChangeListener> changeListeners = new ArrayList<>(1);

    /* Floats from [0,1].  They must be kept distinct, because
     * when you convert them to RGB coordinates HSB(0,0,0) and HSB (.5,0,0)
     * and then convert them back to HSB coordinates, the hue always shifts back to zero.
     */
    private float hue = -1;
    private float sat = -1;
    private float bri = -1;
    private int red = -1;
    private int green = -1;
    private int blue = -1;
    private final FocusListener focusListener = new RepaintFocusListener(this);
    private final BufferedImage image = new BufferedImage(MAX_SIZE, MAX_SIZE, BufferedImage.TYPE_INT_ARGB);
    private final Insets imagePadding = new Insets(6, 6, 6, 6);
    private final KeyListener keyListener = new ColorPickerKeyListener();
    /**
     * A row of pixel data we recycle every time we regenerate this image.
     */
    private final int[] row = new int[MAX_SIZE];
    private final MouseInputListener mouseListener = new ColorPickerMouseInputListener();
    private final ComponentListener componentListener = new ResizeListener();
    private final ColorListenerWrapper colorListenerWrapper;

    /**
     * Creates a new {@code ColorPickerPanel}
     */
    public ColorPickerPanel() {
        setMaximumSize(new Dimension(
            MAX_SIZE + imagePadding.left + imagePadding.right,
            MAX_SIZE + imagePadding.top + imagePadding.bottom));
        setPreferredSize(new Dimension((int) (MAX_SIZE * 0.75), (int) (MAX_SIZE * 0.75)));

        setRGB(0, 0, 0);
        addMouseListener(mouseListener);
        addMouseMotionListener(mouseListener);

        setFocusable(true);
        addKeyListener(keyListener);
        addFocusListener(focusListener);

        setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        addComponentListener(componentListener);

        colorListenerWrapper = ColorListenerWrapper.withListener(colorModel -> setRGB(colorModel.getRed(), colorModel.getGreen(), colorModel.getBlue()));

    }

    /**
     * This listener will be notified when the current HSB or RGB values
     * change.
     *
     * @param changeListener The change listener to add
     */
    public void addChangeListener(ChangeListener changeListener) {
        if (changeListeners.contains(changeListener)) {
            return;
        }
        changeListeners.add(changeListener);
    }

    /**
     * Remove a {@code ChangeListener} so it is no longer
     * notified when the selected color changes.
     *
     * @param changeListener The change listener to remove
     */
    public void removeChangeListener(ChangeListener changeListener) {
        changeListeners.remove(changeListener);
    }

    private void fireChangeListeners() {
        if (changeListeners == null) {
            return;
        }
        for (ChangeListener l : changeListeners) {
            try {
                l.stateChanged(new ChangeEvent(this));
            } catch (RuntimeException e) {
                log.error("Change listener threw exception", e);
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2 = (Graphics2D) g;
        int size = calculateSize();

        g2.translate(getWidth() / 2 - size / 2, getHeight() / 2 - size / 2);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Shape shape;

        if (SATURATION == mode || BRIGHTNESS == mode) {
            shape = new Ellipse2D.Float(0, 0, size, size);
        } else {
            shape = new Rectangle(0, 0, size, size);
        }

        Painter painter = new Painter();
        painter.setGraphics2D(g2);

        if (hasFocus()) {
            painter.paintFocus(shape, 3);
        }

        if (!(shape instanceof Rectangle)) {
            //paint a circular shadow
            g2.translate(2, 2);
            g2.setColor(new Color(0, 0, 0, 20));
            g2.fill(new Ellipse2D.Float(-2, -2, size + 4, size + 4));
            g2.setColor(new Color(0, 0, 0, 40));
            g2.fill(new Ellipse2D.Float(-1, -1, size + 2, size + 2));
            g2.setColor(new Color(0, 0, 0, 80));
            g2.fill(new Ellipse2D.Float(0, 0, size, size));
            g2.translate(-2, -2);
        }

        g2.drawImage(image, 0, 0, size, size, 0, 0, size, size, null);

        g2.setStroke(new BasicStroke(1));
        if (shape instanceof Rectangle) {
            Rectangle r = (Rectangle) shape;
            painter.drawBevel(r);
        } else {
            g2.setColor(new Color(0, 0, 0, 120));
            g2.draw(shape);
        }

        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(1));
        g2.draw(new Ellipse2D.Float(point.x - 3, point.y - 3, 6, 6));
        g2.setColor(Color.black);
        g2.draw(new Ellipse2D.Float(point.x - 4, point.y - 4, 8, 8));

        g.translate(-imagePadding.left, -imagePadding.top);
    }

    /**
     * Set the mode of this panel.
     *
     * @param mode This must be one of the following constants from the {@code ColorPicker} class:
     *             {@code HUE}, <code>SATURATION</code>, <code>BRIGHTNESS</code>, <code>RED</code>, <code>GREEN</code>, or {@code BLUE}
     */
    public void setMode(ColorPickerMode mode) {
        Objects.requireNonNull(mode);

        if (this.mode == mode) {
            return;
        }

        this.mode = mode;
        regenerateImage();
        regeneratePoint();
    }

    /**
     * Sets the selected color of this panel.
     * <P>If this panel is in HUE, SATURATION, or BRIGHTNESS mode, then
     * this method converts these values to HSB coordinates
     * and calls {@code setHSB}.
     * <P>This method may regenerate the graphic if necessary.
     *
     * @param r the red value of the selected color.
     * @param g the green value of the selected color.
     * @param b the blue value of the selected color.
     */
    public void setRGB(int r, int g, int b) {
        if (0 > r || 255 < r) {
            throw new IllegalArgumentException("The red value (" + r + ") must be between [0,255].");
        }
        if (0 > g || 255 < g) {
            throw new IllegalArgumentException("The green value (" + g + ") must be between [0,255].");
        }
        if (0 > b || 255 < b) {
            throw new IllegalArgumentException("The blue value (" + b + ") must be between [0,255].");
        }

        if (red != r || green != g || blue != b) {
            if (RED == mode || GREEN == mode || BLUE == mode) {
                int lastR = red;
                int lastG = green;
                int lastB = blue;
                red = r;
                green = g;
                blue = b;

                if (RED == mode) {
                    if (lastR != r) {
                        regenerateImage();
                    }
                } else if (GREEN == mode) {
                    if (lastG != g) {
                        regenerateImage();
                    }
                } else if (BLUE == mode) {
                    if (lastB != b) {
                        regenerateImage();
                    }
                }
            } else {
                float[] hsb = new float[3];
                Color.RGBtoHSB(r, g, b, hsb);
                setHSB(hsb[0], hsb[1], hsb[2]);
                return;
            }
            regeneratePoint();
            repaint();
            fireChangeListeners();
        }
    }

    /**
     * @return the HSB values of the selected color.
     * Each value is between [0,1].
     */
    public float[] getHSB() {
        return new float[]{hue, sat, bri};
    }

    /**
     * @return the RGB values of the selected color.
     * Each value is between [0,255].
     */
    public int[] getRGB() {
        return new int[]{red, green, blue};
    }

    /**
     * Returns the color at the indicated point in HSB values.
     *
     * @param p a point relative to this panel.
     * @return the HSB values at the point provided.
     */
    private float[] getHSB(Point p) {
        if (RED == mode || GREEN == mode || BLUE == mode) {
            int[] rgb = getRGB(p);
            return Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], null);
        }

        int size = calculateSize();
        p.translate(-(getWidth() / 2 - size / 2), -(getHeight() / 2 - size / 2));
        if (BRIGHTNESS == mode || SATURATION == mode) {
            //the two circular views:
            double radius = size / 2.0;
            double x = p.getX() - size / 2.0;
            double y = p.getY() - size / 2.0;
            double r = StrictMath.sqrt(StrictMath.pow(x, 2.0) + StrictMath.pow(y, 2.0)) / radius;

            if (1 < r) {
                r = 1;
            }

            double theta = StrictMath.atan2(y, x) / (Math.PI * 2.0);
            if (BRIGHTNESS == mode) {
                return new float[]{
                    (float) (theta + 0.25f),
                    (float) r,
                    bri};
            }
            return new float[]{
                (float) (theta + 0.25f),
                sat,
                (float) r
            };
        }
        float s = (float) p.x / size;
        float b = (float) p.y / size;
        if (0 > s) {
            s = 0;
        }
        if (1 < s) {
            s = 1;
        }
        if (0 > b) {
            b = 0;
        }
        if (1 < b) {
            b = 1;
        }
        return new float[]{hue, s, b};
    }

    private int calculateSize() {
        return Math.min(MAX_SIZE, Math.min(getWidth() - imagePadding.left - imagePadding.right, getHeight() - imagePadding.top - imagePadding.bottom));
    }

    /**
     * Returns the color at the indicated point in RGB values.
     *
     * @param p a point relative to this panel.
     * @return the RGB values at the point provided.
     */
    private int[] getRGB(Point p) {
        if (BRIGHTNESS == mode || SATURATION == mode || HUE == mode) {
            float[] hsb = getHSB(p);
            int rgb = Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
            int r = (rgb & 0xff0000) >> 16;
            int g = (rgb & 0xff00) >> 8;
            int b = rgb & 0xff;
            return new int[]{r, g, b};
        }

        int size = calculateSize();
        p.translate(-(getWidth() / 2 - size / 2), -(getHeight() / 2 - size / 2));

        int x2 = p.x * 255 / size;
        int y2 = p.y * 255 / size;
        if (0 > x2) {
            x2 = 0;
        }
        if (255 < x2) {
            x2 = 255;
        }
        if (0 > y2) {
            y2 = 0;
        }
        if (255 < y2) {
            y2 = 255;
        }

        if (RED == mode) {
            return new int[]{red, x2, y2};
        }
        if (GREEN == mode) {
            return new int[]{x2, green, y2};
        }
        return new int[]{x2, y2, blue};
    }

    /**
     * Sets the selected color of this panel.
     * <P>If this panel is in RED, GREEN, or BLUE mode, then
     * this method converts these values to RGB coordinates
     * and calls {@code setRGB}.
     * <P>This method may regenerate the graphic if necessary.
     *
     * @param h the hue value of the selected color.
     * @param s the saturation value of the selected color.
     * @param b the brightness value of the selected color.
     */
    public void setHSB(float h, float s, float b) {
        //hue is cyclic: it can be any value
        h -= Math.floor(h);

        if (0 > s || 1 < s) {
            throw new IllegalArgumentException("The saturation value (" + s + ") must be between [0,1]");
        }
        if (0 > b || 1 < b) {
            throw new IllegalArgumentException("The brightness value (" + b + ") must be between [0,1]");
        }

        if (hue != h || sat != s || bri != b) {
            if (HUE == mode || BRIGHTNESS == mode || SATURATION == mode) {
                float lastHue = hue;
                float lastBri = bri;
                float lastSat = sat;
                hue = h;
                sat = s;
                bri = b;
                if (HUE == mode) {
                    if (lastHue != hue) {
                        regenerateImage();
                    }
                } else if (SATURATION == mode) {
                    if (lastSat != sat) {
                        regenerateImage();
                    }
                } else if (BRIGHTNESS == mode) {
                    if (lastBri != bri) {
                        regenerateImage();
                    }
                }
            } else {

                Color c = new Color(Color.HSBtoRGB(h, s, b));
                setRGB(c.getRed(), c.getGreen(), c.getBlue());
                return;
            }


            Color c = new Color(Color.HSBtoRGB(hue, sat, bri));
            red = c.getRed();
            green = c.getGreen();
            blue = c.getBlue();

            regeneratePoint();
            repaint();
            fireChangeListeners();
        }
    }

    /**
     * Recalculates the (x,y) point used to indicate the selected color.
     */
    private void regeneratePoint() {
        int size = calculateSize();
        if (HUE == mode || SATURATION == mode || BRIGHTNESS == mode) {
            if (HUE == mode) {
                point = new Point((int) (sat * size + 0.5), (int) (bri * size + 0.5));
            } else if (SATURATION == mode) {
                double theta = hue * 2 * Math.PI - Math.PI / 2;
                if (0 > theta) {
                    theta += 2 * Math.PI;
                }

                double r = bri * size / 2;
                point = new Point((int) (r * StrictMath.cos(theta) + 0.5 + size / 2.0), (int) (r * StrictMath.sin(theta) + 0.5 + size / 2.0));
            } else if (BRIGHTNESS == mode) {
                double theta = hue * 2 * Math.PI - Math.PI / 2;
                if (0 > theta) {
                    theta += 2 * Math.PI;
                }
                double r = sat * size / 2;
                point = new Point((int) (r * StrictMath.cos(theta) + 0.5 + size / 2.0), (int) (r * StrictMath.sin(theta) + 0.5 + size / 2.0));
            }
        } else if (RED == mode) {
            point = new Point((int) (green * size / 255.0f + 0.49f),
                (int) (blue * size / 255.0f + 0.49f));
        } else if (GREEN == mode) {
            point = new Point((int) (red * size / 255.0f + 0.49f),
                (int) (blue * size / 255.0f + 0.49f));
        } else if (BLUE == mode) {
            point = new Point((int) (red * size / 255.0f + 0.49f),
                (int) (green * size / 255.0f + 0.49f));
        }
    }

    /**
     * Regenerates the image.
     */
    private synchronized void regenerateImage() {
        int size = calculateSize();

        if (BRIGHTNESS == mode || SATURATION == mode) {
            float bri2 = bri;
            float sat2 = sat;
            float radius = size / 2.0f;
            float k = 1.2f; //the number of pixels to antialias
            for (int y = 0; y < size; y++) {
                float y2 = y - size / 2.0f;
                for (int x = 0; x < size; x++) {
                    float x2 = x - size / 2.0f;
                    double theta = StrictMath.atan2(y2, x2) - 3 * Math.PI / 2.0;
                    if (0 > theta) {
                        theta += 2 * Math.PI;
                    }

                    double r = Math.sqrt(x2 * x2 + y2 * y2);
                    if (r <= radius) {
                        float hue2;
                        if (BRIGHTNESS == mode) {
                            hue2 = (float) (theta / (2 * Math.PI));
                            sat2 = (float) (r / radius);
                        } else { //SATURATION
                            hue2 = (float) (theta / (2 * Math.PI));
                            bri2 = (float) (r / radius);
                        }
                        row[x] = Color.HSBtoRGB(hue2, sat2, bri2);
                        if (r > radius - k) {
                            int alpha = (int) (255 - 255 * (r - radius + k) / k);
                            if (0 > alpha) {
                                alpha = 0;
                            }
                            if (255 < alpha) {
                                alpha = 255;
                            }
                            row[x] &= 0xffffff + (alpha << 24);
                        }
                    } else {
                        row[x] = 0x00000000;
                    }
                }
                image.getRaster().setDataElements(0, y, size, 1, row);
            }
        } else if (HUE == mode) {
            float hue2 = hue;
            for (int y = 0; y < size; y++) {
                float y2 = (float) y / size;
                for (int x = 0; x < size; x++) {
                    float x2 = (float) x / size;
                    row[x] = Color.HSBtoRGB(hue2, x2, y2);
                }
                image.getRaster().setDataElements(0, y, image.getWidth(), 1, row);
            }
        } else { //mode is RED, GREEN, or BLUE
            int red2 = red;
            int green2 = green;
            int blue2 = blue;
            for (int y = 0; y < size; y++) {
                float y2 = (float) y / size;
                for (int x = 0; x < size; x++) {
                    float x2 = (float) x / size;
                    if (RED == mode) {
                        green2 = (int) (x2 * 255 + 0.49);
                        blue2 = (int) (y2 * 255 + 0.49);
                    } else if (GREEN == mode) {
                        red2 = (int) (x2 * 255 + 0.49);
                        blue2 = (int) (y2 * 255 + 0.49);
                    } else {
                        red2 = (int) (x2 * 255 + 0.49);
                        green2 = (int) (y2 * 255 + 0.49);
                    }
                    row[x] = 0xFF000000 + (red2 << 16) + (green2 << 8) + blue2;
                }
                image.getRaster().setDataElements(0, y, size, 1, row);
            }
        }
        repaint();
    }

    @Override
    public void modeChanged(ModeModel modeModel) {
        setMode(modeModel.getMode());
    }

    @Override
    public void colorChanged(ColorModel colorModel) {
        colorListenerWrapper.colorChanged(colorModel);
    }

    public void aboutToChangeColor() {
        colorListenerWrapper.aboutToChangeValue();
    }

    private class ColorPickerKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int dx = 0;
            int dy = 0;
            if (KeyEvent.VK_LEFT == e.getKeyCode()) {
                dx = -1;
            } else if (KeyEvent.VK_RIGHT == e.getKeyCode()) {
                dx = 1;
            } else if (KeyEvent.VK_UP == e.getKeyCode()) {
                dy = -1;
            } else if (KeyEvent.VK_DOWN == e.getKeyCode()) {
                dy = 1;
            }
            int multiplier = 1;
            if (e.isShiftDown() && e.isAltDown()) {
                multiplier = 10;
            } else if (e.isShiftDown() || e.isAltDown()) {
                multiplier = 5;
            }
            if (0 != dx || 0 != dy) {
                int size = calculateSize();

                int offsetX = getWidth() / 2 - size / 2;
                int offsetY = getHeight() / 2 - size / 2;
                mouseListener.mousePressed(new MouseEvent(ColorPickerPanel.this,
                    MouseEvent.MOUSE_PRESSED,
                    System.currentTimeMillis(), 0,
                    point.x + multiplier * dx + offsetX,
                    point.y + multiplier * dy + offsetY,
                    1, false
                ));
            }
        }
    }

    private class ColorPickerMouseInputListener extends MouseInputAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if (calculateSize() > 0) {
                requestFocus();
                Point p = e.getPoint();
                if (BRIGHTNESS == mode || SATURATION == mode || HUE == mode) {
                    float[] hsb = getHSB(p);
                    setHSB(hsb[0], hsb[1], hsb[2]);
                } else {
                    int[] rgb = getRGB(p);
                    setRGB(rgb[0], rgb[1], rgb[2]);
                }
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            mousePressed(e);
        }
    }

    private class ResizeListener extends ComponentAdapter {

        @Override
        public void componentResized(ComponentEvent e) {
            regeneratePoint();
            regenerateImage();
        }

    }
}
