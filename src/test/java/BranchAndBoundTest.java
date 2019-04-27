import math.linear.basic.ObjectiveFunctionType;
import math.linear.basic.Relation;
import math.linear.problem.Problem;
import math.linear.problem.ProblemEquation;
import math.linear.problem.ProblemObjectiveFunction;
import math.linear.simplex.*;
import org.junit.Assert;
import org.junit.Test;

public class BranchAndBoundTest {
    @Test
    public void testBnB1(){
        Problem problem = Problem.getInstance();
        problem.setPrecision(16);
        problem.addEquation(ProblemEquation.make(new double[]{9., 3., -1., 0.}, Relation.LESS_OR_EQUAL, 140.d));
        problem.addEquation(ProblemEquation.make(new double[]{-3., 0., 9., 7.}, Relation.LESS_OR_EQUAL, 180.d));
        problem.addObjectiveFunction(ProblemObjectiveFunction.make(new double[]{4., 12., 1.,2.}, ObjectiveFunctionType.MAXIMUM));

        TableauBuilder tableauBuilder = TableauBuilder.getInstance();
        tableauBuilder.setProbliem(problem);
        Tableau tableau = tableauBuilder.build();

        Tableau solved = SimplexMethod.applyTo(tableau);

        Tableau solvedInt = BranchAndBoundMethod.applyTo(problem, solved);

/*
        Assert.assertArrayEquals(new double[]{0,53,19,1},solvedInt.getSolution(),0.1);

        System.out.println("\n-----------------------------------");
        double[] values = solvedInt.getSolution();
        for(int k = 0; k < values.length; k++){
            System.out.format("x(%1$d)=%2$.0f\n", k+1, values[k]);
        }
        */
    }

}
