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
     * Créé l'arbre d'execution d'une condition
     * @param exp l'expression de la condition à interpréter
     * @param num le numéro de la condition
     * @return boolean si la condition a bien été interprétée
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
     * Créé l'arbre d'execution d'une action
     * @param exp l'expression de l'action à interpréter
     * @param num le numéro de l'action
     * @return boolean si l'action a bien été interprété
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
     * Créé les arbres d'executions des conditions et des actions
     * @param exp l'expression des conditions et actions à interpréter
     * @return boolean si les conditions et les actions ont bien été interprétés
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
     * Stock les indices des voisins dans un tableau
     * @param exp l'expression des conditions et actions à interpréter
     * @return boolean si les coordonnées des voisins ont bien été interprétées
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
     * Ajoute une variable à la liste des variables
     * @param nom le nom de la variable à ajouter
     * @return boolean si la variable n'êtait pas déjà dans la liste
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
     * Recupère l'ensemble des variables présentes dans le code DAC 
     * @param exp l'expression à interpréter
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
     * Retire les commentaires de l'expression
     * @param exp l'expression à interpréter
     * @return String l'expression sans commentaires
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
     * Renvoi la nouvelle valeur de la cellule après application de la règle
     * @param tab le tableau sur lequel on applique la règle
     * @param indices les indices de la cellule du tableau sur laquelle on applique la règle
     * @return double la nouvelle valeur de la cellule après application de la règle
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
     * Interprète un code DAC pour créer les arbres d'executions
     * @param exp l'expression DAC à interpréter
     * @return boolean si l'expression a bien été interprétée
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
     * Applique la règle sur un tableau pour générer un nouveau tableau
     * @param tab le tableau sur lequel on applique la règle
     * @return Tableau le tableau généré après application de la règle
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
     * Renvoi l'expression DAC qui a été interprétée par la classe
     * @return String l'expression interprétée par la classe
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
     * Charge une expression DAC depuis un fichier .dac
     * @param fichier le nom du fichier à charger
     * @return boolean si le fichier a bien été interprété
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
     * Sauvegarde l'expression DAC qui a été interprétée par la classe
     * @param fichier le nom du fichier dans lequel sauvegarder l'expression
     * @return boolean si l'expression a bien été sauvegardée
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
     * Verifie si un nom correspond à celui d'une variable de l'expression
     * @param nom le nom de la variable que l'on cherche
     * @return boolean si le nom correspond à celui d'une variable de l'expression
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
     * Assigne une valeur dans une variable de l'expression
     * @param nom le nom de la variable que l'on souhaite modifier
     * @param val la valeur que l'on assigne à la variable
     * @return boolean si le nom correspond à celui d'une variable de l'expression
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
     * Renvoi la valeur d'une variable de l'expression
     * @param nom le nom de la variable dont on veut connaitre la valeur
     * @return double la valeur de la variable
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
     * Renvoi l'ensemble des noms des varaibles de l'expression
     * @return String [] les noms de variables
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
     * Renvoi le nombre de variables presentes dans l'expression
     * @return int le nombre de variables
     */
    public int getNbVars () {
        if (variables==null) {
            return 0;
        }
        return variables.length;
    }

    /**
     * Retire l'indentation de l'expression
     * @param exp l'expression du DAC
     * @return String l'expression du DAC sans indentation
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
     * Renvoi un message expliquant la potentielle erreur d'interprétation
     * @return String l'erreur rencontrée
     */
    public String getErreur () {
        return erreur;
    }

    /**
     * Renvoi la dimension des tableaux sur lesquelles la règles peut s'appliquer
     * @return int la dimension de la règle
     */
    public int getDim () {
        return dim;
    }
    
}