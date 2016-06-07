/*
 * @(#)BooleanProperty.java
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
package com.bric.util;

public class BooleanProperty extends Property<Boolean> {
	
	public BooleanProperty(String propertyName) {
		this(propertyName, false);
	}
	
	public BooleanProperty(String propertyName,boolean defaultValue) {
		super(propertyName);
		setValue(defaultValue);
	}
}
