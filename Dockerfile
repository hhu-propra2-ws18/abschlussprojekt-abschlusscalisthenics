FROM openjdk:8-jdk-alpine as build-env
WORKDIR ./
COPY ./ ./
RUN ./gradlew bootJar


FROM openjdk:8-jre-alpine
WORKDIR ./
COPY --from=build-env /build/libs/projekt-scs*.jar projekt-scs.jar
ENTRYPOINT ["java", "-jar", "projekt-scs.jar"]