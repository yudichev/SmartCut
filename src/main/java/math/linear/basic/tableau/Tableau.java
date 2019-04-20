package math.linear.basic.tableau;

/*
 * Copyright 2001-2019 by HireRight, Inc. All rights reserved.
 * This software is the confidential and proprietary information
 * of HireRight, Inc. Use is subject to license terms.
 */

/*
 * Represents the tableau of a canonical linear problem
 */

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Tableau
{
    public final int INDEX_NOT_ASSIGNED = -1;
    private int numberOfProblemVariables = INDEX_NOT_ASSIGNED;

    private List<GenericTableauRow> rows;
    private int objectiveFunctionIndex = INDEX_NOT_ASSIGNED;
    private int auxilieryFunctionIndex = INDEX_NOT_ASSIGNED;
    private int rowSize = 0;
    private int precision = 16;

    public int scale = 20;

    private Tableau() {
        rows = new ArrayList<>();
    };

    static Tableau getInstance() {
        return new Tableau();
    }


    void addRow(GenericTableauRow row) {

        int size = row.getSize();
        if(rowSize == 0) {
            rowSize = size;
        }

        if(rowSize != size || size == 0){
            throw new IllegalArgumentException("The size of the row to be added is incompatible");
        }
        this.rows.add(row);
    }

    void addAllRows(List<GenericTableauRow> rows){
        if(rows.isEmpty()) return;

        int size = rows.get(0).getSize();
        if(rowSize == 0) {
            rowSize = size;
        }

        if(rowSize != size || size == 0){
            throw new IllegalArgumentException("The size of the rows to be added is incompatible");
        }
        this.rows.addAll(rows);
    }

    void setObjectiveFunction(ObjectiveFunctionTableauRow objectiveFunction){
        if(objectiveFunctionIndex == INDEX_NOT_ASSIGNED) {
            this.rows.add(objectiveFunction);
            objectiveFunctionIndex = this.rows.indexOf(objectiveFunction);
        } else {
            this.rows.set(objectiveFunctionIndex, objectiveFunction);
        }
    }

    void setAuxilieryFunction(ObjectiveFunctionTableauRow auxilieryFunction){
        if(auxilieryFunctionIndex == INDEX_NOT_ASSIGNED) {
            this.rows.add(auxilieryFunction);
            auxilieryFunctionIndex = this.rows.indexOf(auxilieryFunction);
        } else {
            this.rows.set(auxilieryFunctionIndex, auxilieryFunction);
        }
    }

    void setNumberOfProblemVariables(int numberOfProblemVariables){
        this.numberOfProblemVariables = numberOfProblemVariables;
    }
    /**
     * Set the pricision for coefficients
     * @param precision
     */
    public final void setPrecision(int precision) {
        this.precision = precision;
    }

    /**
     * Returns an unmodifiable list of rows
     * @return
     */
    public final List<GenericTableauRow> getRows() {
        return Collections.unmodifiableList(this.rows);
    }

    public final List<EquationTableauRow> getEquationRows() {
        return this.rows.stream().filter(row -> row instanceof EquationTableauRow)
            .map(row -> (EquationTableauRow) row).collect(Collectors.toList());
    }

    /**
     * Returns the index of the objective function row
     * @return
     */
    public final int getObjectiveFunctionIndex()
    {
        return objectiveFunctionIndex;
    }

    /**
     * Returns the index of the auxiliery function row
     * @return
     */
    public final int getAuxilieryFunctionIndex()
    {
        return auxilieryFunctionIndex;
    }

    public final boolean isTwoPhases() {
        return auxilieryFunctionIndex != INDEX_NOT_ASSIGNED;
    }

    /**
     * Performs pivot operation. The initial state of the tableau must contain at least one equation row and an objective function.
     * @param rowNumber the row number to pivot at
     * @param columnNumber the column number to pivot at
     */
    public final void pivot(int rowNumber, int columnNumber) {
        if(this.rows.size() == 0) {
            throw new IllegalStateException("Tableau contains no data");
        }
        if(rowNumber < 0 || rowNumber > this.rows.size()) {
            throw new IllegalArgumentException("Row number is out of range");
        }
        if(rowNumber == objectiveFunctionIndex || rowNumber == auxilieryFunctionIndex) {
            throw new IllegalArgumentException("Objective function cannot be the pivot row");
        }

        if(columnNumber < 0 || columnNumber > rowSize) {
            throw new IllegalArgumentException("Column number is out of range");
        }

        GenericTableauRow pivotRow = this.rows.get(rowNumber);
        BigDecimal pivotCoefficient = pivotRow.getCoefficients().get(columnNumber);
        if(pivotCoefficient.equals(BigDecimal.ZERO)) {
            throw new RuntimeException("The pivot coefficient is zero");
        }

        pivotRow.multiplyBy(BigDecimal.ONE.divide(pivotCoefficient,new MathContext(precision)));
        if(pivotRow instanceof EquationTableauRow){
            EquationTableauRow equationRow = (EquationTableauRow) pivotRow;
            equationRow.setBasicVariableIndex(columnNumber);
        }

        for(int k = 0 ; k < this.rows.size(); k++) {
            GenericTableauRow currentRow = this.rows.get(k);
            BigDecimal coeff = currentRow.getCoefficients().get(columnNumber);
            if(k != rowNumber) {
                currentRow.addWithFactor(pivotRow, coeff.negate());
            }
        }
    }

    public double[] getSolution(){
        List<EquationTableauRow> equationRows = this.getEquationRows();
        double[] solutionValues = new double[numberOfProblemVariables];
        Arrays.fill(solutionValues,0.d);
        for(int k = 0; k < equationRows.size(); k++ ){
            EquationTableauRow equation = equationRows.get(k);
            int idx = equation.getBasicVariableIndex();
            if(idx > 0 && idx <= numberOfProblemVariables){
                solutionValues[idx - 1] = equation.getCoefficients().get(0).doubleValue();
            }
        }
        return solutionValues;
    }


}
