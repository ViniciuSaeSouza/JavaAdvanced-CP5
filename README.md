# Java Advanced - CP5

Este repositório contém os projetos desenvolvidos para o Checkpoint 5 (CP5) da disciplina Java Advanced, incluindo implementações de criptografia RSA e uma aplicação web Spring Security.

## 📁 Estrutura do Projeto

```
JavaAdvanced-CP5/
├── README.md                    # Este arquivo
├── RSA/                        # Projeto de Criptografia RSA
│   ├── README.md               # Documentação específica do RSA
│   ├── RSA Dados.xlsx          # Dados e especificações do RSA
│   └── RSACriptography/        # Implementação Java do RSA
└── Spring_Security/            # Projeto Spring Security
    └── ConsertaJa/             # Aplicação web ConsertaJa
        ├── README.md           # Documentação específica do ConsertaJa
        └── src/                # Código fonte da aplicação
```

## 🚀 Projetos Incluídos

### 1. RSA - Sistema de Criptografia

- **Localização**: `RSA/`
- **Descrição**: Implementação completa de um sistema de criptografia RSA com comunicação cliente-servidor
- **Tecnologias**: Java puro, Socket Programming
- **Funcionalidades**: Geração de chaves, criptografia/descriptografia, comunicação segura

### 2. ConsertaJa - Sistema de Gestão

- **Localização**: `Spring_Security/ConsertaJa/`
- **Descrição**: Aplicação web completa para gestão de ferramentas e estoque
- **Tecnologias**: Spring Boot, Spring Security, Thymeleaf, PostgreSQL
- **Funcionalidades**: Autenticação, autorização, CRUD completo, interface web

## 🛠️ Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.5.5**
- **Spring Security 6**
- **PostgreSQL**
- **Thymeleaf**
- **Maven**
- **Lombok**

## 📋 Pré-requisitos

- Java 17 ou superior
- Maven 3.6+
- PostgreSQL (para o projeto ConsertaJa)
- IDE de desenvolvimento (IntelliJ IDEA, Eclipse, VS Code)

## 🏃‍♂️ Como Executar

### RSA Project

```bash
cd RSA/RSACriptography
mvn compile
mvn exec:java -Dexec.mainClass="br.com.fiap.suna.Server"
# Em outro terminal:
mvn exec:java -Dexec.mainClass="br.com.fiap.suna.Client"
```

### ConsertaJa Project

```bash
cd Spring_Security/ConsertaJa
# Configure as variáveis de ambiente para o banco de dados
export DB_LINK=jdbc:postgresql://localhost:5432/consertaja
export DB_USER=seu_usuario
export DB_PASSWORD=sua_senha
mvn spring-boot:run
```

## 📚 Documentação

Cada projeto possui sua própria documentação detalhada:

- [RSA - Documentação](./RSA/README.md)
- [ConsertaJa - Documentação](./Spring_Security/ConsertaJa/README.md)

## 👨‍💻 Autor

Desenvolvido para o CP5 da disciplina Java Advanced.

## 📄 Licença

Este projeto é destinado exclusivamente para fins educacionais.
