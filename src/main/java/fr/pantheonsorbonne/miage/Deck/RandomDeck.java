package fr.pantheonsorbonne.miage.Deck;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import fr.pantheonsorbonne.miage.Cartes.Card;

public class RandomDeck implements Deck {

    private final Queue<Card> deck = new LinkedList<>();

    public RandomDeck() {
        List<Card> cards = Card.createTarotDeck();
        Collections.shuffle(cards);
        for (int i = 0; i < cards.size(); i++) {
            this.deck.offer(cards.get(i));
        }
    }

    @Override
    public void displayDeck() {
        for (Card card : deck) {
            System.out.println(card);
        }
    }

    @Override
    public int size() {
        return this.deck.size();
    }

    @Override
    public Card[] getCards(int length) {
        Card[] result = new Card[length];
        for (int i = 0; i < length; i++) {
            result[i] = this.deck.poll();
        }
        return result;
    }

    @Override
    public boolean isEmpty() {
        return deck.isEmpty();
    }


    @Override
    public void addCard(Card card) {
        this.deck.offer(card); // Ajoute une carte à la queue
    }
    
    @Override
    public void addAllCards(Collection<Card> cards) {
    this.deck.addAll(cards); // Ajoute toutes les cartes à la queue
}


}