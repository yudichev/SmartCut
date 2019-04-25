package math.linear.simplex;

/*
 * Class represents a general row in a tableau for a linear problem. It incorporates all basic operations with a row.
 */

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

public abstract class GenericTableauRow
{
    private int INDEX_NOT_ASSIGNED = -1;
    private List<BigDecimal> coefficients;
    private MathContext mathContext = MathContext.DECIMAL64;


    /**
     * Constructor of EquationTableauRow.
     * @param coefficients a list of coefficients
     */
    GenericTableauRow(List<BigDecimal> coefficients) {
        this.coefficients = coefficients;
    };

    /**
     * Defines the pricision
     * @param precision
     */
    final void setPrecision(int precision){
        this.mathContext = new MathContext(precision);
    }

    final int getPrecision(){
        return mathContext.getPrecision();
    }

    /**
     * Returns the number of coefficients
     * @return number of coefficients in a row
     */
    public final int getSize(){
        return coefficients.size();
    }


    /**
     * Returns an unmodifiable list of coefficients in this row
     * @return
     */
    public final List<BigDecimal> getCoefficients(){
        return coefficients;
    }


    /**
     * Calculates the sum of coefficients per column.
     * Let a(j) be the coefficient at index j in this row and b(j) the coefficient at index j in the row passed as argument,
     * and f is the factor. Then a new row is created, for which the coefficient at index j is calculated as follows:
     *
     *  c(j) = a(j) + f * b(j)
     *
     * @param row
     * @param factor
     *
     */
    void addWithFactor(GenericTableauRow row, BigDecimal factor) {
        if( this.getSize() != row.getSize() )
            throw new RuntimeException("The size of the row does not match.");

        for(int k = 0; k < coefficients.size(); k++) {
            coefficients.set(k,restoreInteger(coefficients.get(k).add(row.getCoefficients().get(k).multiply(factor,mathContext),mathContext)));
        }
    }

    /**
     * Multiplies each coefficients with a factor and returns the resulted row.
     * @param factor
     * @return the list of products of the coefficients and the factor.
     */
    void multiplyBy(BigDecimal factor){

        for(int k = 0; k < coefficients.size(); k++){
            BigDecimal coeff = coefficients.get(k);
            if(coeff.equals(BigDecimal.ZERO)) continue;
            coefficients.set(k, restoreInteger(coeff.multiply(factor,mathContext)));
        }
    }

    int getIndexOfMaxAbsValue(){
        int index = INDEX_NOT_ASSIGNED;
        BigDecimal maxAbsValue = BigDecimal.ZERO;
        for(int k = 1; k < coefficients.size(); k++){
            BigDecimal coeff = coefficients.get(k);
            if(coeff.compareTo(maxAbsValue) > 0){
                index = k;
                maxAbsValue = coeff;
            }
        }
        return index;
    }

    void cutTo(int idx) {
        coefficients = coefficients.subList(0, idx);
    }


    private BigDecimal restoreInteger(BigDecimal value){
        BigDecimal intValue = value.setScale(0, RoundingMode.HALF_UP);
        BigDecimal difference = value.subtract(intValue).abs().setScale(getPrecision()/2, RoundingMode.FLOOR);
        if(difference.signum() == 0){
            return intValue;
        } else {
            return value;
        }
    }
}
