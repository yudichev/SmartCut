/*
 * Provides tests for Gomory method
 */

import math.linear.basic.ObjectiveFunctionType;
import math.linear.basic.Relation;
import math.linear.problem.Problem;
import math.linear.problem.ProblemEquation;
import math.linear.problem.ProblemObjectiveFunction;
import math.linear.simplex.GomoryMethod;
import math.linear.simplex.SimplexMethod;
import math.linear.simplex.Tableau;
import math.linear.simplex.TableauBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;

public class GomoryMethodTest
{
    @Test
    public void testGomoryMehtod1(){
        Problem problem = Problem.getInstance();
        problem.setPrecision(16);
        problem.addEquation(ProblemEquation.make(new double[]{9., 3., -1., 0.}, Relation.LESS_OR_EQUAL, 140.d));
        problem.addEquation(ProblemEquation.make(new double[]{-3., 0., 9., 7.}, Relation.LESS_OR_EQUAL, 180.d));
        problem.addObjectiveFunction(ProblemObjectiveFunction.make(new double[]{4., 12., 1.,2.}, ObjectiveFunctionType.MAXIMUM));

        TableauBuilder tableauBuilder = TableauBuilder.getInstance();
        tableauBuilder.setProbliem(problem);
        Tableau tableau = tableauBuilder.build();

        Tableau solved = SimplexMethod.applyTo(tableau);

        Tableau solvedInt = GomoryMethod.applyTo(solved);


        Assert.assertArrayEquals(new double[]{0,53,19,1},solvedInt.getSolution(),0.1);

        System.out.println("\n-----------------------------------");
        double[] values = solvedInt.getSolution();
        for(int k = 0; k < values.length; k++){
            System.out.format("x(%1$d)=%2$.2f\n", k+1, values[k]);
        }
    }

    @Test
    public void testGomoryMehtod2(){
        Problem problem = Problem.getInstance();
        problem.setPrecision(16);
        problem.addEquation(ProblemEquation.make(new double[]{9., 3., 1., 0.}, Relation.GREATER_OR_EQUAL, 140.d));
        problem.addEquation(ProblemEquation.make(new double[]{3., 0., 9., 7.}, Relation.GREATER_OR_EQUAL, 180.d));
        problem.addEquation(ProblemEquation.make(new double[]{4., 2., 1., 6.}, Relation.GREATER_OR_EQUAL, 100.d));
        problem.addObjectiveFunction(ProblemObjectiveFunction.make(new double[]{1., 2., 1.,3.}, ObjectiveFunctionType.MINIMUM));

        TableauBuilder tableauBuilder = TableauBuilder.getInstance();
        tableauBuilder.setProbliem(problem);
        Tableau tableau = tableauBuilder.build();

        Tableau solved = SimplexMethod.applyTo(tableau);

        Tableau solvedInt = GomoryMethod.applyTo(solved);

        System.out.println("\n-----------------------------------");
        double[] values = solvedInt.getSolution();
        for(int k = 0; k < values.length; k++){
            System.out.format("x(%1$d)=%2$.0f\n", k+1, values[k]);
        }

       Assert.assertArrayEquals(new double[]{22,0,13,0},solvedInt.getSolution(),0.1);


    }

    @Test
    public void testGomoryMehtod3(){
        Problem problem = Problem.getInstance();
        problem.setPrecision(16);
        problem.addEquation(ProblemEquation.make(new double[]{2., 1., 7.}, Relation.GREATER_OR_EQUAL, 150.d));
        problem.addEquation(ProblemEquation.make(new double[]{3., -1., 10.}, Relation.GREATER_OR_EQUAL, 170.d));
        problem.addEquation(ProblemEquation.make(new double[]{4., 0., 5.}, Relation.GREATER_OR_EQUAL, 210.d));
        problem.addEquation(ProblemEquation.make(new double[]{2., 3., 1.}, Relation.LESS_OR_EQUAL, 500.d));
        problem.addObjectiveFunction(ProblemObjectiveFunction.make(new double[]{1., 2., 1.}, ObjectiveFunctionType.MINIMUM));

        TableauBuilder tableauBuilder = TableauBuilder.getInstance();
        tableauBuilder.setProbliem(problem);
        Tableau tableau = tableauBuilder.build();

        Tableau solved = SimplexMethod.applyTo(tableau);

        Tableau solvedInt = GomoryMethod.applyTo(solved);


        Assert.assertArrayEquals(new double[]{0,0,42},solvedInt.getSolution(),0.1);

        System.out.println("\n-----------------------------------");
        double[] values = solvedInt.getSolution();
        for(int k = 0; k < values.length; k++){
            System.out.format("x(%1$d)=%2$.0f\n", k+1, values[k]);
        }
    }
    @Test
    public void testGomoryMehtod4(){
        Problem problem = Problem.getInstance();
        problem.setPrecision(16);
        problem.addEquation(ProblemEquation.make(new double[]{3., 4.7, -0.2,0.,4.}, Relation.GREATER_OR_EQUAL, 86.d));
        problem.addEquation(ProblemEquation.make(new double[]{1., 3., 0., 5., 1.2}, Relation.GREATER_OR_EQUAL, 65.d));
        problem.addObjectiveFunction(ProblemObjectiveFunction.make(new double[]{1., 0.2, 3.5, 2.1, 4.7,}, ObjectiveFunctionType.MINIMUM));

        TableauBuilder tableauBuilder = TableauBuilder.getInstance();
        tableauBuilder.setProbliem(problem);
        Tableau tableau = tableauBuilder.build();

        Tableau solved = SimplexMethod.applyTo(tableau);

        Tableau solvedInt = GomoryMethod.applyTo(solved);


        Assert.assertArrayEquals(new double[]{0,22,0,0,0},solvedInt.getSolution(),0.1);

        System.out.println("\n-----------------------------------");
        double[] values = solvedInt.getSolution();
        for(int k = 0; k < values.length; k++){
            System.out.format("x(%1$d)=%2$.0f\n", k+1, values[k]);
        }
    }

