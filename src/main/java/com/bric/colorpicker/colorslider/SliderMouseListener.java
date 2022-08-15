package com.bric.colorpicker.colorslider;

import java.awt.event.MouseEvent;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.MouseInputAdapter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SliderMouseListener extends MouseInputAdapter {

    private final ColorSliderUI colorSliderUI;

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
            v = colorSliderUI.valueForXPosition(x);
        } else {
            int y = e.getY();
            v = colorSliderUI.valueForYPosition(y);
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
