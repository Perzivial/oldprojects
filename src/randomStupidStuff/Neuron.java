package randomStupidStuff;

import java.util.ArrayList;

public class Neuron {
	double[] inputs;
	double[] weights;
	private int threshold;

	public Neuron(int thresh) {
		threshold = thresh;
	}

	public int output() {
		if (sumold() > threshold)
			return 1;
		return 0;
	}

	public int output(int threshold2) {
		if (sumold() > threshold2)
			return 1;
		return 0;
	}

	public int output(double threshold2) {
		if (sumold() > threshold2)
			return 1;
		return 0;
	}

	public double sumold() {
		double sum = 0;
		for (int i = 0; i < inputs.length; i++) {
			sum += inputs[i] * weights[i];
		}
		return sum;
	}

	public double sum() {
		double sum = 0;
		for (int i = 0; i < inputs.length; i++) {
			sum += inputs[i] * weights[i];
		}
		return sigmoid(sum);
	}

	private static double sigmoid(double x) {
		return 1 / (1 + Math.exp(-x));
	}
}
