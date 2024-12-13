# Exercice 3 - Permettre la collaboration en équipe

## Composition de notre équipe

Membre 1
- Nom : BOURREAU
- Prénom : Quentin
- GitHub : https://github.com/BOURREAUQuentin

Membre 2
- Nom : DEBRAY
- Prénom : Antoine
- GitHub : https://github.com/AntoineDebray1

Membre 3
- Nom : MALLERON
- Prénom : Daniel
- GitHub : https://github.com/MalleronDaniel

Membre 4
- Nom : PIGOREAU
- Prénom : Nathan
- GitHub : https://github.com/Nathan-Pigoreau


## Tâche 1 - Choisir un modèle de contrôle de version et une stratégie de branchement

Dans le cadre de notre collaboration entre deux équipes sur un même projet, nous avons opté pour `GitHub` comme plateforme de gestion de code et pour le modèle de branchement `GitFlow`. Ces choix stratégiques visent à garantir une organisation optimale, une meilleure productivité et une meilleure coordination entre les membres des équipes.


## Tâche 2 - Définir les responsabilités des équipes

Pour favoriser une collaboration fluide et efficace entre les deux équipes, il est essentiel de répartir clairement les responsabilités en fonction de l’architecture de l’application basée sur les microservices. Voici comment nous nous sommes répartis les tâches :

### Équipe A : API Gateway (Daniel et Nathan)

- Développement et maintenance de of-api-gateway :
    - Gérer les points d’entrée de l’application.
    - Implémenter les mécanismes d’authentification, de routage, et de limitation de débit (si nécessaire).
    - Assurer la transformation et l’agrégation des requêtes destinées aux microservices sous-jacents.

- Collaboration avec les microservices Back-End :
    - Intégrer les appels aux services product.registry et product.registry.read via des API.


### Équipe B : Product registry (Antoine et Quentin)

- Développement et maintenance de of-product-registry-microservices :
    - Gérer les modules product.registry et product.registry.read.
    - Implémenter la logique métier, le stockage des données, et l’interaction avec la base de données.

- Gestion des bibliothèques partagées dans libs/ :
    - event-sourcing : Implémenter et gérer les mécanismes de gestion des événements si une architecture orientée événement est utilisée.
    - published-language : Standardiser le format des messages ou des modèles utilisés dans l’ensemble des microservices.


## Tâche 3 - README et règles de collaboration

Voir la correction du README à la racine du projet et les règles de collaboration dans le fichier `CONTRIBUTING.md`.


## Tâche 4 - Divisez votre groupe en 2 équipes

Pour assurer une collaboration efficace entre les équipes, nous utiliserons les outils de communication suivants :

- Teams : Gestion des réunions hebdomadaires et synchronisations formelles.
- Discord : Communication en temps réel pour les discussions, questions et décisions rapides.
- Présentiel : Communication durant les cours de TPs pour résoudre rapidement les points bloquants.
- Mail : Communication pour le suivi officiel, partage des livrables, et discussions importantes nécessitant un historique.
- VS Code Live Share : Sessions de codage collaboratif et de débogage entre les membres de l'équipe.


#

BOURREAU Quentin / DEBRAY Antoine / MALLERON Daniel / PIGOREAU Nathan - BUT Info 3.1.B