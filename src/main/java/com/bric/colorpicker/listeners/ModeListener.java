package com.bric.colorpicker.listeners;

import com.bric.colorpicker.models.ModeModel;

@FunctionalInterface
public interface ModeListener {

    void modeChanged(ModeModel modeModel);

}
