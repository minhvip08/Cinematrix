FROM eclipse-temurin:21.0.2_13-jdk-alpine

EXPOSE 443
COPY /target/cinematrix*.jar /usr/local/lib/app.jar
RUN apk update && \
    apk add --no-cache ffmpeg
ENTRYPOINT ["java","-jar","/usr/local/lib/app.jar"]