package math.linear.simplex;

/*
 * Represents a row for tableau, containing coefficients of an objective function
 */

import java.math.BigDecimal;
import java.math.RoundingMode;
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

    enum Type {
        STANDARD, AUXILIARY
    }

    final int getIncomingVariableIndex()
    {
        return getIncomingVariableIndex(getCoefficients().size());
    }

    final int getIncomingVariableIndex(int idx)
    {
        int index = INDEX_NOT_ASSIGNED;
        List<BigDecimal> coefficients = getCoefficients();
        int maxIndex = (idx == INDEX_NOT_ASSIGNED) ? coefficients.size() : Math.min(idx , coefficients.size());
        BigDecimal absMaxCoeff = BigDecimal.ZERO;
        for(int k = 1; k < maxIndex; k++) {
            BigDecimal coeff = coefficients.get(k).setScale(getPrecision()/2,RoundingMode.HALF_UP);
            if(coeff.signum() < 0 ) {
                if(k == INDEX_NOT_ASSIGNED || coeff.abs().compareTo(absMaxCoeff.abs()) > 0) {
                    index = k;
                    absMaxCoeff = coeff;
                }
            }
        }
        return index;
    }

    final boolean isOptimal() {
        return isOptimal(getCoefficients().size());
    }

    final boolean isOptimal(int idx) {
        return getIncomingVariableIndex(idx) == INDEX_NOT_ASSIGNED;
    }

}
