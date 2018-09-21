package math.linear.basic;

import java.util.ArrayList;
import java.util.List;

public class EquationSet {
    private static String ERORR_LENGTH_MISMATCH = "Equation length mismatches the set.";

    private List<Equation> equationList;
    private int rowSize = 0;

    private EquationSet(){
        equationList = new ArrayList<>();
    }

    public final void addEquation(Equation equation){
        if(equationList.size() == 0){
            rowSize = equation.getLength();
        } else if(rowSize != equation.getLength()){
            throw new RuntimeException(ERORR_LENGTH_MISMATCH);
        }
        this.equationList.add(equation);
    }

    public final int getNumberOfEquations(){
        return equationList.size();
    }

    public final Equation getEquation(int n){
        return this.equationList.get(n);
    }

    public final EquationSet swapEquations(int j, int k){
        Equation eqj = this.equationList.get(j).copy();
        Equation eqk = this.equationList.get(k).copy();
        EquationSet eqSet = new EquationSet();
        for(int m = 0; m < this.equationList.size(); m++){
            if(m == j) {eqSet.addEquation(eqk);}
            else if(m == k) {eqSet.addEquation(eqj);}
            else {eqSet.addEquation(this.equationList.get(m).copy());}
        }
        return eqSet;
    }

    public final EquationSet swapColumns(int j, int k){
        EquationSet newEquationSet = create();
        for(Equation eq : this.equationList){
            double[] lvalues = eq.getLeftValues();
            lvalues[j] = eq.getValueAt(k);
            lvalues[k] = eq.getValueAt(j);
            newEquationSet.addEquation(Equation.of(lvalues,eq.getRelation(),eq.getRightValue()));
        }
        return newEquationSet;
    }

    public static EquationSet create(){
        return new EquationSet();
    }

}
