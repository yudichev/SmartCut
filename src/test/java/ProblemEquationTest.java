/*
 * Provides test for ProblemEquation
 */

import math.linear.problem.ProblemEquation;
import math.linear.basic.Relation;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class ProblemEquationTest
{
    @Test
    public void testCreateProblemEquation(){
        double[] coeff = new double[]{1.d,3.d,-6.5d,7.d};
        ProblemEquation equation = ProblemEquation.make(coeff, Relation.GREATER_OR_EQUAL, 9.0d);
        Assert.assertArrayEquals(coeff, Arrays.copyOfRange(equation.getCoefficients(), 1, 5),0.0d);

        Assert.assertEquals(1.d, equation.getCoefficientAt(1),0.0d);
        Assert.assertEquals(3.d, equation.getCoefficientAt(2),0.0d);
        Assert.assertEquals(-6.5d, equation.getCoefficientAt(3),0.0d);
        Assert.assertEquals(7.d, equation.getCoefficientAt(4),0.0d);
        Assert.assertEquals(9.d, equation.getCoefficientAt(0),0.0d);
    }
}
