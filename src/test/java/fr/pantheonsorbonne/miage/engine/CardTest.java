package fr.pantheonsorbonne.miage.engine;

import org.junit.jupiter.api.Test;  // Assurez-vous d'importer la bonne annotation Test
import fr.pantheonsorbonne.miage.Cartes.Card;  // Ajoutez cette ligne d'importation
import fr.pantheonsorbonne.miage.Cartes.CardColor;
import fr.pantheonsorbonne.miage.Cartes.CardValue;
import static org.junit.jupiter.api.Assertions.*;


class CardTest {

    // ...

    @Test
    void testIsPetit() {
        Card petit = new Card(CardColor.ATOUT, CardValue.AS);
        assertTrue(petit.isPetit());

        Card nonPetit = new Card(CardColor.ATOUT, CardValue.DEUX);
        assertFalse(nonPetit.isPetit());
    }

    // ...
}
