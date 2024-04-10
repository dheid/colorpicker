package com.bric.colorpicker.options;

import static javax.swing.JSpinner.DefaultEditor;

import com.bric.colorpicker.ColorPickerMode;
import com.bric.colorpicker.listeners.ColorListener;
import com.bric.colorpicker.listeners.ColorListenerWrapper;
import com.bric.colorpicker.listeners.ModeListener;
import com.bric.colorpicker.listeners.ModeListenerWrapper;
import com.bric.colorpicker.models.ColorModel;
import com.bric.colorpicker.models.ModeModel;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.ButtonGroup;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeListener;

public abstract class Option implements ColorListener, ModeListener {

    protected static final String LOCALIZATION_BUNDLE_PATH = "com.bric.colorpicker.resources.ColorPicker";
    private final ColorListenerWrapper colorListenerWrapper;
    private final ModeListenerWrapper modeListenerWrapper;
    private final JRadioButton radioButton = new JRadioButton();
    private final JSpinner spinner;
    private final JLabel label;
    private final ColorPickerMode mode;

    private ResourceBundle strings;

    protected Option(String localizationKey, ColorPickerMode mode, Locale locale) {
        
        if(locale == null) this.strings = ResourceBundle.getBundle(LOCALIZATION_BUNDLE_PATH);
        else this.strings = ResourceBundle.getBundle(LOCALIZATION_BUNDLE_PATH, locale);

        this.mode = mode;
        spinner = new JSpinner(new SpinnerNumberModel(0, 0, mode.getMax(), 5));
        label = new JLabel(this.strings.getObject(localizationKey).toString());
        colorListenerWrapper = ColorListenerWrapper.withListener(this::doColorChanged);
        modeListenerWrapper = ModeListenerWrapper.withListener(modeModel -> setSelected(modeModel.getMode() == mode));
    }


    protected Option(String text, ColorPickerMode mode) {
        this.mode = mode;
        spinner = new JSpinner(new SpinnerNumberModel(0, 0, mode.getMax(), 5));
        label = new JLabel(text);
        colorListenerWrapper = ColorListenerWrapper.withListener(this::doColorChanged);
        modeListenerWrapper = ModeListenerWrapper.withListener(modeModel -> setSelected(modeModel.getMode() == mode));
    }

    protected abstract void doColorChanged(ColorModel colorModel);

    @Override
    public void colorChanged(ColorModel colorModel) {
        colorListenerWrapper.colorChanged(colorModel);
    }

    public void aboutToChangeColor() {
        colorListenerWrapper.aboutToChangeValue();
    }

    protected JSpinner getSpinner() {
        return spinner;
    }

    protected JLabel getLabel() {
        return label;
    }

    public void addRadioActionListener(ActionListener listener) {
        radioButton.addActionListener(listener);
    }

    public void addSpinnerChangeListener(ChangeListener listener) {
        spinner.addChangeListener(listener);
    }

    public void setSelected(boolean b) {
        radioButton.setSelected(b);
    }

    public void setName(String name) {
        spinner.setName(name);
    }

    public void setValue(int i) {
        spinner.setValue(i);
    }

    public void addTo(Container container, GridBagConstraints constraints) {
        addTo(container, constraints, null);
    }

    public void addTo(Container container, GridBagConstraints constraints, ButtonGroup buttonGroup) {
        constraints.anchor = GridBagConstraints.LINE_END;
        constraints.fill = GridBagConstraints.NONE;
        container.add(label, constraints);
        constraints.gridx++;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        container.add(spinner, constraints);
        constraints.gridx++;
        constraints.fill = GridBagConstraints.NONE;
        container.add(radioButton, constraints);
        constraints.gridy++;
        constraints.gridx = 0;
        buttonGroup.add(radioButton);
    }

    public int getMax() {
        return mode.getMax();
    }

    public int getValue() {
        return ((Number) spinner.getValue()).intValue();
    }

    public boolean isVisible() {
        return label.isVisible();
    }

    public void setRadioButtonVisible(boolean visible) {
        radioButton.setVisible(visible);
    }

    public void setLabelVisible(boolean visible) {
        label.setVisible(visible);
    }

    public void setSpinnerVisible(boolean visible) {
        spinner.setVisible(visible);
    }

    public void setVisible(boolean visible, boolean radioButtonsAllowed) {
        setRadioButtonVisible(visible && radioButtonsAllowed);
        if (spinner != null) {
            spinner.setVisible(visible);
        }
        label.setVisible(visible);
    }

    private JFormattedTextField getTextField() {
        return ((DefaultEditor) spinner.getEditor()).getTextField();
    }

    public void addFocusListener(FocusListener listener) {
        getTextField().addFocusListener(listener);
    }

    public void update(ModeModel modeModel) {
        modeModel.setMode(mode);
    }

    public abstract void update(ColorModel colorModel);

    @Override
    public void modeChanged(ModeModel modeModel) {
        modeListenerWrapper.modeChanged(modeModel);
    }

    public void aboutToChangeMode() {
        modeListenerWrapper.aboutToChangeValue();
    }
}
