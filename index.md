---
css: stylesheets/stylesheet.css
js: javascripts/main.js
layout: main
---

### 1. Préparation de votre environnement
Pour réaliser les différentes étapes de ce hands-on, vous avez besoin d'un elasticsearch.  
 Plusieurs choix s'offre à vous :  
* __Choix numéro 1 : Utiliser Elasticsearch via Docker :__  
`docker run -p 9200:9200 -p 5601:5601 ibeauvais/elasticsearch-kibana`
    

Vous pouvez ensuite accéder à elasticsearch sur [http://localhost:9200/](http://localhost:9200/) et
sense sur [http://localhost:5601/app/sense](http://localhost:5601/app/sense)  
(si vous utilisez docker sur une vm ou via docker-machine remplacer localhost par l'ip de la vm)


* __Choix numéro 2 : Utiliser l'Elasticsearch mis à votre disposition (partagé par tout le monde)__  

Vous pouvez ensuite accéder à elasticsearch sur [http://ip:9200/](http://ip:9200/) et sense sur [http://ip:5601/app/sense](http://ip:5601/app/sense)  
Comme cet elasticsearch est utilisé par plusieurs personnes, veuillez préfixer le nom de vos indexes par
votre nom lors des différentes requêtes de l'exercice.  
Exemple : Au lieu de faire GET http://ip:9200/myIndex/_search, je fais GET
            http://ip:9200/beauvais-myIndex/_search

Pour effectuer des requêtes, vous allez utiliser l'api REST. Pour cela plusieurs choix également :  

* __Utiliser sense :__ application kibana avec auto-complétion et formatage des requêtes  
* __Utiliser cUrl__ 
* __le client http de votre choix__ 

 ---
 
### 2. Découverte de l'api
###### Attention,  pour cette partie, si vous utilisez le elasticsearch en ligne n'oubliez pas de changer le nom d'index __'programmer'__ en __'votre-nom-programmer'__ ######   

   __2.1 Premier document indexé :__    
Requête __POST__ programmer/person/1
{% highlight json %}
{
    "name": "Lovelace",
    "firstname": "Ada",
    "birthday" : "1815-12-10"
}
{% endhighlight %}   
  
---  
   
- **programmer** est le nom de l'indexe
- **person** est le type de document
- **1** est l'id   
   
  __2.2 Retrouver le document par son id :__  
Requête __GET__ programmer/person/1  
    
  __2.3 Indexer d'autres documents (Avec les id 2 et 3):__  
{% highlight json %}
{
    "name": "Gosling",
    "firstname": "James",
    "birthday" : "1954-05-19"
}
{% endhighlight %}  
--- 
{% highlight json %}
{
    "name": "Berners-Lee",
    "firstname": "Tim",
    "birthday" : "1955-06-08"
}
{% endhighlight %}
---
  __2.4 Recherche sans critère :__  
__GET__ programmer/person/_search

  __2.5 Recherche full text :__  
__GET__ programmer/person/_search
{% highlight json %}
{
    "query": {
        "match": {
            "name": "lee"
        }
    }
}
{% endhighlight %}
---
  __2.6 Recherche full text avec highlighting (mise en surbrillance du terme qui "match" le texte de recherche) :__
__GET__ programmer/person/_search
{% highlight json %}
{
    "query": {
        "match": {
            "name": "lee"
        }
    },
    "highlight": {
        "fields": {
            "name":{}
        }
    }
}
{% endhighlight %}
---    
  __2.7 Voir le mapping :__  
__GET__ programmer/person/_mapping
  
  __2.8 Supprimer un document :__  
__DELETE__ programmer/person/{id}
      
  __2.9 Supprimer l'index :__  
__DELETE__ programmer

---

### 3. Recherche d'article de blog
Vous disposez un jeux de donnée à indexer dans elasticsearch contenant les articles du blog de Xebia. Vous allez
dévoir réaliser plusieurs étapes afin d'implémenter la recherche de ces articles. Un document réprésente un
article avec les champs suivants : 

* __title__ : le titre de l'article
* __pubDate__ : La date de publication de l'article
* __creator__ : L'auteur de l'article
* __category__ : L'article appartient à une ou plusieurs catégories
* __description__ : Description courte de l'article
* __content__ : Contenu complet de l'article au format html
  
---

###### Attention, pour cet partie, si vous utilisez le elasticsearch en ligne n’oubliez pas de changer le nom d’index __‘xebia’__ en __‘votre-nom-xebia’__ ######

  __3.1 Création de l'index__  
Créér l'indexe pour recevoir les documents avec le mapping ci-dessous, ce mapping est équivalent au mapping par défaut généré par Elasticsearch mais sera plus facilement modifiable par la suite (Déclaration d'un premier analyzer).
 Pour créér l'indexe 'xebia' avec ce mapping :  
    
__PUT__ xebia
{% highlight json %}
{
    "mappings": {
        "blog": {
            "properties": {
                "category": {
                    "type": "string"
                },
                "content": {
                    "type": "string"
                },
                "creator": {
                    "type": "string"
                },
                "description": {
                    "type": "string"
                },
                "pubDate": {
                    "type": "date",
                    "format": "strict_date_optional_time||epoch_millis"
                },
                "title": {
                    "type": "string"
                }
            }
        }
    },
    "settings": {
        "analysis": {
            "analyzer": {
                "my_analyzer": {
                    "type": "custom",
                    "tokenizer": "standard",
                    "filter": [
                        "lowercase"
                    ],
                    "char_filter": [
                    ]
                }
            },
             "filter": {},
             "tokenizer": {},
             "char_filter": {}
        }
    }
}
{% endhighlight %}
---
  __3.2 Indéxer les documents__  
Pour indexer tous ces documents en une étape vous allez utiliser curl :  

 * Télécharger le dataset [data/xebiablog.data](xebiablog.data)
 * Exécuter une requête bulk indexing :  
  `curl -XPUT http://{host}:9200/{indexName}/blog/_bulk --data-binary @xebiablog.data`
  
  __Vérifier que les 1197 documents sont correctements indexés :__  
  __GET__ xebia/blog/_count  
  
Le fichier xebiablog.data contient l'ensemble des documents à indexer au format :   
{"index" : {"_id":"2"}}  
{"title":"Scrum pour la Recherche","pubDate":"2016-09-19T13:39:42"  ...}        
[...]  

  __3.3 En vous inspirant de l'exercice     2.5, ecrire une requête qui permet de remonter les articles dont <u>le contenu</u> parle de "kodo kojo"__
<blockquote class = 'solution' markdown="1">
__GET__ xebia/blog/_search
{% highlight json %}
{
    "query": {
        "match": {
            "content": "kodo kojo"
        }
    }
}
{% endhighlight %}
</blockquote>
---
  __3.4 La requête précédente permet de rechercher sur le contenu des articles. Cependant en effectuant cette requête sur le texte "Recherche full Text", les résultats ne semblent pas remonter de contenu pertinent.
Utiliser l'highlighting afin de comprendre pourquoi ces résultats sont remontés.__
<blockquote class = 'solution' markdown="1">
GET xebia/blog/_search
{% highlight json %}
{
    "query": {
        "match": {
            "content": "Recherche full Text"
        }
    },
    "highlight": {
        "fields": {
            "content":{}
        }
    }
}{% endhighlight %}
Conclusion : Les caractères html font "matcher" les termes "full" et "text" à tord.
</blockquote>
---
  __3.5 Pour résoudre le problème précédent, changer l'analyser du champ 'content' afin de supprimer les caractères html :__
Pour cela modifier la mapping afin d'utiliser le token filter __html_strip__ dans un 'custom analyzer' et déclarer le champ __'content'__ comme utilisant cet analyzer.  
__Syntaxe :__ 
{% highlight json %}
{
    "{fieldName}": {
          "type": "string",
          "analyzer": "{analyzerName}"
     }
}
{% endhighlight %}

---

  * pour modifier le mapping vous devez : 
    * Supprimer l'indexe
    * Re-créér l'indexe avec le nouveau mapping :   
    * Re-indexer tous les documents (avec le cUrl) 

<blockquote class = 'solution' markdown="1">
__DELETE__ xebia  
__PUT__ xebia
{% highlight json %}
{
  "mappings": {
    "blog": {
      "properties": {
        "category": {
          "type": "string"
        },
        "content": {
          "type": "string",
          "analyzer": "my_analyzer"
        },
        "creator": {
          "type": "string"
        },
        "description": {
          "type": "string"
        },
        "pubDate": {
          "type": "date",
          "format": "strict_date_optional_time||epoch_millis"
        },
        "title": {
          "type": "string"
        }
      }
    }
  },
  "settings": {
    "analysis": {
      "analyzer": {
        "my_analyzer": {
          "type": "custom",
          "tokenizer": "standard",
          "filter": [
            "lowercase"
          ],
          "char_filter": [
            " html_strip"
          ]
        }
      },
        "filter": {},
        "tokenizer": {},
        "char_filter": {}
    }
  }
}
{% endhighlight %}
__curl -XPUT "http://{host}:9200/{indexName}/blog/_bulk" --data-binary @xebiablog.data__
</blockquote>
---  

  __3.6 L'entreprise Typesafe a changé de nom pour Lightbend. Problème les recherches sur "lightbend" ne remontent que 3 résultats. Modifier le mapping afin que toutes les recherches sur un des noms remontent les résultats associés aux 2 noms d'entreprise.__   
  Pour cela ajouter un _filter_ de type synonym

__Syntaxe :__   
{% highlight json %}
{
    "{filterName}": {
          "type": "synonym",
          "synonyms": [
            "term1, term2 => synonym1, synonym2"
          ]
     }
}        
{% endhighlight %}

---


<blockquote class = 'solution' markdown="1">
__DELETE__ xebia  
__PUT__ xebia
{% highlight json %}
{
  "mappings": {
    "blog": {
      "properties": {
        "category": {
          "type": "string"
        },
        "content": {
          "type": "string",
          "analyzer": "my_analyzer"
        },
        "creator": {
          "type": "string"
        },
        "description": {
          "type": "string"
        },
        "pubDate": {
          "type": "date",
          "format": "strict_date_optional_time||epoch_millis"
        },
        "title": {
          "type": "string"
        }
      }
    }
  },
  "settings": {
    "analysis": {
      "analyzer": {
        "my_analyzer": {
          "type": "custom",
          "tokenizer": "standard",
          "filter": [
            "lowercase",
            "mySynonym"
          ],
          "char_filter": [
            " html_strip"
          ]
        }
      },
      "filter": {
        "mySynonym": {
          "type": "synonym",
          "synonyms": [
            "lightbend, typesafe => lightbend, typesafe"
          ]
        }
      },
      "tokenizer": {},
      "char_filter": {}
    }
  }
}
{% endhighlight %}
__curl -XPUT "http://{host}:9200/{indexName}/blog/_bulk" --data-binary @xebiablog.data__
</blockquote>
---

  __3.7 Suppression des posts trop anciens :__   
  Les recherchent peuvent remonter des résultats de 2011. Utilisez la recherche full text conjointement avec un filtre pour ne pas remonter les documents plus ancien de 2 an.      
Pour cela utilisez une **bool** query  et un **range** filter  
__Syntaxe :__   
{% highlight json %}
{
  "query": {
    "bool": {
      "must": [
        {
          "match": {
            "FIELD": "TEXT"
          }
        }
      ],
      "filter": {
        "range": {
          "FIELD": {
            "gte": 10,
            "lte": 20
          }
        }
      }
    }
  }
}       
{% endhighlight %}

<blockquote class = 'solution' markdown="1">
GET xebia/blog/_search
{% highlight json %}
{
  "query": {
    "bool": {
      "must": [
        {
          "match": {
            "content": "java"
          }
        }
      ],
      "filter": {
        "range": {
          "pubDate": {
            "gte": "now-2y"
          }
        }
      }
    }
  }
} 
{% endhighlight %}
_
</blockquote>
---

  __3.8 Requête sur plusieurs champs :__   
  Lorsqu'on fait une recherche de type match query sur "javascript", les résultats ne sont pas assez ciblés sur le sujet. Afin de rendre le résultat plus pertinent modifier la requête précédente pour remplacer la requête de type *match* par une requête de type *multi_match* 
  afin de pouvoir exécuter la même requête conjointement sur le champ "content" et le champ "title".    
  

---


<blockquote class = 'solution' markdown="1">
GET xebia/blog/_search
{% highlight json %}
{
  "query": {
    "bool": {
      "must": [
        {
          "multi_match": {
            "query": "javascript",
            "fields": ["content","title"]
          }
        }
      ],
      "filter": {
        "range": {
          "pubDate": {
            "gte": "now-2y"
          }
        }
      }
    }
  }
}
{% endhighlight %}
_
</blockquote>
---
__3.9 Suggestion :__   
  Nous souhaitons être capable de faire de la suggestion sur la titre des posts dès la première lettre saisie. Pour cela, vous allez utiliser l'api __Completion suggester :__    
  
  -  Ajoutez un champ "suggest" au mapping de type __completion__ et avec comme propriété __"payloads": true__  
  - Utilisez le fichier [data/xebiablogWithSuggest.data](xebiablogWithSuggest.data) pour l'indéxation. Ce fichier contient les mêmes documents mais avec le champ suggest au le format suivant :   
{% highlight json %}
  {
    "suggest": {
            "input": ["<title>"],
            "payload": {
                 "blogId": "<documentId>"
            }
    }
  }
{% endhighlight %}
---  
  - Effectuer une requête de type suggest   
__Syntaxe :__
GET xebia/_suggest
{% highlight json %}      
{
  "<name>":{
    "text" : "<text_to_search>",
        "completion" : {
            "field" : "<suggest_field_name>"
    }
  }
}  
{% endhighlight %}

Cette requête doit pouvoir remonter les titres de recherche sur d, do, doc, dock, docke, docker. Ainsi que l'id du document correspondant.  
Ce mapping permet d'analyser le texte et le stocker dans une structure de donnée optimisée afin de remonter efficacement les suggestions sans passer par une __prefix__ query moins optimisée.

---


<blockquote class = 'solution' markdown="1">
DELETE xebia         
PUT xebia  
{% highlight json %}   
{
  "mappings": {
    "blog": {
      "properties": {
        "category": {
          "type": "string"
        },
        "content": {
          "type": "string",
          "analyzer": "my_analyzer"
        },
        "creator": {
          "type": "string"
        },
        "description": {
          "type": "string"
        },
        "pubDate": {
          "type": "date",
          "format": "strict_date_optional_time||epoch_millis"
        },
        "title": {
          "type": "string"
        },
        "suggest": {
          "type": "completion",
          "payloads": true
        }
      }
    }
  },
  "settings": {
    "analysis": {
      "analyzer": {
        "my_analyzer": {
          "type": "custom",
          "tokenizer": "standard",
          "filter": [
            "lowercase",
            "mySynonym"
          ],
          "char_filter": [
            " html_strip"
          ]
        }
      },
      "filter": {
        "mySynonym": {
          "type": "synonym",
          "synonyms": [
            "lightbend, typesafe => lightbend, typesafe"
          ]
        }
      },
      "tokenizer": {},
      "char_filter": {}
    }
  }
}
 {% endhighlight %}
curl -XPUT "localhost:9200/xebia/blog/_bulk" --data-binary @xebiablogWithSuggest.data

GET xebia/_suggest
{% highlight json %}   
{
  "title-suggest":{
    "text" : "do",
        "completion" : {
            "field" : "suggest"
        }
  }
}
{% endhighlight %}
</blockquote>
---
__3.10 Suggestion fuzzy:__   
  Problème l'api de suggestion ne remonte pas de résultat si la personne qui recherche se trompe dans la saisie du texte.   
   Modifiez la requête de suggestion afin de pouvoir remonter les suggestions liées à Docker si l'on saisie "Doker".  
   Pour cela, ajoutez le paramètre __"fuzzy":{}__ à la requête.
<blockquote class = 'solution' markdown="1">
{% highlight json %}   
{
  "title-suggest":{
    "text" : "doker",
        "completion" : {
            "field" : "suggest",
            "fuzzy" : { }
        }
  }
}
{% endhighlight %}
</blockquote>
__3.11 Aggregations:__   
  Nous souhaitons maintenant ramener toutes les catégories possibles pour un blog. Pour cela utilisez une aggrégations de type __terms__.

  __Syntaxe :__  
  GET xebia/blog/_search
  {% highlight json %}      
  { "size": 0, 
    "aggregations": {
      "<aggregation_name>": {
        "<aggregation_type>": {
          "field": "<field_name>"
        }
      }
    }
  }
{% endhighlight %}  
            
---     

__Attention__ : On doit remonter les terms "exact", pour cela vous allez devoir modifier le mapping.   
  
<blockquote class = 'solution' markdown="1">
DELETE xebia     
PUT xebia     
{% highlight json %}   
{
  "mappings": {
    "blog": {
      "properties": {
        "category": {
          "type": "string",
          "index": "not_analyzed"
        },
        "content": {
          "type": "string",
          "analyzer": "my_analyzer"
        },
        "creator": {
          "type": "string"
        },
        "description": {
          "type": "string"
        },
        "pubDate": {
          "type": "date",
          "format": "strict_date_optional_time||epoch_millis"
        },
        "title": {
          "type": "string"
        },
        "suggest": {
          "type": "completion",
          "payloads": true
        }
      }
    }
  },
  "settings": {
    "analysis": {
      "analyzer": {
        "my_analyzer": {
          "type": "custom",
          "tokenizer": "standard",
          "filter": [
            "lowercase",
            "mySynonym"
          ],
          "char_filter": [
            " html_strip"
          ]
        }
      },
      "filter": {
        "mySynonym": {
          "type": "synonym",
          "synonyms": [
            "lightbend, typesafe => lightbend, typesafe"
          ]
        }
      },
      "tokenizer": {},
      "char_filter": {}
    }
  }
}
{% endhighlight %}
GET xebia/blog/_search
{% highlight json %}   
{ "size": 0, 
  "aggregations": {
    "by_category": {
      "terms": {
        "field": "category",
        "size": 100
      }
    }
  }
}
{% endhighlight %}
</blockquote>
---   

### 4. Recherche appartement
L'agence X-immobilier vient de créér son site internet de recherche de bien immobilier en Île de France.
Vous disposez d'un jeux de donnée à indéxer dans elasticsearch contenant des appartements à vendre 
avec les champs suivants : 

* __price__ : Le prix en euro
* __nbOfRoom__ : Nombre de pièce
* __surface__ : La surface en m²
* __address__ : un object contenant l'adresse :  
    * __street__ : le numéro et la voie
    * __postalCode__ : le Code postal
    * __city__ : La ville
* __location__ : un object contenant les coordonnées géoloc
    * __lat__ : la latitude
    * __lon__ : la longitude    
    
Voici un exemple : 
{% highlight json %}   
{
    "price": 136920,
    "nbOfRoom": 4,
    "surface": 56,
    "address": {
        "street": "21 BOULEVARD DE LA MALIBRAN",
        "postalCode": "77680",
        "city": "ROISSY EN BRIE"
    },
    "location": {
        "lat": 48.794399999999996,
        "lon": 2.64448
    }
}{% endhighlight %}

__4.1 Création de l'index__  
Créér l'indexe pour recevoir les documents avec le mapping ci-dessous.
Le mapping n'aura plus besoin d'être modifier. Noter le mapping du champ location
     
__PUT__ x-immobilier
{% highlight json %}
{
     "mappings": {
       "apartment": {
         "properties": {
           "address": {
             "properties": {
               "city": {
                 "type": "string"
               },
               "postalCode": {
                 "type": "string"
               },
               "street": {
                 "type": "string"
               }
             }
           },
           "location": {
              "type": "geo_point"
           },
           "nbOfRoom": {
             "type": "long"
           },
           "price": {
             "type": "long"
           },
           "surface": {
             "type": "long"
           }
         }
       }
     }
   }
{% endhighlight %}
---
  __4.2 Indéxer les documents__  
Pour indexer tous ces documents en une étape vous allez utiliser curl :  

 * Télécharger le dataset [data/apartment.data](data/apartment.data)
 * Exécuter une requête bulk indexing :  
  `curl -XPUT http://{host}:9200/x-immobilier/apartment/_bulk --data-binary @apartment.data`
  
 __Vérifier que les 3991 documents sont correctements indexés :__  
 __GET__ x-immobilier/_count
   
__4.3 Filtre par rapport à la distance depuis un point__  
Pour les besoins du site, il faut être capable de rechercher les appartements à proximité de certains points d'interêts.  
Ecrire une requête permettant de remontrer les appartements se trouvant à moins de 500m du  
 métro Cadet lat: 48.876135, "lon": 2.344876 en utilisant un filtre de type __geo_distance__

<blockquote class = 'solution' markdown="1">

GET x-immobilier/apartment/_search
{% highlight json %}   
{
  "query": {
    "bool": {
      "filter": {
        "geo_distance": {
          "distance": "500m",
          "location": {
            "lat": 48.876135,
            "lon": 2.344876
          }
        }
      }
    }
  }
}
{% endhighlight %}
</blockquote>
__4.4 Tri par rapport à la distance depuis un point__  
La requête précédente permet aux utilisateurs de remonter les adresses à moins de 500m, cependant les utilisateurs souhaiteraient voir en priorité les apparements les plus proche.
Modifier la requête pour ajouter le tri par ___geo_distance__

<blockquote class = 'solution' markdown="1">

GET x-immobilier/apartment/_search
{% highlight json %}   
{
  "query": {
    "bool": {
      "filter": {
        "geo_distance": {
          "distance": "500m",
          "location": {
            "lat": 48.876135,
            "lon": 2.344876
          }
        }
      }
    }
  },
  "sort": [
    {
      "_geo_distance": {
        "location": {
          "lat": 48.876135,
          "lon": 2.344876
        },
        "order": "asc"
      }
    }
  ]
}
{% endhighlight %}
</blockquote>