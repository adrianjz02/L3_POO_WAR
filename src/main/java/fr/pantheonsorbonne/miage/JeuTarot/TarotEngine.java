package fr.pantheonsorbonne.miage.JeuTarot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import fr.pantheonsorbonne.miage.Cartes.Card;
import fr.pantheonsorbonne.miage.Cartes.CardColor;
import fr.pantheonsorbonne.miage.Cartes.CardValue;
import fr.pantheonsorbonne.miage.Deck.Deck;
import fr.pantheonsorbonne.miage.Joueurs.Player;

public abstract class TarotEngine {

    private final Deck deck;
    private int nombreDeManche;

    static Scanner scanner = new Scanner(System.in);

    protected TarotEngine(Deck deck, int nombreDeManche) {
        this.deck = deck;
        this.nombreDeManche = nombreDeManche;
    }

    public void play() {

        int numPlayers = getInitialPlayers().size();
        Player dog = new Player("Chien");
        List<Player> playerList = getInitialPlayers();

        for (int i = 1; i < nombreDeManche + 1; i++) {
            System.out.println("\nMANCHE NUMÉRO : " + i + "\n");
            List<Card> deckPliAttaquant = new ArrayList<>();
            List<Card> deckPliDefenseur = new ArrayList<>();
            Map<Player, String> mises = new HashMap<>();

            // Les cartes doivent être redistribuée si tous les joueurs ont passé
            // Il faut distribuer les cartes et demander les mises au moins une fois avant
            // de vérifier si une redistribution est nécessaire
            do {
                // Distribuer les cartes
                System.out.println("LE DONNEUR EST " + playerList.get(0).getName());
                Player donneur = playerList.get(0);
                int dogSize = donneur.askDogSize(numPlayers);
                distributeCards(playerList, dogSize, dog, donneur);

                // Vérifier la taille du chien avant de demander les mises
                if (dogSize != dog.getLength()) {
                    System.out.println(
                            "La taille du chien ne correspond pas à la taille du chien demandé, redistribution nécessaire.");
                    reconstitutionDeck(playerList, deck, dog); // Méthode de redistribution
                    moveFirstToLast(playerList); // Le donneur devient celui qui est à droite du donneur.
                    continue; // Passez à la prochaine itération de la boucle do-while pour redistribuer
                } else if (!ontTousLeMemeNombreDeCartes(playerList)) {
                    System.out.println(
                            "Les joueurs ne possèdent pas le même nombre de carte.");
                    reconstitutionDeck(playerList, deck, dog);
                    moveFirstToLast(playerList);
                    continue;
                }

                // Demander les mises
                moveFirstToLast(playerList);
                mises = demanderLesMises(playerList);

                // Vérifier si une redistribution est nécessaire
                if (verifierEtRedistribuerSiNecessaire(mises)) {
                    reconstitutionDeck(playerList, deck, dog); // Méthode de redistribution
                    System.out.println("\nTOUS LES JOUEURS ONT PASSÉ. IL FAUT REDISTRIBUER.\n");
                }

            } while (verifierEtRedistribuerSiNecessaire(mises));

            Player attaquant = trouverJoueurAvecLaPlusGrosseMise(mises);
            String miseMax = mises.get(attaquant);
            System.out.println(attaquant.getName() + " a misé " + miseMax);
            distribuerChienSelonMise(attaquant, miseMax, dog, deckPliAttaquant, deckPliDefenseur);

            for (Player player : playerList) {
                player.sortHand();
            }

            System.out
                    .println("\nLES CARTES ONT ÉTÉ DISTRIBUÉES ET LES ENCHÈRES FAITES ! LA MANCHE PEUT COMMENCER !\n");

            while ((deckPliAttaquant.size() + deckPliDefenseur.size()) != 78) {

                Player gagnantPli = gererPli(playerList, attaquant, deckPliAttaquant, deckPliDefenseur);
                // afficherPli(deckPliAttaquant, "pli de l'attaquant");
                // afficherPli(deckPliDefenseur, "pli des défenseurs");
                movePlayerToFirst(gagnantPli, playerList);

                if ((deckPliAttaquant.size() + deckPliDefenseur.size()) == 78) {
                    verifPetitAuBout(numPlayers, attaquant, gagnantPli, playerList, deckPliAttaquant, deckPliDefenseur);
                    verifExcuseAuBout(numPlayers, attaquant, gagnantPli, playerList, deckPliAttaquant,
                            deckPliDefenseur);
                }

            }

            calculerScoreManche(compteurPoints(deckPliAttaquant), countBouts(deckPliAttaquant), miseMax, playerList,
                    attaquant);

            for (Player player : playerList) {
                System.out.println(player.getName() + " " + player.getScore());
            }

            deck.addAllCards(deckPliAttaquant);
            deck.addAllCards(deckPliDefenseur);

        }
    }

