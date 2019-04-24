package math.linear.simplex;

/*
 * Represents the tableau of a canonical linear problem
 */

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Tableau
{
    public final int INDEX_NOT_ASSIGNED = -1;
    private int numberOfProblemVariables = INDEX_NOT_ASSIGNED;
    private int auxiliaryVariablesFirstIndex = INDEX_NOT_ASSIGNED;

    private List<GenericTableauRow> rows;
    private int objectiveFunctionIndex = INDEX_NOT_ASSIGNED;
    private int auxiliaryFunctionIndex = INDEX_NOT_ASSIGNED;
    private int rowSize = 0;
    private int precision = 16;

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
        row.setPrecision(precision);
        this.rows.add(row);
    }

    int addCuttingRow(GenericTableauRow row) {
        row.setPrecision(precision);
        if(auxiliaryFunctionIndex == INDEX_NOT_ASSIGNED)
        {
            addRow(row);
            return this.rows.size() - 1;
        } else {
            this.rows.add(auxiliaryFunctionIndex++, row);
            return auxiliaryFunctionIndex - 1;
        }
    }

    int getRowSize() {
        return this.rowSize;
    }

    void setRowSize(int rowSize){
        this.rowSize = rowSize;
    }

    void setObjectiveFunction(ObjectiveFunctionTableauRow objectiveFunction){
        objectiveFunction.setPrecision(precision);
        if(objectiveFunctionIndex == INDEX_NOT_ASSIGNED) {
            this.rows.add(objectiveFunction);
            objectiveFunctionIndex = this.rows.indexOf(objectiveFunction);
        } else {
            this.rows.set(objectiveFunctionIndex, objectiveFunction);
        }
    }

    void setAuxiliaryFunction(ObjectiveFunctionTableauRow auxiliaryFunction){
        auxiliaryFunction.setPrecision(precision);
        if(auxiliaryFunctionIndex == INDEX_NOT_ASSIGNED) {
            this.rows.add(auxiliaryFunction);
            auxiliaryFunctionIndex = this.rows.indexOf(auxiliaryFunction);
        } else {
            this.rows.set(auxiliaryFunctionIndex, auxiliaryFunction);
        }
    }

    void setNumberOfProblemVariables(int numberOfProblemVariables){
        this.numberOfProblemVariables = numberOfProblemVariables;
    }


    /**
     * Returns the lowest index of columns related to auxiliary variables
     * @return
     */
    int getAuxiliaryVariablesFirstIndex(){
        return auxiliaryVariablesFirstIndex;
    }

    void setAuxiliaryVariablesFirstIndex(int index){
        this.auxiliaryVariablesFirstIndex = index;
    }
    /**
     * Set the pricision for coefficients
     * @param precision
     */
    final void setPrecision(int precision) {
        this.precision = precision;
    }

    final int getPrecision(){
        return this.precision;
    }

    /**
     * Returns a list of rows
     * @return list of rows
     */
    public final List<GenericTableauRow> getRows() {
        return this.rows;
    }

    /**
     * Returns rows of type EquationTableauRow only
     * @return a list of equation rows
     */
    public final List<EquationTableauRow> getEquationRows() {
        return this.rows.stream().filter(row -> row instanceof EquationTableauRow)
            .map(row -> (EquationTableauRow) row).collect(Collectors.toList());
    }

    /**
     * Returns the index of the objective function row
     * @return the number of the row related to the objective function
     */
    public final int getObjectiveFunctionIndex()
    {
        return objectiveFunctionIndex;
    }

    /**
     * Returns the index of the auxiliary function row
     * @return the number of the row related to the auxiliary function
     */
    public final int getAuxiliaryFunctionIndex()
    {
        return auxiliaryFunctionIndex;
    }


    int getNumberOfProblemVariables(){
        return numberOfProblemVariables;
    }

    int insertAdditionalColumn(){
        int index;
        if(auxiliaryVariablesFirstIndex == INDEX_NOT_ASSIGNED){
            this.rows.stream().forEach(row -> row.getCoefficients().add(BigDecimal.ZERO ));
            index = getRowSize();
        } else {
            this.rows.stream().forEach(row -> row.getCoefficients().add(auxiliaryVariablesFirstIndex, BigDecimal.ZERO));
            index = auxiliaryVariablesFirstIndex++;
        }
        setRowSize(getRowSize() + 1);
        return index;
    }
    /**
     * Performs pivot operation. The initial state of the tableau must contain at least one equation row and an objective function.
     * @param rowNumber the row number to pivot at
     * @param columnNumber the column number to pivot at
     */
    public final void pivot(int rowNumber, int columnNumber) {
        if(this.rows.size() == 0) {
            throw new IllegalStateException("Tableau contains no data.");
        }
        if(rowNumber == INDEX_NOT_ASSIGNED) {
            throw new RuntimeException("Solution does not exist.");
        }
        if(rowNumber < 0 || rowNumber > this.rows.size()) {
            throw new IllegalArgumentException("Row number is out of range.");
        }
        if(rowNumber == objectiveFunctionIndex || rowNumber == auxiliaryFunctionIndex) {
            throw new IllegalArgumentException("Objective function cannot be the pivot row.");
        }


        if(columnNumber < 0 || columnNumber > rowSize) {
            throw new IllegalArgumentException("Column number is out of range.");
        }

        GenericTableauRow pivotRow = this.rows.get(rowNumber);
        BigDecimal pivotCoefficient = pivotRow.getCoefficients().get(columnNumber);
        if(pivotCoefficient.equals(BigDecimal.ZERO)) {
            throw new RuntimeException("The pivot coefficient is zero.");
        }

        pivotRow.multiplyBy(BigDecimal.ONE.divide(pivotCoefficient,new MathContext(precision)));
        pivotRow.getCoefficients().set(columnNumber,BigDecimal.ONE);
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

    /**
     * Returns an array of values of the variables representing a solution of the problem
     * @return array of values
     */
    public final double[] getSolution(){
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

    /**
     * Returns an array of values of the variables representing a solution of the problem
     * @return array of values as BigDecimal
     */
    public final List<BigDecimal> getSolutionBigDecimal(){
        List<EquationTableauRow> equationRows = this.getEquationRows();
        List<BigDecimal> solutionValues = new ArrayList<>();
        for(int m = 0; m <= numberOfProblemVariables; m++)
            solutionValues.add(BigDecimal.ZERO);
        for(int k = 0; k < equationRows.size(); k++ ){
            EquationTableauRow equation = equationRows.get(k);
            int idx = equation.getBasicVariableIndex();
            if(idx > 0 && idx <= numberOfProblemVariables){
                solutionValues.set(idx,  equation.getCoefficients().get(0));
            }
        }
        solutionValues.set(0,this.getRows().get(this.getObjectiveFunctionIndex()).getCoefficients().get(0));
        return solutionValues;
    }

}
