# ConsertaJa - Sistema de Gestão de Ferramentas

Sistema web completo para gestão de ferramentas, estoque e fornecedores, desenvolvido com Spring Boot e Spring Security. O ConsertaJa oferece uma interface moderna e segura para controle de inventário e gestão de usuários.

## 🎯 Sobre o Projeto

O ConsertaJa é uma aplicação web desenvolvida para o Checkpoint 5 (CP5) da disciplina Java Advanced. O sistema permite o gerenciamento completo de:

- **Ferramentas**: Cadastro, edição e controle de inventário
- **Fornecedores**: Gestão de fornecedores de ferramentas
- **Estoque**: Controle de capacidade e localização de armazenamento
- **Usuários**: Sistema completo de autenticação e autorização

## 🏗️ Arquitetura do Sistema

### Stack Tecnológica

- **Backend**: Spring Boot 3.5.5
- **Segurança**: Spring Security 6
- **Frontend**: Thymeleaf + Bootstrap
- **Banco de Dados**: PostgreSQL
- **Build Tool**: Maven
- **Java Version**: 17
- **Lombok**: Para redução de boilerplate

### Padrões Arquiteturais

- **MVC (Model-View-Controller)**
- **Repository Pattern**
- **DTO Pattern**
- **Service Layer Pattern**
- **Exception Handling Global**

## 📁 Estrutura do Projeto

```
ConsertaJa/
├── README.md                           # Este arquivo
├── Dockerfile                          # Configuração Docker
├── pom.xml                            # Configuração Maven
├── src/main/java/consertaJa/ConsertaJa/
│   ├── ConsertaJaApplication.java     # Classe principal
│   ├── annotations/                   # Anotações customizadas
│   │   ├── clazz/                    # Validadores customizados
│   │   └── interfaces/               # Interfaces de validação
│   ├── config/                       # Configurações
│   │   ├── DataInitializer.java      # Inicialização de dados
│   │   └── SecurityConfig.java       # Configuração de segurança
│   ├── controller/                   # Controladores REST/Web
│   │   ├── AdminController.java      # Painel administrativo
│   │   ├── AuthController.java       # Autenticação
│   │   ├── EstoqueController.java    # Gestão de estoque
│   │   ├── FerramentaController.java # Gestão de ferramentas
│   │   ├── FornecedorController.java # Gestão de fornecedores
│   │   ├── HomeController.java       # Página inicial
│   │   ├── SobreController.java      # Página sobre
│   │   └── UsuarioController.java    # Gestão de usuários
│   ├── dto/                          # Data Transfer Objects
│   ├── exception/                    # Tratamento de exceções
│   ├── model/                        # Entidades JPA
│   │   ├── Usuario.java              # Entidade de usuário
│   │   ├── Ferramenta.java           # Entidade de ferramenta
│   │   ├── Fornecedor.java           # Entidade de fornecedor
│   │   ├── Estoque.java              # Entidade de estoque
│   │   ├── Role.java                 # Entidade de perfil
│   │   ├── Tipo.java                 # Enum de tipos
│   │   └── Classificacao.java        # Enum de classificação
│   ├── repository/                   # Repositórios JPA
│   ├── service/                      # Camada de serviços
│   └── utils/                        # Utilitários
├── src/main/resources/
│   ├── application.properties        # Configurações da aplicação
│   ├── templates/                    # Templates Thymeleaf
│   │   ├── admin/                    # Páginas administrativas
│   │   ├── auth/                     # Páginas de autenticação
│   │   ├── error/                    # Páginas de erro
│   │   ├── estoques/                 # Páginas de estoque
│   │   ├── ferramentas/              # Páginas de ferramentas
│   │   ├── fornecedores/             # Páginas de fornecedores
│   │   ├── usuarios/                 # Páginas de usuários
│   │   ├── home.html                 # Página inicial
│   │   ├── layout.html               # Layout base
│   │   └── sobre.html                # Página sobre
│   └── static/                       # Arquivos estáticos
└── target/                           # Arquivos compilados
```

