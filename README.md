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

Voici un exemple de test pour une classe `Car` :

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

## Tests de l'API Web avec MockMvc

### Qu'est-ce que MockMvc ?

MockMvc est un framework de test Spring qui permet de tester les **endpoints REST** de votre application **sans démarrer un vrai serveur**. C'est une simulation qui :
- Lance le contexte Spring de l'application
- Simule les requêtes HTTP (GET, POST, PUT, DELETE, etc.)
- Vérifie les réponses (status code, contenu JSON, headers, etc.)

**Avantage** : Tests rapides et reproductibles sans besoin d'un serveur externe.

### Structure d'un test MockMvc

Chaque test MockMvc utilise :

1. **@SpringBootTest** : Charge le contexte complet de l'application
2. **WebApplicationContext** : Contexte web injecté pour MockMvc
3. **MockMvcBuilders** : Construit une instance de MockMvc
4. **perform()** : Simule une requête HTTP
5. **andExpect()** : Vérifie la réponse

### Exemple : Tester des endpoints REST

Voici comment sont testés les endpoints dans `RentServiceRestTest.java` :

**1. Tester l'ajout d'une voiture (POST)**
```java
@Test
public void testAddCar() throws Exception {
    Car car = new Car("ABC123", "Toyota", 15000.0);
    ObjectMapper objectMapper = new ObjectMapper();
    
    mockMvc.perform(post("/cars")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(car)))
            .andExpect(status().isOk());
}
```

Ce test :
- Crée une voiture avec les propriétés
- Convertit l'objet en JSON avec `ObjectMapper`
- Envoie une requête POST à `/cars`
- Vérifie que la réponse HTTP est **200 OK**

**2. Tester la récupération de voitures (GET)**
```java
@Test
public void testGetCars() throws Exception {
    mockMvc.perform(get("/cars"))
            .andExpect(status().isOk());
}
```

Ce test vérifie que l'endpoint GET `/cars` répond avec un status **200 OK**.

**3. Tester la recherche par plaque (GET /cars/{id})**
```java
@Test
public void testGetCarByPlateNumber() throws Exception {
    mockMvc.perform(get("/cars/ABC123"))
            .andExpect(status().isOk());
}
```

### Vérifications possibles avec MockMvc

Vous pouvez vérifier :
- **status()** : Le code HTTP (200, 404, 500, etc.)
- **content().contentType()** : Le type MIME (application/json, text/html, etc.)
- **jsonPath()** : Le contenu JSON spécifique (`$.id`, `$.cars[0].brand`, etc.)
- **header()** : Les en-têtes HTTP

### Exécuter les tests web

Les tests web sont exécutés de la même façon que les tests unitaires :

```bash
./gradlew test
```

Les résultats incluent à la fois les tests unitaires JUnit ET les tests MockMvc.

## Couverture de code avec JaCoCo et Codecov

### Qu'est-ce que la couverture de code ?

La couverture de code mesure le **pourcentage de votre code source qui est testé**. C'est une métrique importante pour :
- Identifier les parties du code non testées
- Déterminer les zones à risque
- Améliorer la qualité générale du projet

### Comment fonctionne JaCoCo ?

**JaCoCo** (Java Code Coverage) est un outil qui :
1. Analyse l'exécution des tests
2. Enregistre quelles lignes de code sont exécutées
3. Génère un rapport de couverture en HTML et XML

### Générer un rapport de couverture localement

Sur votre machine, dans le dossier `MyService` :

```bash
./gradlew test jacocoTestReport
```

Cela exécute tous les tests ET génère un rapport JaCoCo. Le rapport complet est accessible à :
```
build/reports/jacoco/test/html/index.html
```

Ouvrez ce fichier dans votre navigateur pour voir :
- **Couverture globale** : Pourcentage total de code couvert
- **Par classe** : Détails pour chaque classe
- **Par méthode** : Quelles lignes sont couvertes/non couvertes

### Codecov et GitHub Actions

**Codecov** est une plateforme qui agrège et visualise les rapports de couverture. À chaque **push** ou **pull request**, le workflow GitHub Actions automatiquement :

