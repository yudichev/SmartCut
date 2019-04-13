package linear2;

import math.linear2.basic.Equation;
import math.linear2.basic.Relation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EquationTest2 {
    private Equation _equation1;
    private Equation _equation2;
    private Equation _equation3;

    @Before
    public void prepareEquations(){
        _equation1 = Equation.of(new double[]{1., 2., 3.}, 0, Relation.EQUAL);
        _equation2 = Equation.of(new double[]{4., 5., 7.}, 0, Relation.GREATER_OR_EQUAL);
        _equation3 = Equation.of(new double[]{4., 5., 7.}, 3, Relation.EQUAL);
    }


    @Test
    public void testAdd(){
        Equation sum = _equation1.add(_equation3);
        Assert.assertEquals(5., sum.getCoefficientAt(1).doubleValue(),0.0d);
        Assert.assertEquals(7., sum.getCoefficientAt(2).doubleValue(),0.0d);
        Assert.assertEquals(10., sum.getCoefficientAt(3).doubleValue(),0.0d);
        Assert.assertEquals(3., sum.getCoefficientAt(0).doubleValue(),0.0d);

        try{
            _equation1.add(_equation2);
            Assert.fail("Exception must be thrown here.");
        } catch(RuntimeException ex){
            Assert.assertEquals("Equations with different relation cannot be combined.",ex.getMessage());
        }
    }

    @Test
    public void testNormalize(){
        Equation eq = Equation.of(new double[]{1., 2., 3.}, -3, Relation.GREATER_OR_EQUAL);
        Equation normEq = eq.normalize();
        Assert.assertEquals(-1., normEq.getCoefficientAt(1).doubleValue(), 0.0d);
        Assert.assertEquals(-2., normEq.getCoefficientAt(2).doubleValue(), 0.0d);
        Assert.assertEquals(-3., normEq.getCoefficientAt(3).doubleValue(), 0.0d);
        Assert.assertEquals(Relation.LESS_OR_EQUAL, normEq.getRelation());
        Assert.assertEquals(3., normEq.getCoefficientAt(0).doubleValue(), 0.0d);
    }

}
