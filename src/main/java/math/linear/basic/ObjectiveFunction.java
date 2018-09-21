package math.linear.basic;

/*
 * Copyright 2001-2018 by HireRight, Inc. All rights reserved.
 * This software is the confidential and proprietary information
 * of HireRight, Inc. Use is subject to license terms.
 */

import java.util.stream.DoubleStream;

public class ObjectiveFunction
{
	private static String ERROR_INDEX_OUT_OF_BOUNDS = "Index is out of bounds.";
	private static String ERROR_ZERO_LENGTH = "Cannot create functional with zero length.";

	private double[] values;
	private int indexOfMaximum = 0;
	private int indexOfMaximumAbs = 0;
	private ObjectiveFunctionType type;

	private ObjectiveFunction(double[] values, ObjectiveFunctionType type){
		this.values = values;
		this.type = type;
		calculateIndexOfMaximum();
		calculateIndexOfMaximumAbsolute();
	}

	public double getValueAt(int k){
		if(k >= 0 && k < this.values.length){
			return this.values[k];
		} else {
			throw new RuntimeException(ERROR_INDEX_OUT_OF_BOUNDS);
		}
	}

	public ObjectiveFunctionType getType() {
			return this.type;
		}

	public boolean isOptimal(){
		return DoubleStream.of(this.values).noneMatch(val -> val > 0);
	}

	public ObjectiveFunction copy(){
		double[] newValues = new double[values.length];
		System.arraycopy(this.values,0 ,newValues ,0, values.length);
		return new ObjectiveFunction(newValues, this.type);
	}

	public static ObjectiveFunction create(double[] values, ObjectiveFunctionType type){
		if(values.length == 0){
			throw new RuntimeException(ERROR_ZERO_LENGTH);
		}
		double[] valuesCopy = new double[values.length];
		System.arraycopy(values, 0, valuesCopy, 0, values.length);
		return new ObjectiveFunction(values, type);
	}

	private void calculateIndexOfMaximum(){
		double maxValue = values[0];
		for(int k = 1; k < values.length; k++){
			if(maxValue < values[k]){
				maxValue = values[k];
				this.indexOfMaximum = k;
			}
		}
	}

	private void calculateIndexOfMaximumAbsolute(){
		double maxValue = Math.abs(values[0]);
		for(int k = 1; k < values.length; k++){
			if(maxValue < Math.abs(values[k])){
				maxValue = Math.abs(values[k]);
				this.indexOfMaximumAbs = k;
			}
		}
	}
}
