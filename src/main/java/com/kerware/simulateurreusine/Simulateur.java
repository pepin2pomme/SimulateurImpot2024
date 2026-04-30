package com.kerware.simulateurreusine;
import com.kerware.simulateur.SituationFamiliale;

public class Simulateur {
    private double rFRef, abt, nbPts, mImp, decote;
    private final CalculateurAbattement cAbt = new CalculateurAbattement();
    private final CalculateurParts cPts = new CalculateurParts();
    private final CalculateurBareme cBar = new CalculateurBareme();

    public long calculImpot(int rev, SituationFamiliale sf, int nbe, int nbeh, boolean iso) {
        this.abt = cAbt.calculer(rev);
        this.rFRef = Math.max(0, rev - abt);
        this.nbPts = cPts.calculer(sf, nbe, nbeh, iso);
        
        double impSeul = cBar.calculerBrut(rFRef, (sf == SituationFamiliale.MARIE || sf == SituationFamiliale.PACSE) ? 2.0 : 1.0);
        double impFoyer = cBar.calculerBrut(rFRef, nbPts);

        double plafond = ((nbPts - ((sf == SituationFamiliale.MARIE || sf == SituationFamiliale.PACSE) ? 2.0 : 1.0)) / 0.5) * 1759;
        this.mImp = ((impSeul - impFoyer) >= plafond) ? (impSeul - plafond) : impFoyer;

        double seuil = (sf == SituationFamiliale.MARIE || sf == SituationFamiliale.PACSE) ? 3191 : 1929;
        double maxD = (sf == SituationFamiliale.MARIE || sf == SituationFamiliale.PACSE) ? 1444 : 873;
        this.decote = (mImp < seuil) ? Math.round(maxD - (mImp * 0.4525)) : 0;
        this.decote = Math.max(0, Math.min(decote, mImp));
        this.mImp -= decote;

        return Math.round(mImp);
    }

    public double getrFRef() { return rFRef; }
    public double getAbt() { return abt; }
    public double getNbPts() { return nbPts; }
    public double getmImp() { return mImp; }
    public double getDecote() { return decote; }
}