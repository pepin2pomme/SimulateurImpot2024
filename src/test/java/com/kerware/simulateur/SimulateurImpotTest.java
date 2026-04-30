package com.kerware.simulateur;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class SimulateurImpotTest {

    private ICalculateurImpot calculateur;

    @BeforeEach
    void setUp() {
        calculateur = new com.kerware.simulateurreusine.AdaptateurCodeReusine();
    }

    private int calculer(int revenuNet, SituationFamiliale sf, int nbEnfants, int nbEnfantsH, boolean parentIsole) {
        calculateur.setRevenusNet(revenuNet);
        calculateur.setSituationFamiliale(sf);
        calculateur.setNbEnfantsACharge(nbEnfants);
        calculateur.setNbEnfantsSituationHandicap(nbEnfantsH);
        calculateur.setParentIsole(parentIsole);
        calculateur.calculImpotSurRevenuNet();
        return calculateur.getImpotSurRevenuNet();
    }

    @Nested
    class TestsNegatifs {
        @Test
        void revenuNetNegatif() {
            assertThrows(IllegalArgumentException.class, () -> calculateur.setRevenusNet(-1));
        }
        @Test
        void situationFamilialeNull() {
            assertThrows(IllegalArgumentException.class, () -> calculateur.setSituationFamiliale(null));
        }
        @Test
        void nbEnfantsNegatif() {
            assertThrows(IllegalArgumentException.class, () -> calculateur.setNbEnfantsACharge(-1));
        }
        @Test
        void nbEnfantsHandicapesSuperieurTotal() {
            calculateur.setNbEnfantsACharge(1);
            assertThrows(IllegalArgumentException.class, () -> calculateur.setNbEnfantsSituationHandicap(2));
        }
    }

    @Nested
    class TestsAbattement {
        @Test
        void abattementNormal() {
            calculer(50000, SituationFamiliale.CELIBATAIRE, 0, 0, false);
            assertEquals(5000, calculateur.getAbattement());
        }
        @Test
        void abattementPlafondMax() {
            calculer(200000, SituationFamiliale.CELIBATAIRE, 0, 0, false);
            assertEquals(14171, calculateur.getAbattement());
        }
        @Test
        void abattementPlancherMin() {
            calculer(3000, SituationFamiliale.CELIBATAIRE, 0, 0, false);
            assertEquals(495, calculateur.getAbattement());
        }
    }

    @Nested
    class TestsParts {
        @Test
        void celibataireSansEnfant() {
            calculer(40000, SituationFamiliale.CELIBATAIRE, 0, 0, false);
            assertEquals(1, calculateur.getNbPartsFoyerFiscal());
        }
        @Test
        void marieTroisEnfants() {
            calculer(40000, SituationFamiliale.MARIE, 3, 0, false);
            assertEquals(4, calculateur.getNbPartsFoyerFiscal());
        }
    }

    @ParameterizedTest
    @CsvSource({
            "0,       CELIBATAIRE, 0, 0, false,  0",
            "15000,   CELIBATAIRE, 0, 0, false,  0",
            "30000,   CELIBATAIRE, 0, 0, false,  1637",
            "60000,   CELIBATAIRE, 0, 0, false,  9486",
            "200000,  CELIBATAIRE, 0, 0, false,  60768",
            "40000,   MARIE,       0, 0, false,  698",
            "65000,   MARIE,       3, 0, false,  685",
            "35000,   DIVORCE,     1, 0, true,   550",
            "50000,   DIVORCE,     3, 0, true,   1"
    })
    void impotAttendu(int revenuNet, String sfStr, int nbEnfants, int nbEnfantsH, boolean parentIsole, int impotAttendu) {
        SituationFamiliale sf = SituationFamiliale.valueOf(sfStr);
        assertEquals(impotAttendu, calculer(revenuNet, sf, nbEnfants, nbEnfantsH, parentIsole));
    }

    @Test
    void revenuZero() {
        assertEquals(0, calculer(0, SituationFamiliale.CELIBATAIRE, 0, 0, false));
    }
}