    public boolean ontTousLeMemeNombreDeCartes(List<Player> joueurs) {
        if (joueurs == null || joueurs.isEmpty()) {
            throw new IllegalArgumentException("La liste des joueurs ne peut pas être vide ou null.");
        }

        int tailleInitiale = joueurs.get(0).getLength();

        for (Player joueur : joueurs) {
            if (joueur.getLength() != tailleInitiale) {
                return false; // Un joueur a un nombre de cartes différent
            }
        }

        return true; // Tous les joueurs ont le même nombre de cartes
    }

    public void calculerScoreManche(double nombreDePoints, int nombreDeBouts, String contrat, List<Player> playerList,
            Player attaquant) {
        int seuil;
        switch (nombreDeBouts) {
            case 3:
                seuil = 36;
                break;
            case 2:
                seuil = 41;
                break;
            case 1:
                seuil = 51;
                break;
            default:
                seuil = 56;
                break;
        }

        System.out.println(attaquant.getName() + " possède " + nombreDeBouts
                + " bouts. Il devait donc faire un score égal ou supérieur à " + seuil);
        System.out.println("Il a obtenu " + nombreDePoints + " points.");
        boolean validiteDuContrat;
        double difference = nombreDePoints - seuil;
        System.out.println(attaquant.getName() + " avait misé : " + contrat);
        if (difference >= 0) {
            validiteDuContrat = true;
            System.out.println(attaquant.getName() + " a validé son contrat et gagne donc la manche.");
        } else {
            validiteDuContrat = false;
            System.out.println(attaquant.getName() + " n'a pas pu valider son contrat et perd donc la manche.");
        }

        difference = Math.abs(difference);

        // Arrondi au nombre au dessus si nécessaire
        difference = Math.ceil(difference);
        int differenceArrondi = (int) difference;

        int mise;
        switch (contrat) {
            case "Petite":
                mise = 1;
                break;
            case "Garde":
                mise = 2;
                break;
            case "Garde sans":
                mise = 4;
                break;
            case "Garde contre":
                mise = 6;
                break;
            default:
                throw new IllegalArgumentException("Contrat inconnu");
        }

        int scoreTotal = (25 + differenceArrondi) * mise;

        for (Player player : playerList) {
            if (player.equals(attaquant)) {
                player.addScore(validiteDuContrat ? scoreTotal * (playerList.size() - 1)
                        : -scoreTotal * (playerList.size() - 1));
            } else {
                player.addScore(validiteDuContrat ? -scoreTotal : scoreTotal);
            }
        }

        for (Player player : playerList) {
            System.out.println(player.getScore());
        }
    }

    public void verifPetitAuBout(int numPlayers, Player attaquant, Player gagnantDernierPli, List<Player> playerList,
            List<Card> deckPliAttaquant, List<Card> deckPliDefenseur) {
        if (gagnantDernierPli.equals(attaquant)) {
            for (int i = deckPliAttaquant.size() - numPlayers; i < deckPliAttaquant.size(); i++) {
                if (deckPliAttaquant.get(i).isPetit()) {
                    attaquant.addScore(10);
                }
            }
        } else {
            for (int i = deckPliDefenseur.size() - numPlayers; i < deckPliDefenseur.size(); i++) {
                if (deckPliDefenseur.get(i).isPetit()) {
                    int pointsPerDefender = 10 / (numPlayers - 1); // Diviser le bonus entre les défenseurs uniquement
                    for (Player joueur : playerList) {
                        if (!joueur.equals(attaquant)) {
                            joueur.addScore(pointsPerDefender);
                        }
                    }
                }
            }
        }
    }

