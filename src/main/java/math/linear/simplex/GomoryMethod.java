package math.linear.simplex;

import math.linear.basic.ObjectiveFunctionType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

public class GomoryMethod {
    private static int NOT_ASSIGNED = -1;
    public static Tableau applyTo(Tableau tableau)
    {
        List<BigDecimal> solution = tableau.getSolutionBigDecimal();
        int biggestFractionIndex = getBiggestFractionIndex(solution);
        if(biggestFractionIndex == NOT_ASSIGNED) return tableau;

        EquationTableauRow equation = tableau.getEquationRows().stream().filter(eq -> eq.getBasicVariableIndex() == biggestFractionIndex).findFirst().get();

        List<BigDecimal> fractions = equation.getCoefficients().stream().sequential()
            .map(coeff -> coeff.subtract(coeff.setScale(0, RoundingMode.FLOOR)).negate())
            .collect(Collectors.toList());
        fractions.add(BigDecimal.ONE);

        tableau.getRows().stream().forEach(row -> row.getCoefficients().add(BigDecimal.ZERO));
        tableau.setRowSize(tableau.getRowSize() + 1);
        tableau.addRow(new EquationTableauRow(equation.getSize() + 1,fractions));

        ObjectiveFunctionTableauRow objectiveFunction = (ObjectiveFunctionTableauRow) tableau.getRows().get(tableau.getObjectiveFunctionIndex());
        List<BigDecimal> objFuncCoeffs = objectiveFunction.getCoefficients().stream().sequential().map(coeff -> coeff.negate()).collect(Collectors.toList());

        //TODO create auxiliery function and add it to the tableau

        tableau.setObjectiveFunction(new ObjectiveFunctionTableauRow(ObjectiveFunctionTableauRow.Type.STANDARD, objFuncCoeffs));
        SimplexMethod.applyTwoPhases(tableau);

        return tableau;
    }

    private static int getBiggestFractionIndex(List<BigDecimal> solution) {
        BigDecimal maxfraction = BigDecimal.ZERO;
        int index = NOT_ASSIGNED;
        for(int k = 1; k < solution.size(); k++) {
            BigDecimal value = solution.get(k);
            BigDecimal fraction = value.subtract(value.setScale(0, BigDecimal.ROUND_FLOOR));
            if(fraction.signum() > 0 && fraction.compareTo(maxfraction) > 0)
            {
                maxfraction = fraction;
                index = k;
            }

        }
        return index;
    }

    private static void addCuttingPlane(Tableau tableau, int idx){

    }
}
