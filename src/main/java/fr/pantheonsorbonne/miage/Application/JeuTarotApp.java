package fr.pantheonsorbonne.miage.Application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;

import fr.pantheonsorbonne.miage.Cartes.Card;
import fr.pantheonsorbonne.miage.Deck.Deck;
import fr.pantheonsorbonne.miage.Deck.RandomDeck;
import fr.pantheonsorbonne.miage.JeuTarot.TarotEngine;
import fr.pantheonsorbonne.miage.Joueurs.Player;

public class JeuTarotApp extends TarotEngine {

    private final List<Player> initialPlayers;
    private final Map<String, Queue<Card>> playerCards = new HashMap<>();
    private static Scanner scanner = new Scanner(System.in);
    
    public JeuTarotApp(Deck deck, List<String> playerNames, int nombreDeManche) {
        super(deck, nombreDeManche);
        this.initialPlayers = new ArrayList<>();
        for (String playerName : playerNames) {
            Player player = new Player(playerName); // Création de DumbPlayer
            this.initialPlayers.add(player);
            playerCards.put(playerName, new LinkedList<>());
        }
    }

    public static void main(String... args) {
        // Demander le nombre de joueurs
        System.out.print("Entrez le nombre de joueurs (3 ou 4) : ");
        int numberOfPlayers = scanner.nextInt();
        scanner.nextLine(); // Consommer la nouvelle ligne restante

        List<String> players = new ArrayList<>();
        for (int i = 0; i < numberOfPlayers; i++) {
            System.out.print("Entrez le nom du joueur " + (i + 1) + " : ");
            String playerName = scanner.nextLine();
            players.add(playerName);
        }

        System.out.print("Entrez le nombre de manches : ");
        int nombreDeManche = scanner.nextInt();
        scanner.nextLine(); // Consommer la nouvelle ligne restante

        TarotEngine localTarotGame = new JeuTarotApp(new RandomDeck(), players, nombreDeManche);
        localTarotGame.play();
        System.exit(0);
    }

    @Override
    public List<Player> getInitialPlayers() {
        return this.initialPlayers;
    }

}