    public void verifExcuseAuBout(int numPlayers, Player attaquant, Player gagnantDernierPli, List<Player> playerList,
            List<Card> deckPliAttaquant, List<Card> deckPliDefenseur) {
        if (gagnantDernierPli.equals(attaquant)) {
            for (int i = deckPliAttaquant.size() - numPlayers; i < deckPliAttaquant.size(); i++) {
                if (deckPliAttaquant.get(i).value() == CardValue.EXCUSE) {
                    Card excuseCard = deckPliAttaquant.remove(i); // Enlève la carte Excuse du deck de l'attaquant
                    deckPliDefenseur.add(excuseCard); // Ajoute la carte Excuse au deck des défenseurs
                }
            }
        } else {
            for (int i = deckPliDefenseur.size() - numPlayers; i < deckPliDefenseur.size(); i++) {
                if (deckPliDefenseur.get(i).value() == CardValue.EXCUSE) {
                    Card excuseCard = deckPliDefenseur.remove(i); // Enlève la carte Excuse du deck de l'attaquant
                    deckPliAttaquant.add(excuseCard); // Ajoute la carte Excuse au deck des défenseurs
                }
            }
        }
    }

    /*
     * Le tarot comporte une particularité quant à la valeur des cartes : elles
     * n’ont pas des valeurs entières.
     * 
     * Un bout/ oudler (le 1 et le 21 d’atout et l’excuse) : 4,5 points
     * Un Roi : 4,5 points
     * Une Dame : 3,5 points
     * Un Cavalier : 2,5 points
     * Un Valet : 1,5 points
     * Toutes les autres cartes : 0,5 points
     * Total 91 points
     * 
     * 
     * Ainsi, pour compter on prend toujours une grande carte avec une petite carte
     * pour faire la somme des deux :
     * 
     * Un bout + une petite carte à la couleur ou un atout : 5 points
     * Un Roi + une petite carte à la couleur ou un atout : 5 points
     * Une Dame + une petite carte à la couleur ou un atout : 4 points
     * Un Cavalier + une petite carte à la couleur ou un atout : 3 points
     * Un Valet + une petite carte à la couleur ou un atout : 2 points
     * Deux des autres cartes ensemble : 1 point
     */

    public double compteurPoints(List<Card> deckPli) {
        double points = 0.0;

        for (Card card : deckPli) {
            if (card.isBout()) {
                points += 4.5;
            } else if (card.isKing()) {
                points += 4.5;
            } else if (card.isQueen()) {
                points += 3.5;
            } else if (card.isCavalier()) {
                points += 2.5;
            } else if (card.isValet()) {
                points += 1.5;
            } else {
                points += 0.5; // Toutes les autres cartes
            }
        }

        return points; // Convertir le résultat en int
    }

    public int countBouts(List<Card> deck) {
        int count = 0;
        for (Card card : deck) {
            if (card.isBout()) {
                count++;
            }
        }
        return count;
    }

