/*
 * @(#)ContextualMenuHelper.java
 *
 * $Date$
 *
 * Copyright (c) 2013 by Jeremy Wood.
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
package com.bric.colorpicker.contextualmenu;

import com.bric.colorpicker.listeners.PopupListener;
import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

public class ContextualMenuImpl implements ContextualMenu {

    private final JPopupMenu popup = new JPopupMenu();

    /**
     * Install this contextual menu on the target provided.
     */
    @Override
    public void addComponent(JComponent jc) {
        jc.addMouseListener(new PopupListener(this));

        jc.putClientProperty(MENU_KEY, this);
    }

    @Override
    public JMenuItem add(String menuItemName, final Runnable runnable) {
        JMenuItem menuItem = new JMenuItem(menuItemName);
        menuItem.addActionListener(e -> SwingUtilities.invokeLater(runnable));
        popup.add(menuItem);
        return menuItem;
    }

    @Override
    public void showPopup(Component c, int x, int y) {
        popup.show(c, x, y);
    }

    @Override
    public void removeAll() {
        popup.removeAll();
    }

}
