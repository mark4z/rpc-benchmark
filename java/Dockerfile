FROM openjdk:8-jdk-alpine as builder
WORKDIR /app
COPY ./java/pom.xml .
RUN mvn dependency:go-offline
COPY ./java/src ./src
RUN apk add maven && mvn clean package -DskipTests

FROM openjdk:8-jdk-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app/app.jar","-Xms1800m","-Xms1800m"]