    public Player gererPli(List<Player> joueurs, Player attaquant, List<Card> pliAttaquant, List<Card> pliDefenseur) {

        List<Card> pli = new ArrayList<>();
        CardColor couleurDemandee = null;
        Card carteLaPlusForte = null;
        Player gagnantDuPli = new Player(null);
        boolean atoutJoue = false;
        boolean excuseJouee = false;
        Player joueurExcuse = new Player(null); // Joueur ayant joué l'excuse

        for (Player joueur : joueurs) {
            System.out.println(joueur.getLength());
            boolean carteValide;
            Card carteJouee;
            do {
                carteValide = true; // Présumer que le joueur va jouer une carte valide
                // joueur.showHandSorted();
                carteJouee = joueur.choisirUneCarte();

                if (couleurDemandee == null) {

                    if (carteJouee.value() == CardValue.EXCUSE) {
                        couleurDemandee = null;
                        excuseJouee = true;
                        joueurExcuse = joueur;
                    } else {
                        couleurDemandee = carteJouee.color();
                        carteLaPlusForte = carteJouee;
                        gagnantDuPli = joueur;
                    }

                } else {

                    if (carteJouee.value() == CardValue.EXCUSE) {
                        excuseJouee = true;
                        joueurExcuse = joueur;
                    } else {
                        if (joueur.checkColor(couleurDemandee)) {

                            if (carteJouee.color() != couleurDemandee) {
                                System.out.println(joueur.getName() + ", VOUS DEVEZ JOUER UNE CARTE DE COULEUR "
                                        + couleurDemandee + ".");
                                carteValide = false;

                            } else if ((hasHigherAtout(carteLaPlusForte, joueur.getHand())
                                    && (carteJouee.color() == CardColor.ATOUT))
                                    && carteJouee.compareTo(carteLaPlusForte) <= 0) {
                                System.out.println(joueur.getName() + ", VOUS DEVEZ JOUER UN ATOUT SUPÉRIEUR À "
                                        + carteLaPlusForte + ".");
                                carteValide = false;
                            }

                        } else if (!joueur.checkColor(couleurDemandee) && joueur.checkColor(CardColor.ATOUT)) {
                            // Le joueur n'a pas la couleur demandée mais a des atouts
                            if (carteJouee.color() != CardColor.ATOUT) {
                                System.out.println(joueur.getName()
                                        + ", vous n'avez pas la couleur jouée par le premier joueur et vous possédez un atout.");
                                carteValide = false;

                            } else if ((hasHigherAtout(carteLaPlusForte, joueur.getHand())
                                    && (carteJouee.color() == CardColor.ATOUT))
                                    && carteJouee.compareTo(carteLaPlusForte) <= 0) {
                                System.out.println(joueur.getName() + ", VOUS DEVEZ JOUER UN ATOUT SUPÉRIEUR À "
                                        + carteLaPlusForte + ".");
                                carteValide = false;

                            }
                        }
                    }
                }

            } while (!carteValide);

            if (carteJouee.color() == CardColor.ATOUT) {
                atoutJoue = true;
            }

            if (estCartePlusForte(carteJouee, carteLaPlusForte, couleurDemandee, atoutJoue)) {
                carteLaPlusForte = carteJouee;
                gagnantDuPli = joueur;

            }

            joueur.removeCardFromHand(carteJouee);
            pli.add(carteJouee);

        }

        System.out.println(gagnantDuPli.getName() + " a gagné le pli !\nLe pli : "
                + pli.stream().map(Card::toString).collect(Collectors.joining(" ")));

        boolean estAttaquantQuiAJoueExcuse = joueurExcuse.equals(attaquant);

        if (excuseJouee == true) {
            if (estAttaquantQuiAJoueExcuse) {
                // Parcourir chaque carte du pli
                for (Card carte : pli) {
                    if (carte.value() == CardValue.EXCUSE) {
                        // Ajouter l'Excuse au pli de l'attaquant
                        pliAttaquant.add(carte);
                    } else {
                        // Ajouter les autres cartes au pli des défenseurs
                        pliDefenseur.add(carte);
                    }
                }
                System.out.println(
                        "L'attaquant a joué l'excuse mais perd le pli. L'attaquant reprend l'excuse mais la défense prend une carte en échange dans le pli.");
            } else if (!estAttaquantQuiAJoueExcuse && !gagnantDuPli.equals(attaquant)) {
                // Si le joueur ayant joué l'excuse est défenseur et que la défense gagne le pli
                // on ajoute toutes les cartes dans le pli des défenseurs
                pliDefenseur.addAll(pli);
            } else if (!estAttaquantQuiAJoueExcuse && gagnantDuPli.equals(attaquant)) {
                // si l'attaquant gagne le pli où la défense a joué l'Excuse (et ce n'est pas le
                // dernier pli), l'attaquant gagne le pli, mais l'Excuse retourne à la défense.
                // Si c'est le dernier pli, l'attaquant gagne l'Excuse avec le pli.
                for (Card carte : pli) {
                    if (carte.value() == CardValue.EXCUSE) {
                        pliDefenseur.add(carte);
                    } else {
                        pliAttaquant.add(carte);
                    }
                }
                System.out.println(
                        "La défense a joué l'excuse mais perd le pli. La défense reprend l'excuse mais l'attaquant prend une carte en échange dans le pli.");
            }
        } else {
            if (gagnantDuPli.equals(attaquant)) {
                System.out.println("Le pli va dans le deck de pli de l'attaquant.");
                pliAttaquant.addAll(pli);
            } else {
                pliDefenseur.addAll(pli);
                System.out.println("Le pli va dans le deck de pli des défenseurs.");
            }
        }

        if (areAllCardsAtouts(pli) && joueurs.get(0).getLength() >= 2) {
            System.out.println(
                    "Toutes les cartes du pli sont des atouts. Chaque joueur doit donner deux cartes au joueur de droite.");
            donnerDeuxCartesAuJoueurDeDroite(joueurs);
        }

        return gagnantDuPli; // Le gagnant commence le prochain pli
    }

