/*
 * @(#)DialogFooter.java
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
package com.bric.colorpicker.parts;

import com.bric.colorpicker.JVM;
import com.bric.colorpicker.listeners.FocusArrowListener;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/**
 * This is a row of buttons, intended to be displayed at the
 * bottom of a dialog.
 * <P>On the left of a footer are controls that should apply to the dialog itself,
 * such as "Help" button, or a "Reset Preferences" button.
 * On the far right are buttons that should dismiss this dialog.  They
 * may be presented in different orders on different platforms based
 * on the {@code reverseButtonOrder} boolean.
 * <P>Buttons are also generally normalized, so the widths of buttons
 * are equal.
 * <P>This object will "latch onto" the RootPane that contains it.  It is assumed
 * two DialogFooters will not be contained in the same RootPane. It is also
 * assumed the same DialogFooter will not be passed around to several
 * different RootPanes.
 * <h2>Preset Options</h2>
 * This class has several OPTION constants to create specific buttons.
 * <P>In each constant the first option is the default button unless
 * you specify otherwise.  The Apple Interface Guidelines advises:
 * "The default button should be the button that represents the
 * action that the user is most likely to perform if that action isn't
 * potentially dangerous."
 * <P>The YES_NO options should be approached with special reluctance.
 * Microsoft <A HREF="http://msdn.microsoft.com/en-us/library/aa511331.aspx">cautions</A>,
 * "Use Yes and No buttons only to respond to yes or no questions."  This seems
 * obvious enough, but Apple adds, "Button names should correspond to the action
 * the user performs when pressing the button-for example, Erase, Save, or Delete."
 * So instead of presenting a YES_NO dialog with the question "Do you want to continue?"
 * a better dialog might provide the options "Cancel" and "Continue".  In short: we
 * as developers might tend to lazily use this option and phrase dialogs in such
 * a way that yes/no options make sense, but in fact the commit buttons should be
 * more descriptive.
 * <P>Partly because of the need to avoid yes/no questions, {@code DialogFooter} introduces the
 * dialog type: SAVE_DONT_SAVE_CANCEL_OPTION.  This is mostly straightforward, but
 * there is one catch: on Mac the buttons
 * are reordered: "Save", "Cancel" and "Don't Save".  This is to conform with standard
 * Mac behavior.  (Or, more specifically: because the Apple guidelines
 * state that a button that can cause permanent data loss be as physically far
 * from a "safe" button as possible.)  On all other platforms the buttons are
 * listed in the order "Save", "Don't Save" and "Cancel".
 * <P>Also note the field {@link #reverseButtonOrder}
 * controls the order each option is presented in the dialog from left-to-right.
 * <h2>Platform Differences</h2>
 * These are based mostly on studying Apple and Vista interface guidelines.
 * <ul><LI> On Mac, command-period acts like the escape key in dialogs.</li>
 * <LI> By default button order is reversed on Macs compared to other platforms.  See
 * the {@code DialogFooter.reverseButtonOrder} field for details.</li>
 * <LI> There is a static boolean to control whether button mnemonics should be
 * universally activated.  This was added because
 * when studying Windows XP there seemed to be no fixed rules for whether to
 * use mnemonics or not.  (Some dialogs show them, some dialogs don't.)  So I
 * leave it to your discretion to activate them.  I think this boolean should never be
 * activated on Vista or Mac, but on XP and Linux flavors: that's up to you.  (Remember
 * using the alt key usually activates the mnemonics in most Java look-and-feels, so just
 * because they aren't universally active doesn't mean you're hurting accessibility needs.)</LI></ul>
 */
public class DialogFooter extends JPanel {
    /**
     * This client property is used to impose a meta-shortcut to click a button.
     * This should map to a Character.
     */
    private static final String PROPERTY_META_SHORTCUT = "Dialog.meta.shortcut";
    /**
     * This client property is used to indicate a button is "unsafe".  Apple
     * guidelines state that "unsafe" buttons (such as "discard changes") should
     * be several pixels away from "safe" buttons.
     */
    private static final String PROPERTY_UNSAFE = "Dialog.Unsafe.Action";
    private static final KeyStroke escapeKey = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
    private static final KeyStroke commandPeriodKey = KeyStroke.getKeyStroke(KeyEvent.VK_PERIOD, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());

    /**
     * Path to the localization bundle
     */
    private static final String LOCALIZATION_BUNDLE_PATH = "com.bric.colorpicker.resources.DialogFooter";

