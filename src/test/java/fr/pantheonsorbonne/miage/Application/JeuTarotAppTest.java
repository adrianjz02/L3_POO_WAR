package fr.pantheonsorbonne.miage.Application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.pantheonsorbonne.miage.Deck.RandomDeck;
import fr.pantheonsorbonne.miage.Joueurs.Player;

public class JeuTarotAppTest {

    private JeuTarotApp jeuTarotApp;

    @BeforeEach
    void setUp() {
        jeuTarotApp = new JeuTarotApp(new RandomDeck(), List.of("Player1", "Player2", "Player3"), 3);
    }

    @Test
    void testConstructor() {
        assertNotNull(jeuTarotApp.getInitialPlayers());
    }


    @Test
    void testPlayerInitialization() {
        List<Player> initialPlayers = jeuTarotApp.getInitialPlayers();
        assertEquals(3, initialPlayers.size());

        for (Player player : initialPlayers) {
            assertNotNull(player);
        }
    }


    @Test
    void testMainMethod() {
        // Mock user input for the main method
        String input = "3\nPlayer1\nPlayer2\nPlayer3\n3\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        // Capture the standard output
        // You may need to adjust this part based on your testing framework
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Run the main method
        JeuTarotApp.main();

        // Restore the standard input and output
        System.setIn(System.in);
        System.setOut(System.out);

        // Perform assertions on the output
        String consoleOutput = outputStream.toString();
        assertTrue(consoleOutput.contains("Game Over"));
        // Add more assertions based on your specific output
    }
}
