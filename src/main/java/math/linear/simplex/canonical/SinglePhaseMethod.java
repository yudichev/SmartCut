package math.linear.simplex.canonical;

import math.linear.basic.ObjectiveFunction;

public class SinglePhaseMethod extends  AbstractMethod{

    private static final String CANNOT_SOLVE = "The task cannot be solved by a single phase method. Use double phase method.";

    public static CanonicalProblem solve(CanonicalProblem problem){
        checkSolvable(problem);

        ObjectiveFunction objectiveFunction = problem.getObjectiveFunction();
        problem = findOptimalSolution(problem, objectiveFunction);
        return problem;
    }


    private static void checkSolvable(CanonicalProblem problem){
        if(problem.isTwoPhases()){
            throw new RuntimeException(CANNOT_SOLVE);
        }
    }
}
