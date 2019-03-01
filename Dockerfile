FROM openjdk:8-jdk-alpine AS build
WORKDIR /app
COPY . ./
RUN ./gradlew --no-daemon --stacktrace clean bootJar


FROM openjdk:8-jre-alpine
RUN apk add --no-cache bash && \
	apk update
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar
CMD sleep 30 && java -jar app.jar