package br.com.fiap.suna;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Servidor iniciado na porta " + port + ". Aguardando cliente...");
            clientSocket = serverSocket.accept();
            System.out.println("Cliente conectado: " + clientSocket.getInetAddress().getHostAddress());

            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            gerenciarComunicacao();

        } catch (IOException e) {
            System.err.println("Erro ao iniciar o servidor: " + e.getMessage());
        } finally {
            stop();
        }
    }

    private void gerenciarComunicacao() throws IOException {
        try (Scanner scanner = new Scanner(System.in)) {
            // 1. Gerar chaves RSA do servidor
            System.out.println("Gerando chaves RSA para o servidor...");
            RSA rsaServidor = new RSA();
            System.out.println("Chaves do Servidor geradas.");

            // 2. Enviar chave pública do servidor para o cliente
            System.out.println("Enviando chave pública (N e E) para o cliente...");
            out.println(rsaServidor.getN().toString());
            out.println(rsaServidor.getE().toString());

            // 3. Receber chave pública do cliente
            System.out.println("Aguardando chave pública do cliente...");
            BigInteger nCliente = new BigInteger(in.readLine());
            BigInteger eCliente = new BigInteger(in.readLine());
            System.out.println("Chave pública do cliente recebida.");

            while (true) {
                System.out.println("\nAguardando mensagem do cliente...");
                String msgCifradaCliente = in.readLine();
                if (msgCifradaCliente == null) {
                    System.out.println("Cliente encerrou a conexão.");
                    break;
                }
                System.out.println("Mensagem cifrada recebida do cliente: " + msgCifradaCliente);

                String msgDecifrada = rsaServidor.decifrar(msgCifradaCliente);
                System.out.println("Mensagem decifrada: " + msgDecifrada);
                if ("sair".equalsIgnoreCase(msgDecifrada)) {
                    System.out.println("Cliente solicitou o encerramento.");
                    break;
                }

                System.out.print("Digite sua resposta para o cliente: ");
                String resposta = scanner.nextLine();
                String respostaCifrada = RSA.criptografar(resposta, eCliente, nCliente);
                System.out.println("Enviando resposta cifrada: " + respostaCifrada);
                out.println(respostaCifrada);
                if ("sair".equalsIgnoreCase(resposta)) {
                    break;
                }
            }
        }
    }

    public void stop() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (clientSocket != null) clientSocket.close();
            if (serverSocket != null) serverSocket.close();
            System.out.println("Servidor encerrado.");
        } catch (IOException e) {
            System.err.println("Erro ao fechar o servidor: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start(9600);
    }
}