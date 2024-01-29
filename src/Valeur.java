package src;

public abstract class Valeur {
    
    public abstract boolean set (String exp, int position, int nbVoisins, Variable [] var, String [] erreur, int dim);
    
    public abstract double get (Tableau tab, double [] voisins, int [] indices);
    
    public abstract String getExp ();

    public abstract int getOp (String exp);
    
    public Valeur getVal (String exp, int nbVoisins, Variable [] var, String [] erreur, int dim) {
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
    
    public boolean getInt (String exp,int val []) {
        if (val.length<1) {
            return false;
        }
        while (exp.length()>0 && exp.charAt(0)==' ') {
            exp=exp.substring(1,exp.length());
        }
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

    public boolean getDouble (String exp, double val []) {
        if (val.length<1) {
            return false;
        }
        while (exp.length()>0 && exp.charAt(0)==' ') {
            exp=exp.substring(1,exp.length());
        }
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