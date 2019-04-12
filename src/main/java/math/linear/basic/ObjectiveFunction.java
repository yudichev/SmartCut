package math.linear.basic;

import java.util.Arrays;
import java.util.stream.DoubleStream;

public class ObjectiveFunction
{
	private static final String ERROR_INDEX_OUT_OF_BOUNDS = "Index is out of bounds.";
	private static final String ERROR_ZERO_LENGTH = "Cannot create functional with zero length.";
	private static final String ERROR_LENGTH_MISMATCH = "Objective function's length differs from the length of the array being added.";

	private final double[] values;
	private int indexOfMaximum = 0;
	private int indexOfMaximumAbs = 0;
	private int indexOfMinimum = 0;
	private int indexOfMinimumAbs = 0;
	private final ObjectiveFunctionType type;

	private ObjectiveFunction(double[] values, ObjectiveFunctionType type){
		this.values = values;
		this.type = type;
		calculateIndexOfMaximumMinimum(0);
		calculateIndexOfMaximumMinimumAbsolute();
	}

	public double getValueAt(int k){
		if(k >= 0 && k < this.values.length){
			return this.values[k];
		} else {
			throw new RuntimeException(ERROR_INDEX_OUT_OF_BOUNDS);
		}
	}

	public double[] getValues(){
		double[] newValues = new double[values.length];
		System.arraycopy(this.values, 0, newValues, 0, this.values.length);
		return newValues;
	}

	public ObjectiveFunctionType getType() {
			return this.type;
		}

	public boolean isOptimal(){
		if(ObjectiveFunctionType.MAXIMUM.equals(this.type)) {
			return DoubleStream.of(this.values).map(val -> Math.abs(val) > 1.e-14 ? val : 0.).noneMatch(val -> Double.compare(val, 0.) > 0);
		} else {
			return DoubleStream.of(this.values).map(val -> Math.abs(val) > 1.e-14 ? val : 0.).noneMatch(val -> Double.compare(val, 0.) < 0);
		}
	}

	public ObjectiveFunction add(double[] values){
		if(this.values.length != values.length)
			throw  new RuntimeException(ERROR_LENGTH_MISMATCH);
		double[] sum = new double[this.values.length];
		for(int k = 0; k < this.values.length; k++){
			sum[k] = values[k] + this.values[k];
		}
		return create(sum, this.type);
	}


	public ObjectiveFunction getCanonical(){
		if(ObjectiveFunctionType.MAXIMUM.equals(this.type)){
			return this;
		} else {
			double[] val = new double[this.values.length];
			for(int k = 0; k <this.values.length; k++)
				val[k] = this.values[k] * (-1.);
			return new ObjectiveFunction(val,ObjectiveFunctionType.MAXIMUM);
		}
	}

	public int getIndexOfMaximum(){
		return this.indexOfMaximum;
	}


	public int getIndexOfMinimum(){
		return this.indexOfMinimum;
	}


	public int getIndexOfMaximumAbs(){
		return this.indexOfMaximumAbs;
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
		System.arraycopy(values, 0, valuesCopy, 0, valuesCopy.length);
		return new ObjectiveFunction(values, type);
	}

	public double apply(double[] vector){
		if(vector.length != this.values.length)
			throw new IllegalArgumentException("The length of given array mismatches the objecive function dimensions");

		double result = 0.;
		for(int k = 0; k < vector.length; k++){
			result += vector[k]*this.values[k];
		}
		return result;
	}

	private void calculateIndexOfMaximumMinimum(int m){
		calculateIndexOfMaximumMinimum(m, values.length);
	}

	private void calculateIndexOfMaximumMinimum(int m, int n){
		double maxValue = Arrays.stream(values).limit(n).skip(m).filter(value -> Double.compare(value, 0.d) != 0).findFirst().orElse(0.d);
		double minValue = maxValue;
		for(int k = m + 1; k < n; k++){
			if(Double.compare(maxValue , values[k]) <= 0 && Double.compare(values[k], 0.d) != 0){
				maxValue = values[k];
				this.indexOfMaximum = k;
			} else if(Double.compare(minValue , values[k]) >= 0 && Double.compare(values[k], 0.d) != 0){
				minValue = values[k];
				this.indexOfMinimum = k;
			}
		}
	}

	private void calculateIndexOfMaximumMinimumAbsolute(){
		double maxValue = Math.abs(values[0]);
		double minValue = maxValue;
		for(int k = 1; k < values.length; k++){
			if(Double.compare(maxValue , Math.abs(values[k])) <= 0 && Double.compare(values[k], 0.d) != 0){
				maxValue = Math.abs(values[k]);
				this.indexOfMaximumAbs = k;
			} else if (Double.compare(minValue, Math.abs(values[k])) >= 0 && Double.compare(values[k], 0.d) != 0) {
				minValue = Math.abs(values[k]);
				this.indexOfMinimumAbs = k;
			}
		}
	}
}
