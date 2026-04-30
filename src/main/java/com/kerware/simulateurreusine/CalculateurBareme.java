package com.kerware.simulateurreusine;
public class CalculateurBareme {
    private static final int[] L = {0, 11294, 28797, 82341, 177106, Integer.MAX_VALUE};
    private static final double[] T = {0.0, 0.11, 0.30, 0.41, 0.45};
    public double calculerBrut(double rfr, double pts) {
        double ri = rfr / pts;
        double imp = 0;
        for (int i = 0; i < 5; i++) {
            if (ri >= L[i] && ri < L[i+1]) {
                imp += (ri - L[i]) * T[i];
                break;
            } else {
                imp += (L[i+1] - L[i]) * T[i];
            }
        }
        return Math.round(imp * pts);
    }
}