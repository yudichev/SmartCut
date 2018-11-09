package math.linear.exceptions;

/*
 * Copyright 2001-2018 by HireRight, Inc. All rights reserved.
 * This software is the confidential and proprietary information
 * of HireRight, Inc. Use is subject to license terms.
 */

public class ObjectiveFunctionUnboundedException extends RuntimeException
{
    public ObjectiveFunctionUnboundedException(){
        super("Solution does not exist: objective function is unbounded.");
    }
}
