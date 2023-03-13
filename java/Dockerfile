FROM amazoncorretto:11.0.18-alpine3.17 as builder
WORKDIR /app
COPY ./java/pom.xml .
COPY ./java/settings.xml .
RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories && apk add maven
RUN mvn dependency:go-offline -s settings.xml
COPY ./java/src ./src
RUN mvn clean package -DskipTests -s settings.xml

FROM amazoncorretto:11.0.18-alpine3.17
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
COPY ./ca/http.local.pem .
RUN keytool -import -alias vault -storepass changeit -keystore $JAVA_HOME/jre/lib/security/cacerts -noprompt -trustcacerts -file http.local.pem
ENTRYPOINT ["java","-jar","/app/app.jar","-Xms1800m","-Xms1800m","server.tomcat.max-threads=10000"]