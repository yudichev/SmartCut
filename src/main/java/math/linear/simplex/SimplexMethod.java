package math.linear.simplex;


/*
 * Provides methods to solve problem
 */


import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

public class SimplexMethod
{
    private static int NOT_ASSIGNED = -1;

    public static Tableau applySinglePhase(Tableau tableau){
        int objFuncIdx = tableau.getObjectiveFunctionIndex();
        ObjectiveFunctionTableauRow objFuncRow = (ObjectiveFunctionTableauRow) tableau.getRows().get(objFuncIdx);
        List<EquationTableauRow> equations = tableau.getEquationRows();
        int precision = tableau.getPrecision();
        while (!objFuncRow.isOptimal()) {
            int incomingIndex = objFuncRow.getIncomingVariableIndex();
            int outcomingIndex = getOutcomingIndex(incomingIndex, equations, precision);
            tableau.pivot(outcomingIndex, incomingIndex);
        }

        return tableau;
    }

    public static boolean existsAlternativeSolution(Tableau tableau){
        int objFuncIdx = tableau.getObjectiveFunctionIndex();
        ObjectiveFunctionTableauRow objFuncRow = (ObjectiveFunctionTableauRow) tableau.getRows().get(objFuncIdx);
        int numberOfVariables = tableau.getNumberOfProblemVariables();
        return objFuncRow.getCoefficients().stream().sequential().skip(1).limit(numberOfVariables)
            .filter(coeff->coeff.compareTo(BigDecimal.ZERO) == 0).count() > 0L;
    }

    private static int getOutcomingIndex(int incomingIndex, List<EquationTableauRow> equations, int precision) {
        int outcomingIndex = NOT_ASSIGNED;
        BigDecimal minRatio = null;
        for(int k = 0; k < equations.size(); k++) {
            EquationTableauRow eq = equations.get(k);
            List<BigDecimal> coefficients = eq.getCoefficients();
            BigDecimal currentCoeff = coefficients.get(incomingIndex);
            BigDecimal freeCoeff = coefficients.get(0);

            MathContext mathContext = new MathContext(precision);
            if(currentCoeff.compareTo(BigDecimal.ZERO) > 0){
                BigDecimal ratio = freeCoeff.divide(currentCoeff,mathContext);
                if(minRatio == null || minRatio.compareTo(ratio) > 0){
                    minRatio = ratio;
                    outcomingIndex = k;
                }
            }
        }
        return outcomingIndex;
    }

    public static Tableau applyTwoPhases(Tableau tableau){
        int auxFuncIdx = tableau.getAuxiliaryFunctionIndex();
        ObjectiveFunctionTableauRow auxFuncRow = (ObjectiveFunctionTableauRow) tableau.getRows().get(auxFuncIdx);
        List<EquationTableauRow> equations = tableau.getEquationRows();
        int auxFirstColumnIndex = tableau.getAuxiliaryVariablesFirstIndex();
        for(int k = auxFirstColumnIndex; k < auxFuncRow.getCoefficients().size(); k++){
            EquationTableauRow equation = getEquationWithUnitColumn(equations, k);
            auxFuncRow.addWithFactor(equation, BigDecimal.ONE.negate());
        }

        int objFuncIdx = tableau.getObjectiveFunctionIndex();
        ObjectiveFunctionTableauRow objFuncRow = (ObjectiveFunctionTableauRow) tableau.getRows().get(objFuncIdx);

        int precision = tableau.getPrecision();

        while (!auxFuncRow.isOptimal(auxFirstColumnIndex)) {
            int incomingIndex = auxFuncRow.getIncomingVariableIndex(auxFirstColumnIndex);
            int outcomingIndex = getOutcomingIndex(incomingIndex, equations, precision);
            tableau.pivot(outcomingIndex, incomingIndex);
        }


        if(auxFuncRow.getCoefficients().get(0).setScale(precision - 2, RoundingMode.HALF_UP).compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("The problem has no base plane.");
        };


        while (!objFuncRow.isOptimal(auxFirstColumnIndex)) {
            int incomingIndex = objFuncRow.getIncomingVariableIndex(auxFirstColumnIndex);
            int outcomingIndex = getOutcomingIndex(incomingIndex, equations, precision);
            tableau.pivot(outcomingIndex, incomingIndex);
        }

        return tableau;
    }



    private static EquationTableauRow getEquationWithUnitColumn(List<EquationTableauRow> equations, int columnIndex){
        return  equations.stream().filter(eq -> eq.getCoefficients().get(columnIndex).compareTo(BigDecimal.ONE) == 0).findFirst().get();
    }

    private static int getIncomingIndex(ObjectiveFunctionTableauRow objFunc, ObjectiveFunctionTableauRow auxFunc ){
        int incomingIndexObj = objFunc.getIncomingVariableIndex();
        int incomingIndexAux = auxFunc.getIncomingVariableIndex();

        if(incomingIndexObj == NOT_ASSIGNED){
            return incomingIndexAux;
        } else if(incomingIndexAux == NOT_ASSIGNED) {
            return incomingIndexObj;
        } else  {
            BigDecimal objFuncCoeff = objFunc.getCoefficients().get(incomingIndexObj);
            BigDecimal auxFuncCoeff = auxFunc.getCoefficients().get(incomingIndexAux);
            return objFuncCoeff.abs().compareTo(auxFuncCoeff.abs()) > 0 ? incomingIndexObj : incomingIndexAux;
        }

    }
}
