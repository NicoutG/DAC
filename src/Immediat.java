package src;

/**
 * La classe Immediat étend la classe Valeur et représente une valeur immédiate
 * (c'est-à-dire une valeur constante) dans le contexte d'un automate cellulaire.
 */
public class Immediat extends Valeur {

    /**
     * La valeur immédiate.
     */
    private double val=0;

    /**
     * Configure la valeur immédiate à partir d'une expression, position, nombre de voisins,
     * tableau de variables, tableau d'erreurs et dimension.
     * 
     * @param exp L'expression représentant la valeur immédiate.
     * @param position La position de la valeur dans l'automate (non utilisée dans ce contexte).
     * @param nbVoisins Le nombre de voisins (non utilisé dans ce contexte).
     * @param var Tableau de variables (non utilisé dans ce contexte).
     * @param erreur Tableau pour stocker les messages d'erreur.
     * @param dim La dimension de l'automate (non utilisée dans ce contexte).
     * @return Vrai si la valeur est correctement configurée, faux sinon.
     */
    public boolean set (String exp, int position, int nbVoisins, Variable [] var, String [] erreur, int dim) {
        double [] nouv=new double [1];
        if (!getDouble(exp,nouv)) {
            erreur[0]="Impossible de convertir "+exp+" en reel";
            return false;
        }
        val=nouv[0];
        return true;
    }
    
    /**
     * Renvoie la valeur immédiate.
     * 
     * @param tab Le tableau représentant l'état de l'automate (non utilisé dans ce contexte).
     * @param voisins Les valeurs des voisins de la cellule (non utilisé dans ce contexte).
     * @param indices Les indices des voisins de la cellule (non utilisé dans ce contexte).
     * @return La valeur immédiate.
     */
    public double get (Tableau tab, double [] voisins, int [] indices) {
        return val;
    }
    
    /**
     * Renvoie l'expression représentant la valeur immédiate.
     * 
     * @return Une chaîne de caractères représentant la valeur immédiate.
     */
    public String getExp () {
        return Double.toString(val);
    }

    /**
     * Renvoie l'indice de l'opérateur dans l'expression de la valeur immédiate.
     * 
     * @param exp L'expression de la valeur immédiate.
     * @return L'indice de l'opérateur dans l'expression, ou -1 si aucun n'est trouvé.
     */
    public int getOp (String exp) {
        if (exp.length()==0) {
            return -1;
        }
        try {
            Double.parseDouble(exp);
            return 0;
        }
        catch (Exception e) {
            return -1;
        }
    }
}