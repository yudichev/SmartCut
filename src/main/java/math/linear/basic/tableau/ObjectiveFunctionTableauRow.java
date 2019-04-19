package math.linear.basic.tableau;

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
        STANDARD,
        AUXILIERY
    }

    public int getIncomingVariableIndex()
    {
        int index = INDEX_NOT_ASSIGNED;
        List<BigDecimal> coefficients = getCoefficients();
        BigDecimal absMaxCoeff = BigDecimal.ZERO;
        for(int k = 1; k < coefficients.size(); k++) {
            BigDecimal coeff = coefficients.get(k);
            if(coeff.compareTo(BigDecimal.ZERO) < 0) {
                if(k == INDEX_NOT_ASSIGNED || coeff.compareTo(absMaxCoeff) < 0) {
                    index = k;
                    absMaxCoeff = coeff;
                }
            }
        }
        return index;
    }

    public boolean isOptimal() {
        return getIncomingVariableIndex() == INDEX_NOT_ASSIGNED;
    }
}
