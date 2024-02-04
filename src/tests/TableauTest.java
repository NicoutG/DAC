package src.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;

import src.Tableau;

/**
 * Classe de test pour la classe {@code Tableau}.
 * Fournit plusieurs tests pour vérifier la création, la manipulation,
 * et les fonctionnalités de sauvegarde et de chargement d'un tableau 2D.
 */
class TableauTest {

    /**
     * Instance de {@code Tableau} utilisée pour les tests.
    * Ce tableau est réinitialisé avant chaque test par la méthode {@code setUp()},
    * permettant ainsi de partir sur une base propre pour chaque cas de test.
    */
    private Tableau tableau;

    /**
     * Initialise un nouveau tableau 2D avant chaque test.
     * Crée un tableau de dimension 2 avec une taille de 5x5.
     */
    @BeforeEach
    void setUp() {
        tableau = new Tableau(2, 5);
    }

    /**
     * Teste la création d'un tableau.
     * Vérifie si la dimension et la taille du tableau correspondent aux valeurs attendues.
     */
    @Test
    void testTableauCreation() {
        assertEquals(2, tableau.getDim(), "La dimension du tableau doit être 2.");
        assertEquals(5, tableau.getTaille(), "La taille de chaque dimension doit être 5.");
    }

    /**
     * Teste la définition et la récupération d'une valeur dans le tableau.
     * Vérifie si la valeur à un indice donné est correctement mise à jour et récupérée.
     */
    @Test
    void testSetAndGetVal() {
        tableau.setVal(new int[]{1, 2}, 3.14);
        assertEquals(3.14, tableau.getVal(1, 2), "La valeur à l'indice (1,2) doit être 3.14.");
    }

    /**
     * Teste la sauvegarde et le chargement du tableau depuis un fichier.
     * Vérifie si les données sauvegardées peuvent être correctement chargées.
     *
     * @throws IOException si une erreur d'entrée/sortie se produit pendant la sauvegarde ou le chargement
     */
    @Test
    void testSauvegarderEtCharger() throws IOException {
        String chemin = "testTableau.txt";
        tableau.setVal(new int[]{1, 1}, 2.72);
        assertTrue(tableau.sauvegarder(chemin), "La sauvegarde doit réussir.");
        
        Tableau tableauCharge = new Tableau(chemin);
        assertEquals(2.72, tableauCharge.getVal(1, 1), "La valeur chargée à l'indice (1,1) doit être 2.72.");
        
        // Nettoyage
        Files.delete(Paths.get(chemin));
    }

    /**
     * Teste le remplissage du tableau avec une valeur spécifique.
     * Vérifie si au moins 5 cellules sont correctement remplies avec la valeur donnée.
     */
    @Test
    void testRemplir() {
        tableau.remplir(5, 1.0f);
        int compteur = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (tableau.getVal(i, j) == 1.0) compteur++;
            }
        }
        assertTrue(compteur >= 5, "Au moins 5 cellules doivent être remplies avec la valeur 1.0.");
    }

    /**
     * Teste les fonctions maximum, minimum et moyenne du tableau.
     * Vérifie si les valeurs maximum, minimum et la moyenne sont correctement calculées.
     */
    @Test
    void testMaximumMinimumMoyenne() {
        tableau.setVal(new int[]{0, 0}, 10);
        tableau.setVal(new int[]{4, 4}, -10);
        assertEquals(10, tableau.maximum(), "Le maximum doit être 10.");
        assertEquals(-10, tableau.minimum(), "Le minimum doit être -10.");
        
        assertEquals(0, tableau.moyenne(), "La moyenne devrait être proche de 0, compte tenu des valeurs définies.");
    }

    /**
     * Teste le comptage des occurrences d'une valeur donnée dans le tableau.
     * Vérifie si le nombre d'occurrences de la valeur spécifiée est correct.
     */
    @Test
    void testCount() {
        tableau.setVal(new int[]{0, 0}, 5);
        tableau.setVal(new int[]{1, 1}, 5);
        assertEquals(2, tableau.count(5), "Le nombre d'occurrences de la valeur 5 doit être 2.");
    }
}
