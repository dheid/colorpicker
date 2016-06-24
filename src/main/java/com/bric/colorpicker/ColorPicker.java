package com.bric.colorpicker;/*
 * @(#)ColorPicker.java
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

import com.bric.colorpicker.colorslider.ColorSlider;
import com.bric.colorpicker.colorslider.ColorSliderUI;
import com.bric.colorpicker.listeners.HexFieldListener;
import com.bric.colorpicker.listeners.SelectAllListener;
import com.bric.colorpicker.models.ColorModel;
import com.bric.colorpicker.models.ModeModel;
import com.bric.colorpicker.options.AlphaOption;
import com.bric.colorpicker.options.BlueOption;
import com.bric.colorpicker.options.BrightnessOption;
import com.bric.colorpicker.options.GreenOption;
import com.bric.colorpicker.options.HueOption;
import com.bric.colorpicker.options.Option;
import com.bric.colorpicker.options.RedOption;
import com.bric.colorpicker.options.SaturationOption;
import com.bric.colorpicker.parts.ColorSwatch;
import com.bric.colorpicker.parts.HexField;
import com.bric.colorpicker.parts.OpacitySlider;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ResourceBundle;

/**
 * <p>This is a panel that offers a robust set of controls to pick a color.
 * <P>This was originally intended to replace the {@code JColorChooser}.
 * To use this class to create a color choosing dialog, simply call:
 * <BR>{@code ColorPicker.showDialog(frame, originalColor);}
 * <P>Here is a screenshot of the dialog that call will invoke:
 * <br><IMG SRC="https://javagraphics.java.net/resources/colorpicker.png" alt="ColorPicker Screenshot">
 * <p>However this does not have to invoked as a black-box color dialog. This class
 * is simply a panel, and you can customize and resize it for other looks.
 * For example, you might try the following panel:</p>
 * <BR>{@code ColorPicker picker = new ColorPicker(false, false);}
 * <BR>{@code picker.setPreferredSize(new Dimension(200,160));}
 * <BR>{@code picker.setMode(ColorPicker.HUE);}
 * <br><IMG SRC="https://javagraphics.java.net/resources/colorpicker3.png" alt="ColorPicker Small Screenshot">
 * <P>This will create a miniature color picker that still lets the user choose
 * from every available color, but it does not include all the buttons and
 * numeric controls on the right side of the panel. This might be ideal if you
 * are working with limited space, or non-power-users who don't need the
 * RGB values of a color.
 * <P>To listen to color changes to this panel, you can add a {@code PropertyChangeListener}
 * listening for changes to the {@code SELECTED_COLOR_PROPERTY}. This will be triggered only
 * when the RGB value of the selected color changes.
 * <P>To listen to opacity changes to this panel, use
 * a {@code PropertyChangeListener} listening
 * for changes to the {@code OPACITY_PROPERTY}.
 *
 * @see com.bric.colorpicker.ColorPickerDialog
 * @see com.bric.colorpicker.ColorPickerPanel
 */
public class ColorPicker extends JPanel {
    /**
     * {@code PropertyChangeEvents} will be triggered when the mode changes.
     * (That is, when the wheel switches from HUE, SATURATION, BRIGHTNESS, RED, GREEN, or BLUE modes.)
     */
    public static final String MODE_PROPERTY = "mode";

    /**
     * {@code PropertyChangeEvents} will be triggered for this property when the selected color
     * changes.
     * <P>(Events are only created when then RGB values of the color change.  This means, for example,
     * that the change from HSB(0,0,0) to HSB(.4,0,0) will <i>not</i> generate events, because when the
     * brightness stays zero the RGB color remains (0,0,0).  So although the hueOption moved around, the color
     * is still black, so no events are created.)
     */
    private static final String SELECTED_COLOR_PROPERTY = "selected color";

    /**
     * <code>PropertyChangeEvents</code> will be triggered for this property when {@code setModeControlsVisible()}
     * is called.
     */
    private static final String MODE_CONTROLS_VISIBLE_PROPERTY = "mode controls visible";

    /**
     * The localized STRINGS used in this (and related) panel(s).
     */
    private static ResourceBundle strings = ResourceBundle.getBundle("com.bric.colorpicker.resources.ColorPicker");

