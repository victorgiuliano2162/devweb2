# Estágio 1: Build (Builder)
# Usa a imagem JDK completa para construir o .jar
FROM eclipse-temurin:17-jdk-jammy as builder
WORKDIR /build

# --- Otimização de Cache do Maven ---
# 1. Copia primeiro só o necessário para baixar as dependências
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# 2. Baixa as dependências (esta camada só será refeita se o pom.xml mudar)
RUN ./mvnw dependency:go-offline

# 3. Agora copia o código-fonte
COPY src ./src
# --- Fim da Otimização ---

# 4. Constrói o .jar (a pasta 'target' será criada AQUI, dentro do container 'builder')
RUN ./mvnw package -DskipTests

# ---

# Estágio 2: Imagem Final (Runtime)
# Usa uma imagem JRE leve (Alpine) para rodar a aplicação
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# ✅ A CORREÇÃO CRÍTICA ESTÁ AQUI:
# Copia o .jar do estágio 'builder' (de /build/target/) para o /app/app.jar
COPY --from=builder /build/target/*.jar app.jar

# Expõe a porta que a aplicação usa (ex: 8080)
EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]