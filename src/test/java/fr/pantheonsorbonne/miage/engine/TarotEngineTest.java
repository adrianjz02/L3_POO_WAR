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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public void testCountBouts() {
        // Création d'un ensemble de cartes, dont certaines sont des bouts
        List<Card> pli = Arrays.asList(
                new Card(CardColor.ATOUT, CardValue.AS), // Supposons que c'est un bout
                new Card(CardColor.HEARTS, CardValue.ROI),
                new Card(CardColor.ATOUT, CardValue.VINGT_ET_UN), // Supposons que c'est aussi un bout
                new Card(CardColor.DIAMONDS, CardValue.REINE));

        TarotEngine tarotEngine = new TarotEngineImpl(deck, 1);

        int expectedBouts = 2; // Le nombre attendu de bouts dans le deck
        int actualBouts = tarotEngine.countBouts(pli);

        assertEquals(expectedBouts, actualBouts, "Le nombre de bouts doit être correct");
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

    @Test
    public void testAreAllCardsAtouts_True() {
        // Créez un pli contenant uniquement des cartes Atout
        List<Card> pli = new ArrayList<>();
        pli.add(new Card(CardColor.ATOUT, CardValue.AS));
        pli.add(new Card(CardColor.ATOUT, CardValue.VINGT));
        pli.add(new Card(CardColor.ATOUT, CardValue.DIX_HUIT));

        TarotEngine tarotEngine = new TarotEngineImpl(deck, 1);

        // Vérifiez si toutes les cartes du pli sont des atouts
        boolean result = tarotEngine.areAllCardsAtouts(pli);

        // Vérifications
        assertEquals(true, result, "Toutes les cartes du pli sont des atouts, la méthode devrait renvoyer true");
    }

    @Test
    public void testAreAllCardsAtouts_False() {
        // Créez un pli contenant uniquement des cartes Atout
        List<Card> pli = new ArrayList<>();
        pli.add(new Card(CardColor.ATOUT, CardValue.AS));
        pli.add(new Card(CardColor.DIAMONDS, CardValue.ROI));
        pli.add(new Card(CardColor.ATOUT, CardValue.DIX_HUIT));

        TarotEngine tarotEngine = new TarotEngineImpl(deck, 1);

        // Vérifiez si toutes les cartes du pli sont des atouts
        boolean result = tarotEngine.areAllCardsAtouts(pli);

        // Vérifications
        assertEquals(false, result, "Toutes les cartes du pli sont des atouts, la méthode devrait renvoyer true");
    }

    @Test
    public void testHasHigherAtout() {
        // Créez une carte Atout de référence
        Card referenceCard = new Card(CardColor.ATOUT, CardValue.AS);

        // Créez une liste de cartes contenant des Atouts
        List<Card> DeckAtout = new ArrayList<>();
        DeckAtout.add(new Card(CardColor.ATOUT, CardValue.VINGT));
        DeckAtout.add(new Card(CardColor.ATOUT, CardValue.DIX_HUIT));

        // Créez une liste de cartes contenant des Atouts égaux ou inférieurs
        List<Card> DeckPetitAtout = new ArrayList<>();
        DeckPetitAtout.add(new Card(CardColor.ATOUT, CardValue.ROI));
        DeckPetitAtout.add(new Card(CardColor.ATOUT, CardValue.REINE));
        DeckPetitAtout.add(new Card(CardColor.ATOUT, CardValue.AS)); // Carte de référence

        TarotEngine tarotEngine = new TarotEngineImpl(deck, 1);

        // Vérifications
        assertEquals(true, tarotEngine.hasHigherAtout(referenceCard, DeckAtout),
                "Il y a des Atouts supérieurs dans le deck, la méthode devrait renvoyer true");
        assertEquals(true, tarotEngine.hasHigherAtout(referenceCard, DeckPetitAtout),
                "Toutes les cartes Atout dans le deck sont égales ou inférieures à la carte de référence, la méthode devrait renvoyer false");
    }

    @Test
    public void testReconstitutionDeck() {
        // Préparation des joueurs et du chien
        Player joueur1 = new Player("Joueur1");
        joueur1.addAllCardsToHand(Arrays.asList(new Card(CardColor.HEARTS, CardValue.ROI)));
        Player joueur2 = new Player("Joueur2");
        joueur2.addAllCardsToHand(Arrays.asList(new Card(CardColor.DIAMONDS, CardValue.REINE)));
        Player chien = new Player("Chien");
        chien.addAllCardsToHand(Arrays.asList(new Card(CardColor.CLUBS, CardValue.VALET)));

        List<Player> players = Arrays.asList(joueur1, joueur2);
        Deck deck = new RandomDeck(); // ou une autre implémentation de Deck

        int tailleInitialeDeck = deck.size(); // Taille initiale du deck

        TarotEngine tarotEngine = new TarotEngineImpl(deck, 1);

        // Exécution de la méthode
        tarotEngine.reconstitutionDeck(players, deck, chien);

        // Vérifications
        assertEquals(tailleInitialeDeck + 3, deck.size(), "Le deck doit avoir toutes les cartes récupérées");
        assertTrue(joueur1.emptyHand().isEmpty(), "La main du joueur 1 doit être vide");
        assertTrue(joueur2.emptyHand().isEmpty(), "La main du joueur 2 doit être vide");
        assertTrue(chien.emptyHand().isEmpty(), "La main du chien doit être vide");
    }

    @Test
    public void testTousLesJoueursOntPasse() {
        Map<Player, String> mises = new HashMap<>();
        mises.put(new Player("Joueur1"), "Passer");
        mises.put(new Player("Joueur2"), "Passer");
        mises.put(new Player("Joueur3"), "Passer");

        TarotEngine tarotEngine = new TarotEngineImpl(deck, 1);

        // Exécution de la méthode
        boolean redistributionNecessaire = tarotEngine.verifierEtRedistribuerSiNecessaire(mises);

        // Vérifications
        assertTrue(redistributionNecessaire, "Une redistribution doit être nécessaire si tous les joueurs ont passé");
    }

    @Test
    public void testAuMoinsUnJoueurNePassePas() {
        Map<Player, String> mises = new HashMap<>();
        mises.put(new Player("Joueur1"), "Passer");
        mises.put(new Player("Joueur2"), "Petite");
        mises.put(new Player("Joueur3"), "Passer");

        TarotEngine tarotEngine = new TarotEngineImpl(deck, 1);

        // Exécution de la méthode
        boolean redistributionNecessaire = tarotEngine.verifierEtRedistribuerSiNecessaire(mises);

        // Vérifications
        assertFalse(redistributionNecessaire,
                "Une redistribution ne doit pas être nécessaire si au moins un joueur ne passe pas");
    }

    @Test
    public void testTrouverJoueurAvecLaPlusGrosseMise() {
        // Créer un ensemble de mises
        Map<Player, String> mises = new HashMap<>();
        Player joueur1 = new Player("Joueur1");
        Player joueur2 = new Player("Joueur2");
        Player joueur3 = new Player("Joueur3");

        mises.put(joueur1, "Petite");
        mises.put(joueur2, "Garde");
        mises.put(joueur3, "Passer");

        TarotEngine tarotEngine = new TarotEngineImpl(deck, 1);

        // Appeler la méthode
        Player joueurAvecLaPlusGrosseMise = tarotEngine.trouverJoueurAvecLaPlusGrosseMise(mises);

        // Vérifier que le joueur avec la plus grosse mise est correctement identifié
        assertEquals(joueur2, joueurAvecLaPlusGrosseMise, "Joueur2 doit avoir la plus grosse mise");
    }

    @Test
    public void testDistribuerAuxJoueurs() {
        // Création d'un deck fictif avec un certain nombre de cartes

        // Création d'un joueur
        Player joueur = new Player("Joueur1");

        // Initialisation de TarotEngine avec le deck fictif
        TarotEngine tarotEngine = new TarotEngineImpl(deck, 1);

        int nombreCartesADistribuer = 5;
        int tailleInitialeMain = joueur.getLength();

        // Exécution de la méthode
        tarotEngine.distribuerAuxJoueurs(joueur, nombreCartesADistribuer);

        // Vérification
        assertEquals(tailleInitialeMain + nombreCartesADistribuer, joueur.getLength(),
                "Le joueur doit avoir reçu le nombre correct de cartes");
    }

    @Test
    public void testMoveFirstToLast() {
        // Préparation des joueurs
        Player joueur1 = new Player("Joueur1");
        Player joueur2 = new Player("Joueur2");
        Player joueur3 = new Player("Joueur3");
        List<Player> players = new ArrayList<>(Arrays.asList(joueur1, joueur2, joueur3));

        TarotEngine tarotEngine = new TarotEngineImpl(deck, 1);

        // Exécution de la méthode
        tarotEngine.moveFirstToLast(players);

        // Vérifications
        assertEquals(joueur2, players.get(0), "Joueur2 doit être maintenant le premier joueur");
        assertEquals(joueur3, players.get(1), "Joueur3 doit être maintenant le deuxième joueur");
        assertEquals(joueur1, players.get(2), "Joueur1 doit être maintenant le dernier joueur");
    }

    @Test
    public void testMovePlayerToFirst() {
        // Préparation des joueurs dans une liste modifiable
        Player joueur1 = new Player("Joueur1");
        Player joueur2 = new Player("Joueur2");
        Player joueur3 = new Player("Joueur3");
        List<Player> players = new ArrayList<>();
        players.add(joueur1);
        players.add(joueur2);
        players.add(joueur3);

        TarotEngine tarotEngine = new TarotEngineImpl(deck, 1);

        // Joueur2 devrait être déplacé en première position
        tarotEngine.movePlayerToFirst(joueur2, players);

        // Vérification que Joueur2 est maintenant le premier joueur
        assertEquals(joueur2, players.get(0), "Joueur2 doit être le premier joueur");
        // Vérification que les autres joueurs sont dans l'ordre attendu
        assertEquals(joueur3, players.get(1), "Joueur3 doit être maintenant en deuxième position");
        assertEquals(joueur1, players.get(2), "Joueur1 doit être maintenant en dernière position");
    }

    @Test
    public void testGererPli() {
        // Création des joueurs et initialisation de leur main
        Player attaquant = new MockPlayer("Attaquant", Stream.of(
                new Card(CardColor.ATOUT, CardValue.VINGT_ET_UN), // Le bout
                new Card(CardColor.HEARTS, CardValue.ROI) // Une autre carte
        ).collect(Collectors.toList()));

        Player defenseur = new MockPlayer("Defenseur", Stream.of(
                new Card(CardColor.ATOUT, CardValue.DIX), // Un atout mais pas le plus fort
                new Card(CardColor.CLUBS, CardValue.REINE) // Une autre carte
        ).collect(Collectors.toList()));

        List<Player> joueurs = new ArrayList<>();
        joueurs.add(attaquant);
        joueurs.add(defenseur);

        // Initialisation de TarotEngine
        TarotEngine tarotEngine = new TarotEngineImpl(deck, 1);

        List<Card> pliAttaquant = new ArrayList<>();
        List<Card> pliDefenseur = new ArrayList<>();

        // Exécution de la méthode
        Player gagnantDuPli = tarotEngine.gererPli(joueurs, attaquant, pliAttaquant, pliDefenseur);

        // Vérifications
        assertEquals(attaquant, gagnantDuPli, "L'attaquant doit gagner le pli avec le bout");
        assertTrue(pliAttaquant.contains(new Card(CardColor.ATOUT, CardValue.VINGT_ET_UN)),
                "Le pli de l'attaquant doit contenir le bout");
        assertTrue(pliDefenseur.isEmpty(), "Le pli du défenseur doit être vide");
    }

    @Test
    public void testGererPliToutesCartesAtouts() {
        // Création des joueurs et initialisation de leur main
        Player attaquant = new MockPlayer("Attaquant", Stream.of(
                new Card(CardColor.ATOUT, CardValue.VINGT_ET_UN) // Atout 21
        ).collect(Collectors.toList()));

        Player defenseur1 = new MockPlayer("Defenseur1", Stream.of(
                new Card(CardColor.ATOUT, CardValue.DIX) // Atout 10
        ).collect(Collectors.toList()));

        Player defenseur2 = new MockPlayer("Defenseur2", Stream.of(
                new Card(CardColor.ATOUT, CardValue.DOUZE) // Atout 12

        ).collect(Collectors.toList()));

        List<Player> joueurs = new ArrayList<>();
        joueurs.add(attaquant);
        joueurs.add(defenseur1);
        joueurs.add(defenseur2);

        // Initialisation de TarotEngine
        TarotEngine tarotEngine = new TarotEngineImpl(deck, 1);

        List<Card> pliAttaquant = new ArrayList<>();
        List<Card> pliDefenseur = new ArrayList<>();

        // Exécution de la méthode
        Player gagnantDuPli = tarotEngine.gererPli(joueurs, attaquant, pliAttaquant, pliDefenseur);

        // Vérifications
        assertEquals(attaquant.getName(), gagnantDuPli.getName(),
                "L'attaquant doit gagner le pli avec le plus fort atout (Atout 21)");
        assertTrue(pliAttaquant.contains(new Card(CardColor.ATOUT, CardValue.VINGT_ET_UN)),
                "Le pli de l'attaquant doit contenir le plus fort atout (Atout 21)");

        assertTrue(pliAttaquant.contains(new Card(CardColor.ATOUT, CardValue.DIX)),
                "Le pli de l'attaquant doit contenir un atout de Defenseur1 (Atout 10)");
        assertTrue(pliAttaquant.contains(new Card(CardColor.ATOUT, CardValue.DOUZE)));
        assertTrue(pliDefenseur.isEmpty(), "Le pli des défenseurs doit être vide");
    }

    @Test
    public void testGererPliAvecExcuse() {
        // Création des joueurs et initialisation de leur main
        Player attaquant = new MockPlayer("Attaquant", Stream.of(
                new Card(CardColor.ATOUT, CardValue.VINGT_ET_UN), // Le bout
                new Card(CardColor.HEARTS, CardValue.ROI) // Une autre carte
        ).collect(Collectors.toList()));

        Player defenseur = new MockPlayer("Defenseur", Stream.of(
                new Card(CardColor.EXCUSE, CardValue.EXCUSE) // L'Excuse
        ).collect(Collectors.toList()));

        Player defenseur2 = new MockPlayer("Defenseur", Stream.of(
                new Card(CardColor.ATOUT, CardValue.DIX), // Un atout mais pas le plus fort
                new Card(CardColor.CLUBS, CardValue.REINE) // Une autre carte
        ).collect(Collectors.toList()));

        List<Player> joueurs = new ArrayList<>();
        joueurs.add(attaquant);
        joueurs.add(defenseur);
        joueurs.add(defenseur2);

        // Initialisation de TarotEngine
        TarotEngine tarotEngine = new TarotEngineImpl(deck, 1);

        List<Card> pliAttaquant = new ArrayList<>();
        List<Card> pliDefenseur = new ArrayList<>();

        // Exécution de la méthode
        Player gagnantDuPli = tarotEngine.gererPli(joueurs, attaquant, pliAttaquant, pliDefenseur);

        // Vérifications
        assertEquals(attaquant.getName(), gagnantDuPli.getName(), "L'attaquant doit gagner le pli avec l'Excuse");
        assertFalse(pliAttaquant.isEmpty(), "Le pli de l'attaquant doit être vide");
        assertTrue(pliDefenseur.contains(new Card(CardColor.EXCUSE, CardValue.EXCUSE)),
                "Le pli du défenseur doit contenir l'Excuse");
    }

    @Test
    public void testGererPliAvecExcuseAttaquant() {
        // Création des joueurs et initialisation de leur main
        Player attaquant = new MockPlayer("Attaquant", Stream.of(
                new Card(CardColor.ATOUT, CardValue.VINGT_ET_UN), // Le bout
                new Card(CardColor.EXCUSE, CardValue.EXCUSE) // Une autre carte
        ).collect(Collectors.toList()));

        Player defenseur = new MockPlayer("Defenseur", Stream.of(
                new Card(CardColor.HEARTS, CardValue.ROI) // L'Excuse
        ).collect(Collectors.toList()));

        Player defenseur2 = new MockPlayer("Defenseur", Stream.of(
                new Card(CardColor.ATOUT, CardValue.DIX), // Un atout mais pas le plus fort
                new Card(CardColor.CLUBS, CardValue.REINE) // Une autre carte
        ).collect(Collectors.toList()));

        List<Player> joueurs = new ArrayList<>();
        joueurs.add(attaquant);
        joueurs.add(defenseur);
        joueurs.add(defenseur2);

        // Initialisation de TarotEngine
        TarotEngine tarotEngine = new TarotEngineImpl(deck, 1);

        List<Card> pliAttaquant = new ArrayList<>();
        List<Card> pliDefenseur = new ArrayList<>();

        // Exécution de la méthode
        Player gagnantDuPli = tarotEngine.gererPli(joueurs, attaquant, pliAttaquant, pliDefenseur);

        // Vérifications
        assertEquals(defenseur.getName(), gagnantDuPli.getName(), "L'attaquant doit gagner le pli avec l'Excuse");
        assertFalse(pliAttaquant.isEmpty(), "Le pli de l'attaquant doit être vide");
        assertTrue(pliAttaquant.contains(new Card(CardColor.EXCUSE, CardValue.EXCUSE)),
                "Le pli du défenseur doit contenir l'Excuse");
    }

    @Test
    public void testVerifExcuseAuBoutAttaquant() {
        // Création des joueurs et initialisation des plis
        Player attaquant = new Player("Attaquant");
        Player defenseur1 = new Player("Defenseur1");
        Player defenseur2 = new Player("Defenseur2");
        Player gagnantDernierPli = attaquant; // Simule que l'attaquant a gagné le dernier pli
        List<Player> playerList = new ArrayList<>(); // Ajoutez tous les joueurs si nécessaire

        playerList.add(attaquant);
        playerList.add(defenseur1);
        playerList.add(defenseur2);

        // Création des plis avec l'Excuse présente
        List<Card> deckPliAttaquant = new ArrayList<>();
        deckPliAttaquant.add(new Card(CardColor.EXCUSE, CardValue.EXCUSE)); // Ajoute l'Excuse
        deckPliAttaquant.add(new Card(CardColor.SPADES, CardValue.ROI)); // Autre carte
        deckPliAttaquant.add(new Card(CardColor.DIAMONDS, CardValue.REINE)); // Autre carte

        List<Card> deckPliDefenseur = new ArrayList<>();
        deckPliDefenseur.add(new Card(CardColor.ATOUT, CardValue.DEUX)); // Ajoute l'Excuse
        deckPliDefenseur.add(new Card(CardColor.SPADES, CardValue.DEUX)); // Autre carte
        deckPliDefenseur.add(new Card(CardColor.DIAMONDS, CardValue.CAVALIER)); // Autre carte

        TarotEngine tarotEngine = new TarotEngineImpl(deck, 1);

        // Exécution de la méthode
        tarotEngine.verifExcuseAuBout(playerList.size(), attaquant, gagnantDernierPli, playerList, deckPliAttaquant,
                deckPliDefenseur);

        // Vérifications
        assertFalse(deckPliAttaquant.contains(new Card(CardColor.EXCUSE, CardValue.EXCUSE)),
                "Le pli de l'attaquant ne doit pas contenir l'Excuse");
        assertTrue(deckPliDefenseur.contains(new Card(CardColor.EXCUSE, CardValue.EXCUSE)),
                "Le pli des défenseurs doit contenir l'Excuse");
    }

    @Test
    public void testVerifExcuseAuBoutDefenseurs() {
        // Créez les joueurs et initialisation des plis
        Player attaquant = new Player("Attaquant");
        Player defenseur1 = new Player("Defenseur1");
        Player defenseur2 = new Player("Defenseur2");
        Player gagnantDernierPli = defenseur1; // Simule que le défenseur 1 a gagné le dernier pli
        List<Player> playerList = new ArrayList<>(); // Ajoutez tous les joueurs si nécessaire

        playerList.add(attaquant);
        playerList.add(defenseur1);
        playerList.add(defenseur2);

        // Création des plis avec l'Excuse présente
        List<Card> deckPliAttaquant = new ArrayList<>();
        deckPliAttaquant.add(new Card(CardColor.HEARTS, CardValue.DIX)); // Autre carte
        deckPliAttaquant.add(new Card(CardColor.DIAMONDS, CardValue.VALET)); // Autre carte

        List<Card> deckPliDefenseur = new ArrayList<>();
        deckPliDefenseur.add(new Card(CardColor.EXCUSE, CardValue.EXCUSE)); // Ajoute l'Excuse
        deckPliDefenseur.add(new Card(CardColor.SPADES, CardValue.ROI)); // Autre carte
        deckPliDefenseur.add(new Card(CardColor.CLUBS, CardValue.REINE)); // Autre carte

        TarotEngine tarotEngine = new TarotEngineImpl(deck, 1);

        // Exécution de la méthode
        tarotEngine.verifExcuseAuBout(playerList.size(), attaquant, gagnantDernierPli, playerList, deckPliAttaquant,
                deckPliDefenseur);

        // Vérifications
        assertTrue(deckPliAttaquant.contains(new Card(CardColor.EXCUSE, CardValue.EXCUSE)),
                "Le pli de l'attaquant doit contenir l'Excuse");
        assertFalse(deckPliDefenseur.contains(new Card(CardColor.EXCUSE, CardValue.EXCUSE)),
                "Le pli des défenseurs ne doit pas contenir l'Excuse");
    }


    @Test
    public void testSeDefausser() {
        // Créer une liste de cartes pour le joueur, incluant un Bout et un Roi
        List<Card> hand = new ArrayList<>();
        hand.add(new Card(CardColor.ATOUT, CardValue.AS)); // Bout
        hand.add(new Card(CardColor.ATOUT, CardValue.DEUX)); // Atout ordinaire
        hand.add(new Card(CardColor.HEARTS, CardValue.ROI)); // Roi
        MockPlayer joueur = new MockPlayer("Joueur", hand);

        // Créer une liste pour recevoir les cartes défaussées
        List<Card> pliJoueur = new ArrayList<>();

        // Simuler le processus de défausse sans interaction utilisateur
        joueur.setDefausseSimulation(new Card[] {
            new Card(CardColor.ATOUT, CardValue.DEUX), // Prétend que le joueur choisit un atout ordinaire à défausser
            // Notez que l'ordre des cartes à défausser est important si vous répétez des cartes
        });

        // Exécuter la méthode de défausse
        TarotEngine tarotEngine = new TarotEngineImpl(deck, 1); // Utilisez votre implémentation concrète avec les dépendances nécessaires
        tarotEngine.seDefausser(joueur, 1, pliJoueur, new Scanner(System.in));

        // Vérifier les résultats
        assertEquals(1, pliJoueur.size(), "Une carte doit être défaussée");
        assertTrue(pliJoueur.contains(new Card(CardColor.ATOUT, CardValue.DEUX)), "La carte défaussée doit être l'Atout DEUX");
        assertFalse(joueur.getHand().contains(new Card(CardColor.ATOUT, CardValue.DEUX)), "Le joueur ne doit plus avoir l'Atout DEUX");
    }
    // Classe MockPlayer pour simuler un joueur
    private static class MockPlayer extends Player {
        private List<Card> hand;
        private Card[] defausseSimulation;

        public MockPlayer(String name, List<Card> hand) {
            super(name);
            this.hand = hand;
        }

        public void setDefausseSimulation(Card[] defausseSimulation) {
            this.defausseSimulation = defausseSimulation;
        }

        @Override
        public Card choisirUneCarteADefausser() {
            // Simule le choix d'une carte à défausser selon l'ordre prédéfini
            if (defausseSimulation.length > 0) {
                Card carte = defausseSimulation[0];
                defausseSimulation = Arrays.copyOfRange(defausseSimulation, 1, defausseSimulation.length);
                return carte;
            }
            return null;
        }


        @Override
        public Card choisirUneCarte() {
            // Assurez-vous qu'il y a des cartes à choisir
            if (!hand.isEmpty()) {
                // Simule le choix de la carte Excuse si présente, sinon la première carte
                return hand.stream()
                        .filter(card -> card.color() == CardColor.EXCUSE && card.value() == CardValue.EXCUSE)
                        .findFirst()
                        .orElse(hand.remove(0)); // Retire et retourne la première carte
            } else {
                // Retourne null si la main est vide
                return null;
            }
        }

        @Override
        public boolean checkColor(CardColor color) {
            // Vérifie si le joueur a une carte de la couleur demandée
            return hand.stream().anyMatch(card -> card.color() == color);
        }

        @Override
        public boolean removeCardFromHand(Card card) {
            return hand.remove(card);
        }

        @Override
        public int getLength() {
            // Retourne la taille de la main
            return hand.size();
        }
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
