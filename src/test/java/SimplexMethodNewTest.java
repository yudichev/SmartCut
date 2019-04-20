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
import org.junit.Test;

import java.math.MathContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimplexMethodNewTest
{
    @Test
    public void testSinglePhase() {
        Problem problem = Problem.getInstance();
        problem.setPrecision(16);
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


        solved.getRows().forEach(row -> {row.getCoefficients().forEach(coeff -> System.out.print(coeff + ", "));
            System.out.println("\n");});
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
            System.out.println("\n");});
    }

}
