package fr.pantheonsorbonne.miage.Joueurs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import fr.pantheonsorbonne.miage.Cartes.Card;
import fr.pantheonsorbonne.miage.Cartes.CardColor;

public class Player {

    private String name;
    private List<Card> hand; // La main du joueur, contenant ses cartes.
    private String mise;
    private int score;
    public static Scanner scanner = new Scanner(System.in);

    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>(); // Initialise la main du joueur à vide.
        this.mise = null; // Initialise la mise du joueur
        this.score = 0;
    }

    // Méthode pour ajouter une carte à la main du joueur.
    public void addCardsToHand(Card[] cards) {
        for (Card card : cards) {
            if (card != null) {
                hand.add(card);
            }
        }
    }

    public void addAllCardsToHand(List<Card> cards) {
        // Convertir la liste de cartes en un tableau
        Card[] cardsArray = new Card[cards.size()];
        cardsArray = cards.toArray(cardsArray);

        // Utiliser la méthode existante pour ajouter les cartes
        addCardsToHand(cardsArray);
    }

    // Méthode pour retirer une carte de la main du joueur.
    public boolean removeCardFromHand(Card card) {
        return hand.remove(card);
    }

    // Méthode pour obtenir la main du joueur.
    public List<Card> getHand() {
        return new ArrayList<>(hand); // Retourne une copie de la main pour éviter la modification directe.
    }

    // Méthode pour obtenir le nom du joueur.
    public String getName() {
        return this.name;
    }

    // Méthode pour afficher la main du joueur pas mélangé.
    public void showHand() {
        System.out.println(name + " : " + hand);
    }

    // Méthode pour afficher la main du joueur une fois triée.
    public void showHandSorted() {
        System.out.println("[A = Atout ; P = Pique ; = T = Trèfle ; C = Coeur ; D = Carreau (Diamond)]");
        CardColor currentColor = null;
        for (Card card : hand) {
            if (currentColor != card.color()) {
                // Changement de couleur, aller à la ligne
                if (currentColor != null) {
                    System.out.println(); // Saut de ligne pour chaque nouvelle couleur
                }
                currentColor = card.color();
                System.out.print(currentColor + " : ");
            }
            System.out.print(card.value() + " ");
        }
        System.out.println(); // Nouvelle ligne à la fin de l'affichage
    }

    public int getLength() {
        return this.hand.size();
    }

    // Méthode pour définir la mise du joueur.
    public void setMise(String mise) {
        this.mise = mise;
    }

    // Méthode pour obtenir la mise du joueur.
    public String getMise() {
        return this.mise;
    }

    public int getScore() {
        return this.score;
    }

    public void addScore(int points) {
        this.score += points;
    }

    // On demande la mise du joueur
    public String demanderMise(String[] optionsDeMise, int miseMaxIndex) {
        System.out.println();
        showHand();
        System.out.print(getName() + ", choisissez votre mise (Passer, Petite, Garde, Garde sans, Garde contre): ");
        String mise;
        int miseIndex;

        do {
            mise = scanner.nextLine();
            miseIndex = Arrays.asList(optionsDeMise).indexOf(mise);

            if (miseIndex <= miseMaxIndex && miseIndex != 0) {
                System.out.println("Vous devez choisir une mise supérieure ou passer.");
            }
        } while (miseIndex <= miseMaxIndex && miseIndex != 0);

        return mise;
    }

    // On demande au joueur si il veut distribuer ou donner au chien
    public String choisirActionPourLeChien() {
        System.out.print("Choisissez l'action: Distribuer (D) ou Ajouter au Chien (C): ");
        String action = scanner.nextLine();
        return action;
    }

    // On demande au jouer la taille du chien en fonction du nombre de joueur
    public int askDogSize(int numPlayers) {

        int dogSize;

        while (true) {
            System.out.println(this.getName() + " veuillez choisir la taille du chien. ");
            if (numPlayers == 3) {
                System.out.print("Options valides: 3, 6, 9 : ");
            } else if (numPlayers == 4) {
                System.out.print("Options valides: 2, 6, 10 : ");
            }

            dogSize = scanner.nextInt();
            scanner.nextLine(); // Lire et ignorer le caractère de fin de ligne restant

            // Vérifiez si la taille du chien est valide en fonction du nombre de joueurs
            if ((numPlayers == 3 && (dogSize == 3 || dogSize == 6 || dogSize == 9)) ||
                    (numPlayers == 4 && (dogSize == 2 || dogSize == 6 || dogSize == 10))) {
                break; // Taille valide, sortir de la boucle
            } else {
                System.out.println("Taille du chien invalide. Veuillez réessayer.");
            }
        }
        return dogSize;
    }

    // Demande au joueur quelle carte il souhaite défausser
    public Card choisirUneCarteADefausser() {
        System.out.print(this.getName()
                + ", entrez la carte que vous souhaitez défausser (par exemple, 'C5' pour le 5 de coeur) : ");

        while (true) {
            String choix = scanner.nextLine();
            // Trouver la carte correspondante dans la main du joueur
            for (Card card : this.hand) {
                if (card.toString().equalsIgnoreCase(choix)) {
                    System.out.println(this.getName() + " a défausser la carte : " + card);
                    return card;
                }
            }

            System.out.print("Carte non trouvée, veuillez réessayer : ");
        }
    }

    // Choisi une carte qu'il souhaite jouer
    public Card choisirUneCarte() {
        System.out.print(this.getName()
                + ", entrez la carte que vous souhaitez choisir (par exemple, 'C5' pour le 5 de coeur) : ");

        while (true) {
            String choix = scanner.nextLine();
            // Trouver la carte correspondante dans la main du joueur
            for (Card card : this.hand) {
                if (card.toString().equalsIgnoreCase(choix)) {
                    System.out.println(this.getName() + " a choisi la carte : " + card);
                    return card;
                }
            }

            System.out.print("Carte non trouvée, veuillez réessayer : ");
        }
    }

    // Vérifie si le joueur possède bien la couleur demander dans son deck pour
    // pouvoir jouer
    public boolean checkColor(CardColor color) {
        return this.hand.stream().anyMatch(card -> card.color() == color);
    }

    // Retourne les cartes qui étaient dans la main
    public List<Card> emptyHand() {
        List<Card> cardsToReturn = new ArrayList<>(this.hand);
        this.hand.clear(); // Vide la main du joueur
        return cardsToReturn;
    }

    public void sortHand() {
        Collections.sort(this.hand, new Comparator<Card>() {
            @Override
            public int compare(Card c1, Card c2) {
                // First, compare by color using the accessor methods
                int colorCompare = Integer.compare(c1.color().ordinal(), c2.color().ordinal());
                if (colorCompare != 0) {
                    return colorCompare;
                }

                // Si les couleurs sont identidiques on compare par leur valeur

                return Integer.compare(c2.value().ordinal(), c1.value().ordinal());

            }
        });
    }

    // Règles supplémentaire : S'il y un pli avec que des aouts, les joueurs donnent
    // deux cartes au hasard au joueur de droite.
    public List<Card> choisirDeuxCartes() {
        List<Card> chosenCards = new ArrayList<>();

        System.out.println(this.getName() + ", choisissez deux cartes à donner : ");
        while (chosenCards.size() < 2) {
            Card card = choisirUneCarte();
            if (!chosenCards.contains(card)) { // Assurez-vous que la carte n'est pas déjà choisie
                chosenCards.add(card);
                this.hand.remove(card); // Retirer la carte choisie de la main

                if (chosenCards.size() < 2) {
                    System.out.print("Choisissez une autre carte : ");
                }
            } else {
                System.out.print("Cette carte a déjà été choisie, veuillez en choisir une autre : ");
            }
        }

        return chosenCards;
    }

}
