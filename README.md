# DevOps

# Multi-stage Docker build

https://github.com/charroux/rent/blob/main/MyService/Dockerfile

Créer imaage Docker : 

docker build -t votreimage .

docker run -p 8080:8080 votreimage

Tester dans votre navigateur


# Collaborer à un projet : le concept du pull request

Quand un développeur collabore à un projet il porocède de la façon suivante : 

- il récupère le projet sur sa machine (git clone)
- il créé une copie du projet afin de ne pas affecter le code qui est déjà en production (git branch et git checkout)
- il travail à débogeur le code ou à développer une nouvelle fonctionnalité (git add, git commit)
- il écrit aussl les programmes de tests qui valident son travail
- et enfin il envoie sa copie du code vers le serveur git (git push)

Le chef de projet peut alors déclencher un processus d'intégration continue (CI) en lançant les procédures de tests écrit oar le développeur :
 c'est le pull request. Un script va alors être déclanché sur un serveur de test. 
Si les tests du développeurs sont concluants, le chef de projet peut alors décider de fisionner la copie du développeur avec la version originale (git marge).
 Tous les développeurs doivent alors récupérer la mise à jour du code sur leur machine en faisant un git pull. 
Et c'est là qu'on comprend le terme pull request qui est finalament une demande de pull faite par un développeur au chez de projet quand il a finit son travail.

DES LORS QUE LE CODE EST TESTÉ SUR LES SERVEURS DE GITHUB, VOUS POUVEZ UTILISEZ N'IMPORTE QUEL ÉDITEUR DE TEXTE SUR VOTRE MACHINE POUR CODER.

## Premier essai de pull request

### Launch a workflow when the code is updated

Créer une nouvelle branche sur votre machine:
```
git branch newcarservice
```
Se déplacer vers la nouvelle branche:
```
git checkout newcarservice
```
Modifier puis mettre à jour avec un :
```
git commit -a -m "newcarservice"
```
Se remettre sur la brnache main:
```
git checkout main
```
Envoyer les changements vers GitHub :
```
git push -u origin newcarservice
```
A partir de là, vous jouez le rôle d'un chef de projet.

Créer un pull request chez Github en comparant la nouvelle branche avec la votre. 
C'est un ce moment là qu"un script d'intégration continue va se déclencher chez Github. 
Goithub trouve le code de ce script dans votre projet : https://github.com/charroux/rent/blob/main/.github/workflows/action.yml

Etudiez ce script et suivez son bon déreoulement chez Github. Si tout va bien, vous pourrez alors "merger" les branches chez Github.

NE PAS OUBLIER de faire alors un 
```
git pull origin main
```
Sur toutes les machines des développeurs (y-compris celle du développeur qui a soumis son code) afin de mettre à jour la branche main sinon le serveur Github n'acceptera pas de nouveau push au pretexte que le code n'est pas à jour.

La nouvelle branche peut alors être effacée sur la machine du développeur est chez Github :

```
git branch -D newcarservice
```
```
git push origin --delete newcarservice
```

## Tests unitaires avec JUnit

### Qu'est-ce qu'un test unitaire ?

Un test unitaire est un programme qui vérifie qu'une partie du code fonctionne correctement. C'est comme une mini-application de test qui :
- Crée des données de test
- Exécute du code
- Vérifie que le résultat est correct

**Pourquoi c'est important ?** Les tests permettent de trouver les bugs rapidement et de vérifier que chaque modification n'a pas cassé la fonctionnalité existante.

### Structure des tests

Dans ce projet, les tests sont organisés ainsi :
```
src/test/java/com/example/myservice/
├── controllers/
│   └── RentServiceRestTest.java    (Tests de l'API REST)
├── entities/
│   └── CarTest.java                (Tests du modèle Car)
└── services/
    └── CarServiceTest.java         (Tests du service métier)
```

### Exécuter les tests localement

Sur votre machine, dans le dossier `MyService` :

```bash
./gradlew test
```

Les résultats seront affichés dans le terminal :
- ✅ Tests réussis en vert
- ❌ Tests échoués en rouge

### Voir les résultats détaillés

Les rapports complets sont générés dans :
```
build/reports/tests/test/index.html
```

Ouvrez ce fichier dans votre navigateur pour voir :
- Quels tests ont réussi/échoué
- Le détail des erreurs
- La couverture de code

### Tests automatiques avec GitHub Actions

Chaque fois que vous faites un :
- **Push** sur une branche
- **Pull Request** 

GitHub déclenche automatiquement les tests. Vous pouvez voir les résultats dans l'onglet **Actions** de votre repository.

**Important** : Si les tests échouent, le code ne peut pas être fusionné ! Vous devez corriger les tests avant de faire un merge.

### Exemple : Écrire un test simple

Voici un exemple de test pour la classe `Car` :

```java
@Test
public void testCarConstructor() {
    Car car = new Car("ABC123", "Toyota", 15000.0);
    assertEquals("ABC123", car.getPlateNumber());
    assertEquals("Toyota", car.getBrand());
    assertEquals(15000.0, car.getPrice());
}
```

Ce test :
1. Crée une voiture
2. Vérifie que les propriétés sont correctes
3. Si les assertions échouent, le test est rouge ❌

