# Sistema de Gerenciamento de Tickets de Reparo

**Desenvolvimento Web 2**  
Projeto submetido à disciplina de Desenvolvimento Web 2.

## Integrantes

- Victor Giuliano
- Arthur Santos

## Tecnologias Utilizadas

### Backend
- **Java 17**
- **Spring Boot** (com Spring Security, Spring Data JPA, Spring Cache)
- **JWT** (JSON Web Tokens) para autenticação e autorização
- **Caffeine** para cache local
- **PostgreSQL** como banco de dados

### Frontend
- **Vue.js** (Vue 3)
- **Vite** como ferramenta de build

## Objetivo do Projeto

A aplicação tem como objetivo ser uma **plataforma de gerenciamento de criação e acompanhamento de tickets de reparo** no ambiente profissional.

Funcionalidades principais:
- Criação, visualização, edição e acompanhamento de chamados técnicos
- Controle de acesso baseado em **roles** (cargos) com diferentes níveis de permissão
- Autenticação segura via JWT
- Filtragem avançada de chamados (por setor, tipo, responsável, data, status, etc.)
- Performance otimizada com uso de cache (Caffeine)

## Arquitetura

O projeto segue uma arquitetura cliente-servidor clássica:

- **Backend**: API RESTful desenvolvida em Spring Boot, responsável pela lógica de negócio, segurança e persistência de dados.
- **Frontend**: Aplicação SPA (Single Page Application) em Vue.js, consumindo a API do backend.
- **Banco de Dados**: PostgreSQL para armazenamento persistente.

## Funcionalidades Principais

- Cadastro e autenticação de usuários
- Criação e gerenciamento de chamados técnicos
- Diferentes níveis de acesso conforme o cargo do usuário (ex: Diretor, Gerente, Técnico)
- Listagem e filtragem avançada de chamados
- Cache de consultas frequentes para melhor performance

## Como Executar o Projeto

### Pré-requisitos
- Java 17
- Node.js (para o frontend)
- PostgreSQL
- Maven ou Gradle

### Backend
1. Clone o repositório
2. Configure o banco de dados no `application.yml` ou `application.properties`
3. Execute:
   ```bash
   ./mvnw spring-boot:run
