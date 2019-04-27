package math.linear.problem;

import math.linear.basic.MathUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Problem {
    private List<ProblemEquation> equations;
    private ProblemObjectiveFunction objectiveFunction;
    private int precision = 16;
    private int numberOfVariables = MathUtils.NOT_ASSIGNED;

    private Problem() {
        equations = new ArrayList<>();
    }

    public static Problem getInstance() {
        return new Problem();
    }

    public void addEquation(ProblemEquation equation){
        this.equations.add(equation);
        if(numberOfVariables == MathUtils.NOT_ASSIGNED){
            numberOfVariables = equation.getLength() - 1;
        }
    }

    public void addAllEquations(List<ProblemEquation> equations){
        this.equations.addAll(equations);
        if(numberOfVariables == MathUtils.NOT_ASSIGNED){
            numberOfVariables = equations.get(0).getLength() - 1;
        }
    }

    public void addObjectiveFunction(ProblemObjectiveFunction objectiveFunction){
        this.objectiveFunction = objectiveFunction;
    }

    public int getNumberOfVariables(){
        return numberOfVariables;
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
        int length = MathUtils.NOT_ASSIGNED;
        for(ProblemEquation eq : equations){
            if(length == -1) {
                length = eq.getLength();
            } else if(length != eq.getLength()) {
                throw new RuntimeException("Equations system is inconsistent. Equations must have equal length.");
            }
        }

    }


}
