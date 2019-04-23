package math.linear.simplex;

/*
 * Builds a tableau from initial problem
 */

import math.linear.basic.Relation;
import math.linear.problem.Problem;
import math.linear.problem.ProblemEquation;
import math.linear.problem.ProblemObjectiveFunction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TableauBuilder
{
    private static int NOT_ASSIGNED = -1;
    private static double ZERO = 0.d;

    private Problem problem;
    private int numberOfVariables = NOT_ASSIGNED;
    private int totalNumberOfVariables = NOT_ASSIGNED;
    private int nonBasicVariablesFirstIndex = NOT_ASSIGNED ;
    private int auxiliaryVariablesFirstIndex = NOT_ASSIGNED;

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

        Tableau tableau = Tableau.getInstance();
        tableau.setPrecision(problem.getPrecision());
        tableau.setNumberOfProblemVariables(numberOfVariables);



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

        addEquations(equations, tableau);


        if(auxiliaryVariablesFirstIndex != NOT_ASSIGNED) {
            List<BigDecimal> auxFunctionCoeffs = new ArrayList<>(totalNumberOfVariables);
            for(int m = 0; m < auxiliaryVariablesFirstIndex; m++) {
                auxFunctionCoeffs.add(m,BigDecimal.ZERO);
            }
            for(int m = auxiliaryVariablesFirstIndex; m < totalNumberOfVariables; m++) {
                auxFunctionCoeffs.add(m,BigDecimal.ONE);
            }

            tableau.setAuxiliaryFunction( new ObjectiveFunctionTableauRow(ObjectiveFunctionTableauRow.Type.AUXILIARY, auxFunctionCoeffs));
        }

        tableau.setAuxiliaryVariablesFirstIndex(auxiliaryVariablesFirstIndex);


        return tableau;
    }

    private void addEquations(List<ProblemEquation> equations, Tableau tableau)
    {
        for(int k = 0, knb = nonBasicVariablesFirstIndex, kaux = auxiliaryVariablesFirstIndex; k < equations.size(); k++) {
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

            if(auxiliaryVariablesFirstIndex == NOT_ASSIGNED) {
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
    }

    private void checkData() {
        if(problem == null) {
            throw new IllegalStateException("Problem is not set");
        }
        problem.validate();
    }

    private void analyze() {
        List<ProblemEquation> equations = problem.getEquations();
        totalNumberOfVariables = equations.get(0).getLength();
        numberOfVariables = totalNumberOfVariables - 1;
        int numberOfAdditionalVariables = 0;
        int numberOfAuxiliaryVariables = 0;
        for(ProblemEquation eq : equations) {
            Relation relation = eq.getRelation();
            double freeCoefficient = eq.getCoefficientAt(0);
            boolean isFreeCoefficientNegative = Double.compare(freeCoefficient, ZERO) < 0;
            if(relation.isEqual()) {
                numberOfAuxiliaryVariables++;
            } else if(((relation.isLessOrEqual() && isFreeCoefficientNegative)
                || (relation.isGreaterOrEqual() && !isFreeCoefficientNegative))){
                numberOfAuxiliaryVariables++;
                numberOfAdditionalVariables++;
            } else {
                numberOfAdditionalVariables++;
            }
        }
        nonBasicVariablesFirstIndex = totalNumberOfVariables;
        totalNumberOfVariables += numberOfAdditionalVariables;
        if(numberOfAuxiliaryVariables > 0) {
            auxiliaryVariablesFirstIndex = totalNumberOfVariables;
            totalNumberOfVariables += numberOfAuxiliaryVariables;
        }
    }
}
