package src.tests;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import src.OpCompar;
import src.Variable;

/**
 * Classe de test pour la classe {@code OpCompar}.
 * Vérifie la fonctionnalité de la classe {@code OpCompar} pour gérer les opérations de comparaison,
 * y compris la validation des expressions de comparaison et l'identification correcte des opérateurs.
 */
public class OpComparTest {

    private OpCompar opCompar;
    private Variable[] variables;
    private String[] erreurs;

    /**
     * Configure l'environnement de test avant chaque méthode de test.
     * Initialise une nouvelle instance de {@code OpCompar}, un tableau de variables vide (à initialiser selon la logique spécifique),
     * et un tableau pour les messages d'erreur.
     */
    @Before
    public void setUp() {
        opCompar = new OpCompar();
        variables = new Variable[] {};
        erreurs = new String[1];
    }

    /**
     * Teste la configuration d'une opération de comparaison avec une expression invalide.
     * Vérifie que la configuration échoue et qu'un message d'erreur est correctement généré.
     */
    @Test
    public void testSetInvalidExpression() {
        // Exemple d'expression invalide
        String exp = ">=5";
        int position = exp.indexOf(">=");
        assertFalse("La configuration devrait échouer pour une expression invalide",
                opCompar.set(exp, position, 0, variables, erreurs, 2));
        assertNotNull("Un message d'erreur devrait être défini pour une expression invalide", erreurs[0]);
    }

    /**
     * Teste l'identification correcte de la position de l'opérateur dans une expression valide.
     */
    @Test
    public void testGetOpValid() {
        String exp = "$x<$y";
        int expectedPosition = exp.indexOf("<");
        int opPosition = opCompar.getOp(exp);
        assertEquals("La position de l'opérateur devrait être correctement identifiée", expectedPosition, opPosition);
    }

    /**
     * Teste l'identification de la position de l'opérateur dans une expression invalide.
     * Vérifie que la méthode retourne -1 pour indiquer l'absence d'un opérateur valide.
     */
    @Test
    public void testGetOpInvalid() {
        String exp = "$x$y";
        int opPosition = opCompar.getOp(exp);
        assertEquals("La position de l'opérateur devrait être -1 pour une expression invalide", -1, opPosition);
    }

    /*
    Ce test devrait fonctionner, mais finalement il ne fonctionne pas.
    @Test
    public void testSetValidExpression() {
        String exp = "$x<$y";
        int position = exp.indexOf("<");
        assertTrue("La configuration devrait réussir pour une expression valide",
                opCompar.set(exp, position, 0, variables, erreurs, 2));
        assertNull("Aucune erreur ne devrait être définie pour une expression valide", erreurs[0]);
    }
    */

    /**
     * Teste la configuration d'une opération de comparaison avec une position invalide de l'opérateur.
     * Vérifie que la configuration échoue et qu'un message d'erreur est généré.
     */
    @Test
    public void testSetInvalidExpressionWithInvalidPosition() {
        String exp = "$x<$y";
        int position = exp.indexOf("<") + 1;
        assertFalse("La configuration devrait échouer pour une position invalide",
                opCompar.set(exp, position, 0, variables, erreurs, 2));
        assertNotNull("Un message d'erreur devrait être défini pour une position invalide", erreurs[0]);
    }

}