    private ColorModel colorModel = new ColorModel();
    private ModeModel modeModel = new ModeModel();
    private ColorSlider slider = new ColorSlider();
    private Option alphaOption = new AlphaOption();
    private Option hueOption = new HueOption();
    private Option saturationOption = new SaturationOption();
    private Option brightnessOption = new BrightnessOption();
    private Option redOption = new RedOption();
    private Option greenOption = new GreenOption();
    private Option blueOption = new BlueOption();
    private ColorSwatch preview = new ColorSwatch(50);
    private JLabel hexLabel = new JLabel(strings.getObject("hexLabel").toString());
    private HexField hexField = new HexField();
    private JPanel expertControls = new JPanel(new GridBagLayout());
    private ColorPickerPanel colorPanel = new ColorPickerPanel();
    private OpacitySlider opacitySlider = new OpacitySlider();
    private JLabel opacityLabel = new JLabel(strings.getObject("opacityLabel").toString());

    /**
     * Create a new {@code ColorPicker} with all controls visible except opacity.
     */
    public ColorPicker() {
        this(true, false);
    }

    /**
     * Create a new {@code ColorPicker}.
     *
     * @param showExpertControls the labels/spinners/buttons on the right side of a
     *                           {@code ColorPicker} are optional.  This boolean will control whether they
     *                           are shown or not.
     *                           <P>It may be that your users will never need or want numeric control when
     *                           they choose their colors, so hiding this may simplify your interface.
     * @param includeOpacity     whether the opacity controls will be shown
     */
    public ColorPicker(boolean showExpertControls, boolean includeOpacity) {
        super(new GridBagLayout());

        initNames();

        GridBagConstraints constraints = new GridBagConstraints();

        Insets normalInsets = new Insets(3, 3, 3, 3);

        JPanel optionsPanel = new JPanel(new GridBagLayout());
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = normalInsets;
        ButtonGroup buttonGroup = new ButtonGroup();

        Option[] options = {
                hueOption, saturationOption, brightnessOption, redOption, greenOption, blueOption
        };

        for (int optionIndex = 0; optionIndex < options.length; optionIndex++) {
            if (optionIndex == 3 || optionIndex == 6) {
                constraints.insets = new Insets(normalInsets.top + 10, normalInsets.left, normalInsets.bottom, normalInsets.right);
            } else {
                constraints.insets = normalInsets;
            }
            options[optionIndex].addTo(optionsPanel, constraints, buttonGroup);
        }
        constraints.insets = new Insets(normalInsets.top + 10, normalInsets.left, normalInsets.bottom, normalInsets.right);
        constraints.anchor = GridBagConstraints.LINE_END;
        constraints.fill = GridBagConstraints.NONE;
        optionsPanel.add(hexLabel, constraints);

        constraints.gridx++;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        optionsPanel.add(hexField, constraints);

        alphaOption.addTo(optionsPanel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = normalInsets;
        constraints.gridwidth = 2;
        add(colorPanel, constraints);

        constraints.gridwidth = 1;
        constraints.insets = normalInsets;
        constraints.gridx += 2;
        constraints.weighty = 1;
        constraints.gridwidth = 1;
        constraints.fill = GridBagConstraints.VERTICAL;
        constraints.weightx = 0;
        add(slider, constraints);

        constraints.gridx++;
        constraints.fill = GridBagConstraints.VERTICAL;
        constraints.gridheight = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(0, 0, 0, 0);
        add(expertControls, constraints);

        constraints.gridx = 0;
        constraints.gridheight = 1;
        constraints.gridy = 1;
        constraints.weightx = 0;
        constraints.weighty = 0;
        constraints.insets = normalInsets;
        constraints.anchor = GridBagConstraints.CENTER;
        add(opacityLabel, constraints);

        constraints.gridx++;
        constraints.gridwidth = 2;
        constraints.weightx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(opacitySlider, constraints);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridheight = 1;
        constraints.gridwidth = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weighty = 1;
        constraints.weightx = 1;
        constraints.anchor = GridBagConstraints.PAGE_START;
        constraints.insets = new Insets(normalInsets.top, normalInsets.left + 8, normalInsets.bottom + 10, normalInsets.right + 8);
        expertControls.add(preview, constraints);

        constraints.gridy++;
        constraints.weighty = 0;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(normalInsets.top, normalInsets.left, 0, normalInsets.right);
        expertControls.add(optionsPanel, constraints);

        initializeColorPanel();
        initializeSlider();
        initializePreview();
        initializeHexField();

        initialize(hueOption);
        initialize(saturationOption);
        initialize(brightnessOption);

        initialize(redOption);
        initialize(greenOption);
        initialize(blueOption);

        initializeOpacitySlider();
        initialize(alphaOption);

        setExpertControlsVisible(showExpertControls);

        setOpacityVisible(includeOpacity);

        setOpaque(this, false);

        setColor(Color.BLACK);
        setMode(ColorPickerMode.BRIGHTNESS);
    }

    private void initialize(Option option) {
        colorModel.addListener(option);
        option.addSpinnerChangeListener(e -> {
            if (colorModel.isChanging()) {
                return;
            }
            option.aboutToChangeColor();
            option.update(colorModel);
        });

        modeModel.addListener(option);
        option.addRadioActionListener(e -> {
            if (colorModel.isChanging()) {
                return;
            }
            option.aboutToChangeMode();
            option.update(modeModel);
        });

        option.addFocusListener(new SelectAllListener());
    }

    private static void setOpaque(JComponent jc, boolean opaque) {
        if (jc instanceof JTextField) {
            return;
        }

        jc.setOpaque(false);
        if (jc instanceof JSpinner) {
            return;
        }

        for (int a = 0; a < jc.getComponentCount(); a++) {
            JComponent child = (JComponent) jc.getComponent(a);
            setOpaque(child, opaque);
        }
    }

    private static void requireValidFloat(float f, String paramName) {
        if (Float.isInfinite(f) || Float.isNaN(f)) {
            throw new IllegalArgumentException("The " + paramName + " value '" + f + "' is not a valid number.");
        }

        if (f < 0.0f || f > 1.0f) {
            throw new IllegalArgumentException("The " + paramName + " value '" + f + "' must be between [0,1]");
        }
    }

    private void initializeOpacitySlider() {
        colorModel.addListener(opacitySlider);
        opacitySlider.addChangeListener(e -> {
            if (!opacitySlider.getValueIsAdjusting()) {
                if (colorModel.isChanging()) {
                    return;
                }
                opacitySlider.aboutToChangeColor();
                colorModel.setAlpha(opacitySlider.getValue());
            }
        });
    }

    private void initializeHexField() {
        colorModel.addListener(hexField);
        HexFieldListener hexFieldListener = new HexFieldListener();
        hexFieldListener.setColorModel(colorModel);
        hexFieldListener.setHexField(hexField);
        hexField.getDocument().addDocumentListener(hexFieldListener);
        hexField.addFocusListener(new SelectAllListener());
    }

    private void initializePreview() {
        preview.setOpaque(true);
        colorModel.addListener(preview);
    }

    private void initializeColorPanel() {
        int height = expertControls.getPreferredSize().height;
        colorPanel.setPreferredSize(new Dimension(height, height));

        colorModel.addListener(colorPanel);
        modeModel.addListener(colorPanel);

        colorPanel.addChangeListener(e -> {
            if (colorModel.isChanging()) {
                return;
            }
            int[] rgb = colorPanel.getRGB();
            colorPanel.aboutToChangeColor();
            colorModel.setColor(new Color(rgb[0], rgb[1], rgb[2]));
        });
    }

    private void initializeSlider() {
        colorModel.addListener(slider);
        modeModel.addListener(slider);
        slider.addChangeListener(e -> {
            if (!slider.getValueIsAdjusting()) {
                if (colorModel.isChanging()) {
                    return;
                }
                slider.aboutToChangeColor();
                ColorPickerMode mode = modeModel.getMode();
                switch (mode) {
                    case HUE:
                        colorModel.setHue(slider.getValue() / (float) mode.getMax());
                        break;
                    case BRIGHTNESS:
                        colorModel.setBrightness(slider.getValue() / (float) mode.getMax());
                        break;
                    case SATURATION:
                        colorModel.setSaturation(slider.getValue() / (float) mode.getMax());
                        break;
                    case RED:
                        colorModel.setRed(slider.getValue());
                        break;
                    case GREEN:
                        colorModel.setGreen(slider.getValue());
                        break;
                    case BLUE:
                        colorModel.setBlue(slider.getValue());
                        break;
                }
            }
        });
        slider.setUI(new ColorSliderUI(slider, this));
    }

    /**
     * @return the currently selected {@code Option}
     */
    public Option getSelectedOption() {
        ColorPickerMode mode = getMode();

        switch (mode) {
            case HUE:
                return hueOption;
            case SATURATION:
                return saturationOption;
            case BRIGHTNESS:
                return brightnessOption;
            case RED:
                return redOption;
            case GREEN:
                return greenOption;
            case BLUE:
                return blueOption;
            default:
                return null;
        }
    }

    private void initNames() {
        hexField.setName("Hex");
        hueOption.setName("Hue");
        saturationOption.setName("Saturation");
        brightnessOption.setName("Brightness");
        redOption.setName("Red");
        greenOption.setName("Green");
        blueOption.setName("Blue");
    }

    /**
     * This controls whether the hex field (and label) are visible or not.
     * <P>Note this lives inside the "expert controls", so if {@code setExpertControlsVisible(false)}
     * has been called, then calling this method makes no difference: the hex controls will be hidden.
     */
    public void setHexControlsVisible(boolean b) {
        hexLabel.setVisible(b);
        hexField.setVisible(b);
    }

    /**
     * This controls whether the preview swatch visible or not.
     * <P>Note this lives inside the "expert controls", so if {@code setExpertControlsVisible(false)}
     * has been called, then calling this method makes no difference: the swatch will be hidden.
     */
    public void setPreviewSwatchVisible(boolean b) {
        preview.setVisible(b);
    }

    /**
     * The labels/spinners/buttons on the right side of a {@code ColorPicker}
     * are optional.  This method will control whether they are shown or not.
     * <P>It may be that your users will never need or want numeric control when
     * they choose their colors, so hiding this may simplify your interface.
     *
     * @param b whether to show or hide the expert controls.
     */
    public void setExpertControlsVisible(boolean b) {
        expertControls.setVisible(b);
    }

    /**
     * This controls whether the radio buttons that adjust the mode are visible.
     * <P>(These buttons appear next to the spinners in the expert controls.)
     * <P>Note these live inside the "expert controls", so if {@code setExpertControlsVisible(false)}
     * has been called, then these will never be visible.
     *
     * @param b
     */
    public void setModeControlsVisible(boolean b) {
        hueOption.setRadioButtonVisible(b && hueOption.isVisible());
        saturationOption.setRadioButtonVisible(b && saturationOption.isVisible());
        brightnessOption.setRadioButtonVisible(b && brightnessOption.isVisible());
        redOption.setRadioButtonVisible(b && redOption.isVisible());
        greenOption.setRadioButtonVisible(b && greenOption.isVisible());
        blueOption.setRadioButtonVisible(b && blueOption.isVisible());
        putClientProperty(MODE_CONTROLS_VISIBLE_PROPERTY, b);
    }

    /**
     * @return the current mode of this {@code ColorPicker}.
     * <BR>This will return <code>HUE</code>,  <code>SATURATION</code>,  {@code BRIGHTNESS},
     * <code>RED</code>,  <code>GREEN</code>, or {@code BLUE}.
     * <P>The default mode is {@code BRIGHTNESS}, because that provides the most
     * aesthetic/recognizable color wheel.
     */
    public ColorPickerMode getMode() {
        return modeModel.getMode();
    }

    /**
     * Sets the mode of this {@code ColorPicker}.
     * This is especially useful if this picker is in non-expert mode, so
     * the radio buttons are not visible for the user to directly select.
     *
     * @param mode must be HUE, SATURATION, BRIGHTNESS, RED, GREEN or BLUE.
     */
    public void setMode(ColorPickerMode mode) {
        if (mode == null) {
            throw new IllegalArgumentException("mode must not be null");
        }
        modeModel.setMode(mode);
    }

    /**
     * Sets the current color of this {@code ColorPicker}
     *
     * @param r the redOption value.  Must be between [0,255].
     * @param g the greenOption value.  Must be between [0,255].
     * @param b the blueOption value.  Must be between [0,255].
     */
    public void setRGB(int r, int g, int b) {
        setColor(new Color(r, g, b));
    }

    /**
     * @return the current <code>Color</code> this {@code ColorPicker} has selected.
     */
    public Color getColor() {
        return colorModel.getColor();
    }

    /**
     * Sets the current color of this {@code ColorPicker}.
     *
     * @param newColor the new color to use.
     */
    public void setColor(Color newColor) {
        Color lastColor = colorModel.getColor();
        colorModel.setColor(newColor);
        firePropertyChange(SELECTED_COLOR_PROPERTY, lastColor, newColor);
    }

    /**
     * This returns the panel with several rows of spinner controls.
     * <P>Note you can also call methods such as {@code setRGBControlsVisible()} to adjust
     * which controls are showing.
     * <P>(This returns the panel this {@code ColorPicker} uses, so if you put it in
     * another container, it will be removed from this {@code ColorPicker}.)
     *
     * @return the panel with several rows of spinner controls.
     */
    public JPanel getExpertControls() {
        return expertControls;
    }

    /**
     * This shows or hides the RGB spinner controls.
     * <P>Note these live inside the "expert controls", so if {@code setExpertControlsVisible(false)}
     * has been called, then calling this method makes no difference: the RGB controls will be hidden.
     *
     * @param b whether the controls should be visible or not.
     */
    public void setRGBControlsVisible(boolean b) {
        boolean radioButtonsAllowed = areRadioButtonsAllowed();
        redOption.setVisible(b, radioButtonsAllowed);
        greenOption.setVisible(b, radioButtonsAllowed);
        blueOption.setVisible(b, radioButtonsAllowed);
    }

    private boolean areRadioButtonsAllowed() {
        Boolean z = (Boolean) getClientProperty(MODE_CONTROLS_VISIBLE_PROPERTY);
        if (z != null) {
            return z;
        }
        return true;
    }

    /**
     * This shows or hides the HSB spinner controls.
     * <P>Note these live inside the "expert controls", so if {@code setExpertControlsVisible(false)}
     * has been called, then calling this method makes no difference: the HSB controls will be hidden.
     *
     * @param b whether the controls should be visible or not.
     */
    public void setHSBControlsVisible(boolean b) {
        boolean radioButtonsAllowed = areRadioButtonsAllowed();
        hueOption.setVisible(b, radioButtonsAllowed);
        saturationOption.setVisible(b, radioButtonsAllowed);
        brightnessOption.setVisible(b, radioButtonsAllowed);
    }

    /**
     * This shows or hides the alphaOption controls.
     * <P>Note the alphaOption spinner live inside the "expert controls", so if {@code setExpertControlsVisible(false)}
     * has been called, then this method does not affect that spinner.
     * However, the opacity colorslider is <i>not</i> affected by the visibility of the export controls.
     *
     * @param b
     */
    public final void setOpacityVisible(boolean b) {
        opacityLabel.setVisible(b);
        opacitySlider.setVisible(b);
        alphaOption.setLabelVisible(b);
        alphaOption.setSpinnerVisible(b);
    }

    /**
     * @return the <code>ColorPickerPanel</code> this {@code ColorPicker} displays.
     */
    public ColorPickerPanel getColorPanel() {
        return colorPanel;
    }

    /**
     * Sets the current color of this {@code ColorPicker}
     *
     * @param h the hue value.
     * @param s the saturation value.  Must be between [0,1].
     * @param b the blueOption value.  Must be between [0,1].
     */
    public void setHSB(float h, float s, float b) {

        requireValidFloat(h, "hue");
        requireValidFloat(s, "saturation");
        requireValidFloat(b, "brightness");

        setColor(Color.getHSBColor(h, s, b));
    }

    /**
     * @return the current HSB coordinates of this ColorPicker. Each value is between [0,1].
     * @see Color
     */
    public float[] getHSB() {
        return colorModel.getHSB();
    }

    /**
     * @return the current RGB coordinates of this ColorPicker. Each value is between [0,255].
     */
    public int[] getRGB() {
        return colorModel.getRGB();
    }

    /**
     * Sets the currently selected opacity.
     *
     * @param opacity an int between 0 and 255.
     */
    public void setOpacity(int opacity) {
        setColor(new Color(colorModel.getRed(), colorModel.getGreen(), colorModel.getBlue(), opacity));
    }

}
