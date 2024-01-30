package src;

import java.nio.file.*;
import java.io.*;

/**
 * La classe Regles gère les règles d'un automate cellulaire, incluant les conditions et les actions.
 * Elle permet de lire, de définir, de sauvegarder et d'appliquer des règles à un automate.
 */
public class Regles {

    /**
     * dim est la dimension de l'espace de l'automate.
     */
    private int dim;

    /**
     * valide est un booléen indiquant si les règles sont valides ou non.
     */
    private boolean valide;

    /**
     * voisins est un tableau d'entiers représentant les coordonnées des voisins.
     */
    private int [][] voisins;

    /**
     * conditions est un tableau de conditions.
     */
    private Condition [] conditions;

    /**
     * actions est un tableau d'actions.
     */
    private Action [] actions;

    /**
     * variables est un tableau de variables.
     */
    private Variable [] variables;

    /**
     * erreur est une chaîne de caractères contenant les messages d'erreur.
     */
    private String erreur;

    /**
     * Constructeur par défaut de Regles
     */
    public Regles () {
        dim=0;
        valide=false;
        voisins=null;
        conditions=null;
        actions=null;
        variables=null;
        erreur="";
    }

    /**
     * Constructeur de Regles qui permet de charger une règle
     * 
     * @param fichier Le fichier contenant les règles.
     */
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
    
    /**
     * Définit une condition pour l'automate à une position donnée.
     * 
     * @param exp L'expression de la condition.
     * @param num La position dans le tableau de conditions.
     * @return Vrai si la condition est définie avec succès, faux sinon.
     */
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
    
    /**
     * Définit une action pour l'automate à une position donnée.
     * 
     * @param exp L'expression de l'action.
     * @param num La position dans le tableau d'actions.
     * @return Vrai si l'action est définie avec succès, faux sinon.
     */
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
    
    /**
     * Configure les conditions et actions de l'automate à partir d'une expression.
     * 
     * @param exp L'expression représentant les règles de l'automate.
     * @return Vrai si les règles sont définies avec succès, faux sinon.
     */
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
    
    /**
     * Définit les coordonnées des voisins pour l'automate.
     * 
     * @param exp L'expression représentant les coordonnées des voisins.
     * @return Vrai si les voisins sont définis avec succès, faux sinon.
     */
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

    /**
     * Ajoute une variable si elle n'existe pas déjà.
     * 
     * @param nom Le nom de la variable à ajouter.
     * @return Vrai si la variable est ajoutée, faux si elle existe déjà.
     */
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

    /**
     * Configure les variables de l'automate à partir d'une expression.
     * 
     * @param exp L'expression contenant les variables à configurer.
     */
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

    /**
     * Retire les commentaires d'une expression.
     * 
     * @param exp L'expression à traiter.
     * @return L'expression sans commentaires.
     */
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
    
    /**
     * Calcule la valeur d'une cellule à partir de ses voisins.
     * 
     * @param tab Le tableau représentant l'état de l'automate.
     * @param indices Les indices de la cellule.
     * @return La valeur de la cellule.
     */
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

    /**
     * Configure les règles de l'automate à partir d'une expression.
     * 
     * @param exp L'expression représentant les règles de l'automate.
     * @return Vrai si les règles sont définies avec succès, faux sinon.
     */
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
    
    /**
     * Applique les règles de l'automate à un tableau.
     * 
     * @param tab Le tableau représentant l'état de l'automate.
     * @return Le tableau résultant de l'application des règles.
     */
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
    
    /**
     * Renvoie l'expression représentant les règles de l'automate.
     * 
     * @return Une chaîne de caractères représentant les règles de l'automate.
     */
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
    
    /**
     * Charge les règles d'un fichier.
     * 
     * @param fichier Le fichier contenant les règles.
     * @return Vrai si les règles sont chargées avec succès, faux sinon.
     */
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
    
    /**
     * Sauvegarde les règles dans un fichier.
     * 
     * @param fichier Le fichier dans lequel sauvegarder les règles.
     * @return Vrai si les règles sont sauvegardées avec succès, faux sinon.
     */
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

    /**
     * Vérifie si une variable est définie dans les règles de l'automate.
     * 
     * @param nom Le nom de la variable à vérifier.
     * @return Vrai si la variable est définie, faux sinon.
     */
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

    /**
     * Définit une variable dans les règles de l'automate.
     * 
     * @param nom Le nom de la variable à définir.
     * @param val La valeur de la variable.
     * @return Vrai si la variable est définie avec succès, faux sinon.
     */
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

    /**
     * Récupère la valeur d'une variable définie dans les règles de l'automate.
     * 
     * @param nom Le nom de la variable.
     * @return La valeur de la variable.
     */
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

    /**
     * Renvoie la liste des noms de toutes les variables définies dans les règles de l'automate.
     * 
     * @return Un tableau contenant les noms des variables.
     */
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

    /**
     * Renvoie le nombre de variables définies dans les règles de l'automate.
     * 
     * @return Le nombre de variables.
     */
    public int getNbVars () {
        if (variables==null) {
            return 0;
        }
        return variables.length;
    }

    /**
     * Simplifie une expression en retirant les espaces et les retours à la ligne.
     * 
     * @param exp L'expression à simplifier.
     * @return L'expression simplifiée.
     */
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

    /**
     * Renvoie vrai si les règles sont valides, faux sinon.
     * 
     * @return Vrai si les règles sont valides, faux sinon.
     */
    public String getErreur () {
        return erreur;
    }

    /**
     * Renvoie vrai si les règles sont valides, faux sinon.
     * 
     * @return Vrai si les règles sont valides, faux sinon.
     */
    public int getDim () {
        return dim;
    }
    
}