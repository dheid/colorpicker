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

    /**
     * The major Java version being used (1.4, 1.5, 1.6, etc.), or
     * -1 if this value couldn't be correctly determined.
     */
    private static final float JAVA_VERSION = getMajorJavaVersion();

    private static final String OS_NAME = getOSName();
    /**
     * Whether this session is on a Mac.
     */
    public static final boolean IS_MAC = OS_NAME.contains("mac");
     /**
     * Whether this session is on Vista.
     */
    public static final boolean IS_VISTA = OS_NAME.contains("vista");

    private static String getOSName() {
        try {
            return System.getProperty("os.name").toLowerCase();
        } catch (AccessControlException e) {
            return "unknown";
        }
    }

    public static boolean isUsingQuartz() {
        try {
            return IS_MAC && (JAVA_VERSION > 0 && JAVA_VERSION < 1.4f || System.getProperty("apple.awt.graphics.UseQuartz") != null && System.getProperty("apple.awt.graphics.UseQuartz").equals("true"));
        } catch (AccessControlException e) {
            return false;
        }
    }

    /**
     * This converts the system property "java.version" to a float value.
     * This drops rightmost digits until a legitimate float can be parsed.
     * <BR>For example, this converts "1.6.0_05" to "1.6".
     * <BR>This value is cached as the system property "java.major.version".  Although
     * technically this value is a String, it will always be parseable as a float.
     *
     * @throws AccessControlException this may be thrown in unsigned applets!  Beware!
     */
    private static float getMajorJavaVersion() throws AccessControlException {
        System.getProperty("java.major.version");
        String s = System.getProperty("java.version");
        float f = -1;
        int i = s.length();
        while (f < 0 && i > 0) {
            try {
                f = Float.parseFloat(s.substring(0, i));
            } catch (NumberFormatException e) {
                // ignore
            }
            i--;
        }
        String majorVersion = Float.toString(f);
        System.setProperty("java.major.version", majorVersion);
        return Float.parseFloat(majorVersion);
    }

}
