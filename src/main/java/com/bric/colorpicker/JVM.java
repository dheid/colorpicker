/*
 * @(#)JVM.java
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
package com.bric.colorpicker;

import java.security.AccessControlException;

/**
 * Static methods relating to the JVM environment.
 * <P>Instead of burying a constant like "isQuartz" in its most
 * relevant class (such as OptimizedGraphics2D), it should be
 * stored here so if other classes need to access it they don't
 * necessary have to
 */
public class JVM {

    private static final String OS_NAME = getOSName();
    /**
     * Whether this session is on a Mac.
     */
    public static final boolean IS_MAC = OS_NAME.contains("mac");

    private static String getOSName() {
        try {
            return System.getProperty("os.name").toLowerCase();
        } catch (AccessControlException e) {
            return "unknown";
        }
    }

    public static boolean isUsingQuartz() {
        try {
            return IS_MAC && System.getProperty("apple.awt.graphics.UseQuartz") != null && System.getProperty("apple.awt.graphics.UseQuartz").equals("true");
        } catch (AccessControlException e) {
            return false;
        }
    }

}
