package fr.pantheonsorbonne.miage.Joueurs;

import java.util.Random;

import fr.pantheonsorbonne.miage.Cartes.Card;

public class DumbPlayer extends Player {

    public Random random = new Random();
    


    public DumbPlayer(String name) {
        super(name);

    }

    @Override
    public int askDogSize(int numPlayers) {
        int dogSize;
        int[] options;

        if (numPlayers == 3) {
            options = new int[] { 3, 6, 9 };
        } else if (numPlayers == 4) {
            options = new int[] { 2, 6, 10 };
        } else {
            throw new IllegalArgumentException("Nombre de joueurs non supporté: " + numPlayers);
        }

        // Choisir un indice au hasard parmi les options
        int randomIndex = random.nextInt(options.length);
        dogSize = options[randomIndex];

        System.out.println(this.getName() + " a choisi la taille du chien : " + dogSize);

        return dogSize;
    }

    @Override
    public String choisirActionPourLeChien() {
        System.out.print("Choisissez l'action: Distribuer (D) ou Ajouter au Chien (C): ");
        String action = random.nextBoolean() ? "D" : "C";
        return action;
    }

    @Override
    public String demanderMise(String[] optionsDeMise, int miseMaxIndex) {
        showHand();
        System.out.print(getName() + ", choisissez votre mise (Passer, Petite, Garde, Garde sans, Garde contre): \n");

        int miseIndex;
        do {
            miseIndex = random.nextInt(optionsDeMise.length);
        } while (miseIndex <= miseMaxIndex && miseIndex != 0);

        String mise = optionsDeMise[miseIndex];
        System.out.println("\n" + getName() + " a choisi: " + mise);

        return mise;
    }

    @Override
    public Card choisirUneCarteADefausser() {

        // Sélectionner une carte au hasard dans la main du joueur
        int randomIndex = random.nextInt(this.getHand().size());
        Card cardADefausser = this.getHand().get(randomIndex);

        System.out.println(this.getName() + " a défaussé la carte : " + cardADefausser);

        return cardADefausser;
    }

    @Override
    public Card choisirUneCarte() {

        // Sélectionner une carte au hasard dans la main du joueur
        int randomIndex = random.nextInt(this.getHand().size());
        Card cardChoisie = this.getHand().get(randomIndex);

        System.out.println(this.getName() + " a choisi la carte : " + cardChoisie);

        return cardChoisie;
    }
}