    public void donnerDeuxCartesAuJoueurDeDroite(List<Player> players) {
        // On demande d'abord aux joueurs de selectionner les cartes qu'ils souhaitent
        // donner
        List<List<Card>> cartes = new ArrayList<>();
        for (Player player : players) {
            player.showHandSorted();
            cartes.add(player.choisirDeuxCartes());
        }

        // Distribution des cartes
        for (int i = 0; i < players.size(); i++) {
            // Utilise le modulo pour boucler à l'index 0 après le dernier joueur
            Player receveur = players.get((i + 1) % players.size());
            List<Card> carteARecevoir = cartes.get(i);

            receveur.addAllCardsToHand(carteARecevoir);
            receveur.sortHand();
        }
        System.out.println("Les cartes ont été données aux joueurs.");
    }

    public boolean areAllCardsAtouts(List<Card> pli) {
        for (Card card : pli) {
            if (card.color() != CardColor.ATOUT) {
                return false; // Si une carte n'est pas un atout, retourne false immédiatement
            }
        }
        return true; // Toutes les cartes sont des atouts
    }

    public boolean hasHigherAtout(Card card, List<Card> deck) {
        if (card.color() != CardColor.ATOUT) {
            // Si la carte n'est pas un atout, il n'est pas nécessaire de continuer.
            return false;
        }

        return deck.stream().anyMatch(deckCard -> deckCard.color() == CardColor.ATOUT && deckCard.compareTo(card) > 0);
    }

    private boolean estCartePlusForte(Card carteJouee, Card carteLaPlusForte, CardColor couleurDemandee,
            boolean atoutJoue) {
        // Si la couleur demandée est un atout ou si un atout a été joué après une
        // couleur non-atout
        if (couleurDemandee == CardColor.ATOUT || atoutJoue) {
            // Les atouts sont comparés entre eux
            if (carteJouee.color() == CardColor.ATOUT) {
                return carteJouee.compareTo(carteLaPlusForte) > 0;
            } else {
                return false; // Une carte non-atout ne peut pas battre un atout
            }
        } else if (carteJouee.color() == couleurDemandee) {
            // Comparer les cartes de la couleur demandée entre elles
            return carteJouee.compareTo(carteLaPlusForte) > 0;
        }
        // Si la carte jouée n'est pas de la couleur demandée et aucun atout n'a été
        // joué, elle ne peut pas gagner
        return false;
    }

    public void afficherPli(List<Card> pli, String nomDuPli) {
        System.out.println("Contenu du " + nomDuPli + " :");

        if (pli.isEmpty()) {
            System.out.println("Le pli est vide.");
        } else {
            for (Card carte : pli) {
                System.out.print(carte + " "); // Supposant que la méthode toString() de la classe Card est bien définie
            }
        }
        System.out.println();
    }

    public void distribuerChienSelonMise(Player joueurAvecLaPlusGrosseMise, String miseMax, Player dog,
            List<Card> deckPliAttaquant, List<Card> deckPliDefenseur) {

        // Récupérer les cartes du chien
        int sizeDog = dog.getLength();
        List<Card> cartesDuChien = new ArrayList<>();

        switch (miseMax) {
            case "Petite":
            case "Garde":
                // Dans le cas d'une Petite ou d'une Garde, le joueur avec la plus grosse mise
                // reçoit le chien
                System.out.println("\nVoici le chien : " + dog.getHand());
                cartesDuChien = dog.emptyHand();
                joueurAvecLaPlusGrosseMise.addAllCardsToHand(cartesDuChien);
                System.out
                        .println(joueurAvecLaPlusGrosseMise.getName() + " a reçu les cartes du chien pour sa mise de : "
                                + miseMax + "\n");

                // Demander à l'attaquant de se défausser de cartes équivalant à la taille du
                // chien

                seDefausser(joueurAvecLaPlusGrosseMise, sizeDog, deckPliAttaquant, scanner);
                break;

            case "Garde sans":
                // Dans le cas d'une Garde sans, le chien va directement dans le pli de
                // l'attaquant
                cartesDuChien = dog.emptyHand();
                deckPliAttaquant.addAll(cartesDuChien);
                System.out.println(
                        joueurAvecLaPlusGrosseMise.getName() + " joue sans le chien pour sa mise de Garde sans\n");
                break;

            case "Garde contre":
                // Dans le cas d'une Garde contre, c'est la défense qui obtient le chien
                cartesDuChien = dog.emptyHand();
                deckPliDefenseur.addAll(cartesDuChien);
                System.out
                        .println("Les cartes du chien ont été ajoutées au pli des défenseurs pour une Garde contre.\n");
                break;
        }
    }

