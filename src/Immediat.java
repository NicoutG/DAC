package src;

public class Immediat extends Valeur {
    private double val=0;
    
    public boolean set (String exp, int position, int nbVoisins, Variable [] var, String [] erreur, int dim) {
        double [] nouv=new double [1];
        if (!getDouble(exp,nouv)) {
            erreur[0]="Impossible de convertir "+exp+" en reel";
            return false;
        }
        val=nouv[0];
        return true;
    }
    
    public double get (Tableau tab, double [] voisins, int [] indices) {
        return val;
    }
    
    public String getExp () {
        return Double.toString(val);
    }

    public int getOp (String exp) {
        if (exp.length()==0) {
            return -1;
        }
        try {
            Double.parseDouble(exp);
            return 0;
        }
        catch (Exception e) {
            return -1;
        }
    }
}