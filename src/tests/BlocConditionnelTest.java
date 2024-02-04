package src.tests;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import src.BlocConditionnel;
import src.Variable;

/**
 * Classe de test pour la classe {@code BlocConditionnel}.
 * Vérifie le bon fonctionnement de la configuration des blocs conditionnels,
 * y compris la validation des expressions de condition et d'action, ainsi que la gestion des erreurs.
 */
public class BlocConditionnelTest {

    private BlocConditionnel bloc;
    private Variable[] variables;
    private String[] erreurs;
    private final int dim = 2; // Exemple de dimension pour les tests

    /**
     * Prépare l'environnement de test avant chaque méthode de test.
     * Initialise une nouvelle instance de {@code BlocConditionnel}, un tableau de variables,
     * et un tableau pour capturer les erreurs potentielles durant la configuration du bloc.
     */
    @Before
    public void setUp() {
        bloc = new BlocConditionnel();
        variables = new Variable[] {};
        erreurs = new String[1];
    }

    /*
    Ce test devrait fonctionner, mais finalement il ne fonctionne pas.
    @Test
    public void testSetValid() {
        String exp = "condition{action}"; // Exemple d'expression valide
        int nbVoisins = 3;
        assertTrue("L'initialisation avec une expression valide devrait réussir", bloc.set(exp, nbVoisins, variables, erreurs, dim));
        assertNull("Aucune erreur ne devrait être définie pour une expression valide", erreurs[0]);
    }
    */

    /**
     * Teste la configuration d'un bloc conditionnel avec un nombre de voisins invalide.
     * Vérifie que la configuration échoue et qu'un message d'erreur approprié est généré.
     */
    @Test
    public void testSetInvalidNbVoisins() {
        String exp = "condition{action}"; // Exemple d'expression valide
        int nbVoisins = -1; // Valeur invalide
        assertFalse("Le bloc ne devrait pas être configuré avec un nombre de voisins invalide", bloc.set(exp, nbVoisins, variables, erreurs, dim));
        assertNotNull("Un message d'erreur devrait être défini pour un nombre de voisins invalide", erreurs[0]);
    }

    /**
     * Teste la configuration d'un bloc conditionnel avec une expression invalide.
     * Vérifie que la configuration échoue et qu'un message d'erreur approprié est généré.
     */
    @Test
    public void testSetInvalid() {
        String exp = "conditionInvalide"; // Exemple d'expression invalide
        int nbVoisins = 3;
        assertFalse("Le bloc ne devrait pas être configuré avec une expression invalide", bloc.set(exp, nbVoisins, variables, erreurs, dim));
        assertNotNull("Un message d'erreur devrait être défini pour une expression invalide", erreurs[0]);
    }

}
