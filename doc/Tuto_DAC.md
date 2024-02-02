# DAC

## Introduction

Le **DAC** (Définition d'Automates Cellulaires) est un language qui permet comme son nom l'indique de définir des automates cellulaires. Il permet notamment de définir les voisins et les règles appliquées sur chaque cellules de l'automate (un tableau de dimension n).

Voici un exemple de code écrit en DAC :
```
0, -1;
0, 1;
-1, 0;
1, 0;
@

#(2)==-1 {
    $var_Exe4:3+#(2); 
    0.9:2;
}
sin(#(4))<=0.5 && (#(0)>8 || #(0)<val(0,1)) {  /* Ceci est un commentaire */
    coord(1)==4 {
        2:majority()-1;
    }
    #(3)<=count(#(0)) {
        1:4*verif(average()!=1);
    }
}
```

## Documentation

### Généralités

Il est important de noter que la mise en forme n'est pas importante en DAC.
On peut tout écrire sur une ligne mais la mise en forme facilite la compréhension du code.
Les caracteres `/*` et `*/` permettent d'écrire des commentaires qui sont ignorés lors la compilation du fichier DAC.

Par exemple, on peut écrire :
`/* Ceci est un commentaire */`


### Définition des voisins

Un fichier DAC commence toujours par la définition des voisins. 

- Pour définir un voisin, on écrit ses coordonnées relatives à la cellule sur laquelle la règle est appliquée.
- Les coordonnées sont des entiers et sont séparés par le caractère : `,`.
- Tous les voisins doivent avoir le même nombre de coordonnées.
- Ce nombre de coordonnées définit la dimension des tableaux sur lesquels peuvent s'appliquer ces règles.

**Il est donc indispensable de définir au moins 1 voisin même si on ne l'utilise pas.**

- La définition d'un voisin se finit par le caractère `;`.
- La fin de la définition des voisins est marquée par le caractère `@`.

Voici un exemple de définition de voisins :
 ```
0, -1;
0, 1;
-1, 0;
1, 0;
@
```
- 4 voisins définis
- s'applique sur des tableaux de dimensions 2 (car deux coordonnées pour chaque voisin)
- les voisins sont : la cellule du dessus, du dessous, de gauche et de droite 


### Les types de valeurs

La partie suivante du code DAC utilise plusieurs types de valeurs.

##### Le type immediat

Il s'agit d'un simple réel.

Exemple ci-dessous :
- -1
- 0.5
- 0.9
- 2
- 4
- 8

##### Le type variable

Le type variable est défini par le caractère `$`.
Ce qui suit `$` est une chaine de caracteres composée de lettres, de chiffres et de `_` (underscore).

Les variables valent 0 par défaut et peuvent contenir des réels.

Les valeurs des variables peuvent être modifiés après la définition des règles.

Exemple ci-dessous :
`$var_Exe4` : vaut 0 mais peut être modifié après la définition de la règle
    
##### Le type fonction

Le type fonction est défini par un mot et des paramètres définis entre parenthèses.
Rien n'empêche de créer une variable du même nom.
Ce type permet de renvoyer un réel.

Voici les différentes fonctions possibles :
- `maximum` : Prend 0 paramètres et renvoie la valeur maximale parmi les voisins
- `minimum` : Prend 0 paramètres et renvoie la valeur minimale parmi les voisins
- `majority` : Prend 0 paramètres et renvoie la valeur la plus présente parmi les voisins
- `minority` : Prend 0 paramètres et renvoie la valeur la moins présente parmi les voisins
- `average` : Prend 0 paramètres et renvoie la moyenne des valeurs des voisins
- `median` : Prend 0 paramètres et renvoie la médiane des valeurs des voisins
- `sum` : Prend 0 paramètres et renvoie la somme des valeurs des voisins
- `length` : Prend 0 paramètres et renvoie la taille du tableau
- `verif` : Prend 1 paramètre de type Condition et renvoie 1 si elle est validée, 0 sinon.
- `count` : Prend 1 paramètre de type Valeur et renvoie le nombre de voisins ayant cette va0leur.
- `#` : Prend 1 paramètre de type entier et renvoie la valeur contenue dans le i eme voisin. (0 étant la cellule actuelle).
- `cos` : Prend 1 paramètre de type Valeur et renvoie le cosinus en degrés de cette valeur.
- `sin` : Prend 1 paramètre de type Valeur et renvoie le sinus en degrés de cette valeur.
- `tan` : Prend 1 paramètre de type Valeur et renvoie la tangente en degrés de cette valeur.
- `exp` : Prend 1 paramètre de type Valeur et renvoie l'exponentielle de cette valeur.
- `ln` : Prend 1 paramètre de type Valeur et renvoie le logarithme de cette valeur.
- `rand`. Prend 1 paramètre de type entier et renvoi un entier supérieur ou égal à 0 et inférieur strictement à cet entier.
- `coord`. Prend 1 paramètre de type entier et renvoi la i ème coordonnée de la cellule.
- `int`. Prend 1 paramètre de type Valeur et renvoie la partie entière de cette valeur.
- `max`. Prend 2 paramètre de types Valeur et renvoie la valeur la plus grande.
- `min`. Prend 2 paramètre de types Valeur et renvoie la valeur la plus petite.
- `val`. Prend autant de paramètres de types Valeur que la dimensions des tableaux sur lesquels la règle s'applique et renvoi la valeur contenue dans la cellule à ces coordonnées.
        
Par exemple :
- `1:verif(average()!=1);` Dans ce code, `average()` renvoie la moyenne des valeurs des voisins.
- `2:majority()-1;` Dans ce code, `majority()` renvoie la valeur la plus présente parmi les voisins.
- `#(0)` renvoie la valeur contenue dans la cellule sur laquelle la règle est appliquée.
- `#(2)` renvoie la valeur contenue dans la cellule de coordonnées 0, 1 relativement à la cellule sur laquelle la règle est appliquée.
- `#(3)` renvoie la valeur contenue dans la cellule de coordonnées -1, 0 relativement à la cellule sur laquelle la règle est appliquée.
- `#(4)` renvoie la valeur contenue dans la cellule de coordonnées 1, 0 relativement à la cellule sur laquelle la règle est appliquée.
- `verif(average()!=1)` renvoie `1` si `average()!=0`, `0` sinon.
- `count(#(0))` renvoie le nombre de voisins dont leur valeur est `#(0)`.
- `coord(1)` renvoie la première coordonnée de la cellule sur laquelle la règle est appliquée.
- `sin(#(4))` renvoie le sinus de `#(4)`.
- `val(0,1)` Prend 2 paramètres car la règle s'applique sur des tableaux de dimension 2 et renvoie la valeur contenue dans la cellule aux coordonnées 0,1.


##### Les types opérateur binaire

Les types opérateur binaire sont les résultats d'opérations arithmetiques.
- **Addition** défini par `+`. Prend une valeur à sa gauche et à sa droite.
- **Soustraction** défini par `-`. Prend une valeur à sa gauche et à sa droite.
- **Modulo** défini par `%`. Prend une valeur à sa gauche et à sa droite.
- **Multiplication** défini par `*`. Prend une valeur à sa gauche et à sa droite.
- **Division** défini par `/`. Prend une valeur à sa gauche et à sa droite.
- **Puissance** défini par `^`. Prend une valeur à sa gauche et à sa droite.
        
Par exemple :
- `4*verif(average()!=1)` renvoie le produit de `4` et `verif(average()!=1)`.
- `3+#(2)` renvoie l'addition de `3` et `#(2)`.
- `majority()-1` renvoie la soustraction de `majority()` par `1`.


### Définition des règles

Après avoir défini les voisins, on peut désormais définir les règles.
Ces règles sont une succession de blocs.
Lorsque la condition d'un bloc est validée, on parcourt les blocs ou on execute l'action qu'elle contient.
Si aucune action n'est réalisée à la fin du code, la cellule garde la même valeur.

#### Les blocs

Chaque bloc est défini par une accolade ouvrante `{` et une accolade fermante `}`.
De plus, chaque bloc est précédé d'une condition qui permet de définir quand le bloc doit être parcouru.

Un bloc contient toujours soit une action, soit d'autres blocs.

Par exemple :
- `#(2)==-1 {}` contient l'action `$var_Exe4:3+#(2); 0.9:2;` cette action est executé seulement si `#(2)==-1`.
- `sin(#(4))<=0.5 && (#(0)>8 || #(0)<val(0,1)) {}` contient 2 blocs qui seront parcouru seulement si aucune action n'a encore été executée et que `sin(#(4))<=0.5 && (#(0)>8 || #(0)<val(0,1))`.
- `coord(1)==4 {}` contient l'action `2:majority()-1;` cette action est executé seulement si aucune action n'a encore été executée, que `sin(#(4))<=0.5 && (#(0)>8 || #(0)<val(0,1))` et que `coord(1)==4`.
- `#(3)<=count(#(0)) {}` contient l'action `1:4*verif(average()!=1);` cette action est executé seulement si aucune action n'a encore été executée, que `sin(#(4))<=0.5 && (#(0)>8 || #(0)<val(0,1))` et que `#(3)<=count(#(0))`.

##### Les conditions

La condition est testée de gauche à droite et prend en compte le parenthésage.

Une condition peut être composée de plusieurs opérateurs conditionnels :

- **Et** défini par `&&`. Prend une condition à sa gauche et à sa droite.
- **Ou** défini par `||`. Prend une condition à sa gauche et à sa droite.
- **Non** défini par `!`. Prend une condition à sa droite.
- **Egal** défini par `==`. Prend une valeur à sa gauche et à sa droite.
- **Different** défini par `!=`. Prend une valeur à sa gauche et à sa droite.
- **Inférieur** défini par `<`. Prend une valeur à sa gauche et à sa droite.
- **Supérieur** défini par `>`. Prend une valeur à sa gauche et à sa droite.
- **Inférieur ou égal** défini par `<=`. Prend une valeur à sa gauche et à sa droite.
- **Supérieur ou égal** défini par `>=`. Prend une valeur à sa gauche et à sa droite.
        
Par exemple :
- `#(2)==-1` est valide si `#(2)` est égal à -1.
- `sin(#(4))<=0.5 && (#(0)>8 || #(0)<val(0,1))` est valide si `sin(#(4))` est inférieur à 0.5 et que `#(0)` n'est pas compris entre 4 et val(0,1).
- `coord(1)==4` est valide si `coord(1)` vaut 4.
- `#(3)<=count(#(0))` est valide si `#(3)` est inférieur ou égal à `count(#(0))`.
    
##### Les actions

Une action se trouve toujours dans un bloc.
Si la condition du dessus est valide alors on exécute l'action.

Une action peut être composée de plusieurs instructions.

##### Les instructions

Une instruction se finit par le caractère `;` et commence par un coefficient qui représente le poids de l'instruction au sein de l'action.

- Ce coefficient est une valeur.
- Si la valeur est négative, la valeur 0 est appliquée.
- Le coefficient est suivi par `:` puis d'une valeur.

Lors de l'exécution d'une instruction, on assigne à la cellule sur laquelle la règle est appliquée la valeur définie.
        
Lors de l'exécution d'une action, on exécute une seule instruction prise au hasard en fonction de son coefficient.

Plus son coefficient est élevé, plus l'instruction a de chances d'être exécutée.

Prenons un exemple simple : `$x:1, $y:2`. Ce code signifie qu'il y a une probabilité de `$x/($x+$y)` que la cellule sur laquelle la règle s'applique prenne la valeur 1 et une probabilité de `$y/($x+$y)` qu'elle prenne la valeur 2.

Maintenant un exemple plus poussé, reprenons le code de l'introduction :
 ```
0, -1;
0, 1;
-1, 0;
1, 0;
@

#(2)==-1 {
    $var_Exe4:3+#(2); 
    0.9:2;
}
sin(#(4))<=0.5 && (#(0)>8 || #(0)<val(0,1)) {  /* Ceci est un commentaire */
    coord(1)==4 {
        2:majority()-1;
    }
    #(3)<=count(#(0)) {
        1:4*verif(average()!=1);
    }
}
```

- Si `#(2)==-1` est valide, on execute `$var_Exe4:3+#(2); 0.9:2;`;
- La première instruction a une probabilité de `$var_Exe4/($var_Exe4+0.9)` d'être exécutée.
On assignerait alors la valeur de `3+#(2)` à la cellule sur laquelle la règle est appliquée.
- La deuxième instruction a une probabilité de `0.9/($var_Exe4+0.9)` d'être exécutée.
On assignerait alors la valeur 2 à la cellule sur laquelle la règle est appliquée.
- Si `sin(#(4))<=0.5 && (#(0)>8 || #(0)<val(0,1))` et `coord(1)==4` sont valides, on execute `2:majority()-1;`
- L'instruction a une probabilité de `2/2` = 100% de chances d'être exécutée.
On assignerait alors la valeur de `majority()-1` à la cellule sur laquelle la règle est appliquée.
- Si `sin(#(4))<=0.5 && (#(0)>8 || #(0)<val(0,1))` et `#(3)<=count(#(0))` sont valides, on execute `1:4*verif(average()!=1);`
- L'instruction a une probabilité de `1/1` = 100% de chances d'être exécutée.
On assignerait alors la valeur de `4*verif(average()!=1)` à la cellule sur laquelle la règle est appliquée.


Vous avez désormais toutes les clés pour définir vos propres automates cellulaires.


