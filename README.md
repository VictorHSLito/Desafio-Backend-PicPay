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

## 🚀 Como Rodar o Projeto

Siga os passos abaixo para configurar e executar o projeto em sua máquina local.

### Pré-requisitos

Certifique-se de ter as seguintes ferramentas instaladas:

* [Java Development Kit (JDK) 17](https://www.oracle.com/java/technologies/downloads/) ou superior
* [Maven](https://maven.apache.org/download.cgi)
* [MySQL](https://dev.mysql.com/downloads/mysql/) ou um banco de dados de sua preferência

### Configuração

1.  **Clone o repositório:**
    ```bash
    git clone [https://github.com/VictorHSLito/Desafio-Backend-PicPay.git](https://github.com/VictorHSLito/Desafio-Backend-PicPay.git)
    cd Desafio-Backend-PicPay
    ```

2.  **Configure o arquivo `application.properties` com as informações do seu banco de dados:**
    Dentro do diretório do projeto, especificamente dentro da pasta `resources` há o arquivo **`application.properties`** em que é necessário configurar os parâmetros do banco de dados. Um exemplo de configuração:

    ```
    spring.application.name=picpay-simplificado

    spring.datasource.url=jdbc:mysql://localhost:3306/picpay
    spring.datasource.username=root
    spring.datasource.password=
    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
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
    java -jar target/desafio-backend-picpay-0.0.1-SNAPSHOT.jar # Verifique o nome exato do JAR
    ```
    O servidor estará rodando em `http://localhost:8080` (ou na porta definida no `application.properties`).

5. **Teste os endpoints da API**:
    Após a aplicação iniciar, você poderá testar os endpoints através de ferramentas como **Postman**, **Insomnia** ou **cURL**. Verifique a próxima seção para a lista dos principais endpoints.
---

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

- **Containerização Completa**: Utilizar ferramentas de containerização como Docker e Docker Compose para empacotar a aplicação e o banco de dados, facilitando o deploy em diferentes ambientes e garantindo a consistência.

- **Expandir Endpoints da API**: Adicionar novos endpoints para operações de GET, PUT e DELETE para recursos como usuários e carteiras, permitindo uma gestão mais completa e flexível dos dados.

- **Arquitetura Mais Robusta e Escalável**: Explorar e implementar padrões de arquitetura que visem maior resiliência, manutenibilidade e capacidade de escalonamento do sistema, como microsserviços, event-driven, ou outras abordagens que se adequem aos requisitos de um sistema financeiro.
