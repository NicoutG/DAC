# DAC

## Introduction

Le **DAC** (Définition d'Automates Cellulaires) est un language qui permet comme son nom l'indique de définir des automates cellulaires. Il permet notamment de définir les voisins et les règles appliquées sur chaque cellules d'un tableau (un tableau de dimension n).
Ce projet permet d'interpréter ce langage.

## Contributeurs 

- ANTOINE Maxime : Site web du DAC
- GALLET Nicolas : Langage DAC et interpréteur
- GINHAC Jules : Documentation du code

## Mentions

Site web du DAC : http://dac.poly-api.fr

## Fichiers et Dossiers

### src
    - L'ensemble du code java necessaire à l'interprétation du DAC.
    - Main.java : Divers exemples d'utilisations du DAC.

### doc
    - Tuto_DAC.md : Le tuto pour écrire en DAC.
    - Diagramme_DAC : Le diagramme des classes de l'interpréteur du DAC.
![Diagramme des classes](https://github.com/NiCoutG/DAC/blob/main/doc/Diagramme_DAC.png?raw=true)

### data
#### dac
    - Des exemples de codes écrits en DAC.

#### tableaux
    - Des exemples de tableaux enregistrés.

## Mise en place

Afin de pouvoir interpréter le DAC et appliquer les règles, 2 classes sont nécessaires :

    - Tableau : Permet de créer un tableau de dimension n sur lequel on peut appliquer des règles.
    - Regles : Permet d'interpréter le DAC et d'appliquer la règle sur un tableau.


