package math.linear.simplex.canonical;

import math.linear.basic.Equation;
import math.linear.basic.EquationSet;
import math.linear.basic.ObjectiveFunction;
import math.linear.basic.ObjectiveFunctionType;

import java.util.Arrays;
import java.util.stream.Stream;

public class DoublePhaseMethod extends  AbstractMethod{

    public static CanonicalProblem prepareAuxilieryFunction(CanonicalProblem problem){
        CanonicalProblem auxProblem = addAuxiliaryVariables(problem);
        int eqNumber = auxProblem.getEquationSet().getNumberOfEquations();
        if(eqNumber == 0)
            throw new RuntimeException("Nothig to solve");

        ObjectiveFunction auxilieryFunction = auxProblem.getAuxFunction();

        int auxVariablesNumber = auxProblem.getEquationSet().getNumberOfEquations();
        EquationSet equationSet = auxProblem.getEquationSet();
        for(int k = 0; k < auxVariablesNumber; k++){
            auxilieryFunction = auxilieryFunction.add(equationSet.getEquation(k).applyFactor(-1.).getLeftValues());
        }

        auxProblem.setAuxFunction(auxilieryFunction);

        return auxProblem;
    }

    private static CanonicalProblem addAuxiliaryVariables(CanonicalProblem problem) {
        EquationSet origSet = problem.getEquationSet();
        int eqNumber = origSet.getNumberOfEquations();
        if(eqNumber == 0)
            throw new RuntimeException("Nothig to solve");

        int eqOrigLength = origSet.getEquation(0).getLength();
        int auxLength = eqOrigLength + eqNumber;

        EquationSet auxSet = EquationSet.create();
        for (int k = 0; k < eqNumber; k++) {
            Equation eq = origSet.getEquation(k);

            double[] auxLeftValues = new double[auxLength];
            Arrays.fill(auxLeftValues, 0.d);
            System.arraycopy(eq.getLeftValues(), 0, auxLeftValues, 0, eqOrigLength);
            auxLeftValues[eqOrigLength + k] = 1.d;
            Equation auxEquation = Equation.of(auxLeftValues, eq.getRelation(), eq.getRightValue());
            auxSet.addEquation(auxEquation);
        }


        double[] objFunctionValues = new double[auxLength];
        Arrays.fill(objFunctionValues,eqOrigLength,auxLength,0.d);
        ObjectiveFunction srcObjectiveFunction = problem.getObjectiveFunction();
        double[] srcValues = srcObjectiveFunction.getValues();
        int srcLength = srcValues.length;
        System.arraycopy(srcValues,0,objFunctionValues,0,srcLength);

        ObjectiveFunction extObjFunc = ObjectiveFunction.create(objFunctionValues, srcObjectiveFunction.getType());
        CanonicalProblem extProblem = CanonicalProblem.create(auxSet,extObjFunc);

        double[] auxValues = new double[eqOrigLength + eqNumber];
        Arrays.fill(auxValues,0.d);
        Arrays.fill(auxValues,eqOrigLength,eqOrigLength + eqNumber, 1.d);
        ObjectiveFunction auxFunc = ObjectiveFunction.create(auxValues,ObjectiveFunctionType.MINIMUM);
        extProblem.setAuxFunction(auxFunc);

        return extProblem;
    }


    public static CanonicalProblem solveAuxiliery(CanonicalProblem problem){
        checkSolvable(problem);

        int numberOfEquations = problem.getEquationSet().getNumberOfEquations();

        ObjectiveFunction objectiveFunction = problem.getAuxFunction();
        while(!objectiveFunction.isOptimal(numberOfEquations)) {
            int col = objectiveFunction.getIndexOfMinimum();
            int row = problem.getPivotRowIdx(col);
            problem = problem.gaussianExclusion(row, col);
            objectiveFunction = problem.getAuxFunction();
        }
        return problem;
    }

    public static CanonicalProblem solve(CanonicalProblem problem){
        checkSolvable(problem);

        int numberOfEquations = problem.getEquationSet().getNumberOfEquations();

        ObjectiveFunction objectiveFunction = problem.getObjectiveFunction();
        int length = objectiveFunction.getValues().length - numberOfEquations;
        while(!objectiveFunction.isOptimal(numberOfEquations)) {
            int col = objectiveFunction.getIndexOfMaximum(0,length);
            int row = problem.getPivotRowIdx(col);
            problem = problem.gaussianExclusion(row, col);
            objectiveFunction = problem.getObjectiveFunction();
        }
        return problem;
    }

}