package math.linear.simplex;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

public class GomoryMethod {
    private static int NOT_ASSIGNED = -1;

    public static Tableau applyTo(Tableau tableau)
    {
        tableau.cutoffAuxiliary();
        List<BigDecimal> solution = tableau.getSolutionBigDecimal();
        int biggestFractionIndex = getBiggestFractionIndex(solution, tableau.getPrecision());

        while(biggestFractionIndex != NOT_ASSIGNED ) {
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
        fractions.add(BigDecimal.ONE.negate());
        EquationTableauRow cuttingRow = new EquationTableauRow(equation.getSize() + 1,fractions);


        tableau.getRows().stream().forEach(row -> row.getCoefficients().add(BigDecimal.ZERO));
        tableau.setRowSize(tableau.getRowSize() + 1);
        tableau.addCuttingRow(cuttingRow);

        int auxilieryFunctionIndex = tableau.getAuxiliaryFunctionIndex();

        int rowIdx = auxilieryFunctionIndex < 0 ? tableau.getRows().size() - 1 : tableau.getAuxiliaryFunctionIndex() - 1;
        int columnIdx = getIncomingIndex(tableau,rowIdx);
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

    private static int getIncomingIndex(EquationTableauRow cuttingRow, ObjectiveFunctionTableauRow objFunc) {
        int index = NOT_ASSIGNED;
        int precision = cuttingRow.getPrecision();
        List<BigDecimal> cuttingRowCoefficients = cuttingRow.getCoefficients();
        List<BigDecimal> objectiveFunctionCoefficients = objFunc.getCoefficients();
        BigDecimal minValue = null;
        for(int k = 1; k < cuttingRow.getSize() - 1; k++){
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

    private static int getIncomingIndex(Tableau tableux, int cuttingRowIndex){
        int precision = tableux.getPrecision();
        BigDecimal minRatio = null;
        int index = NOT_ASSIGNED;
        /*
        for(int k = 1; k < tableux.getRowSize() ; k++){
            int outcomingIndex = SimplexMethod.getOutcomingIndex(k,tableux.getEquationRows(),precision);
            if(outcomingIndex == cuttingRowIndex) return k;

            if(outcomingIndex == NOT_ASSIGNED) continue;
            List<BigDecimal> coeffs = tableux.getRows().get(outcomingIndex).getCoefficients();
            BigDecimal ratio = coeffs.get(0).divide(coeffs.get(k),precision/2,RoundingMode.HALF_UP);
            if(minRatio == null || (ratio.signum() > 0 && ratio.compareTo(minRatio) < 0)) {
                minRatio = ratio;
                index = k;
            }

        }
        */
        /*
        if(index == NOT_ASSIGNED){
            GenericTableauRow cuttingRow  = tableux.getRows().get(cuttingRowIndex);
            List<BigDecimal> coeffs = cuttingRow.getCoefficients();
            BigDecimal maxValue = null;
            for(int k = 1; k < cuttingRow.getSize(); k++){
                BigDecimal currentValue = coeffs.get(k);
                if(maxValue == null || currentValue.compareTo(maxValue) > 0) {
                    maxValue = currentValue;
                    index = k;
                }
            }
        }
        */

        ObjectiveFunctionTableauRow objFunc = tableux.getObjectiveFunction();
        GenericTableauRow cutRow = tableux.getRows().get(cuttingRowIndex);
        for(int k = 1; k < cutRow.getSize() - 1 ; k++){
            BigDecimal cutCoeff = cutRow.getCoefficients().get(k);
            if(cutCoeff.signum() == 0) continue;
            BigDecimal objfCoeff = objFunc.getCoefficients().get(k);
            BigDecimal ratio = objfCoeff.divide(cutCoeff,precision/2,RoundingMode.HALF_UP);
            if(minRatio == null || ratio.compareTo(minRatio) < 0){
                minRatio = ratio;
                index = k;
            }
        }

        return index;
    }

}
