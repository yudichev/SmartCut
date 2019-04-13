package linear2;

import math.linear2.basic.EquationSet;
import math.linear2.basic.Equation;
import math.linear2.basic.Relation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class EquationTest2 {
    private Equation _equation1;
    private Equation _equation2;
    private Equation _equation3;
    private EquationSet _equationSet;

    @Before
    public void prepareEquations(){
        _equation1 = Equation.of(new double[]{1., 2., 3.}, 0, Relation.EQUAL);
        _equation2 = Equation.of(new double[]{4., 5., 7.}, 0, Relation.GREATER_OR_EQUAL);
        _equation3 = Equation.of(new double[]{4., 5., 7.}, 3, Relation.EQUAL);

        _equationSet = EquationSet.create();
        _equationSet.addEquation(_equation1);
        _equationSet.addEquation(_equation2);
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

    @Test
    public void testCreateEquationSet() {

        Assert.assertEquals(1., _equation1.getCoefficientAt(1).doubleValue(), 0.0d);
        Assert.assertEquals(2., _equation1.getCoefficientAt(2).doubleValue(), 0.0d);
        Assert.assertEquals(3., _equation1.getCoefficientAt(3).doubleValue(), 0.0d);
        Assert.assertEquals(Relation.EQUAL, _equation1.getRelation());
        Assert.assertEquals(0., _equation1.getCoefficientAt(0).doubleValue(), 0.0d);

        Assert.assertEquals(4., _equation2.getCoefficientAt(1).doubleValue(), 0.0d);
        Assert.assertEquals(5., _equation2.getCoefficientAt(2).doubleValue(), 0.0d);
        Assert.assertEquals(7., _equation2.getCoefficientAt(3).doubleValue(), 0.0d);
        Assert.assertEquals(Relation.GREATER_OR_EQUAL, _equation2.getRelation());
        Assert.assertEquals(0., _equation1.getCoefficientAt(0).doubleValue(), 0.0d);

        Assert.assertEquals(_equationSet.getNumberOfEquations(), 2);
    }

    @Test
    public void testEquationModification(){

        Equation eq = _equationSet.getEquations().get(1);
        Assert.assertEquals(4., eq.getCoefficientAt(1).doubleValue(),0.0d);
        Assert.assertEquals(5., eq.getCoefficientAt(2).doubleValue(),0.0d);
        Assert.assertEquals(7., eq.getCoefficientAt(3).doubleValue(),0.0d);
        Assert.assertEquals(Relation.GREATER_OR_EQUAL, eq.getRelation());

        Equation feq = eq.multiplyBy(BigDecimal.valueOf(7.));
        Assert.assertEquals(4.*7., feq.getCoefficientAt(1).doubleValue(),0.0d);
        Assert.assertEquals(5.*7., feq.getCoefficientAt(2).doubleValue(),0.0d);
        Assert.assertEquals(7.*7., feq.getCoefficientAt(3).doubleValue(),0.0d);
        Assert.assertEquals(Relation.GREATER_OR_EQUAL, eq.getRelation());

        feq = eq.multiplyBy(BigDecimal.valueOf(-3.));
        Assert.assertEquals(4.*(-3.), feq.getCoefficientAt(1).doubleValue(),0.0d);
        Assert.assertEquals(5.*(-3.), feq.getCoefficientAt(2).doubleValue(),0.0d);
        Assert.assertEquals(7.*(-3.), feq.getCoefficientAt(3).doubleValue(),0.0d);
        Assert.assertEquals(Relation.LESS_OR_EQUAL, feq.getRelation());


        try {
            _equationSet.addEquation(Equation.of(new double[]{1., 2., 3., 4.}, 6, Relation.GREATER_OR_EQUAL));
            Assert.fail("Exception must be thrown here");
        } catch(RuntimeException ex) {
            Assert.assertEquals("Equation length mismatches the set.", ex.getMessage());
        }
    }

}
