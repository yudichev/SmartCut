package math.linear.problem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Problem {
    private List<ProblemEquation> equations;
    private ProblemObjectiveFunction objectiveFunction;
    private int precision = 16;

    private Problem() {
        equations = new ArrayList<>();
    }

    public static Problem getInstance() {
        return new Problem();
    }

    public void addEquation(ProblemEquation equation){
        this.equations.add(equation);
    }

    public void addObjectiveFunction(ProblemObjectiveFunction objectiveFunction){
        this.objectiveFunction = objectiveFunction;
    }

    public List<ProblemEquation> getEquations(){
        return Collections.unmodifiableList(equations);
    }

    public ProblemObjectiveFunction getObjectiveFunction(){
        return objectiveFunction;
    }

    public void setPrecision(int precision){
        this.precision = precision;
    }

    public int getPrecision() {
        return this.precision;
    }

    public void validate() {
        if(equations == null || equations.size() == 0) {
            throw new IllegalStateException("No equations assigned");
        }
        if(objectiveFunction == null){
            throw new IllegalStateException("No objective function assigned");
        }
        int length = -1;
        for(ProblemEquation eq : equations){
            if(length == -1) {
                length = eq.getLength();
            } else if(length != eq.getLength()) {
                throw new RuntimeException("Equations system is inconsistent. Equations must have equal length.");
            }
        }

    }


}
