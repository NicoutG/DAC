package src;

import java.util.Random;

/**
 * La classe Etude est une extension de la classe Valeur. Elle est utilisée pour effectuer
 * différentes opérations statistiques sur un ensemble de valeurs, typiquement les valeurs
 * des cellules voisines dans un automate cellulaire.
 */
public class Etude extends Valeur{

    /**
     * L'opération à effectuer sur les valeurs.
     */
    private String op="";

    /**
     * Liste des opérations possibles.
     */
    private String [] opList={"maximum","minimum","majority","minority","average","median","sum", "length"};
    
    /**
     * Configure l'objet Etude avec une expression donnée, une position, un nombre de voisins,
     * un tableau de variables, un tableau d'erreurs et une dimension.
     * 
     * @param exp L'expression spécifiant l'opération à effectuer.
     * @param position La position de la cellule dans l'automate.
     * @param nbVoisins Le nombre de voisins de la cellule.
     * @param var Tableau de variables utilisées dans l'expression.
     * @param erreur Tableau pour stocker les messages d'erreur.
     * @param dim La dimension de l'espace de l'automate.
     * @return Vrai si l'objet Etude est correctement configuré, faux sinon.
     */
    public boolean set (String exp, int position, int nbVoisins, Variable [] var, String [] erreur, int dim) {
        if (exp.length()<=position) {
            erreur[0]="Impossible de convertir "+exp+" en valeur d'etude";
            return false;
        }
        boolean b=false;
        for (int i=0;i<opList.length;i++) {
            if (exp.equals(opList[i])) {
                b=true;
                op=opList[i];
                break;
            }
        }
        if (!b) {
            erreur[0]="Aucune valeur d'etude ne correspond à "+exp;
            return false;
        }
        return true;
    }
    
    /**
     * Renvoie le résultat de l'opération spécifiée sur un ensemble de valeurs.
     * 
     * @param tab Le tableau représentant l'état de l'automate.
     * @param voisins Les valeurs des voisins de la cellule.
     * @param indices Les indices des voisins de la cellule.
     * @return Le résultat de l'opération.
     */
    public double get (Tableau tab, double [] voisins, int [] indices) {
        switch (op) {
            case "maximum": return maximum(voisins);
            case "minimum": return minimum(voisins);
            case "majority": return majority(voisins);
            case "minority": return minority(voisins);
            case "average": return average(voisins);
            case "median": return median(voisins);
            case "sum": return sum(voisins);
            case "length": return tab.getTaille();
        }
        return 0;
    }
    
    /**
     * Retourne l'expression de l'opération en cours.
     * 
     * @return Une chaîne de caractères représentant l'opération.
     */
    public String getExp () {
        return op;
    }

    /**
     * Récupère l'opérateur utilisé dans l'expression de l'opération.
     * 
     * @param exp L'expression de l'opération.
     * @return L'indice de l'opérateur dans l'expression, ou -1 si aucun n'est trouvé.
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

    /**
     * Calcule le maximum d'un tableau de valeurs.
     * @param vals Le tableau de valeurs.
     * @return Le maximum.
     */
    private double maximum (double [] vals) {
        double val0=vals[0];
        for (int i=1;i<vals.length;i++) {
            if (val0<vals[i]) {
                val0=vals[i];
            }
        }
        return val0;
    }

    /**
     * Calcule le minimum d'un tableau de valeurs.
     * @param vals Le tableau de valeurs.
     * @return Le minimum.
     */
    private double minimum (double [] vals) {
        double val0=vals[0];
        for (int i=1;i<vals.length;i++) {
            if (val0>vals[i]) {
                val0=vals[i];
            }
        }
        return val0;
    }

    /**
     * Détermine la valeur majoritaire d'un tableau de valeurs.
     * En cas d'égalité, une valeur est sélectionnée aléatoirement parmi les égalités.
     * 
     * @param vals Tableau de valeurs doubles.
     * @return La valeur majoritaire ou une des valeurs majoritaires si égalité.
     */
    private double majority (double [] vals) {
        double [] valuni=new double [vals.length];
        double [] ocu=new double [vals.length];
        int nb=0;
        boolean b;
        for (int i=0;i<vals.length;i++) {
            b=false;
            for (int j=0;j<nb;j++) {
                if (vals[i]==valuni[j]) {
                    b=true;
                    ocu[j]++;
                }
            }
            if (!b) {
                valuni[nb]=vals[i];
                ocu[nb]=1;
                nb++;
            }
        }
        double val0=valuni[0];
        double nb0=ocu[0];
        Random random = new Random();
        int occurrence=1;
        for (int i=1;i<nb;i++) {
            if (nb0<ocu[i]) {
                nb0=ocu[i];
                val0=valuni[i];
                occurrence=1;
            }
            if (nb0==ocu[i]) {
                occurrence++;
                if (random.nextInt(1000)<1000/occurrence) {
                    val0=valuni[i];
                }
            }
        }
        return val0;
    }

    /**
     * Détermine la valeur minoritaire d'un tableau de valeurs.
     * En cas d'égalité, une valeur est sélectionnée aléatoirement parmi les égalités.
     * 
     * @param vals Tableau de valeurs doubles.
     * @return La valeur minoritaire ou une des valeurs minoritaires si égalité.
     */
    private double minority (double [] vals) {
        double [] valuni=new double [vals.length];
        double [] ocu=new double [vals.length];
        int nb=0;
        boolean b;
        for (int i=0;i<vals.length;i++) {
            b=false;
            for (int j=0;j<nb;j++) {
                if (vals[i]==valuni[j]) {
                    b=true;
                    ocu[j]++;
                }
            }
            if (!b) {
                valuni[nb]=vals[i];
                ocu[nb]=1;
                nb++;
            }
        }
        double val0=valuni[0];
        double nb0=ocu[0];
        Random random = new Random();
        int occurrence=1;
        for (int i=1;i<nb;i++) {
            if (nb0>ocu[i]) {
                nb0=ocu[i];
                val0=valuni[i];
                occurrence=1;
            }
            if (nb0==ocu[i]) {
                occurrence++;
                if (random.nextInt(1000)<1000/occurrence) {
                    val0=valuni[i];
                }
            }
        }
        return val0;
    }

    /**
     * Calcule la moyenne d'un tableau de valeurs.
     * 
     * @param vals Tableau de valeurs doubles.
     * @return La moyenne des valeurs du tableau.
     */
    private double average (double [] vals) {
        return sum(vals)/vals.length;
    }

    /**
     * Calcule la médiane d'un tableau de valeurs.
     * 
     * @param vals Tableau de valeurs doubles.
     * @return La médiane des valeurs du tableau.
     */
    private double median (double [] vals) {
        double t;
        int max=(vals.length+2)/2;
        for (int i=0;i<max;i++) {
            for (int j=i+1;j<vals.length;j++) {
                if (vals[j]>vals[i]) {
                    t=vals[i];
                    vals[i]=vals[j];
                    vals[j]=t;
                }
            }
        }
        if (vals.length%2==1) {
            return vals[max-1];
        }
        return (vals[max-1]+vals[max-2])/2;
    }

    /**
     * Calcule la somme d'un tableau de valeurs.
     * 
     * @param vals Tableau de valeurs doubles.
     * @return La somme des valeurs du tableau.
     */
     private double sum (double [] vals) {
        double tot=0;
        for (int i=0;i<vals.length;i++) {
            tot+=vals[i];
        }
        return tot;
     }

}
