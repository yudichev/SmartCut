package math.linear.basic;

import java.util.Arrays;

import static java.util.Arrays.*;

public class Equation {
    private static final String ERROR_OUT_OF_BOUNDS = "Index is out of bounds.";
    private static final String ERROR_RELATION_MISMATCH = "Equations with different relation cannot be combined.";
    private static final String ERROR_LENGTH_MISMATCH = "Equations of different length cannot be combined";

    private int length;
    private double[] leftValues;
    private double rightValue;
    private Relation relation;

    private Equation(){}

    private Equation(double[] leftValues, double rightValue, Relation relation){
     this.leftValues = leftValues;
     this.rightValue = rightValue;
     this.relation = relation;
     this.length = this.leftValues.length;
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
    public final double getValueAt(int n){
        if(n >= 0 && n < length){
            return leftValues[n];
        } else {
            throw new RuntimeException(ERROR_OUT_OF_BOUNDS);
        }
    }

    public final double[] getLeftValues(){
        return Arrays.copyOf(this.leftValues,this.length);
    }

    /**
     * Returns right value of the equation
     * @return
     */
    public final double getRightValue(){
        return this.rightValue;
    }

    /**
     * Multiply the equation by a factor
     * @param factor factor to multiply
     * @return product of equation and the given factor
     */
    public final Equation applyFactor(double factor){
        double[] lvalues = new double[this.length];
        for(int k = 0; k < this.length; k++) {
            lvalues[k] = factor * this.leftValues[k];
        }
        Relation rel = factor >= 0 ? this.relation : this.relation.invert();
        return new Equation(lvalues,rightValue * factor,rel);
    }

    /**
     * Add two equations
     * @param eq equation to add
     * @return result of equation sum
     */
    public final Equation add(Equation eq){
        if(!this.getRelation().equals(eq.getRelation())){
            throw new RuntimeException(ERROR_RELATION_MISMATCH);
        }
        if(this.getLength() != eq.getLength()){
            throw new RuntimeException((ERROR_LENGTH_MISMATCH));
        }
        double[] feq = new double[this.getLength()];
        for(int k = 0; k < this.length; k++){
            feq[k] = this.leftValues[k] + eq.getValueAt(k);
        }
        return Equation.of(feq,this.relation,this.rightValue + eq.getRightValue());
    }

    /**
     * Creates a copy of the equation
     * @return copy of equation
     */
    public Equation copy(){
        double[] lvalues = new double[this.length];
        System.arraycopy(this.leftValues, 0, lvalues, 0, this.length);
        return new Equation(lvalues,rightValue,this.relation);
    }

    /**
     * Normalizes the equation, i.e., convert it to an equation with non-negative right value.
     * @return normalized equation
     */
    public Equation normalize(){
        if(Double.compare(this.getRightValue(),0.) > 0){
            return this.copy();
        } else {
            return this.applyFactor(-1.);
        }
    }

    /**
     * Generates an equation for linear problem for the given array of coefficients, right value and relation
     * @param values array of coefficients
     * @param rel type of relation {@link Relation}, EQUAL, LESS_OR_EQUAL, GREATER_OR_EQUAL
     * @param rightValue right value of the equation
     * @return
     */
    public static Equation of(double[] values, Relation rel, double rightValue){
        double[] leftValues = copyOf(values,values.length);
        return new Equation(leftValues, rightValue, rel);
    }
}
