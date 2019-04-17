package math.linear2.basic;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents an equation in a lenear problem. Contains coefficients and a relation, which can be
 * EQUAL, LESS_OR_EQUAL, GREATER_OR_EQUAL.
 */
public class Equation {

    private static final String ERROR_RELATION_MISMATCH = "Equations with different relation cannot be combined.";
    private static final String ERROR_LENGTH_MISMATCH = "Equations of different length cannot be combined";

    private List<BigDecimal> coefficients;
    private Relation relation;

    private Equation(){}

    private Equation(List<BigDecimal> coefficients, Relation relation){
        this.coefficients = coefficients;
        this.relation = relation;
    }

    /**
     * Constructs an instance of Equation for the given list of coefficitents, right coefficient and the relation.
     * @param coeffs coefficients
     * @param b right value of the equation (not multiplied by a variable)
     * @param relation
     * @return instance of Equation
     */
    public static Equation of(double[] coeffs, double b, Relation relation)
    {
        List<BigDecimal> coefficients = new ArrayList<>();
        coefficients.add(BigDecimal.valueOf(b));
        coefficients.addAll(Arrays.stream(coeffs).sequential()
                .mapToObj(val -> BigDecimal.valueOf(val)).collect(Collectors.toList()));
        return new Equation(coefficients,relation);
    }

    /**
     * Returns a list of coefficients
     * @return
     */
    public List<BigDecimal> getCoefficients() {
        return this.coefficients;
    }

    public BigDecimal getCoefficientAt(int n){
        return this.coefficients.get(n);
    }

    /**
     * Returns the relation type
     * @return
     */
    public Relation getRelation(){
        return this.relation;
    }

    /**
     * Multiply the equation by the given factor
     * @param factor
     * @return the result of multiplication
     */
    public Equation multiplyBy(BigDecimal factor){
        List<BigDecimal> coefficients = this.coefficients.stream().sequential()
                .map(val -> val.multiply(factor)).collect(Collectors.toList());
        Relation relation = factor.signum() == -1 ? this.relation.invert() : this.relation;
        return new Equation(coefficients,relation);
    }

    /**
     * Add two equations
     * @param equation
     * @return the result of addition
     */
    public Equation add(Equation equation){
        if(!this.relation.equals(equation.getRelation())){
            throw new RuntimeException(ERROR_RELATION_MISMATCH);
        }
        if(this.coefficients.size() != equation.getCoefficients().size()){
            throw new RuntimeException(ERROR_LENGTH_MISMATCH);
        }
        List<BigDecimal> coefficients = new ArrayList<>(this.coefficients.size());
        Iterator<BigDecimal> iteratorA = this.coefficients.iterator();
        Iterator<BigDecimal> iteratorB = equation.getCoefficients().iterator();
        while (iteratorA.hasNext()){
            coefficients.add(iteratorA.next().add(iteratorB.next()));
        }
        return new Equation(coefficients,this.relation);
    }

    /**
     * Changes relation so that the right value of the equation (coefficient at index 0) is positive
     * @return
     */
    public Equation normalize(){
        if(this.coefficients.get(0).compareTo(BigDecimal.ZERO) < 0){
            return this.multiplyBy(BigDecimal.valueOf(-1));
        }
        return this;
    }

}
