package math.linear.basic;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class EquationSet {
    private static final String ERROR_LENGTH_MISMATCH = "Equation length mismatches the set.";

    private final List<Equation> equationList;
    private int rowSize = 0;

    private EquationSet(){
        equationList = new ArrayList<>();
    }

    public final void addEquation(Equation equation){
        if(equationList.size() == 0){
            rowSize = equation.getLength();
        } else if(rowSize != equation.getLength()){
            throw new RuntimeException(ERROR_LENGTH_MISMATCH);
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

    public Stream<Equation> stream(){
        return this.equationList.stream();
    }

    public static EquationSet create(){
        return new EquationSet();
    }

}
