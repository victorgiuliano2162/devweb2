# Stage 1: Build da aplicação Vue.js
FROM node:18-alpine AS build-stage

# Define o diretório de trabalho
WORKDIR /app

# Copia apenas os arquivos de dependências primeiro (cache otimizado)
COPY package*.json ./

# Instala TODAS as dependências (incluindo devDependencies para o build)
RUN npm install

# Copia todo o código fonte
COPY . .

# Gera o build otimizado para produção
RUN npm run build

# Stage 2: Servir com Nginx
FROM nginx:stable-alpine AS production-stage

# Remove a configuração padrão do Nginx
RUN rm /etc/nginx/conf.d/default.conf

# Copia a configuração customizada do Nginx
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Copia os arquivos buildados do stage anterior
COPY --from=build-stage /app/dist /usr/share/nginx/html

# Expõe a porta 80
EXPOSE 80

# Healthcheck para o container
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD wget --quiet --tries=1 --spider http://localhost/ || exit 1

# Inicia o Nginx em modo foreground
CMD ["nginx", "-g", "daemon off;"]