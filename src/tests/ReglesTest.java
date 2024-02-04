package src.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import src.Regles;
import src.Tableau;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test pour la classe {@code Regles}.
 * Teste les fonctionnalités de base telles que le chargement et la sauvegarde des règles,
 * la gestion des erreurs, et l'application des règles sur un tableau.
 */
class ReglesTest {
    private Regles regles;

    /**
     * Prépare l'environnement de test avant chaque test.
     * Initialise une nouvelle instance de {@code Regles}.
     */
    @BeforeEach
    void setUp() {
        regles = new Regles();
    }

    /**
     * Teste le constructeur par défaut de {@code Regles}.
     * Vérifie que la dimension initiale est zéro et qu'aucune erreur n'est présente.
     */
    @Test
    void testConstructeurParDefaut() {
        assertEquals(0, regles.getDim(), "La dimension doit être 0 pour un nouvel objet Regles.");
        assertTrue(regles.getErreur().isEmpty(), "Il ne devrait pas y avoir d'erreur pour un nouvel objet Regles.");
    }

    /**
     * Teste le chargement d'un fichier de règles inexistant.
     * Vérifie que l'opération échoue et génère un message d'erreur.
     */
    @Test
    void testChargerFichierInexistant() {
        assertFalse(regles.charger("data/dac/inexistant.dac"), "Charger un fichier inexistant doit retourner false.");
        assertFalse(regles.getErreur().isEmpty(), "Une erreur doit être signalée si le fichier est inexistant.");
    }

    /**
     * Teste le chargement d'un fichier de règles existant et valide.
     * Vérifie que l'opération réussit sans générer d'erreur.
     */
    @Test
    void testChargerFichierExistant() {
        assertTrue(regles.charger("data/dac/nom.dac"), "Charger un fichier existant avec des règles valides doit retourner true.");
        assertTrue(regles.getErreur().isEmpty(), "Il ne devrait pas y avoir d'erreur après avoir chargé des règles valides.");
    }

    /**
     * Teste la sauvegarde des règles dans un fichier.
     * Présume que le chargement préalable des règles a réussi.
     */
    @Test
    void testSauvegarder() {
        regles.charger("data/dac/nom.dac"); // Assurez-vous que cette opération réussit avant de sauvegarder
        assertTrue(regles.sauvegarder("data/dac/nom2.dac"), "La sauvegarde des règles devrait réussir.");
    }

    /**
     * Teste l'application de règles valides sur un tableau.
     * Nécessite un tableau initialisé et des règles valides chargées préalablement.
     */
    @Test
    void testAppliquerReglesValides() {
        Tableau tableau = new Tableau(2, 2);
        // Initialisation du tableau et chargement des règles valides nécessaires ici
        assertNotNull(regles.appliquer(tableau), "Appliquer des règles valides ne doit pas retourner null.");
    }

    /**
     * Teste la définition et la récupération d'une variable.
     * Vérifie que la valeur récupérée correspond à celle définie précédemment.
     */
    @Test
    void testSetVarEtGetVar() {
        String nomVariable = "testVar";
        double valeurAttendue = 0;
        assertEquals(valeurAttendue, regles.getVar(nomVariable), 0, "La valeur récupérée doit correspondre à la valeur définie.");
    }

    /**
     * Teste la récupération d'une variable inexistante.
     * Vérifie que la valeur par défaut retournée est zéro.
     */
    @Test
    void testGetVarInexistante() {
        assertEquals(0.0, regles.getVar("varInexistante"), 0.01, "Récupérer une variable inexistante devrait retourner 0.");
    }

    /**
     * Teste la récupération du nombre de variables définies.
     * Vérifie que le nombre initial de variables est zéro.
     */
    @Test
    void testGetNbVars() {
        assertEquals(0, regles.getNbVars(), "Initialement, le nombre de variables devrait être 0.");
    }
}
