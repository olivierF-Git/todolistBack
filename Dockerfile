FROM maven:3.2.5-jdk-8u40

# Declarer l'emplacement de l'appli
# ENV APP_HOME /opt/spring-boot-journal
# RUN mkdir --parents $APP_HOME

RUN mkdir --parents /opt/tache-tracker-ws
WORKDIR /opt/tache-tracker-ws

# On ajoute le fichier POM de manière sélective
ADD pom.xml /opt/tache-tracker-ws

# get all the downloads out of the way
RUN mvn verify clean --fail-never


ADD . /opt/tache-tracker-ws
# pour construire le jar exécutable
RUN mvn package spring-boot:repackage