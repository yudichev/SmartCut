package math.linear.simplex.canonical;

import math.linear.basic.Equation;
import math.linear.basic.EquationSet;
import math.linear.basic.ObjectiveFunction;
import math.linear.basic.ObjectiveFunctionType;

import java.util.Arrays;
import java.util.stream.Stream;

public class DoublePhaseMethod {
    private EquationSet equationSet;
    private ObjectiveFunction objfunc;

    public static CanonicalProblem processFirstPhase(CanonicalProblem problem){
        CanonicalProblem auxProblem = getAuxiliaryProblem(problem);
        int eqNumber = auxProblem.getEquationSet().getNumberOfEquations();

        ObjectiveFunction objectiveFunction = problem.getObjectiveFunction();
        while(!isValidPlane(objectiveFunction,eqNumber)) {
            int col = objectiveFunction.getIndexOfMaximum();
            int row = problem.getPivotRowIdx(col);
            problem = problem.gaussianExclusion(row, col);
            objectiveFunction = problem.getObjectiveFunction();
        }



        return null;
    }

    private static CanonicalProblem getAuxiliaryProblem(CanonicalProblem problem) {
        EquationSet origSet = problem.getEquationSet();
        int eqNumber = origSet.getNumberOfEquations();
        if(eqNumber == 0)
            throw new RuntimeException("Nothig to solve");

        int eqOrigLength = origSet.getEquation(0).getLength();
        int auxLength = eqOrigLength + eqNumber;

        EquationSet auxSet = EquationSet.create();
        for (int k = 0; k < eqNumber; k++) {
            Equation eq = auxSet.getEquation(k);

            double[] auxLeftValues = new double[auxLength];
            Arrays.fill(auxLeftValues, 0.d);
            System.arraycopy(eq.getLeftValues(), 0, auxLeftValues, 0, eqOrigLength);
            auxLeftValues[eqOrigLength + k] = 1.d;
            Equation auxEquation = Equation.of(auxLeftValues, eq.getRelation(), eq.getRightValue());
            auxSet.addEquation(auxEquation);
        }

        double[] auxObjFunctionValues = new double[auxLength];
        Arrays.fill(auxObjFunctionValues,0.);
        Arrays.fill(auxObjFunctionValues,eqOrigLength,auxLength,1.d);
        ObjectiveFunction auxFunc = ObjectiveFunction.create(auxObjFunctionValues, ObjectiveFunctionType.MINIMUM);

        return CanonicalProblem.create(auxSet,auxFunc);
    }

    private  static boolean isValidPlane(ObjectiveFunction objfunc, int auxNumber){
        double[] values = objfunc.getValues();
        return Arrays.stream(values).skip(values.length - auxNumber).noneMatch(val -> Double.compare(val,0.d) > 0);
    }
}