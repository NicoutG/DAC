package src;

/**
 * La classe abstraite Condition représente une condition dans un automate cellulaire.
 * Elle définit le cadre pour évaluer des conditions basées sur des expressions, des variables,
 * et des états de voisinage dans un automate cellulaire.
 */
public abstract class Condition {
    
    /**
     * Configure la condition avec une expression donnée, la position de la cellule,
     * le nombre de voisins, un tableau de variables, un tableau pour les erreurs et la dimension.
     * 
     * @param exp L'expression définissant la condition.
     * @param position La position de la cellule concernée.
     * @param nbVoisins Le nombre de voisins considérés pour la condition.
     * @param var Tableau de variables utilisées dans l'expression.
     * @param erreur Tableau pour stocker les messages d'erreur.
     * @param dim La dimension de l'espace de l'automate.
     * @return Vrai si la condition est correctement configurée, faux sinon.
     */
    public abstract boolean set (String exp, int position, int nbVoisins, Variable [] var, String [] erreur, int dim);
    
    /**
     * Évalue la condition pour une cellule donnée dans un tableau d'état d'automate.
     * 
     * @param tab Le tableau représentant l'état de l'automate.
     * @param voisins Les valeurs des voisins de la cellule.
     * @param indices Les indices des voisins de la cellule.
     * @return Vrai si la condition est remplie, faux sinon.
     */
    public abstract boolean get (Tableau tab, double [] voisins, int [] indices);
    
    /**
     * Retourne l'expression représentant la condition.
     * 
     * @return Une chaîne de caractères représentant l'expression de la condition.
     */
    public abstract String getExp ();

    /**
     * Récupère l'opérateur utilisé dans l'expression de la condition.
     * 
     * @param exp L'expression de la condition.
     * @return L'indice de l'opérateur dans l'expression, ou -1 si aucun n'est trouvé.
     */
    public abstract int getOp (String exp);
    
    /**
     * Crée et configure une instance de Condition basée sur une expression donnée.
     * La méthode détermine le type de condition (binaire, unaire, comparaison, etc.)
     * et l'initialise avec les paramètres fournis.
     * 
     * @param exp L'expression définissant la condition.
     * @param nbVoisins Le nombre de voisins de la cellule.
     * @param var Tableau de variables utilisées dans l'expression.
     * @param erreur Tableau pour stocker les messages d'erreur.
     * @param dim La dimension de l'espace de l'automate.
     * @return Une instance de Condition configurée ou null en cas d'échec.
     */
    public Condition getCond (String exp, int nbVoisins, Variable [] var, String [] erreur, int dim) {
        Condition cond;
        int n=(new OpLogBin ()).getOp(exp);
        if (n!=-1) {
            cond=new OpLogBin ();
            if (cond.set(exp,n,nbVoisins,var,erreur,dim)) {
                return cond;
            }
            return null;
        }
        n=(new OpLogUni ()).getOp(exp);
        if (n!=-1) {
            cond=new OpLogUni ();
            if (cond.set(exp,n,nbVoisins,var,erreur,dim)) {
                return cond;
            }
            return null;
        }
        n=(new OpCompar ()).getOp(exp);
        if (n!=-1) {
            cond=new OpCompar ();
            if (cond.set(exp,n,nbVoisins,var,erreur,dim)) {
                return cond;
            }
            return null;
        }
        erreur[0]="Aucune condition correspondante pour "+exp;
        return null;
    }
}