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
        fractions.add(BigDecimal.ONE.negate());
        EquationTableauRow cuttingRow = new EquationTableauRow(equation.getSize() + 1,fractions);


        tableau.getRows().stream().forEach(row -> row.getCoefficients().add(BigDecimal.ZERO));
        tableau.setRowSize(tableau.getRowSize() + 1);
        tableau.addCuttingRow(cuttingRow);

        int columnIdx = cuttingRow.getIndexOfMaxAbsValue();
        int auxilieryFunctionIndex = tableau.getAuxiliaryFunctionIndex();
        int rowIdx = auxilieryFunctionIndex < 0 ? tableau.getRows().size() - 1 : tableau.getAuxiliaryFunctionIndex() - 1;
        tableau.pivot(rowIdx, columnIdx);

        SimplexMethod.applySinglePhase(tableau);
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

}
