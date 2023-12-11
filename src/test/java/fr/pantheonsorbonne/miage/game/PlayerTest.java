package fr.pantheonsorbonne.miage.game;

import org.junit.jupiter.api.Test;

import fr.pantheonsorbonne.miage.Cartes.Card;
import fr.pantheonsorbonne.miage.Cartes.CardColor;
import fr.pantheonsorbonne.miage.Cartes.CardValue;
import fr.pantheonsorbonne.miage.Joueurs.Player;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Scanner;

class PlayerTest {

    @Test
    void testAddCardsToHand() {
        Player player = new Player("John");
        Card[] cards = {
            new Card(CardColor.HEARTS, CardValue.AS),
            new Card(CardColor.DIAMONDS, CardValue.DIX),
            new Card(CardColor.CLUBS, CardValue.ROI)
        };
        player.addCardsToHand(cards);
        assertEquals(3, player.getHand().sizeC());
    }

    @Test
    void testRemoveCardFromHand() {
        Player player = new Player("Alice");
        Card cardToRemove = new Card(CardColor.SPADES, CardValue.REINE);
        player.addCardsToHand(new Card[]{cardToRemove});
        assertTrue(player.removeCardFromHand(cardToRemove));
        assertEquals(0, player.getHand().size());
    }

    @Test
    void testShowHand() {
        Player player = new Player("Bob");
        Card[] cards = {
        new Card(CardColor.CLUBS, CardValue.CINQ),
        new Card(CardColor.HEARTS, CardValue.VALET)
        };
        player.addCardsToHand(cards);
        assertEquals("Bob : [5♠, J♥]", player.getHand().toString());
    }

    @Test
    void testSortHand() {
        Player player = new Player("Charlie");
        Card[] unsortedCards = {
            new Card(CardColor.HEARTS, CardValue.DEUX),
            new Card(CardColor.CLUBS, CardValue.REINE),
            new Card(CardColor.DIAMONDS, CardValue.HUIT),
            new Card(CardColor.SPADES, CardValue.AS)
        };
        player.addCardsToHand(unsortedCards);
        player.sortHand();
        assertEquals("Charlie : [A♠, Q♣, 8♦, 2♥]", player.getHand().toString());
    }

    @Test
    void testChooseTwoCards() {
        Player player = new Player("David");
        Card[] cards = {
            new Card(CardColor.SPADES, CardValue.QUATRE),
            new Card(CardColor.HEARTS, CardValue.SEPT),
            new Card(CardColor.CLUBS, CardValue.ROI),
            new Card(CardColor.DIAMONDS, CardValue.AS)
        };
        player.addCardsToHand(cards);

        // Mocking user input for testing purposes
        player.scanner = new Scanner("S4\nH7\n");
        
        assertEquals(2, player.choisirDeuxCartes().size());
    }
    
}