    public void seDefausser(Player joueur, int nombreDeCartesADefausser, List<Card> pliJoueur, Scanner scanner) {
        joueur.sortHand();
        joueur.showHandSorted();
        System.out.println(
                joueur.getName() + ", veuillez choisir " + nombreDeCartesADefausser + " carte(s) à défausser.");

        for (int i = 0; i < nombreDeCartesADefausser; i++) {

            Card carteADefausser = joueur.choisirUneCarteADefausser();

            if (carteADefausser.isBout() || carteADefausser.isKing()) {
                System.out.println("Vous ne pouvez pas défausser de Bout (Atout 1, Atout 21, Excuse) ou de Roi ! ! !");
                i--;
            } else {
                joueur.removeCardFromHand(carteADefausser);
                pliJoueur.add(carteADefausser);
            }
        }
    }

    public void reconstitutionDeck(List<Player> playerList, Deck deck, Player dog) {
        for (Player joueur : playerList) {
            List<Card> playerCards = joueur.emptyHand(); // Récupère les cartes du joueur
            deck.addAllCards(playerCards);
        }
        // Ne pas oublier de récupérer les cartes du chien également
        List<Card> dogCards = dog.emptyHand();
        deck.addAllCards(dogCards);
    }

    public boolean verifierEtRedistribuerSiNecessaire(Map<Player, String> mises) {
        // Vérifiez si tous les joueurs ont passé
        boolean tousPasses = true;
        for (Map.Entry<Player, String> entry : mises.entrySet()) {
            String mise = entry.getValue();
            if (mise != null && !mise.equals("Passer")) {
                tousPasses = false;
                break; // Pas besoin de vérifier les autres joueurs si l'un d'eux n'a pas passé
            }
        }

        // Si tous les joueurs ont passé, retournez vrai pour indiquer une redistibution
        // nécessaire
        return tousPasses;
    }

    public Map<Player, String> demanderLesMises(List<Player> players) {
        String[] optionsDeMise = { "Passer", "Petite", "Garde", "Garde sans", "Garde contre" };
        int miseMaxIndex = -1; // -1 signifie qu'aucune mise n'a encore été faite
        Map<Player, String> misesDesJoueurs = new HashMap<>(); // HashMap pour stocker les mises des joueurs

        for (Player joueur : players) {
            String mise = joueur.demanderMise(optionsDeMise, miseMaxIndex);
            int miseIndex = Arrays.asList(optionsDeMise).indexOf(mise);

            // Mise à jour de la plus forte mise si nécessaire
            if (miseIndex > miseMaxIndex) {
                miseMaxIndex = miseIndex;
            }

            misesDesJoueurs.put(joueur, mise); // Stocker la mise dans le HashMap
        }

        return misesDesJoueurs;
    }

    public Player trouverJoueurAvecLaPlusGrosseMise(Map<Player, String> misesDesJoueurs) {
        String[] optionsDeMise = { "Passer", "Petite", "Garde", "Garde sans", "Garde contre" };
        Player joueurAvecLaPlusGrosseMise = null;
        int miseMaxIndex = -1;

        for (Map.Entry<Player, String> entry : misesDesJoueurs.entrySet()) {
            int indexDeLaMise = Arrays.asList(optionsDeMise).indexOf(entry.getValue());

            if (indexDeLaMise > miseMaxIndex) {
                miseMaxIndex = indexDeLaMise;
                joueurAvecLaPlusGrosseMise = entry.getKey();
            }
        }

        return joueurAvecLaPlusGrosseMise;
    }

