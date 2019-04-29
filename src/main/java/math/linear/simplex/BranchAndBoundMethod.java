package math.linear.simplex;

import math.linear.basic.MathUtils;
import math.linear.basic.Relation;
import math.linear.problem.Problem;
import math.linear.problem.ProblemEquation;
import math.linear.problem.ProblemObjectiveFunction;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class BranchAndBoundMethod {

    public static Tableau applyTo(Problem problem, Tableau tableau){
        Problem nextProblem = problem;
        int precision = tableau.getPrecision();
        List<BigDecimal> solution = tableau.getSolutionBigDecimal();

        int nonIntegerIndex;
        BigDecimal value = null;
        Tableau nextTableau = tableau;
        do {
            nonIntegerIndex = MathUtils.NOT_ASSIGNED;
            for (int k = 1; k < solution.size(); k++) {
                value = solution.get(k);
                if (!MathUtils.isInteger(value, precision)) {
                    nonIntegerIndex = k;
                    break;
                }
            }

            if (nonIntegerIndex == MathUtils.NOT_ASSIGNED) {
                return nextTableau;
            }


            Iteration nextResult = getNextStep(nextProblem, precision, nonIntegerIndex, value);
            nextTableau = nextResult.solution;
            nextProblem = nextResult.problem;
            if (nextTableau != null)
                solution = nextTableau.getSolutionBigDecimal();
        } while (true);
    }

    private static Iteration getNextStep(Problem problem, int precision, int nonIntegerIndex, BigDecimal value) {
        double floor = value.setScale(0, RoundingMode.FLOOR).doubleValue();
        double ceiling = value.setScale(0,RoundingMode.CEILING).doubleValue();

        double[] equation = new double[problem.getNumberOfVariables()];
        Arrays.fill(equation,0.d);
        equation[nonIntegerIndex - 1] = 1.d;

        Problem problem1 = Problem.getInstance();
        problem1.setPrecision(precision);
        problem1.addAllEquations(problem.getEquations());
        problem1.addEquation(ProblemEquation.make(equation, Relation.LESS_OR_EQUAL, floor));
        problem1.addObjectiveFunction(problem.getObjectiveFunction().copy());


        TableauBuilder builder = TableauBuilder.getInstance();
        builder.setProbliem(problem1);
        Tableau tableau1 = builder.build();


        Tableau solution1;
        try {
            solution1 = SimplexMethod.applyTo(tableau1);
            solution1.getSolutionBigDecimal().stream().forEach(v -> System.out.println(v.toPlainString() + " "));

        } catch (RuntimeException ex ){
            solution1 = null;
            System.out.println(ex.getMessage());
        }

        Problem problem2 = Problem.getInstance();
        problem2.setPrecision(precision);
        problem2.addAllEquations(problem.getEquations());
        problem2.addEquation(ProblemEquation.make(equation, Relation.GREATER_OR_EQUAL, ceiling));
        problem2.addObjectiveFunction(problem.getObjectiveFunction().copy());


        builder = TableauBuilder.getInstance();
        builder.setProbliem(problem2);
        Tableau tableau2 = builder.build();


        Tableau solution2;
        try{
            solution2 = SimplexMethod.applyTo(tableau2);
            solution2.getSolutionBigDecimal().stream().forEach(v -> System.out.println(v.toPlainString() + " "));
        } catch (RuntimeException ex){
            solution2 = null;
            System.out.println(ex.getMessage());
        }

        //TODO implement algorithm of choosing solutions and proceed to next iteration
        if(solution1 == null && solution2 != null)
            return new Iteration(problem2,solution2);

        if(solution1 != null && solution2 == null)
            return new Iteration(problem1, solution1);

        if(solution1 != null && solution2 != null){
            BigDecimal free1 = solution1.getSolutionBigDecimal().get(0);
            BigDecimal free2 = solution2.getSolutionBigDecimal().get(0);

            double dfree1 = evaluateObjective(problem.getObjectiveFunction(),solution1.getSolution());
            double dfree2 = evaluateObjective(problem.getObjectiveFunction(),solution2.getSolution());
            if(Double.compare(dfree1,dfree2) > 0) {
                return new Iteration(problem1,solution1);
            } else {
                return new Iteration(problem2, solution2);
            }
        }

        return null;
    }

    private static double evaluateObjective(ProblemObjectiveFunction objFunc, double[] solution){
        double[] objFunCoeff = objFunc.getCoefficients();
        double result = 0.;
        for(int k = 1; k < solution.length; k++){
            result += objFunCoeff[k] * solution[k];
        }
        return result;
    }

    private static class Iteration {
        Problem problem;
        Tableau solution;
        Iteration(Problem problem, Tableau solution){
            this.problem = problem;
            this.solution = solution;
        }
    }
}
