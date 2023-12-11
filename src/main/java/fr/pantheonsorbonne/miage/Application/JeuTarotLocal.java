package fr.pantheonsorbonne.miage.Application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import fr.pantheonsorbonne.miage.Cartes.Card;
import fr.pantheonsorbonne.miage.Deck.Deck;
import fr.pantheonsorbonne.miage.Deck.RandomDeck;
import fr.pantheonsorbonne.miage.JeuTarot.TarotEngine;
import fr.pantheonsorbonne.miage.Joueurs.DumbPlayer;
import fr.pantheonsorbonne.miage.Joueurs.Player;

public class JeuTarotLocal extends TarotEngine {

    private final List<Player> initialPlayers;
    private final Map<String, Queue<Card>> playerCards = new HashMap<>();

    protected JeuTarotLocal(Deck deck, List<String> playerNames, int nombreDeManche) {
        super(deck, nombreDeManche);
        this.initialPlayers = new ArrayList<>();
        for (String playerName : playerNames) {
            Player player = new DumbPlayer(playerName);
            this.initialPlayers.add(player);
            playerCards.put(playerName, new LinkedList<>());
        }
    }

    public static void main(String... args) {
        TarotEngine localTarotGame = new JeuTarotLocal(new RandomDeck(), Arrays.asList("Adrian", "Nino", "Nicolas", "Banane"),
                10);
        localTarotGame.play();
        System.exit(0);
    }

    @Override
    protected List<Player> getInitialPlayers() {
        return this.initialPlayers;
    }

}
