/*
 * @(#)ColorSwatch.java
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
package com.bric.colorpicker.parts;

import com.bric.colorpicker.colorslider.Painter;
import com.bric.colorpicker.contextualmenu.ContextualMenuHelper;
import com.bric.colorpicker.listeners.ColorListener;
import com.bric.colorpicker.models.ColorModel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.Toolkit;
import java.awt.datatransfer.Transferable;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.JPanel;

/**
 * This is a square, opaque panel used to indicate
 * a certain color.
 * <P>The color is assigned with the {@code setForeground()} method.
 * <P>Also the user can right-click this panel and select 'Copy' to send
 * a 100x100 image of this color to the clipboard.  (This feature was
 * added at the request of a friend who paints; she wanted to select a
 * color and then quickly print it off, and then mix her paints to match
 * that shade.)
 */
public class ColorSwatch extends JPanel implements ColorListener {
    /**
     * If this client property maps to true, then a "Copy" menu item will be available in a contextual menu.
     * The default value for this key is assumed to be false if undefined.
     */
    private static final String PROPERTY_COPY_CONTEXTUAL_MENU_ITEM = ColorSwatch.class + ".copyContextualMenuItem";
    private static final String LOCALIZATION_BUNDLE_PATH = "com.bric.colorpicker.resources.ColorPicker";
    private static TexturePaint checkerPaint;
    private final ResourceBundle strings;
    private final int w;

    public ColorSwatch(int width) {
        this(width, null);
    }

    private ColorSwatch(Color color, int width) {
        this(width);
        setForeground(color);
    }

    private ColorSwatch(Color color, int width, Locale locale) {
        this(width, locale);
        setForeground(color);
    }

    public ColorSwatch(int width, Locale locale) {
        if(locale == null) strings = ResourceBundle.getBundle(LOCALIZATION_BUNDLE_PATH);
        else strings = ResourceBundle.getBundle(LOCALIZATION_BUNDLE_PATH, locale);

        w = width;
        setPreferredSize(new Dimension(width, width));
        setMinimumSize(new Dimension(width, width));
        PropertyChangeListener pcl = evt -> updateContextualMenu();

        addPropertyChangeListener(PROPERTY_COPY_CONTEXTUAL_MENU_ITEM, pcl);
        updateContextualMenu();
    }

    private static TexturePaint getCheckerPaint() {
        if (checkerPaint == null) {
            int t = 8;
            BufferedImage bi = new BufferedImage(t * 2, t * 2, BufferedImage.TYPE_INT_RGB);
            Graphics g = bi.createGraphics();
            g.setColor(Color.white);
            g.fillRect(0, 0, 2 * t, 2 * t);
            g.setColor(Color.lightGray);
            g.fillRect(0, 0, t, t);
            g.fillRect(t, t, t, t);
            checkerPaint = new TexturePaint(bi, new Rectangle(0, 0, bi.getWidth(), bi.getHeight()));
        }
        return checkerPaint;
    }

    private void updateContextualMenu() {
        ContextualMenuHelper.clear(this);
        if (Boolean.TRUE.equals(getClientProperty(PROPERTY_COPY_CONTEXTUAL_MENU_ITEM))) {
            String menuItemName = strings.getObject("Copy").toString();
            Runnable runnable = () -> {
                BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
                Graphics2D g = image.createGraphics();
                g.setColor(getBackground());
                g.fillRect(0, 0, image.getWidth(), image.getHeight());
                g.dispose();
                Transferable contents = new ImageTransferable(image);
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(contents, null);
            };
            ContextualMenuHelper.add(this, menuItemName, runnable);
        }
    }

    @Override
    public void paint(Graphics g0) {
        super.paint(g0); //may be necessary for some look-and-feels?

        Graphics2D g = (Graphics2D) g0;

        Color c = getForeground();
        int w2 = Math.min(getWidth(), w);
        int h2 = Math.min(getHeight(), w);
        Rectangle r = new Rectangle(getWidth() / 2 - w2 / 2, getHeight() / 2 - h2 / 2, w2, h2);

        if (c.getAlpha() < 255) {
            TexturePaint checkers = getCheckerPaint();
            g.setPaint(checkers);
            g.fillRect(r.x, r.y, r.width, r.height);
        }
        g.setColor(c);
        g.fillRect(r.x, r.y, r.width, r.height);
        Painter painter = new Painter();
        painter.setGraphics2D(g);
        painter.drawBevel(r);
    }

    @Override
    public void colorChanged(ColorModel colorModel) {
        setForeground(colorModel.getColor());
    }
}

