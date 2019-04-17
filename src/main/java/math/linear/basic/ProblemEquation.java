package math.linear.basic;

/*
 * Class represents an initial problem equation
 */

import java.util.Arrays;

import static java.util.Arrays.copyOf;

public class ProblemEquation
{
    private static final String ERROR_OUT_OF_BOUNDS = "Index is out of bounds.";

    private int length;
    private double[] coefficients;
    private double rightValue;
    private Relation relation;

    private ProblemEquation(){}

    private ProblemEquation(double[] coeff, double rightValue, Relation relation){
        this.coefficients = coeff;
        this.rightValue = rightValue;
        this.relation = relation;
        this.length = this.coefficients.length;
    }

    /**
     * Returns the length of the equation
     * @return equation length
     */
    public final int getLength(){
        return length;
    }

    /**
     * Returns the relation type in the equation
     * @return relation type
     */
    public final Relation getRelation(){
        return relation;
    }

    /**
     * Returns a value of equation a given position
     * @param n the number of position (the first one has index 0)
     * @return the value at position n
     */
    public final double getCoefficientAt(int n){
        if(n >= 0 && n < length){
            return coefficients[n];
        } else {
            throw new RuntimeException(ERROR_OUT_OF_BOUNDS);
        }
    }

    public final double[] getCoefficients(){
        return Arrays.copyOf(this.coefficients,this.length);
    }

    /**
     * Returns right value of the equation
     * @return
     */
    public final double getRightValue(){
        return this.rightValue;
    }




    /**
     * Creates a copy of the equation
     * @return copy of equation
     */
    public ProblemEquation copy(){
        double[] lvalues = new double[this.length];
        System.arraycopy(this.coefficients, 0, lvalues, 0, this.length);
        return new ProblemEquation(lvalues,rightValue,this.relation);
    }


    /**
     * Generates an equation for linear problem for the given array of coefficients, right value and relation
     * @param coeff array of coefficients
     * @param rel type of relation {@link Relation}, EQUAL, LESS_OR_EQUAL, GREATER_OR_EQUAL
     * @param rightValue right value of the equation
     * @return
     */
    public static ProblemEquation of(double[] coeff, Relation rel, double rightValue){
        return new ProblemEquation(copyOf(coeff,coeff.length), rightValue, rel);
    }
}
