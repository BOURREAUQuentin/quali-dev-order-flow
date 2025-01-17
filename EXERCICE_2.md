# Exercice 2 - Corriger les problèmes de qualité du microservice du registre de produits

## Tâche 1 - Compléter les commentaires et la Javadoc

J'ai ajouté des docstrings pour chaque classe où il en manquait pour le microservice du registre de produits. J'ai également ajouté quelques commentaires sur des parties que je trouvais importantes ou compliqués à comprendre rapidement.


## Tâche 2 - Corriger les RuntimeException paresseuses

J'ai géré les RuntimeException paresseuses en les remplaçant par des exceptions personnalisés que j'ai créés et ajoutées dans un dossier exception du microservice. Je l'ai fait pour le microservice du registre de produits (obligatoire), mais aussi dans l'ensemble du projet où il y avait ce problème.


## Tâche 3 - Convertir les payloads des Entités (couche de persistance) en records

J'ai converti l'ensemble des payloads des entités (anciennement `class`) en `record` dans le microservice du registre de produits.


## Tâche 4 - Modifier les Entités (couche de persistance) pour utiliser des champs privés avec des accesseurs

J'ai modifié les entités pour utiliser des champs privés avec des accessesseurs du fait notamment que Sonarlint affichait un warning sur toutes les payloads des entités.


## Tâche 4.1 - Champs publics sur les entités Panache

Le fichier ProductRegistryEventEntity.java contient une classe annotée avec `@MongoEntity` (utilisée avec Quarkus Panache MongoDB), ainsi ses champs ne doivent pas être privés, contrairement aux recommandations de SonarLint.

Pourquoi les champs publics sont requis dans une MongoEntity ?

- Quarkus Panache MongoDB repose sur un modèle simplifié pour gérer les entités. Les champs publics sont accessibles directement par le framework pour simplifier les opérations de sérialisation/désérialisation, comme le mapping entre les objets Java et les documents MongoDB.

- Dans les entités Panache, les getters et setters ne sont pas nécessaires. Le framework accède directement aux champs publics pour lire et écrire les données, ce qui élimine la surcharge de méthodes inutiles et améliore la lisibilité du code.

- Si les champs sont rendus privés, Quarkus Panache ne pourra pas y accéder automatiquement. Cela nécessiterait d'implémenter manuellement des accesseurs (getters et setters), ce qui va à l’encontre de l'objectif principal de Panache, qui est de simplifier la gestion des entités.

- Enfin, la documentation officielle de Quarkus recommande d'utiliser des champs publics pour les entités Panache MongoDB. Ce modèle est conçu pour des applications où la simplicité et la rapidité de développement sont prioritaires.

Il faudrait donc ajouter un commentaire dans le code pour expliquer pourquoi les champs sont publics, afin d’éviter toute confusion future (ce que j'ai fait).


## Tâche 5 - Corriger les erreurs et avertissements signalés par SonarLint 

J'ai mis à jour le fichier ProductRegistryService.java car il comportait un commentaire TODO à remplir donc Sonarlint soulevait une erreur : `Complete the task associated to this TODO comment.sonarlint(java:S1135)`.

J'ai corrigé le problème `Define a constant instead of duplicating this literal "Command: " 4 times. [+4 locations]sonarlint(java:S1192)` en définissant justement une constante de type string pour éviter la répétition et avoir une meilleure maintenabilité du code, améliorant ainsi la qualité du code.


## Tâche 6 - Ajouter des tests d'intégration

J'ai créé un fichier ProductRegistryCommandResourceTest en respectant le même package que la classe d'origine `ProductRegistryCommandResource`. J'ai ajouté des tests d'intégration dans ce fichier qui contient la classe `ProductRegistryCommandResourceTest` pour tester l'intégration entre le client et le serveur via l'API HTTP et couvrir ces tests : 

- Trois tests de création d'un produit (avec un produit valide, avec un produit invalide et avec un produit nul) pour vérifier `POST /api/product/registry/registerProduct`.

- Trois tests de modification d'un produit (avec un produit valide, avec un produit invalide et avec un produit nul) pour vérifier `POST /api/product/registry/updateProduct`.

- Trois tests de suppression d'un produit (avec un produit valide, avec un produit invalide et avec un produit nul) pour vérifier `POST /api/product/registry/deleteProduct`.


## Tâche 7 - Ajouter des tests unitaires

J'ai créé des tests unitaires pour couvrir les principales fonctionnalités du microservice du registre de produits. Ces tests se concentrent sur la logique métier et l'intégrité des méthodes des agrégats, en simulant les dépendances via des mocks. Voici les points clés de cette tâche que j'ai réalisé :

- J'ai ajouté des tests pour valider la gestion des commandes par le ProductRegistry. Cela inclut les cas où les commandes sont valides et ceux où elles ne le sont pas, en vérifiant que les exceptions sont bien levées pour les cas invalides.

- J'ai couvert les événements métier comme ProductRegistered et ProductRemoved, en testant leur impact sur l'état interne de l'agrégat. Par exemple :
    - Après un ProductRegistered, le produit doit être présent dans le registre.
    - Après un ProductRemoved, le produit doit être absent du registre.

- Des tests ont été ajoutés pour vérifier le bon fonctionnement des méthodes utilitaires, telles que hasProductWithId, hasProduct, et isProductNameAvailable.

- J'ai ajouté des tests pour valider le mécanisme de gestion des versions de l'agrégat, en m'assurant que la version s'incrémente correctement après chaque modification.

- J'ai utilisé Mockito pour simuler le comportement du ProductRegistryService, isolant ainsi les tests de la logique métier de l'agrégat sans dépendre d'autres parties du code.


#

BOURREAU Quentin - BUT Info 3.1.B