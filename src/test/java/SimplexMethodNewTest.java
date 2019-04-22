/*
 *
 */

import math.linear.basic.ObjectiveFunctionType;
import math.linear.basic.Relation;
import math.linear.problem.Problem;
import math.linear.problem.ProblemEquation;
import math.linear.problem.ProblemObjectiveFunction;
import math.linear.simplex.Tableau;
import math.linear.simplex.TableauBuilder;
import math.linear.simplex.SimplexMethod;
import org.junit.Assert;
import org.junit.Test;

import java.math.MathContext;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.util.Timer;


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

        Tableau solved = SimplexMethod.applySinglePhase(tableau);

        System.out.println("\n-----------------------------------");
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

        Tableau solved = SimplexMethod.applySinglePhase(tableau);

        System.out.println("\n-----------------------------------");
        double[] values = solved.getSolution();
        for(int k = 0; k < values.length; k++){
            System.out.format("x(%1$d)=%2$.3f\n", k+1, values[k]);
        }

        solved.getRows().forEach(row -> {row.getCoefficients()
                .forEach(coeff -> System.out.print(
                        coeff.round(new MathContext(5)).stripTrailingZeros().toEngineeringString()  + ", "));
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

        Tableau solved = SimplexMethod.applySinglePhase(tableau);

        System.out.println("\n-----------------------------------");
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

        Tableau solved = SimplexMethod.applySinglePhase(tableau);

        System.out.println("\n-----------------------------------");
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
            SimplexMethod.applySinglePhase(tableau);
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

        Tableau solved = SimplexMethod.applyTwoPhases(tableau);

        System.out.println("\n-----------------------------------");
        double[] values = solved.getSolution();
        for(int k = 0; k < values.length; k++){
            System.out.format("x(%1$d)=%2$.3f\n", k+1, values[k]);
        }

        Assert.assertArrayEquals(new double[]{0.615d,0.d,3.384d,0.},values,0.01);

        solved.getRows().forEach(row -> {row.getCoefficients()
            .forEach(coeff -> System.out.print(
                    coeff.round(new MathContext(5)).stripTrailingZeros().toEngineeringString()  + ", "));
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

        Tableau solved = SimplexMethod.applyTwoPhases(tableau);

        System.out.println("\n-----------------------------------");
        double[] values = solved.getSolution();
        for(int k = 0; k < values.length; k++){
            System.out.format("x(%1$d)=%2$.3f\n", k+1, values[k]);
        }

        Assert.assertArrayEquals(new double[]{0.d,1.666d,0.733d,0.},values,0.01);

        solved.getRows().forEach(row -> {row.getCoefficients()
            .forEach(coeff -> System.out.print(
                    coeff.round(new MathContext(5)).stripTrailingZeros().toEngineeringString()  + ", "));
            System.out.println();});

    }

    @Test
    public void testTwoPhases3(){
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

        Tableau solved = SimplexMethod.applyTwoPhases(tableau);

        System.out.println("\n-----------------------------------");
        double[] values = solved.getSolution();
        for(int k = 0; k < values.length; k++){
            System.out.format("x(%1$d)=%2$.3f\n", k+1, values[k]);
        }

        Assert.assertArrayEquals(new double[]{0.d,1.666d,0.733d,0.},values,0.01);

        solved.getRows().forEach(row -> {row.getCoefficients()
            .forEach(coeff -> System.out.print(
                    coeff.round(new MathContext(5)).stripTrailingZeros().toEngineeringString()  + ", "));
            System.out.println();});

    }

    @Test
    public void testTwoPhases4(){
        Problem problem = Problem.getInstance();
        problem.setPrecision(16);
        problem.addEquation(ProblemEquation.make(new double[]{1., 3., 5., 1.}, Relation.GREATER_OR_EQUAL, 5.d));
        problem.addEquation(ProblemEquation.make(new double[]{2., 1., 0., 0.}, Relation.LESS_OR_EQUAL, 10.d));
        problem.addEquation(ProblemEquation.make(new double[]{3., 7., 4., 0.}, Relation.EQUAL, 4.d));
        problem.addEquation(ProblemEquation.make(new double[]{0., 2., 5., 1.}, Relation.GREATER_OR_EQUAL, 7.d));
        problem.addEquation(ProblemEquation.make(new double[]{4., 1., 1., 7.}, Relation.GREATER_OR_EQUAL, 1.d));
        problem.addObjectiveFunction(ProblemObjectiveFunction.make(new double[]{3., 9., 7.,6.}, ObjectiveFunctionType.MINIMUM));

        TableauBuilder tableauBuilder = TableauBuilder.getInstance();
        tableauBuilder.setProbliem(problem);
        Tableau tableau = tableauBuilder.build();

        Tableau solved = SimplexMethod.applyTwoPhases(tableau);

        System.out.println("\n-----------------------------------");
        double[] values = solved.getSolution();
        for(int k = 0; k < values.length; k++){
            System.out.format("x(%1$d)=%2$.3f\n", k+1, values[k]);
        }

        Assert.assertArrayEquals(new double[]{0.d,0.d,1.d,2.d},values,0.01);

        solved.getRows().forEach(row -> {row.getCoefficients()
            .forEach(coeff -> System.out.print(coeff.round(new MathContext(5)).stripTrailingZeros()  + ", "));
            System.out.println();});

    }


    @Test
    public void testTwoPhases5(){
        Instant start = Instant.now();
        Problem problem = Problem.getInstance();
        problem.setPrecision(32);
        problem.addEquation(ProblemEquation.make(new double[]{3., 1.2, 2.1, 4.}, Relation.GREATER_OR_EQUAL, 34.7d));
        problem.addEquation(ProblemEquation.make(new double[]{7., 6., 15., 34.}, Relation.GREATER_OR_EQUAL, 56.d));
        problem.addEquation(ProblemEquation.make(new double[]{1., 3., -1., 8.}, Relation.LESS_OR_EQUAL, 29.1d));
        problem.addEquation(ProblemEquation.make(new double[]{3., 9.1, 7., 16.}, Relation.GREATER_OR_EQUAL, 45.2d));
        problem.addEquation(ProblemEquation.make(new double[]{24., 7.4, 15., 9.4}, Relation.GREATER_OR_EQUAL, 80.d));
        problem.addEquation(ProblemEquation.make(new double[]{3.1, 1., 2., -3.2}, Relation.LESS_OR_EQUAL, 16.7d));
        problem.addEquation(ProblemEquation.make(new double[]{5., 1.3, 2.1, 1}, Relation.GREATER_OR_EQUAL, 4d));
        problem.addObjectiveFunction(ProblemObjectiveFunction.make(new double[]{4., 6., 1.,6.}, ObjectiveFunctionType.MINIMUM));

        TableauBuilder tableauBuilder = TableauBuilder.getInstance();
        tableauBuilder.setProbliem(problem);
        Tableau tableau = tableauBuilder.build();

        Tableau solved = SimplexMethod.applyTwoPhases(tableau);

        System.out.println("\n-----------------------------------");
        double[] values = solved.getSolution();
        for(int k = 0; k < values.length; k++){
            System.out.format("x(%1$d)=%2$.3f\n", k+1, values[k]);
        }

        Assert.assertArrayEquals(new double[]{0.d,0.d,12.081d,2.332d},values,0.01);

        solved.getRows().forEach(row -> {row.getCoefficients()
                .forEach(coeff -> System.out.print(coeff.round(new MathContext(5)).stripTrailingZeros()  + ", "));
            System.out.println();});
        Instant end = Instant.now();
        System.out.println("Duration " + Duration.between(start,end).toMillis() + "ms");


    }

    @Test
    public void testTwoPhases6(){
        Instant start = Instant.now();
        Problem problem = Problem.getInstance();
        problem.setPrecision(32);
        problem.addEquation(ProblemEquation.make(new double[]{3., 1.2, 2.1, 4.,7.,-2,11.,0.}, Relation.GREATER_OR_EQUAL, 56.d));
        problem.addEquation(ProblemEquation.make(new double[]{4.3, 1.9, -1.2, 4.8,17.,6.,-2,11.}, Relation.GREATER_OR_EQUAL, 38.d));
        problem.addEquation(ProblemEquation.make(new double[]{2., 1.5, 7., -0.5, 6., 4.2,1.1,0.1}, Relation.LESS_OR_EQUAL, 234.d));
        problem.addObjectiveFunction(ProblemObjectiveFunction.make(new double[]{2.4, 3.7, 16.5,41.,5.,1.7,6.3,4.2}, ObjectiveFunctionType.MINIMUM));

        TableauBuilder tableauBuilder = TableauBuilder.getInstance();
        tableauBuilder.setProbliem(problem);
        Tableau tableau = tableauBuilder.build();

        Tableau solved = SimplexMethod.applyTwoPhases(tableau);

        System.out.println("\n-----------------------------------");
        double[] values = solved.getSolution();
        for(int k = 0; k < values.length; k++){
            System.out.format("x(%1$d)=%2$.3f\n", k+1, values[k]);
        }

        Assert.assertArrayEquals(new double[]{0.d,0.d,0.d,0.d,2.636d,0.d,3.412d,0.d},values,0.01);

        solved.getRows().forEach(row -> {row.getCoefficients()
                .forEach(coeff -> System.out.print(coeff.round(new MathContext(5)).stripTrailingZeros()  + ", "));
            System.out.println();});
        Instant end = Instant.now();
        System.out.println("Duration " + Duration.between(start,end).toMillis() + "ms");


    }


    @Test
    public void testTwoPhasesHasNoBasePlane(){
        Problem problem = Problem.getInstance();
        problem.setPrecision(16);
        problem.addEquation(ProblemEquation.make(new double[]{1., 3., 5., -1.}, Relation.GREATER_OR_EQUAL, 5.d));
        problem.addEquation(ProblemEquation.make(new double[]{2., 6., 0., 0.}, Relation.EQUAL, 10.d));
        problem.addEquation(ProblemEquation.make(new double[]{3., 7., 4., 0.}, Relation.GREATER_OR_EQUAL, 4.d));
        problem.addEquation(ProblemEquation.make(new double[]{0., 2., 5., 1.}, Relation.GREATER_OR_EQUAL, 7.d));
        problem.addEquation(ProblemEquation.make(new double[]{4., 1., 1., 7.}, Relation.LESS_OR_EQUAL, 1.d));
        problem.addObjectiveFunction(ProblemObjectiveFunction.make(new double[]{3., 9., 7.,6.}, ObjectiveFunctionType.MINIMUM));

        TableauBuilder tableauBuilder = TableauBuilder.getInstance();
        tableauBuilder.setProbliem(problem);
        Tableau tableau = tableauBuilder.build();

        try
        {
            SimplexMethod.applyTwoPhases(tableau);
            Assert.fail("Exception 'The problem has no base plane.' must be throw here");
        } catch(RuntimeException ex){
            Assert.assertEquals("The problem has no base plane.", ex.getMessage());
        }
    }
}
