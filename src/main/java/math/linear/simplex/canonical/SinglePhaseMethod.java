package math.linear.simplex.canonical;

import math.linear.basic.Equation;
import math.linear.basic.EquationSet;
import math.linear.basic.ObjectiveFunction;

public class SinglePhaseMethod {

    private static String CANNOT_SOLVE = "The task cannot be solved by a single phase method. Use double phase method.";


    public static double[] solve(CanonicalProblem problem){
        checkSolvable(problem);

        ObjectiveFunction objectiveFunction = problem.getObjectiveFunction();
        while(!objectiveFunction.isOptimal()) {
            int col = objectiveFunction.getIndexOfMaximum();
            int row = problem.getPivotRowIdx(col);
            problem = problem.gaussianExclusion(row, col);
            objectiveFunction = problem.getObjectiveFunction();
        }
        double[] unsortedSolution = problem.getEquationSet().stream().mapToDouble(Equation::getRightValue).toArray();
        EquationSet eqSet = problem.getEquationSet();
        int m = eqSet.getNumberOfEquations();
        double[] sortedSolution = new double[eqSet.getEquation(0).getLength()];

        for(int k = 0; k < m; k++){
            Equation eq = eqSet.getEquation(k);
            int t = 0;
            for(int j = 0; j < eq.getLength(); j++){
                if(Double.compare(eq.getValueAt(j), 1.) == 0){
                    t = j;
                    break;
                }
            }
            sortedSolution[t] = unsortedSolution[k];
        }
        double[] solution = new double[m];
        System.arraycopy(sortedSolution, 0, solution, 0, m);
        return solution;
    }

    private static void checkSolvable(CanonicalProblem problem){
        if(problem.isTwoPhases()){
            throw new RuntimeException(CANNOT_SOLVE);
        }
    }

}
