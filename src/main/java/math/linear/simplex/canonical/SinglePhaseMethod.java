package math.linear.simplex.canonical;

import math.linear.basic.Equation;
import math.linear.basic.EquationSet;
import math.linear.basic.ObjectiveFunction;

public class SinglePhaseMethod extends  AbstractMethod{

    private static String CANNOT_SOLVE = "The task cannot be solved by a single phase method. Use double phase method.";

    public static CanonicalProblem solve(CanonicalProblem problem){
        checkSolvable(problem);

        ObjectiveFunction objectiveFunction = problem.getObjectiveFunction();
        while(!objectiveFunction.isOptimal()) {
            int col = objectiveFunction.getIndexOfMaximum();
            int row = problem.getPivotRowIdx(col);
            problem = problem.gaussianExclusion(row, col);
            objectiveFunction = problem.getObjectiveFunction();
        }
        return problem;
    }



    protected static void checkSolvable(CanonicalProblem problem){
        if(problem.isTwoPhases()){
            throw new RuntimeException(CANNOT_SOLVE);
        }
    }
}
