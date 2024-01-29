package src;

import java.nio.file.*;
import java.io.*;

public class Regles {
    private int dim;
    private boolean valide;
    private int [][] voisins;
    private Condition [] conditions;
    private Action [] actions;
    private Variable [] variables;
    private String erreur;

    public Regles () {
        dim=0;
        valide=false;
        voisins=null;
        conditions=null;
        actions=null;
        variables=null;
        erreur="";
    }

    public Regles (String fichier) {
        dim=0;
        valide=false;
        voisins=null;
        conditions=null;
        actions=null;
        variables=null;
        erreur="";
        charger(fichier);
    }
    
    private boolean setCondition (String exp, int num) {
        if (num<0 || conditions.length-1<num) {
            return false;
        }
        exp=(new Immediat ()).deParenthesage(exp);
        String [] er=new String [1];
        er[0]=erreur;
        conditions[num]=(new OpLogBin ()).getCond(exp,voisins.length,variables,er,dim);
        erreur=er[0];
        if (conditions[num]==null) {
            return false;
        }
        return true;
    }
    
    private boolean setAction (String exp, int num) {
        if (num<0 || conditions.length-1<num) {
            return false;
        }
        actions[num]=new Action ();
        String [] er=new String [1];
        er[0]=erreur;
        boolean ret=actions[num].set(exp,voisins.length,variables,er,dim);
        erreur=er[0];
        return ret;
    }
    
    private boolean setCondActions (String exp) {
        setVariables(exp);
        String [] exps=exp.split(";");
        if (exps.length==0) {
            erreur="Aucune action trouvée";
            return false;
        }
        conditions=new Condition [exps.length];
        actions=new Action [exps.length];
        String [] acts=null;
        for (int i=0;i<exps.length;i++) {
            acts=exps[i].split("\\?");
            if (acts==null || acts.length!=2) {
                if (acts.length>2) {
                    erreur="Action manquante";
                }
                else {
                    erreur="Condition attendue à gauche et action attendue à droite de ?";
                }
                return false;
            }
            if (!setCondition(acts[0],i)) {
                return false;
            }
            if (!setAction(acts[1],i)) {
                return false;
            }
        }
        return true;
    }
    
    private boolean setVoisins (String exp) {
        String [] vois=exp.split(";");
        if (vois==null || vois.length<1) {
            erreur="Aucun voisin defini";
            return false;
        }
        String [] indices=vois[0].split(",");
        if (indices==null || indices.length<1) {
            erreur="Aucun voisin defini";
            return false;
        }
        dim=indices.length;
        voisins=new int [vois.length][dim];
        for (int j=0;j<vois.length;j++) {
            indices=vois[j].split(",");
            if (indices==null || indices.length!=dim) {
                erreur="Les coordonnées du voisin "+(j+1)+" ne sont pas de la meme dimension";
                return false;
            }
            int [] nb=new int [1];
            Valeur fonction=new Immediat ();
            for (int i=0;i<dim;i++) {
                if (!fonction.getInt(indices[i],nb)) {
                    erreur="Les coordonnées des voisins doivent etre des entiers";
                    return false;
                }
                voisins[j][i]=nb[0];
            }
        }
        return true;
    }

    private boolean addVariable (String nom) {
        if (!isVariable(nom)) {
            if (variables==null) {
                variables=new Variable[1];
                variables[0]=new Variable ();
                String [] er=new String [1];
                er[0]=erreur;
                variables[0].set("$"+nom,nom.length(),0,null,er,dim);
                erreur=er[0];
            }
            else {
                Variable [] nouv=new Variable[variables.length+1];
                for (int i=0;i<variables.length;i++) {
                    nouv[i]=variables[i];
                }
                nouv[variables.length]=new Variable ();
                String [] er=new String [1];
                er[0]=erreur;
                nouv[variables.length].set("$"+nom,nom.length(),0,null,er,dim);
                erreur=er[0];
                variables=nouv;
            }
            return true;
        }
        return false;
    }

    private void setVariables (String exp) {
        Valeur fonction=new Variable ();
        String sub;
        int n;
        for (int i=0;i<exp.length();i++) {
            sub=exp.substring(i,exp.length());
            n=fonction.getOp(sub);
            if (n!=-1) {
                addVariable(sub.substring(1,n+1));
            }
        }
    }

    private String retireCom (String exp) {
        if (!exp.contains("/*") || !exp.contains("*/")) {
            return exp;
        }
        String res="";
        boolean com=false;
        for (int i=0;i<exp.length()-1;i++) {
            if (exp.charAt(i)=='/' && exp.charAt(i+1)=='*') {
                i+=2;
                com=true;
            }
            else {
                if (exp.charAt(i)=='*' && exp.charAt(i+1)=='/') {
                    i+=2;
                    if (!com) {
                        return " ";
                    }
                    com=false;
                }
            }
            if (!com && i<exp.length()) {
                res+=exp.charAt(i);
            }
        }
        if (com) {
            return " ";
        }
        return res;
    }
    
