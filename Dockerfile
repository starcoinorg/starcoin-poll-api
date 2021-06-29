FROM openjdk:8-jdk-alpine as build
WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN --mount=type=cache,target=/root/.m2 ./mvnw install -DskipTests

FROM openjdk:8-jdk-alpine
ARG STARCOIN_MYSQL_URL
ARG STARCOIN_MYSQL_USER
ARG STARCOIN_MYSQL_PWD

ENV STARCOIN_MYSQL_URL=$STARCOIN_MYSQL_URL
ENV STARCOIN_MYSQL_USER=$STARCOIN_MYSQL_USER
ENV STARCOIN_MYSQL_PWD=$STARCOIN_MYSQL_PWD
RUN addgroup -S starcoin && adduser -S starcoin -G starcoin
VOLUME /tmp
USER starcoin
ARG DEPENDENCY=/workspace/app/target
COPY --from=build ${DEPENDENCY}/starcoin-poll-api-0.0.1-SNAPSHOT.jar /app/lib/app.jar
ENTRYPOINT ["java","-noverify","-XX:TieredStopAtLevel=1","-jar","app/lib/app.jar","-Dspring.main.lazy-initialization=true","STARCOIN_MYSQL_URL=$STARCOIN_MYSQL_URL","STARCOIN_MYSQL_USER=$STARCOIN_MYSQL_USER","STARCOIN_MYSQL_PWD=$STARCOIN_MYSQL_PWD"]