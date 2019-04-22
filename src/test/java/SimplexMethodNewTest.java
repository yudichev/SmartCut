/*
 *
 */

import math.linear.basic.ObjectiveFunctionType;
import math.linear.basic.Relation;
import math.linear.basic.problem.Problem;
import math.linear.basic.problem.ProblemEquation;
import math.linear.basic.problem.ProblemObjectiveFunction;
import math.linear.basic.tableau.Tableau;
import math.linear.basic.tableau.TableauBuilder;
import math.linear.simplex.canonical.SimplexMethodNew;
import org.junit.Assert;
import org.junit.Test;

import java.math.MathContext;


public class SimplexMethodNewTest
{
    @Test
    public void testSinglePhase() {
        Problem problem = Problem.getInstance();
        problem.setPrecision(32);
        problem.addEquation(ProblemEquation.make(new double[]{1.d,7.d, -4.d}, Relation.LESS_OR_EQUAL, 50.d));
        problem.addEquation(ProblemEquation.make(new double[]{6.d,2.d, 1.d}, Relation.LESS_OR_EQUAL, 20.d));
        problem.addObjectiveFunction(ProblemObjectiveFunction.make(new double[]{3.d,0.d,4.d}, ObjectiveFunctionType.MAXIMUM));

        TableauBuilder tableauBuilder = TableauBuilder.getInstance();
        tableauBuilder.setProbliem(problem);
        Tableau tableau = tableauBuilder.build();

        Tableau solved = SimplexMethodNew.applySinglePhase(tableau);

        double[] values = solved.getSolution();
        for(int k = 0; k < values.length; k++){
            System.out.format("x(%1$d)=%2$.1f\n", k+1, values[k]);
        }


        solved.getRows().forEach(row -> {row.getCoefficients().forEach(coeff -> System.out.format(coeff.toEngineeringString() + ", "));
            System.out.println();});
    }

    @Test
    public void testSinglePhase2() {


        Problem problem = Problem.getInstance();
        problem.setPrecision(16);
        problem.addEquation(ProblemEquation.make(new double[]{9., 3., -1., 0.}, Relation.LESS_OR_EQUAL, 14.d));
        problem.addEquation(ProblemEquation.make(new double[]{-3., 0., 9., 7.}, Relation.LESS_OR_EQUAL, 18.d));
        problem.addEquation(ProblemEquation.make(new double[]{12., -2., 6., 11.}, Relation.LESS_OR_EQUAL, 35.d));
        problem.addEquation(ProblemEquation.make(new double[]{1., 0., 16., -4.}, Relation.LESS_OR_EQUAL, 21.d));
        problem.addObjectiveFunction(ProblemObjectiveFunction.make(new double[]{4., 12., 1.,2.}, ObjectiveFunctionType.MAXIMUM));

        TableauBuilder tableauBuilder = TableauBuilder.getInstance();
        tableauBuilder.setProbliem(problem);
        Tableau tableau = tableauBuilder.build();

        Tableau solved = SimplexMethodNew.applySinglePhase(tableau);

        double[] values = solved.getSolution();
        for(int k = 0; k < values.length; k++){
            System.out.format("x(%1$d)=%2$.3f\n", k+1, values[k]);
        }

        solved.getRows().forEach(row -> {row.getCoefficients()
                .forEach(coeff -> System.out.print(coeff.round(new MathContext(5)).stripTrailingZeros()  + ", "));
            System.out.println();});
    }


    @Test
    public void testSinglePhase3() {


        Problem problem = Problem.getInstance();
        problem.setPrecision(16);
        problem.addEquation(ProblemEquation.make(new double[]{14., 2., 11.7, -9.}, Relation.LESS_OR_EQUAL, 124.d));
        problem.addEquation(ProblemEquation.make(new double[]{5., -4., 0., 0.}, Relation.LESS_OR_EQUAL, 100.d));
        problem.addEquation(ProblemEquation.make(new double[]{12., 0., -26., 31.}, Relation.LESS_OR_EQUAL, 135.d));
        problem.addEquation(ProblemEquation.make(new double[]{10., -0.1, 54., 14.}, Relation.LESS_OR_EQUAL, 178.d));
        problem.addObjectiveFunction(ProblemObjectiveFunction.make(new double[]{27., 1.4, -1.,32.}, ObjectiveFunctionType.MAXIMUM));

        TableauBuilder tableauBuilder = TableauBuilder.getInstance();
        tableauBuilder.setProbliem(problem);
        Tableau tableau = tableauBuilder.build();

        Tableau solved = SimplexMethodNew.applySinglePhase(tableau);

        double[] values = solved.getSolution();
        for(int k = 0; k < values.length; k++){
            System.out.format("x(%1$d)=%2$.3f\n", k+1, values[k]);
        }

        Assert.assertArrayEquals(new double[]{9.061d,0.d,1.148d,1.81d},values,0.01);
    }

