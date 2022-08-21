package com.bric.colorpicker.colorslider;

import java.awt.event.MouseEvent;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.MouseInputAdapter;
import javax.swing.plaf.basic.BasicSliderUI;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SliderMouseListener extends MouseInputAdapter {

    @NonNull
    private final BasicSliderUI sliderUi;

    @NonNull
    private final JSlider slider;

    @Override
    public void mousePressed(MouseEvent e) {
        slider.setValueIsAdjusting(true);
        updateSliderValue(e);
    }

    private void updateSliderValue(MouseEvent e) {
        int v;
        if (slider.getOrientation() == SwingConstants.HORIZONTAL) {
            int x = e.getX();
            v = sliderUi.valueForXPosition(x);
        } else {
            int y = e.getY();
            v = sliderUi.valueForYPosition(y);
        }
        slider.setValue(v);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        updateSliderValue(e);
        slider.setValueIsAdjusting(false);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        updateSliderValue(e);
    }
}
