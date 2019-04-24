package math.linear.simplex;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

public class GomoryMethod {
    private static int NOT_ASSIGNED = -1;

    public static Tableau applyTo(Tableau tableau)
    {
        List<BigDecimal> solution = tableau.getSolutionBigDecimal();
        int biggestFractionIndex = getBiggestFractionIndex(solution, tableau.getPrecision());

        while(biggestFractionIndex != NOT_ASSIGNED) {
            getCuttingPlane(tableau, biggestFractionIndex);
            solution = tableau.getSolutionBigDecimal();
            biggestFractionIndex = getBiggestFractionIndex(solution, tableau.getPrecision());
        }

        return tableau;
    }

    private static void getCuttingPlane(Tableau tableau, int biggestFractionIndex)
    {
        EquationTableauRow equation = tableau.getEquationRows().stream().filter(eq -> eq.getBasicVariableIndex() == biggestFractionIndex).findFirst().get();

        List<BigDecimal> fractions = equation.getCoefficients().stream().sequential()
            .map(coeff -> coeff.subtract(coeff.setScale(0, RoundingMode.FLOOR)))
            .collect(Collectors.toList());
        EquationTableauRow cuttingRow = new EquationTableauRow(0,fractions);
        int rowIdx = tableau.addCuttingRow(cuttingRow);
        int additionalColumnIdx = tableau.insertAdditionalColumn();
        cuttingRow.getCoefficients().set(additionalColumnIdx,BigDecimal.ONE.negate());

        int columnIdx = getIncomingIndex(cuttingRow, (ObjectiveFunctionTableauRow) tableau.getRows().get(0),additionalColumnIdx);
        tableau.pivot(rowIdx, columnIdx);

        //SimplexMethod.applySinglePhase(tableau);
    }

    private static int getBiggestFractionIndex(List<BigDecimal> solution, int precision) {
        BigDecimal maxfraction = BigDecimal.ZERO;
        int index = NOT_ASSIGNED;
        for(int k = 1; k < solution.size(); k++) {
            BigDecimal value = solution.get(k).setScale(precision/2, RoundingMode.HALF_UP);
            BigDecimal fraction = value.subtract(value.setScale(0, BigDecimal.ROUND_FLOOR));
            if(fraction.signum() > 0 && fraction.compareTo(maxfraction) > 0)
            {
                maxfraction = fraction;
                index = k;
            }

        }
        return index;
    }

    private static int getIncomingIndex(EquationTableauRow cuttingRow, ObjectiveFunctionTableauRow objFunc, int exIdx) {
        int index = NOT_ASSIGNED;
        int precision = cuttingRow.getPrecision();
        List<BigDecimal> cuttingRowCoefficients = cuttingRow.getCoefficients();
        List<BigDecimal> objectiveFunctionCoefficients = objFunc.getCoefficients();
        BigDecimal minValue = null;
        for(int k = 1; k < cuttingRowCoefficients.size() - 1; k++){
            if(k == exIdx) continue;
            BigDecimal crValue = cuttingRowCoefficients.get(k).abs();
            BigDecimal ofValue = objectiveFunctionCoefficients.get(k).abs();
            if(crValue.signum() != 0) {
                BigDecimal ratio = ofValue.divide(crValue, precision, RoundingMode.HALF_UP);
                if(minValue == null) {
                    minValue = ratio;
                    index = k;
                } else if(ratio.compareTo(minValue) < 0){
                    minValue = ratio;
                    index = k;
                }
            }
        }

        return index;
    }

}
