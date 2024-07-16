FROM openjdk:17

# Informar el puerto donde se ejecuta el contenedor (informativo)
EXPOSE 8080

# Definir directorio raíz de nuestro contenedor
WORKDIR /canchamanager

# Copiar y pegar archivos dentro del contenedor
COPY ./pom.xml ./pom.xml
COPY ./.mvn ./.mvn
COPY ./mvnw ./mvnw

# Dar permisos de ejecución al script mvnw
RUN chmod +x ./mvnw

# Descargar las dependencias
RUN ./mvnw dependency:go-offline

# Copiar el código fuente dentro del contenedor
COPY ./src ./src

# Construir nuestra aplicación
RUN ./mvnw clean install -DskipTests

# Levantar nuestra aplicación cuando el contenedor inicie
ENTRYPOINT ["java","-jar","./target/CanchaManager-0.0.1-SNAPSHOT.jar"]

# Prueba
