package com.bric.colorpicker.contextualmenu;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import java.awt.Component;

public interface ContextualMenu {

    String MENU_KEY = ContextualMenu.class.getName() + ".menuKey";

    void addComponent(JComponent jc);

    JMenuItem add(String menuItemName, Runnable runnable);

    void showPopup(Component c, int x, int y);

    void removeAll();
}
