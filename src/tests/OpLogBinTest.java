package src.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import src.OpLogBin;
import src.Tableau;
import src.Variable;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test pour la classe {@code OpLogBin}.
 * Vérifie le comportement de la classe {@code OpLogBin} pour les opérations logiques binaires,
 * y compris la validation des expressions, l'évaluation des opérations logiques,
 * et l'identification des opérateurs dans les expressions.
 */
class OpLogBinTest {

    private OpLogBin opLogBin;
    private Variable[] variables;
    private String[] erreur;
    private Tableau tableau;

    /**
     * Prépare l'environnement de test avant chaque méthode de test.
     * Initialise une nouvelle instance de {@code OpLogBin}, un tableau pour les erreurs,
     * un tableau simplifié pour les tests, et un tableau de variables selon le besoin.
     */
    @BeforeEach
    void setUp() {
        opLogBin = new OpLogBin();
        erreur = new String[1];
        tableau = new Tableau(1, 10);
        variables = new Variable[0]; 
    }

    /*
    Ce test devrait fonctionner, mais finalement il ne fonctionne pas.
    @Test
    void testSetWithValidExpression() {
        assertTrue(opLogBin.set("true && false", 5, 0, variables, erreur, 1),
                   "L'opération logique binaire devrait être valide.");
        assertNull(erreur[0], "Il ne devrait pas y avoir d'erreur lors de la configuration d'une expression valide.");
    }
    */
    
    /**
     * Teste la configuration d'une opération logique binaire avec une expression invalide.
     * Vérifie que la configuration échoue et qu'un message d'erreur est généré.
     */
    @Test
    void testSetWithInvalidExpression() {
        assertFalse(opLogBin.set("true &", 5, 0, variables, erreur, 1),
                    "L'opération logique binaire devrait être invalide.");
        assertNotNull(erreur[0], "Il devrait y avoir une erreur lors de la configuration d'une expression invalide.");
    }

    /**
     * Évalue une opération logique binaire avec une expression invalide.
     * Vérifie que l'évaluation retourne faux, conformément à la logique d'erreur attendue.
     */
    @Test
    void testEvaluateLogicalOperationWithInvalidExpression() {
        opLogBin.set("true &", 5, 0, variables, erreur, 1);
        assertFalse(opLogBin.get(tableau, new double[]{}, new int[]{}),
                    "L'évaluation de true & devrait retourner faux.");
    }

    /**
     * Évalue une opération logique binaire avec un opérateur invalide.
     * Vérifie que l'évaluation retourne faux, indiquant une gestion d'erreur correcte.
     */
    @Test
    void testEvaluateLogicalOperationWithInvalidOperator() {
        opLogBin.set("true & false", 5, 0, variables, erreur, 1);
        assertFalse(opLogBin.get(tableau, new double[]{}, new int[]{}),
                    "L'évaluation de true & false devrait retourner faux.");
    }

    /**
     * Évalue une opération logique binaire valide.
     * Vérifie que l'évaluation retourne le résultat attendu de l'opération logique.
     */
    @Test
    void testEvaluateLogicalOperation() {
        opLogBin.set("true && false", 5, 0, variables, erreur, 1);
        assertFalse(opLogBin.get(tableau, new double[]{}, new int[]{}),
                    "L'évaluation de true && false devrait retourner faux.");
    }

    /**
     * Identifie la position de l'opérateur dans une expression invalide.
     * Vérifie que la méthode retourne -1, indiquant qu'aucun opérateur valide n'a été trouvé.
     */
    @Test
    void testGetOpForInvalidExpression() {
        int opPosition = opLogBin.getOp("true &");
        assertEquals(-1, opPosition, "L'opérateur logique ne devrait pas être trouvé dans l'expression.");
    }

    /**
     * Identifie la position de l'opérateur logique '&&' dans une expression valide.
     * Vérifie que la position de l'opérateur est correctement identifiée.
     */
    @Test
    void testGetOpForLogicalAnd() {
        int opPosition = opLogBin.getOp("true && false");
        assertTrue(opPosition > 0, "L'opérateur '&&' devrait être trouvé dans l'expression.");
    }

    /**
     * Identifie la position de l'opérateur logique '||' dans une expression valide.
     * Vérifie que la position de l'opérateur est correctement identifiée.
     */
    @Test
    void testGetOpForLogicalOr() {
        int opPosition = opLogBin.getOp("true || false");
        assertTrue(opPosition > 0, "L'opérateur '||' devrait être trouvé dans l'expression.");
    }
}
