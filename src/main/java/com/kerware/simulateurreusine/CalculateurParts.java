package com.kerware.simulateurreusine;
import com.kerware.simulateur.SituationFamiliale;
public class CalculateurParts {
    public double calculer(SituationFamiliale sf, int nbe, int nbeh, boolean iso) {
        double p = (sf == SituationFamiliale.MARIE || sf == SituationFamiliale.PACSE) ? 2.0 : 1.0;
        p += (nbe <= 2) ? nbe * 0.5 : 1.0 + (nbe - 2);
        if (iso && nbe > 0) p += 0.5;
        p += nbeh * 0.5;
        return p;
    }
}