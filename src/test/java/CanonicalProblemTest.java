import math.linear.basic.*;
import math.linear.simplex.canonical.CanonicalProblem;
import math.linear.simplex.canonical.SimplexMethod;
import math.linear.simplex.canonical.SinglePhaseMethod;
import org.junit.Assert;
import org.junit.Test;

public class CanonicalProblemTest
{

    @Test
    public void testCanonical() {
        Equation eq1 = Equation.of(new double[]{1., 2.}, Relation.GREATER_OR_EQUAL, -5.);
        Equation eq2 = Equation.of(new double[]{5., 4.}, Relation.LESS_OR_EQUAL, 7.);
        EquationSet eqset = EquationSet.create();
        eqset.addEquation(eq1);
        eqset.addEquation(eq2);

        ObjectiveFunction objfunc = ObjectiveFunction.create(new double[]{3., -4.}, ObjectiveFunctionType.MAXIMUM);
        CanonicalProblem problem = CanonicalProblem.create(eqset, objfunc);
        EquationSet set2 = problem.getEquationSet();
        Assert.assertArrayEquals(new double[]{-1., -2., 1., 0}, set2.getEquation(0).getLeftValues(), 0.0);
        Assert.assertArrayEquals(new double[]{5., 4., 0., 1}, set2.getEquation(1).getLeftValues(), 0.0);
        Assert.assertTrue(set2.getEquation(0).getRelation().isEqual());
        Assert.assertTrue(set2.getEquation(1).getRelation().isEqual());
        Assert.assertEquals(5., set2.getEquation(0).getRightValue(), 0.0d);
        Assert.assertArrayEquals(new double[]{3., -4., 0., 0.}, problem.getObjectiveFunction().getValues(), 0.0d);
        Assert.assertEquals(ObjectiveFunctionType.MAXIMUM, problem.getObjectiveFunction().getType());
    }

    @Test
    public void testCanonicalWithExceptions(){
        try
        {
            Equation eq1 = Equation.of(new double[]{ 1., 2. }, Relation.GREATER_OR_EQUAL, 5.);
            Equation eq2 = Equation.of(new double[]{ 5., 4. }, Relation.LESS_OR_EQUAL, 7.);
            EquationSet eqset = EquationSet.create();
            eqset.addEquation(eq1);
            eqset.addEquation(eq2);

            ObjectiveFunction objfunc = ObjectiveFunction.create(new double[]{ 3., -4. }, ObjectiveFunctionType.MAXIMUM);
            CanonicalProblem problem = CanonicalProblem.create(eqset, objfunc);
           // SinglePhaseMethod.findSolution(problem);
            double[] solution = SimplexMethod.solve(problem);
            Assert.fail("An exception must be here");
        } catch(RuntimeException ex){
            Assert.assertEquals("Solution does not exist: failed to find initial canonical tableau", ex.getMessage());
        }
    }



    @Test
    public void testPivot() {
        Equation eq1 = Equation.of(new double[]{1., 2.}, Relation.LESS_OR_EQUAL, 5);
        Equation eq2 = Equation.of(new double[]{4., 3.}, Relation.LESS_OR_EQUAL, 9);
        EquationSet equationSet = EquationSet.create();
        equationSet.addEquation(eq1);
        equationSet.addEquation(eq2);
        ObjectiveFunction objectiveFunction = ObjectiveFunction.create(new double[]{2., 3.}, ObjectiveFunctionType.MAXIMUM);

        CanonicalProblem problem = CanonicalProblem.create(equationSet, objectiveFunction);
        int col = objectiveFunction.getIndexOfMaximum();
        int row = problem.getPivotRowIdx(col);
        CanonicalProblem problem1 = problem.gaussianExclusion(row, col);

        Assert.assertArrayEquals(new double[]{0.5, 1., 0.5, 0.}, problem1.getEquationSet().getEquation(0).getLeftValues(), 0.0);
        Assert.assertArrayEquals(new double[]{2.5, 0., -1.5, 1.}, problem1.getEquationSet().getEquation(1).getLeftValues(), 0.0);
        Assert.assertEquals(2.5, problem1.getEquationSet().getEquation(0).getRightValue(), 0.0d);
        Assert.assertEquals(1.5, problem1.getEquationSet().getEquation(1).getRightValue(), 0.0d);

        Assert.assertEquals(0., problem1.getObjectiveFunction().getValueAt(col), 0.0d);
        Assert.assertFalse(problem1.getObjectiveFunction().isOptimal());

        int col1 = problem1.getObjectiveFunction().getIndexOfMaximum();
        int row1 = problem1.getPivotRowIdx(col1);
        CanonicalProblem problem2 = problem1.gaussianExclusion(row1, col1);

        Assert.assertArrayEquals(new double[]{0., 1., 0.8, -0.2}, problem2.getEquationSet().getEquation(0).getLeftValues(), 0.0);
        Assert.assertArrayEquals(new double[]{1., 0., -0.6, 0.4}, problem2.getEquationSet().getEquation(1).getLeftValues(), 1.e-10);
        Assert.assertEquals(2.2, problem2.getEquationSet().getEquation(0).getRightValue(), 0.0d);
        Assert.assertEquals(0.6, problem2.getEquationSet().getEquation(1).getRightValue(), 1.e-10);

        Assert.assertArrayEquals(new double[]{0., 0., -1.2, -0.2}, problem2.getObjectiveFunction().getValues(), 0.0d);
        Assert.assertTrue(problem2.getObjectiveFunction().isOptimal());
    }

