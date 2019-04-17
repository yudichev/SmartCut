package math.linear.basic;

/*
 * Class represents a row in a tableau for a linear problem. It incorporates all operations with a row.
 */

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class TableauRow
{
    private int basicVariableIndex;
    private List<BigDecimal> coefficients;

    private TableauRow() {};

    /**
     * Constructor of TableauRow.
     * @param index index of basic variable calculated via coefficients of this row
     * @param coefficients a list of coefficients
     */
    TableauRow(int index, List<BigDecimal> coefficients) {
        this.basicVariableIndex = index;
        this.coefficients = coefficients;
    };

    /**
     * Returns the number of coefficients
     * @return number of coefficients in a row
     */
    int getSize(){
        return coefficients.size();
    }

    /**
     * Sets the index of the related basic variable
     * @param index
     */
    void setBasicVariableIndex(int index){
        this.basicVariableIndex = index;
    }

    /**
     * Returns an unmodifiable list of coefficients in this row
     * @return
     */
    List<BigDecimal> getCoefficients(){
        return Collections.unmodifiableList(coefficients);
    }

    /**
     * Calculates the sum of coefficients per column.
     * Let a(j) be the coefficient at index j in this row and b(j) the coefficient at index j in the row passed as argument.
     * Then a new row is created, for which the coefficient at index j is calculated as follows:
     *
     * c(j) = a(j) + b(j)
     *
     * @param row the row to add
     * @return the row with the sums of the coefficients.
     */
    TableauRow add(TableauRow row) {
        if( this.getSize() != row.getSize() )
            throw new RuntimeException("The size of the row does not match.");

        List<BigDecimal> result = new ArrayList<>(row.getSize());
        Iterator<BigDecimal> iterator1 = this.coefficients.iterator();
        Iterator<BigDecimal> iterator2 = row.getCoefficients().iterator();
        while (iterator1.hasNext()) {
            result.add(iterator1.next().add(iterator2.next()));
        }
        return new TableauRow(this.basicVariableIndex,result);
    }

    /**
     * Calculates the sum of coefficients per column.
     * Let a(j) be the coefficient at index j in this row and b(j) the coefficient at index j in the row passed as argument,
     * and f is the factor. Then a new row is created, for which the coefficient at index j is calculated as follows:
     *
     *  c(j) = a(j) + f * b(j)
     *
     * @param row
     * @param factor
     * @return
     */
    TableauRow addWithFactor(TableauRow row, BigDecimal factor) {
        if( this.getSize() != row.getSize() )
            throw new RuntimeException("The size of the row does not match.");

        List<BigDecimal> result = new ArrayList<>(row.getSize());
        Iterator<BigDecimal> iterator1 = this.coefficients.iterator();
        Iterator<BigDecimal> iterator2 = row.getCoefficients().iterator();
        while (iterator1.hasNext()) {
            result.add(iterator1.next().add(iterator2.next().multiply(factor)));
        }
        return new TableauRow(this.basicVariableIndex,result);
    }

    /**
     * Multiplies each coefficients with a factor and returns the resulted row.
     * @param factor
     * @return
     */
    TableauRow multiplyBy(BigDecimal factor){
        List<BigDecimal> coefficients = this.coefficients.stream().map(coeff -> coeff.multiply(factor)).collect(Collectors.toList());
        return new TableauRow(this.basicVariableIndex, coefficients);
    }
}
