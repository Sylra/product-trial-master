# Mes notes:

## Backend:
- Utilisation de Java 17 avec Spring Web pour la création des services web.
- Gestion de la persistence, utilisation de JPA (Java Persistence API), permettant de définir les opérations CRUD simples et suffisants pour ce projet. 
- Utilisation d'une base de données sous forme d'un fichier avec H2, qui a l'avantage d'être adapté dans un contexte Java. Ce fichier est crée lors du lancement de l'application, et persiste lors des lancements futurs.
- Les entités (comme Product) utilisent la dépendence Lombok, permettant de spécifier des annotations à la place de devoir définir les Getter ou Setter par exemple.
- Utilisation des classes DTO (Data Transfer Object) pour créer une abstraction ou séparation de responsabilité pour les entités, en ne rendant accessible que les propriétés nécessaire pour un Create ou un Update dans le cas de Product. 
- Ajout de la validation des propriétés, par exemple "name" qui ne doit pas être vide dans la requête.
- Choix d'une architecture MVC et non MVCS pour rester avec une base plutôt légère. Hormis pour la gestion de la sécurité (point spécifié plus bas)
- Ajout d'une classe Test pour le controlleur Product.
- Ajout de Spring Security pour la gestion de l'authentication avec JWT, et de jsonwebtoken.io pour l'utilitaire sur la création et la validation du token.
- Ajout par défaut d'un utilisateur "admin@admin.com" lors du lancement de l'application pour la première fois.
- Protection de l'API Product n'autorisant que l'admin à effectuer POST, PATCH et DELETE.
- Ajout de la liste d'achats en créant une API nommé CartController, avec les tables Cart et CartProduct, dont ce dernier gère la liaison Many-to-Many entre Cart et Product.
- Ajout de la spécification de l'API dans back/swagger/api.yaml (utilisable par l'outil Swagger).
- Ajout de la Cross Origin Resource Sharing (CORS) pour n'autoriser que des origines distantes définies à accéder au resources du serveur.
- 
- Temps passé: 17h
-
## Frontend:
- La liste des produits est affiché sous forme de grille (3 par colonnes) pour optimiser l'espace.
- Gestion automatique du token (dans le localStorage) lors de l'ouverture du site, pouvant ensuite être utilisé par la gestion des produits, qui est presque fonctionelle.
-
- Temps passé: 3h

## Améliorations:
- Séparation dev et prod
- Conteneurisation avec Docker.
### Backend
- Si token invalide ou expiré, il est plus standard de retourner 401 Unauthorized plutôt que 403 Forbidden.
- La suite de tests actuel n'est plus fonctionnel depuis l'ajout de JWT, et nécessite donc des adaptations.
- Documentation du code avec des commentaires.
- Chiffrement du mot de passe pour Account (avec BCrypt par exemple).
- Lier la liste d'achats (et liste de souhaits) à un utilisateur, pour le moment n'importe quel utilisateur peut agir sur une quelconque liste.
- Une meilleur couverture de tests unitaires.
- Limiter le nombre de requêtes dans un interval donné.
### Frontend
- Authentification manuelle mettant le token dans le localStorage car pour l'instant, cela authentifie l'admin en dur
- Gestion des erreurs sur ProductService au lieu de rebondir sur le JSON.
- Validation de la gestion des Produits.

---
---
---

# Consignes

- Vous êtes développeur front-end : vous devez réaliser les consignes décrites dans le chapitre [Front-end](#Front-end)

- Vous êtes développeur back-end : vous devez réaliser les consignes décrites dans le chapitre [Back-end](#Back-end) (*)

- Vous êtes développeur full-stack : vous devez réaliser les consignes décrites dans le chapitre [Front-end](#Front-end) et le chapitre [Back-end](#Back-end) (*)

(*) Afin de tester votre API, veuillez proposer une stratégie de test appropriée.

## Front-end

Le site de e-commerce d'Alten a besoin de s'enrichir de nouvelles fonctionnalités.

### Partie 1 : Shop

- Afficher toutes les informations pertinentes d'un produit sur la liste
- Permettre d'ajouter un produit au panier depuis la liste 
- Permettre de supprimer un produit du panier
- Afficher un badge indiquant la quantité de produits dans le panier
- Permettre de visualiser la liste des produits qui composent le panier.

### Partie 2

- Créer un nouveau point de menu dans la barre latérale ("Contact")
- Créer une page "Contact" affichant un formulaire
- Le formulaire doit permettre de saisir son email, un message et de cliquer sur "Envoyer"
- Email et message doivent être obligatoirement remplis, message doit être inférieur à 300 caractères.
- Quand le message a été envoyé, afficher un message à l'utilisateur : "Demande de contact envoyée avec succès".

### Bonus : 

- Ajouter un système de pagination et/ou de filtrage sur la liste des produits
- On doit pouvoir visualiser et ajuster la quantité des produits depuis la liste et depuis le panier 

## Back-end

### Partie 1

Développer un back-end permettant la gestion de produits définis plus bas.
Vous pouvez utiliser la technologie de votre choix parmi la liste suivante :

- Node.js/Express
- Java/Spring Boot
- C#/.net Core
- PHP/Symphony : Utilisation d'API Platform interdite


Le back-end doit gérer les API suivantes : 

| Resource           | POST                  | GET                            | PATCH                                    | PUT | DELETE           |
| ------------------ | --------------------- | ------------------------------ | ---------------------------------------- | --- | ---------------- |
| **/products**      | Create a new product  | Retrieve all products          | X                                        | X   |     X            |
| **/products/:id**  | X                     | Retrieve details for product 1 | Update details of product 1 if it exists | X   | Remove product 1 |

Un produit a les caractéristiques suivantes : 

``` typescript
class Product {
  id: number;
  code: string;
  name: string;
  description: string;
  image: string;
  category: string;
  price: number;
  quantity: number;
  internalReference: string;
  shellId: number;
  inventoryStatus: "INSTOCK" | "LOWSTOCK" | "OUTOFSTOCK";
  rating: number;
  createdAt: number;
  updatedAt: number;
}
```

Le back-end créé doit pouvoir gérer les produits dans une base de données SQL/NoSQL ou dans un fichier json.

### Partie 2

- Imposer à l'utilisateur de se connecter pour accéder à l'API.
  La connexion doit être gérée en utilisant un token JWT.  
  Deux routes devront être créées :
  * [POST] /account -> Permet de créer un nouveau compte pour un utilisateur avec les informations fournies par la requête.   
    Payload attendu : 
    ```
    {
      username: string,
      firstname: string,
      email: string,
      password: string
    }
    ```
  * [POST] /token -> Permet de se connecter à l'application.  
    Payload attendu :  
    ```
    {
      email: string,
      password: string
    }
    ```
    Une vérification devra être effectuée parmi tout les utilisateurs de l'application afin de connecter celui qui correspond aux infos fournies. Un token JWT sera renvoyé en retour de la reqûete.
- Faire en sorte que seul l'utilisateur ayant le mail "admin@admin.com" puisse ajouter, modifier ou supprimer des produits. Une solution simple et générique devra être utilisée. Il n'est pas nécessaire de mettre en place une gestion des accès basée sur les rôles.
- Ajouter la possibilité pour un utilisateur de gérer un panier d'achat pouvant contenir des produits.
- Ajouter la possibilité pour un utilisateur de gérer une liste d'envie pouvant contenir des produits.

## Bonus

Vous pouvez ajouter des tests Postman ou Swagger pour valider votre API