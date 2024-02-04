package src.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import src.Fonction;
import src.Variable;

/**
 * Classe de test pour la classe {@code Fonction}.
 * Teste les fonctionnalités de base de la classe {@code Fonction}, y compris la validation
 * des expressions de fonction et l'identification des opérateurs de fonction.
 */
class FonctionTest {

    private Fonction fonction;
    private String[] erreur;
    private Variable[] var;

    /**
     * Prépare l'environnement de test avant chaque test.
     * Initialise une nouvelle instance de {@code Fonction}, un tableau pour les messages d'erreur,
     * et un tableau de variables (vide pour simplification).
     */
    @BeforeEach
    void setUp() {
        fonction = new Fonction();
        erreur = new String[1];
        var = new Variable[0]; // Assumant qu'il n'y a pas de variable pour simplifier
    }

    /**
     * Teste la configuration d'une fonction sin avec une expression invalide.
     * Vérifie que la configuration échoue et qu'un message d'erreur approprié est généré.
     */
    @Test
    void testSetSinFunctionWithInvalidExpression() {
        assertFalse(fonction.set("sin()", 3, 0, var, erreur, 1),
                    "La configuration de la fonction sin avec une expression invalide doit échouer.");
        assertNotNull(erreur[0], "Un message d'erreur doit être fourni pour une expression invalide.");
    }

    /**
     * Teste l'identification de l'opérateur dans une expression de fonction.
     * Vérifie que la position de l'opérateur de fonction est correctement identifiée.
     */
    @Test
    void testGetOpForFunction() {
        int opPosition = fonction.getOp("cos(45)");
        assertTrue(opPosition >= 0, "L'opérateur de la fonction doit être identifié correctement dans l'expression.");
    }
}
