package math.linear2.basic;

import java.util.ArrayList;
import java.util.List;

public class EquationSet {
    private static final String ERROR_LENGTH_MISMATCH = "Equation length mismatches the set.";

    private List<Equation> equations;

    private EquationSet(){
        this.equations = new ArrayList<>();
    }

    public static EquationSet create(){
        return new EquationSet();
    }

    public void addEquation(Equation eq){
        if(equations.size() > 0 && equations.get(0).getCoefficients().size() != eq.getCoefficients().size()){
            throw new RuntimeException(ERROR_LENGTH_MISMATCH);
        }
        this.equations.add(eq);
    }

    public List<Equation> getEquations(){
        return equations;
    }

    public int getNumberOfEquations(){
        return this.equations.size();
    }
}
