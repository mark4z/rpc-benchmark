FROM openjdk:8-jdk-alpine as builder
WORKDIR /app
COPY ./java .
RUN apk add maven && mvn clean package -DskipTests -s ./settings.xml

FROM openjdk:8-jdk-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app/app.jar","-Xms1800m","-Xms1800m"]