package com.kerware.simulateur;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class SimulateurTest {

    private ICalculateurImpot calculateur;

    @BeforeEach
    void setUp() {
        calculateur = new AdaptateurCodeHerite();
    }

    @Test
    @DisplayName("Doit rejeter un revenu net négatif")
    void testRevenuNetNegatif() {
        assertThrows(IllegalArgumentException.class, () -> calculateur.setRevenusNet(-500));
    }

    @Test
    @DisplayName("Doit rejeter une situation familiale nulle")
    void testSituationFamilialeNulle() {
        assertThrows(IllegalArgumentException.class, () -> calculateur.setSituationFamiliale(null));
    }

    @Test
    @DisplayName("Doit rejeter un nombre d'enfants négatif")
    void testEnfantsNegatif() {
        assertThrows(IllegalArgumentException.class, () -> calculateur.setNbEnfantsACharge(-1));
    }

    @Test
    @DisplayName("Doit rejeter un nb d'enfants handicapés supérieur au nb total d'enfants")
    void testEnfantsHandicapesInvalides() {
        calculateur.setNbEnfantsACharge(2);
        assertThrows(IllegalArgumentException.class, () -> calculateur.setNbEnfantsSituationHandicap(3));
    }

    @ParameterizedTest(name = "Revenu {0} € -> Abattement attendu : {1} €")
    @CsvSource({
        "2000, 495",
        "30000, 3000",
        "200000, 14171"
    })
    @DisplayName("EXG_IMPOT_02 - Vérification des limites d'abattement")
    void testAbattement(int revenu, int abattementAttendu) {
        calculateur.setRevenusNet(revenu);
        calculateur.calculImpotSurRevenuNet();
        assertEquals(abattementAttendu, calculateur.getAbattement());
    }

    @ParameterizedTest(name = "Sit: {0}, Enf: {1}, Isolé: {2}, Handicap: {3} -> Parts attendues: {4}")
    @CsvSource({
        "CELIBATAIRE, 0, false, 0, 1.0",
        "MARIE, 0, false, 0, 2.0",
        "DIVORCE, 2, false, 0, 2.0",
        "CELIBATAIRE, 3, false, 0, 3.0",
        "CELIBATAIRE, 1, true, 0, 2.0",
        "MARIE, 1, false, 1, 3.0"
    })
    @DisplayName("EXG_IMPOT_03 - Calcul du nombre de parts")
    void testCalculParts(SituationFamiliale situation, int nbEnfants, boolean isole, int nbHandicap, double partsAttendues) {
        calculateur.setSituationFamiliale(situation);
        calculateur.setNbEnfantsACharge(nbEnfants);
        calculateur.setParentIsole(isole);
        calculateur.setNbEnfantsSituationHandicap(nbHandicap);
        calculateur.calculImpotSurRevenuNet();
        
        assertEquals((int) partsAttendues, calculateur.getNbPartsFoyerFiscal());
    }

    @ParameterizedTest(name = "Revenu: {0}, Sit: {1}, Enf: {2}, Handi: {3}, Isolé: {4} -> Impôt: {5}")
    @CsvSource({
        "65000, MARIE, 3, 0, false, 685",
        "65000, MARIE, 3, 1, false, 0",
        "35000, DIVORCE, 1, 0, true, 550",
        "35000, DIVORCE, 2, 0, true, 0",
        "50000, DIVORCE, 3, 0, true, 1",
        "50000, DIVORCE, 3, 1, true, 0",
        "200000, CELIBATAIRE, 0, 0, false, 60768"
    })
    @DisplayName("GOLDEN MASTER - Résultats globaux (EXG_01, 04, 05, 06)")
    void testGoldenMaster(int revenu, SituationFamiliale sit, int nbEnf, int nbHandi, boolean isole, int impotAttendu) {
        calculateur.setRevenusNet(revenu);
        calculateur.setSituationFamiliale(sit);
        calculateur.setNbEnfantsACharge(nbEnf);
        calculateur.setNbEnfantsSituationHandicap(nbHandi);
        calculateur.setParentIsole(isole);
        
        calculateur.calculImpotSurRevenuNet();
        
        assertEquals(impotAttendu, calculateur.getImpotSurRevenuNet());
    }
}