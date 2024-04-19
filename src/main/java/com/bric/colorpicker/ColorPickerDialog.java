/*
 * @(#)ColorPickerDialog.java
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

import com.bric.colorpicker.parts.DialogFooter;
import com.bric.colorpicker.parts.EscapeKeyBehavior;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.JComponent;
import javax.swing.JDialog;

/**
 * This wraps a {@code ColorPicker} in a simple dialog with "OK" and "Cancel" options.
 * <P>(This object is used by the static calls in {@code ColorPicker} to show a dialog.)
 *
 * @see ColorPicker
 * @see ColorPickerPanel
 */
public class ColorPickerDialog extends JDialog {

    private static final String LOCALIZATION_BUNDLE_PATH = "com.bric.colorpicker.resources.ColorPickerDialog";
    public static final JComponent[] LEFT_COMPONENTS = new JComponent[0];

    private ColorPicker colorPicker;
    private Color returnValue;
    private final ActionListener okListener = new OkListener();

    public ColorPickerDialog() {
        this((Frame) null, Color.BLUE, false);
    }

    public ColorPickerDialog(Frame owner, Color color, boolean includeOpacity) {
        this(owner, color, includeOpacity, null);
    }

    public ColorPickerDialog(Dialog owner, Color color, boolean includeOpacity) {
        this(owner, color, includeOpacity, null);
    }

    public ColorPickerDialog(Locale locale) {
        this((Frame) null, Color.BLUE, false, locale);
    }

    public ColorPickerDialog(Frame owner, Color color, boolean includeOpacity, Locale locale) {
        super(owner);
        initialize(owner, color, includeOpacity, locale);
    }

    public ColorPickerDialog(Dialog owner, Color color, boolean includeOpacity, Locale locale) {
        super(owner);
        initialize(owner, color, includeOpacity, locale);
    }

    /**
     * This creates a modal dialog prompting the user to select a color.
     *
     * @param owner          the dialog this new dialog belongs to.  This must be a Frame or a Dialog.
     *                       Java 1.6 supports Windows here, but this package is designed/compiled to work in Java 1.4,
     *                       so an {@code IllegalArgumentException} will be thrown if this target is a {@code Window}.
     * @param title          the title for the dialog.
     * @param originalColor  the color the {@code ColorPicker} initially points to.
     * @param includeOpacity whether to add a control for the opacity of the color.
     * @param locale the current active local of the app
     * @return the {@code Color} the user chooses, or {@code null} if the user cancels the dialog.
     */
    public static Color showDialog(Window owner, String title, Color originalColor, boolean includeOpacity, Locale locale) {
        ColorPickerDialog dialog;
        ResourceBundle strings;

        if(locale == null) strings = ResourceBundle.getBundle(LOCALIZATION_BUNDLE_PATH);
        else strings = ResourceBundle.getBundle(LOCALIZATION_BUNDLE_PATH, locale);

        if (owner instanceof Frame || owner == null) {
            dialog = new ColorPickerDialog((Frame) owner, originalColor, includeOpacity, locale);
        } else if (owner instanceof Dialog) {
            dialog = new ColorPickerDialog((Dialog) owner, originalColor, includeOpacity, locale);
        } else {
            throw new IllegalArgumentException("the owner (" + owner.getClass().getName() + ") must be a java.awt.Frame or a java.awt.Dialog");
        }

        if (title == null) {
            dialog.setTitle(strings.getObject("ColorPickerDialogTitle").toString());
        } else {
            dialog.setTitle(title);
        }
        dialog.pack();
        dialog.setVisible(true);
        return dialog.returnValue;
    }


