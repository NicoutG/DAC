package src;

/**
 * La classe Action représente une action dans un automate cellulaire.
 * Elle contient des probabilités et des valeurs correspondantes pour effectuer des actions.
 */
public class Action {

    /**
     * proba est un tableau de valeurs représentant les probabilités d'effectuer une action.
     */
    private Valeur [] proba=null;

    /**
     * valeurs est un tableau de valeurs représentant les valeurs des actions.
     */
    private Valeur [] valeurs=null;

    /**
     * Configure l'action à partir d'une expression, du nombre de voisins,
     * d'un tableau de variables, d'un tableau pour les messages d'erreur et de la dimension.
     * 
     * @param exp L'expression définissant les actions et leurs probabilités.
     * @param nbVoisins Le nombre de voisins considérés pour l'action.
     * @param var Tableau de variables utilisées dans l'expression.
     * @param erreur Tableau pour stocker les messages d'erreur.
     * @param dim La dimension de l'espace de l'action.
     * @return Vrai si l'action est correctement configurée, faux sinon.
     */
    public boolean set (String exp, int nbVoisins, Variable [] var, String [] erreur, int dim) {
        String [] exps=exp.split(",");
        if (exps==null || exps.length==0) {
            erreur[0]="Instruction manquante";
            return false;
        }
        int nbActions=exps.length;
        proba=new Valeur [nbActions];
        valeurs=new Valeur [nbActions];
        for (int i=0; i<nbActions; i++) {
            String [] act=exps[i].split(":");
            if (act.length!=2) {
                if (act.length>2) {
                    erreur[0]="Plusieurs : trouvés dans la meme instruction";
                }
                else {
                    erreur[0]="Valeurs attendues à gauche et à droite de :";
                }
                return false;
            }
            act[0]=(new Immediat ()).deParenthesage(act[0]);
            proba[i]=(new Immediat ()).getVal(act[0],nbVoisins,var,erreur,dim);
            if (proba[i]==null) {
                return false;
            }
            act[1]=(new Immediat ()).deParenthesage(act[1]);
            valeurs[i]=(new Immediat ()).getVal(act[1],nbVoisins,var,erreur,dim);
            if (valeurs[i]==null) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Calcule la valeur de l'action basée sur un tableau, les valeurs des voisins et leurs indices.
     * 
     * @param tab Le tableau utilisé pour calculer la valeur de l'action.
     * @param voisins Les valeurs des voisins.
     * @param indices Les indices des voisins.
     * @return La valeur calculée de l'action ou -1 si aucune action ne peut être effectuée.
     */
    public double get (Tableau tab, double [] voisins, int [] indices) {
        int nb=proba.length;
        if (nb<1) 
            return -1;
        double [] tirage;
        tirage=new double[nb];
        tirage[0]=Math.max(0,proba[0].get(tab, voisins, indices));
        for (int i=1;i<nb;i++) {
            tirage[i]=tirage[i-1]+Math.max(0,proba[i].get(tab, voisins, indices));
        }
        double rand=tirage[nb-1]*Math.random();
        for (int i=0;i<nb;i++) {
            if (rand<=tirage[i]) {
                return valeurs[i].get(tab,voisins,indices);
            }
        }
        return -1;
    }
    
    /**
     * Retourne l'expression représentant les probabilités et les valeurs de l'action.
     * 
     * @return Une chaîne de caractères représentant l'expression de l'action.
     */
    public String getExp () {
        String exp=proba[0].getExp()+":"+valeurs[0].getExp();
        for (int i=1;i<proba.length;i++) {
            exp+=", \r\n    "+proba[i].getExp()+":"+valeurs[i].getExp();
        }
        exp+="; ";
        return exp;
    }
}