1. Exécute les tests avec JaCoCo
2. Télécharge le rapport vers Codecov
3. Ajoute un commentaire à votre PR avec :
   - Le pourcentage de couverture global
   - Les modifications de couverture par rapport à main
   - Des badges indiquant la santé du code

### Consulter les résultats sur Codecov

1. Allez sur https://app.codecov.io
2. Connectez-vous avec votre compte GitHub
3. Sélectionnez le repository `rent`
4. Vous verrez :
   - L'historique de la couverture
   - Les fichiers avec la meilleure/pire couverture
   - Les comparaisons entre branches

### Interpréter le rapport

**Exemple de couverture JaCoCo :**
- 🟢 **90-100%** : Très bon, presque tout est testé
- 🟡 **70-89%** : Acceptable, mais des zones non testées
- 🔴 **< 70%** : À risque, besoin d'ajouter des tests

### Bonnes pratiques

✅ **À faire :**
- Viser une couverture **≥ 80%** en production
- Tester les cas de succès ET les cas d'erreur
- Exclure les codes générés (`getter/setter`, configurations, etc.)
- Réviser les parties non couvertes lors des PRs

❌ **À éviter :**
- Augmenter la couverture artificiellement avec des tests inutiles
- Viser 100% de couverture (rarement réaliste)
- Tester uniquement les "happy paths"

### Configuration du projet

Ce projet est configuré pour :
- Générer des rapports **XML** et **HTML** avec JaCoCo
- Uploader automatiquement vers Codecov en CI/CD
- Exclure certaines zones (config, entities, DTOs) de la couverture pour plus de clarté

## Qualité du code avec SonarCloud

### Qu'est-ce que SonarCloud ?

**SonarCloud** est une plateforme cloud qui analyse la **qualité du code** en continu. Elle détecte :
- **Bugs** : Erreurs potentielles et code défectueux
- **Vulnérabilités de sécurité** : Failles de sécurité connues
- **Code smells** : Mauvaises pratiques et code difficile à maintenir
- **Duplications** : Code dupliqué inutile
- **Couverture de tests** : Intégration avec JaCoCo

### Comment fonctionne SonarCloud ?

À chaque **push** ou **pull request**, le workflow GitHub Actions :

1. Exécute les tests avec JaCoCo (comme vu précédemment)
2. Lance l'analyse SonarQube
3. Envoie les résultats à SonarCloud
4. Ajoute un rapport de qualité à votre PR

### Consulter les résultats sur SonarCloud

1. Allez sur https://sonarcloud.io
2. Connectez-vous avec votre compte GitHub
3. Sélectionnez l'organisation `benoit-charroux` et le projet `rent`
4. Vous verrez un **dashboard complet** avec :

**Métriques principales :**
- 📊 **Reliability** (Fiabilité) : Nombre de bugs détectés
- 🔒 **Security** (Sécurité) : Vulnérabilités et hotspots de sécurité
- 🧹 **Maintainability** (Maintenabilité) : Code smells et dettes techniques
- ✅ **Coverage** (Couverture) : Pourcentage de code testé (issu de JaCoCo)
- 🔄 **Duplications** : Pourcentage de code dupliqué

**Détails par fichier :**
- Cliquez sur **Issues** pour voir tous les problèmes identifiés
- Filtrez par sévérité : 🔴 Critical, 🟠 Major, 🟡 Minor, ℹ️ Info
- Consultez les **Pull Requests** pour voir l'historique des analyses

### Interpréter un rapport SonarCloud

**Exemple de qualité :**
- 🟢 **A** : Excellent, très peu de problèmes
- 🟢 **B** : Bon, quelques problèmes à corriger
- 🟡 **C** : Acceptable, mais besoin d'améliorations
- 🟠 **D** : Faible, plusieurs problèmes
- 🔴 **E** : Mauvais, nombreux problèmes

### Activer SonarCloud pour les Pull Requests

Le projet est configuré pour afficher des **statuts de qualité** directement dans les PRs :

