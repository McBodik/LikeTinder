package com.mcbodik.liketinder.utils;

public class VectorUtils {
	public float[] normalization(float x, float y) {
		float[] result = new float[2];
		float distance = (float) Math.sqrt(x * x + y * y);
		result[0] = x / distance;
		result[1] = y / distance;
		return result;
	}
}
