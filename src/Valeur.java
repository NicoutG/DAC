package src;

/**
 * La classe abstraite Valeur représente une valeur générique dans un automate cellulaire.
 * Elle fournit les méthodes de base pour gérer différentes types de valeurs.
 */
public abstract class Valeur {
    
    /**
     * Configure la valeur en fonction de l'expression et de la position donnée.
     *
     * @param exp L'expression à analyser.
     * @param position La position de l'élément dans l'expression.
     * @param nbVoisins Le nombre de voisins dans le contexte de l'automate.
     * @param var Un tableau de variables disponibles.
     * @param erreur Un tableau pour stocker les messages d'erreur.
     * @param dim La dimension de l'automate.
     * @return Vrai si la configuration réussit, faux sinon.
     */
    public abstract boolean set (String exp, int position, int nbVoisins, Variable [] var, String [] erreur, int dim);
    
    /**
     * Récupère la valeur actuelle dans le contexte de l'automate.
     *
     * @param tab Le tableau représentant l'état de l'automate.
     * @param voisins Les valeurs des voisins de la cellule actuelle.
     * @param indices Les indices de la cellule actuelle.
     * @return La valeur calculée.
     */
    public abstract double get (Tableau tab, double [] voisins, int [] indices);
    
    /**
     * Obtient la représentation sous forme de chaîne de la valeur.
     *
     * @return La représentation textuelle de la valeur.
     */
    public abstract String getExp ();

    /**
     * Identifie le type d'opération basé sur l'expression donnée.
     *
     * @param exp L'expression à analyser.
     * @return L'indice de l'opération dans l'expression, -1 si aucune opération n'est trouvée.
     */
    public abstract int getOp (String exp);
    
    /**
     * Obtient la valeur correspondante à l'expression donnée.
     *
     * @param exp L'expression à analyser.
     * @param nbVoisins Le nombre de voisins dans le contexte de l'automate.
     * @param var Un tableau de variables disponibles.
     * @param erreur Un tableau pour stocker les messages d'erreur.
     * @param dim La dimension de l'automate.
     * @return La valeur correspondante à l'expression.
     */
    public Valeur getVal (String exp, int nbVoisins, Variable [] var, String [] erreur, int dim) {
        exp=exp.trim();
        Valeur val;
        int n=(new OpAriBin ()).getOp(exp);
        if (n!=-1) {
            val=new OpAriBin ();
            if (val.set(exp,n,nbVoisins,var,erreur,dim)) {
                return val;
            }
            return null;
        }
        n=(new OpAriUni ()).getOp(exp);
        if (n!=-1) {
            val=new OpAriUni ();
            if (val.set(exp,n,nbVoisins,var,erreur,dim)) {
                return val;
            }
            return null;
        }
        n=(new Variable ()).getOp(exp);
        if (n!=-1) {
            if (var!=null) {
                String nomVar=exp.substring(1,n+1);
                for (int i=0;i<var.length;i++) {
                    if (var[i].getNom().equals(nomVar)) {
                        return var[i];
                    }
                }
            }
            erreur[0]="Variable "+exp+" introuvable";
            return null;
        }
        n=(new Immediat ()).getOp(exp);
        if (n!=-1) {
            val=new Immediat ();
            if (val.set(exp,n,nbVoisins,var,erreur,dim)) {
                return val;
            }
            return null;
        }
        n=(new Etude ()).getOp(exp);
        if (n!=-1) {
            val=new Etude ();
            if (val.set(exp,n,nbVoisins,var,erreur,dim)) {
                return val;
            }
            return null;
        }
        erreur[0]="Aucune valeur correspondante pour "+exp;
        return null;
    }
    
    /**
     * Convertit une chaîne en un entier.
     *
     * @param exp La chaîne à convertir.
     * @param val Un tableau pour stocker le résultat converti.
     * @return Vrai si la conversion réussit, faux sinon.
     */
    public boolean getInt (String exp,int val []) {
        if (val.length<1) {
            return false;
        }
        exp=exp.trim();
        if (!(exp.length()>0)) {
            return false;
        }
        boolean neg=false;
        int i=0;
        if (exp.charAt(0)=='-') {
            neg=true;
            i++;
        }
        val[0]=0;
        while (i<exp.length() && '0'<=exp.charAt(i) && exp.charAt(i)<='9') {
            val[0]=10*val[0]+exp.charAt(i)-'0';
            i++;
        }
        if (i<exp.length()) {
            return false;
        }
        if ((neg && i==1) || (!neg && i==0)) {
            return false;
        }
        if (neg) {
            val[0]*=-1;
        }
        return true;
    }

    /**
     * Convertit une chaîne en un double.
     *
     * @param exp La chaîne à convertir.
     * @param val Un tableau pour stocker le résultat converti.
     * @return Vrai si la conversion réussit, faux sinon.
     */
    public boolean getDouble (String exp, double val []) {
        if (val.length<1) {
            return false;
        }
        exp=exp.trim();
        val[0]=0.0;
        int i=0;
        boolean neg=false;
        if (exp.charAt(0)=='-') {
            neg=true;
            i++;
        }
        while (i<exp.length() && '0'<=exp.charAt(i) && exp.charAt(i)<='9') {
            val[0]=10*val[0]+exp.charAt(i)-'0';
            i++;
        }
        if (i==0) {
            return false;
        }
        if (i<exp.length() && exp.charAt(i)=='.') {
            double val2=0.1;
            i++;
            while (i<exp.length() && '0'<=exp.charAt(i) && exp.charAt(i)<='9') {
                val[0]+=val2*(exp.charAt(i)-'0');
                val2*=0.1;
                i++;
            }
        }
        if (i<exp.length()) {
            return false;
        }
        if (neg) {
            val[0]*=-1;
        }
        return true;
    }

    /**
     * Supprime les parenthèses extérieures d'une expression si nécessaire.
     *
     * @param exp L'expression à analyser.
     * @return L'expression sans parenthèses extérieures.
     */
    public String deParenthesage (String exp) {
        int par=0;
        for (int i=0;i<exp.length();i++) {
            switch (exp.charAt(i)) {
                case '(':par++;break;
                case ')':par--;break;
            }
            if (par==0 && i<exp.length()-1) {
                return exp;
            }
        }
        if (exp.length()>=2 && exp.charAt(0)=='(' && exp.charAt(exp.length()-1)==')') {
            return deParenthesage(exp.substring(1,exp.length()-1));
        }
        return exp;
    }
}