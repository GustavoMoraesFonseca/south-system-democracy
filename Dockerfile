FROM maven:3-jdk-11 as builder
RUN mkdir -p /build
WORKDIR /build
COPY pom.xml /build
RUN mvn -B dependency:resolve dependency:resolve-plugins
COPY src /build/src
RUN mvn clean package -DskipTests

FROM openjdk:11-slim as runtime
EXPOSE 9091
ENV APP_HOME /southsystem-voto-v1-0.0.1-SNAPSHOT
ENV JAVA_OPTS=""
RUN mkdir $APP_HOME
RUN mkdir $APP_HOME/config
RUN mkdir $APP_HOME/log
VOLUME $APP_HOME/log
VOLUME $APP_HOME/config
WORKDIR $APP_HOME
COPY --from=builder /build/target/*.jar southsystem-voto-v1-0.0.1-SNAPSHOT.jar
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar southsystem-voto-v1-0.0.1-SNAPSHOT.jar" ]