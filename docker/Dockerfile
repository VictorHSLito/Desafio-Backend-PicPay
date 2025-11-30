#Imagem Java com Maven para build do .jar do projeto
FROM maven:3.8.3-openjdk-17 as build

# Cria um diretório /app
WORKDIR /app

# Copia os arquivos contidos dentro da pasta src e o pom.xml
COPY ./pom.xml .

COPY ./src ./src

# Flag -DskipTest faz com que não sejam executados testes da pasta /tests.
RUN mvn clean package -DskipTests

# Imagem leve do Java apenas para rodar o .jar
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=build /app/target/Picpay-0.0.1-SNAPSHOT.jar /app/app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]