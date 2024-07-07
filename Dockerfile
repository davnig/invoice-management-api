FROM gradle:jdk17 AS BUILD_STAGE
WORKDIR /usr/app
COPY . .
RUN gradle build || return 1

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=BUILD_STAGE /usr/app/build/libs/api.jar .
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "api.jar"]