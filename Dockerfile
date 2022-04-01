FROM maven:3.2.5-jdk-8u40

# Declarer l'emplacement de l'appli
# ENV APP_HOME /opt/spring-boot-journal
# RUN mkdir --parents $APP_HOME

RUN mkdir --parents /opt/issue-tracker-ws
WORKDIR /opt/issue-tracker-ws

# On ajoute le fichier POM de manière sélective
ADD pom.xml /opt/issue-tracker-ws

# get all the downloads out of the way
RUN mvn verify clean --fail-never


ADD . /opt/issue-tracker-ws
# pour construire le jar exécutable
RUN mvn package spring-boot:repackage