package com.bric.colorpicker.colorslider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.bric.colorpicker.ColorPicker;
import javax.swing.JSlider;
import javax.swing.plaf.basic.BasicSliderUI.TrackListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ColorSliderUITest {

    private final JSlider slider = new JSlider();

    private final ColorPicker colorPicker = new ColorPicker();

    private ColorSliderUI colorSliderUI;

    @BeforeEach
    void setUp() {
        colorSliderUI = new ColorSliderUI(slider, colorPicker);
    }

    @Test
    void expectsNonNullSlider() {

        assertThatThrownBy(() -> new ColorSliderUI(null, colorPicker))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("slider is marked non-null but is null");

    }

    @Test
    void expectsNonNullColorPicker() {

        assertThatThrownBy(() -> new ColorSliderUI(slider, null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("colorPicker is marked non-null but is null");

    }

    @Test
    void installsListeners() {

        colorSliderUI.installListeners(slider);

        assertThat(slider.getMouseListeners()[0]).isInstanceOf(TrackListener.class);
        assertThat(slider.getMouseListeners()[1]).isInstanceOf(SliderMouseListener.class);
        assertThat(slider.getMouseMotionListeners()[0]).isInstanceOf(TrackListener.class);
        assertThat(slider.getMouseMotionListeners()[1]).isInstanceOf(SliderMouseListener.class);

    }

    @Test
    void uninstallsListeners() {

        colorSliderUI.installListeners(slider);

        colorSliderUI.uninstallListeners(slider);

        assertThat(slider.getMouseListeners()).hasSize(1);
        assertThat(slider.getMouseMotionListeners()).hasSize(1);

    }
}
