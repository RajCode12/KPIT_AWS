FROM openjdk:11-jdk as build
WORKDIR /app
COPY . .
RUN ["./mvnw", "clean", "package", "-DskipTests"]

FROM openjdk:11-jdk 
COPY --from=build /app/target/library-api-1.0.jar /app/library-api-1.0.jar
EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/library-api-1.0.jar"]