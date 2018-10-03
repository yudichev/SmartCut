package math.linear.simplex.canonical;

import math.linear.basic.Equation;
import math.linear.basic.EquationSet;
import math.linear.basic.ObjectiveFunction;
import math.linear.basic.Relation;

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

        EquationSet newSet = EquationSet.create();
        for (int k = 0; k < eqset.getNumberOfEquations(); k++){
            Equation equation = eqset.getEquation(k).normalize();
            equation = extend(equation, inqualityNumber, k);
            newSet.addEquation(equation);
        }
        return new CanonicalProblem(newSet, func.getCanonical());
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
        if(Double.compare(srcEquation.getRightValue(),0.0d) > 0){
            extLvalues[srcLength + rowNumber] = 1.0d;
        } else {
            extLvalues[srcLength + rowNumber] = -1.0d;
        }
        return Equation.of(extLvalues, Relation.EQUAL, srcEquation.getRightValue());
    }


    public int getRowIndexForIteration(int columnIdx){
        int rowIdx = 0;
        double maxRate = this.equationSet.getEquation(0).getValueAt(columnIdx);


        int columnSize = this.equationSet.getNumberOfEquations();
        for(int k = 0; k < columnSize; k++){
            Equation eq = this.equationSet.getEquation(k);
            double val = eq.getValueAt(columnIdx);
            double rval = eq.getRightValue();
            if(rval > 0. && val >= 0.){
                double rate = rval / val;
                if(rate > maxRate){
                    maxRate = rate;
                    rowIdx = k;
                }
            }
        }

        return rowIdx;
    }
}
