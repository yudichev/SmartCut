package math.linear.simplex;

/*
 * Copyright 2001-2019 by HireRight, Inc. All rights reserved.
 * This software is the confidential and proprietary information
 * of HireRight, Inc. Use is subject to license terms.
 */

/*
 * Represents a row for tableau, containing coefficients of an objective function
 */

import java.math.BigDecimal;
import java.util.List;

public class ObjectiveFunctionTableauRow extends GenericTableauRow
{
    public final int INDEX_NOT_ASSIGNED = -1;

    private Type type;
    /**
     * Constructor of EquationTableauRow.
     *
     * @param coefficients a list of coefficients
     */
    ObjectiveFunctionTableauRow(Type type, List<BigDecimal> coefficients)
    {
        super(coefficients);
        this.type = type;
    }

    public enum Type {
        STANDARD, AUXILIARY
    }

    public final int getIncomingVariableIndex()
    {
        return getIncomingVariableIndex(getCoefficients().size());
    }

    public final int getIncomingVariableIndex(int idx)
    {

        int index = INDEX_NOT_ASSIGNED;
        List<BigDecimal> coefficients = getCoefficients();
        int maxIndex = Math.min(idx, coefficients.size());
        BigDecimal absMaxCoeff = BigDecimal.ZERO;
        for(int k = 1; k < maxIndex; k++) {
            BigDecimal coeff = coefficients.get(k);
            if(coeff.compareTo(BigDecimal.ZERO) < 0 ) {
                if(k == INDEX_NOT_ASSIGNED || coeff.abs().compareTo(absMaxCoeff.abs()) > 0) {
                    index = k;
                    absMaxCoeff = coeff;
                }
            }
        }
        return index;
    }

    public final boolean isOptimal() {
        return isOptimal(getCoefficients().size());
    }

    public final boolean isOptimal(int idx) {
        return getIncomingVariableIndex(idx) == INDEX_NOT_ASSIGNED;
    }
}
