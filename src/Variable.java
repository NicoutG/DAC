package src;

/**
 * La classe Variable représente une variable dans un automate cellulaire.
 * Elle hérite de la classe Valeur et permet de stocker et de manipuler une valeur numérique.
 */
public class Variable extends Valeur {

    /**
     * Le nom de la variable.
     */
    private String nom=" ";

    /**
     * La valeur numérique stockée dans la variable.
     */
    private double val=0;
    
    /**
     * Configure la variable avec une expression donnée.
     *
     * @param exp L'expression représentant la variable.
     * @param position La position du caractère dans l'expression.
     * @param nbVoisins Le nombre de voisins (non utilisé pour les variables).
     * @param var Un tableau de variables (non utilisé pour cette méthode).
     * @param erreur Un tableau pour stocker les messages d'erreur.
     * @param dim La dimension de l'automate (non utilisée pour les variables).
     * @return Vrai si la variable est configurée avec succès, faux sinon.
     */
    public boolean set (String exp, int position, int nbVoisins, Variable [] var, String [] erreur, int dim) {
        if (exp.length()<=1 || exp.charAt(0)!='$' || position>=exp.length()) {
            erreur[0]="Impossible de convertir "+exp+" en variable";
            return false;
        }
        nom=exp.substring(1,position+1);
        val=0;
        return true;
    }
    
    /**
     * Obtient la valeur actuelle de la variable.
     *
     * @param tab Le tableau représentant l'état de l'automate (non utilisé pour les variables).
     * @param voisins Les valeurs des voisins (non utilisé pour les variables).
     * @param indices Les indices (non utilisé pour les variables).
     * @return La valeur de la variable.
     */
    public double get (Tableau tab, double [] voisins, int [] indices) {
        return val;
    }
    
    /**
     * Retourne la représentation sous forme de chaîne de la variable.
     *
     * @return La représentation textuelle de la variable.
     */
    public String getExp () {
        return "$"+nom;
    }

    /**
     * Identifie le nom de la variable dans l'expression.
     *
     * @param exp L'expression à analyser.
     * @return L'indice de fin du nom de la variable dans l'expression, -1 si aucun nom de variable n'est trouvé.
     */
    public int getOp (String exp) {
        if (exp.length()>1 && exp.charAt(0)=='$'){
            int res=1;
            while(res<exp.length() && (('a'<=exp.charAt(res) && exp.charAt(res)<='z') || ('A'<=exp.charAt(res) && exp.charAt(res)<='Z') || ('0'<=exp.charAt(res) && exp.charAt(res)<='9') || exp.charAt(res)=='_')) {
                res++;
            }
            if (res>1) {
                return res-1;
            }
        }
        return -1;
    }

    /**
     * Obtient le nom de la variable.
     *
     * @return Le nom de la variable.
     */
    public String getNom () {
        return nom;
    }

    /**
     * Définit la valeur de la variable.
     *
     * @param v La nouvelle valeur de la variable.
     */
    public void setVal (double v) {
        val=v;
    }
}