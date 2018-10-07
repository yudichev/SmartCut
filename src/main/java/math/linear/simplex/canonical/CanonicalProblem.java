package math.linear.simplex.canonical;

import math.linear.basic.Equation;
import math.linear.basic.EquationSet;
import math.linear.basic.ObjectiveFunction;
import math.linear.basic.Relation;

import java.util.Arrays;
import java.util.stream.Collectors;

public class CanonicalProblem {
    private EquationSet equationSet;
    private ObjectiveFunction objfunc;

    private CanonicalProblem(){
    }

    private CanonicalProblem(EquationSet set, ObjectiveFunction objfunc){
        this.equationSet = set;
        this.objfunc = objfunc;
    }


    public static CanonicalProblem create(EquationSet eqset, ObjectiveFunction func){
        int inqualityNumber = (int) eqset.stream().filter(equation -> !equation.getRelation().equals(Relation.EQUAL)).count();

        if (inqualityNumber == 0)
            return new CanonicalProblem(eqset,func);

        EquationSet newSet = EquationSet.create();
        for (int k = 0; k < eqset.getNumberOfEquations(); k++){
            Equation equation = eqset.getEquation(k).normalize();
            equation = extend(equation, inqualityNumber, k);
            newSet.addEquation(equation);
        }
        return new CanonicalProblem(newSet, extend(func.getCanonical(),inqualityNumber));
    }

    public EquationSet getEquationSet(){
        return this.equationSet;
    }

    public ObjectiveFunction getObjectiveFunction(){return this.objfunc;}

    private static Equation extend(Equation srcEquation, int extLength, int rowNumber){
        double[] lvalues = srcEquation.getLeftValues();
        int srcLength = lvalues.length;
        double[] extLvalues = new double[srcLength + extLength];
        System.arraycopy(lvalues,0,extLvalues,0,srcLength);
        if(srcEquation.getRelation().equals(Relation.GREATER_OR_EQUAL)){
            extLvalues[srcLength + rowNumber] = -1.0d;
        } else {
            extLvalues[srcLength + rowNumber] = 1.0d;
        }
        return Equation.of(extLvalues, Relation.EQUAL, srcEquation.getRightValue());
    }

    private static ObjectiveFunction extend(ObjectiveFunction func, int extLength){
        double[] newFuncCoeff = new double[func.getValues().length + extLength];
        Arrays.fill(newFuncCoeff, 0.0d);
        System.arraycopy(func.getValues(),0,newFuncCoeff,0,func.getValues().length);
        return ObjectiveFunction.create(newFuncCoeff,func.getType());
    }


    public int getPivotRowIdx(int columnIdx){
        int rowIdx = 0;
        double maxRate = this.equationSet.getEquation(0).getValueAt(columnIdx);

        int columnSize = this.equationSet.getNumberOfEquations();
        for(int k = 0; k < columnSize; k++){
            Equation eq = this.equationSet.getEquation(k);
            double val = eq.getValueAt(columnIdx);
            double rval = eq.getRightValue();
            if((Double.compare(rval, 0.) > 0 && Double.compare(val , 0.) > 0) ||
                    (Double.compare(rval , 0) < 0 && Double.compare(val , 0.) < 0)){
                double rate = val / rval;
                if(Double.compare(rate , maxRate) > 0){
                    maxRate = rate;
                    rowIdx = k;
                }
            }
        }

        return rowIdx;
    }

    public CanonicalProblem gaussianExclusion(int row, int col){
        Equation baseEquation = this.getEquationSet().getEquation(row);
        double factor = baseEquation.getValueAt(col);
        if(factor == 0.) throw new RuntimeException("Divisiona by zero");

        baseEquation = baseEquation.applyFactor(1./factor);

        EquationSet newSet = EquationSet.create();

        for(int k = 0; k < this.getEquationSet().getNumberOfEquations(); k++){
            if(k == row){
                newSet.addEquation(baseEquation);
            }
            else
            {
                Equation eq = this.getEquationSet().getEquation(k);
                double coeff = eq.getValueAt(col);
                if(Double.compare(coeff, 0.d) != 0){
                    newSet.addEquation(eq.add(baseEquation.applyFactor(-1.*coeff)));
                }
            }
        }

        double objfunccoeff = this.getObjectiveFunction().getValueAt(col);
        ObjectiveFunction objfunc = this.getObjectiveFunction();
        if(Double.compare(objfunccoeff, 0.d) != 0){
            objfunc = this.getObjectiveFunction().add(baseEquation.applyFactor(-1.*objfunccoeff).getLeftValues());
        }

        return CanonicalProblem.create(newSet,objfunc);
    }

    public static double[] solve(CanonicalProblem problem){
        ObjectiveFunction objectiveFunction = problem.getObjectiveFunction();
        while(!objectiveFunction.isOptimal()) {
            int col = objectiveFunction.getIndexOfMaximum();
            int row = problem.getPivotRowIdx(col);
            problem = problem.gaussianExclusion(row, col);
            objectiveFunction = problem.getObjectiveFunction();
        }
        return problem.getEquationSet().stream().mapToDouble(eq -> eq.getRightValue()).toArray();
    }


}
