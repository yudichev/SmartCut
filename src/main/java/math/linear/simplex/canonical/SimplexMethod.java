package math.linear.simplex.canonical;

public class SimplexMethod {

    public static double[] solve(CanonicalProblem problem){
        if(problem.isTwoPhases()){
            return DoublePhaseMethod.solve(problem);
        } else {
            return SinglePhaseMethod.solve(problem);
        }
    }

    public static CanonicalProblem findSolution(CanonicalProblem problem){
        if(problem.isTwoPhases()){
            return DoublePhaseMethod.findSolution(problem);
        } else {
            return SinglePhaseMethod.findSolution(problem);
        }
    }
}
