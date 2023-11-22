# Use a imagem do OpenJDK com Java 17
FROM amazoncorretto:17

# Adiciona o arquivo JAR da aplicação ao contêiner
COPY target/dining-room-0.0.1-SNAPSHOT.jar /app.jar

# Variáveis de ambiente
ENV DB_HOST=mysql
ENV DB_PORT=3306

# Expõe a porta em que a aplicação Spring Boot está rodando
EXPOSE 8080

# Comando para executar a aplicação quando o contêiner for iniciado
CMD ["java", "-jar", "/app.jar"]
