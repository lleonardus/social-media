FROM maven AS MAVEN_BUILD
COPY ./ ./
RUN mvn clean package

FROM openjdk
COPY --from=MAVEN_BUILD /target/social-media-0.0.1-SNAPSHOT.jar /app.jar
CMD ["java", "-jar", "/app.jar"]