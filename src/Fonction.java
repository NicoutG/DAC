package src;

import java.util.Random;

/**
 * La classe OpAriUni étend la classe Valeur et représente une opération arithmétique unaire
 * (comme le cosinus, le sinus, l'exponentielle, etc.) dans le contexte d'un automate cellulaire.
 */
public class Fonction extends Valeur{

    /**
     * parametres est l'ensemble des paramètres de la fonction
     */
    private Object parametres;

    /**
     * fonction est le numéro de la fonction utilisée.
     */
    private int fonction;

    /**
     * fonctionList est la liste des fonctions possibles.
     */
    private String [] fonctionsList=  {"maximum","minimum","majority","minority","average","median","sum", "length","verif","count","#","cos","sin","tan","exp","ln","rand","coord","int","abs","max","min","val"};
    
    /**
     * nbParams est le nombre de paramètres que prend chaque fonction.
     * -1 si le nombre de paramètres dépend de la dimension de la règle.
     */
    private int [] nbParams=          {    0    ,    0    ,     0    ,     0    ,    0    ,    0   ,  0  ,     0   ,   1   ,   1   , 1 ,  1  ,  1  ,  1  ,  1  ,  1 ,   1  ,   1   ,  1  ,  1  ,  2  ,  2  ,  -1 };
    
    /**
     * paramType définit le type des paramètres de la fonction.
     * 'n' si aucun paramètres, 'v' pour des valeurs, 'c' pour des conditions et 'i' pour des entiers
     */
    private char [] paramType=        {   'n'   ,   'n'   ,    'n'   ,    'n'   ,   'n'   ,   'n'  , 'n' ,    'n'  ,  'c'  ,  'v'  ,'i', 'v' , 'v' , 'v' , 'v' , 'v',  'i' ,  'i'  , 'v' , 'v' , 'v' , 'v' , 'v' };

    /**
     * Configure la fonction à partir d'une expression, position, nombre de voisins,
     * tableau de variables, tableau d'erreurs et dimension.
     * 
     * @param exp L'expression représentant la fonction.
     * @param position La position de la fin du nom de la fonction dans l'expression.
     * @param nbVoisins Le nombre de voisins (non utilisé dans certains contextes).
     * @param var Tableau de variables utilisées dans l'expression.
     * @param erreur Tableau pour stocker les messages d'erreur.
     * @param dim La dimension de l'automate.
     * @return Vrai si l'opération est correctement configurée, faux sinon.
     */
    public boolean set (String exp, int position, int nbVoisins, Variable [] var, String [] erreur,int dim) {
        if (exp.length()<=position) {
            erreur[0]="Impossible de convertir "+exp+" en fonction";
            return false;
        }
        fonction=-1;
        int debut=0;
        for (int i=0;i<fonctionsList.length;i++) {
            debut=position-fonctionsList[i].length()+1;
            if (0<=debut && exp.substring(debut,position+1).equals(fonctionsList[i])) {
                fonction=i;
            }
        }
        if (fonction==-1) {
            erreur[0]="Aucune fonction ne correspond à "+exp;
            return false;
        }
        exp=exp.substring(position+1,exp.length());
        int longueur=nbParams[fonction];
        if (longueur!=0) {
            if (longueur==-1) {
                longueur=dim;
            }
            switch (paramType[fonction]) {
                case 'v':parametres=new Valeur [longueur];break;
                case 'c':parametres=new Condition [longueur];break;
                case 'i':parametres=new int [longueur];break;
                default: {
                    erreur[0]="Le type "+paramType[fonction]+" n'est pas défini";
                    return false;
                }
            }
            if (exp.length()>=2 && exp.charAt(0)=='(' && exp.charAt(exp.length()-1)!=')') {
                erreur[0]="Parenthèse fermante manquante après : "+exp;
                return false;
            }
            if (exp.length()>=2 && exp.charAt(0)!='(' && exp.charAt(exp.length()-1)==')') {
                erreur[0]="Parenthèse ouvrante manquante après : "+fonctionsList[fonction];
                return false;
            }
            if (exp.length()<2 || exp.charAt(0)!='(' || exp.charAt(exp.length()-1)!=')') {
                erreur[0]="Parenthèses manquante après : "+fonctionsList[fonction];
                return false;
            }
            debut=1;
            int num=0;
            int par=-1;
            for (int i=0;i<exp.length();i++) {
                if (exp.charAt(i)=='(') {
                    par++;
                }
                else {
                    if (exp.charAt(i)==')') {
                        if (par<0) {
                            erreur[0]="Erreur de parenthèsage dans la fonction : "+fonctionsList[fonction];
                            return false;
                        }
                        par--;
                    }
                }
                if ((par==0 && (exp.charAt(i)==',') || (par==-1 && (exp.charAt(i)==')')))) {
                    String expSet=deParenthesage(exp.substring(debut, i));
                    switch (paramType[fonction]) {
                        case 'v': {
                            ((Valeur [])parametres)[num]=(new Immediat ()).getVal(expSet,nbVoisins,var,erreur,dim);
                            if (((Valeur [])parametres)[num]==null) {
                                erreur[0]="Impossible de convertir : "+expSet+" en Valeur";
                                return false;
                            }
                        }break;
                        case 'c': {
                            ((Condition [])parametres)[num]=(new OpLogBin ()).getCond(expSet,nbVoisins,var,erreur,dim);
                            if (((Condition [])parametres)[num]==null) {
                                erreur[0]="Impossible de convertir : "+expSet+" en Condition";
                                return false;
                            }
                        }break;
                        case 'i': {
                            int [] entier=new int [1];
                            if (!getInt(expSet,entier)) {
                                erreur[0]="Impossible de convertir : "+expSet+" en entier";
                                return false;
                            }
                            ((int [])parametres)[num]=entier[0];
                        }break;
                    }
                    num++;
                    debut=i+1;
                }
            }
            if (par!=-1) {
                erreur[0]="Parenthèses manquante après : "+fonctionsList[fonction];
                return false;
            }
            if (num!=longueur) {
                erreur[0]="La fonction "+fonctionsList[fonction]+" attend "+longueur+" paramètres contre "+num+" donnés";
                return false;
            }
        }
        else {
            parametres=null;
            if (!(exp.equals("") || exp.equals("()"))) {
                erreur[0]="Aucun paramètre attendu dans la fonction "+fonctionsList[fonction];
                return false;
            }
        }
        return true;
    }
    
