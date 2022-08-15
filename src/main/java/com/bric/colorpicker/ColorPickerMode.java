package com.bric.colorpicker;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ColorPickerMode {
    HUE(360), BRIGHTNESS(100), SATURATION(100), RED(255), GREEN(255), BLUE(255), ALPHA(255);

    private final int max;

}
