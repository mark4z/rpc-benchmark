FROM ubuntu as builder
ARG version=11.0.18.10-1
RUN set -eux \
    && apt-get update \
    && apt-get install -y --no-install-recommends \
        curl ca-certificates gnupg software-properties-common fontconfig java-common \
    && curl -fL https://apt.corretto.aws/corretto.key | apt-key add - \
    && add-apt-repository 'deb https://apt.corretto.aws stable main' \
    && mkdir -p /usr/share/man/man1 || true \
    && apt-get update \
    && apt-get install -y maven \
    && apt-get install -y java-11-amazon-corretto-jdk=1:$version \
    && apt-get purge -y --auto-remove -o APT::AutoRemove::RecommendsImportant=false \
        curl gnupg software-properties-common
ENV LANG C.UTF-8
ENV JAVA_HOME=/usr/lib/jvm/java-11-amazon-corretto

WORKDIR /app
COPY ./java/pom.xml .
COPY ./java/settings.xml .
RUN mvn dependency:go-offline -s settings.xml
COPY ./java/src ./src
RUN mvn clean package -DskipTests -s settings.xml

FROM amazoncorretto:11.0.18-alpine3.17
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
COPY ./ca/http.local.pem .
RUN keytool -import -alias vault -storepass changeit -keystore $JAVA_HOME/jre/lib/security/cacerts -noprompt -trustcacerts -file http.local.pem
ENTRYPOINT ["java","-jar","/app/app.jar","-Xms1800m","-Xms1800m","server.tomcat.max-threads=10000"]