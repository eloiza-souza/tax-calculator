# API de Cálculo de Impostos com Spring Boot, TDD, Spring Security e JWT 🚀

## Descrição do Projeto
Este projeto é uma API RESTful desenvolvida para gerenciar e calcular impostos no Brasil. A API permite o registro de diferentes tipos de impostos, como ICMS, ISS, IPI, entre outros, e realiza cálculos com base no tipo de imposto e no valor base fornecido. Além disso, a API é segura, utilizando Spring Security e JWT (JSON Web Token) para autenticação e autorização.

## Funcionalidades
- **Gerenciamento de Tipos de Impostos**:
  - Lista todos os tipos de impostos disponíveis.
  - Cadastra novos tipos de impostos (nome, descrição e alíquota).
  - Obtem detalhes de um tipo de imposto específico pelo ID.
  - Exclui um tipo de imposto pelo ID.
  
- **Cálculo de Impostos**:
  - Calcula o valor do imposto com base no tipo de imposto e no valor base fornecido.
  
- **Segurança**:
  - Implementação de autenticação e autorização utilizando Spring Security e JWT.
  - Apenas usuários autenticados podem acessar os endpoints.
  - O acesso a endpoints de criação, exclusão e cálculo de impostos é restrito para usuários com o papel de ADMIN.

## Tecnologias Utilizadas 🛠️
- **Java 17**
- **Spring Boot**
- **Spring Security**
- **JWT (JSON Web Token)**
- **PostgreSQL**
- **Swagger/OpenAPI**
- **JUnit** para testes unitários
- **Maven** para gerenciamento de dependências

## Endpoints Disponíveis
### Gerenciamento de Impostos
- **GET /api/tax/tipos**: Retorna a lista de todos os tipos de impostos cadastrados.
- **POST /api/tax/tipos**: Cadastra um novo tipo de imposto. (Acesso restrito ao papel ADMIN)
- **GET /api/tax/tipos/{id}**: Retorna os detalhes de um tipo de imposto específico pelo ID.
- **DELETE /api/tax/tipos/{id}**: Exclui um tipo de imposto pelo ID. (Acesso restrito ao papel ADMIN)

### Cálculo de Impostos
- **POST /api/tax/calculo**: Calcula o valor do imposto com base no tipo de imposto e no valor base fornecido. (Acesso restrito ao papel ADMIN)

### Gerenciamento de Usuários
- **POST /api/tax/user/register**: Registra um novo usuário no sistema.
- **POST /api/tax/user/login**: Autentica um usuário e retorna um token JWT.

## Segurança 🔒
- **Autenticação**: Utiliza JWT para autenticar usuários.
- **Autorização**: Apenas usuários com o papel ADMIN podem acessar endpoints sensíveis, como criação, exclusão e cálculo de impostos.
- **Hashing de Senhas**: As senhas dos usuários são armazenadas de forma segura utilizando BCrypt.

## Instruções para Configuração e Execução
### Pré-requisitos
- **Java 17** ou superior
- **Maven**
- **PostgreSQL**

### Passos para Configuração
1. Clone o repositório:
   ```bash
   git clone https://github.com/eloiza-souza/tax-calculator.git
   cd tax-calculator
   ```

2. Configure o banco de dados no arquivo `application.yml`:
   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5432/tax_calculator
       username: postgres
       password: admin
   ```

3. Compile e execute o projeto:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

4. Acesse a documentação Swagger para testar os endpoints:
   - URL: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Exemplos de Uso
### 1. Registro de Imposto
**Requisição**:
POST /api/tax/tipos
```json
{
  "name": "IPI",
  "description": "Imposto sobre Produtos Industrializados",
  "rate": 12.0
}
```

**Resposta**:
```json
{
  "id": 3,
  "name": "IPI",
  "description": "Imposto sobre Produtos Industrializados",
  "rate": 12.0
}
```

### 2. Cálculo de Imposto
POST /api/tax/calculo
**Requisição**:

```json
{
  "taxId": 1,
  "baseValue": 1000.0
}
```

**Resposta**:
```json
{
  "taxName": "ICMS",
  "baseValue": 1000.0,
  "rate": 18.0,
  "taxCalculated": 180.0
}
```

## Testes ✅
- Os testes foram implementados utilizando **JUnit**.
- Para executar os testes, utilize o comando:
  ```bash
  mvn test
  ```

## Contribuição
Contribuições são bem-vindas! 🎉 Sinta-se à vontade para abrir issues ou enviar pull requests.

