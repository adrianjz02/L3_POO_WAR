package fr.pantheonsorbonne.miage.Cartes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public record Card(CardColor color, CardValue value) implements Comparable<Card> {

    public boolean isPetit() {
        return this.color == CardColor.ATOUT && this.value == CardValue.AS;
    }

    public boolean isBout() {
        return (this.color == CardColor.EXCUSE && this.value == CardValue.EXCUSE) ||
                (this.color == CardColor.ATOUT && this.value == CardValue.AS) ||
                (this.color == CardColor.ATOUT && this.value == CardValue.VINGT_ET_UN);
    }

    public boolean isKing() {
        return this.value == CardValue.ROI;
    }

    public boolean isQueen() {
        return this.value == CardValue.REINE;
    }

    public boolean isValet() {
        return this.value == CardValue.VALET;
    }

    public boolean isCavalier() {
        return this.value == CardValue.CAVALIER;
    }

    public boolean isPetiteCarte() {
        // Vérifier si ce n'est pas un Roi, une Reine, un Cavalier, un Valet, ni un Bout
        boolean isFigure = this.value == CardValue.ROI || this.value == CardValue.REINE ||
                this.value == CardValue.CAVALIER || this.value == CardValue.VALET;
        boolean isBout = (this.color == CardColor.EXCUSE && this.value == CardValue.EXCUSE) ||
                (this.color == CardColor.ATOUT && (this.value == CardValue.AS || this.value == CardValue.VINGT_ET_UN));

        return !isFigure && !isBout;
    }

    @Override
    public String toString() {
        return color + "" + value;
    }

    public static String cardsToString(Card[] cards) {
        return Arrays.stream(cards).map(Card::toString).collect(Collectors.joining(";"));
    }

    public static List<Card> createTarotDeck() {
        List<Card> deck = new ArrayList<>();

        // Ajouter l'Excuse comme une carte unique
        deck.add(new Card(CardColor.EXCUSE, CardValue.EXCUSE));

        // Ajouter les atouts
        for (CardValue value : CardValue.values()) {
            if (value.ordinal() < CardValue.VALET.ordinal() && value != CardValue.EXCUSE) {
                deck.add(new Card(CardColor.ATOUT, value));
            }
        }

        // Ajouter les autres cartes
        for (CardColor color : CardColor.values()) {
            if (color != CardColor.ATOUT && color != CardColor.EXCUSE) {
                for (CardValue value : CardValue.values()) {
                    if ((value.ordinal() >= CardValue.VALET.ordinal() && value.ordinal() <= CardValue.ROI.ordinal()) ||
                            (value.ordinal() >= CardValue.AS.ordinal() && value.ordinal() <= CardValue.DIX.ordinal())) {
                        deck.add(new Card(color, value));
                    }
                }
            }
        }

        return deck;
    }

    @Override
    public int compareTo(Card carte) {
        if (this.color == CardColor.ATOUT && carte.color != CardColor.ATOUT) {
            return 1; // Les atouts sont toujours plus forts que les autres couleurs
        } else if (this.color != CardColor.ATOUT && carte.color == CardColor.ATOUT) {
            return -1; // Les non-atouts sont toujours plus faibles que les atouts
        } else if (this.color == CardColor.ATOUT && carte.color == CardColor.ATOUT) {
            // Comparer par valeur pour les atouts
            return this.value.ordinal() - carte.value.ordinal();
        } else {
            // Ici, vous pouvez décider de la logique de comparaison pour les non-atouts
            // Par exemple, les comparer par leur valeur ou considérer toutes les valeurs
            // comme égales
            return this.value.ordinal() - carte.value.ordinal(); // Exemple: comparaison par valeur
        }
    }

    /*
     * public static void main(String[] args) {
     * List<Card> deck = createTarotDeck();
     * Collections.shuffle(deck);
     * System.out.println(deck);
     * System.out.println(deck.size());
     * }
     * 
     */

}