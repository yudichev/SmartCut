package math.linear.simplex.integer;

import math.linear.basic.Equation;
import math.linear.basic.EquationSet;
import math.linear.basic.ObjectiveFunction;
import math.linear.simplex.canonical.CanonicalProblem;

import java.util.Arrays;

public class GomoryMethod {
    public static int getIndexOfRowWithMaximalFraction(CanonicalProblem problem){
        int j = -1;
        double fraction = -1.;
        EquationSet eqSet = problem.getEquationSet();
        if(eqSet.getNumberOfEquations() == 0)
            throw new RuntimeException("Nothing to solve");

        for(int k = 0; k < eqSet.getNumberOfEquations(); k++){
            double rvalue = eqSet.getEquation(k).getRightValue();
            double currentFraction = rvalue - Math.floor(rvalue);
            if(fraction <  currentFraction){
                j = k;
                fraction = currentFraction;
            }
        }
        return j;
    }

    public static Equation getCuttingEquation(CanonicalProblem problem){
        int rowNumber = getIndexOfRowWithMaximalFraction(problem);
        Equation eq = problem.getEquationSet().getEquation(rowNumber);
        double[] lvalues = new double[eq.getLength() + 1];
        Arrays.fill(lvalues, 1.d);
        for(int k = 0; k < eq.getLength(); k++){
            double  value = eq.getValueAt(k);
            lvalues[k] = value - Math.floor(value);
        }
        double rvalue = eq.getRightValue() - Math.floor(eq.getRightValue());
        return Equation.of(lvalues,eq.getRelation(),rvalue);
    }


    public static CanonicalProblem addCuttingPlane(CanonicalProblem problem){
        EquationSet equationSet = EquationSet.create();
        EquationSet srcEqSet = problem.getEquationSet();
        for(int k = 0; k < srcEqSet.getNumberOfEquations(); k++){
            Equation srcEquation = srcEqSet.getEquation(k);
            double[] lvalues = new double[srcEquation.getLength() + 1];
            Arrays.fill(lvalues,0.d);
            System.arraycopy(srcEquation.getLeftValues(),0,lvalues,0,srcEquation.getLength());
            Equation newEquation = Equation.of(lvalues,srcEquation.getRelation(),srcEquation.getRightValue());
            equationSet.addEquation(newEquation);
        }
        equationSet.addEquation(getCuttingEquation(problem));
        ObjectiveFunction srcObjectiveFunction = problem.getObjectiveFunction();
        double[] srcObjFuncValues = srcObjectiveFunction.getValues();
        double[] objFuncValues = new double[srcObjFuncValues.length];
        Arrays.fill(objFuncValues,0.d);
        System.arraycopy(srcObjFuncValues,0,objFuncValues,0,srcObjFuncValues.length);
        ObjectiveFunction newObjFunc = ObjectiveFunction.create(objFuncValues,srcObjectiveFunction.getType());
        return CanonicalProblem.create(equationSet,newObjFunc);
    }
}
