# RSA - Sistema de Criptografia

Sistema de comunicação segura implementado em Java que utiliza o algoritmo de criptografia RSA para garantir a confidencialidade das mensagens trocadas entre cliente e servidor.

## 🔐 Sobre o RSA

O RSA (Rivest-Shamir-Adleman) é um algoritmo de criptografia assimétrica que utiliza um par de chaves:

- **Chave Pública**: Usada para criptografar mensagens
- **Chave Privada**: Usada para descriptografar mensagens

## 📁 Estrutura do Projeto

```
RSA/
├── README.md                   # Este arquivo
├── RSA Dados.xlsx             # Especificações e dados do projeto
└── RSACriptography/           # Código fonte Java
    ├── pom.xml                # Configuração Maven
    └── src/main/java/br/com/fiap/suna/
        ├── RSA.java           # Implementação do algoritmo RSA
        ├── Server.java        # Servidor de comunicação
        ├── Client.java        # Cliente de comunicação
        └── Connection.java    # Classe auxiliar de conexão
```

## 🔧 Funcionalidades

### Algoritmo RSA

- Geração automática de chaves públicas e privadas
- Criptografia de mensagens usando chaves públicas
- Descriptografia de mensagens usando chaves privadas
- Utilização de números primos fixos (p=1009, q=1013) para demonstração

### Comunicação Cliente-Servidor

- Estabelecimento de conexão TCP na porta 9600
- Troca segura de chaves públicas entre cliente e servidor
- Criptografia bidirecional de mensagens
- Interface de linha de comando para interação

## 🚀 Como Executar

### Pré-requisitos

- Java 17 ou superior
- Maven 3.6+

### Passos para Execução

1. **Compile o projeto:**

   ```bash
   cd RSA/RSACriptography
   mvn compile
   ```

2. **Execute o servidor (em um terminal):**

   ```bash
   mvn exec:java -Dexec.mainClass="br.com.fiap.suna.Server"
   ```

3. **Execute o cliente (em outro terminal):**
   ```bash
   mvn exec:java -Dexec.mainClass="br.com.fiap.suna.Client"
   ```

### Fluxo de Execução

1. O servidor inicia e aguarda conexões na porta 9600
2. O cliente se conecta ao servidor
3. Ambos geram suas chaves RSA
4. Servidor envia sua chave pública para o cliente
5. Cliente envia sua chave pública para o servidor
6. Comunicação segura bidirecional é estabelecida
7. Digite "sair" para encerrar a comunicação

## 🔍 Detalhes Técnicos

### Parâmetros RSA Utilizados

- **p = 1009** (número primo)
- **q = 1013** (número primo)
- **n = p × q = 1,022,117** (módulo)
- **φ(n) = (p-1) × (q-1) = 1,020,096** (função totiente)
- **e = 65537** (expoente público)
- **d = e^(-1) mod φ(n)** (expoente privado)

### Processo de Criptografia

1. Mensagem é convertida para bytes
2. Cada byte é elevado à potência 'e' módulo 'n'
3. Resultado é enviado como string de números separados por espaços

### Processo de Descriptografia

1. String cifrada é dividida em números
2. Cada número é elevado à potência 'd' módulo 'n'
3. Resultado é convertido de volta para caractere

## 📊 Exemplo de Uso

```
Servidor iniciado na porta 9600. Aguardando cliente...
Cliente conectado: 127.0.0.1
Gerando chaves RSA para o servidor...
Chaves do Servidor geradas.
Enviando chave pública (N e E) para o cliente...
Aguardando chave pública do cliente...
Chave pública do cliente recebida.

Aguardando mensagem do cliente...
Mensagem cifrada recebida do cliente: 123456 789012 345678
Mensagem decifrada: Olá servidor!
Digite sua resposta para o cliente: Olá cliente!
Enviando resposta cifrada: 987654 321098 765432
```

## ⚠️ Observações Importantes

- **Uso Educacional**: Este projeto é destinado exclusivamente para fins educacionais
- **Chaves Fixas**: Os números primos são fixos para facilitar a demonstração
- **Segurança**: Para uso em produção, seria necessário implementar geração de números primos aleatórios maiores
- **Limitações**: O sistema atual suporta apenas um cliente por vez

## 🐛 Tratamento de Erros

O sistema inclui tratamento de erros para:

- Falhas de conexão de rede
- Problemas de entrada/saída
- Erros de criptografia/descriptografia
- Desconexões inesperadas

## 📝 Logs e Debug

O sistema produz logs detalhados mostrando:

- Status das conexões
- Geração de chaves
- Processo de criptografia/descriptografia
- Mensagens enviadas e recebidas
