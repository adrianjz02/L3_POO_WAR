package fr.pantheonsorbonne.miage.engine;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import fr.pantheonsorbonne.miage.Cartes.Card;
import fr.pantheonsorbonne.miage.Cartes.CardColor;
import fr.pantheonsorbonne.miage.Cartes.CardValue;
import fr.pantheonsorbonne.miage.Deck.Deck;
import fr.pantheonsorbonne.miage.Deck.RandomDeck;
import fr.pantheonsorbonne.miage.JeuTarot.TarotEngine;
import fr.pantheonsorbonne.miage.Joueurs.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TarotEngineTest {

    private Deck deck = new RandomDeck(); // ou une autre implémentation de Deck

    @Test
    public void testCalculerScoreManche() {
        // Préparer les joueurs
        List<Player> players = new ArrayList<>();
        Player attaquant = new Player("Attaquant");
        players.add(attaquant);
        players.add(new Player("Defenseur1"));
        players.add(new Player("Defenseur2"));
        players.add(new Player("Defenseur3"));

        // Instance de TarotEngine
        TarotEngine tarotEngine = new TarotEngineImpl(deck, 10);

        // Simuler un scénario de jeu
        double nombreDePoints = 60; // Points obtenus par l'attaquant
        int nombreDeBouts = 2; // Nombre de bouts en possession de l'attaquant
        String contrat = "Garde"; // Type de contrat

        // Appeler la méthode à tester
        tarotEngine.calculerScoreManche(nombreDePoints, nombreDeBouts, contrat, players, attaquant);

        // Vérifier les scores
        assertTrue(attaquant.getScore() > 0, "L'attaquant devrait avoir un score positif");
        for (Player player : players) {
            if (!player.equals(attaquant)) {
                assertTrue(player.getScore() < 0, "Les défenseurs devraient avoir un score négatif");
            }
        }
    }

    @Test
    void testCompteurPoints() {
        List<Card> deckPli = Arrays.asList(
                new Card(CardColor.ATOUT, CardValue.AS),
                new Card(CardColor.SPADES, CardValue.ROI),
                new Card(CardColor.SPADES, CardValue.REINE),
                new Card(CardColor.SPADES, CardValue.CAVALIER),
                new Card(CardColor.SPADES, CardValue.VALET),
                new Card(CardColor.SPADES, CardValue.DIX));

        TarotEngine tarotEngine = new TarotEngineImpl(deck, 10);

        double expectedPoints = 4.5 + +4.5 + 3.5 + 2.5 + 1.5 + 0.5; // Selon vos règles de jeu
        double actualPoints = tarotEngine.compteurPoints(deckPli);

        assertEquals(expectedPoints, actualPoints, 0.01);
    }

    @Test
    public void testAllPlayersHaveSameNumberOfCards() {
        List<Player> players = Arrays.asList(new Player("Joueur1"), new Player("Joueur2"), new Player("Joueur3"));

        TarotEngine tarotEngine = new TarotEngineImpl(deck, 10);

        for (Player player : players) {
            player.addCardsToHand(deck.getCards(10));
        }

        assertTrue(tarotEngine.ontTousLeMemeNombreDeCartes(players),
                "Tous les joueurs devraient avoir le même nombre de cartes");
    }

    @Test
    public void testPlayersHaveDifferentNumberOfCards() {
        List<Player> players = new ArrayList<>();

        Player p1 = new Player("Adrian");
        Player p2 = new Player("Nino");
        Player p3 = new Player("Monsieur Herbaut");

        p1.addCardsToHand(deck.getCards(7));
        p2.addCardsToHand(deck.getCards(9));
        p3.addCardsToHand(deck.getCards(10));

        players.add(p1);
        players.add(p2);
        players.add(p3);
        TarotEngine tarotEngine = new TarotEngineImpl(deck, 10);

        assertFalse(tarotEngine.ontTousLeMemeNombreDeCartes(players),
                "Les joueurs ne devraient pas avoir le même nombre de cartes");
    }


    @Test
    public void testPlayersListIsNull() {
        TarotEngine tarotEngine = new TarotEngineImpl(deck, 10);

        assertThrows(IllegalArgumentException.class, () -> tarotEngine.ontTousLeMemeNombreDeCartes(null),
                "La liste des joueurs ne peut pas être null");
    }

    @Test
    public void testPlayersListIsEmpty() {
        TarotEngine tarotEngine = new TarotEngineImpl(deck, 10);

        assertThrows(IllegalArgumentException.class,
                () -> tarotEngine.ontTousLeMemeNombreDeCartes(Collections.emptyList()),
                "La liste des joueurs ne peut pas être vide");
    }

    @Test
    public void testAjouterAuChien() {
        // Initialisation du deck et du joueur chien
        Deck deck = new RandomDeck(); // Assurez-vous que ce deck contient suffisamment de cartes
        Player chien = new Player("Chien");

        // Création de l'instance de TarotEngine
        TarotEngine tarotEngine = new TarotEngineImpl(deck,10);

        // Nombre de cartes à ajouter
        int nombreCartes = 6;

        // Appel de la méthode
        tarotEngine.ajouterAuChien(chien, nombreCartes);

        // Vérification
        assertEquals(nombreCartes, chien.getLength(), "Le chien doit avoir le nombre correct de cartes");

    }


    
    @Test
    public void testDistributeCards() {
        // Préparation de l'environnement de test
        Deck deck = new RandomDeck(); // Deck avec un nombre de cartes prédéfini
        List<Player> players = Arrays.asList(new Player("Joueur1"), new Player("Joueur2"), new Player("Joueur3"));
        Player chien = new Player("Chien");
        Player donneur = players.get(0); // Le premier joueur est le donneur

        // Initialisation de TarotEngine
        TarotEngine tarotEngine = new TarotEngineImpl(deck,1);

        // Taille initiale du deck
        int tailleInitialeDeck = deck.size();

        // Exécution de la méthode
        tarotEngine.distributeCards(players, 3, chien, donneur);

        // Vérifications
        assertTrue(chien.getLength() <= 3, "Le chien doit avoir 3 cartes ou moins");
        for (Player player : players) {
            assertTrue(player.getLength() > 0, "Chaque joueur doit avoir des cartes");
        }
        assertEquals(tailleInitialeDeck - chien.getLength() - players.stream().mapToInt(Player::getLength).sum(), deck.size(), "Le nombre de cartes restantes dans le deck doit être correct");

    }


    private static class TarotEngineImpl extends TarotEngine {
        public TarotEngineImpl(Deck deck, int nombreDeManche) {
            super(deck, nombreDeManche);
        }

        @Override
        protected List<Player> getInitialPlayers() {
            throw new UnsupportedOperationException("Unimplemented method 'getInitialPlayers'");
        }
    }
}
