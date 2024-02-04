package src.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import src.OpAriBin;
import src.Variable;
import src.Tableau;

/**
 * Classe de test pour la classe {@code OpAriBin}.
 * Cette classe effectue des tests sur les opérations arithmétiques binaires,
 * y compris l'addition, la soustraction, la multiplication, et la division.
 * Elle vise à vérifier la correcte configuration et évaluation des expressions arithmétiques.
 */
class OpAriBinTest {

    /**
     * Instance de {@code OpAriBin} utilisée pour tester les opérations arithmétiques binaires.
     */
    private OpAriBin opAriBin;

    /**
     * Tableau de {@code Variable} pour simuler les variables dans les expressions, si nécessaire.
     */
    private Variable[] variables;

    /**
     * Tableau pour capturer les messages d'erreur lors de la configuration des opérations.
     */
    private String[] erreur;

    /**
     * Instance de {@code Tableau} pour fournir un contexte aux évaluations des opérations, si nécessaire.
     */
    private Tableau tableau;

    /**
     * Initialise les composants nécessaires avant chaque test.
     * Crée une nouvelle instance de {@code OpAriBin}, un tableau pour les messages d'erreur,
     * un tableau simplifié pour les tests, et initialise les variables si nécessaire.
     */
    @BeforeEach
    void setUp() {
        opAriBin = new OpAriBin();
        erreur = new String[1];
        tableau = new Tableau(1, 10); 
        variables = new Variable[2];
    }

    /**
     * Teste la configuration d'une opération d'addition valide.
     * Vérifie que l'opération est correctement configurée sans erreurs.
     */
    @Test
    void testSetWithValidAddition() {
        assertTrue(opAriBin.set("3+2", 1, 0, variables, erreur, 1),
                   "L'opération d'addition devrait être configurée correctement.");
    }

    /**
     * Évalue le résultat d'une opération d'addition.
     * Vérifie que l'évaluation de l'expression donne le résultat attendu.
     */
    @Test
    void testEvaluateAddition() {
        opAriBin.set("3+2", 1, 0, variables, erreur, 1);
        double result = opAriBin.get(tableau, new double[]{}, new int[]{});
        assertEquals(5.0, result, "Le résultat de 3+2 devrait être 5.");
    }

    /**
     * Teste la configuration d'une opération de soustraction valide.
     * Vérifie que l'opération est correctement configurée sans erreurs.
     */
    @Test
    void testSetWithValidSubtraction() {
        assertTrue(opAriBin.set("5-2", 1, 0, variables, erreur, 1),
                   "L'opération de soustraction devrait être configurée correctement.");
    }

    /**
     * Évalue le résultat d'une opération de soustraction.
     * Vérifie que l'évaluation de l'expression donne le résultat attendu.
     */
    @Test
    void testEvaluateSubtraction() {
        opAriBin.set("5-2", 1, 0, variables, erreur, 1);
        double result = opAriBin.get(tableau, new double[]{}, new int[]{});
        assertEquals(3.0, result, "Le résultat de 5-2 devrait être 3.");
    }

    /**
     * Récupère l'expression formatée pour une multiplication.
     * Vérifie que l'expression est correctement formatée avec des parenthèses.
     */
    @Test
    void testGetExpForMultiplication() {
        opAriBin.set("4*3", 1, 0, variables, erreur, 1);
        String exp = opAriBin.getExp();
        assertEquals("(4.0*3.0)", exp, "L'expression retournée devrait être correctement formatée pour une multiplication.");
    }

    /**
     * Détermine la position de l'opérateur dans une division.
     * Vérifie que la position de l'opérateur est correctement identifiée dans l'expression.
     */
    @Test
    void testGetOpForDivision() {
        int opPosition = opAriBin.getOp("6/3");
        assertTrue(opPosition > 0, "L'opérateur '/' devrait être trouvé dans l'expression.");
    }
}
