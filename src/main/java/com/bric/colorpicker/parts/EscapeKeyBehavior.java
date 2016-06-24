package com.bric.colorpicker.parts;

public enum EscapeKeyBehavior {

    /**
     * This triggers the cancel button when the escape key is pressed.  If no
     * cancel button is present: this does nothing.
     * (Also on Macs command+period acts the same as the escape key.)
     * <p>This should only be used if the cancel button does not lead to data
     * loss, because users may quickly press the escape key before reading
     * the text in a dialog.
     */
    TRIGGERS_CANCEL,

    /**
     * This triggers the default button when the escape key is pressed.  If no
     * default button is defined: this does nothing.
     * (Also on Macs command+period acts the same as the escape key.)
     * <p>This should only be used if the default button does not lead to data
     * loss, because users may quickly press the escape key before reading
     * the text in a dialog.
     */
    TRIGGERS_DEFAULT,
    /**
     * This triggers the non-default button when the escape key is pressed.  If no
     * non-default button is defined: this does nothing.
     * (Also on Macs command+period acts the same as the escape key.)
     * <p>This should only be used if the non-default button does not lead to data
     * loss, because users may quickly press the escape key before reading
     * the text in a dialog.
     */
    TRIGGERS_NONDEFAULT
}
