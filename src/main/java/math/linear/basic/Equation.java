package math.linear.basic;

import java.util.Arrays;

import static java.util.Arrays.*;

public class Equation {
    private static String ERROR_OUT_OF_BOUNDS = "Index is out of bounds.";
    private static String ERROR_RELATION_MISMATCH = "Equations with different relation cannot be combined.";
    private static String ERROR_LENGTH_MISMATCH = "Equations of different length cannot be combined";

    private int length;
    private double[] leftValues;
    private double rightValue;
    private Relation relation;

    private Equation(){};

    private Equation(double[] leftValues, double rightValue, Relation relation){
     this.leftValues = leftValues;
     this.rightValue = rightValue;
     this.relation = relation;
     this.length = this.leftValues.length;
    }

    public final int getLength(){
        return length;
    }

    public final Relation getRelation(){
        return relation;
    }

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

    public final double getRightValue(){
        return this.rightValue;
    }

    public final Equation applyFactor(double factor){
        double[] lvalues = new double[this.length];
        for(int k = 0; k < this.length; k++) lvalues[k] = factor * this.leftValues[k];
        Relation rel = factor >= 0 ? this.relation : this.relation.invert();
        return new Equation(lvalues,rightValue * factor,rel);
    }

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

    public Equation copy(){
        double[] lvalues = new double[this.length];
        System.arraycopy(this.leftValues, 0, lvalues, 0, this.length);
        return new Equation(lvalues,rightValue,this.relation);
    }

    public Equation normalize(){
        if(Relation.GREATER_OR_EQUAL.equals(this.relation)){
            return this.applyFactor(-1.);
        } else {
            return this.copy();
        }
    }

    public static Equation of(double[] values, Relation rel, double rightValue){
        double[] leftValues = copyOf(values,values.length);
        return new Equation(leftValues, rightValue, rel);
    }
}
