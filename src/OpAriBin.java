package src;

/**
 * La classe OpAriBin étend la classe Valeur et représente une opération arithmétique binaire
 * (comme l'addition, la soustraction, la multiplication, etc.) dans le contexte d'un automate cellulaire.
 */
public class OpAriBin extends Valeur {

    /**
     * val1 et val2 sont les deux valeurs sur lesquelles l'opération binaire est effectuée.
     */
    private Valeur val1=null;
    private Valeur val2=null;

    /**
     * op est l'opérateur utilisé pour l'opération binaire.
     */
    private String op="";

    /**
     * opList est la liste des opérations binaires possibles.
     */
    private String [][] opList={{"+","-"},{"%"},{"*","/"},{"^"}};
    
    /**
     * Configure l'opération arithmétique binaire à partir d'une expression, position, nombre de voisins,
     * tableau de variables, tableau d'erreurs et dimension.
     * 
     * @param exp L'expression représentant l'opération binaire.
     * @param position La position de l'opérateur dans l'expression.
     * @param nbVoisins Le nombre de voisins (non utilisé dans ce contexte).
     * @param var Tableau de variables utilisées dans l'expression.
     * @param erreur Tableau pour stocker les messages d'erreur.
     * @param dim La dimension de l'automate (non utilisée dans ce contexte).
     * @return Vrai si l'opération est correctement configurée, faux sinon.
     */
    public boolean set (String exp, int position, int nbVoisins, Variable [] var, String [] erreur, int dim) {
        if (exp.length()<=position) {
            erreur[0]="Impossible de convertir "+exp+" en operation arithmetique binaire";
            return false;
        }
        if (position==0 && exp.charAt(position)!='-') {
            erreur[0]="Valeur attendue à gauche de l'operateur "+exp.charAt(position);
            return false;
        }
        if (position==exp.length()-1) {
            erreur[0]="Valeur attendue à droite de l'operateur "+exp.charAt(position);
            return false;
        }
        boolean b=false;
        int debut=0;
        for (int j=0;j<opList.length;j++) {
            for (int i=0;i<opList[j].length;i++) {
                debut=position-opList[j][i].length()+1;
                if (0<=debut && exp.substring(debut,position+1).equals(opList[j][i])) {
                    b=true;
                    op=opList[j][i];
                }
            }
        }
        if (!b) {
            erreur[0]="Aucune operation arithmetique binaire ne correspond à "+exp;
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
     * Renvoie le résultat de l'opération arithmétique binaire.
     * 
     * @param tab Le tableau représentant l'état de l'automate.
     * @param voisins Les valeurs des voisins (non utilisées dans ce contexte).
     * @param indices Les indices des voisins (non utilisés dans ce contexte).
     * @return Le résultat de l'opération binaire.
     */
    public double get (Tableau tab, double [] voisins, int [] indices) {
        switch (op) {
            case "+": return val1.get(tab,voisins,indices)+val2.get(tab,voisins,indices);
            case "-": return val1.get(tab,voisins,indices)-val2.get(tab,voisins,indices);
            case "%": return modulo(val1.get(tab,voisins,indices),val2.get(tab,voisins,indices));
            case "*": return val1.get(tab,voisins,indices)*val2.get(tab,voisins,indices);
            case "/": return val1.get(tab,voisins,indices)/val2.get(tab,voisins,indices);
            case "^": return Math.pow(val1.get(tab,voisins,indices),val2.get(tab,voisins,indices));
        }   
        return 0;
    }
    
    /**
     * Retourne l'expression de l'opération binaire sous forme de chaîne de caractères.
     * 
     * @return Une chaîne représentant l'opération binaire.
     */
    public String getExp () {
        return "("+val1.getExp()+op+val2.getExp()+")";
    }

    /**
     * Détermine la position de l'opérateur dans une expression arithmétique binaire.
     * 
     * @param exp L'expression à évaluer.
     * @return La position de l'opérateur dans l'expression, ou -1 si aucun n'est trouvé.
     */
    public int getOp (String exp) {
        if (exp.length()<3) {
            return -1;
        }
        int debut;
        for (int j=0;j<opList.length;j++) {
            int par=0;
            for (int i=exp.length()-1;i>=0;i--) {
                if (exp.charAt(i)=='(') {
                    par--;
                }
                else {
                    if (exp.charAt(i)==')') {
                        par++;
                    }
                    else {
                        if (par==0) {
                            for (int k=0;k<opList[j].length;k++) {
                                debut=i-opList[j][k].length()+1;
                                if (0<=debut && exp.substring(debut,i+1).equals(opList[j][k])) {
                                    if (i!=0 || !opList[j][k].equals("-")) {
                                        return i;
                                    }
                                }
                            }
                        }
                    }
                }
                if (par<0) {
                    return -1;
                }
            }
        }
        return -1;
    }

    public double modulo (double nb1, double nb2) {
        double result = nb1 % nb2;
        if (result<0) {
            result+=nb2;
        }
        return result;
    }
}