import math.linear.basic.*;
import math.linear.simplex.canonical.CanonicalProblem;
import math.linear.simplex.canonical.DoublePhaseMethod;
import math.linear.simplex.canonical.SimplexMethod;
import math.linear.simplex.canonical.SinglePhaseMethod;
import math.linear.simplex.integer.GomoryMethod;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class GomoryMethodTest {


    @Test
    public void test1() {
        Equation eq1 = Equation.of(new double[]{5., 11., 6., 12.}, Relation.GREATER_OR_EQUAL, 189.);
        Equation eq2 = Equation.of(new double[]{3., 6., 0., 2.}, Relation.GREATER_OR_EQUAL, 145.);
        EquationSet eqset = EquationSet.create();
        eqset.addEquation(eq1);
        eqset.addEquation(eq2);

        ObjectiveFunction objfunc = ObjectiveFunction.create(new double[]{1., 7., 1., 2.}, ObjectiveFunctionType.MINIMUM);

        CanonicalProblem problem = CanonicalProblem.create(eqset, objfunc);

        CanonicalProblem problem1 = SimplexMethod.findSolution(problem);

        Equation cutEq = GomoryMethod.getCuttingEquation(problem1);

        CanonicalProblem problem2 = GomoryMethod.addCuttingPlane(problem, cutEq);


        problem2.getEquationSet().stream().forEach(
                equation -> {
                    Arrays.stream(equation.getLeftValues()).forEach(value -> System.out.print(value + ", "));
                    System.out.println(";");}
        );

        double[] sol = SimplexMethod.solve(problem2);

        Arrays.stream(sol).forEach(val-> System.out.print(val + ", "));

    }

}
