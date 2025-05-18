# Étape de build avec OpenJDK 17 et installation de Maven
FROM openjdk:17-slim AS build

# Installer Maven
RUN apt-get update && apt-get install -y maven

# Dossier de travail dans le conteneur
WORKDIR /app

# Copier le fichier pom.xml et télécharger les dépendances Maven
COPY pom.xml .

# Télécharger les dépendances Maven
RUN mvn dependency:go-offline

# Copier tout le code source
COPY src ./src

# Compiler le projet et créer le fichier JAR
RUN mvn clean install -DskipTests

# Étape d'exécution avec une image plus légère
FROM eclipse-temurin:17-jdk-alpine

# Dossier de travail dans le conteneur
WORKDIR /app

# Copier le JAR depuis l'étape de build
COPY --from=build /app/target/*.jar app.jar

# 🔥 Définir le profil actif pour Spring Boot
ENV SPRING_PROFILES_ACTIVE=docker

# Exposer le port de l'application
EXPOSE 9090

# Démarrer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]
