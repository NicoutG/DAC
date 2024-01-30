package src;

import java.util.Random;

/**
 * La classe OpAriUni étend la classe Valeur et représente une opération arithmétique unaire
 * (comme le cosinus, le sinus, l'exponentielle, etc.) dans le contexte d'un automate cellulaire.
 */
public class OpAriUni extends Valeur{

    /**
     * obj est la valeur sur laquelle l'opération unaire est effectuée.
     */
    private Object obj;

    /**
     * op est l'opérateur utilisé pour l'opération unaire.
     */
    private String op="";

    /**
     * opList est la liste des opérations unaires possibles.
     */
    private String [] opList={"verif","count","#","cos","sin","tan","exp","ln","rand","coord","int"};
    
    /**
     * Configure l'opération arithmétique uniaire à partir d'une expression, position, nombre de voisins,
     * tableau de variables, tableau d'erreurs et dimension.
     * 
     * @param exp L'expression représentant l'opération uniaire.
     * @param position La position de l'opérateur dans l'expression.
     * @param nbVoisins Le nombre de voisins (non utilisé dans certains contextes).
     * @param var Tableau de variables utilisées dans l'expression.
     * @param erreur Tableau pour stocker les messages d'erreur.
     * @param dim La dimension de l'automate.
     * @return Vrai si l'opération est correctement configurée, faux sinon.
     */
    public boolean set (String exp, int position, int nbVoisins, Variable [] var, String [] erreur,int dim) {
        if (exp.length()<=position) {
            erreur[0]="Impossible de convertir "+exp+" en operation arithmetique unaire";
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
            erreur[0]="Aucune operation arithmetique unaire ne correspond à "+exp;
            return false;
        }
        String exp1=(new Immediat ()).deParenthesage(exp.substring(position+1,exp.length()));
        if (op.equals("verif")) {
            obj=(new OpLogBin ()).getCond(exp1,nbVoisins,var,erreur,dim);
            if (obj!=null) {
                return true;
            }
        }
        if (op.equals("count")) {
            obj=(new Immediat ()).getVal(exp1,nbVoisins,var,erreur,dim);
            if (obj!=null) {
                return true;
            }
        }
        if (op.equals("#")) {
            int [] val=new int [1];
            if (getInt(exp1,val)) {
                if (val[0]<=nbVoisins) {
                    obj=val[0];
                    return true;
                }
                erreur[0]="L'entier qui suit la fonction '#' doit être compris entre 0 et "+nbVoisins;
            }
        }
        if (op.equals("cos") || op.equals("sin") || op.equals("tan") || op.equals("exp") || op.equals("ln") || op.equals("int")) {
            obj=(new Immediat ()).getVal(exp1,nbVoisins,var,erreur,dim);
            if (obj!=null) {
                return true;
            }
        }
        if (op.equals("rand")) {
            int [] val=new int [1];
            if (getInt(exp1,val)) {
                if (0<val[0]) {
                    obj=val[0];
                    return true;
                }
                erreur[0]="L'entier qui suit la fonction 'rand' doit être supérieur à 0";
            }
        }
        if (op.equals("coord")) {
            int [] val=new int [1];
            if (getInt(exp1,val)) {
                if (0<val[0] && val[0]<=dim) {
                    obj=val[0];
                    return true;
                }
                erreur[0]="L'entier qui suit la fonction 'coord' doit être compris entre 1 et "+dim;
            }
        }
        return false;
    }
    
    /**
     * Renvoie le résultat de l'opération arithmétique uniaire.
     * 
     * @param tab Le tableau représentant l'état de l'automate.
     * @param voisins Les valeurs des voisins.
     * @param indices Les indices des voisins ou de la cellule elle-même.
     * @return Le résultat de l'opération uniaire.
     */
    public double get (Tableau tab, double [] voisins, int [] indices) {
        switch (op) {
            case "verif": {
                if (((Condition)obj).get(tab, voisins, indices)) {
                    return 1;
                }
                return 0;
            }
            case "count": {
                double res=0;
                if (voisins!=null) {
                    double val=((Valeur)obj).get(tab, voisins, indices);
                    for (int i=0;i<voisins.length;i++) {
                        if (voisins[i]==val) {
                            res++;
                        }
                    }
                }
                return res;
            }
            case "#": {
                if ((int)obj==0) {
                    return tab.getVal(indices);
                }
                return voisins[(int)obj-1];
            }
            case "cos": return Math.cos(Math.PI*((Valeur)obj).get(tab, voisins, indices)/180);
            case "sin": return Math.sin(Math.PI*((Valeur)obj).get(tab, voisins, indices)/180);
            case "tan": return Math.tan(Math.PI*((Valeur)obj).get(tab, voisins, indices)/180);
            case "exp": return Math.exp(((Valeur)obj).get(tab, voisins, indices));
            case "ln": return Math.log(((Valeur)obj).get(tab, voisins, indices));
            case "rand": {
                Random rand=new Random();
                return rand.nextInt((int)obj);
            }
            case "coord": return indices[(int)obj-1];
            case "int": return (int)((Valeur)obj).get(tab, voisins, indices);
        }
        return 0;
    }
    
    /**
     * Retourne l'expression de l'opération uniaire sous forme de chaîne de caractères.
     * 
     * @return Une chaîne représentant l'opération uniaire.
     */
    public String getExp () {
        if (obj instanceof Condition) {
            return op+"("+((Condition)obj).getExp()+")";
        }
        if (obj instanceof Valeur) {
            return op+"("+((Valeur)obj).getExp()+")";
        }
        return op+"("+obj+")";
    }

    /**
     * Détermine la position de l'opérateur dans une expression arithmétique uniaire.
     * 
     * @param exp L'expression à évaluer.
     * @return La position de l'opérateur dans l'expression, ou -1 si aucun n'est trouvé.
     */
    public int getOp (String exp) {
        if (exp.length()==0) {
            return -1;
        }
        int par=0;
        int debut;
        for (int i=0;i<exp.length();i++) {
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