    /**
     * Renvoie le résultat de la fonction.
     * 
     * @param tab Le tableau représentant l'état de l'automate.
     * @param voisins Les valeurs des voisins.
     * @param indices Les indices des voisins ou de la cellule elle-même.
     * @return Le résultat de la fonction.
     */
    public double get (Tableau tab, double [] voisins, int [] indices) {
        switch (fonctionsList[fonction]) {
            case "maximum": return maximum(voisins);
            case "minimum": return minimum(voisins);
            case "majority": return majority(voisins);
            case "minority": return minority(voisins);
            case "average": return average(voisins);
            case "median": return median(voisins);
            case "sum": return sum(voisins);
            case "length": return tab.getTaille();
            case "verif": {
                if (((Condition [])parametres)[0].get(tab, voisins, indices)) {
                    return 1;
                }
                return 0;
            }
            case "count": {
                double res=0;
                if (voisins!=null) {
                    double val=((Valeur [])parametres)[0].get(tab, voisins, indices);
                    for (int i=0;i<voisins.length;i++) {
                        if (voisins[i]==val) {
                            res++;
                        }
                    }
                }
                return res;
            }
            case "#": {
                int entier=modulo(((int [])parametres)[0],(voisins.length+1));
                if (entier==0) {
                    return tab.getVal(indices);
                }
                return voisins[entier-1];
            }
            case "cos": return Math.cos(Math.PI*((Valeur [])parametres)[0].get(tab, voisins, indices)/180);
            case "sin": return Math.sin(Math.PI*((Valeur [])parametres)[0].get(tab, voisins, indices)/180);
            case "tan": return Math.tan(Math.PI*((Valeur [])parametres)[0].get(tab, voisins, indices)/180);
            case "exp": return Math.exp(((Valeur [])parametres)[0].get(tab, voisins, indices));
            case "ln": return Math.log(((Valeur [])parametres)[0].get(tab, voisins, indices));
            case "rand": {
                Random rand=new Random();
                int entier=((int [])parametres)[0];
                if (entier<0)
                    return -rand.nextInt(-entier);
                else
                    if (entier>0)
                        return -rand.nextInt(entier);
                    else
                        return 0;
            }
            case "coord": {
                int entier=modulo((((int [])parametres)[0]-1),tab.getDim())+1;
                return indices[entier-1];
            }
            case "int": return (int)((Valeur [])parametres)[0].get(tab, voisins, indices);
            case "abs": return Math.abs(((Valeur [])parametres)[0].get(tab, voisins, indices));
            case "max": return Math.max(((Valeur [])parametres)[0].get(tab, voisins, indices),((Valeur [])parametres)[1].get(tab, voisins, indices));
            case "min": return Math.min(((Valeur [])parametres)[0].get(tab, voisins, indices),((Valeur [])parametres)[1].get(tab, voisins, indices));
            case "val": {
                int [] vals=new int [((Valeur [])parametres).length];
                for (int i=0;i<vals.length;i++)
                    vals[i]=(int)((Valeur [])parametres)[i].get(tab, voisins, indices);
                return tab.getVal(vals);
            }
        }
        return 0;
    }
    
    /**
     * Retourne l'expression de la fonction sous forme de chaîne de caractères.
     * 
     * @return Une chaîne représentant la fonction.
     */
    public String getExp () {
        int nb=nbParams[fonction];
        if (nb==0) {
            return fonctionsList[fonction]+"()";
        }
        String res=fonctionsList[fonction]+"(";
        switch (paramType[fonction]) {
            case 'v': {
                for (int i=0;i<((Valeur [])parametres).length;i++)
                    res+=((Valeur [])parametres)[i].getExp()+",";
            }break;
            case 'c': {
                for (int i=0;i<((Condition [])parametres).length;i++)
                    res+=((Condition [])parametres)[i].getExp()+",";
            }break;
            case 'i': {
                for (int i=0;i<((int [])parametres).length;i++)
                    res+=((int [])parametres)[i]+",";
            }break;
        }
        res=res.substring(0,res.length()-1)+")";
        return res;
    }

    /**
     * Détermine la position de la fonction dans une expression.
     * 
     * @param exp L'expression à évaluer.
     * @return La position de la fonction dans l'expression, ou -1 si aucun n'est trouvé.
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
                    if (par==0) {
                        for (int k=0;k<fonctionsList.length;k++) {
                            debut=i-fonctionsList[k].length()+1;
                            if (0<=debut && exp.substring(debut,i+1).equals(fonctionsList[k])) {
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

    /**
     * Calcule le modulo de deux entiers.
     * 
     * @param val1 Le premier entier.
     * @param val2 Le second entier.
     * @return Le modulo de val1 par val2.
     */
    private int modulo (int val1, int val2) {
        if (val1>=0) {
            return val1%val2;
        }
        return (val2-(-val1-1)%val2-1);
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