# Build stage: Java 21, Gradle
FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

# Gradle wrapper and project config
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Prefer offline + no-daemon; use dependency cache layer
RUN ./gradlew dependencies --no-daemon || true

COPY . .

RUN ./gradlew war --no-daemon -x test

# Runtime stage: Tomcat 11 + JRE 21
FROM tomcat:11.0-jre21

COPY --from=builder /app/build/libs/ROOT.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]