    @Test
    public void testSinglePhase4() {


        Problem problem = Problem.getInstance();
        problem.setPrecision(16);
        problem.addEquation(ProblemEquation.make(new double[]{7.1d, -3.d, 4.d}, Relation.LESS_OR_EQUAL, 45.d));
        problem.addEquation(ProblemEquation.make(new double[]{-1.d, 4.d, 9.d}, Relation.LESS_OR_EQUAL, 36.d));
        problem.addEquation(ProblemEquation.make(new double[]{-3.d, 12.d, 14.d}, Relation.LESS_OR_EQUAL, 21.d));
        problem.addObjectiveFunction(ProblemObjectiveFunction.make(new double[]{4., 8.2, 7.8}, ObjectiveFunctionType.MAXIMUM));

        TableauBuilder tableauBuilder = TableauBuilder.getInstance();
        tableauBuilder.setProbliem(problem);
        Tableau tableau = tableauBuilder.build();

        Tableau solved = SimplexMethodNew.applySinglePhase(tableau);

        double[] values = solved.getSolution();
        for(int k = 0; k < values.length; k++){
            System.out.format("x(%1$d)=%2$.3f\n", k+1, values[k]);
        }

        Assert.assertArrayEquals(new double[]{7.913d,3.728d,0.d},values,0.01);
    }

    @Test
    public void testSinglePhaseNoSolution() {
        Problem problem = Problem.getInstance();
        problem.setPrecision(16);
        problem.addEquation(ProblemEquation.make(new double[]{14., 2., 0., 0.}, Relation.LESS_OR_EQUAL, 124.d));
        problem.addEquation(ProblemEquation.make(new double[]{5., -4., 0., 0.}, Relation.LESS_OR_EQUAL, 100.d));
        problem.addObjectiveFunction(ProblemObjectiveFunction.make(new double[]{27., 1.4, -1.,32.}, ObjectiveFunctionType.MAXIMUM));

        TableauBuilder tableauBuilder = TableauBuilder.getInstance();
        tableauBuilder.setProbliem(problem);
        Tableau tableau = tableauBuilder.build();

        try
        {
            SimplexMethodNew.applySinglePhase(tableau);
            Assert.fail("Exception 'Solution does not exist' must be thrown here.");
        }catch(RuntimeException ex){
            Assert.assertEquals("Solution does not exist.", ex.getMessage());
        }
    }


    @Test
    public void testTwoPhases(){
        Problem problem = Problem.getInstance();
        problem.setPrecision(16);
        problem.addEquation(ProblemEquation.make(new double[]{14., 0., 1., 7.}, Relation.GREATER_OR_EQUAL, 12.d));
        problem.addEquation(ProblemEquation.make(new double[]{5., 4., 5., 3.}, Relation.GREATER_OR_EQUAL, 20.d));
        problem.addObjectiveFunction(ProblemObjectiveFunction.make(new double[]{2., 1.4, 1.,3.}, ObjectiveFunctionType.MINIMUM));

        TableauBuilder tableauBuilder = TableauBuilder.getInstance();
        tableauBuilder.setProbliem(problem);
        Tableau tableau = tableauBuilder.build();

        Tableau solved = SimplexMethodNew.applyTwoPhases(tableau);

        double[] values = solved.getSolution();
        for(int k = 0; k < values.length; k++){
            System.out.format("x(%1$d)=%2$.3f\n", k+1, values[k]);
        }

        Assert.assertArrayEquals(new double[]{0.615d,0.d,3.384d,0.},values,0.01);

        solved.getRows().forEach(row -> {row.getCoefficients()
            .forEach(coeff -> System.out.print(coeff.round(new MathContext(5)).stripTrailingZeros()  + ", "));
            System.out.println();});

    }

    @Test
    public void testTwoPhases2(){
        Problem problem = Problem.getInstance();
        problem.setPrecision(16);
        problem.addEquation(ProblemEquation.make(new double[]{1., 3., 5., -1.}, Relation.GREATER_OR_EQUAL, 5.d));
        problem.addEquation(ProblemEquation.make(new double[]{2., 6., 0., 0.}, Relation.GREATER_OR_EQUAL, 10.d));
        problem.addEquation(ProblemEquation.make(new double[]{3., 7., 4., 0.}, Relation.GREATER_OR_EQUAL, 4.d));
        problem.addEquation(ProblemEquation.make(new double[]{0., 2., 5., 1.}, Relation.GREATER_OR_EQUAL, 7.d));
        problem.addEquation(ProblemEquation.make(new double[]{4., 1., 1., 7.}, Relation.GREATER_OR_EQUAL, 1.d));
        problem.addObjectiveFunction(ProblemObjectiveFunction.make(new double[]{4., 3., 1.,7.}, ObjectiveFunctionType.MINIMUM));

        TableauBuilder tableauBuilder = TableauBuilder.getInstance();
        tableauBuilder.setProbliem(problem);
        Tableau tableau = tableauBuilder.build();

        Tableau solved = SimplexMethodNew.applyTwoPhases(tableau);

        double[] values = solved.getSolution();
        for(int k = 0; k < values.length; k++){
            System.out.format("x(%1$d)=%2$.3f\n", k+1, values[k]);
        }

        Assert.assertArrayEquals(new double[]{0.d,1.666d,0.733d,0.},values,0.01);

        solved.getRows().forEach(row -> {row.getCoefficients()
            .forEach(coeff -> System.out.print(coeff.round(new MathContext(5)).stripTrailingZeros()  + ", "));
            System.out.println();});

    }
}
