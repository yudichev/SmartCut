/*
 * Provides test for ProblemObjectiveFunction
 */

import math.linear.basic.ObjectiveFunctionType;
import math.linear.problem.ProblemObjectiveFunction;
import org.junit.Assert;
import org.junit.Test;

public class ProblemObjectiveFunctionTest
{
    @Test
    public void testCreate(){
        double[] val1 = new double[] {1.d, 2.d, 3.d};
        ProblemObjectiveFunction fun1 = ProblemObjectiveFunction.make(val1, ObjectiveFunctionType.MAXIMUM);
        Assert.assertEquals(1.d, fun1.getCoefficientAt(1), 0.0d);
        Assert.assertEquals(2.d, fun1.getCoefficientAt(2), 0.0d);
        Assert.assertEquals(3.d, fun1.getCoefficientAt(3), 0.0d);
        Assert.assertEquals(ObjectiveFunctionType.MAXIMUM, fun1.getType());
        Assert.assertEquals(fun1.getType(),ObjectiveFunctionType.MAXIMUM);

    }
}
