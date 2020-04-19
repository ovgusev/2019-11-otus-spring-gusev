FROM openjdk:11-jre-slim
COPY /target/31-book-store-1.0.jar /app/book-store.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/book-store.jar"]
