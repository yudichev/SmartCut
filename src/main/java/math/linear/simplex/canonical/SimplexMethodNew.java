package math.linear.simplex.canonical;

/*
 * Provides methods to solve problem
 */


import math.linear.basic.tableau.EquationTableauRow;
import math.linear.basic.tableau.ObjectiveFunctionTableauRow;
import math.linear.basic.tableau.Tableau;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class SimplexMethodNew
{
    private static int NOT_ASSIGNED = -1;

    public static Tableau applySinglePhase(Tableau tableau){
        int objFuncIdx = tableau.getObjectiveFunctionIndex();
        ObjectiveFunctionTableauRow objFuncRow = (ObjectiveFunctionTableauRow) tableau.getRows().get(objFuncIdx);
        while (!objFuncRow.isOptimal()) {
            int incomingIndex = objFuncRow.getIncomingVariableIndex();
            int outcomingIndex = getOutcomingIndex(incomingIndex, tableau.getEquationRows(), tableau.scale);
            tableau.pivot(outcomingIndex, incomingIndex);
        }

        return tableau;
    }

    private static int getOutcomingIndex(int incomingIndex, List<EquationTableauRow> equations, int scale) {
        int outcomingIndex = NOT_ASSIGNED;
        BigDecimal maxRatio = null;
        for(int k = 0; k < equations.size(); k++) {
            EquationTableauRow eq = equations.get(k);
            List<BigDecimal> coefficients = eq.getCoefficients();
            BigDecimal currentCoeff = coefficients.get(incomingIndex);
            BigDecimal freeCoeff = coefficients.get(0);

            if(currentCoeff.compareTo(BigDecimal.ZERO) > 0 && !(freeCoeff.compareTo(BigDecimal.ZERO) == 0)){
                BigDecimal ratio = currentCoeff.divide(freeCoeff, scale, RoundingMode.HALF_UP);
                if(maxRatio == null || maxRatio.compareTo(ratio) < 0){
                    maxRatio = ratio;
                    outcomingIndex = k;
                }
            }
        }
        return outcomingIndex;
    }
}
