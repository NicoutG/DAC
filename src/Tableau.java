package src;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;

/**
 * Classe représentant un tableau pour un automate cellulaire.
 * Le tableau peut être multidimensionnel et contient des données numériques.
 */
public class Tableau {

    /**
     * La dimension du tableau.
     */
    private int dim;

    /**
     * La taille du tableau.
     */
    private int taille;

    /**
     * Le tableau de données.
     */
    private Object [] tab;
    
    /**
     * Constructeur pour créer un tableau de dimension et de taille spécifiées.
     * 
     * @param d La dimension du tableau.
     * @param t La taille de chaque dimension du tableau.
     */
    public Tableau (int d, int t) {
        if (d>0 && t>0) {
            dim=d;
            taille=t;
            tab=new Object [taille];
            if (dim>1) {
                for (int i=0;i<taille;i++) {
                    tab[i]=new Tableau(dim-1,taille);
                }
            }
            else {
                for (int i=0;i<taille;i++) {
                    tab[i]=0.0;
                }
            }
        }
        else {
            System.out.println("creation impossible : La dimension et la taille doivent etre superieur à 0");
            }
    }

    /**
     * Constructeur pour charger un tableau à partir d'un fichier.
     * 
     * @param fichier Le chemin du fichier à charger.
     */
    public Tableau (String fichier) {
        if (!charger(fichier)) {
            System.out.println("creation impossible : Impossible de lire le fichier");
        }
    }

    /**
     * Simplifie une chaîne de caractères en supprimant les espaces et retours à la ligne.
     * 
     * @param exp La chaîne de caractères à simplifier.
     * @return Un tableau de chaînes de caractères simplifiées.
     */
    private String [] simplification (String exp) {
        String [] exps=exp.split("\r?\n|\r");
        exp="";
        for (int i=0;i<exps.length;i++) {
            exp+=exps[i]+" ";
        }
        exps=exp.split(" ");
        int t=0;
        for (int i=0;i<exps.length;i++) {
            if (!exps[i].equals("")) {
                t++;
            }
        }
        if (t!=exps.length) {
            String [] exps2=new String [t];
            int indice=0;
            for (int i=0;i<exps.length;i++) {
                if (!exps[i].equals("")) {
                    exps2[indice]=exps[i];
                    indice++;
                }
            }
            exps=exps2;
        }
        return exps;
    }

    /**
     * Charge les données du tableau à partir d'un fichier.
     * 
     * @param fichier Le chemin du fichier à charger.
     * @return Vrai si le chargement réussit, faux sinon.
     */
    public boolean charger (String fichier) {
        try {
            String exp=new String(Files.readAllBytes(Paths.get(fichier)));
            String [] exps=simplification(exp);
            if (exps==null || exps.length<2) {
                return false;
            }
            dim=Integer.parseInt(exps[1]);
            taille=Integer.parseInt(exps[3]);
            tab=new Object [taille];
            if (dim>1) {
                for (int i=0;i<taille;i++) {
                    tab[i]=new Tableau(dim-1,taille);
                }
            }
            else {
                for (int i=0;i<taille;i++) {
                    tab[i]=0.0;
                }
            }
            if (exps.length!=4+Math.pow(taille,dim)) {
                return false;
            }
            int [] indices= new int [dim];
            int num=4;
            while (indices[dim-1]<taille) {
                for (int i=dim-1;i>=0;i--) {
                    if (indices[i]>=taille) {
                        indices[i]=0;
                    }
                }
                setVal(indices,Double.valueOf(exps[num]));
                num++;
                indices[0]++;
                for (int i=0;i<dim-1;i++) {
                    if (indices[i]>=taille) {
                        indices[i+1]++;
                    }
                }
            }
            return true;
        }
        catch (IOException e) {
            return false;
        }
    }

    /**
     * Sauvegarde les données du tableau dans un fichier.
     * 
     * @param fichier Le chemin du fichier où sauvegarder.
     * @return Vrai si la sauvegarde réussit, faux sinon.
     */
    public boolean sauvegarder (String fichier) {
        try {
            String exp=new String ();
            exp="Dim: "+dim+" Taille: "+taille+"\r\n";
            int [] indices= new int [dim];
            while (indices[dim-1]<taille) {
                for (int i=dim-1;i>=0;i--) {
                    if (indices[i]>=taille) {
                        indices[i]=0;
                        exp+="\r\n";
                    }
                }
                exp+=getVal(indices)+" ";
                indices[0]++;
                for (int i=0;i<dim-1;i++) {
                    if (indices[i]>=taille) {
                        indices[i+1]++;
                    }
                }
            }
            Files.write(Paths.get(fichier), exp.getBytes());
            return true;
        } catch (IOException e) {
            return false;
        }
    }

