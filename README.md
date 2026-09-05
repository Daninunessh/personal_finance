# 💰 Personal Finance API

[![Java 21](https://img.shields.io/badge/Java-25-orange?style=flat-square&logo=java)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-brightgreen?style=flat-square&logo=spring)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?style=flat-square&logo=postgresql)](https://www.postgresql.org/)

API RESTful para gerenciamento de finanças pessoais, desenvolvida com **Java 25** e **Spring Boot**. O projeto aplica os princípios da **Clean Architecture** para garantir testabilidade, baixo acoplamento e independência de frameworks no núcleo de domínio.

---

## 🎯 Sobre o Projeto

O **Personal Finance API** é uma aplicação backend para controle de finanças pessoais.
Através da API, será possível:

### Principais Recursos
- 👤 Gerenciar usuários
- 🏦 Gerenciar contas financeiras
- 🏷️ Gerenciar categorias de receitas e despesas
- 💸 Registrar transações
- 📊 Consultar informações financeiras
- 💳 Gerenciar cartões de crédito
- 🎯 Criar e acompanhar metas financeiras

O projeto será desenvolvido de forma incremental, começando pelos recursos básicos de CRUD e evoluindo para funcionalidades mais avançadas, como autenticação, testes automatizados e Docker.

---

## 🛠️ Tech Stack

- **Linguagem & Framework:** Java 21, Spring Boot (Web, Data JPA, Validation, Security)
- **Banco de Dados:** PostgreSQL
- **Testes:** JUnit 5, Mockito
- **Infraestrutura & DevOps:** Docker, Docker Compose, GitHub Actions, AWS *(planejado)*
- **Documentação:** OpenAPI / Swagger *(planejado)*

---

## 🏗️ Arquitetura

A estrutura do projeto é inspirada nos conceitos de **Clean Architecture** e **DDD**, garantindo a inversão de dependência em direção ao domínio.

```text
src/main/java/com/financeapi
├── application      # Casos de uso, DTOs e interfaces de repositório
├── domain           # Entidades de negócio e regras puras
├── infrastructure   # Implementações JPA, integrações externas e configurações
└── presentation     # Controllers REST e mapeamento de DTOs

```

# 🐳 Executando o projeto

## Pré-requisitos

Certifique-se de ter instalado:

- Java 25
- Maven
- PostgreSQL (ou Docker, futuramente)

---

## Configuração do banco

Crie um banco PostgreSQL:

```sql
CREATE DATABASE finance_api;
```

Configurar o Banco de Dados:
Crie um banco de dados PostgreSQL local ou altere as credenciais no **src/main/resources/application.properties**:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/finance_api
spring.datasource.username=postgres
spring.datasource.password=postgres
```

---

## Executando a aplicação

Clone o projeto:

```bash
  git clone https://github.com/seu-usuario/personal-finance-api.git
```

Execute:

```bash
  # Linux/macOS
  ./mvnw spring-boot:run

  # Windows
  mvnw.cmd spring-boot:run
```

A aplicação estará disponível em:

```text
http://localhost:8080
```

---
# 👩‍💻 Autora

**Danielle Nunes**

Desenvolvedora Backend Java | Software Engineer | APIs REST & Microsserviços | Spring Boot | AWS Certified

---