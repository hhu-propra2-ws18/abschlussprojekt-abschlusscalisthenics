FROM openjdk:8-jdk-alpine as build-env
WORKDIR ./
COPY ./ ./
RUN ./gradlew bootJar


FROM openjdk:8-jre-alpine
WORKDIR ./
COPY --from=build-env /build/libs/*.jar *.jar
ENTRYPOINT ["java", "-jar", "*.jar"]