    @Test
    public void testSolve() {
        Equation eq1 = Equation.of(new double[]{1., 2.}, Relation.LESS_OR_EQUAL, 5);
        Equation eq2 = Equation.of(new double[]{4., 3.}, Relation.LESS_OR_EQUAL, 9);
        EquationSet equationSet = EquationSet.create();
        equationSet.addEquation(eq1);
        equationSet.addEquation(eq2);
        ObjectiveFunction objectiveFunction = ObjectiveFunction.create(new double[]{2., 3.}, ObjectiveFunctionType.MAXIMUM);

        CanonicalProblem problem = CanonicalProblem.create(equationSet, objectiveFunction);
        double[] solution = SimplexMethod.solve(problem);
        Assert.assertArrayEquals(new double[]{0.6, 2.2}, solution, 1.e-10);
    }

    @Test
    public void testSolve1() {
        Equation eq1 = Equation.of(new double[]{2., 5., 1.}, Relation.LESS_OR_EQUAL, 4);
        Equation eq2 = Equation.of(new double[]{4., 0., 9.}, Relation.LESS_OR_EQUAL, 12);
        Equation eq3 = Equation.of(new double[]{1., 2., 6.}, Relation.LESS_OR_EQUAL, 5);
        EquationSet equationSet = EquationSet.create();
        equationSet.addEquation(eq1);
        equationSet.addEquation(eq2);
        equationSet.addEquation(eq3);
        ObjectiveFunction objectiveFunction = ObjectiveFunction.create(new double[]{2., 7., 1.}, ObjectiveFunctionType.MAXIMUM);

        CanonicalProblem problem = CanonicalProblem.create(equationSet, objectiveFunction);
        double[] solution = SimplexMethod.solve(problem);
        Assert.assertArrayEquals(new double[]{0., 0.8, 0.}, solution, 1.e-10);
    }

    @Test
    public void testSolve2() {
        Equation eq1 = Equation.of(new double[]{9., 3., -1.}, Relation.LESS_OR_EQUAL, 4);
        Equation eq2 = Equation.of(new double[]{-3., 0., 9.}, Relation.LESS_OR_EQUAL, 8);
        Equation eq3 = Equation.of(new double[]{12., -2., 6.}, Relation.LESS_OR_EQUAL, 5);
        EquationSet equationSet = EquationSet.create();
        equationSet.addEquation(eq1);
        equationSet.addEquation(eq2);
        equationSet.addEquation(eq3);
        ObjectiveFunction objectiveFunction = ObjectiveFunction.create(new double[]{2., -2., 1.}, ObjectiveFunctionType.MAXIMUM);

        CanonicalProblem problem = CanonicalProblem.create(equationSet, objectiveFunction);
        double[] solution = SimplexMethod.solve(problem);
        Assert.assertArrayEquals(new double[]{5./12., 0., 0.}, solution, 1.e-10);
    }

    @Test
    public void testSolve3() {
        Equation eq1 = Equation.of(new double[]{9., 3., -1., 0.}, Relation.LESS_OR_EQUAL, 14);
        Equation eq2 = Equation.of(new double[]{-3., 0., 9., 7.}, Relation.LESS_OR_EQUAL, 18);
        Equation eq3 = Equation.of(new double[]{12., -2., 6., 11.}, Relation.LESS_OR_EQUAL, 35);
        Equation eq4 = Equation.of(new double[]{1., 0., 16., -4.}, Relation.LESS_OR_EQUAL, 21);
        EquationSet equationSet = EquationSet.create();
        equationSet.addEquation(eq1);
        equationSet.addEquation(eq2);
        equationSet.addEquation(eq3);
        equationSet.addEquation(eq4);
        ObjectiveFunction objectiveFunction = ObjectiveFunction.create(new double[]{4., 12., 1.,2.}, ObjectiveFunctionType.MAXIMUM);

        CanonicalProblem problem = CanonicalProblem.create(equationSet, objectiveFunction);
        double[] solution = SimplexMethod.solve(problem);
        Assert.assertArrayEquals(new double[]{0., 2291./444., 219./148.,99./148.}, solution, 1.e-10);
    }
}
