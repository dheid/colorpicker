/*
 * @(#)ColorPickerDemo.java
 *
 * $Date$
 *
 * Copyright (c) 2011 by Jeremy Wood.
 * All rights reserved.
 *
 * The copyright of this software is owned by Jeremy Wood. 
 * You may not use, copy or modify this software, except in  
 * accordance with the license agreement you entered into with  
 * Jeremy Wood. For details see accompanying license terms.
 * 
 * This software is probably, but not necessarily, discussed here:
 * https://javagraphics.java.net/
 * 
 * That site should also contain the most recent official version
 * of this software.  (See the SVN repository for more details.)
 */
package com.bric.colorpicker.demo;

import com.bric.colorpicker.ColorPicker;
import com.bric.colorpicker.ColorPickerDialog;

import javax.swing.JButton;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;

public class ColorPickerFrame extends JFrame {

    public static final String FRAME_TITLE = "Color Picker Demo";

    private final JButton button = new JButton("Show Dialog");

    public ColorPickerFrame(ColorPicker picker) throws HeadlessException {
        super(FRAME_TITLE);
        setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;

        add(picker, c);
        c.gridy++;
        c.weighty = 0.0;

        add(picker.getExpertControls(), c);
        c.gridy++;
        c.fill = GridBagConstraints.NONE;

        add(button, c);
        button.addActionListener(e -> {
            Color color = picker.getColor();
            color = ColorPickerDialog.showDialog(this, color, true);
            if (color != null) {
                picker.setColor(color);
            }
        });

        pack();
        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
