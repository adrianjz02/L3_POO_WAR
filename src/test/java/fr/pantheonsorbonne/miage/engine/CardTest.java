package fr.pantheonsorbonne.miage.engine;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;  // Assurez-vous d'importer la bonne annotation Test
import fr.pantheonsorbonne.miage.Cartes.Card;  // Ajoutez cette ligne d'importation
import fr.pantheonsorbonne.miage.Cartes.CardColor;
import fr.pantheonsorbonne.miage.Cartes.CardValue;

class CardTest {

    // ...

    @Test
    void testIsPetit() {
        Card petit = new Card(CardColor.ATOUT, CardValue.AS);
        assertTrue(petit.isPetit());

        Card nonPetit = new Card(CardColor.ATOUT, CardValue.DEUX);
        assertFalse(nonPetit.isPetit());
    }

    @Test
    void testIsBout() {
        Card excuseCard = new Card(CardColor.EXCUSE, CardValue.EXCUSE);
        assertTrue(excuseCard.isBout(), "Excuse devrait être un bout");

        Card asAtoutCard = new Card(CardColor.ATOUT, CardValue.AS);
        assertTrue(asAtoutCard.isBout(), "AS d'atout devrait être un bout");

        Card vingtEtUnCard = new Card(CardColor.ATOUT, CardValue.VINGT_ET_UN);
        assertTrue(vingtEtUnCard.isBout(), "VINGT_ET_UN d'atout devrait être un bout");

        // Ajoutez d'autres cas de test pour isBout() si nécessaire
    }

    @Test
    void testIsKing() {
        Card kingCard = new Card(CardColor.CLUBS, CardValue.ROI);
        assertTrue(kingCard.isKing(), "Carte ROI devrait être un roi");

        Card notKingCard = new Card(CardColor.CLUBS, CardValue.DIX);
        assertFalse(notKingCard.isKing(), "Carte DIX ne devrait pas être un roi");

        // Ajoutez d'autres cas de test pour isKing() si nécessaire
    }

    // ...
}
