FROM openjdk:11-jdk-alpine as builder
WORKDIR /app
COPY ./java .
RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories && apk add maven
RUN mvn clean package -DskipTests -s settings.xml

FROM openjdk:8-jdk-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app/app.jar","-Xms1800m","-Xms1800m"]