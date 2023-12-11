package fr.pantheonsorbonne.miage.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

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

    @Test
    void testIsQueen() {
        Card queenCard = new Card(CardColor.CLUBS, CardValue.REINE);
        assertTrue(queenCard.isQueen(), "Carte REINE devrait être une reine");

        Card notQueenCard = new Card(CardColor.CLUBS, CardValue.DIX);
        assertFalse(notQueenCard.isQueen(), "Carte DIX ne devrait pas être une reine");

        // Ajoutez d'autres cas de test pour isQueen() si nécessaire
    }

    @Test
    void testIsValet() {
        Card valetCard = new Card(CardColor.CLUBS, CardValue.VALET);
        assertTrue(valetCard.isValet(), "Carte VALET devrait être un valet");

        Card notValetCard = new Card(CardColor.CLUBS, CardValue.DIX);
        assertFalse(notValetCard.isValet(), "Carte DIX ne devrait pas être un valet");

        // Ajoutez d'autres cas de test pour isValet() si nécessaire
    }

    @Test
    void testIsCavalier() {
        Card cavalierCard = new Card(CardColor.CLUBS, CardValue.CAVALIER);
        assertTrue(cavalierCard.isCavalier(), "Carte CAVALIER devrait être un cavalier");

        Card notCavalierCard = new Card(CardColor.CLUBS, CardValue.DIX);
        assertFalse(notCavalierCard.isCavalier(), "Carte DIX ne devrait pas être un cavalier");

        // Ajoutez d'autres cas de test pour isCavalier() si nécessaire
    }

    @Test
    void testIsPetiteCarte() {
        Card petiteCarte = new Card(CardColor.CLUBS, CardValue.SEPT);
        assertTrue(petiteCarte.isPetiteCarte(), "Carte SEPT devrait être une petite carte");

        Card roiCarte = new Card(CardColor.CLUBS, CardValue.ROI);
        assertFalse(roiCarte.isPetiteCarte(), "Carte ROI ne devrait pas être une petite carte");

        // Ajoutez d'autres cas de test pour isPetiteCarte() si nécessaire
    }

    @Test
    void testToString() {
        Card card = new Card(CardColor.CLUBS, CardValue.AS);
        assertEquals("T1", card.toString(), "La représentation en chaîne de la carte devrait être correcte");
    }

    @Test
    void testCardsToString() {
        Card[] cards = {
                new Card(CardColor.CLUBS, CardValue.AS),
                new Card(CardColor.CLUBS, CardValue.DIX),
                new Card(CardColor.CLUBS, CardValue.ROI)
        };
        assertEquals("SOME_COLORAS;SOME_COLORDIX;SOME_COLORROI", Card.cardsToString(cards),
                "La représentation en chaîne des cartes devrait être correcte");
    }

    @Test
    void testCreateTarotDeck() {
        List<Card> deck = Card.createTarotDeck();

        // Assurez-vous que le deck contient l'Excuse
        assertTrue(deck.stream().anyMatch(card -> card.color() == CardColor.EXCUSE && card.value() == CardValue.EXCUSE),
                "Le deck devrait contenir l'Excuse");

        // Assurez-vous que le deck contient des atouts
        assertTrue(deck.stream().anyMatch(card -> card.color() == CardColor.ATOUT),
                "Le deck devrait contenir des atouts");

        // Assurez-vous que le deck contient des cartes de différentes couleurs et valeurs
        for (CardColor color : CardColor.values()) {
            for (CardValue value : CardValue.values()) {
                assertTrue(deck.stream().anyMatch(card -> card.color() == color && card.value() == value),
                        "Le deck devrait contenir une carte de couleur " + color + " et de valeur " + value);
            }
        }
    }

    @Test
    void testCompareTo() {
        Card atoutCard = new Card(CardColor.ATOUT, CardValue.AS);
        Card nonAtoutCard = new Card(CardColor.CLUBS, CardValue.AS);

        assertTrue(atoutCard.compareTo(nonAtoutCard) > 0, "Les atouts devraient être plus forts que les non-atouts");
        assertTrue(nonAtoutCard.compareTo(atoutCard) < 0, "Les non-atouts devraient être plus faibles que les atouts");

        // Ajoutez d'autres cas de test pour compareTo() si nécessaire
    }

    // ...
}