    /**
     * This is the client property of buttons created in static methods by this class.
     */
    private static final String PROPERTY_OPTION = "DialogFooter.propertyOption";
    private static int uniqueCtr;
    /**
     * Used to indicate the user selected "Cancel" in a dialog.
     * <BR>Also this can be used as a dialog type, to indicate that "Cancel" should
     * be the only option presented to the user.
     * <P>Note the usage is similar to JOptionPane's, but the numerical value is
     * different, so you cannot substitute JOptionPane.CANCEL_OPTION for DialogFooter.CANCEL_OPTION.
     */
    private static final int CANCEL_OPTION = uniqueCtr++;
    /**
     * Used to indicate the user selected "OK" in a dialog.
     * <BR>Also this can be used as a dialog type, to indicate that "OK" should
     * be the only option presented to the user.
     * <P>Note the usage is similar to JOptionPane's, but the numerical value is
     * different, so you cannot substitute JOptionPane.OK_OPTION for DialogFooter.OK_OPTION.
     */
    public static final int OK_OPTION = uniqueCtr++;
    /**
     * Used to indicate the user selected "No" in a dialog.
     * <BR>Also this can be used as a dialog type, to indicate that "No" should
     * be the only option presented to the user.
     * <P>Note the usage is similar to JOptionPane's, but the numerical value is
     * different, so you cannot substitute JOptionPane.NO_OPTION for DialogFooter.NO_OPTION.
     */
    private static final int NO_OPTION = uniqueCtr++;
    /**
     * Used to indicate the user selected "Yes" in a dialog.
     * <BR>Also this can be used as a dialog type, to indicate that "Yes" should
     * be the only option presented to the user.
     * <P>Note the usage is similar to JOptionPane's, but the numerical value is
     * different, so you cannot substitute JOptionPane.YES_OPTION for DialogFooter.YES_OPTION.
     */
    private static final int YES_OPTION = uniqueCtr++;
    /**
     * Used to indicate a dialog should present a "Yes" and "No" option.
     * <P>Note the usage is similar to JOptionPane's, but the numerical value is
     * different, so you cannot substitute JOptionPane.YES_NO_OPTION for DialogFooter.YES_NO_OPTION.
     */
    private static final int YES_NO_OPTION = uniqueCtr++;
    /**
     * Used to indicate a dialog should present a "Yes", "No", and "Cancel" option.
     * <P>Note the usage is similar to JOptionPane's, but the numerical value is
     * different, so you cannot substitute JOptionPane.YES_NO_CANCEL_OPTION for DialogFooter.YES_NO_CANCEL_OPTION.
     */
    private static final int YES_NO_CANCEL_OPTION = uniqueCtr++;
    /**
     * Used to indicate a dialog should present a "OK" and "Cancel" option.
     * <P>Note the usage is similar to JOptionPane's, but the numerical value is
     * different, so you cannot substitute JOptionPane.OK_CANCEL_OPTION for DialogFooter.OK_CANCEL_OPTION.
     */
    public static final int OK_CANCEL_OPTION = uniqueCtr++;
    /**
     * Used to indicate a dialog should present a "Save", "Don't Save", and "Cancel" option.
     */
    private static final int SAVE_DONT_SAVE_CANCEL_OPTION = uniqueCtr++;
    /**
     * Used to indicate a dialog should present a "Don't Save" and "Save" option.
     * This will be used for QOptionPaneCommon.FILE_EXTERNAL_CHANGES.
     */
    private static final int DONT_SAVE_SAVE_OPTION = uniqueCtr++;
    /**
     * Used to indicate the user selected "Save" in a dialog.
     * <BR>Also this can be used as a dialog type, to indicate that "Save"
     * should be the only option presented to the user.
     */
    private static final int SAVE_OPTION = uniqueCtr++;
    /**
     * Used to indicate the user selected "Don't Save" in a dialog.
     * <BR>Also this can be used as a dialog type, to indicate that "Don't Save"
     * should be the only option presented to the user.
     */
    private static final int DONT_SAVE_OPTION = uniqueCtr++;

    /**
     * The localized STRINGS used in dialogs.
     */
    private static ResourceBundle strings = ResourceBundle.getBundle(LOCALIZATION_BUNDLE_PATH);

