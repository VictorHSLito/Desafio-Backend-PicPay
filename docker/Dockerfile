FROM maven:3.8.3-openjdk-17 AS build

WORKDIR /app

COPY ./pom.xml .

RUN mvn dependency:go-offline -DskipTest

COPY ./src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=build /app/target/Picpay-0.0.1-SNAPSHOT.jar /app/app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]