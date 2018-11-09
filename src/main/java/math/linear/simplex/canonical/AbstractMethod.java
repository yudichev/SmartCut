package math.linear.simplex.canonical;

import math.linear.basic.EquationSet;
import math.linear.basic.ObjectiveFunction;

import java.util.Arrays;

public abstract class AbstractMethod {

    static CanonicalProblem findOptimalSolution(CanonicalProblem problem, ObjectiveFunction objectiveFunction)
    {
        while(!objectiveFunction.isOptimal()) {
            int col = objectiveFunction.getIndexOfMaximum();
            int row = problem.getPivotRowIdx(col);
            problem = problem.gaussianExclusion(row, col);
            objectiveFunction = problem.getObjectiveFunction();
        }
        return problem;
    }

    public static double[] extractSolution(CanonicalProblem problem, int originalVariablesNumber){

        double[] fullSolution = new double[problem.getObjectiveFunction().getValues().length];
        Arrays.fill(fullSolution, 0.);
        int[] indices = problem.getPermutations();
        EquationSet equationSet = problem.getEquationSet();
        for(int k = 0; k < equationSet.getNumberOfEquations(); k++){
            fullSolution[indices[k]] = equationSet.getEquation(k).getRightValue();
        }

        double[] solution = new double[originalVariablesNumber];
        System.arraycopy(fullSolution,0,solution,0,solution.length);
        return solution;
    }
}
