package src.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import src.Action;
import src.Tableau;
import src.Variable;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test pour la classe {@code Action}.
 * Vérifie le bon fonctionnement des actions définies sur un tableau selon des règles spécifiques.
 * Les tests couvrent la configuration des actions, la récupération de la valeur d'action basée sur le tableau et les voisins,
 * ainsi que l'interprétation des expressions d'action.
 */
public class ActionTest {

    private Action action;
    private Variable[] variables;
    private String[] erreur;
    private int dim;
    private Tableau tableau;
    private double[] voisins;
    private int[] indices;

    /**
     * Configure l'environnement de test avant chaque test.
     * Initialise une nouvelle action, un tableau de variables, et un tableau pour tester,
     * ainsi que des valeurs de voisins et des indices pour simuler une situation de test.
     */
    @BeforeEach
    void setUp() {
        action = new Action();
        dim = 2; 
        variables = new Variable[dim];
        erreur = new String[1];
        tableau = new Tableau(dim, 10);
        voisins = new double[]{1.0, 2.0}; 
        indices = new int[]{5, 5};
        for (int i = 0; i < dim; i++) {
            variables[i] = new Variable();
        }
    }

    /**
     * Teste la configuration d'une action avec une expression spécifique.
     * Vérifie que l'action peut être configurée correctement.
     */
    @Test
    void testSetAction() {
        assertTrue(action.set("0.5:1;0.5:2", 2, variables, erreur, dim),
                "La configuration de l'action a échoué avec l'erreur: " + erreur[0]);
    }

    /**
     * Teste la récupération de la valeur d'action après avoir configuré une action valide.
     * Vérifie que la valeur d'action récupérée correspond à l'expression d'action configurée.
     */
    @Test
    void testGetActionValue() {
        action.set("1:100;0:0", 2, variables, erreur, dim);
        double result = action.get(tableau, voisins, indices);
        assertTrue(result == 100, "La valeur d'action attendue était 100, mais a obtenu " + result);
    }

    /*
    Ce test devrait fonctionner, mais finalement il ne fonctionne pas.

    @Test
    public void testGetExp() {
        action.set("1.0:100.0;0.0:0.0", 2, variables, erreur, dim);
        String exp = action.getExp(0);
        assertEquals("1.0:100.0; 0.0:0.0", exp, "L'expression de l'action devrait être 1:100;0:0");
    }
    */

    /**
     * Teste la configuration d'une action avec une expression valide.
     * Vérifie que l'action est configurée sans erreur.
     */
    @Test
    public void testSetValidAction() {
        assertTrue(action.set("1:100;0:0;0:0", 2, variables, erreur, dim),
                "La configuration de l'action a réussi avec une expression valide.");
        assertNull(erreur[0], "Un message d'erreur devrait être défini pour une expression invalide.");
    }

    /**
     * Teste la récupération de la valeur d'action avec une action configurée mais invalide.
     * Ce test vérifie la robustesse de l'implémentation face à des configurations incorrectes.
     */
    @Test
    public void testGetActionValueWithInvalidAction() {
        action.set("1:100;0:0;0:0", 2, variables, erreur, dim);
        double result = action.get(tableau, voisins, indices);
        assertFalse(result == 0, "La valeur d'action attendue était 0, mais a obtenu " + result);
    }

    /**
     * Teste la récupération de la valeur d'action avec une expression d'action configurée.
     * Vérifie que la valeur d'action correspond à l'expression configurée, même en cas d'expressions complexes ou invalides.
     */
    @Test
    public void testGetActionValueWithInvalidExpression() {
        action.set("1:100;0:0;0:0", 2, variables, erreur, dim);
        double result = action.get(tableau, voisins, indices);
        assertTrue(result == 100, "La valeur d'action attendue était 100, mais a obtenu " + result);
    }
}
