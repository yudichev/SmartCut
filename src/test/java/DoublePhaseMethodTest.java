import math.linear.basic.*;
import math.linear.simplex.canonical.CanonicalProblem;
import math.linear.simplex.canonical.DoublePhaseMethod;
import math.linear.simplex.canonical.SinglePhaseMethod;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class DoublePhaseMethodTest {

    @Test
    public void testDoublePhase() {
        Equation eq1 = Equation.of(new double[]{3., 2.}, Relation.GREATER_OR_EQUAL, 3.);
        Equation eq2 = Equation.of(new double[]{5., 4.}, Relation.GREATER_OR_EQUAL, 6.);
        EquationSet eqset = EquationSet.create();
        eqset.addEquation(eq1);
        eqset.addEquation(eq2);

        ObjectiveFunction objfunc = ObjectiveFunction.create(new double[]{1., 2.}, ObjectiveFunctionType.MINIMUM);

        CanonicalProblem problem = CanonicalProblem.create(eqset, objfunc);

        CanonicalProblem problem2 = DoublePhaseMethod.processFirstPhase(problem);

        CanonicalProblem problem3 = CanonicalProblem.create(problem2.getEquationSet(), problem2.getAuxFunction().getCanonical());

        double[] solution = SinglePhaseMethod.solve(problem3);

        Assert.assertArrayEquals(new double[]{6./5., 0.}, solution, 0.0d);

    }
}
