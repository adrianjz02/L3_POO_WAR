package fr.pantheonsorbonne.miage.Deck;

import java.util.Collection;

import fr.pantheonsorbonne.miage.Cartes.Card;

public interface Deck {

    default Card getCard() {
        return getCards(1)[0];
    }

    void displayDeck();

    Card[] getCards(int length);

    boolean isEmpty();

    int size();

    void addCard(Card card);

    void addAllCards(Collection<Card> cards);
}
