import math.linear.basic.EquationSet;
import math.linear.basic.Relation;
import math.linear.basic.Equation;
import org.junit.Assert;
import org.junit.Test;
import sun.jvm.hotspot.utilities.AssertionFailure;

import java.util.Arrays;

public class EquationTest {
    @Test
    public void testRow(){
        Equation equation1 = Equation.of(new double[]{1.,2.,3.},Relation.EQUAL,0);
        Assert.assertEquals(1., equation1.getValueAt(0),0.0d);
        Assert.assertEquals(2., equation1.getValueAt(1),0.0d);
        Assert.assertEquals(3., equation1.getValueAt(2),0.0d);
        Assert.assertEquals(Relation.EQUAL, equation1.getRelation());
        Assert.assertEquals(0., equation1.getRightValue(),0.0d);

        Equation equation2 = Equation.of(new double[]{4.,5.,7.},Relation.GREATER_THEN,0);
        Assert.assertEquals(4., equation2.getValueAt(0),0.0d);
        Assert.assertEquals(5., equation2.getValueAt(1),0.0d);
        Assert.assertEquals(7., equation2.getValueAt(2),0.0d);
        Assert.assertEquals(Relation.GREATER_THEN, equation2.getRelation());
        Assert.assertEquals(0., equation1.getRightValue(),0.0d);

        EquationSet set = EquationSet.create();
        set.addEquation(equation1);
        set.addEquation(equation2);
        Assert.assertEquals(set.getNumberOfEquations(),2);

        Equation eq = set.getEquation(1);
        Assert.assertEquals(4., eq.getValueAt(0),0.0d);
        Assert.assertEquals(5., eq.getValueAt(1),0.0d);
        Assert.assertEquals(7., eq.getValueAt(2),0.0d);
        Assert.assertEquals(Relation.GREATER_THEN, eq.getRelation());

        Equation feq = eq.applyFactor(7.);
        Assert.assertEquals(4.*7., feq.getValueAt(0),0.0d);
        Assert.assertEquals(5.*7., feq.getValueAt(1),0.0d);
        Assert.assertEquals(7.*7., feq.getValueAt(2),0.0d);
        Assert.assertEquals(Relation.GREATER_THEN, eq.getRelation());

        feq = eq.applyFactor(-3.);
        Assert.assertEquals(4.*(-3.), feq.getValueAt(0),0.0d);
        Assert.assertEquals(5.*(-3.), feq.getValueAt(1),0.0d);
        Assert.assertEquals(7.*(-3.), feq.getValueAt(2),0.0d);
        Assert.assertEquals(Relation.LESS_THEN, feq.getRelation());

        feq = eq.copy();
        Assert.assertEquals(4., feq.getValueAt(0),0.0d);
        Assert.assertEquals(5., feq.getValueAt(1),0.0d);
        Assert.assertEquals(7., feq.getValueAt(2),0.0d);
        Assert.assertEquals(Relation.GREATER_THEN, feq.getRelation());

        EquationSet set2 = set.swapEquations(0,1);
        Equation eq2 = set2.getEquation(0);
        eq = set.getEquation(1);
        Assert.assertEquals(eq2.getValueAt(0), eq.getValueAt(0),0.0d);
        Assert.assertEquals(eq2.getValueAt(1), eq.getValueAt(1),0.0d);
        Assert.assertEquals(eq2.getValueAt(2), eq.getValueAt(2),0.0d);
        Assert.assertEquals(eq2.getRelation(), eq.getRelation());

        try {
            set2.addEquation(Equation.of(new double[]{1., 2., 3., 4.}, Relation.GREATER_OR_EQUAL, 6.));
            Assert.fail("Exception must be thrown here");
        } catch(RuntimeException ex) {
            Assert.assertEquals("Equation length mismatches the set.", ex.getMessage());
        }
    }

    @Test
    public void testAdd(){
        Equation equation1 = Equation.of(new double[]{1.,2.,3.},Relation.EQUAL,1.);
        Equation equation2 = Equation.of(new double[]{4.,5.,7.},Relation.EQUAL,2.);
        Equation sum = equation1.add(equation2);
        Assert.assertEquals(5., sum.getValueAt(0),0.0d);
        Assert.assertEquals(7., sum.getValueAt(1),0.0d);
        Assert.assertEquals(10., sum.getValueAt(2),0.0d);
        Assert.assertEquals(3., sum.getRightValue(),0.0d);
    }
}