## 🗄️ Modelo de Dados

### Entidades Principais

#### Usuario

- **Tabela**: `TDS_Users_Ferramentas`
- **Campos**: id, username, password, email, nomeCompleto, telefone, enabled, dataCriacao
- **Relacionamentos**: Many-to-Many com Role
- **Segurança**: Implementa UserDetails do Spring Security

#### Ferramenta

- **Tabela**: `TAB_CONSERTAJA_FERRAMENTA`
- **Campos**: id, nome, tipo, classificacao, tamanho, preco, quantidade
- **Relacionamentos**: Many-to-One com Fornecedor e Estoque
- **Enums**: Tipo (MANUAL, ELETRICA, HIDRAULICA, PNEUMATICA), Classificacao (PEQUENA, MEDIA, GRANDE)

#### Fornecedor

- **Tabela**: `TAB_CONSERTAJA_FORNECEDOR`
- **Campos**: id, nome, cnpj, telefone, email, endereco
- **Relacionamentos**: One-to-Many com Ferramenta

#### Estoque

- **Tabela**: `TAB_CONSERTAJA_ESTOQUE`
- **Campos**: id, capacidade, localArmazenamento
- **Relacionamentos**: One-to-Many com Ferramenta

#### Role

- **Tabela**: `TDS_Roles_Ferramentas`
- **Campos**: id, nome, descricao
- **Tipos**: ADMIN, USER

## 🔐 Sistema de Segurança

### Autenticação

- **Método**: Form-based authentication
- **Página de Login**: `/login`
- **Campo de Login**: Email (não username)
- **Criptografia**: BCrypt para senhas
- **Sessões**: HTTP Session com invalidação no logout

### Autorização

- **ADMIN**: Acesso completo ao sistema
  - Gestão de usuários
  - Painel administrativo
  - Todas as funcionalidades de USER
- **USER**: Acesso limitado
  - Gestão de ferramentas
  - Gestão de fornecedores
  - Gestão de estoque
  - Visualização do próprio perfil

### Configurações de Segurança

```java
// Rotas públicas
"/", "/home", "/sobre", "/login", "/signup", "/css/**", "/js/**", "/images/**", "/error/**"

// Rotas administrativas
"/admin/**", "/usuarios/**" -> hasRole("ADMIN")

// Rotas de usuário
"/fornecedores/**", "/ferramentas/**", "/estoques/**" -> hasAnyRole("USER", "ADMIN")

// Rotas autenticadas
"/perfil/**" -> authenticated()
```

## 👥 Usuários Padrão

### Administrador

- **Email**: concertaja@gmail.com
- **Senha**: ConsertaJa@2025
- **Username**: admin_consertaja
- **Perfil**: ADMIN

### Usuário Normal

- **Username**: usuario_normal
- **Senha**: user123
- **Email**: usuario@exemplo.com
- **Perfil**: USER

## 🚀 Como Executar

### Pré-requisitos

- Java 17 ou superior
- Maven 3.6+
- PostgreSQL 12+
- IDE de desenvolvimento (recomendado: IntelliJ IDEA)

### Configuração do Banco de Dados

1. **Instale o PostgreSQL** e crie um banco de dados:

```sql
CREATE DATABASE consertaja;
```

2. **Configure as variáveis de ambiente**:

```bash
# Windows
set DB_LINK=jdbc:postgresql://localhost:5432/consertaja
set DB_USER=seu_usuario
set DB_PASSWORD=sua_senha

# Linux/Mac
export DB_LINK=jdbc:postgresql://localhost:5432/consertaja
export DB_USER=seu_usuario
export DB_PASSWORD=sua_senha
```

### Execução

1. **Clone o repositório**:

```bash
git clone <url-do-repositorio>
cd Spring_Security/ConsertaJa
```

2. **Compile o projeto**:

```bash
mvn clean compile
```

3. **Execute a aplicação**:

```bash
mvn spring-boot:run
```

4. **Acesse a aplicação**:

- URL: http://localhost:8080
- Login admin: concertaja@gmail.com / ConsertaJa@2025

## 🐳 Docker

O projeto inclui um Dockerfile para containerização:

```bash
# Build da imagem
docker build -t consertaja .

# Executar o container
docker run -p 8080:8080 consertaja
```

## 📊 Funcionalidades

### Para Administradores

- ✅ Dashboard administrativo
- ✅ Gestão completa de usuários (CRUD)
- ✅ Visualização de estatísticas
- ✅ Controle de permissões
- ✅ Todas as funcionalidades de usuário

### Para Usuários

- ✅ Gestão de ferramentas (CRUD)
- ✅ Gestão de fornecedores (CRUD)
- ✅ Gestão de estoque (CRUD)
- ✅ Visualização do próprio perfil
- ✅ Edição de dados pessoais
- ✅ Controle de quantidade em estoque

### Funcionalidades Gerais

- ✅ Sistema de login/logout
- ✅ Validação de dados
- ✅ Tratamento de erros personalizado
- ✅ Interface responsiva
- ✅ Navegação intuitiva
- ✅ Feedback visual para operações

## 🛠️ Tecnologias e Dependências

### Dependências Principais

```xml
<dependencies>
    <!-- Spring Boot Starters -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-thymeleaf</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- Database -->
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
    </dependency>

    <!-- Utilities -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
    </dependency>

    <!-- Thymeleaf Security -->
    <dependency>
        <groupId>org.thymeleaf.extras</groupId>
        <artifactId>thymeleaf-extras-springsecurity6</artifactId>
    </dependency>
</dependencies>
```

## 🧪 Testes

O projeto inclui testes básicos:

```bash
# Executar todos os testes
mvn test

# Executar testes específicos
mvn test -Dtest=ConsertaJaApplicationTests
```

## 📝 Logs e Monitoramento

- **Logs**: Configurados via Logback
- **SQL**: Habilitado para desenvolvimento (`spring.jpa.show-sql=true`)
- **Erros**: Tratamento global com páginas customizadas
- **Validação**: Mensagens de erro detalhadas

## 🔧 Configurações Avançadas

### application.properties

```properties
# Banco de dados
spring.datasource.url=${DB_LINK}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Segurança
server.error.include-message=always
server.error.include-binding-errors=always

# Thymeleaf
spring.mvc.hiddenmethod.filter.enabled=true
```

## 🚨 Tratamento de Erros

O sistema possui tratamento global de exceções:

- **400**: Bad Request
- **403**: Forbidden (Acesso negado)
- **404**: Not Found
- **500**: Internal Server Error

Páginas de erro customizadas em `/src/main/resources/templates/error/`

## 📱 Interface do Usuário

- **Framework**: Bootstrap 5
- **Templates**: Thymeleaf
- **Responsivo**: Design mobile-first
- **Navegação**: Menu lateral para admin, topo para usuários
- **Feedback**: Alertas e mensagens de sucesso/erro

## 🔄 Fluxo de Desenvolvimento

1. **Desenvolvimento**: IntelliJ IDEA com Spring Boot plugin
2. **Versionamento**: Git com branches para features
3. **Build**: Maven com profiles para dev/prod
4. **Deploy**: Docker container ou JAR standalone

## 📈 Melhorias Futuras

- [ ] API REST completa
- [ ] Integração com sistemas de pagamento
- [ ] Relatórios em PDF/Excel
- [ ] Notificações por email
- [ ] Sistema de backup automático
- [ ] Auditoria de operações
- [ ] Cache com Redis
- [ ] Testes de integração completos

## 👨‍💻 Desenvolvimento

Projeto desenvolvido para o CP5 da disciplina Java Advanced, demonstrando:

- Arquitetura MVC com Spring Boot
- Segurança com Spring Security
- Persistência com JPA/Hibernate
- Interface web com Thymeleaf
- Padrões de desenvolvimento Java
- Boas práticas de programação

## 📄 Licença

Este projeto é destinado exclusivamente para fins educacionais e acadêmicos.
