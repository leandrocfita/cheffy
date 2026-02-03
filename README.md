# Cheffy - Sistema de Gestão para Restaurantes

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-18.0-blue.svg)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-Enabled-2496ED.svg)](https://www.docker.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

## 📋 Sobre o Projeto

Cheffy é um sistema backend robusto desenvolvido para gerenciar operações de múltiplos restaurantes através de uma plataforma compartilhada. O projeto foi criado como parte do Tech Challenge do curso de Pós-Graduação em Arquitetura e Desenvolvimento Java da FIAP.

### Problema

Um grupo de restaurantes na região identificou a necessidade de um sistema de gestão compartilhado devido aos altos custos de sistemas individuais. A solução permite que clientes escolham restaurantes com base na qualidade da comida, enquanto os estabelecimentos compartilham uma plataforma eficiente para gerenciar suas operações.

### Objetivo

Desenvolver um backend completo utilizando Spring Boot, implementando gestão de usuários, autenticação segura, e preparando a base para futuras funcionalidades como gestão de restaurantes, cardápios e pedidos.

## 🚀 Tecnologias Utilizadas

### Core
- **Java 21** - Linguagem de programação
- **Spring Boot 3.5.7** - Framework principal
- **Maven** - Gerenciamento de dependências

### Frameworks e Bibliotecas
- **Spring Data JPA** - Persistência de dados
- **Spring Security** - Autenticação e autorização
- **Spring Validation** - Validação de dados
- **Hibernate** - ORM
- **PostgreSQL** - Banco de dados relacional
- **Flyway** - Versionamento e migração de banco de dados
- **JWT (jjwt)** - Tokens de autenticação
- **MapStruct** - Mapeamento de objetos
- **Lombok** - Redução de boilerplate
- **SpringDoc OpenAPI** - Documentação Swagger

### DevOps
- **Docker** - Containerização
- **Docker Compose** - Orquestração de containers

## 🔧 Pré-requisitos

- [Docker Desktop](https://www.docker.com/products/docker-desktop/) instalado e em execução
- [Git](https://git-scm.com/) instalado
- Portas 8080 (aplicação) e 5432 (PostgreSQL) disponíveis

## 🚀 Como Executar

### 1. Clonar o Repositório

```bash
git clone https://github.com/thiagosslima/cheffy.git
cd cheffy
```

### 2. Configurar Variáveis de Ambiente

Crie um arquivo `.env` na raiz do projeto:

```bash
touch .env
```

Adicione o seguinte conteúdo:

```env
POSTGRES_PASSWORD=postgres123
DB_HOST=postgres
DB_PORT=5432
JWT_SECRET=chave_secreta_jwt_minimo_256_bits_para_seguranca_adequada
```

### 3. Iniciar a Aplicação

```bash
docker compose up --build
```

Este comando irá:
- Baixar a imagem do PostgreSQL 18.0
- Construir a imagem da aplicação
- Iniciar o banco de dados
- Executar migrations do Flyway automaticamente
- Iniciar a aplicação Spring Boot
- Popular os perfis iniciais (CLIENT e OWNER)

### 4. Acessar a Aplicação

- **API Base URL**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

### 5. Testar a API

#### Criar um usuário:
```bash
curl -X POST http://localhost:8080/api/v1/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "João Silva",
    "email": "joao@email.com",
    "login": "joao.silva",
    "password": "SenhaForte@123456",
    "profileType": "CLIENT",
    "address": {
      "streetName": "Rua Teste",
      "number": 123,
      "city": "São Paulo",
      "postalCode": "12345678",
      "neighborhood": "Centro",
      "stateProvince": "SP",
      "main": true
    }
  }'
```

#### Fazer login:
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "login": "joao.silva",
    "password": "SenhaForte@123456"
  }'
```

#### Buscar usuário (com token):
```bash
curl -X GET http://localhost:8080/api/v1/users/name/João%20Silva \
  -H "Authorization: Bearer SEU_TOKEN_AQUI"
```

### 6. Parar a Aplicação

```bash
docker compose down
```

Para remover também os volumes (dados do banco):
```bash
docker compose down -v
```

## 📚 Documentação

### Swagger UI
Acesse http://localhost:8080/swagger-ui.html para visualizar e testar todos os endpoints da API de forma interativa.

### Database Migrations
O projeto utiliza Flyway para versionamento do banco de dados. Consulte [Database Migrations Guide](src/main/resources/db/migration/README.md) para mais informações sobre como criar e gerenciar migrations.

### Collection Postman
Importe o arquivo [Cheffy_API_Collection.json](https://github.com/thiagosslima/cheffy/blob/develop/src/main/resources/Cheffy_Postman_Collection.json) no Postman para testar todos os cenários:
- Autenticação
- CRUD de usuários
- Gerenciamento de endereços
- Validações e tratamento de erros

## 🏗️ Arquitetura

O projeto segue Clean Architecture com separação clara de responsabilidades:

```
┌─────────────────────────────────────┐
│      Presentation Layer             │  ← Controllers, DTOs, Mappers
├─────────────────────────────────────┤
│      Application Layer              │  ← Use Cases, Application DTOs
├─────────────────────────────────────┤
│        Domain Layer                 │  ← Entities, Ports, Business Rules
├─────────────────────────────────────┤
│     Infrastructure Layer            │  ← JPA, Security, Flyway Config
├─────────────────────────────────────┤
│         Database Layer              │  ← PostgreSQL + Flyway Migrations
└─────────────────────────────────────┘
```

### Estrutura de Diretórios
```
src/main/
├── java/br/com/fiap/cheffy/
│   ├── presentation/        # Controllers, DTOs Web
│   ├── application/         # Use Cases
│   ├── domain/              # Entities, Ports
│   ├── infrastructure/      # JPA, Config, Security
│   └── shared/              # Exceptions, Utils
└── resources/
    └── db/migration/        # Flyway Migrations
```

## 🔐 Segurança

- **Autenticação JWT**: Tokens stateless com expiração de 1 hora
- **BCrypt**: Criptografia de senhas com salt automático
- **Validação de Senha Forte**: Mínimo de 12 caracteres
- **HTTPS Ready**: Preparado para uso com certificados SSL/TLS
- **CORS**: Configurável para ambientes de produção
- **SQL Injection Protection**: Uso de JPA/Hibernate com prepared statements

## 🛠️ Desenvolvimento Local (Sem Docker)

### Pré-requisitos
- Java 21
- Maven 3.8+
- PostgreSQL 18.0

### Configuração
1. Instale e configure o PostgreSQL
2. Crie o banco de dados `cheffy`
3. Configure as variáveis de ambiente
4. Execute:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

## 👥 Equipe

- Leandro Fita
- Igor Costa
- Rodrigo Ferreira
- Thiago Soares
- Victor Reis

## 📄 Licença

Este projeto foi desenvolvido como parte do Tech Challenge da FIAP e é disponibilizado para fins educacionais.

## 🤝 Contribuindo

Este é um projeto acadêmico, mas sugestões e feedback são bem-vindos!

## 📞 Contato

Para dúvidas ou sugestões, abra uma issue no repositório.

---

**Desenvolvido pela equipe Cheffy - FIAP 2026**
