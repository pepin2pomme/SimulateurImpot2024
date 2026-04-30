package com.kerware.simulateurreusine;
public class CalculateurAbattement {
    public double calculer(int revenu) {
        double a = revenu * 0.10;
        return Math.min(Math.max(a, 495), 14171);
    }
}