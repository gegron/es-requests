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
   __2.1 Premier document indexé :__    
Requête __POST__ {indexName}/{indexType}/{id}
{% highlight json %}
{
    "name": "Lovelace",
    "firstname": "Ada",
    "birthday" : "1815-12-10"
}
{% endhighlight %}  
---  
  __2.2 Retrouver le document par son id :__  
Requête __GET__ {indexName}/{indexType}/{id}  
    
  __2.3 Indexer d'autres documents :__  
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
__GET__ {indexName}/{indexType}/_search

  __2.5 Recherche full text :__  
__GET__ {indexName}/{indexType}/_search
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
__GET__ {indexName}/{indexType}/_search
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
__GET__ {indexName}/{indexType}/_mapping
  
  __2.8 Supprimer un document :__  
__DELETE__ {indexName}/{indexType}/{id}
      
  __2.9 Supprimer l'index :__  
__DELETE__ {indexName}

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
  
  
Pour indexer tous ces documents en une étape vous allez utiliser curl :
  * Télécharger le dataset <a href="data/xebiablog.data">xebiablog.data</a><br/>
  * Exécuter une requête bulk indexing :  
__curl -XPUT "http://{host}:9200/{indexName}/blog/_bulk" --data-binary @xebiablog.data__

__Vérifier que les 1199 documents sont correctements indexés :__  
__GET__ {indexName}/blog/_count  

__Début de l'exercice :__

  __3.1 Ecrire une requête qui permet de remonter les articles dont <u>le contenu</u> parle de "kodo kojo"__
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
  __3.2 La requête précédente permet de rechercher sur le contenu des articles. Cependant en effectuant cette requête sur le texte "Recherche full Text", les résultats ne semblent pas remonter de résultat pertinent.
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
  __3.3 Pour résoudre le problème précédent, changer l'analyser du champ "content" afin de supprimer les caractères html :__
  * Récupérer le mapping existant
  * ajouter un custom analyzer qui va définir : 
    * Un tokenizer de type **standard**
    * un filter de type **lowercase**
    * un char filter de type **html_strip**
  * pour modifier le mapping vous devez : 
    * Supprimer l'indexe
    * Re-créér l'indexe avec le nouveau mapping :   
    * Re-indexer tous les documents (avec le cUrl) 
  * voici la requête de création d'indexe avec un mapping contenant un custom analyzer :     
__PUT {indexName}__   
{% highlight json %}
{
    "mappings" : {
        "blog" : {
            "properties": {
                "{field_name}" : {
                    "type" : "string",
                    "analyzer": "my_analyzer"
                }
            }
        }   
    },
    "settings": {
        "analysis": {
            "analyzer": {
                "my_analyzer": {
                    "type": "custom",
                    "tokenizer": "{tokenizer_name}",
                    "filter": ["{filter_name}"],
                    "char_filter": ["{char_filter_name}"]
                }
            }
        }
    }
}    
{% endhighlight %}
<blockquote class = 'solution' markdown="1">
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
      }
    }
  }
}
{% endhighlight %}

---
