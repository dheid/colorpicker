package com.bric.colorpicker.colorslider;

import static com.bric.colorpicker.ColorPickerMode.HUE;

import com.bric.colorpicker.ColorPickerMode;
import com.bric.colorpicker.listeners.ColorListener;
import com.bric.colorpicker.listeners.ColorListenerWrapper;
import com.bric.colorpicker.listeners.ModeListener;
import com.bric.colorpicker.models.ColorModel;
import com.bric.colorpicker.models.ModeModel;
import java.text.MessageFormat;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

public class ColorSlider extends JSlider implements ColorListener, ModeListener {

    private ColorPickerMode mode;
    private ColorModel colorModel;
    private final ColorListenerWrapper colorListenerWrapper;

    public ColorSlider() {
        super(SwingConstants.VERTICAL, 0, 100, 0);
        colorListenerWrapper = ColorListenerWrapper.withListener(colorModel -> updateValue());
    }

    @Override
    public void colorChanged(ColorModel colorModel) {
        this.colorModel = colorModel;
        if (mode != null) {
            colorListenerWrapper.colorChanged(colorModel);
        }
    }

    private void updateValue() {
        switch (mode) {
            case HUE:
                setValue((int) (colorModel.getHue() * mode.getMax()));
                break;
            case BRIGHTNESS:
                setValue((int) (colorModel.getBrightness() * mode.getMax()));
                break;
            case SATURATION:
                setValue((int) (colorModel.getSaturation() * mode.getMax()));
                break;
            case RED:
                setValue(colorModel.getRed());
                break;
            case GREEN:
                setValue(colorModel.getGreen());
                break;
            case BLUE:
                setValue(colorModel.getBlue());
                break;
            case ALPHA:
                setValue(colorModel.getAlpha());
                break;
            default:
                throw new IllegalStateException(MessageFormat.format("Mode not supported: {0}", mode));
        }
        repaint();
    }

    @Override
    public void modeChanged(ModeModel modeModel) {
        mode = modeModel.getMode();
        setInverted(mode == HUE);
        setMaximum(mode.getMax());

        if (colorModel != null) {
            aboutToChangeColor();
            updateValue();
        }
    }

    public void aboutToChangeColor() {
        colorListenerWrapper.aboutToChangeValue();
    }
}