    @Test
    public void testGomoryMethod5(){
        Instant start = Instant.now();
        Problem problem = Problem.getInstance();
        problem.setPrecision(32);
        problem.addEquation(ProblemEquation.make(new double[]{3., 1, 2, 4.}, Relation.GREATER_OR_EQUAL, 34d));
        problem.addEquation(ProblemEquation.make(new double[]{7., 6., 15., 34.}, Relation.GREATER_OR_EQUAL, 56.d));
        problem.addEquation(ProblemEquation.make(new double[]{1., 3., -1., 8.}, Relation.LESS_OR_EQUAL, 29d));
        problem.addEquation(ProblemEquation.make(new double[]{3., 9, 7., 16.}, Relation.GREATER_OR_EQUAL, 45d));
        problem.addEquation(ProblemEquation.make(new double[]{24., 7, 15., 9}, Relation.GREATER_OR_EQUAL, 80.d));
        problem.addEquation(ProblemEquation.make(new double[]{3, 1., 2., -3}, Relation.LESS_OR_EQUAL, 16d));
        problem.addEquation(ProblemEquation.make(new double[]{5., 1, 2, 1}, Relation.GREATER_OR_EQUAL, 4d));
        problem.addObjectiveFunction(ProblemObjectiveFunction.make(new double[]{4., 6., 1.,6.}, ObjectiveFunctionType.MINIMUM));

        TableauBuilder tableauBuilder = TableauBuilder.getInstance();
        tableauBuilder.setProbliem(problem);
        Tableau tableau = tableauBuilder.build();

        Tableau solved = SimplexMethod.applyTo(tableau);

        Tableau solvedInt = GomoryMethod.applyTo(solved);

        System.out.println("\n-----------------------------------");
        double[] values = solvedInt.getSolution();
        for(int k = 0; k < values.length; k++){
            System.out.format("x(%1$d)=%2$.0f\n", k+1, values[k]);
        }

       Assert.assertArrayEquals(new double[]{0.d,0.d,11.d,3.d},values,0.01);

        Instant end = Instant.now();
        System.out.println("Duration " + Duration.between(start,end).toMillis() + "ms");

    }

    @Test
    public void testGomoryMethod6(){
        Instant start = Instant.now();
        Problem problem = Problem.getInstance();
        problem.setPrecision(32);
        problem.addEquation(ProblemEquation.make(new double[]{3., 1.2, 2, 4.}, Relation.GREATER_OR_EQUAL, 34.3d));
        problem.addEquation(ProblemEquation.make(new double[]{7., 6.7, 15., 34.}, Relation.GREATER_OR_EQUAL, 56.d));
        problem.addEquation(ProblemEquation.make(new double[]{1.4, 3., -1., 8.}, Relation.LESS_OR_EQUAL, 29d));
        problem.addEquation(ProblemEquation.make(new double[]{3., 9, 7., 16.}, Relation.GREATER_OR_EQUAL, 45d));
        problem.addEquation(ProblemEquation.make(new double[]{24., 7, 15., 9.2}, Relation.GREATER_OR_EQUAL, 80.d));
        problem.addEquation(ProblemEquation.make(new double[]{3, 1., 2., -3}, Relation.LESS_OR_EQUAL, 16d));
        problem.addEquation(ProblemEquation.make(new double[]{5., 1, 2, 1}, Relation.GREATER_OR_EQUAL, 4d));
        problem.addObjectiveFunction(ProblemObjectiveFunction.make(new double[]{4., 6., 1.,6.}, ObjectiveFunctionType.MINIMUM));

        TableauBuilder tableauBuilder = TableauBuilder.getInstance();
        tableauBuilder.setProbliem(problem);
        Tableau tableau = tableauBuilder.build();

        Tableau solved = SimplexMethod.applyTo(tableau);

        Tableau solvedInt = GomoryMethod.applyTo(solved);

        System.out.println("\n-----------------------------------");
        double[] values = solvedInt.getSolution();
        for(int k = 0; k < values.length; k++){
            System.out.format("x(%1$d)=%2$.0f\n", k+1, values[k]);
        }

        Assert.assertArrayEquals(new double[]{0.d,0.d,11.d,3.d},values,0.01);

        Instant end = Instant.now();
        System.out.println("Duration " + Duration.between(start,end).toMillis() + "ms");

    }


}
