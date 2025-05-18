# Projet algo 2: Réseau de transport

## Description générale

## Table des matières

1. [Description générale](#description-générale)
2. [Auteurs](#auteurs)
3. [Compilation](#compilation)
4. [Exécution](#exécution)

## Auteurs

| Prénom  | Nom          | Matricule |
| ------  | ------------ | --------- |
| Romain  | Liefferinckx | 000591790 |
| Manuel  | Rocca        | 000596086 |

## Compilation

```sh
Etre a la racine du projet et faire "make" pour compiler.
```

## Exécution

```sh
GTFS doit être a la racine du projet.
```

```sh
Etre a la racine du projet et faire "make run" pour exécuter le programme.
```

### Exemple type

```sh
# Exemple type d'utilisation du programme:
# 1er input: Nom de la station de départ
Aubange

# 2ème input: Nom de la station d'arrivée
DELTA

# 3ème input: Heure de départ au format HH;MM;SS
08;00;00

# 4ème input: Choix du mode (default/variant)
default -> pour le mode par défaut et le programme se lance directement
sinon -> une liste d'input va vous etre demandée afin de savoir quels moyen de transport voulez vous utiliser.

# Si on veut refaire une recherche:
# Entrer n'importe quel caractère lorsque le programme demande:
Would you like to search for another itinerary? (no to exit):
# Sinon, entrer "no" pour quitter.
```