    private double get (Tableau tab, int [] indices) {
        int [] vois=new int [dim];
        double [] valVois=new double [voisins.length];
        for (int j=0;j<voisins.length;j++) {
            for (int i=0;i<dim;i++) {
                vois[i]=indices[i]+voisins[j][i];
            }
            valVois[j]=tab.getVal(vois);
        }
        for (int i=0;i<conditions.length;i++) {
            if (conditions[i].get(tab, valVois, indices)) {
                return actions[i].get(tab,valVois,indices);
            }
        }
        return tab.getVal(indices);
    }

    public boolean set (String exp) {
        erreur="";
        exp=retireCom(exp);
        exp=simplification(exp);
        String [] exps=exp.split("@");
        valide=!(exps==null || exps.length!=2);
        if (!valide) {
            if (exps.length>2) {
                erreur="Plusieurs @ trouvés";
            }
            else {
                erreur="Arguments à gauche et à droite de @ necessaires";
            }
            return false;
        }
        valide=setVoisins(exps[0]);
        if (!valide) {
            return false;
        }
        valide=setCondActions(exps[1]);
        return valide;
    }
    
    public Tableau appliquer (Tableau tab) {
        if (tab.getDim()!=dim || !valide) {
            return tab;
        }
        int [] indices= new int [dim];
        int taille=tab.getTaille();
        Tableau res=new Tableau (dim,taille);
        while (indices[0]<taille) {
            for (int i=0;i<dim;i++) {
                if (indices[i]>=taille) {
                    indices[i]=0;
                }
            }
            res.setVal(indices,get(tab,indices));
            indices[dim-1]++;
            for (int i=dim-2;i>=0;i--) {
                if (indices[i+1]>=taille) {
                    indices[i]++;
                }
            }
        }
        return res;
    }
    
    public String getExp () {
        if (valide) {
            String exp="";
            for (int j=0;j<voisins.length;j++) {
                exp+=voisins[j][0];
                for (int i=1;i<dim;i++) {
                    exp+=","+voisins[j][i];
                }
                exp+=";\r\n";
            }
            exp+="@\r\n\r";
            for (int i=0;i<conditions.length;i++) {
                exp+=conditions[i].getExp()+"?\r\n    "+actions[i].getExp()+"\r\n";
            }
            return exp;
        }
        return "Error";
    }
    
    public boolean charger (String fichier) {
        try {
            String exp=new String(Files.readAllBytes(Paths.get(fichier)));
            return set(exp);
        }
        catch (IOException e) {
            erreur="Fichier "+fichier+" introuvable";
            return false;
        }
    }
    
    public boolean sauvegarder (String fichier) {
        if (valide) {
            try {
                Files.write(Paths.get(fichier), getExp().getBytes());
                return true;
            } catch (IOException e) {
                erreur="Fichier "+fichier+" introuvable";
                return false;
            }
        }
        erreur="Regle non valide";
        return false;
    }

    public boolean isVariable (String nom) {
        if (variables!=null) {
            for (int i=0;i<variables.length;i++) {
                if (variables[i].getNom().equals(nom)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean setVar (String nom, double val) {
        if (variables!=null) {
            for (int i=0;i<variables.length;i++) {
                if (nom.equals(variables[i].getNom())) {
                    variables[i].setVal(val);
                    return true;
                }
            }
        }
        return false;
    }

    public double getVar (String nom) {
        if (variables!=null) {
            for (int i=0;i<variables.length;i++) {
                if (nom.equals(variables[i].getNom())) {
                    return variables[i].get(null,null,null);
                }
            }
        }
        return 0;
    }

    public String [] getVarList () {
        if (variables!=null) {
            String [] list=new String [variables.length];
            for (int i=0;i<variables.length;i++) {
                list[i]=variables[i].getNom();
            }
            return list;
        }
        return null;
    }

    public int getNbVars () {
        if (variables==null) {
            return 0;
        }
        return variables.length;
    }

    private String simplification (String exp) {
        String [] exps=exp.split("\r?\n|\r");
        String res="";
        for (int i=0;i<exps.length;i++) {
            res+=exps[i];
        }
        exps=res.split(" ");
        res="";
        for (int i=0;i<exps.length;i++) {
            res+=exps[i];
        }
        return res;
    }

    public String getErreur () {
        return erreur;
    }

    public int getDim () {
        return dim;
    }
    
}