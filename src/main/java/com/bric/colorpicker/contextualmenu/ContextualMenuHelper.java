package com.bric.colorpicker.contextualmenu;

import javax.swing.JComponent;

import static com.bric.colorpicker.contextualmenu.ContextualMenu.MENU_KEY;

public final class ContextualMenuHelper {

    private ContextualMenuHelper() {
    }

    /**
     * Clear any registered contextual menu information for this target.
     *
     * @param component the target to purge all contextual menu info for.
     */
    public static void clear(JComponent component) {
        ContextualMenu cm = (ContextualMenu) component.getClientProperty(MENU_KEY);
        if (cm == null) {
            return;
        }
        cm.removeAll();
    }

    /**
     * Add a menu item that invokes the argument Runnable when selected.
     *
     * @param component    The component to add
     * @param menuItemName The label of the context menu entry
     * @param runnable     this is invoked (via SwingUtilities.invokeLater()) when this menu
     *                     item is selected.
     */
    public static void add(JComponent component, String menuItemName, Runnable runnable) {
        ContextualMenu cmh = ContextualMenuHelper.getContextualMenuHelper(component);
        cmh.add(menuItemName, runnable);
    }

    /**
     * Return the ContextualMenu the static helper methods refer to.
     *
     * @param jc the target to retrieve a common ContextualMenu for.
     * @return the common ContextualMenu for the argument. This will create one
     * if it doesn't already exist.
     */
    private static ContextualMenu getContextualMenuHelper(JComponent jc) {
        ContextualMenu cmh = (ContextualMenu) jc.getClientProperty(MENU_KEY);
        if (cmh == null) {
            cmh = new ContextualMenuImpl();
            cmh.addComponent(jc);
            jc.putClientProperty(MENU_KEY, cmh);
        }
        return cmh;
    }

}
