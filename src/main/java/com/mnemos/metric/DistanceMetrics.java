package com.mnemos.metric;

import java.util.function.BiFunction;

public class DistanceMetrics {

    public static double euclidean(double[] a, double[] b) {
        double sum = 0;
        for (int i = 0; i < a.length; i++) {
            double d = a[i] - b[i];
            sum += d * d;
        }
        return Math.sqrt(sum);
    }

    public static double cosine(double[] a, double[] b) {
        double dot = 0, normA = 0, normB = 0;
        for (int i = 0; i < a.length; i++) {
            dot += a[i] * b[i];
            normA += a[i] * a[i];
            normB += b[i] * b[i];
        }
        if (normA == 0 || normB == 0) return 1.0;
        return 1.0 - (dot / (Math.sqrt(normA) * Math.sqrt(normB)));
    }

    public static double manhattan(double[] a, double[] b) {
        double sum = 0;
        for (int i = 0; i < a.length; i++) {
            sum += Math.abs(a[i] - b[i]);
        }
        return sum;
    }

    public static BiFunction<double[], double[], Double> getFunction(String name) {
        if (name == null) return DistanceMetrics::cosine;
        switch (name.toLowerCase()) {
            case "euclidean":
                return DistanceMetrics::euclidean;
            case "manhattan":
                return DistanceMetrics::manhattan;
            case "cosine":
            default:
                return DistanceMetrics::cosine;
        }
    }
}
