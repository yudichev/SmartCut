/*
 *
 */

import math.linear.basic.ObjectiveFunctionType;
import math.linear.basic.Relation;
import math.linear.basic.problem.ProblemEquation;
import math.linear.basic.problem.ProblemObjectiveFunction;
import math.linear.basic.tableau.Tableau;
import math.linear.basic.tableau.TableauBuilder;
import math.linear.simplex.canonical.SimplexMethodNew;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class SimplexMethodNewTest
{
    @Test
    public void testSinglePhase() {
        List<ProblemEquation> equations = new ArrayList<>();
        equations.add(ProblemEquation.make(new double[]{1.d,7.d, -4.d}, Relation.LESS_OR_EQUAL, 50.d));
        equations.add(ProblemEquation.make(new double[]{6.d,2.d, 1.d}, Relation.LESS_OR_EQUAL, 20.d));

        ProblemObjectiveFunction objFunc = ProblemObjectiveFunction.make(new double[]{3.d,0.d,4.d}, ObjectiveFunctionType.MAXIMUM);

        TableauBuilder tableauBuilder = TableauBuilder.getInstance();
        tableauBuilder.setEquations(equations);
        tableauBuilder.setObjectiveFunction(objFunc);
        Tableau tableau = tableauBuilder.build();

        Tableau solved = SimplexMethodNew.applyTo(tableau);

        solved.getRows().forEach(row -> {row.getCoefficients().forEach(coeff -> System.out.print(coeff + ", "));
            System.out.println("\n");});
    }

}
