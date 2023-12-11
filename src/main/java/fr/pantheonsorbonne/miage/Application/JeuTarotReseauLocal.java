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

public class JeuTarotReseauLocal extends TarotEngine {

    private final List<Player> initialPlayers;
    private final Map<String, Queue<Card>> playerCards = new HashMap<>();

    protected JeuTarotReseauLocal(Deck deck, List<String> playerNames, int nombreDeManche) {
        super(deck, nombreDeManche);
        this.initialPlayers = new ArrayList<>();
        for (int i = 0; i < playerNames.size(); i++) {
            Player player;
            if (i < playerNames.size() - 1) {
                // Pour les premiers joueurs, créez des instances de DumbPlayer
                player = new DumbPlayer(playerNames.get(i));
            } else {
                // Pour le dernier joueur, créez une instance de Player
                player = new Player(playerNames.get(i));
            }
            this.initialPlayers.add(player);
            playerCards.put(playerNames.get(i), new LinkedList<>());
        }
    }

    public static void main(String... args) {
        TarotEngine localTarotGame = new JeuTarotReseauLocal(new RandomDeck(), Arrays.asList("Adrian", "Nino", "Nicolas", "Banane"),
                50);
        localTarotGame.play();
        System.exit(0);
    }

    @Override
    public List<Player> getInitialPlayers() {
        return this.initialPlayers;
    }

}
