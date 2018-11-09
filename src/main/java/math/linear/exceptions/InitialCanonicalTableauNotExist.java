package math.linear.exceptions;

/*
 * Copyright 2001-2018 by HireRight, Inc. All rights reserved.
 * This software is the confidential and proprietary information
 * of HireRight, Inc. Use is subject to license terms.
 */

public class InitialCanonicalTableauNotExist extends RuntimeException
{
    public InitialCanonicalTableauNotExist()
    {
        super("Solution does not exist: failed to find initial canonical tableau");
    }
}
