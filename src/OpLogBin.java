package src;

import java.util.Arrays;

/**
 * La classe OpLogBin étend la classe Condition et représente une opération logique binaire
 * (comme ET logique '&&' ou OU logique '||') dans le contexte d'un automate cellulaire.
 */
public class OpLogBin extends Condition{

    /**
     * cond1 et cond2 sont les deux conditions comparées.
     */
    private Condition cond1=null;
    private Condition cond2=null;

    /**
     * op est l'opérateur utilisé pour l'opération logique binaire.
     */
    private String op="";

    /**
     * opList est la liste des opérations logiques binaires possibles.
     */
    private String [] opList={"&&","||"};
    
    /**
     * Configure l'opération logique binaire à partir d'une expression, position, nombre de voisins,
     * tableau de variables, tableau d'erreurs et dimension.
     * 
     * @param exp L'expression représentant l'opération logique binaire.
     * @param position La position de l'opérateur dans l'expression.
     * @param nbVoisins Le nombre de voisins (non utilisé dans certains contextes).
     * @param var Tableau de variables utilisées dans l'expression.
     * @param erreur Tableau pour stocker les messages d'erreur.
     * @param dim La dimension de l'automate.
     * @return Vrai si l'opération est correctement configurée, faux sinon.
     */
    public boolean set (String exp, int position, int nbVoisins, Variable [] var, String [] erreur, int dim) {
        if (exp.length()<=position || !Arrays.toString(opList).contains(""+exp.charAt(position))) {
            erreur[0]="Impossible de convertir "+exp+" en condition binaire";
            return false;
        }
        boolean b=false;
        int debut=0;
        for (int i=0;i<opList.length;i++) {
            debut=position-opList[i].length()+1;
            if (0<=debut && exp.substring(debut,position+1).equals(opList[i])) {
                b=true;
                op=opList[i];
            }
        }
        if (!b) {
            erreur[0]="Aucune operation conditionnelle binaire ne correspond à "+exp;
            return false;
        }
        String exp1=(new Immediat ()).deParenthesage(exp.substring(0,debut));
        String exp2=(new Immediat ()).deParenthesage(exp.substring(position+1,exp.length()));
        cond1=getCond(exp1,nbVoisins,var,erreur, dim);
        if (cond1==null) {
            return false;
        }
        cond2=getCond(exp2,nbVoisins,var,erreur, dim);
        if (cond2==null) {
            return false;
        }
        return true;
    }
    
    /**
     * Évalue l'opération logique binaire pour une cellule donnée dans un tableau d'état d'automate.
     * 
     * @param tab Le tableau représentant l'état de l'automate.
     * @param voisins Les valeurs des voisins de la cellule.
     * @param indices Les indices des voisins de la cellule.
     * @return Vrai si la condition logique binaire est remplie, faux sinon.
     */
    public boolean get (Tableau tab, double [] voisins, int [] indices) {
        switch (op) {
            case "&&": return cond1.get(tab,voisins,indices) && cond2.get(tab,voisins,indices);
            case "||": return cond1.get(tab,voisins,indices) || cond2.get(tab,voisins,indices);
        }
        return false;
    }
    
    /**
     * Retourne l'expression représentant l'opération logique binaire.
     * 
     * @return Une chaîne de caractères représentant l'expression de l'opération.
     */
    public String getExp () {
        return "("+cond1.getExp()+")"+op+"("+cond2.getExp()+")";
    }

    /**
     * Récupère la position de l'opérateur dans l'expression de l'opération logique binaire.
     * 
     * @param exp L'expression à évaluer.
     * @return La position de l'opérateur dans l'expression, ou -1 si aucun n'est trouvé.
     */
    public int getOp (String exp) {
        if (exp.length()<3) {
            return -1;
        }
        int par=0;
        int debut;
        for (int i=exp.length()-1;i>=1;i--) {
            if (exp.charAt(i)=='(') {
                par--;
            }
            else {
                if (exp.charAt(i)==')') {
                    par++;
                }
                else {
                    if (par==0) {
                        for (int k=0;k<opList.length;k++) {
                            debut=i-opList[k].length()+1;
                            if (0<=debut && exp.substring(debut,i+1).equals(opList[k])) {
                                return i;
                            }
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