    public void distributeCards(List<Player> players, int dogSize, Player dog, Player donneur) {
        int compteur_chien = 0;
        /**
         * On commence à l'index NUMÉRO 1 car le donneur est le joueur à l'index 0.
         * Sachant que le premier joueur à être distribué est le joueur NUMÉRO 1,
         * Lorsqu'on va entrer dans la boucle while, les premières cartes distribuées
         * seront pour celles du joueur 1.
         */
        int index_joueur = 1;
        int totalCartesDistribuees = 0;
        int nombreTotalCartes = deck.size();

        while (!deck.isEmpty()) {
            String askDonneur = donneur.choisirActionPourLeChien();

            /**
             * Vérifier si le chien peut être distribué
             * Les 3 premières cartes et les 3 dernières cartes ne peuvent pas être
             * distribuer au chien
             */

            boolean peutDistribuerChien = totalCartesDistribuees >= 3
                    && (nombreTotalCartes - totalCartesDistribuees) > 3;

            if (askDonneur.equalsIgnoreCase("C") && compteur_chien < dogSize && peutDistribuerChien) {
                ajouterAuChien(dog, 1);
                compteur_chien++;
                System.out.println("TAILLE DU CHIEN : " + compteur_chien);
                /*
                 * On distribue directement 3 cartes au prochain joueur après avoir donnée une
                 * carte au chien
                 * car le donneur ne peut pas donner 2 cartes au chien copnsécutivement
                 */
                distribuerAuxJoueurs(players.get(index_joueur), 3);
                totalCartesDistribuees += 4; // Une carte au chien + trois cartes au joueur
            } else if (askDonneur.equalsIgnoreCase("D")) {
                distribuerAuxJoueurs(players.get(index_joueur), 3);
                totalCartesDistribuees += 3; // Trois cartes au joueur
            } else {
                System.out.println("Entrée invalide. Veuillez choisir 'D' ou 'C'.");
                continue; // Pour ignorer l'incrément de l'index_joueur et demander à nouveau
            }

            // Gérer les cas spéciaux pour la distribution des dernières cartes
            if ((deck.size() == 3 && dogSize == 3) || (deck.size() == 6 && dogSize == 9)) {
                int cartesADistribuer = deck.size() == 3 ? 1 : 2;
                for (int i = 0; i < 3; i++) {
                    // Distribuer le nombre approprié de cartes au joueur actuel
                    distribuerAuxJoueurs(players.get((index_joueur + i) % players.size()), cartesADistribuer);
                }
                break; // Sortie de la boucle après la distribution des dernières cartes
            } else if ((deck.size() == 4 && dogSize == 2) || (deck.size() == 8 && dogSize == 10)) {
                int cartesADistribuer = deck.size() == 4 ? 1 : 2;
                for (int i = 0; i < 4; i++) {
                    // Distribuer le nombre approprié de cartes au joueur actuel
                    distribuerAuxJoueurs(players.get((index_joueur + i) % players.size()), cartesADistribuer);
                }
                break; // Sortie de la boucle après la distribution des dernières cartes
            }

            index_joueur = (index_joueur + 1) % players.size();
            System.out.println("Cartes restantes dans le deck: " + deck.size());
        }
    }

    // Méthodes séparées pour la distribution
    public void ajouterAuChien(Player dog, int nombreCartes) {
        // Logique d'ajout au chien
        dog.addCardsToHand(deck.getCards(nombreCartes));
    }

    public void distribuerAuxJoueurs(Player player, int nombreCartes) {
        // Logique de distribution aux joueurs
        player.addCardsToHand(deck.getCards(nombreCartes));
        System.out.println(nombreCartes + " cartes ont été distribué à : " + player.getName());
    }

    public void moveFirstToLast(List<Player> playerList) {
        if (playerList != null && !playerList.isEmpty()) {
            // Retirer le premier élément et le stocker
            Player firstElement = playerList.remove(0);

            // Ajouter cet élément à la fin de la liste
            playerList.add(firstElement);
        }
    }

    public void movePlayerToFirst(Player joueur, List<Player> playerList) {
        if (playerList != null && !playerList.isEmpty() && playerList.contains(joueur)) {
            while (!playerList.get(0).equals(joueur)) {
                moveFirstToLast(playerList);
            }
        }
    }

    protected abstract List<Player> getInitialPlayers();

}
