## TODO

- Ajouter Lombok ?

## Données du blog

### Initialisation des données

    $ docker run -p 9200:9200 elasticsearch:2.3.3
    $ cd src/main/resources
    $ curl -XPUT 192.168.99.100:9200/xebia/blog/_bulk --data-binary @xebiablog.json

### Recherche à implémenter

- Recherche sur l'auteur (en mode multi-critère avec mapping différent)
- Recherche sur le titre
- Recherche avec filtre et sans filtre (le filtre est mis en cache et est restrictif, attention il faut utiliser le filtered-query !)
    - Filtre avec les catégories
- Search as you type (ou API suggestion)

En Jest montrer les 2 possibilités

- Utilisation du client elasticsearch
- Copier coller d'une requête Json


## Description des attributs pour les données VCUB :


| Champ    | Description                      | Type                   |
|----------|----------------------------------|------------------------|
| GID      | Clé primaire                     | [Nombre entier]        |
| NUMSTAT  | Numéro de le station             | [Chaîne de caractères] |
| IDENT    | Identifiant                      | [Chaîne de caractères] |
| ADRESSE  | Adresse                          | [Chaîne de caractères] |
| COMMUNE  | Commune                          | [Chaîne de caractères] |
| DATESERV | Date de mise en service          | [Chaîne de caractères] |
| LIGNCORR | Correspondance de ligne          | [Chaîne de caractères] |
| NBSUPPOR | Nombre de supports               | [Chaîne de caractères] |
| NOM      | Nom de la station                | [Chaîne de caractères] |
| TARIF    | Tarification                     | [Chaîne de caractères] |
| TERMBANC | Terminal bancaire                | [Chaîne de caractères] |
| TYPEA    | Type de station                  | [Chaîne de caractères] |
| GEOM     | Géométrie de l'objet             | [Géométrie]            |
| GEOM_ERR | Code d'erreur géométrique Oracle | [Chaîne de caractères] |
| GEOM_O   | Angle de l'objet                 | [Nombre réel]          |
| CDATE    | Date de création de l'objet      | [Date]                 |
| MDATE    | Date de modification de l'objet  | [Date]                 |

http://data.bordeaux-metropole.fr/dicopub/#TB_STVEL_P

SIG - CUB suivi de l'année d'extraction des données. Ex : SIG - CUB 2013


### Pour initialiser les données

    $ docker run -p 9200:9200 elasticsearch:2.3.3

Et exécuter le main InitVcubData.java