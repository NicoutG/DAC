package src;

public class Main {

    // Un automate que l'on charge depuis des fichiers
    public static void automateChargement () {
        Tableau tab=new Tableau ("data/tableaux/tab1.txt"); // Initialisation d'un tableau à partir d'un fichier txt
        Regles reg=new Regles("data/dac/jeu_vie.dac"); // Initialisation d'une règle à partir d'un fichier dac
        for (int i=0;i<10;i++) {
            System.out.println("Etape "+i+" :");
            tab.afficher(true); // Affichage du tableau sous forme d'entiers
            System.out.println();
            tab=reg.appliquer(tab); // On applique la règle sur le tableau pour générer le tableau suivant
        }
    }

    // Un automate que l'on défini en entier
    public static void automateDefinition () {
        Tableau tab=new Tableau (2,10); // Initialisation d'un tableau de dimension 2 et de taille 10
        tab.setVal(0,0,1.8); // Modification des valeurs du tableau
        tab.setVal(1,4,5.7);
        tab.setVal(9,2,3.1);
        tab.setVal(3,8,-4.3);
        tab.setVal(5,5,-16);

        Regles reg=new Regles("dac/jeu_vie.dac"); // Initialisation d'une règle
        reg.set("1,0; 1,-1; 0,-1; -1,-1; -1,0; -1,1; 0,1; 1,1; @ 1.0==1.0? 1.0:(maximum-minimum);"); // Définition de la règle à partir d'un code DAC

        for (int i=0;i<10;i++) {
            System.out.println("Etape "+i+" :");
            tab.afficher(false); // Affichage du tableau sous forme de réels (Affiche toutes les décimales)
            System.out.println();
            tab=reg.appliquer(tab); // On applique la règle sur le tableau pour générer le tableau suivant
        }
    }

    public static void main (String[] args) {
        automateChargement();
        //automateDefinition();
    }
}
