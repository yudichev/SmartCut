import math.linear.basic.*;
import math.linear.simplex.canonical.CanonicalProblem;
import math.linear.simplex.canonical.DoublePhaseMethod;
import org.junit.Assert;
import org.junit.Test;

public class DoublePhaseMethodTest {

    /**
     * Case 0: 4 variables, 4 equations, find minimum.
     */
    @Test
    public void testDoublePhase() {
        Equation eq1 = Equation.of(new double[]{5., 1., -2., 3.}, Relation.GREATER_OR_EQUAL, 12.);
        Equation eq2 = Equation.of(new double[]{1., 3., 0., 2.}, Relation.GREATER_OR_EQUAL, 23.);
        Equation eq3 = Equation.of(new double[]{3., 2., 1., 7.}, Relation.GREATER_OR_EQUAL, 45.);
        Equation eq4 = Equation.of(new double[]{3., -7., 1., 1.}, Relation.GREATER_OR_EQUAL, 64.);
        EquationSet eqset = EquationSet.create();
        eqset.addEquation(eq1);
        eqset.addEquation(eq2);
        eqset.addEquation(eq3);
        eqset.addEquation(eq4);

        ObjectiveFunction objfunc = ObjectiveFunction.create(new double[]{7., 3., 1., 2.}, ObjectiveFunctionType.MINIMUM);

        CanonicalProblem problem = CanonicalProblem.create(eqset, objfunc);

        double[] solution = DoublePhaseMethod.solve(problem);


        Assert.assertArrayEquals(new double[]{0., 0.,36.,28.}, solution, 0.0d);

    }

    /**
     * Case 1: 4 variables, 2 equations. Find minimum.
     */
    @Test
    public void testDoublePhase1() {
        Equation eq1 = Equation.of(new double[]{5., 11., 6., 12.}, Relation.GREATER_OR_EQUAL, 189.);
        Equation eq2 = Equation.of(new double[]{3., 6., 0., 2.}, Relation.GREATER_OR_EQUAL, 145.);
        EquationSet eqset = EquationSet.create();
        eqset.addEquation(eq1);
        eqset.addEquation(eq2);

        ObjectiveFunction objfunc = ObjectiveFunction.create(new double[]{1., 7., 1., 2.}, ObjectiveFunctionType.MINIMUM);

        CanonicalProblem problem = CanonicalProblem.create(eqset, objfunc);

        double[] solution = DoublePhaseMethod.solve(problem);


        Assert.assertArrayEquals(new double[]{145./3., 0.,0.,0.}, solution, 0.0d);

    }

    /**
     * Case 2: 6 variables, 5 equations, find minimum.
     */
    @Test
    public void testDoublePhase2(){
        Equation eq1 = Equation.of(new double[]{12., 0., 4., 1., 15.,0.}, Relation.GREATER_OR_EQUAL, 123.);
        Equation eq2 = Equation.of(new double[]{4., 1., 1., 0., 0.,0.}, Relation.GREATER_OR_EQUAL, 16.);
        Equation eq3 = Equation.of(new double[]{0., 2., 2., 5., 1.,0.}, Relation.GREATER_OR_EQUAL, 23.);
        Equation eq4 = Equation.of(new double[]{3., 15., 4., 0., 0.,4.}, Relation.GREATER_OR_EQUAL, 345.);
        Equation eq5 = Equation.of(new double[]{5., 1., 23., 0., 12.,40.}, Relation.GREATER_OR_EQUAL, 567.);
        EquationSet eqset = EquationSet.create();
        eqset.addEquation(eq1);
        eqset.addEquation(eq2);
        eqset.addEquation(eq3);
        eqset.addEquation(eq4);
        eqset.addEquation(eq5);

        ObjectiveFunction objfunc = ObjectiveFunction.create(new double[]{4., 1., 15., 2.,1.,7.}, ObjectiveFunctionType.MINIMUM);

        CanonicalProblem problem = CanonicalProblem.create(eqset, objfunc);

        double[] solution = DoublePhaseMethod.solve(problem);

        Assert.assertArrayEquals(new double[]{0., 23., 0., 0., 136./3., 0.}, solution, 1.e-14);
    }


    @Test
    public void testDoublePhase3(){
        Equation eq1 = Equation.of(new double[]{4, 5}, Relation.GREATER_OR_EQUAL, 7);
        Equation eq2 = Equation.of(new double[]{2, -1}, Relation.GREATER_OR_EQUAL, 1);
        Equation eq3 = Equation.of(new double[]{1, 5}, Relation.GREATER_OR_EQUAL, 4);
        Equation eq4 = Equation.of(new double[]{7, 2}, Relation.GREATER_OR_EQUAL, 3);
        Equation eq5 = Equation.of(new double[]{-3, 6}, Relation.GREATER_OR_EQUAL, 10);
        Equation eq6 = Equation.of(new double[]{2, 4}, Relation.GREATER_OR_EQUAL, 21);
        EquationSet eqset = EquationSet.create();
        eqset.addEquation(eq1);
        eqset.addEquation(eq2);
        eqset.addEquation(eq3);
        eqset.addEquation(eq4);
        eqset.addEquation(eq5);
        eqset.addEquation(eq6);

        ObjectiveFunction objfunc = ObjectiveFunction.create(new double[]{4,6},ObjectiveFunctionType.MINIMUM);

        CanonicalProblem problem = CanonicalProblem.create(eqset, objfunc);

        double[] solution = DoublePhaseMethod.solve(problem);

        Assert.assertArrayEquals(new double[]{2.5, 4.}, solution, 1.e-14);
    }
}
