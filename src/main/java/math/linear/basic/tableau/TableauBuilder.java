package math.linear.basic.tableau;

/*
 * Builds a tableau from initial problem
 */

import math.linear.basic.Relation;
import math.linear.basic.problem.ProblemEquation;
import math.linear.basic.problem.ProblemObjectiveFunction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TableauBuilder
{
    private static int NOT_ASSIGNED = -1;
    private static double ZERO = 0.d;

    private List<ProblemEquation> equations;
    private ProblemObjectiveFunction objectiveFunction;
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


    public Tableau build() {
        checkData();

        nonBasicVariablesFirstIndex =  equations.get(0).getLength();
        totalNumberOfVariables = equations.get(0).getLength() + equations.size();

        int auxilieryVariablesCount = equations.stream().mapToInt(TableauBuilder::getNumberOfNeededAuxilieryVariables).sum();

        if(auxilieryVariablesCount > 0) {
            auxilieryVariablesFirstIndex = totalNumberOfVariables;
            totalNumberOfVariables += auxilieryVariablesCount;
        }

        List<GenericTableauRow> rows = new ArrayList<>(equations.size() + 2);

        Tableau tableau = Tableau.getInstance();
        for(int k = 0; k < equations.size(); k++) {
            ProblemEquation equation = equations.get(k);
            Relation relation = equation.getRelation();
            double freeCoefficient = equation.getCoefficientAt(0);
            boolean isFreeCoefficientNegative = Double.compare(freeCoefficient, ZERO) < 0;

            List<BigDecimal> coeffs = new ArrayList<>(totalNumberOfVariables);

            for(int m = 0; m < nonBasicVariablesFirstIndex; m++){
                double coeff = equation.getCoefficientAt(m);
                if(isFreeCoefficientNegative) {
                    coeff = coeff * (-1);
                    relation = relation.invert();
                }
                coeffs.add(m, BigDecimal.valueOf(coeff));
            }
            for(int m = nonBasicVariablesFirstIndex; m < totalNumberOfVariables; m++){
                coeffs.add(m, BigDecimal.ZERO);
            }

            if(auxilieryVariablesCount == 0) {
                coeffs.set(k + nonBasicVariablesFirstIndex, BigDecimal.ONE);
                tableau.addRow(new EquationTableauRow(k + nonBasicVariablesFirstIndex, coeffs));
            } else {
                if(relation.isGreaterOrEqual()){
                    coeffs.set(k + auxilieryVariablesFirstIndex, BigDecimal.ONE);
                    coeffs.set(k + nonBasicVariablesFirstIndex, BigDecimal.ONE.negate());
                    tableau.addRow(new EquationTableauRow(k + auxilieryVariablesFirstIndex, coeffs));
                } else if(relation.isEqual()){
                    coeffs.set(k + auxilieryVariablesFirstIndex, BigDecimal.ONE);
                    tableau.addRow(new EquationTableauRow(k + auxilieryVariablesFirstIndex, coeffs));
                }
            }
        }

        List<BigDecimal> objectiveFunctionCoeffs = new ArrayList<>(totalNumberOfVariables);
        double factor = objectiveFunction.getType().isFindMaximum() ? -1.d : 1.d;
        for(int m = 0; m < nonBasicVariablesFirstIndex; m++) {
            objectiveFunctionCoeffs.add(m,BigDecimal.valueOf(objectiveFunction.getCoefficientAt(m) * factor));
        }

        tableau.setObjectiveFunction(new ObjectiveFunctionTableauRow(ObjectiveFunctionTableauRow.Type.STANDARD, objectiveFunctionCoeffs));

        if(auxilieryVariablesCount > 0) {
            List<BigDecimal> auxFunctionCoeffs = new ArrayList<>(totalNumberOfVariables);
            for(int m = 0; m < auxilieryVariablesFirstIndex; m++) {
                auxFunctionCoeffs.add(m,BigDecimal.ZERO);
            }
            for(int m = auxilieryVariablesFirstIndex; m < totalNumberOfVariables; m++) {
                auxFunctionCoeffs.add(m,BigDecimal.ONE);
            }
            tableau.setAuxilieryFunction( new ObjectiveFunctionTableauRow(ObjectiveFunctionTableauRow.Type.AUXILIERY, auxFunctionCoeffs));
        }

        return tableau;
    }

    private void checkData() {
        if(equations == null || equations.isEmpty()) {
            throw new IllegalStateException("Equations are not assigned");
        }
        if(objectiveFunction == null) {
            throw new IllegalStateException("Objective function is not assigned");
        }
    }


    private static int getNumberOfNeededAuxilieryVariables(ProblemEquation equation){
        Relation relation = equation.getRelation();
        double freeCoefficient = equation.getCoefficientAt(0);
        if(relation.isEqual()) {
            return 1;
        } else if(((relation.isLessOrEqual() && Double.compare(freeCoefficient, ZERO) < 0)
                || (relation.isGreaterOrEqual() && Double.compare(freeCoefficient, ZERO) > 0))) {
            return 2;
        } else {
            return 0;
        }
    }


}
