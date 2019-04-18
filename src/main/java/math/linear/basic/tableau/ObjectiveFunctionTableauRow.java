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
}
