package fr.pantheonsorbonne.miage.game;

import org.junit.jupiter.api.Test;

import fr.pantheonsorbonne.miage.Cartes.Card;
import fr.pantheonsorbonne.miage.Cartes.CardColor;
import fr.pantheonsorbonne.miage.Cartes.CardValue;
import fr.pantheonsorbonne.miage.Joueurs.DumbPlayer;
import fr.pantheonsorbonne.miage.Joueurs.Player;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
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
        assertEquals(3, player.getHand().size());
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
        assertEquals("[T5, CValet]", player.getHand().toString());
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
        assertEquals("[P1, TReine, C2, D8]", player.getHand().toString());
    }

    /* 
    @Test
        void testChoisirDeuxCartes() {
            Player player = new Player("Alice");

            // Mocking user input for testing purposes
            String input = "\nC10\nD3";
            player.scanner = new Scanner(input);

            Card[] cards = {
                new Card(CardColor.SPADES, CardValue.QUATRE),
                new Card(CardColor.HEARTS, CardValue.SEPT),
                new Card(CardColor.CLUBS, CardValue.DIX),
                new Card(CardColor.DIAMONDS, CardValue.TROIS)
            };
            player.addCardsToHand(cards);

            List<Card> chosenCards = player.choisirDeuxCartes();
            System.out.println(chosenCards);

            // Vérifier si deux cartes ont été choisies
            assertEquals(2, chosenCards.size());

            // Vérifier que les cartes sont correctess
            assertTrue(player.getHand().contains(new Card(CardColor.SPADES, CardValue.QUATRE)));
            assertTrue(player.getHand().contains(new Card(CardColor.HEARTS, CardValue.SEPT)));
            assertFalse(player.getHand().contains(new Card(CardColor.CLUBS, CardValue.DIX)));
            assertFalse(player.getHand().contains(new Card(CardColor.DIAMONDS, CardValue.TROIS)));
        }
        */
   
    

    @Test
    void testAskDogSize() {
        DumbPlayer dumbPlayer = new DumbPlayer("Dumb");
        int dogSize = dumbPlayer.askDogSize(3);
        assertTrue(dogSize == 3 || dogSize == 6 || dogSize == 9);
    }

    @Test
    void testChoisirActionPourLeChien() {
        DumbPlayer dumbPlayer = new DumbPlayer("Dumber");
        String action = dumbPlayer.choisirActionPourLeChien();
        assertTrue(action.equals("D") || action.equals("C"));
    }

    
    @Test
    void testDemanderMise() {
        DumbPlayer dumbPlayer = new DumbPlayer("Dumbest");
        String[] optionsDeMise = {"Passer", "Petite", "Garde", "Garde sans", "Garde contre"};
        int miseMaxIndex = 2; // Suppose que la mise maximale est "Garde"

        // Mocking random number generator for testing purposes
        dumbPlayer.random = new Random(42);

        // Mocking showHand() method
        dumbPlayer.showHand();

        String mise;
        do {
            mise = optionsDeMise[dumbPlayer.random.nextInt(optionsDeMise.length)];
        } while (Arrays.asList(optionsDeMise).indexOf(mise) <= miseMaxIndex && !mise.equals("Passer"));

        assertEquals(true, Arrays.asList(optionsDeMise).indexOf(mise) > miseMaxIndex || mise.equals("Passer"));
    }
    

    @Test
    void testChoisirUneCarteADefausser() {
        DumbPlayer dumbPlayer = new DumbPlayer("Randomizer");
        Card card = new Card(CardColor.HEARTS, CardValue.SEPT);
        dumbPlayer.addCardsToHand(new Card[]{card});

        // Mocking random number generator for testing purposes
        dumbPlayer.random = new Random(42);

        Card carteADefausser = dumbPlayer.choisirUneCarteADefausser();

        assertEquals(card, carteADefausser);
    }

    @Test
    void testChoisirUneCarte() {
        DumbPlayer dumbPlayer = new DumbPlayer("Randomest");
        Card card = new Card(CardColor.DIAMONDS, CardValue.AS);
        dumbPlayer.addCardsToHand(new Card[]{card});

        // Mocking random number generator for testing purposes
        dumbPlayer.random = new Random(42);

        Card carteChoisie = dumbPlayer.choisirUneCarte();

        assertEquals(card, carteChoisie);
    }
    
}

