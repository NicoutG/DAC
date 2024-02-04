package src.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import src.Immediat;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test pour la classe {@code Immediat}.
 * Teste la fonctionnalité de l'objet {@code Immediat} pour gérer des expressions immédiates,
 * y compris la validation de nombres valides et invalides, ainsi que la récupération de valeurs et d'expressions.
 */
class ImmediatTest {
    /**
     * Instance de {@code Immediat} utilisée pour les tests.
     */
    private Immediat immediat;
    
    /**
     * Tableau utilisé pour capturer les messages d'erreur.
     */
    private String[] erreur;

    /**
     * Configure l'environnement de test avant chaque méthode de test.
     * Initialise une nouvelle instance de {@code Immediat} et un tableau pour les erreurs.
     */
    @BeforeEach
    void setUp() {
        immediat = new Immediat();
        erreur = new String[1];
    }

    /**
     * Teste la configuration d'un nombre valide comme expression immédiate.
     * Vérifie que l'opération réussit et qu'aucun message d'erreur n'est généré.
     */
    @Test
    void testSetWithValidNumber() {
        String expression = "42.0";
        assertTrue(immediat.set(expression, 0, 0, null, erreur, 0), "La configuration avec un nombre valide doit réussir.");
        assertNull(erreur[0], "Il ne devrait pas y avoir d'erreur lors de la configuration avec un nombre valide.");
    }

    /**
     * Teste la configuration d'une chaîne invalide comme expression immédiate.
     * Vérifie que l'opération échoue et qu'un message d'erreur est correctement généré.
     */
    @Test
    void testSetWithInvalidNumber() {
        String expression = "notANumber";
        assertFalse(immediat.set(expression, 0, 0, null, erreur, 0), "La configuration avec une chaîne invalide doit échouer.");
        assertNotNull(erreur[0], "Une erreur doit être signalée lors de la configuration avec une chaîne invalide.");
    }

    /**
     * Teste la récupération de la valeur d'une expression immédiate valide.
     * Vérifie que la valeur récupérée correspond à la valeur configurée initialement.
     */
    @Test
    void testGetValue() {
        String expression = "3.14";
        immediat.set(expression, 0, 0, null, erreur, 0);
        assertEquals(3.14, immediat.get(null, null, null), 0.001, "La valeur récupérée doit correspondre à la valeur configurée.");
    }

    /**
     * Teste la récupération de l'expression textuelle d'une valeur immédiate.
     * Vérifie que l'expression récupérée correspond à la représentation textuelle de la valeur configurée.
     */
    @Test
    void testGetExp() {
        String expression = "2.718";
        immediat.set(expression, 0, 0, null, erreur, 0);
        assertEquals("2.718", immediat.getExp(), "L'expression récupérée doit correspondre à la représentation textuelle de la valeur configurée.");
    }

    /**
     * Teste la détermination de la présence d'un opérateur dans une expression numérique valide.
     * Vérifie que le résultat indique correctement l'absence d'opérateur dans une telle expression.
     */
    @Test
    void testGetOp() {
        assertEquals(0, immediat.getOp("42"), "Un opérateur devrait être trouvé dans une expression numérique valide.");
    }
}
