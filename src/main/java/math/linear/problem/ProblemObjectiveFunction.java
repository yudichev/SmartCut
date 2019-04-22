package math.linear.problem;

/*
 * Copyright 2001-2019 by HireRight, Inc. All rights reserved.
 * This software is the confidential and proprietary information
 * of HireRight, Inc. Use is subject to license terms.
 */

/*
 * Represents an objective function in the linear programming problem
 */

import math.linear.basic.ObjectiveFunctionType;

public class ProblemObjectiveFunction
{
    private static final String ERROR_INDEX_OUT_OF_BOUNDS = "Index is out of bounds.";
    private static final String ERROR_ZERO_LENGTH = "Cannot make functional with zero length.";

    private final double[] coefficients;
    private final ObjectiveFunctionType type;

    ProblemObjectiveFunction(double[] coefficients, ObjectiveFunctionType type){
        this.coefficients = coefficients;
        this.type = type;
    }

    public final double getCoefficientAt(int k){
        if(k >= 0 && k < this.coefficients.length){
            return this.coefficients[k];
        } else {
            throw new RuntimeException(ERROR_INDEX_OUT_OF_BOUNDS);
        }
    }

    public final double[] getCoefficients(){
        double[] newValues = new double[coefficients.length];
        System.arraycopy(this.coefficients, 0, newValues, 0, this.coefficients.length);
        return newValues;
    }

    public final ObjectiveFunctionType getType() {
        return this.type;
    }


    public final static ProblemObjectiveFunction make(double[] values, ObjectiveFunctionType type){
        if(values.length == 0){
            throw new RuntimeException(ERROR_ZERO_LENGTH);
        }
        double[] valuesExtended = new double[values.length + 1];
        valuesExtended[0] = 0.d;
        System.arraycopy(values, 0, valuesExtended, 1, values.length);
        return new ProblemObjectiveFunction(valuesExtended, type);
    }



}
