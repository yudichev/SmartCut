package math.linear.basic.tableau;

/*
 * Builds a tableau from initial problem
 */

import math.linear.basic.Relation;
import math.linear.basic.problem.Problem;
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

    private Problem problem;
    private boolean isCanonicalForm = false;
    private int numberOfVariables = NOT_ASSIGNED;
    private int totalNumberOfVariables = NOT_ASSIGNED;
    private int nonBasicVariablesFirstIndex = NOT_ASSIGNED ;
    private int auxilieryVariablesFirstIndex = NOT_ASSIGNED;

    private TableauBuilder() {}

    /**
     * Returns an instance of the tableau builder
     * @return
     */
    public static TableauBuilder getInstance() {
        return new TableauBuilder();
    }

    /**
     * Sets the problem
     * @param probliem
     */
    public void setProbliem(Problem probliem){
        this.problem = probliem;
    }


    /**
     * Build a tableau to be processed by simplex method
     * @return tableau
     */
    public Tableau build() {
        checkData();
        analyze();
        List<ProblemEquation> equations = this.problem.getEquations();
        List<GenericTableauRow> rows = new ArrayList<>(equations.size() + 2);

        Tableau tableau = Tableau.getInstance();
        tableau.setPrecision(problem.getPrecision());
        tableau.setNumberOfProblemVariables(numberOfVariables);

        for(int k = 0, knb = nonBasicVariablesFirstIndex, kaux = auxilieryVariablesFirstIndex; k < equations.size(); k++) {
            ProblemEquation equation = equations.get(k);
            Relation relation = equation.getRelation();
            double freeCoefficient = equation.getCoefficientAt(0);
            boolean isFreeCoefficientNegative = Double.compare(freeCoefficient, ZERO) < 0;

            if(isFreeCoefficientNegative)
                relation = relation.invert();

            List<BigDecimal> coeffs = new ArrayList<>(totalNumberOfVariables);

            for(int m = 0; m < nonBasicVariablesFirstIndex; m++){
                double coeff = equation.getCoefficientAt(m);
                if(isFreeCoefficientNegative) {
                    coeff = coeff * (-1);
                }
                coeffs.add(m, BigDecimal.valueOf(coeff));
            }
            for(int m = nonBasicVariablesFirstIndex; m < totalNumberOfVariables; m++){
                coeffs.add(m, BigDecimal.ZERO);
            }

            if(auxilieryVariablesFirstIndex == NOT_ASSIGNED) {
                coeffs.set(knb++, BigDecimal.ONE);
                tableau.addRow(new EquationTableauRow(k + nonBasicVariablesFirstIndex, coeffs));
            } else {
                if(relation.isGreaterOrEqual()){
                    coeffs.set(kaux, BigDecimal.ONE);
                    coeffs.set(knb, BigDecimal.ONE.negate());
                    tableau.addRow(new EquationTableauRow(kaux, coeffs));
                    kaux++;
                    knb++;
                } else if(relation.isEqual()){
                    coeffs.set(kaux, BigDecimal.ONE);
                    tableau.addRow(new EquationTableauRow(kaux, coeffs));
                    kaux++;
                } else {
                    coeffs.set(knb, BigDecimal.ONE);
                    tableau.addRow(new EquationTableauRow(knb, coeffs));
                    knb++;
                }

            }
        }

        List<BigDecimal> objectiveFunctionCoeffs = new ArrayList<>(totalNumberOfVariables);
        ProblemObjectiveFunction objectiveFunction = this.problem.getObjectiveFunction();
        double factor = objectiveFunction.getType().isFindMaximum() ? -1.d : 1.d;
        for(int m = 0; m < nonBasicVariablesFirstIndex; m++) {
            objectiveFunctionCoeffs.add(m,BigDecimal.valueOf(objectiveFunction.getCoefficientAt(m) * factor));
        }
        for(int m = nonBasicVariablesFirstIndex; m < totalNumberOfVariables; m++) {
            objectiveFunctionCoeffs.add(m,BigDecimal.ZERO);
        }

        tableau.setObjectiveFunction(new ObjectiveFunctionTableauRow(ObjectiveFunctionTableauRow.Type.STANDARD, objectiveFunctionCoeffs));

        if(auxilieryVariablesFirstIndex != NOT_ASSIGNED) {
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
        if(problem == null) {
            throw new IllegalStateException("Problem is not set");
        }
        problem.validate();
        List<ProblemEquation> equations = problem.getEquations();
        if(equations == null || equations.isEmpty()) {
            throw new IllegalStateException("Equations are not assigned");
        }
        ProblemObjectiveFunction objectiveFunction = this.problem.getObjectiveFunction();
        if(objectiveFunction == null) {
            throw new IllegalStateException("Objective function is not assigned");
        }
    }

    private void analyze() {
        List<ProblemEquation> equations = problem.getEquations();
        totalNumberOfVariables = equations.get(0).getLength();
        numberOfVariables = totalNumberOfVariables - 1;
        int numberOfAdditionalVariables = 0;
        int numberOfAuxilieryVariables = 0;
        for(ProblemEquation eq : equations) {
            Relation relation = eq.getRelation();
            double freeCoefficient = eq.getCoefficientAt(0);
            boolean isFreeCoefficientNegative = Double.compare(freeCoefficient, ZERO) < 0;
            if(relation.isEqual()) {
                numberOfAuxilieryVariables++;
            } else if(((relation.isLessOrEqual() && isFreeCoefficientNegative)
                || (relation.isGreaterOrEqual() && !isFreeCoefficientNegative))){
                numberOfAuxilieryVariables++;
                numberOfAdditionalVariables++;
            } else {
                numberOfAdditionalVariables++;
            }
        }
        nonBasicVariablesFirstIndex = totalNumberOfVariables;
        totalNumberOfVariables += numberOfAdditionalVariables;
        if(numberOfAuxilieryVariables > 0) {
            auxilieryVariablesFirstIndex = totalNumberOfVariables;
            totalNumberOfVariables += numberOfAuxilieryVariables;
        }
    }
}
