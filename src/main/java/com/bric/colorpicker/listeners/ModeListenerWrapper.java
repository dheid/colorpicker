package com.bric.colorpicker.listeners;

import com.bric.colorpicker.models.ModeModel;

public class ModeListenerWrapper extends SecuredListenerWrapper<ModeListener> implements ModeListener {

    private ModeModel model;

    public static ModeListenerWrapper withListener(ModeListener modeListener) {
        ModeListenerWrapper wrapper = new ModeListenerWrapper();
        wrapper.setListener(modeListener);
        return wrapper;
    }

    @Override
    protected void doValueChanged() {
        getListener().modeChanged(model);
    }

    @Override
    public void modeChanged(ModeModel modeModel) {
        model = modeModel;
        valueChanged();
    }

}
