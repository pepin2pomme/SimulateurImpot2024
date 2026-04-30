package com.kerware.simulateur;
import com.kerware.simulateurreusine.Simulateur;

public class AdaptateurCodeHerite implements ICalculateurImpot {

    private Simulateur simulateur;
    
    private int revenusNet = 0;
    private SituationFamiliale situationFamiliale = SituationFamiliale.CELIBATAIRE;
    private int nbEnfantsACharge = 0;
    private int nbEnfantsSituationHandicap = 0;
    private boolean parentIsole = false;
    
    private int impotFinal = 0;

    public AdaptateurCodeHerite() {
        this.simulateur = new Simulateur();
    }

    @Override
    public void setRevenusNet(int rn) {
        if (rn < 0) throw new IllegalArgumentException("Les revenus nets ne peuvent pas être négatifs.");
        this.revenusNet = rn;
    }

    @Override
    public void setSituationFamiliale(SituationFamiliale sf) {
        if (sf == null) throw new IllegalArgumentException("La situation familiale est obligatoire.");
        this.situationFamiliale = sf;
    }

    @Override
    public void setNbEnfantsACharge(int nbe) {
        if (nbe < 0) throw new IllegalArgumentException("Le nombre d'enfants ne peut pas être négatif.");
        this.nbEnfantsACharge = nbe;
    }

    @Override
    public void setNbEnfantsSituationHandicap(int nbesh) {
        if (nbesh < 0 || nbesh > this.nbEnfantsACharge) {
            throw new IllegalArgumentException("Le nombre d'enfants handicapés est invalide par rapport au total.");
        }
        this.nbEnfantsSituationHandicap = nbesh;
    }

    @Override
    public void setParentIsole(boolean pi) {
        this.parentIsole = pi;
    }

    @Override
    public void calculImpotSurRevenuNet() {
        this.impotFinal = (int) this.simulateur.calculImpot(
            this.revenusNet, 
            this.situationFamiliale, 
            this.nbEnfantsACharge, 
            this.nbEnfantsSituationHandicap, 
            this.parentIsole
        );
    }

    @Override
    public int getRevenuFiscalReference() {
        return (int) this.simulateur.getrFRef();
    }

    @Override
    public int getAbattement() {
        return (int) this.simulateur.getAbt();
    }

    @Override
    public int getNbPartsFoyerFiscal() {
        return (int) this.simulateur.getNbPts();
    }

    @Override
    public int getImpotAvantDecote() {
        return (int) (this.simulateur.getmImp() + this.simulateur.getDecote());
    }

    @Override
    public int getDecote() {
        return (int) this.simulateur.getDecote();
    }

    @Override
    public int getImpotSurRevenuNet() {
        return this.impotFinal;
    }
}