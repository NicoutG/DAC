package src;

/**
 * La classe OpCompar étend la classe Condition et représente une condition de comparaison
 * (comme inférieur, supérieur, égal, etc.) dans le contexte d'un automate cellulaire.
 */
public class OpCompar extends Condition{

    /**
     * val1 et val2 sont les deux valeurs comparées.
     */
    private Valeur val1=null;
    private Valeur val2=null;

    /**
     * op est l'opérateur utilisé pour la comparaison.
     */
    private String op="";

    /**
     * opList est la liste des opérations de comparaison possibles.
     */
    private String [] opList={"<=",">=","==","!=","<",">"};
    
    /**
     * Configure la condition de comparaison à partir d'une expression, position, nombre de voisins,
     * tableau de variables, tableau d'erreurs et dimension.
     * 
     * @param exp L'expression représentant la condition de comparaison.
     * @param position La position de l'opérateur dans l'expression.
     * @param nbVoisins Le nombre de voisins (non utilisé dans certains contextes).
     * @param var Tableau de variables utilisées dans l'expression.
     * @param erreur Tableau pour stocker les messages d'erreur.
     * @param dim La dimension de l'automate.
     * @return Vrai si la condition est correctement configurée, faux sinon.
     */
    public boolean set (String exp, int position, int nbVoisins, Variable [] var, String [] erreur, int dim) {
        if (position==0) {
            erreur[0]="Valeur attendue à gauche de l'operateur "+exp.charAt(position);
            return false;
        }
        if (position==exp.length()-1) {
            erreur[0]="Valeur attendue à droite de l'operateur "+exp.charAt(position);
            return false;
        }
        if (exp.length()<=position) {
            erreur[0]="Impossible de convertir "+exp+" en condition de comparaison";
            return false;
        }
        boolean b=false;
        int debut=0;
        for (int i=0;i<opList.length;i++) {
            debut=position-opList[i].length()+1;
            if (0<=debut && exp.substring(debut,position+1).equals(opList[i])) {
                b=true;
                op=opList[i];
                break;
            }
        }
        if (!b) {
            erreur[0]="Aucune operation de comparaison ne correspond à "+exp;
            return false;
        }
        String exp1=(new Immediat ()).deParenthesage(exp.substring(0,debut));
        String exp2=(new Immediat ()).deParenthesage(exp.substring(position+1,exp.length()));
        val1=(new Immediat ()).getVal(exp1,nbVoisins,var,erreur,dim);
        if (val1==null) {
            return false;
        }
        val2=(new Immediat ()).getVal(exp2,nbVoisins,var,erreur,dim);
        if (val2==null) {
            return false;
        }
        return true;
    }
    
    /**
     * Évalue la condition de comparaison pour une cellule donnée dans un tableau d'état d'automate.
     * 
     * @param tab Le tableau représentant l'état de l'automate.
     * @param voisins Les valeurs des voisins de la cellule.
     * @param indices Les indices des voisins de la cellule.
     * @return Vrai si la condition est remplie, faux sinon.
     */
    public boolean get (Tableau tab, double [] voisins, int [] indices) {
        switch (op) {
            case "<=": return val1.get(tab,voisins,indices) <= val2.get(tab,voisins,indices);
            case ">=": return val1.get(tab,voisins,indices) >= val2.get(tab,voisins,indices);
            case "==": return val1.get(tab,voisins,indices) == val2.get(tab,voisins,indices);
            case "!=": return val1.get(tab,voisins,indices) != val2.get(tab,voisins,indices);
            case "<": return val1.get(tab,voisins,indices) < val2.get(tab,voisins,indices);
            case ">": return val1.get(tab,voisins,indices) > val2.get(tab,voisins,indices);
        }
        return false;
    }
    
    /**
     * Retourne l'expression représentant la condition de comparaison.
     * 
     * @return Une chaîne de caractères représentant l'expression de la condition.
     */
    public String getExp () {
        return val1.getExp()+op+val2.getExp();
    }

    /**
     * Récupère la position de l'opérateur dans l'expression de la condition de comparaison.
     * 
     * @param exp L'expression de la condition.
     * @return La position de l'opérateur dans l'expression, ou -1 si aucun n'est trouvé.
     */
    public int getOp (String exp) {
        if (exp.length()==0) {
            return -1;
        }
        int par=0;
        int debut;
        for (int i=exp.length()-1;i>=0;i--) {
            if (exp.charAt(i)=='(') {
                par--;
            }
            else {
                if (exp.charAt(i)==')') {
                    par++;
                }
                else {
                    for (int k=0;k<opList.length;k++) {
                        debut=i-opList[k].length()+1;
                        if (0<=debut && exp.substring(debut,i+1).equals(opList[k])) {
                            return i;
                        }
                    }
                }
            }
            if (par<0) {
                return -1;
            }
        }
        return -1;
    }
}