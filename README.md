# 🚀 Gestion des Incidents

## 📝 Description

Cette application **Java (Swing)** permet de gérer les incidents signalés par les utilisateurs. Elle offre les fonctionnalités suivantes :

✅ Signalement d'incidents (avec possibilité de joindre des fichiers de traces ou d'images)
✅ Consultation et suivi des incidents
✅ Assignation des incidents aux développeurs
✅ Traitement des incidents (ajout de commentaires, modification du statut)
✅ Gestion des utilisateurs (responsables, développeurs, rapporteurs)

---

## 🛠️ Technologies utilisées

🔹 **Langage :** Java  
🔹 **Interface utilisateur :** Swing  
🔹 **Base de données :** XAMPP (incluant MySQL et phpMyAdmin)  
🔹 **Gestion de dépendances :** Maven  
🔹 **Serveur web (développement) :** Apache  
🔹 **Système de contrôle de version :** Git  

---

## 📌 Prérequis

Avant de commencer, assurez-vous d'avoir les éléments suivants installés :

🔸 **JDK (Java Development Kit) :** Version 8 ou supérieure ([Télécharger JDK](https://www.oracle.com/java/technologies/downloads/))  
🔸 **IDE (Integrated Development Environment) :**  
&nbsp;&nbsp;&nbsp;&nbsp;➡️ [Eclipse](https://www.eclipse.org/downloads/)  
&nbsp;&nbsp;&nbsp;&nbsp;➡️ [IntelliJ IDEA](https://www.jetbrains.com/idea/download/)  
&nbsp;&nbsp;&nbsp;&nbsp;➡️ [NetBeans](https://netbeans.apache.org/download/)  
🔸 **Maven(optionnel) :** Version 3.6 ou supérieure ([Télécharger Maven](https://maven.apache.org/download.cgi))  
🔸 **XAMPP :** Serveur local incluant MySQL et phpMyAdmin ([Télécharger XAMPP](https://www.apachefriends.org/index.html))  
🔸 **Git :** Système de contrôle de version Git ([Télécharger Git](https://git-scm.com/downloads))  

---

## 🚀 Installation

1️⃣ **Clonez le dépôt via SSH :**

```bash
git clone git@github.com:PapaAbdoulayeNdiayeETP4A/gestion-incidents.git
```

2️⃣ **Importez le projet dans votre IDE :**
* **Eclipse :** `File` -> `Import...` -> `Maven` -> `Existing Maven Projects`
* **IntelliJ IDEA :** `File` -> `Open` et sélectionnez le répertoire du projet
* **NetBeans :** `File` -> `Open Project` et sélectionnez le répertoire du projet

3️⃣ **Configurez la base de données :**
* Lancez **XAMPP** et démarrez **MySQL** et **Apache**.
* Créez une base de données nommée `gestion_incidents` via **phpMyAdmin**.
* Modifiez le fichier `src/main/resources/application.properties` :
  ```properties
  spring.datasource.url=jdbc:mysql://localhost:3306/gestion_incidents
  spring.datasource.username=root
  spring.datasource.password=
  ```

4️⃣ **Installez les dépendances Maven :**

```bash
mvn install
```

---

## 🎯 Utilisation

▶ **Exécutez l'application :**
* Depuis votre IDE, exécutez la classe `Main.java` située dans le package `com.gestionincidents`.

▶ **Accédez à l'application :**
* L'application **Swing** s'ouvrira.

---

## 🤝 Contribution

Les contributions sont **les bienvenues** ! Voici comment contribuer au projet :

🔹 **Créez une branche :**

```bash
git checkout -b <nom_de_votre_branche>
```

🔹 **Effectuez vos modifications :**
* Modifiez le code, ajoutez des fonctionnalités, corrigez des bugs, etc.

🔹 **Commitez vos modifications :**

```bash
git add .
git commit -m "Description de vos modifications"
```

🔹 **Envoyez vos modifications :**

```bash
git push origin <nom_de_votre_branche>
```

🔹 **Créez une pull request :**
* Depuis la page de votre dépôt sur GitHub, créez une **pull request** pour soumettre vos modifications à l'équipe du projet.

---

## 👥 Auteurs

👤 **Papa Abdoulaye Ndiaye**  
👤 **Mouhamed Abdoulaye Ndoye**  

---

## 📜 Licence

Ce projet est sous **licence MIT**. 📝

