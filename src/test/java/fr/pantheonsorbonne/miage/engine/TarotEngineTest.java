package fr.pantheonsorbonne.miage.engine;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import fr.pantheonsorbonne.miage.Joueurs.Player;
import fr.pantheonsorbonne.miage.model.Game;


public class TarotEngineTest {
    
    @Test
    public void testAllPlayersHaveSameNumberOfCards() {
        List<Player> players = new ArrayList<>();
        Player attaquant = new Player("Player1");
        Player defenseur1 = new Player("Player2");
        Player defenseur2 = new Player("Player3");

        players.add(attaquant); // Supposons que Player ait un constructeur qui définit le nombre de cartes
        players.add(defenseur1);
        players.add(defenseur2);

        Game game = new Game();
        assertTrue("Tous les joueurs devraient avoir le même nombre de cartes", game.ontTousLeMemeNombreDeCartes(players));
    }


}
