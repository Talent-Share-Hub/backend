FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY build/libs/talent-share-hub-1.0.jar TalentShareHub.jar
ENTRYPOINT ["java","-jar","TalentShareHub.jar"]