package fr.pantheonsorbonne.miage.Cartes;

public enum CardColor {
    EXCUSE("E"),
    ATOUT("A"),
    SPADES("P"),
    CLUBS("T"),
    HEARTS("C"),
    DIAMONDS("D");

    private final String symbol;

    CardColor(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }
}
