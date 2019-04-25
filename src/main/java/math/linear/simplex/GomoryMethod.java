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
            addCuttingRow(tableau, biggestFractionIndex);
            solution = tableau.getSolutionBigDecimal();
            biggestFractionIndex = getBiggestFractionIndex(solution, tableau.getPrecision());
        }

        return tableau;
    }

    private static void addCuttingRow(Tableau tableau, int biggestFractionIndex)
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
    }

    /*
    Returns the index of the solution variable with the biggest fraction.
     */
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
    

    private static int getIncomingIndex(Tableau tableux, int cuttingRowIndex){
        int precision = tableux.getPrecision();
        BigDecimal minRatio = null;
        int index = NOT_ASSIGNED;
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