package fr.pantheonsorbonne.miage.engine;

import org.junit.jupiter.api.Test;

import fr.pantheonsorbonne.miage.Cartes.Card;
import fr.pantheonsorbonne.miage.Cartes.CardColor;
import fr.pantheonsorbonne.miage.Cartes.CardValue;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    @Test
    void testIsPetit() {
        Card petitCard = new Card(CardColor.ATOUT, CardValue.AS);
        assertTrue(petitCard.isPetit());

        Card notPetitCard = new Card(CardColor.ATOUT, CardValue.DIX);
        assertFalse(notPetitCard.isPetit());
    }

    @Test
    void testIsBout() {
        // Ajoutez des tests similaires pour la méthode isBout()
    }

    @Test
    void testIsKing() {
        // Ajoutez des tests similaires pour la méthode isKing()
    }

    @Test
    void testIsQueen() {
        // Ajoutez des tests similaires pour la méthode isQueen()
    }

    @Test
    void testIsValet() {
        // Ajoutez des tests similaires pour la méthode isValet()
    }

    @Test
    void testIsCavalier() {
        // Ajoutez des tests similaires pour la méthode isCavalier()
    }

    @Test
    void testIsPetiteCarte() {
        // Ajoutez des tests similaires pour la méthode isPetiteCarte()
    }

    @Test
    void testCardsToString() {
        // Ajoutez des tests similaires pour la méthode cardsToString()
    }

    @Test
    void testCreateTarotDeck() {
        // Ajoutez des tests similaires pour la méthode createTarotDeck()
    }

    @Test
    void testCompareTo() {
        // Ajoutez des tests similaires pour la méthode compareTo()
    }
}
