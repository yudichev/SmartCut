package math.linear.simplex;

/*
 * Class represents a row in a tableau for a linear problem. It incorporates all operations with a row.
 */

import java.math.BigDecimal;
import java.util.List;


public final class EquationTableauRow extends GenericTableauRow
{
    private int basicVariableIndex;

    /**
     * Constructor of EquationTableauRow.
     * @param index index of basic variable calculated via coefficients of this row
     * @param coefficients a list of coefficients
     */
    EquationTableauRow(int index, List<BigDecimal> coefficients) {
        super(coefficients);
        this.basicVariableIndex = index;
    };

    /**
     * Sets the index of the related basic variable
     * @param index
     */
    void setBasicVariableIndex(int index){
        this.basicVariableIndex = index;
    }

    /**
     * Returns the index of the basic variable which is expressed via this equation
     * @return
     */
    int getBasicVariableIndex() {
        return this.basicVariableIndex;
    }

}
