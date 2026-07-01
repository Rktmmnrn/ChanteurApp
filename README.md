# ChanteurApp

ChanteurApp est une application Android native en Java permettant de gérer une liste de chanteurs favoris. Elle propose un écran d'accueil, une liste principale, un formulaire d'ajout/modification, une recherche en temps réel et un stockage local SQLite.

## Fonctionnalites

- Afficher tous les chanteurs enregistres, tries par nom.
- Ajouter un chanteur avec son nom, sa date de naissance et une photo optionnelle.
- Modifier un chanteur existant depuis la liste ou depuis les resultats de recherche.
- Supprimer un chanteur depuis la liste.
- Rechercher un chanteur par son nom en temps reel.
- Selectionner une photo depuis la galerie avec conservation de l'URI.
- Afficher un splash screen avant l'ecran principal.

## Technologies

- Android natif
- Java 11
- Gradle Kotlin DSL
- Android Gradle Plugin 9.2.1
- Gradle Wrapper 9.4.1
- SQLite via `SQLiteOpenHelper`
- AndroidX AppCompat
- RecyclerView
- CardView
- Material Components
- ConstraintLayout

## Configuration Android

Le module principal est `:app`.

| Element | Valeur |
| --- | --- |
| Package / namespace | `com.eni.chanteurapp` |
| Application ID | `com.eni.chanteurapp` |
| minSdk | 24 |
| targetSdk | 36 |
| compileSdk | 36.1 |
| versionName | `1.0` |
| versionCode | `1` |

L'application demande l'acces aux images:

- `READ_MEDIA_IMAGES` pour Android 13 et plus.
- `READ_EXTERNAL_STORAGE` jusqu'a Android 12.

## Structure du projet

```text
.
├── app/
│   ├── build.gradle.kts
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/com/eni/chanteurapp/
│       │   ├── SplashActivity.java
│       │   ├── MainActivity.java
│       │   ├── AddEditActivity.java
│       │   ├── SearchActivity.java
│       │   ├── ChanteurAdapter.java
│       │   ├── database/DBHelper.java
│       │   └── model/Chanteur.java
│       └── res/
│           ├── layout/
│           ├── drawable/
│           ├── mipmap-*/
│           ├── values/
│           └── xml/
├── build.gradle.kts
├── settings.gradle.kts
├── gradle/libs.versions.toml
└── gradlew
```

## Architecture de l'application

### `SplashActivity`

Point d'entree de l'application. Elle affiche le theme splash pendant environ 2 secondes, puis ouvre `MainActivity`.

### `MainActivity`

Ecran principal. Il initialise la base SQLite, affiche les chanteurs dans un `RecyclerView`, gere le bouton d'ajout, le bouton de recherche et la suppression avec confirmation.

### `AddEditActivity`

Ecran de formulaire utilise pour deux cas:

- ajout d'un nouveau chanteur lorsque aucun identifiant n'est fourni;
- modification d'un chanteur lorsque l'intent contient l'extra `id`.

La photo est choisie via `ACTION_OPEN_DOCUMENT`, puis l'URI est stockee dans la base locale.

### `SearchActivity`

Ecran de recherche. Un `TextWatcher` lance une recherche SQLite a chaque modification du champ texte. Les resultats utilisent le meme adapter que la liste principale.

### `ChanteurAdapter`

Adapter RecyclerView charge d'afficher chaque chanteur avec:

- photo;
- nom;
- date de naissance;
- action de suppression.

Les actions sont remontees aux Activities via l'interface `OnChanteurClickListener`.

### `DBHelper`

Classe d'acces aux donnees basee sur `SQLiteOpenHelper`. Elle centralise les operations CRUD:

- `addChanteur`
- `getAllChanteurs`
- `getChanteurById`
- `updateChanteur`
- `deleteChanteur`
- `searchChanteurs`

## Modele de donnees

La base locale s'appelle `chanteurs.db`. Elle contient une table `chanteur`.

| Colonne | Type | Description |
| --- | --- | --- |
| `idchant` | `INTEGER PRIMARY KEY AUTOINCREMENT` | Identifiant unique |
| `nom` | `TEXT NOT NULL` | Nom du chanteur |
| `datenais` | `TEXT` | Date de naissance |
| `photo` | `TEXT` | URI de la photo selectionnee |

Le modele Java correspondant est `Chanteur`.

## Installation et execution

### Prerequis

- Android Studio recent.
- JDK compatible Java 11.
- SDK Android installe avec la plateforme cible utilisee par le projet.
- Un emulateur Android ou un appareil physique.

### Depuis Android Studio

1. Ouvrir le dossier du projet dans Android Studio.
2. Laisser Gradle synchroniser le projet.
3. Selectionner un appareil ou un emulateur.
4. Lancer la configuration `app`.

### Depuis le terminal

Rendre le wrapper executable si necessaire:

```bash
chmod +x ./gradlew
```

Compiler l'application:

```bash
./gradlew assembleDebug
```

Installer la version debug sur un appareil connecte:

```bash
./gradlew installDebug --no-configuration-cache
```

Lancer les tests unitaires:

```bash
./gradlew test
```

Lancer les tests instrumentes:

```bash
./gradlew connectedAndroidTest
```

## Ressources principales

- `app/src/main/res/layout/activity_main.xml`: ecran principal et liste.
- `app/src/main/res/layout/activity_add_edit.xml`: formulaire d'ajout/modification.
- `app/src/main/res/layout/activity_search.xml`: ecran de recherche.
- `app/src/main/res/layout/item_chanteur.xml`: cellule de liste.
- `app/src/main/res/drawable/splash_background.xml`: fond du splash screen.
- `app/src/main/res/values/colors.xml`: couleurs principales.
- `app/src/main/res/values/themes.xml`: themes de l'application.

## Points d'attention

- Les tests presents sont les tests d'exemple generes par Android Studio; ils ne couvrent pas encore les comportements metier.
- La date de naissance est stockee sous forme de texte. Il n'y a pas encore de validation stricte du format.
- Les images sont referencees par URI, pas copiees dans le stockage interne de l'application.
- En cas de modification du schema SQLite, il faudra augmenter `DB_VERSION` dans `DBHelper` et adapter `onUpgrade`.
- Dans `item_chanteur.xml`, le bouton de suppression est un `ImageButton`; l'adapter doit utiliser un type compatible pour eviter les erreurs de cast a l'execution.
- Dans `activity_main.xml`, le message de liste vide est place dans `layoutVide`; l'affichage/masquage doit rester coherent entre le conteneur et `tvVide`.

## Pistes d'amelioration

- Ajouter une validation de date ou un `DatePicker`.
- Ajouter des tests unitaires pour `DBHelper`.
- Remplacer `notifyDataSetChanged()` par `DiffUtil` dans le RecyclerView.
- Ajouter une confirmation de suppression dans l'ecran de recherche.
- Centraliser les textes visibles dans `strings.xml` pour faciliter la traduction.
- Gerer une image par defaut plus adaptee qu'une icone launcher.
