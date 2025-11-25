FROM eclipse-temurin:17-jdk

# Configura o timezone
ENV TZ=America/Sao_Paulo
RUN apt-get update && apt-get install -y tzdata \
    && ln -sf /usr/share/zoneinfo/$TZ /etc/localtime \
    && echo $TZ > /etc/timezone \
    && dpkg-reconfigure -f noninteractive tzdata

# Diretório temporário
VOLUME /tmp

# Copia o JAR gerado para dentro do container
COPY target/*.jar app.jar

# Comando de inicialização do Spring Boot
ENTRYPOINT ["java", "-Duser.timezone=America/Sao_Paulo", "-jar", "app.jar"]
