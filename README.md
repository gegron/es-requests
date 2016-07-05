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


To init data :

docker run -p 9200:9200 elasticsearch:2.3.3
launch InitVcubData main class