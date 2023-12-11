package fr.pantheonsorbonne.miage.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.pantheonsorbonne.miage.Application.JeuTarotApp;
import fr.pantheonsorbonne.miage.Deck.RandomDeck;
import fr.pantheonsorbonne.miage.JeuTarot.TarotEngine; // Ajout de cette ligne

class TarotEngineTest {
    private JeuTarotApp jeuTarotApp;

    @BeforeEach
    void setUp() {
        // Vous pouvez initialiser vos objets ici avant chaque test si nécessaire
    }

    @Test
    void testJeuTarotAppInitialization() {
        // Testez l'initialisation de votre classe JeuTarotApp

        // Exemple :
        List<String> playerNames = List.of("Joueur1", "Joueur2", "Joueur3");
        int nombreDeManche = 3;
        jeuTarotApp = new JeuTarotApp(new RandomDeck(), playerNames, nombreDeManche);

        assertNotNull(jeuTarotApp);
        assertEquals(playerNames.size(), jeuTarotApp.getInitialPlayers().size());
    }
}