1. Ouvrez une **Pull Request** sur GitHub
2. Descendez jusqu'à **Checks**
3. Vous verrez **SonarCloud Code Analysis**
4. Le statut peut être :
   - ✅ **Passed** : La qualité du code est acceptable
   - ❌ **Failed** : Des problèmes de sécurité ou de qualité critiques ont été détectés

### Bonnes pratiques pour la qualité

✅ **À faire :**
- Viser une note **A** ou **B** minimum
- Corriger les vulnérabilités de sécurité avant le merge
- Revoir les **bugs critiques** et **majeurs**
- Maintenir la couverture de tests **≥ 80%**

❌ **À éviter :**
- Merger une PR avec des **issues critiques de sécurité**
- Ignorer les rapports SonarCloud
- Augmenter la dette technique sans limite

### Configuration du projet

#### Organisation GitHub vs SonarCloud

- **Organisation GitHub** : `benoit-charroux` (votre compte d'organisation sur GitHub)
- **Organisation SonarCloud** : `benoit-charroux-1` (créée automatiquement lors de la liaison avec GitHub)

Ces deux organisations sont **liées automatiquement** grâce à l'authentification GitHub. SonarCloud a besoin d'une organisation distincte pour gérer les permissions et les projets.

#### Fichiers de configuration

La configuration SonarCloud se trouve dans deux fichiers :

1. **[MyService/build.gradle](MyService/build.gradle)** - Configuration SonarQube/Gradle
   - Clé du projet : `benoit-charroux_rent`
   - Organisation SonarCloud : `benoit-charroux-1`
   - URL hôte : `https://sonarcloud.io`
   - Chemins source/test et rapport JaCoCo

2. **[.github/workflows/action.yml](.github/workflows/action.yml)** - Workflow GitHub Actions
   - Lance l'analyse SonarCloud automatiquement à chaque push/PR
   - Exécute les tests et génère le rapport JaCoCo
   - Envoie les résultats à SonarCloud via le `SONAR_TOKEN`

#### Initialiser le SONAR_TOKEN

**Important** : Le `SONAR_TOKEN` est un secret nécessaire pour que GitHub Actions puisse envoyer les résultats à SonarCloud.

**Procédure :**

1. **Générer le token dans SonarCloud**
   - Allez sur https://sonarcloud.io
   - Cliquez sur votre profil (coin supérieur droit) → **Security**
   - Sous **Generate Tokens**, cliquez **Generate**
   - Donnez-lui un nom (ex: `github-rent`)
   - **Copiez le token** (vous ne pourrez pas le revoir après)

2. **Ajouter le secret à GitHub**
   - Allez sur https://github.com/benoit-charroux/rent
   - **Settings** → **Secrets and variables** → **Actions**
   - Cliquez **New repository secret**
   - **Name** : `SONAR_TOKEN`
   - **Value** : Collez le token SonarCloud
   - Cliquez **Add secret**

3. **Vérifier que c'est fonctionnel**
   - Faites un `git push` sur une branche
   - Allez dans **Actions** et vérifiez que le workflow s'exécute
   - L'étape **Build and analyze** doit réussir
   - Allez sur [SonarCloud Dashboard](https://sonarcloud.io/organizations/benoit-charroux-1) pour voir l'analyse

#### Technologies utilisées

Ce projet utilise :
- **SonarCloud Cloud** : Hébergement sur le cloud (gratuit pour les projets open-source)
- **Organisation SonarCloud** : `benoit-charroux-1`
- **Clé du projet** : `benoit-charroux_rent`
- **Intégration GitHub** : Rapports automatiques dans les PRs et checks
- **JaCoCo + SonarQube** : Analyse complète de la couverture et de la qualité

### Ressources utiles

- 🌐 [SonarCloud Dashboard](https://sonarcloud.io/organizations/benoit-charroux-1)
- 📖 [Documentation SonarCloud](https://docs.sonarcloud.io/)
- 🔐 [Règles de sécurité](https://rules.sonarsource.com/java/)

