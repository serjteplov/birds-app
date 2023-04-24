FROM gradle:jdk11 as builder
MAINTAINER serj.teplov@gmail.com
WORKDIR /app
COPY ./ ./birds-app
WORKDIR /app/birds-app/birds-app-api-base-v1
RUN --mount=type=cache,target=./.gradle gradle clean build publishToMavenLocal
WORKDIR /app/birds-app/birds-app-ktor
RUN --mount=type=cache,target=./.gradle gradle clean build

FROM openjdk:11 as backend
WORKDIR /birds-app-jar
COPY --from=builder /app/birds-app/birds-app-ktor/build/libs ./
ENTRYPOINT java -jar birds-app-ktor-0.0.1-all.jar
