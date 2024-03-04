FROM openjdk:17-oracle
WORKDIR /usr/src/app
COPY prediction/target/prediction-0.0.1-SNAPSHOT.jar .
COPY prediction/src/main/resources/application.properties .
RUN sed -i 's/localhost/host.docker.internal/g' application.properties
CMD ["java", "-jar", "prediction-0.0.1-SNAPSHOT.jar"]
EXPOSE 8002