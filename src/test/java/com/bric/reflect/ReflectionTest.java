/*
 * @(#)ReflectionTest.java
 *
 * $Date$
 *
 * Copyright (c) 2014 by Jeremy Wood.
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
package com.bric.reflect;

import org.junit.Test;

import java.awt.Color;

import static org.junit.Assert.assertEquals;

public class ReflectionTest {

	@Test
	public void testParseColor1() {
		assertEquals( new Color(255,100,9), Reflection.parse("new java.awt.Color(255,100,9)") );
	}

	@Test
	public void testParseColor2() {
		assertEquals( Color.lightGray, Reflection.parse("java.awt.Color.lightGray") );
	}

	@Test
	public void testParseString1() {
		assertEquals( "abc", Reflection.parse("\"abc\"") );
	}
}
