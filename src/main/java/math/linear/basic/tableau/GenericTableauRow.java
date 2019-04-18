package math.linear.basic.tableau;

/*
 * Copyright 2001-2019 by HireRight, Inc. All rights reserved.
 * This software is the confidential and proprietary information
 * of HireRight, Inc. Use is subject to license terms.
 */

/*
 * Class represents a general row in a tableau for a linear problem. It incorporates all basic operations with a row.
 */

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

abstract class GenericTableauRow
{
    private List<BigDecimal> coefficients;


    /**
     * Constructor of EquationTableauRow.
     * @param coefficients a list of coefficients
     */
    GenericTableauRow(List<BigDecimal> coefficients) {
        this.coefficients = coefficients;
    };

    /**
     * Returns the number of coefficients
     * @return number of coefficients in a row
     */
    public final int getSize(){
        return coefficients.size();
    }


    /**
     * Returns an unmodifiable list of coefficients in this row
     * @return
     */
    public final List<BigDecimal> getCoefficients(){
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
     */
     void add(GenericTableauRow row) {
        if( this.getSize() != row.getSize() )
            throw new RuntimeException("The size of the row does not match.");
        for(int k = 0; k < this.coefficients.size(); k++) {
            coefficients.set(k,coefficients.get(k).add(row.getCoefficients().get(k)));
        }
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
     *
     */
    void addWithFactor(GenericTableauRow row, BigDecimal factor) {
        if( this.getSize() != row.getSize() )
            throw new RuntimeException("The size of the row does not match.");

        for(int k = 0; k < this.coefficients.size(); k++) {
            coefficients.set(k,coefficients.get(k).add(row.getCoefficients().get(k).multiply(factor)));
        }
    }

    /**
     * Multiplies each coefficients with a factor and returns the resulted row.
     * @param factor
     * @return the list of products of the coefficients and the factor.
     */
    void multiplyBy(BigDecimal factor){
        this.coefficients.stream().forEach(coeff -> coeff = coeff.multiply(factor));
    }
}