package math.linear.problem;

/*
 * Copyright 2001-2019 by HireRight, Inc. All rights reserved.
 * This software is the confidential and proprietary information
 * of HireRight, Inc. Use is subject to license terms.
 */

/*
 * Class represents an initial problem equation
 */

import math.linear.basic.Relation;

import java.util.Arrays;

import static java.util.Arrays.copyOf;

public class ProblemEquation
{
    private static final String ERROR_OUT_OF_BOUNDS = "Index is out of bounds.";

    private int length;
    private double[] coefficients;
    private Relation relation;

    private ProblemEquation(){}

    private ProblemEquation(double[] coeff, Relation relation){
        this.coefficients = coeff;
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
     * Creates a copy of the equation
     * @return copy of equation
     */
    public final ProblemEquation copy(){
        double[] coeffs = new double[this.length];
        System.arraycopy(this.coefficients, 0, coeffs, 0, this.length);
        return new ProblemEquation(coeffs,this.relation);
    }


    /**
     * Generates an equation for linear problem for the given array of coefficients, right value and relation
     * @param coeff array of coefficients
     * @param rel type of relation {@link Relation}, EQUAL, LESS_OR_EQUAL, GREATER_OR_EQUAL
     * @param rightValue right value of the equation
     * @return
     */
    public final static ProblemEquation make(double[] coeff, Relation rel, double rightValue){
        double[] coeffExt = new double[coeff.length + 1];
        coeffExt[0] = rightValue;
        System.arraycopy(coeff, 0, coeffExt, 1, coeff.length);
        return new ProblemEquation(coeffExt, rel);
    }
}
