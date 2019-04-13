package math.linear2.basic;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents objective function for linear problem.
 */
public class ObjectiveFunction {
    private static final String ERROR_INDEX_OUT_OF_BOUNDS = "Index is out of bounds.";
    private static final String ERROR_ZERO_LENGTH = "Cannot create functional with zero length.";
    private static final String ERROR_LENGTH_MISMATCH = "Objective function's length differs from the length of the array being added.";

    private List<BigDecimal> coefficients;
    private int indexOfMaximum = 0;
    private ObjectiveFunctionType type;

    private ObjectiveFunction(List<BigDecimal> coefficients, ObjectiveFunctionType type){
        this.coefficients = coefficients;
        this.type = type;
        calculateIndexOfMaximum();
    }

    private void calculateIndexOfMaximum(){
        BigDecimal maxValue = coefficients.stream().filter(val -> val.compareTo(BigDecimal.ZERO) != 0)
                .max(this::compare).orElse(BigDecimal.ZERO);
        indexOfMaximum = coefficients.indexOf(maxValue);
    }


    private int compare(BigDecimal o1, BigDecimal o2) {
        return o1.compareTo(o2);
    }

    /**
     * Creates an instance of ObjectiveFunction for the given coefficients and the type
     * @param coeffs coefficients of the objective function
     * @param type
     * @return instance of ObjectiveFunction
     */
    public ObjectiveFunction create(double[] coeffs, ObjectiveFunctionType type){
        List<BigDecimal> coefficients = Arrays.stream(coeffs).sequential()
                .mapToObj(val -> BigDecimal.valueOf(val)).collect(Collectors.toList());
        return new ObjectiveFunction(coefficients, type);
    }
}
