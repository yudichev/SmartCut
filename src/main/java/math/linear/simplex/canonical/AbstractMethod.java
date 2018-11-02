package math.linear.simplex.canonical;

import math.linear.basic.Equation;
import math.linear.basic.EquationSet;

import java.util.Arrays;

public abstract class AbstractMethod {


    public static double[] extractSolution(CanonicalProblem problem, int originalVariablesNumber){
        /*
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
        */

        double[] fullSolution = new double[problem.getObjectiveFunction().getValues().length];
        Arrays.fill(fullSolution, 0.);
        int[] indices = problem.getIndices();
        EquationSet equationSet = problem.getEquationSet();
        for(int k = 0; k < equationSet.getNumberOfEquations(); k++){
            fullSolution[indices[k]] = equationSet.getEquation(k).getRightValue();
        }

        double[] solution = new double[originalVariablesNumber];
        System.arraycopy(fullSolution,0,solution,0,solution.length);
        return solution;
    }
}
