package src;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;

public class Tableau {
    private int dim;
    private int taille;
    private Object [] tab;
    
    //d la dimension du tableau et t la taille
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

    public Tableau (String fichier) {
        if (!charger(fichier)) {
            System.out.println("creation impossible : Impossible de lire le fichier");
        }
    }

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
    
    public int getDim () {
        return dim;
    }
    
    public int getTaille () {
        return taille;
    }
    
    //Les arguments sont des indices
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
    
    //Les premiers arguments sont les indices et le dernier est la valeur
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

    public void afficher (boolean entier) {
        int [] indices= new int [dim];
        while (indices[dim-1]<taille) {
            for (int i=dim-1;i>=0;i--) {
                if (indices[i]>=taille) {
                    indices[i]=0;
                    System.out.print("\r\n");
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
    }

}