/*
 * Provides tests for Gomory method
 */

import javafx.scene.control.Tab;
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

        Tableau solved = SimplexMethod.applySinglePhase(tableau);

        Tableau solvedInt = GomoryMethod.applyTo(solved);


        Assert.assertArrayEquals(new double[]{0,53,19,1},solvedInt.getSolution(),0.1);

        System.out.println("\n-----------------------------------");
        double[] values = solvedInt.getSolution();
        for(int k = 0; k < values.length; k++){
            System.out.format("x(%1$d)=%2$.0f\n", k+1, values[k]);
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

        Tableau solved = SimplexMethod.applyTwoPhases(tableau);

        Tableau solvedInt = GomoryMethod.applyTo(solved);


       Assert.assertArrayEquals(new double[]{22,0,13,0},solvedInt.getSolution(),0.1);

        System.out.println("\n-----------------------------------");
        double[] values = solvedInt.getSolution();
        for(int k = 0; k < values.length; k++){
            System.out.format("x(%1$d)=%2$.0f\n", k+1, values[k]);
        }
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

        Tableau solved = SimplexMethod.applyTwoPhases(tableau);

        Tableau solvedInt = GomoryMethod.applyTo(solved);


        Assert.assertArrayEquals(new double[]{0,0,42},solvedInt.getSolution(),0.1);

        System.out.println("\n-----------------------------------");
        double[] values = solvedInt.getSolution();
        for(int k = 0; k < values.length; k++){
            System.out.format("x(%1$d)=%2$.0f\n", k+1, values[k]);
        }
    }

}
