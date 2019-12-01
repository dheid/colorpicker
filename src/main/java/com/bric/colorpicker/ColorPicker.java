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
import com.bric.colorpicker.listeners.ColorListener;
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
     * {@code PropertyChangeEvents} will be triggered for this property when {@code setModeControlsVisible()}
     * is called.
     */
    private static final String MODE_CONTROLS_VISIBLE_PROPERTY = "mode controls visible";

    /**
     * The localized STRINGS used in this (and related) panel(s).
     */
    private static final ResourceBundle strings = ResourceBundle.getBundle("com.bric.colorpicker.resources.ColorPicker");

    private final ColorModel colorModel = new ColorModel();
    private final ModeModel modeModel = new ModeModel();
    private final ColorSlider slider = new ColorSlider();
    private final Option alphaOption = new AlphaOption();
    private final Option hueOption = new HueOption();
    private final Option saturationOption = new SaturationOption();
    private final Option brightnessOption = new BrightnessOption();
    private final Option redOption = new RedOption();
    private final Option greenOption = new GreenOption();
    private final Option blueOption = new BlueOption();
    private final ColorSwatch preview = new ColorSwatch(50);
    private final JLabel hexLabel = new JLabel(ColorPicker.strings.getObject("hexLabel").toString());
    private final HexField hexField = new HexField();
    private final JPanel expertControls = new JPanel(new GridBagLayout());
    private final ColorPickerPanel colorPanel = new ColorPickerPanel();
    private final OpacitySlider opacitySlider = new OpacitySlider();
    private final JLabel opacityLabel = new JLabel(ColorPicker.strings.getObject("opacityLabel").toString());

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

        this.initNames();

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
            this.hueOption, this.saturationOption, this.brightnessOption, this.redOption, this.greenOption, this.blueOption
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
        optionsPanel.add(this.hexLabel, constraints);

        constraints.gridx++;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        optionsPanel.add(this.hexField, constraints);

        this.alphaOption.addTo(optionsPanel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = normalInsets;
        constraints.gridwidth = 2;
        this.add(this.colorPanel, constraints);

        constraints.gridwidth = 1;
        constraints.insets = normalInsets;
        constraints.gridx += 2;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.VERTICAL;
        constraints.weightx = 0;
        this.add(this.slider, constraints);

        constraints.gridx++;
        constraints.fill = GridBagConstraints.VERTICAL;
        constraints.gridheight = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(0, 0, 0, 0);
        this.add(this.expertControls, constraints);

        constraints.gridx = 0;
        constraints.gridheight = 1;
        constraints.gridy = 1;
        constraints.weightx = 0;
        constraints.weighty = 0;
        constraints.insets = normalInsets;
        constraints.anchor = GridBagConstraints.CENTER;
        this.add(this.opacityLabel, constraints);

        constraints.gridx++;
        constraints.gridwidth = 2;
        constraints.weightx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        this.add(this.opacitySlider, constraints);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridheight = 1;
        constraints.gridwidth = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weighty = 1;
        constraints.weightx = 1;
        constraints.anchor = GridBagConstraints.PAGE_START;
        constraints.insets = new Insets(normalInsets.top, normalInsets.left + 8, normalInsets.bottom + 10, normalInsets.right + 8);
        this.expertControls.add(this.preview, constraints);

        constraints.gridy++;
        constraints.weighty = 0;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(normalInsets.top, normalInsets.left, 0, normalInsets.right);
        this.expertControls.add(optionsPanel, constraints);

        this.initializeColorPanel();
        this.initializeSlider();
        this.initializePreview();
        this.initializeHexField();

        this.initialize(this.hueOption);
        this.initialize(this.saturationOption);
        this.initialize(this.brightnessOption);

        this.initialize(this.redOption);
        this.initialize(this.greenOption);
        this.initialize(this.blueOption);

        this.initializeOpacitySlider();
        this.initialize(this.alphaOption);

        this.setExpertControlsVisible(showExpertControls);

        this.setOpacityVisible(includeOpacity);

        ColorPicker.setOpaque(this, false);

        this.setColor(Color.BLACK);
        this.setMode(ColorPickerMode.BRIGHTNESS);
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
            ColorPicker.setOpaque(child, opaque);
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

    private void initialize(Option option) {
        this.colorModel.addColorListener(option);
        option.addSpinnerChangeListener(e -> {
            if (this.colorModel.isChanging()) {
                return;
            }
            option.aboutToChangeColor();
            option.update(this.colorModel);
        });

        this.modeModel.addListener(option);
        option.addRadioActionListener(e -> {
            if (this.colorModel.isChanging()) {
                return;
            }
            option.aboutToChangeMode();
            option.update(this.modeModel);
        });

        option.addFocusListener(new SelectAllListener());
    }

    private void initializeOpacitySlider() {
        this.colorModel.addColorListener(this.opacitySlider);
        this.opacitySlider.addChangeListener(e -> {
            if (!this.opacitySlider.getValueIsAdjusting()) {
                if (this.colorModel.isChanging()) {
                    return;
                }
                this.opacitySlider.aboutToChangeColor();
                this.colorModel.setAlpha(this.opacitySlider.getValue());
            }
        });
    }

    private void initializeHexField() {
        this.colorModel.addColorListener(this.hexField);
        HexFieldListener hexFieldListener = new HexFieldListener();
        hexFieldListener.setColorModel(this.colorModel);
        hexFieldListener.setHexField(this.hexField);
        this.hexField.getDocument().addDocumentListener(hexFieldListener);
        this.hexField.addFocusListener(new SelectAllListener());
    }

    private void initializePreview() {
        this.preview.setOpaque(true);
        this.colorModel.addColorListener(this.preview);
    }

    private void initializeColorPanel() {
        int height = this.expertControls.getPreferredSize().height;
        this.colorPanel.setPreferredSize(new Dimension(height, height));

        this.colorModel.addColorListener(this.colorPanel);
        this.modeModel.addListener(this.colorPanel);

        this.colorPanel.addChangeListener(e -> {
            if (this.colorModel.isChanging()) {
                return;
            }
            int[] rgb = this.colorPanel.getRGB();
            this.colorPanel.aboutToChangeColor();
            this.colorModel.setColor(new Color(rgb[0], rgb[1], rgb[2]));
        });
    }

    private void initializeSlider() {
        this.colorModel.addColorListener(this.slider);
        this.modeModel.addListener(this.slider);
        this.slider.addChangeListener(e -> {
            if (!this.slider.getValueIsAdjusting()) {
                if (this.colorModel.isChanging()) {
                    return;
                }
                this.slider.aboutToChangeColor();
                ColorPickerMode mode = this.modeModel.getMode();
                switch (mode) {
                    case HUE:
                        this.colorModel.setHue(this.slider.getValue() / (float) mode.getMax());
                        break;
                    case BRIGHTNESS:
                        this.colorModel.setBrightness(this.slider.getValue() / (float) mode.getMax());
                        break;
                    case SATURATION:
                        this.colorModel.setSaturation(this.slider.getValue() / (float) mode.getMax());
                        break;
                    case RED:
                        this.colorModel.setRed(this.slider.getValue());
                        break;
                    case GREEN:
                        this.colorModel.setGreen(this.slider.getValue());
                        break;
                    case BLUE:
                        this.colorModel.setBlue(this.slider.getValue());
                        break;
                }
            }
        });
        this.slider.setUI(new ColorSliderUI(this.slider, this));
    }

    /**
     * @return the currently selected {@code Option}
     */
    public Option getSelectedOption() {
        ColorPickerMode mode = this.getMode();

        switch (mode) {
            case HUE:
                return this.hueOption;
            case SATURATION:
                return this.saturationOption;
            case BRIGHTNESS:
                return this.brightnessOption;
            case RED:
                return this.redOption;
            case GREEN:
                return this.greenOption;
            case BLUE:
                return this.blueOption;
            default:
                return null;
        }
    }

    private void initNames() {
        this.hexField.setName("Hex");
        this.hueOption.setName("Hue");
        this.saturationOption.setName("Saturation");
        this.brightnessOption.setName("Brightness");
        this.redOption.setName("Red");
        this.greenOption.setName("Green");
        this.blueOption.setName("Blue");
    }

    /**
     * This controls whether the hex field (and label) are visible or not.
     * <P>Note this lives inside the "expert controls", so if {@code setExpertControlsVisible(false)}
     * has been called, then calling this method makes no difference: the hex controls will be hidden.
     *
     * @param hexControlsVisible Enables or disables visibility
     */
    public void setHexControlsVisible(boolean hexControlsVisible) {
        this.hexLabel.setVisible(hexControlsVisible);
        this.hexField.setVisible(hexControlsVisible);
    }

    /**
     * This controls whether the preview swatch visible or not.
     * <P>Note this lives inside the "expert controls", so if {@code setExpertControlsVisible(false)}
     * has been called, then calling this method makes no difference: the swatch will be hidden.
     *
     * @param previewSwatchVisible Enables or disables visibility
     *
     */
    public void setPreviewSwatchVisible(boolean previewSwatchVisible) {
        this.preview.setVisible(previewSwatchVisible);
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
        this.expertControls.setVisible(b);
    }

    /**
     * This controls whether the radio buttons that adjust the mode are visible.
     * <P>(These buttons appear next to the spinners in the expert controls.)
     * <P>Note these live inside the "expert controls", so if {@code setExpertControlsVisible(false)}
     * has been called, then these will never be visible.
     *
     * @param visible should controls be visible or not
     */
    public void setModeControlsVisible(boolean visible) {
        this.hueOption.setRadioButtonVisible(visible && this.hueOption.isVisible());
        this.saturationOption.setRadioButtonVisible(visible && this.saturationOption.isVisible());
        this.brightnessOption.setRadioButtonVisible(visible && this.brightnessOption.isVisible());
        this.redOption.setRadioButtonVisible(visible && this.redOption.isVisible());
        this.greenOption.setRadioButtonVisible(visible && this.greenOption.isVisible());
        this.blueOption.setRadioButtonVisible(visible && this.blueOption.isVisible());
        this.putClientProperty(ColorPicker.MODE_CONTROLS_VISIBLE_PROPERTY, visible);
    }

    /**
     * @return the current mode of this {@code ColorPicker}.
     * <BR>This will return <code>HUE</code>,  {@code SATURATION},  {@code BRIGHTNESS},
     * <code>RED</code>,  {@code GREEN}, or {@code BLUE}.
     * <P>The default mode is {@code BRIGHTNESS}, because that provides the most
     * aesthetic/recognizable color wheel.
     */
    public ColorPickerMode getMode() {
        return this.modeModel.getMode();
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
        this.modeModel.setMode(mode);
    }

    /**
     * Sets the current color of this {@code ColorPicker}
     *
     * @param r the redOption value.  Must be between [0,255].
     * @param g the greenOption value.  Must be between [0,255].
     * @param b the blueOption value.  Must be between [0,255].
     */
    public void setRGB(int r, int g, int b) {
        this.setColor(new Color(r, g, b));
    }

    /**
     * @return the current {@code Color} this {@code ColorPicker} has selected.
     */
    public Color getColor() {
        return this.colorModel.getColor();
    }

    /**
     * Sets the current color of this {@code ColorPicker}.
     *
     * @param newColor the new color to use.
     */
    public void setColor(Color newColor) {
        Color lastColor = this.colorModel.getColor();
        this.colorModel.setColor(newColor);
        this.firePropertyChange(ColorPicker.SELECTED_COLOR_PROPERTY, lastColor, newColor);
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
        return this.expertControls;
    }

    /**
     * This shows or hides the RGB spinner controls.
     * <P>Note these live inside the "expert controls", so if {@code setExpertControlsVisible(false)}
     * has been called, then calling this method makes no difference: the RGB controls will be hidden.
     *
     * @param b whether the controls should be visible or not.
     */
    public void setRGBControlsVisible(boolean b) {
        boolean radioButtonsAllowed = this.areRadioButtonsAllowed();
        this.redOption.setVisible(b, radioButtonsAllowed);
        this.greenOption.setVisible(b, radioButtonsAllowed);
        this.blueOption.setVisible(b, radioButtonsAllowed);
    }

    private boolean areRadioButtonsAllowed() {
        Boolean z = (Boolean) this.getClientProperty(ColorPicker.MODE_CONTROLS_VISIBLE_PROPERTY);
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
        boolean radioButtonsAllowed = this.areRadioButtonsAllowed();
        this.hueOption.setVisible(b, radioButtonsAllowed);
        this.saturationOption.setVisible(b, radioButtonsAllowed);
        this.brightnessOption.setVisible(b, radioButtonsAllowed);
    }

    /**
     * This shows or hides the alphaOption controls.
     * <P>Note the alphaOption spinner live inside the "expert controls", so if {@code setExpertControlsVisible(false)}
     * has been called, then this method does not affect that spinner.
     * However, the opacity colorslider is <i>not</i> affected by the visibility of the export controls.
     *
     * @param visible should opacity controls be visible
     */
    public final void setOpacityVisible(boolean visible) {
        this.opacityLabel.setVisible(visible);
        this.opacitySlider.setVisible(visible);
        this.alphaOption.setLabelVisible(visible);
        this.alphaOption.setSpinnerVisible(visible);
    }

    /**
     * @return the {@code ColorPickerPanel} this {@code ColorPicker} displays.
     */
    public ColorPickerPanel getColorPanel() {
        return this.colorPanel;
    }

    /**
     * Sets the current color of this {@code ColorPicker}
     *
     * @param h the hue value.
     * @param s the saturation value.  Must be between [0,1].
     * @param b the blueOption value.  Must be between [0,1].
     */
    public void setHSB(float h, float s, float b) {

        ColorPicker.requireValidFloat(h, "hue");
        ColorPicker.requireValidFloat(s, "saturation");
        ColorPicker.requireValidFloat(b, "brightness");

        this.setColor(Color.getHSBColor(h, s, b));
    }

    /**
     * @return the current HSB coordinates of this ColorPicker. Each value is between [0,1].
     * @see Color
     */
    public float[] getHSB() {
        return this.colorModel.getHSB();
    }

    /**
     * @return the current RGB coordinates of this ColorPicker. Each value is between [0,255].
     */
    public int[] getRGB() {
        return this.colorModel.getRGB();
    }

    /**
     * Sets the currently selected opacity.
     *
     * @param opacity an int between 0 and 255.
     */
    public void setOpacity(int opacity) {
        this.setColor(new Color(this.colorModel.getRed(), this.colorModel.getGreen(), this.colorModel.getBlue(), opacity));
    }

    public void addColorListener(ColorListener listener) {
        this.colorModel.addColorListener(listener);
    }

    public void removeColorListener(ColorListener listener) {
        this.colorModel.removeColorListener(listener);
    }

}
