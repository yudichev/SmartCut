/*
 * Copyright 2001-2019 by HireRight, Inc. All rights reserved.
 * This software is the confidential and proprietary information
 * of HireRight, Inc. Use is subject to license terms.
 */

import math.linear.basic.ObjectiveFunctionType;
import math.linear.basic.problem.ProblemObjectiveFunction;
import org.junit.Assert;
import org.junit.Test;

public class ProblemObjectiveFunctionTest
{
    @Test
    public void testCreate(){
        double[] val1 = new double[] {1., 2., 3.};
        ProblemObjectiveFunction fun1 = ProblemObjectiveFunction.make(val1, ObjectiveFunctionType.MAXIMUM);
        Assert.assertEquals(1., fun1.getCoefficientAt(1), 0.0d);
        Assert.assertEquals(2., fun1.getCoefficientAt(2), 0.0d);
        Assert.assertEquals(3., fun1.getCoefficientAt(3), 0.0d);
        Assert.assertArrayEquals(val1,fun1.getCoefficients(),0.0d);
        Assert.assertEquals(ObjectiveFunctionType.MAXIMUM, fun1.getType());
        Assert.assertEquals(fun1.getType(),ObjectiveFunctionType.MAXIMUM);

    }
}
