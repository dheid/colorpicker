package com.bric.colorpicker.listeners;

import com.bric.colorpicker.models.ColorModel;

public class ColorListenerWrapper extends SecuredListenerWrapper<ColorListener> implements ColorListener {

    private ColorModel model;

    public static ColorListenerWrapper withListener(ColorListener colorListener) {
        ColorListenerWrapper wrapper = new ColorListenerWrapper();
        wrapper.setListener(colorListener);
        return wrapper;
    }

    @Override
    protected void doValueChanged() {
        getListener().colorChanged(model);
    }

    @Override
    public void colorChanged(ColorModel colorModel) {
        model = colorModel;
        valueChanged();
    }

}
