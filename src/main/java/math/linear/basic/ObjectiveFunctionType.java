package math.linear.basic;

/*
 * Copyright 2001-2018 by HireRight, Inc. All rights reserved.
 * This software is the confidential and proprietary information
 * of HireRight, Inc. Use is subject to license terms.
 */

public enum ObjectiveFunctionType
{
	MAXIMUM,
	MINIMUM;

	public boolean isFindMaximum(){
		return MAXIMUM.equals(this);
	}

	public boolean isFindMinimum(){
		return MINIMUM.equals(this);
	}
}
