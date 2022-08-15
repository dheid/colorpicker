/*
 * @(#)PlafPaintUtils.java
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

import com.bric.colorpicker.JVM;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

/**
 * Some static methods for some common painting functions.
 *
 * @author Jeremy Wood
 **/
public class Painter {

    private Graphics2D graphics2D;

    /**
     * Four shades of white, each with increasing opacity.
     */
    private static final Color[] WHITES = {
            new Color(255, 255, 255, 50),
            new Color(255, 255, 255, 100),
            new Color(255, 255, 255, 150)
    };

    /**
     * Four shades of black, each with increasing opacity.
     */
    private static final Color[] BLACKS = {
            new Color(0, 0, 0, 50),
            new Color(0, 0, 0, 100),
            new Color(0, 0, 0, 150)
    };

    /**
     * @return the color used to indicate when a component has
     * focus.  By default this uses the color (64,113,167), but you can
     * override this by calling:
     * <BR>{@code UIManager.put("focusRing",customColor);}
     */
    private static Color getFocusRingColor() {
        Color obj = UIManager.getColor("Focus.color");
        if (obj != null) {
            return obj;
        }
        Color focusRingColor = UIManager.getColor("focusRing");
        if (focusRingColor != null) {
            return focusRingColor;
        }
        return new Color(64, 113, 167);
    }

    public void setGraphics2D(Graphics2D graphics2D) {
        this.graphics2D = graphics2D;
    }

    /**
     * Paints 3 different strokes around a shape to indicate focus.
     * The widest stroke is the most transparent, so this achieves a nice
     * "glow" effect.
     * <P>The catch is that you have to render this underneath the shape,
     * and the shape should be filled completely.
     *
     * @param shape     the shape to outline
     * @param pixelSize the number of pixels the outline should cover.
     */
    public void paintFocus(Shape shape, int pixelSize) {
        Color focusColor = getFocusRingColor();
        graphics2D = (Graphics2D) graphics2D.create();
        try {
            Color[] focusArray = {
                    new Color(focusColor.getRed(), focusColor.getGreen(), focusColor.getBlue(), 235 * focusColor.getAlpha() / 255),
                    new Color(focusColor.getRed(), focusColor.getGreen(), focusColor.getBlue(), 130 * focusColor.getAlpha() / 255),
                    new Color(focusColor.getRed(), focusColor.getGreen(), focusColor.getBlue(), 80 * focusColor.getAlpha() / 255)
            };

            if (JVM.isUsingQuartz()) {
                graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics2D.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            } else {
                graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics2D.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
            }

            graphics2D.setStroke(new BasicStroke(2 * pixelSize + 1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            graphics2D.setColor(focusArray[2]);
            graphics2D.draw(shape);
            if (2 * pixelSize + 1 > 0) {
                graphics2D.setStroke(new BasicStroke(2 * pixelSize - 2 + 1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                graphics2D.setColor(focusArray[1]);
                graphics2D.draw(shape);
            }
            if (2 * pixelSize - 4 + 1 > 0) {
                graphics2D.setStroke(new BasicStroke(2 * pixelSize - 4 + 1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                graphics2D.setColor(focusArray[0]);
                graphics2D.draw(shape);
            }
        } finally {
            graphics2D.dispose();
        }
    }

    /**
     * Uses translucent shades of white and black to draw highlights
     * and shadows around a rectangle, and then frames the rectangle
     * with a shade of gray (120).
     * <P>This should be called to add a finishing touch on top of
     * existing graphics.
     *
     * @param r the rectangle to paint.
     */
    public void drawBevel(Rectangle r) {
        graphics2D.setStroke(new BasicStroke(1));
        drawColors(BLACKS, r.x, r.y + r.height, r.x + r.width, r.y + r.height, SwingConstants.SOUTH);
        drawColors(BLACKS, r.x + r.width, r.y, r.x + r.width, r.y + r.height, SwingConstants.EAST);

        drawColors(WHITES, r.x, r.y, r.x + r.width, r.y, SwingConstants.NORTH);
        drawColors(WHITES, r.x, r.y, r.x, r.y + r.height, SwingConstants.WEST);

        graphics2D.setColor(new Color(120, 120, 120));
        graphics2D.drawRect(r.x, r.y, r.width, r.height);
    }

    private void drawColors(Color[] colors, int x1, int y1, int x2, int y2, int direction) {
        int numColors = colors.length;
        for (int a = 0; a < numColors; a++) {
            graphics2D.setColor(colors[numColors - a - 1]);
            if (direction == SwingConstants.SOUTH) {
                graphics2D.drawLine(x1, y1 - a, x2, y2 - a);
            } else if (direction == SwingConstants.NORTH) {
                graphics2D.drawLine(x1, y1 + a, x2, y2 + a);
            } else if (direction == SwingConstants.EAST) {
                graphics2D.drawLine(x1 - a, y1, x2 - a, y2);
            } else if (direction == SwingConstants.WEST) {
                graphics2D.drawLine(x1 + a, y1, x2 + a, y2);
            }
        }
    }
}
