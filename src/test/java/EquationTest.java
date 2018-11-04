import math.linear.basic.Equation;
import math.linear.basic.EquationSet;
import math.linear.basic.Relation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EquationTest {
    private Equation _equation1;
    private Equation _equation2;
    private Equation _equation3;
    private EquationSet _equationSet;

    @Before
    public void prepareEquations(){
        _equation1 = Equation.of(new double[]{1., 2., 3.}, Relation.EQUAL, 0);
        _equation2 = Equation.of(new double[]{4., 5., 7.}, Relation.GREATER_OR_EQUAL,0);
        _equation3 = Equation.of(new double[]{4., 5., 7.}, Relation.EQUAL,3);
        _equationSet = EquationSet.create();
        _equationSet.addEquation(_equation1);
        _equationSet.addEquation(_equation2);
    }


    @Test
    public void testCreateEquationSet() {

        Assert.assertEquals(1., _equation1.getValueAt(0), 0.0d);
        Assert.assertEquals(2., _equation1.getValueAt(1), 0.0d);
        Assert.assertEquals(3., _equation1.getValueAt(2), 0.0d);
        Assert.assertEquals(Relation.EQUAL, _equation1.getRelation());
        Assert.assertEquals(0., _equation1.getRightValue(), 0.0d);

        Assert.assertEquals(4., _equation2.getValueAt(0), 0.0d);
        Assert.assertEquals(5., _equation2.getValueAt(1), 0.0d);
        Assert.assertEquals(7., _equation2.getValueAt(2), 0.0d);
        Assert.assertEquals(Relation.GREATER_OR_EQUAL, _equation2.getRelation());
        Assert.assertEquals(0., _equation1.getRightValue(), 0.0d);

        Assert.assertEquals(_equationSet.getNumberOfEquations(), 2);
    }

    @Test
    public void testEquationModification(){

        Equation eq = _equationSet.getEquation(1);
        Assert.assertEquals(4., eq.getValueAt(0),0.0d);
        Assert.assertEquals(5., eq.getValueAt(1),0.0d);
        Assert.assertEquals(7., eq.getValueAt(2),0.0d);
        Assert.assertEquals(Relation.GREATER_OR_EQUAL, eq.getRelation());

        Equation feq = eq.applyFactor(7.);
        Assert.assertEquals(4.*7., feq.getValueAt(0),0.0d);
        Assert.assertEquals(5.*7., feq.getValueAt(1),0.0d);
        Assert.assertEquals(7.*7., feq.getValueAt(2),0.0d);
        Assert.assertEquals(Relation.GREATER_OR_EQUAL, eq.getRelation());

        feq = eq.applyFactor(-3.);
        Assert.assertEquals(4.*(-3.), feq.getValueAt(0),0.0d);
        Assert.assertEquals(5.*(-3.), feq.getValueAt(1),0.0d);
        Assert.assertEquals(7.*(-3.), feq.getValueAt(2),0.0d);
        Assert.assertEquals(Relation.LESS_OR_EQUAL, feq.getRelation());

        feq = eq.copy();
        Assert.assertEquals(4., feq.getValueAt(0),0.0d);
        Assert.assertEquals(5., feq.getValueAt(1),0.0d);
        Assert.assertEquals(7., feq.getValueAt(2),0.0d);
        Assert.assertEquals(Relation.GREATER_OR_EQUAL, feq.getRelation());

        EquationSet set2 = _equationSet.swapEquations(0,1);
        Equation eq2 = set2.getEquation(0);
        eq = _equationSet.getEquation(1);
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
        Equation sum = _equation1.add(_equation3);
        Assert.assertEquals(5., sum.getValueAt(0),0.0d);
        Assert.assertEquals(7., sum.getValueAt(1),0.0d);
        Assert.assertEquals(10., sum.getValueAt(2),0.0d);
        Assert.assertEquals(3., sum.getRightValue(),0.0d);

        try{
            _equation1.add(_equation2);
            Assert.fail("Exception must be thrown here.");
        } catch(RuntimeException ex){
            Assert.assertEquals("Equations with different relation cannot be combined.",ex.getMessage());
        }
    }

    @Test
    public void testNormalize(){
        Equation eq = Equation.of(new double[]{1., 2., 3.}, Relation.GREATER_OR_EQUAL, -3.);
        Equation normEq = eq.normalize();
        Assert.assertEquals(-1., normEq.getValueAt(0), 0.0d);
        Assert.assertEquals(-2., normEq.getValueAt(1), 0.0d);
        Assert.assertEquals(-3., normEq.getValueAt(2), 0.0d);
        Assert.assertEquals(Relation.LESS_OR_EQUAL, normEq.getRelation());
        Assert.assertEquals(3., normEq.getRightValue(), 0.0d);
    }

}
