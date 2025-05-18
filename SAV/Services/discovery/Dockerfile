# Étape de build avec OpenJDK 17 et installation de Maven
FROM openjdk:17-slim AS build

# Installer Maven
RUN apt-get update && apt-get install -y maven

# Dossier de travail dans le conteneur
WORKDIR /app

# Copier les fichiers Maven
COPY pom.xml .

# Télécharger les dépendances
RUN mvn dependency:go-offline

# Copier le code source
COPY src ./src

# Compiler l'application
RUN mvn clean install -DskipTests

# Étape finale : utiliser une image plus légère pour exécuter l'app
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copier le JAR compilé depuis l'étape précédente
COPY --from=build /app/target/*.jar app.jar

# Exposer le port Eureka
EXPOSE 8761

# Commande de démarrage
ENTRYPOINT ["java", "-jar", "app.jar"]
