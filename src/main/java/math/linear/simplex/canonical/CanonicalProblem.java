package math.linear.simplex.canonical;

import math.linear.basic.Equation;
import math.linear.basic.EquationSet;
import math.linear.basic.ObjectiveFunction;
import math.linear.basic.Relation;

import java.util.Arrays;

public class CanonicalProblem
{

    private EquationSet equationSet;
    private ObjectiveFunction objfunc;
    private ObjectiveFunction auxObjFunc;
    private boolean flagTwoPhases;


    private CanonicalProblem(){
    }

    private CanonicalProblem(EquationSet set, ObjectiveFunction objfunc){
        this.equationSet = set;
        this.objfunc = objfunc;
        flagTwoPhases = false;
    }


    public static CanonicalProblem create(EquationSet eqset, ObjectiveFunction func){
        int inequalityNumber = (int) eqset.stream().filter(equation -> !equation.getRelation().equals(Relation.EQUAL)).count();


        boolean needTwoPhases = eqset.stream().anyMatch(eq -> eq.getRelation().isGreaterOrEqual());

        if (inequalityNumber == 0)
            return new CanonicalProblem(eqset,func);

        EquationSet newSet = EquationSet.create();
        for (int k = 0; k < eqset.getNumberOfEquations(); k++){
            Equation equation = eqset.getEquation(k).normalize();
            equation = extend(equation, inequalityNumber, k);
            newSet.addEquation(equation);
        }

        CanonicalProblem problem = new CanonicalProblem(newSet, extend(func.getCanonical(),inequalityNumber));
        if(needTwoPhases) problem.setTwoPhases();
        return problem;
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
        int rowIdx = -1;
        Equation firstEquation = this.equationSet.getEquation(0);
        double maxRate = -1.;

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
        if(rowIdx < 0) throw new RuntimeException("Optimal index is not found");

        return rowIdx;
    }

    public CanonicalProblem gaussianExclusion(int row, int col){
        Equation baseEquation = this.getEquationSet().getEquation(row);
        double factor = baseEquation.getValueAt(col);
        if(factor == 0.) throw new RuntimeException("Division by zero");

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
                } else {
                    newSet.addEquation(eq);
                }
            }
        }

        double objfunccoeff = this.objfunc.getValueAt(col);
        ObjectiveFunction objfunc = this.objfunc;
        if(Double.compare(objfunccoeff, 0.d) != 0){
            objfunc = this.objfunc.add(baseEquation.applyFactor(-1.*objfunccoeff).getLeftValues());
        }

        CanonicalProblem newProblem = CanonicalProblem.create(newSet,objfunc);

        if(auxObjFunc != null)
        {
            ObjectiveFunction auxFunc = auxObjFunc;
            objfunccoeff = this.auxObjFunc.getValueAt(col);
            if(Double.compare(objfunccoeff, 0.d) != 0){
                auxFunc = this.auxObjFunc.add(baseEquation.applyFactor(-1.*objfunccoeff).getLeftValues());
            }
            newProblem.setAuxFunction(auxFunc);
        }

        return newProblem;
    }

    public void setTwoPhases(){
        this.flagTwoPhases = true;
    }

    public boolean isTwoPhases(){
        return flagTwoPhases;
    }

    public void setAuxFunction(ObjectiveFunction auxFunc){
        if(this.getObjectiveFunction().getValues().length != auxFunc.getValues().length)
            throw new RuntimeException("Auxiliery objective function length mismatch");
        auxObjFunc = auxFunc;
    }

    public ObjectiveFunction  getAuxFunction(){
        return this.auxObjFunc;
    }

}
