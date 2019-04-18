package math.linear.basic.tableau;

/*
 * Builds a tableau from initial problem
 */

import math.linear.basic.Relation;
import math.linear.basic.problem.ProblemEquation;
import math.linear.basic.problem.ProblemObjectiveFunction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

public class TableauBuilder
{
    private static int NOT_ASSIGNED = -1;

    private List<ProblemEquation> equations;
    private ProblemObjectiveFunction objectiveFunction;
    private ProblemObjectiveFunction auxilieryFunction;
    private boolean isCanonicalForm = false;
    private int totalNumberOfVariables = NOT_ASSIGNED;
    private int nonBasicVariablesFirstIndex = NOT_ASSIGNED ;
    private int auxilieryVariablesFirstIndex = NOT_ASSIGNED;

    private TableauBuilder() {}

    public static TableauBuilder getInstance() {
        return new TableauBuilder();
    }

    public TableauBuilder setEquations(List<ProblemEquation> equations) {
        this.equations = equations;
        return this;
    }

    public TableauBuilder setObjectiveFunction(ProblemObjectiveFunction objectiveFunction) {
        this.objectiveFunction = objectiveFunction;
        return this;
    }

    public TableauBuilder setAuxilieryFunction(ProblemObjectiveFunction auxilieryFunction) {
        this.auxilieryFunction = auxilieryFunction;
        return this;
    }

    public Tableau build() {
        checkData();

        nonBasicVariablesFirstIndex =  equations.get(0).getLength();
        totalNumberOfVariables = equations.get(0).getLength() + equations.size();

        int auxilieryVariablesCount = (int) equations.stream().filter(TableauBuilder::needsAuxilieryVariable).count();

        if(auxilieryVariablesCount > 0) {
            auxilieryVariablesFirstIndex = totalNumberOfVariables;
            totalNumberOfVariables += auxilieryVariablesCount;
        }

        List<GenericTableauRow> rows = new ArrayList<>(equations.size() + 2);
        for(int k = 0; k < equations.size(); k++) {
            ProblemEquation equation = equations.get(k);
            Relation relation = equation.getRelation();
            double freeCoefficient = equation.getCoefficientAt(0);
            List<BigDecimal> coeffs = new ArrayList<>(totalNumberOfVariables);
            for(int m = 0; m < totalNumberOfVariables; m++){
                double coeff = equation.getCoefficientAt(m);
                if(Double.compare(freeCoefficient, 0.d) < 0)
                    coeff = coeff * (-1);
                coeffs.set(m, BigDecimal.valueOf(coeff));
            }
            for(int m = nonBasicVariablesFirstIndex; m < totalNumberOfVariables; m++){
                coeffs.set(m, BigDecimal.ZERO);
            }
            coeffs.set(k + nonBasicVariablesFirstIndex, BigDecimal.ONE);

            if(auxilieryVariablesCount > 0 && needsAuxilieryVariable(equation)) {
                coeffs.set(k + auxilieryVariablesFirstIndex, BigDecimal.ONE);
                coeffs.set(k + nonBasicVariablesFirstIndex, BigDecimal.ONE.negate());
                rows.add(new EquationTableauRow(k + auxilieryVariablesFirstIndex, coeffs));
            } else {
                coeffs.set(k + nonBasicVariablesFirstIndex, BigDecimal.ONE);
                rows.add(new EquationTableauRow(k + nonBasicVariablesFirstIndex, coeffs));
            }


        }

        return null;
    }

    private void checkData() {
        if(equations == null || equations.isEmpty()) {
            throw new IllegalStateException("Equations are not assigned");
        }
        if(objectiveFunction == null) {
            throw new IllegalStateException("Objective function is not assigned");
        }
    }

    private static EquationTableauRow getEquationRow(ProblemEquation equation) {
        stream(equation.getCoefficients()).
    }

    private static boolean needsAuxilieryVariable(ProblemEquation equation) {
        Relation relation = equation.getRelation();
        double freeCoefficient = equation.getCoefficientAt(0);
        return ((relation.isLessOrEqual() && Double.compare(freeCoefficient, 0.d) < 0)
            || (relation.isGreaterOrEqual() && Double.compare(freeCoefficient, 0.d) > 0));

    }


}
