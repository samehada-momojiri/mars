FROM eclipse-temurin:25-jre
WORKDIR /app

COPY build/libs/mars-0.0.1-SNAPSHOT.jar app.jar
ADD https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v2.27.0/opentelemetry-javaagent.jar /opentelemetry-javaagent.jar

EXPOSE 8080
ENTRYPOINT ["java", "-Duser.language=java", "-Duser.country=JP", "-Duser.timezone=Asia/Tokyo", "-Dfile.encoding=UTF-8", "-javaagent:/opentelemetry-javaagent.jar", "-jar", "app.jar"]
