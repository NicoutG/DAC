package src;

public class Variable extends Valeur {
    private String nom=" ";
    private double val=0;
    
    public boolean set (String exp, int position, int nbVoisins, Variable [] var, String [] erreur, int dim) {
        if (exp.length()<=1 || exp.charAt(0)!='$' || position>=exp.length()) {
            erreur[0]="Impossible de convertir "+exp+" en variable";
            return false;
        }
        nom=exp.substring(1,position+1);
        val=0;
        return true;
    }
    
    public double get (Tableau tab, double [] voisins, int [] indices) {
        return val;
    }
    
    public String getExp () {
        return "$"+nom;
    }

    public int getOp (String exp) {
        if (exp.length()>1 && exp.charAt(0)=='$'){
            int res=1;
            while(res<exp.length() && (('a'<=exp.charAt(res) && exp.charAt(res)<='z') || ('A'<=exp.charAt(res) && exp.charAt(res)<='Z') || ('0'<=exp.charAt(res) && exp.charAt(res)<='9') || exp.charAt(res)=='_')) {
                res++;
            }
            if (res>1) {
                return res-1;
            }
        }
        return -1;
    }

    public String getNom () {
        return nom;
    }

    public void setVal (double v) {
        val=v;
    }
}