     /**
     * Remplit le tableau avec une valeur spécifiée, jusqu'à un nombre donné de cellules.
     * 
     * @param nb Le nombre de cellules à remplir.
     * @param val La valeur à utiliser pour remplir.
     */
    public void remplir (int nb, float val) {
        int [] indices= new int [dim];
        ArrayList <int []> libres=new ArrayList <int []> ();
        while (indices[dim-1]<taille) {
            for (int i=dim-1;i>=0;i--) {
                if (indices[i]>=taille) {
                    indices[i]=0;
                }
            }
            if (getVal(indices)!=val) {
                int [] add=new int [dim];
                for (int i=0;i<dim;i++) {
                    add[i]=indices[i];
                }
                libres.add(add);
            }
            indices[0]++;
            for (int i=0;i<dim-1;i++) {
                if (indices[i]>=taille) {
                    indices[i+1]++;
                }
            }
        }
        if (nb>=libres.size()) {
            for (int i=0;i<libres.size();i++) {
                setVal(libres.get(i),val);
            }
        }
        else {
            Random random = new Random();
            int hasard;
            for (int i=0;i<nb;i++) {
                hasard=random.nextInt(libres.size());
                setVal(libres.get(hasard),val);
                libres.remove(hasard);
            }
        }
    }

    /**
     * Initialise le tableau avec des valeurs aléatoires dans une plage donnée.
     * 
     * @param min La valeur minimale (incluse) pour l'aléatoire.
     * @param max La valeur maximale (incluse) pour l'aléatoire.
     */
    public void intialiserAleatoirement (int min, int max) {
        int [] indices= new int [dim];
        Random random = new Random();
        while (indices[dim-1]<taille) {
            for (int i=dim-1;i>=0;i--) {
                if (indices[i]>=taille) {
                    indices[i]=0;
                }
            }
            setVal(indices,random.nextInt(min,max+1));
            indices[0]++;
            for (int i=0;i<dim-1;i++) {
                if (indices[i]>=taille) {
                    indices[i+1]++;
                }
            }
        }
    }
    
    /**
     * Renvoie la dimension du tableau.
     * 
     * @return La dimension du tableau.
     */
    public int getDim () {
        return dim;
    }
    
    /**
     * Renvoie la taille du tableau.
     * 
     * @return La taille du tableau.
     */
    public int getTaille () {
        return taille;
    }
    
    /**
     * Récupère la valeur d'une cellule spécifiée par ses indices.
     * 
     * @param args Les indices de la cellule dans chaque dimension.
     * @return La valeur de la cellule.
     */
    public double getVal (int... args) {
        if (args.length==dim) {
            int arg0=args[0];
            while (arg0<0) {
                arg0+=taille;
            }
            arg0=arg0%taille;
            if (tab[arg0] instanceof Tableau) {
                int [] args2=new int [args.length-1];
                for (int i=0;i<args.length-1;i++) {
                    args2[i]=args[i+1];
                }
                return ((Tableau)tab[arg0]).getVal(args2);
            }
            return (double)tab[arg0];
        }
        System.out.println("get impossible : "+dim+" parametres attendus");
        return -1;
    }
    
    /**
     * Modifie la valeur d'une cellule spécifiée par ses indices.
     * 
     * @param args Les indices de la cellule dans chaque dimension.
     * @param val La nouvelle valeur de la cellule.
     * @return Vrai si la modification réussit, faux sinon.
     */
    public boolean setVal (int [] args, double val) {
        if (args.length==dim) {
            int arg0=args[0];
            while (arg0<0) {
                arg0+=taille;
            }
            arg0=arg0%taille;
            if (args.length==1) {
                tab[arg0]=val;
                return true;
            }
            else {
                int [] args2=new int [args.length-1];
                for (int i=0;i<args.length-1;i++) {
                    args2[i]=args[i+1];
                }
                return ((Tableau)tab[arg0]).setVal(args2,val);
            }
        }
        System.out.println("set impossible : "+dim+" int attendus dans le tableau");
        return false;
    }
    
