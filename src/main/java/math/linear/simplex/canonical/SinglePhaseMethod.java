package math.linear.simplex.canonical;

import math.linear.basic.ObjectiveFunction;

public class SinglePhaseMethod extends  AbstractMethod{

    private static final String CANNOT_SOLVE = "The task cannot be solved by a single phase method. Use double phase method.";

    static CanonicalProblem findSolution(CanonicalProblem problem){
        checkSolvable(problem);

        ObjectiveFunction objectiveFunction = problem.getObjectiveFunction();
        problem = findOptimalSolution(problem, objectiveFunction);
        return problem;
    }

    static double[] solve(CanonicalProblem problem){
        int originalNumberOfVariables = problem.getOriginalNumberOfVariables();

        CanonicalProblem optimalTableau = findSolution(problem);

        return extractSolution(optimalTableau, originalNumberOfVariables);
    }



    private static void checkSolvable(CanonicalProblem problem){
        if(problem.isTwoPhases()){
            throw new RuntimeException(CANNOT_SOLVE);
        }
    }
}
