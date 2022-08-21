package com.bric.colorpicker.colorslider;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.bric.colorpicker.ColorPicker;
import javax.swing.JSlider;
import javax.swing.plaf.basic.BasicSliderUI;
import org.junit.jupiter.api.Test;

class SliderMouseListenerTest {

    private final JSlider slider = new JSlider();

    @Test
    void ensuresSliderUiIsNotNull() {

        assertThatThrownBy(() -> new SliderMouseListener(null, slider))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("sliderUi is marked non-null but is null");

    }

    @Test
    void ensuresSliderIsNotNull() {

        ColorPicker colorPicker = new ColorPicker();
        BasicSliderUI sliderUi = new ColorSliderUI(slider, colorPicker);

        assertThatThrownBy(() -> new SliderMouseListener(sliderUi, null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("slider is marked non-null but is null");

    }

}