    /**
     * Définit la valeur d'une cellule spécifiée par ses indices.
     * 
     * @param args Les indices de la cellule suivis de la valeur à assigner.
     * @return Vrai si la valeur a été assignée avec succès, faux sinon.
     */
    public boolean setVal (double... args) {
        if (args!=null && args.length==dim+1) {
            double val=args[dim];
            int [] indices=new int [dim];
            for (int i=0;i<dim;i++) {
                indices[i]=(int)args[i];
            }
            return setVal(indices,val);
        }
        System.out.println("set impossible : "+(dim+1)+" parametres attendus");
        return false;
    }

    /**
     * Affiche le contenu du tableau.
     * 
     * @param entier Si vrai, affiche les valeurs en tant qu'entiers, sinon en tant que doubles.
     */
    public void afficher (boolean entier) {
        int [] indices= new int [dim];
        while (indices[dim-1]<taille) {
            for (int i=dim-1;i>=0;i--) {
                if (indices[i]>=taille) {
                    indices[i]=0;
                    System.out.println();
                }
            }
            if (entier) {
                System.out.print((int)getVal(indices)+" ");
            }
            else {
                System.out.print(getVal(indices)+" ");
            }
            indices[0]++;
            for (int i=0;i<dim-1;i++) {
                if (indices[i]>=taille) {
                    indices[i+1]++;
                }
            }
        }
        System.out.println();
    }

    /**
     * Renvoie la valeur maximale présente dans le tableau.
     * 
     * @return La valeur du maximum.
     */
    public double maximum () {
        int [] indices= new int [dim];
        double maximum=getVal(indices);
        double val;
        while (indices[dim-1]<taille) {
            for (int i=dim-1;i>=0;i--) {
                if (indices[i]>=taille) {
                    indices[i]=0;
                }
            }
            val=getVal(indices);
            if (val>maximum) {
                maximum=val;
            }
            indices[0]++;
            for (int i=0;i<dim-1;i++) {
                if (indices[i]>=taille) {
                    indices[i+1]++;
                }
            }
        }
        return maximum;
    }

    /**
     * Renvoie la valeur minimale présente dans le tableau.
     * 
     * @return La valeur du minimum.
     */
    public double minimum () {
        int [] indices= new int [dim];
        double minimum=getVal(indices);
        double val;
        while (indices[dim-1]<taille) {
            for (int i=dim-1;i>=0;i--) {
                if (indices[i]>=taille) {
                    indices[i]=0;
                }
            }
            val=getVal(indices);
            if (val<minimum) {
                minimum=val;
            }
            indices[0]++;
            for (int i=0;i<dim-1;i++) {
                if (indices[i]>=taille) {
                    indices[i+1]++;
                }
            }
        }
        return minimum;
    }

    /**
     * Renvoie la moyenne des valeurs du tableau.
     * 
     * @return La moyenne des valeurs du tableau.
     */
    public double moyenne () {
        int [] indices= new int [dim];
        double somme=0;
        while (indices[dim-1]<taille) {
            for (int i=dim-1;i>=0;i--) {
                if (indices[i]>=taille) {
                    indices[i]=0;
                }
            }
            somme+=getVal(indices);
            indices[0]++;
            for (int i=0;i<dim-1;i++) {
                if (indices[i]>=taille) {
                    indices[i+1]++;
                }
            }
        }
        return somme/Math.pow(taille,dim);
    }

    /**
     * Renvoie le nombre d'occurences d'une valeur dans le tableau.
     * 
     * @param val La valeur dont on retourne le nombre d'occurences.
     * @return Le nombre d'occurence de la valeur dans le tableau.
     */
    public int count (double val) {
        int [] indices= new int [dim];
        int oc=0;
        while (indices[dim-1]<taille) {
            for (int i=dim-1;i>=0;i--) {
                if (indices[i]>=taille) {
                    indices[i]=0;
                }
            }
            if (val==getVal(indices)) {
                oc++;
            }
            indices[0]++;
            for (int i=0;i<dim-1;i++) {
                if (indices[i]>=taille) {
                    indices[i+1]++;
                }
            }
        }
        return oc;
    }

}