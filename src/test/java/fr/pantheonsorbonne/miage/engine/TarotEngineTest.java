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
import java.util.Map;

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
        TarotEngine tarotEngine = new TarotEngineImpl(deck, 10);

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
        TarotEngine tarotEngine = new TarotEngineImpl(deck, 1);

        // Taille initiale du deck
        int tailleInitialeDeck = deck.size();

        // Exécution de la méthode
        tarotEngine.distributeCards(players, 3, chien, donneur);

        // Vérifications
        assertTrue(chien.getLength() <= 3, "Le chien doit avoir 3 cartes ou moins");
        for (Player player : players) {
            assertTrue(player.getLength() > 0, "Chaque joueur doit avoir des cartes");
        }
        assertEquals(tailleInitialeDeck - chien.getLength() - players.stream().mapToInt(Player::getLength).sum(),
                deck.size(), "Le nombre de cartes restantes dans le deck doit être correct");

    }

    @Test
    public void testDemanderLesMises() {
        // Préparer les joueurs
        Player joueur1 = new Player("Joueur1");
        joueur1.setMise("Petite"); // Assurez-vous que cette méthode est implémentée dans Player
        Player joueur2 = new Player("Joueur2");
        joueur2.setMise("Garde");

        List<Player> players = Arrays.asList(joueur1, joueur2);

        // Assurez-vous de fournir un deck initialisé
        Deck deck = new RandomDeck(); // ou une autre implémentation de Deck
        TarotEngine tarotEngine = new TarotEngineImpl(deck, 10);

        // Appel de la méthode
        Map<Player, String> mises = tarotEngine.demanderLesMises(players);

        // Vérifications
        assertEquals("Petite", mises.get(joueur1), "La mise de Joueur1 doit être Petite");
        assertEquals("Garde", mises.get(joueur2), "La mise de Joueur2 doit être Garde");
    }

    @Test
    public void testCountBouts() {
        // Création d'un ensemble de cartes, dont certaines sont des bouts
        List<Card> pli = Arrays.asList(
                new Card(CardColor.ATOUT, CardValue.AS), // Supposons que c'est un bout
                new Card(CardColor.HEARTS, CardValue.ROI),
                new Card(CardColor.ATOUT, CardValue.VINGT_ET_UN), // Supposons que c'est aussi un bout
                new Card(CardColor.DIAMONDS, CardValue.REINE)
        );

        TarotEngine tarotEngine = new TarotEngineImpl(deck, 1);

        int expectedBouts = 2; // Le nombre attendu de bouts dans le deck
        int actualBouts = tarotEngine.countBouts(pli);

        assertEquals(expectedBouts, actualBouts, "Le nombre de bouts doit être correct");
    }

    @Test
    public void testDonnerDeuxCartesAuJoueurDeDroite() {
        // Préparation des joueurs
        Player joueur1 = new Player("Joueur1");
        joueur1.addAllCardsToHand(Arrays.asList(new Card(CardColor.ATOUT, CardValue.AS), new Card(CardColor.HEARTS, CardValue.ROI))); // Ajoutez 2 cartes
        Player joueur2 = new Player("Joueur2");
        joueur2.addAllCardsToHand(Arrays.asList(new Card(CardColor.ATOUT, CardValue.VINGT_ET_UN), new Card(CardColor.ATOUT, CardValue.VINGT))); // Ajoutez 2 cartes
        Player joueur3 = new Player("Joueur3");
        joueur3.addAllCardsToHand(Arrays.asList(new Card(CardColor.HEARTS, CardValue.REINE), new Card(CardColor.HEARTS, CardValue.VALET))); // Ajoutez 2 cartes

        List<Player> players = Arrays.asList(joueur1, joueur2, joueur3);

        TarotEngine tarotEngine = new TarotEngineImpl(deck, 1);

        // Enregistrez le nombre de cartes initiales pour chaque joueur
        int cartesInitialesJoueur1 = joueur1.getLength();
        int cartesInitialesJoueur2 = joueur2.getLength();
        int cartesInitialesJoueur3 = joueur3.getLength();

        // Exécutez la méthode
        tarotEngine.donnerDeuxCartesAuJoueurDeDroite(players);

        // Vérifiez si chaque joueur a reçu deux cartes du joueur à sa gauche
        assertEquals(cartesInitialesJoueur1, joueur1.getLength(), "Joueur1 doit avoir le même nombre de cartes");
        assertEquals(cartesInitialesJoueur2 + 2, joueur2.getLength(), "Joueur2 doit recevoir 2 cartes de Joueur1");
        assertEquals(cartesInitialesJoueur3 + 2, joueur3.getLength(), "Joueur3 doit recevoir 2 cartes de Joueur2");
    }


    @Test
    public void testVerifPetitAuBoutAttaquantGagne() {
        // Préparation des joueurs, des plis et du jeu
        Player attaquant = new Player("Attaquant");
        Player defenseur1 = new Player("Defenseur1");
        Player defenseur2 = new Player("Defenseur2");
        List<Player> players = Arrays.asList(attaquant, defenseur1, defenseur2);

        List<Card> deckPliAttaquant = new ArrayList<>(); // Créez un pli contenant le Petit pour l'attaquant
        deckPliAttaquant.add(new Card(CardColor.ATOUT, CardValue.AS)); // Ajoute le "Petit" (Atout 1)
        deckPliAttaquant.add(new Card(CardColor.HEARTS, CardValue.ROI)); // Roi de Cœur
        deckPliAttaquant.add(new Card(CardColor.DIAMONDS, CardValue.REINE)); // Dame de Carreau

        List<Card> deckPliDefenseur = new ArrayList<>(); // Créez un pli pour les défenseurs sans le Petit

        TarotEngine tarotEngine = new TarotEngineImpl(deck, 10);
        tarotEngine.verifPetitAuBout(players.size(), attaquant, attaquant, players, deckPliAttaquant, deckPliDefenseur);

        // Vérifications
        assertEquals(10, attaquant.getScore(), "L'attaquant devrait avoir un bonus pour le Petit au bout");
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
