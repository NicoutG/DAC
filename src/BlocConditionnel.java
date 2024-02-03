package src;

/**
 * La classe BlocConditionnel représente bloc de conditions et d'actions dans un automate cellulaire.
 * Elle contient la condition à valider pour explorer les blocs ou les actions qu'elle contient.
 */
public class BlocConditionnel {

    /**
     * condition est la condition à valider pour explorer le bloc.
     */
    private Condition condition;

    /**
     * condActions contient une Action ou un tableau de BlocConditionnel.
     */
    private Object condActions;

    /**
     * Crée et configure le BlocConditionnel basé sur une expression donnée.
     * 
     * @param exp L'expression définissant les blocs.
     * @param nbVoisins Le nombre de voisins de la cellule.
     * @param var Tableau de variables utilisées dans l'expression.
     * @param erreur Tableau pour stocker les messages d'erreur.
     * @param dim La dimension de l'espace de l'automate.
     * @return Vrai si le bloc est correctement configuré, faux sinon.
     */
    public boolean set (String exp, int nbVoisins, Variable [] var, String [] erreur, int dim) {
        int nb=0;
        while (nb<exp.length() && exp.charAt(nb)!='{') {
            nb++;
        }
        if (nb<exp.length() && exp.charAt(nb)=='{') {
            condition=(new OpLogBin ()).getCond((new Immediat ()).deParenthesage(exp.substring(0, nb)),nbVoisins,var,erreur,dim);
            if (condition==null) {
                return false;
            }
            if (exp.charAt(exp.length()-1)=='}') {
                exp=exp.substring(nb+1,exp.length()-1);
                int nbBlocs=getNbBlocs(exp);
                if (nbBlocs==-1) {
                    erreur[0]="Erreur d'accolades";
                    return false;
                }
                if (nbBlocs==0) {
                    condActions=new Action ();
                    return ((Action)condActions).set(exp,nbVoisins,var,erreur,dim);
                }
                else {
                    condActions=getBlocs(exp,nbVoisins,var,erreur,dim);
                    if (condActions==null) {
                        return false;
                    }
                    return true;
                }
            }
        }
        erreur[0]="Impossible de créer le bloc conditionnel";
        return false;
    }

    /**
     * Évalue la condition du bloc pour une cellule donnée dans un tableau d'état d'automate et retourne la valeur de l'action.
     * 
     * @param tab Le tableau représentant l'état de l'automate.
     * @param voisins Les valeurs des voisins de la cellule.
     * @param indices Les indices des voisins de la cellule.
     * @return Un double si toutes les conditions sont validées, null sinon.
     */
    public Object get (Tableau tab, double [] voisins, int [] indices) {
        if (condition.get(tab,voisins,indices)) {
            if (condActions instanceof Action) {
                return ((Action)condActions).get(tab,voisins,indices);
            }
            Object res;
            for (int i=0;i<((BlocConditionnel [])condActions).length;i++) {
                res=((BlocConditionnel [])condActions)[i].get(tab,voisins,indices);
                if (res!=null) {
                    return res;
                }
            }
        }
        return null;
    }

    /**
     * Retourne l'expression représentant les blocs conditionnels.
     * 
     * @param indent L'indice qui définit le nombre d'espaces à mettre avant.
     * @return Une chaîne de caractères représentant l'expression du bloc conditionnels.
     */
    public String getExp (int indent) {
        String exp="";
        for (int i=0;i<indent;i++) {
            exp+="    ";
        }
        exp+=condition.getExp()+" {\r\n";
        if (condActions instanceof Action) {
            exp+=((Action)condActions).getExp(indent+1);
        }
        else {
            for (int i=0;i<((BlocConditionnel [])condActions).length;i++) {
                exp+=((BlocConditionnel [])condActions)[i].getExp(indent+1);
            }
        }
        exp+="\r\n";
        for (int i=0;i<indent;i++) {
            exp+="  ";
        }
        exp+="}\r\n";
        return exp;
    }

    /**
     * Crée et configure un tableau d'instances de BlocConditionnel basé sur une expression donnée.
     * 
     * @param exp L'expression définissant les blocs.
     * @param nbVoisins Le nombre de voisins de la cellule.
     * @param var Tableau de variables utilisées dans l'expression.
     * @param erreur Tableau pour stocker les messages d'erreur.
     * @param dim La dimension de l'espace de l'automate.
     * @return Un tableau d'instances de BlocConditionnel configuré ou null en cas d'échec.
     */
    public BlocConditionnel [] getBlocs (String exp, int nbVoisins, Variable [] var, String [] erreur, int dim) {
        int nb=getNbBlocs(exp);
        if (nb<-1) {
            erreur[0]="Erreur d'accolades";
            return null;
        }
        if (nb==0) {
            erreur[0]="Impossible de créer le bloc conditionnel";
            return null;
        }
        BlocConditionnel [] blocs=new BlocConditionnel [nb];
        int debut=0;
        int num=0;
        int par=0;
        for (int i=0;i<exp.length();i++) {
            if (exp.charAt(i)=='{') {
                par++;
            }
            else {
                if (exp.charAt(i)=='}') {
                    par--;
                    if (par==0) {
                        blocs[num]=new BlocConditionnel ();
                        if (!blocs[num].set(exp.substring(debut, i+1),nbVoisins,var,erreur,dim)) {
                            return null;
                        }
                        num++;
                        debut=i+1;
                    }
                }
            }
        }
        return blocs;
    }

    /**
     * Retourne le nombre de blocs contenus dans l'expression.
     * 
     * @param exp L'expression définissant les blocs.
     * @return Le nombre de blocs présent dans l'expression.
     */
    private int getNbBlocs (String exp) {
        int par=0;
        int nb=0;
        for (int i=0;i<exp.length();i++) {
            if (exp.charAt(i)=='{') {
                par++;
            }
            else {
                if (exp.charAt(i)=='}') {
                    par--;
                    if (par==0) {
                        nb++;
                    }
                }
            }
            if (par<0) {
                return -1;
            }
        }
        if (par!=0) {
            return -1;
        }
        return nb;
    }
}
