package math.linear.simplex.canonical;

import math.linear.basic.EquationSet;
import math.linear.basic.ObjectiveFunction;
import math.linear.basic.ObjectiveFunctionType;
import math.linear.exceptions.InitialCanonicalTableauNotExist;
import math.linear.exceptions.ObjectiveFunctionUnboundedException;

import java.util.Arrays;

public class DoublePhaseMethod extends  AbstractMethod{

    private static CanonicalProblem prepareAuxilieryFunction(CanonicalProblem problem){
        EquationSet eqSet = problem.getEquationSet();
        int eqNumber = eqSet.getNumberOfEquations();
        if(eqNumber == 0)
            throw new RuntimeException("Nothing to solve");

        int eqLength = eqSet.getEquation(0).getLength();
        double[] values = new double[eqLength];
        Arrays.fill(values,0.d);
        ObjectiveFunction auxilieryFunction = ObjectiveFunction.create(values, ObjectiveFunctionType.MINIMUM);

        EquationSet equationSet = problem.getEquationSet();
        for(int k = 0; k < eqNumber; k++){
            auxilieryFunction = auxilieryFunction.add(equationSet.getEquation(k).applyFactor(-1.).getLeftValues());
        }

        problem.setAuxFunction(auxilieryFunction);

        return problem;
    }


    private static CanonicalProblem solvePhaseOne(CanonicalProblem problem){
        ObjectiveFunction objectiveFunction = problem.getAuxFunction();
        try
        {
            while(!objectiveFunction.isOptimal())
            {
                int col = objectiveFunction.getIndexOfMinimum();
                int row = problem.getPivotRowIdx(col);
                problem = problem.gaussianExclusion(row, col);
                objectiveFunction = problem.getAuxFunction();
            }
        } catch(ObjectiveFunctionUnboundedException ex){
            throw new InitialCanonicalTableauNotExist();
        }

        return problem;
    }

    private static CanonicalProblem solvePhaseTwo(CanonicalProblem problem){

        ObjectiveFunction objectiveFunction = problem.getObjectiveFunction();
        problem = findOptimalSolution(problem, objectiveFunction);
        return problem;
    }



    public static double[] solve(CanonicalProblem problem){
        int originalNumberOfVariables = problem.getOriginalNumberOfVariables();

        CanonicalProblem problem2 = prepareAuxilieryFunction(problem);

        CanonicalProblem problem3 = solvePhaseOne(problem2);

        ObjectiveFunction auxObjectiveFunction = problem3.getAuxFunction();

        if(Math.abs(auxObjectiveFunction.apply(DoublePhaseMethod.extractSolution(problem3, auxObjectiveFunction.getValues().length))) > 1.e-14 )
        {
            throw new InitialCanonicalTableauNotExist();
        };

        CanonicalProblem problem4 = solvePhaseTwo(problem3);

        return DoublePhaseMethod.extractSolution(problem4, originalNumberOfVariables);
    }

}
