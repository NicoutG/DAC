package src.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import src.Variable;

/**
 * Classe de test pour la classe {@code Variable}.
 * Effectue des tests pour vérifier le bon fonctionnement des méthodes de
 * manipulation et d'évaluation des variables, y compris la gestion des expressions valides
 * et invalides, ainsi que la mise à jour des valeurs des variables.
 */
class VariableTest {

    /**
     * Instance de {@code Variable} utilisée pour tester.
     */
    private Variable variable;
    
    /**
     * Tableau pour capturer les messages d'erreur lors de la définition des variables.
     */
    private String[] erreur;

    /**
     * Initialise les composants nécessaires avant chaque test.
     * Crée une nouvelle instance de {@code Variable} et un tableau pour les messages d'erreur.
     */
    @BeforeEach
    void setUp() {
        variable = new Variable();
        erreur = new String[1];
    }

    /**
     * Teste la définition d'une variable avec une expression valide.
     * Vérifie que la variable est correctement configurée et que son nom est correctement établi.
     */
    @Test
    void testSetWithValidExpression() {
        assertTrue(variable.set("$x", 1, 0, null, erreur, 1),
                   "La variable devrait être configurée correctement avec une expression valide.");
        assertEquals("x", variable.getNom(), "Le nom de la variable devrait être 'x'.");
    }

    /**
     * Teste la tentative de définition d'une variable avec une expression invalide.
     * Vérifie que la configuration échoue et qu'un message d'erreur approprié est retourné.
     */
    @Test
    void testSetWithInvalidExpression() {
        assertFalse(variable.set("x", 1, 0, null, erreur, 1),
                    "La configuration devrait échouer avec une expression invalide.");
        assertNotNull(erreur[0], "Un message d'erreur devrait être défini pour une expression invalide.");
    }

    /**
     * Teste la récupération de la valeur d'une variable après sa définition.
     * Vérifie que la valeur retournée correspond à la valeur définie.
     */
    @Test
    void testGetAfterSettingValue() {
        variable.set("$x", 1, 0, null, erreur, 1);
        variable.setVal(5.0);
        assertEquals(5.0, variable.get(null, null, null),
                     "La méthode get devrait retourner la valeur correcte de la variable.");
    }

    /**
     * Teste la récupération de l'expression textuelle d'une variable.
     * Vérifie que l'expression retournée correspond à celle définie initialement.
     */
    @Test
    void testGetExp() {
        variable.set("$x", 1, 0, null, erreur, 1);
        String expectedExp = "$x";
        assertEquals(expectedExp, variable.getExp(),
                     "La méthode getExp devrait retourner la représentation textuelle correcte de la variable.");
    }

    /**
     * Teste la mise à jour de la valeur d'une variable.
     * Vérifie que la valeur de la variable est correctement mise à jour.
     */
    @Test
    void testSetValUpdatesValue() {
        variable.set("$x", 1, 0, null, erreur, 1);
        variable.setVal(10.0);
        variable.setVal(20.0); // Mise à jour de la valeur
        assertEquals(20.0, variable.get(null, null, null),
                     "La méthode setVal devrait mettre à jour la valeur de la variable.");
    }
}
