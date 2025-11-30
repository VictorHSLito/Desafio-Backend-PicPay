# Desafio-Backend-PicPay

![Java](https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Maven](https://img.shields.io/badge/Apache_Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)

---

## 📖 Sobre o Projeto

Este projeto é uma implementação de um sistema de backend para um desafio com foco em simular funcionalidades de uma carteira digital, inspirado no desafio técnico proposto pelo **PicPay** no link a seguir: https://github.com/PicPay/picpay-desafio-backend?tab=readme-ov-file. Ele foi desenvolvido para demonstrar habilidades em construção de APIs RESTful, manipulação de transações financeiras, validações de negócio, integração com serviços externos e banco de dados.

---

## ✨ Funcionalidades

O sistema oferece as seguintes funcionalidades principais:

* **Criação de Usuários**: Cadastro de usuários, diferenciando entre Lojistas e Usuários Comuns.
* **Criação de Carteiras**: Associações de carteiras entre usuários
* **Transferência de Valores**: Realização de transferências entre carteiras de usuários.
    * Validação de saldo suficiente.
    * Validação de tipo de usuário (lojistas não podem enviar, apenas receber).
    * Notificação de transação (simulada via API externa).
    * Autorização de transação (simulada via API externa).
* **Histórico de Transações**: Listagem de transações realizadas por um usuário.

---

## 🛠️ Tecnologias Utilizadas

* **Linguagem**: [Java 17](https://www.oracle.com/java/technologies/downloads/)
* **Framework**: [Spring Boot](https://spring.io/projects/spring-boot)
* **Web Framework**: [Spring Web](https://docs.spring.io/spring-framework/reference/web/webmvc.html)
* **Persistência**: [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
* **Banco de Dados**: [MySQL](https://www.mysql.com/)
* **Client HTTP Declarativo**: [Feign Client](https://github.com/OpenFeign/feign)
* **Testes Unitários/Integração**: [JUnit 5](https://junit.org/junit5/) e [Mockito](https://site.mockito.org/)
* **Gerenciador de Dependências**: [Maven](https://maven.apache.org/)

---

## 🚀 Como Rodar o Projeto Localmente

Siga os passos abaixo para configurar e executar o projeto em sua máquina local.

### Pré-requisitos

Certifique-se de ter as seguintes ferramentas instaladas:

* [Java Development Kit (JDK) 17](https://www.oracle.com/java/technologies/downloads/) ou superior
* [Maven](https://maven.apache.org/download.cgi)
* [Postgres](https://www.postgresql.org/) ou um banco de dados de sua preferência
* [Docker](https://www.docker.com/) (Opcional)

### Configuração

1.  **Clone o repositório:**
    ```bash
    git clone https://github.com/VictorHSLito/Desafio-Backend-PicPay.git
    ```
    ```bash
    cd Desafio-Backend-PicPay
    ```

2.  **Copie o arquivo `application.properties.example` e renomeie-o para `application.properties`**. Configure as propriedades desse novo arquivo, conforme exemplo abaixo, com as informações do seu banco de dados. Exemplo de configuração:

    ```
    spring.application.name=picpay-simplificado

    spring.datasource.url=jdbc:postgresql://localhost:5432/picpaydb
    spring.datasource.username=picpay-admin
    spring.datasource.password=1234
    spring.datasource.driver-class-name=org.postgresql.Driver

    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
    ```

3.  **Instale as dependências do projeto com o Maven:**
    ```bash
    mvn clean install
    ```
    Este comando compilará o código, rodará os testes e empacotará a aplicação.

4.  **Inicie a aplicação:**
    ```bash
    mvn spring-boot:run
    ```
    Ou se preferir executar o JAR gerado:
    ```bash
    java -jar target/Picpay-0.0.1-SNAPSHOT.jar # Verifique o nome exato do JAR gerado
    ```
    O servidor estará rodando em `http://localhost:8080` (ou na porta definida no `application.properties`).

5. **Teste os endpoints da API**:
    Após a aplicação iniciar, você poderá testar os endpoints através de ferramentas como **Postman**, **Insomnia** ou **cURL**. Verifique a próxima seção para a lista dos principais endpoints.
---

## 🚀 Como Rodar o Projeto Via Docker

Siga os passos abaixo para rodar o projeto via Docker:

1.  **Clone o repositório:**
    ```bash
    git clone https://github.com/VictorHSLito/Desafio-Backend-PicPay.git
    ```
    ```bash
    cd Desafio-Backend-PicPay
    ```
2.  **Copie o arquivo `application.properties.example` e renomeie-o para `application.properties`**. Configure as propriedades desse novo arquivo, conforme exemplo abaixo, com as informações do seu banco de dados. Exemplo de configuração:
    ```
    spring.application.name=picpay-simplificado

    spring.datasource.url=jdbc:postgresql://<nome_do_container_docker_nao_mais_localhost>:5432/picpaydb
    spring.datasource.username=picpay-admin
    spring.datasource.password=1234
    spring.datasource.driver-class-name=org.postgresql.Driver

    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
    ```

3. **Copie o arquivo `.env.example` e renomeie-o para `.env` e mude as configurações caso não queira utilizar as configurações padrões fornecidas no `docker-compose.yml`**.

#### Importante:

**As configurações do seu `application.properties` e do seu `.env` devem ser as mesmas, pois o .env é utilizado para sobrescrever as variáveis de ambiente do docker, enquanto que o `application.properties` serve para informar as variáveis de ambiente que a aplicação Java irá utilizar.**


## 🧪 Testes

Para rodar os testes da aplicação:

```bash
mvn test
```

## 📚 Endpoints da API

A API expõe os seguintes endpoints principais para interação:

**POST** `/user/create`: Cria um novo usuário (comum ou lojista).

**POST** `/wallet/create`: Cria uma nova carteira e associa com um usuário

**POST** `/transfer/`: Realiza uma nova transação (transferência de valores) entre carteiras.

## 🚀 Para o Futuro

Futuramente pretendo dar continuidade em algumas funções que ficaram faltando, dentre elas as que considero mais relevantes:

- **Aprimorar Cobertura de Testes**: Implementar mais testes automatizados (unitários, de integração e end-to-end) para garantir a robustez e a confiabilidade das funcionalidades existentes e futuras.

- ~~**Containerização Completa**: Utilizar ferramentas de containerização como Docker e Docker Compose para empacotar a aplicação e o banco de dados, facilitando o deploy em diferentes ambientes e garantindo a consistência.~~ **Realizado: Vide PR** [#1](https://github.com/VictorHSLito/Desafio-Backend-PicPay/pull/1)

- **Expandir Endpoints da API**: Adicionar novos endpoints para operações de GET, PUT e DELETE para recursos como usuários e carteiras, permitindo uma gestão mais completa e flexível dos dados.

- **Arquitetura Mais Robusta e Escalável**: Explorar e implementar padrões de arquitetura que visem maior resiliência, manutenibilidade e capacidade de escalonamento do sistema, como microsserviços, event-driven, ou outras abordagens que se adequem aos requisitos de um sistema financeiro.
