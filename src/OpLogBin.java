package src;

import java.util.Arrays;

public class OpLogBin extends Condition{
    private Condition cond1=null;
    private Condition cond2=null;
    private String op="";
    private String [] opList={"&&","||"};
    
    public boolean set (String exp, int position, int nbVoisins, Variable [] var, String [] erreur, int dim) {
        if (exp.length()<=position || !Arrays.toString(opList).contains(""+exp.charAt(position))) {
            erreur[0]="Impossible de convertir "+exp+" en condition binaire";
            return false;
        }
        boolean b=false;
        int debut=0;
        for (int i=0;i<opList.length;i++) {
            debut=position-opList[i].length()+1;
            if (0<=debut && exp.substring(debut,position+1).equals(opList[i])) {
                b=true;
                op=opList[i];
            }
        }
        if (!b) {
            erreur[0]="Aucune operation conditionnelle binaire ne correspond à "+exp;
            return false;
        }
        String exp1=(new Immediat ()).deParenthesage(exp.substring(0,debut));
        String exp2=(new Immediat ()).deParenthesage(exp.substring(position+1,exp.length()));
        cond1=getCond(exp1,nbVoisins,var,erreur, dim);
        if (cond1==null) {
            return false;
        }
        cond2=getCond(exp2,nbVoisins,var,erreur, dim);
        if (cond2==null) {
            return false;
        }
        return true;
    }
    
    public boolean get (Tableau tab, double [] voisins, int [] indices) {
        switch (op) {
            case "&&": return cond1.get(tab,voisins,indices) && cond2.get(tab,voisins,indices);
            case "||": return cond1.get(tab,voisins,indices) || cond2.get(tab,voisins,indices);
        }
        return false;
    }
    
    public String getExp () {
        return "("+cond1.getExp()+")"+op+"("+cond2.getExp()+")";
    }

    public int getOp (String exp) {
        if (exp.length()<3) {
            return -1;
        }
        int par=0;
        int debut;
        for (int i=exp.length()-1;i>=1;i--) {
            if (exp.charAt(i)=='(') {
                par--;
            }
            else {
                if (exp.charAt(i)==')') {
                    par++;
                }
                else {
                    if (par==0) {
                        for (int k=0;k<opList.length;k++) {
                            debut=i-opList[k].length()+1;
                            if (0<=debut && exp.substring(debut,i+1).equals(opList[k])) {
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
}