    /**
     * This creates a modal dialog prompting the user to select a color.
     * <P>This uses a generic dialog title: "Choose a Color".
     *
     * @param owner          the dialog this new dialog belongs to.  This must be a Frame or a Dialog.
     *                       Java 1.6 supports Windows here, but this package is designed/compiled to work in Java 1.4,
     *                       so an {@code IllegalArgumentException} will be thrown if this target is a {@code Window}.
     * @param originalColor  the color the {@code ColorPicker} initially points to.
     * @param includeOpacity whether to add a control for the opacity of the color.
     * @return the {@code Color} the user chooses, or {@code null} if the user cancels the dialog.
     */
    public static Color showDialog(Window owner, Color originalColor, boolean includeOpacity) {
        return showDialog(owner, null, originalColor, includeOpacity, null);
    }

        /**
     * This creates a modal dialog prompting the user to select a color.
     * <P>This uses a generic dialog title: "Choose a Color".
     *
     * @param owner          the dialog this new dialog belongs to.  This must be a Frame or a Dialog.
     *                       Java 1.6 supports Windows here, but this package is designed/compiled to work in Java 1.4,
     *                       so an {@code IllegalArgumentException} will be thrown if this target is a {@code Window}.
     * @param originalColor  the color the {@code ColorPicker} initially points to.
     * @param includeOpacity whether to add a control for the opacity of the color.
     * @param local the current active local of the app
     * @return the {@code Color} the user chooses, or {@code null} if the user cancels the dialog.
     */
    public static Color showDialog(Window owner, Color originalColor, boolean includeOpacity, Locale locale) {
        return showDialog(owner, null, originalColor, includeOpacity, locale);
    }

    /**
     * This creates a modal dialog prompting the user to select a color.
     * <P>This uses a generic dialog title: "Choose a Color", and does not include opacity.
     *
     * @param owner         the dialog this new dialog belongs to.  This must be a Frame or a Dialog.
     *                      Java 1.6 supports Windows here, but this package is designed/compiled to work in Java 1.4,
     *                      so an {@code IllegalArgumentException} will be thrown if this target is a {@code Window}.
     * @param originalColor the color the {@code ColorPicker} initially points to.
     * @return the {@code Color} the user chooses, or {@code null} if the user cancels the dialog.
     */
    public static Color showDialog(Window owner, Color originalColor) {
        return showDialog(owner, null, originalColor, false, null);
    }

    /**
     * This creates a modal dialog prompting the user to select a color.
     * <P>This uses a generic dialog title: "Choose a Color", and does not include opacity.
     *
     * @param owner         the dialog this new dialog belongs to.  This must be a Frame or a Dialog.
     *                      Java 1.6 supports Windows here, but this package is designed/compiled to work in Java 1.4,
     *                      so an {@code IllegalArgumentException} will be thrown if this target is a {@code Window}.
     * @param originalColor the color the {@code ColorPicker} initially points to.
     * @param locale  the current active local of the app
     * @return the {@code Color} the user chooses, or {@code null} if the user cancels the dialog.
     */
    public static Color showDialog(Window owner, Color originalColor, Locale locale) {
        return showDialog(owner, null, originalColor, false, locale);
    }

    private void initialize(Component owner, Color color, boolean includeOpacity, Locale locale) {

        colorPicker = new ColorPicker(true, includeOpacity, locale);
        setModal(true);
        setResizable(false);
        getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.insets = new Insets(10, 10, 10, 10);
        getContentPane().add(colorPicker, constraints);
        constraints.gridy++;
        DialogFooter footer = DialogFooter.createDialogFooter(LEFT_COMPONENTS,
                DialogFooter.OK_CANCEL_OPTION, DialogFooter.OK_OPTION, EscapeKeyBehavior.TRIGGERS_CANCEL, locale);
        constraints.gridy++;
        constraints.weighty = 0;
        getContentPane().add(footer, constraints);
        colorPicker.setColor(color);
        pack();
        setLocationRelativeTo(owner);

        footer.addOkButtonActionListener(okListener);
    }

    /**
     * @return the color committed when the user clicked 'OK'.  Note this returns {@code null}
     * if the user canceled this dialog, or exited via the close decoration.
     */
    public Color getColor() {
        return returnValue;
    }

    private class OkListener implements ActionListener {

        OkListener() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            returnValue = colorPicker.getColor();
        }
    }
}

