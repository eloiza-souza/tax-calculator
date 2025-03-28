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
- **JaCoCo** para análise de cobertura de testes

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
1. Clone o repositório (escolha uma das opções abaixo):
- Usando HTTPS:
 ```bash
 git clone https://github.com/eloiza-souza/tax-calculator.git
 ```
- Usando SSH (requer configuração de chave SSH):
 ```bash
 git clone git@github.com:eloiza-souza/tax-calculator.git
 ```

2. Configure as variáveis de ambiente para o banco de dados:
```bash
export DATABASE_URL=jdbc:postgresql://localhost:5432/tax_calculator
export DATABASE_USERNAME=postgres
export DATABASE_PASSWORD=admin
export SERVER_PORT=8080
```

3. Compile e execute o projeto:
```bash
mvn clean install
mvn spring-boot:run
```

4. Acesse a documentação Swagger para testar os endpoints:
- URL: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Processo de Autenticação no Swagger 🔒
A API utiliza o Swagger para documentar e testar os endpoints. Como a API é protegida por autenticação JWT, é necessário fornecer um token válido para acessar os endpoints protegidos diretamente pelo Swagger UI.

### Como funciona a autenticação no Swagger:
1. **Endpoint de Login**:
 - Antes de acessar os endpoints protegidos, você deve autenticar-se utilizando o endpoint de login:
   POST /api/tax/user/login
 - ```json
   {
     "username": "usuario123",
     "password": "senhaSegura"
   }
   ```
 - A resposta será um token JWT:
   ```json
   {
     "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
   }
   ```

2. **Configurar o Token no Swagger**:
 - No Swagger UI, clique no botão **Authorize** no canto superior direito.
 - Uma janela será exibida solicitando o token de autenticação.
 - Insira o token.

3. **Acesso aos Endpoints**:
 - Após autorizar, você poderá acessar os endpoints protegidos diretamente pelo Swagger UI.
 - O token será automaticamente incluído no cabeçalho das requisições como `Authorization: Bearer <seu_token>`.

### Dica 💡
- Certifique-se de que o token JWT gerado pelo endpoint de login não tenha expirado antes de usá-lo no Swagger.
- O tempo de expiração do token pode ser configurado no arquivo `application.yml`:
```yaml
jwt:
  secret: 3k9J2+7k5f8h1L9m2Pq7Xy8Z0aBcDeFgHiJkLmNoPqRsTuVwXyZ1234567890==
  expiration: 3600000 # 1 hora
```

### Benefícios da Autenticação no Swagger
- Permite testar endpoints protegidos diretamente na interface do Swagger.
- Garante que apenas usuários autenticados possam acessar recursos sensíveis.
- Facilita o desenvolvimento e a validação da API.


### Conclusão
O processo de autenticação no Swagger é essencial para testar endpoints protegidos de forma segura e eficiente. Com a configuração adequada, você pode simular o comportamento real da API diretamente na interface do Swagger UI.


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
Os testes foram implementados utilizando **JUnit**. Para executar os testes, utilize o comando:
  ```
  mvn test
  ```

## Cobertura de Testes com JaCoCo 📊
O projeto utiliza o **JaCoCo** (Java Code Coverage) para medir a cobertura de testes. O JaCoCo é uma ferramenta poderosa que ajuda a identificar quais partes do código estão sendo testadas e quais não estão.

### Como gerar o relatório de cobertura:
1. Execute os testes com o comando:
  ```
  mvn test
  ```
2. Gere o relatório de cobertura com o comando:
  ```
  mvn jacoco:report
  ```
3. O relatório será gerado no diretório `target/site/jacoco/`. Abra o arquivo `index.html` em um navegador para visualizar o relatório detalhado.

### Benefícios do JaCoCo:
- Identifica áreas do código que precisam de mais testes.
- Ajuda a melhorar a qualidade do código.
- Garante que funcionalidades críticas estejam cobertas por testes.

## Contribuição
Contribuições são bem-vindas! 🎉 Sinta-se à vontade para abrir issues ou enviar pull requests.

