package fr.pantheonsorbonne.miage.Cartes;

public enum CardValue {
    EXCUSE("Excuse"),
    AS("1"),
    DEUX("2"),
    TROIS("3"),
    QUATRE("4"),
    CINQ("5"),
    SIX("6"),
    SEPT("7"),
    HUIT("8"),
    NEUF("9"),
    DIX("10"),
    ONZE("11"),
    DOUZE("12"),
    TREIZE("13"),
    QUATORZE("14"),
    QUINZE("15"),
    SEIZE("16"),
    DIX_SEPT("17"),
    DIX_HUIT("18"),
    DIX_NEUF("19"),
    VINGT("20"),
    VINGT_ET_UN("21"),
    VALET("Valet"),
    CAVALIER("Cavalier"),
    REINE("Reine"),
    ROI("Roi");

    private final String symbol;

    CardValue(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }
}