    /**
     * Used to indicate the user selected an option not otherwise
     * specified in this set of constants.  It may be possible
     * that the user closed this dialog with the close decoration,
     * or else another agent dismissed this dialog.
     * <p>If you use a safely predesigned set of options this
     * will not be used.
     */
    private static final AncestorListener escapeTriggerListener = new AncestorListener() {

        @Override
        public void ancestorAdded(AncestorEvent event) {
            JButton button = (JButton) event.getComponent();
            Window w = SwingUtilities.getWindowAncestor(button);
            if (w instanceof RootPaneContainer) {
                setRootPaneContainer(button, (RootPaneContainer) w);
            } else {
                setRootPaneContainer(button, null);
            }
        }

        private void setRootPaneContainer(JButton button, RootPaneContainer c) {
            RootPaneContainer lastContainer = (RootPaneContainer) button.getClientProperty("bric.footer.rpc");
            if (Objects.equals(lastContainer, c)) {
                return;
            }

            if (lastContainer != null) {
                lastContainer.getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).remove(
                    escapeKey);
                lastContainer.getRootPane().getActionMap().remove(escapeKey);

                if (JVM.IS_MAC) {
                    lastContainer.getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).remove(
                        commandPeriodKey);
                }

            }

            if (c != null) {
                c.getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(
                    escapeKey, escapeKey);
                c.getRootPane().getActionMap().put(escapeKey, new ClickAction(button));

                if (JVM.IS_MAC) {
                    c.getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(
                        commandPeriodKey, escapeKey);
                }
            }
            button.putClientProperty("bric.footer.rpc", c);
        }

        @Override
        public void ancestorMoved(AncestorEvent event) {
            ancestorAdded(event);
        }

        @Override
        public void ancestorRemoved(AncestorEvent event) {
            ancestorAdded(event);

        }

    };
    /**
     * This indicates whether the dismiss controls should be displayed in reverse
     * order.  When you construct a DialogFooter, the dismiss controls should be listed
     * in order of priority (with the most preferred listed first, the least preferred last).
     * If this boolean is false, then those components will be listed in that order.  If this is
     * true, then those components will be listed in the reverse order.
     * <P>By default on Mac this is true, because Macs put the default button on the right
     * side of dialogs.  On all other platforms this is false by default.
     * <P>Window's <A HREF="http://msdn.microsoft.com/en-us/library/ms997497.aspx">guidelines</A>
     * advise to, "Position the most important button -- typically the default command --
     * as the first button in the set."
     */
    private static final boolean reverseButtonOrder = JVM.IS_MAC;
    /**
     * This action takes the Window associated with the source of this event,
     * hides it, and then calls {@code dispose()} on it.
     * <P>(This will not throw an exception if there is no parent window,
     * but it does nothing in that case...)
     */
    private static final ActionListener closeDialogAndDisposeAction = new DialogFooter.CloseDialogAndDisposeAction();
    private final ActionListener innerActionListener = new InnerActionListener();
    private final JComponent[] leftControls;
    private final JComponent[] dismissControls;
    private final boolean autoClose;
    private final JButton defaultButton;
    /**
     * This addresses code that must involve the parent RootPane and Window.
     */
    private final HierarchyListener hierarchyListener = new HierarchyListener() {
        @Override
        public void hierarchyChanged(HierarchyEvent e) {
            processRootPane();
            processWindow();
        }

        private void processRootPane() {
            JRootPane root = SwingUtilities.getRootPane(DialogFooter.this);
            if (root == null) {
                return;
            }
            root.setDefaultButton(defaultButton);


            for (JComponent dismissControl : dismissControls) {
                if (dismissControl instanceof JButton) {
                    Character ch = (Character) dismissControl.getClientProperty(
                        PROPERTY_META_SHORTCUT);
                    if (ch != null) {
                        KeyStroke keyStroke = KeyStroke.getKeyStroke(ch.charValue(), Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
                        root.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(keyStroke, keyStroke);
                        root.getActionMap().put(keyStroke, new ClickAction((JButton) dismissControl));
                    }
                }
            }
        }

        private void processWindow() {
            Window window = SwingUtilities.getWindowAncestor(DialogFooter.this);
            if (window == null) {
                return;
            }

            window.setFocusTraversalPolicy(new DelegateFocusTraversalPolicy(window.getFocusTraversalPolicy()) {

                @Override
                public Component getDefaultComponent(Container focusCycleRoot) {
                    /* If the default target would naturally be in the footer *anyway*:
                     * Make sure the default target is the default button.
                     *
                     * However if the default target lies elsewhere (a text field or
                     * check box in the dialog): that should retain the default focus.
                     *
                     */
                    Component defaultComponent = super.getDefaultComponent(focusCycleRoot);
                    if (isAncestorOf(defaultComponent)) {
                        JButton button = defaultButton;
                        if (button != null && button.isShowing() && button.isEnabled() && button.isFocusable()) {
                            return button;
                        }
                    }
                    return defaultComponent;
                }
            });
        }
    };
    private int buttonGap;
    private int unsafeButtonGap;

        /**
     * Create a new {@code DialogFooter}.
     *
     * @param leftControls    the controls on the left side of this dialog, such as a help target, or a "Reset" button.
     * @param dismissControls the controls on the right side of this dialog that should dismiss this dialog.  Also
     *                        called "action" buttons.
     * @param autoClose       whether the dismiss buttons should automatically close the containing window.
     *                        If this is {@code false}, then it is assumed someone else is taking care of closing/disposing the
     *                        containing dialog
     * @param defaultButton   the optional button in {@code dismissControls} to make the default button in this dialog.
     *                        (May be null.)
     */
    private DialogFooter(JComponent[] leftControls, JComponent[] dismissControls, boolean autoClose, JButton defaultButton) {
        this(leftControls, dismissControls, autoClose, defaultButton, null);
    }

    /**
     * Create a new {@code DialogFooter}.
     *
     * @param leftControls    the controls on the left side of this dialog, such as a help target, or a "Reset" button.
     * @param dismissControls the controls on the right side of this dialog that should dismiss this dialog.  Also
     *                        called "action" buttons.
     * @param autoClose       whether the dismiss buttons should automatically close the containing window.
     *                        If this is {@code false}, then it is assumed someone else is taking care of closing/disposing the
     *                        containing dialog
     * @param defaultButton   the optional button in {@code dismissControls} to make the default button in this dialog.
     *                        (May be null.)
     * @param local the current active local of the app
     */
    private DialogFooter(JComponent[] leftControls, JComponent[] dismissControls, boolean autoClose, JButton defaultButton, Locale locale) {
        super(new GridBagLayout());

        if(locale == null) strings = ResourceBundle.getBundle(LOCALIZATION_BUNDLE_PATH);
        else strings = ResourceBundle.getBundle(LOCALIZATION_BUNDLE_PATH, locale);

        this.autoClose = autoClose;
        //this may be common:
        if (leftControls == null) {
            leftControls = new JComponent[]{};
        }
        //erg, this shouldn't be, but let's not throw an error because of it?
        if (dismissControls == null) {
            dismissControls = new JComponent[]{};
        }
        this.leftControls = copy(leftControls);
        this.dismissControls = copy(dismissControls);
        this.defaultButton = defaultButton;

        for (int a = 0; a < dismissControls.length; a++) {
            dismissControls[a].putClientProperty("dialog.footer.index", a);
            if (dismissControls[a] instanceof JButton) {
                ((AbstractButton) dismissControls[a]).addActionListener(innerActionListener);
            } else {
                //think of things like the JLink: it a label, but it has an ActionListener models
                try {
                    Class<?> cl = dismissControls[a].getClass();
                    Method m = cl.getMethod("addRadioActionListener", ActionListener.class);
                    m.invoke(dismissControls[a], innerActionListener);
                } catch (Throwable t) {
                    //do nothing
                }
            }
        }

        addHierarchyListener(hierarchyListener);

        for (JComponent leftControl : leftControls) {
            addFocusArrowListener(leftControl);
        }
        for (JComponent dismissControl : dismissControls) {
            addFocusArrowListener(dismissControl);
        }

        if (JVM.IS_MAC) {
            setButtonGap(12);
        } else {
            setButtonGap(6);
        }
        setUnsafeButtonGap(24);

        installGUI();
    }

    /**
     * Creates a new "Cancel" button.
     *
     * @param escapeKeyIsTrigger if true then pressing the escape
     *                           key will trigger this button.  (Also on Macs command-period will act
     *                           like the escape key.)  This should be {@code false} if this button
     *                           can lead to permanent data loss.
     */
    private static JButton createCancelButton(boolean escapeKeyIsTrigger) {
        JButton button = new JButton(strings.getString("dialogCancelButton"));
        button.setMnemonic(strings.getString("dialogCancelMnemonic").charAt(0));
        button.putClientProperty(PROPERTY_OPTION, CANCEL_OPTION);
        if (escapeKeyIsTrigger) {
            makeEscapeKeyActivate(button);
        }
        return button;
    }

    /**
     * This guarantees that when the escape key is pressed
     * (if its parent window has the keyboard focus) this button
     * is clicked.
     * <p>It is assumed that no two buttons will try to consume
     * escape keys in the same window.
     *
     * @param button the button to trigger when the escape key is pressed.
     */
    private static void makeEscapeKeyActivate(AbstractButton button) {
        button.addAncestorListener(escapeTriggerListener);
    }

     /**
     * Creates a new "OK" button.
     *
     * @param escapeKeyIsTrigger if true then pressing the escape
     *                           key will trigger this button.  (Also on Macs command-period will act
     *                           like the escape key.)  This should be {@code false} if this button
     *                           can lead to permanent data loss.
     */
    private static JButton createOKButton(boolean escapeKeyIsTrigger) {
        JButton button = new JButton(strings.getString("dialogOKButton"));
        button.setMnemonic(strings.getString("dialogOKMnemonic").charAt(0));
        button.putClientProperty(PROPERTY_OPTION, OK_OPTION);
        if (escapeKeyIsTrigger) {
            makeEscapeKeyActivate(button);
        }
        return button;
    }

    /**
     * Creates a new "Yes" button.
     *
     * @param escapeKeyIsTrigger if true then pressing the escape
     *                           key will trigger this button.  (Also on Macs command-period will act
     *                           like the escape key.)  This should be {@code false} if this button
     *                           can lead to permanent data loss.
     */
    private static JButton createYesButton(boolean escapeKeyIsTrigger) {
        JButton button = new JButton(strings.getString("dialogYesButton"));
        button.setMnemonic(strings.getString("dialogYesMnemonic").charAt(0));
        button.putClientProperty(PROPERTY_OPTION, YES_OPTION);
        if (escapeKeyIsTrigger) {
            makeEscapeKeyActivate(button);
        }
        return button;
    }


    /**
     * Creates a new "No" button.
     *
     * @param escapeKeyIsTrigger if true then pressing the escape
     *                           key will trigger this button.  (Also on Macs command-period will act
     *                           like the escape key.)  This should be {@code false} if this button
     *                           can lead to permanent data loss.
     */
    private static JButton createNoButton(boolean escapeKeyIsTrigger) {
        JButton button = new JButton(strings.getString("dialogNoButton"));
        button.setMnemonic(strings.getString("dialogNoMnemonic").charAt(0));
        button.putClientProperty(PROPERTY_OPTION, NO_OPTION);
        if (escapeKeyIsTrigger) {
            makeEscapeKeyActivate(button);
        }
        return button;
    }


    /**
     * Creates a new "Save" button.
     *
     * @param escapeKeyIsTrigger if true then pressing the escape
     *                           key will trigger this button.  (Also on Macs command-period will act
     *                           like the escape key.)  This should be {@code false} if this button
     *                           can lead to permanent data loss.
     */
    private static JButton createSaveButton(boolean escapeKeyIsTrigger) {
        JButton button = new JButton(strings.getString("dialogSaveButton"));
        button.setMnemonic(strings.getString("dialogSaveMnemonic").charAt(0));
        button.putClientProperty(PROPERTY_OPTION, SAVE_OPTION);
        if (escapeKeyIsTrigger) {
            makeEscapeKeyActivate(button);
        }
        return button;
    }


    /**
     * Creates a new "Don't Save" button.
     *
     * @param escapeKeyIsTrigger if true then pressing the escape
     *                           key will trigger this button.  (Also on Macs command-period will act
     *                           like the escape key.)  This should be {@code false} if this button
     *                           can lead to permanent data loss.
     */
    private static JButton createDontSaveButton(boolean escapeKeyIsTrigger) {
        String text = strings.getString("dialogDontSaveButton");
        JButton button = new JButton(text);
        button.setMnemonic(strings.getString("dialogDontSaveMnemonic").charAt(0));
        button.putClientProperty(PROPERTY_OPTION, DONT_SAVE_OPTION);
        //Don't know if this documented by Apple, but command-D usually triggers "Don't Save" buttons:
        button.putClientProperty(PROPERTY_META_SHORTCUT, text.charAt(0));
        if (escapeKeyIsTrigger) {
            makeEscapeKeyActivate(button);
        }
        return button;
    }

      /**
     * Creates a {@code DialogFooter}.
     *
     * @param leftComponents    the components to put on the left side of the footer.
     *                          <P>The Apple guidelines state that this area is reserved for
     *                          "button[s] that affect the contents of the dialog itself, such as Reset [or Help]".
     * @param options           one of the OPTIONS fields in this class, such as YES_NO_OPTION or CANCEL_OPTION.
     * @param defaultButton     the OPTION field corresponding to the button that
     *                          should be the default button, or -1 if there should be no default button.
     * @param escapeKeyBehavior one of the EscapeKeyBehavior options in this class.
     * @return a {@code DialogFooter}
     */
    public static DialogFooter createDialogFooter(JComponent[] leftComponents, int options, int defaultButton, EscapeKeyBehavior escapeKeyBehavior) {
        return createDialogFooter(leftComponents, options, defaultButton, escapeKeyBehavior, null);
    }

    /**
     * Creates a {@code DialogFooter}.
     *
     * @param leftComponents    the components to put on the left side of the footer.
     *                          <P>The Apple guidelines state that this area is reserved for
     *                          "button[s] that affect the contents of the dialog itself, such as Reset [or Help]".
     * @param options           one of the OPTIONS fields in this class, such as YES_NO_OPTION or CANCEL_OPTION.
     * @param defaultButton     the OPTION field corresponding to the button that
     *                          should be the default button, or -1 if there should be no default button.
     * @param escapeKeyBehavior one of the EscapeKeyBehavior options in this class.
     * @param local the current active local of the app
     * @return a {@code DialogFooter}
     */
    public static DialogFooter createDialogFooter(JComponent[] leftComponents, int options, int defaultButton, EscapeKeyBehavior escapeKeyBehavior, Locale locale) {

        if (escapeKeyBehavior == EscapeKeyBehavior.TRIGGERS_NONDEFAULT) {
            int buttonCount = 1;
            if (options == OK_CANCEL_OPTION || options == YES_NO_OPTION || options == DONT_SAVE_SAVE_OPTION) {
                buttonCount = 2;
            } else if (options == SAVE_DONT_SAVE_CANCEL_OPTION || options == YES_NO_CANCEL_OPTION) {
                buttonCount = 3;
            }
            if (defaultButton != -1) {
                buttonCount--;
            }
            if (buttonCount > 1) {
                throw new IllegalArgumentException("request for escape key to map to " + buttonCount + " buttons.");
            }
        }

        if(locale == null) strings = ResourceBundle.getBundle(LOCALIZATION_BUNDLE_PATH);
        else strings = ResourceBundle.getBundle(LOCALIZATION_BUNDLE_PATH, locale);

        JButton cancelButton = null;
        if (options == CANCEL_OPTION ||
            options == OK_CANCEL_OPTION ||
            options == SAVE_DONT_SAVE_CANCEL_OPTION ||
            options == YES_NO_CANCEL_OPTION) {
            cancelButton = createCancelButton(escapeKeyBehavior == EscapeKeyBehavior.TRIGGERS_CANCEL ||
                escapeKeyBehavior == EscapeKeyBehavior.TRIGGERS_NONDEFAULT && defaultButton != CANCEL_OPTION ||
                defaultButton == CANCEL_OPTION &&
                    escapeKeyBehavior == EscapeKeyBehavior.TRIGGERS_DEFAULT);
        }
        JButton dontSaveButton = null;
        if (options == DONT_SAVE_OPTION || options == SAVE_DONT_SAVE_CANCEL_OPTION || options == DONT_SAVE_SAVE_OPTION) {
            dontSaveButton = createDontSaveButton(
                escapeKeyBehavior == EscapeKeyBehavior.TRIGGERS_NONDEFAULT && defaultButton != DONT_SAVE_OPTION ||
                    escapeKeyBehavior == EscapeKeyBehavior.TRIGGERS_DEFAULT && defaultButton == DONT_SAVE_OPTION);
        }
        JButton noButton = null;
        if (options == NO_OPTION || options == YES_NO_OPTION || options == YES_NO_CANCEL_OPTION) {
            noButton = createNoButton(
                escapeKeyBehavior == EscapeKeyBehavior.TRIGGERS_NONDEFAULT && defaultButton != NO_OPTION ||
                    escapeKeyBehavior == EscapeKeyBehavior.TRIGGERS_DEFAULT && defaultButton == NO_OPTION);
        }
        JButton okButton = null;
        if (options == OK_OPTION ||
            options == OK_CANCEL_OPTION) {
            okButton = createOKButton(
                escapeKeyBehavior == EscapeKeyBehavior.TRIGGERS_NONDEFAULT && defaultButton != OK_OPTION ||
                    escapeKeyBehavior == EscapeKeyBehavior.TRIGGERS_DEFAULT && defaultButton == OK_OPTION);
        }
        JButton saveButton = null;
        if (options == SAVE_OPTION || options == SAVE_DONT_SAVE_CANCEL_OPTION || options == DONT_SAVE_SAVE_OPTION) {
            saveButton = createSaveButton(
                escapeKeyBehavior == EscapeKeyBehavior.TRIGGERS_NONDEFAULT && defaultButton != SAVE_OPTION ||
                    escapeKeyBehavior == EscapeKeyBehavior.TRIGGERS_DEFAULT && defaultButton == SAVE_OPTION);
        }
        JButton yesButton = null;
        if (options == YES_OPTION || options == YES_NO_OPTION || options == YES_NO_CANCEL_OPTION) {
            yesButton = createYesButton(
                escapeKeyBehavior == EscapeKeyBehavior.TRIGGERS_NONDEFAULT && defaultButton != YES_OPTION ||
                    escapeKeyBehavior == EscapeKeyBehavior.TRIGGERS_DEFAULT && defaultButton == YES_OPTION);
        }

        JButton[] dismissControls;
        if (options == CANCEL_OPTION) {
            dismissControls = new JButton[]{cancelButton};
        } else if (options == DONT_SAVE_OPTION) {
            dismissControls = new JButton[]{dontSaveButton};
        } else if (options == NO_OPTION) {
            dismissControls = new JButton[]{noButton};
        } else if (options == OK_CANCEL_OPTION) {
            dismissControls = new JButton[]{okButton, cancelButton};
        } else if (options == OK_OPTION) {
            dismissControls = new JButton[]{okButton};
        } else if (options == DONT_SAVE_SAVE_OPTION) {
            dismissControls = new JButton[]{dontSaveButton, saveButton};
        } else if (options == SAVE_DONT_SAVE_CANCEL_OPTION) {
            setUnsafe(dontSaveButton, true);
            dismissControls = new JButton[]{saveButton, dontSaveButton, cancelButton};
        } else if (options == SAVE_OPTION) {
            dismissControls = new JButton[]{saveButton};
        } else if (options == YES_NO_CANCEL_OPTION) {
            dismissControls = new JButton[]{yesButton, noButton, cancelButton};
        } else if (options == YES_NO_OPTION) {
            dismissControls = new JButton[]{yesButton, noButton};
        } else if (options == YES_OPTION) {
            dismissControls = new JButton[]{yesButton};
        } else {
            throw new IllegalArgumentException("Unrecognized dialog type.");
        }


        JButton theDefaultButton = null;
        for (JButton dismissControl : dismissControls) {
            int i = (Integer) dismissControl.getClientProperty(PROPERTY_OPTION);
            if (i == defaultButton) {
                theDefaultButton = dismissControl;
            }
        }

        return new DialogFooter(leftComponents, dismissControls, true, theDefaultButton, locale);
    }

    /**
     * Clones an array of JComponents
     */
    private static JComponent[] copy(JComponent... c) {
        JComponent[] newArray = new JComponent[c.length];
        System.arraycopy(c, 0, newArray, 0, c.length);
        return newArray;
    }

    private static void addFocusArrowListener(JComponent jc) {
        /* Check to see if someone already added this kind of listener:
         */
        KeyListener[] listeners = jc.getKeyListeners();
        for (KeyListener listener : listeners) {
            if (listener instanceof FocusArrowListener) {
                return;
            }
        }
        //Add our own:
        jc.addKeyListener(new FocusArrowListener());
    }

    /**
     * This takes a set of buttons and gives them all the width/height
     * of the largest button among them.
     * <P>(More specifically, this sets the {@code preferredSize}
     * of each button to the largest preferred size in the list of buttons.
     *
     * @param buttons an array of buttons.
     */
    private static void normalizeButtons(JButton... buttons) {
        int maxWidth = 0;
        int maxHeight = 0;
        for (int a = 0; a < buttons.length; a++) {
            buttons[a].setPreferredSize(null);
            Dimension d = buttons[a].getPreferredSize();
            Number n = (Number) buttons[a].getClientProperty(PROPERTY_OPTION);
            if (n != null && n.intValue() == DONT_SAVE_OPTION ||
                d.width > 80) {
                buttons[a] = null;
            }
            if (buttons[a] != null) {
                maxWidth = Math.max(d.width, maxWidth);
                maxHeight = Math.max(d.height, maxHeight);
            }
        }
        for (JButton button : buttons) {
            if (button != null) {
                button.setPreferredSize(new Dimension(maxWidth, maxHeight));
            }
        }
    }

    /**
     * This indicates that an action button risks losing user's data.
     * On Macs an unsafe button is spaced farther away from safe buttons.
     */
    private static boolean isUnsafe(JComponent c) {
        Boolean b = (Boolean) c.getClientProperty(PROPERTY_UNSAFE);
        if (b == null) {
            return Boolean.FALSE;
        }
        return b;
    }

    /**
     * This sets the unsafe flag for buttons.
     */
    private static void setUnsafe(JComponent c, boolean b) {
        c.putClientProperty(PROPERTY_UNSAFE, b);
    }

    private void setButtonGap(int gap) {
        if (buttonGap == gap) {
            return;
        }
        buttonGap = gap;
        installGUI();
    }

     private void setUnsafeButtonGap(int unsafeGap) {
         if (unsafeButtonGap == unsafeGap) {
             return;
         }
         unsafeButtonGap = unsafeGap;
         installGUI();
     }

    private void installGUI() {
        removeAll();

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0;
        c.weighty = 1;
        c.fill = GridBagConstraints.NONE;
        c.insets = new Insets(0, 0, 0, 0);
        c.anchor = GridBagConstraints.CENTER;
        for (JComponent leftControl : leftControls) {
            add(leftControl, c);
            c.gridx++;
            c.insets = new Insets(0, 0, 0, buttonGap);
        }
        c.weightx = 1;
        c.insets = new Insets(0, 0, 0, 0);
        JPanel fluff = new JPanel();
        fluff.setOpaque(false);

        if (leftControls.length > 0) {
            add(fluff, c); //fluff to enforce the left and right sides
            c.gridx++;
        }
        c.weightx = 0;
        int unsafeCtr = 0;
        int safeCtr = 0;
        for (JComponent dismissControl1 : dismissControls) {
            if (JVM.IS_MAC && isUnsafe(dismissControl1)) {
                unsafeCtr++;
            } else {
                safeCtr++;
            }
        }
        JButton[] unsafeButtons = new JButton[unsafeCtr];
        JButton[] safeButtons = new JButton[safeCtr];
        unsafeCtr = 0;
        safeCtr = 0;
        for (JComponent dismissControl : dismissControls) {
            if (dismissControl instanceof JButton) {
                if (JVM.IS_MAC && isUnsafe(dismissControl)) {
                    unsafeButtons[unsafeCtr++] = (JButton) dismissControl;
                } else {
                    safeButtons[safeCtr++] = (JButton) dismissControl;
                }
            }
        }

        c.ipadx = 0;
        c.ipady = 0;
        c.insets = new Insets(0, 0, 0, 0);
        for (int a = 0; a < unsafeButtons.length; a++) {
            JComponent comp = reverseButtonOrder ?
                unsafeButtons[unsafeButtons.length - 1 - a] :
                unsafeButtons[a];
            add(comp, c);
            c.gridx++;
            c.insets.left = buttonGap;
        }
        if (unsafeButtons.length > 0) {
            c.insets.left = unsafeButtonGap;
        } else if (leftControls.length == 0) {
            c.weightx = 1;
            add(fluff, c);
            c.weightx = 0;
            c.gridx++;
        }

        for (int a = 0; a < safeButtons.length; a++) {
            JComponent comp = reverseButtonOrder ?
                safeButtons[safeButtons.length - 1 - a] :
                safeButtons[a];

            add(comp, c);
            c.gridx++;
            c.insets.left = buttonGap;
        }

        normalizeButtons(unsafeButtons);
        normalizeButtons(safeButtons);
    }

    /**
     * Finds a certain type of button, if it is available.
     *
     * @param buttonType of the options in this class (such as YES_OPTION or CANCEL_OPTION)
     * @return the button that maps to that option, or null if no such button was found.
     */
    public JButton getButton(int buttonType) {
        for (int a = 0; a < getComponentCount(); a++) {
            if (getComponent(a) instanceof JButton) {
                JButton button = (JButton) getComponent(a);
                Object value = button.getClientProperty(PROPERTY_OPTION);
                int intValue = -1;
                if (value instanceof Number) {
                    intValue = ((Number) value).intValue();
                }
                if (intValue == buttonType) {
                    return button;
                }
            }
        }
        return null;
    }

    public void addOkButtonActionListener(ActionListener okListener) {
        getButton(OK_OPTION).addActionListener(okListener);
    }

    private static class CloseDialogAndDisposeAction extends AbstractAction {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
            Component src = (Component) e.getSource();
            Container parent = src.getParent();
            while (parent != null) {
                if (parent instanceof JInternalFrame) {
                    parent.setVisible(false);
                    ((JInternalFrame) parent).dispose();
                    return;
                }
                if (parent instanceof Window) {
                    parent.setVisible(false);
                    ((Window) parent).dispose();
                    return;
                }
                parent = parent.getParent();
            }
        }
    }

    private class InnerActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JComponent lastSelectedComponent = (JComponent) e.getSource();

            if (autoClose) {
                closeDialogAndDisposeAction.actionPerformed(e);
            }
        }
    }
}
