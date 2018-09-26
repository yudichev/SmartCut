package math.linear.simplex.canonical;

import math.linear.basic.Equation;
import math.linear.basic.EquationSet;
import math.linear.basic.Relation;

public class CanonicalProblem {
    private EquationSet originalSet;

    private CanonicalProblem(){
    }

    private CanonicalProblem(EquationSet set){
        this.originalSet = set;
    }


    public static CanonicalProblem createFromEquationSet(EquationSet eqset){
        int inqualityNumber = (int) eqset.stream().filter(equation -> !equation.getRelation().equals(Relation.EQUAL)).count();

        EquationSet newSet = EquationSet.create();
        for (int k = 0; k < eqset.getNumberOfEquations(); k++){
            Equation equation = eqset.getEquation(k).normalize();
            equation = extend(equation, inqualityNumber, k);
            newSet.addEquation(equation);
        }
        return new CanonicalProblem(newSet);
    }

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
}
