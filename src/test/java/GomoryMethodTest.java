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
import org.junit.Test;

public class GomoryMethodTest
{
    @Test
    public void testAddCuttingPlane(){
        Problem problem = Problem.getInstance();
        problem.setPrecision(16);
        problem.addEquation(ProblemEquation.make(new double[]{9., 3., -1., 0.}, Relation.LESS_OR_EQUAL, 140.d));
        problem.addEquation(ProblemEquation.make(new double[]{-3., 0., 9., 7.}, Relation.LESS_OR_EQUAL, 180.d));
        problem.addObjectiveFunction(ProblemObjectiveFunction.make(new double[]{4., 12., 1.,2.}, ObjectiveFunctionType.MAXIMUM));

        TableauBuilder tableauBuilder = TableauBuilder.getInstance();
        tableauBuilder.setProbliem(problem);
        Tableau tableau = tableauBuilder.build();

        Tableau solved = SimplexMethod.applySinglePhase(tableau);

        Tableau tableau1 = GomoryMethod.applyTo(solved);

        System.out.println("\n-----------------------------------");
        double[] values = tableau1.getSolution();
        for(int k = 0; k < values.length; k++){
            System.out.format("x(%1$d)=%2$.3f\n", k+1, values[k]);
        }
    }
}
