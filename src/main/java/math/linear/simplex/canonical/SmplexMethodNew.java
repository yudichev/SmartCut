package math.linear.simplex.canonical;

/*
 * Copyright 2001-2019 by HireRight, Inc. All rights reserved.
 * This software is the confidential and proprietary information
 * of HireRight, Inc. Use is subject to license terms.
 */

import math.linear.basic.Equation;
import math.linear.basic.tableau.EquationTableauRow;
import math.linear.basic.tableau.GenericTableauRow;
import math.linear.basic.tableau.ObjectiveFunctionTableauRow;
import math.linear.basic.tableau.Tableau;

import java.math.BigDecimal;
import java.util.List;

public class SmplexMethodNew
{
    private static int NOT_ASSIGNED = -1;

    public static Tableau  applyTo(Tableau tableau){
        int objFuncIdx = tableau.getObjectiveFunctionIndex();
        ObjectiveFunctionTableauRow objFuncRow = (ObjectiveFunctionTableauRow) tableau.getRows().get(objFuncIdx);
        while (!objFuncRow.isOptimal()) {
            int incomingIndex = objFuncRow.getIncomingVariableIndex();
            int outcomingIndex = getOutcomingIndex(incomingIndex, tableau.getEquationRows());
            tableau.pivot(outcomingIndex, incomingIndex);
        }

        return null;
    }

    private static int getOutcomingIndex(int incomingIndex, List<EquationTableauRow> equations) {
        int outcomingIndex = NOT_ASSIGNED;
        BigDecimal maxRatio = null;
        for(int k = 0; k < equations.size(); k++) {
            EquationTableauRow eq = equations.get(k);
            List<BigDecimal> coefficients = eq.getCoefficients();
            BigDecimal currentCoeff = coefficients.get(incomingIndex);
            BigDecimal freeCoeff = coefficients.get(0);

            if(currentCoeff.compareTo(BigDecimal.ZERO) > 0 && !(freeCoeff.compareTo(BigDecimal.ZERO) == 0)){
                BigDecimal ratio = currentCoeff.divide(freeCoeff);
                if(maxRatio == null || maxRatio.compareTo(ratio) < 0){
                    maxRatio = ratio;
                    outcomingIndex = k;
                }
            }
        }
        return outcomingIndex;
    }
}
