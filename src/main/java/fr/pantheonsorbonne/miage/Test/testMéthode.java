package fr.pantheonsorbonne.miage.Test;

import java.util.ArrayList;
import java.util.List;

import fr.pantheonsorbonne.miage.Joueurs.Player;

public class testMéthode {

    public static void calculerScoreManche(double nombreDePoints, int nombreDeBouts, String contrat, List<Player> playerList,
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

        if (difference >= 0) {
            validiteDuContrat = true;
            System.out.println(attaquant.getName() + " a validé son contrat et gagne donc la manche.");
        } else {
            validiteDuContrat = false;
            System.out.println(attaquant.getName() + " n'a pas pu valider son contrat et perd donc la manche.");
        }

        difference = Math.abs(difference);

        //Arrondi au nombre au dessus si nécessaire
        difference = Math.ceil(difference);
        int differenceArrondi = (int)difference;

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
                player.addScore(validiteDuContrat ? scoreTotal*(playerList.size()-1) : -scoreTotal*(playerList.size()-1));
            } else {
                player.addScore(validiteDuContrat ? -scoreTotal : scoreTotal);
            }
        }

        for (Player player : playerList) {
            System.out.println(player.getScore());
        }
    }

    public static void main(String... args) {
        // Création des joueurs
        Player attaquant = new Player("Joueur 1");
        Player defenseur1 = new Player("Joueur 2");
        Player defenseur2 = new Player("Joueur 3");
        Player defenseur3 = new Player("Joueur 4");
        // Ajout des joueurs dans une liste
        List<Player> playerList = new ArrayList<>();
        playerList.add(attaquant);
        playerList.add(defenseur1);
        playerList.add(defenseur2);
        playerList.add(defenseur3);

        // Points, bouts, et contrat pour l'exemple
        double nombreDePoints = 42; // Points réalisés par l'attaquant
        int nombreDeBouts = 2; // Nombre de bouts détenus par l'attaquant
        String contrat = "Petite"; // Type de contrat

        // Création d'une instance de TarotGame et lancement de la méthode
        // calculerScoreManche

        calculerScoreManche(nombreDePoints, nombreDeBouts, contrat, playerList, attaquant);
    }

    // Reste de votre classe TarotGame, y compris la méthode calculerScoreManche...
}
