import math.linear.basic.Equation;
import math.linear.basic.EquationSet;
import math.linear.basic.Relation;
import math.linear.simplex.canonical.CanonicalProblem;
import org.junit.Assert;
import org.junit.Test;

public class CanonicalProblemTest {

    @Test
    public void testCanonical(){
        Equation eq1 = Equation.of(new double[]{1., 2.}, Relation.GREATER_OR_EQUAL, 5.);
        Equation eq2 = Equation.of(new double[]{5.,4.},Relation.LESS_OR_EQUAL,7.);
        EquationSet eqset = EquationSet.create();
        eqset.addEquation(eq1);
        eqset.addEquation(eq2);
        CanonicalProblem problem = CanonicalProblem.createFromEquationSet(eqset);
        EquationSet set2 = problem.getEquationSet();
        Assert.assertArrayEquals(new double[]{-1.,-2.,-1.,0},set2.getEquation(0).getLeftValues(),0.0);
        Assert.assertArrayEquals(new double[]{5.,4.,0.,1},set2.getEquation(1).getLeftValues(),0.0);
        Assert.assertTrue(set2.getEquation(0).getRelation().isEqual());
        Assert.assertTrue(set2.getEquation(1).getRelation().isEqual());
        Assert.assertEquals(set2.getEquation(0).getRightValue(),-5.,0.0d);
    }
}
