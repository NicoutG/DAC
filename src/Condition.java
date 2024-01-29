package src;

public abstract class Condition {
    
    public abstract boolean set (String exp, int position, int nbVoisins, Variable [] var, String [] erreur, int dim);
    
    public abstract boolean get (Tableau tab, double [] voisins, int [] indices);
    
    public abstract String getExp ();

    public abstract int getOp (String exp);
    
    public Condition getCond (String exp, int nbVoisins, Variable [] var, String [] erreur, int dim) {
        Condition cond;
        int n=(new OpLogBin ()).getOp(exp);
        if (n!=-1) {
            cond=new OpLogBin ();
            if (cond.set(exp,n,nbVoisins,var,erreur,dim)) {
                return cond;
            }
            return null;
        }
        n=(new OpLogUni ()).getOp(exp);
        if (n!=-1) {
            cond=new OpLogUni ();
            if (cond.set(exp,n,nbVoisins,var,erreur,dim)) {
                return cond;
            }
            return null;
        }
        n=(new OpCompar ()).getOp(exp);
        if (n!=-1) {
            cond=new OpCompar ();
            if (cond.set(exp,n,nbVoisins,var,erreur,dim)) {
                return cond;
            }
            return null;
        }
        erreur[0]="Aucune condition correspondante pour "+exp;
        return null;
    }
}