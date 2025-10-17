package br.com.fiap.suna;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void startConnection(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            System.out.println("Conectado ao servidor em " + ip + ":" + port);

            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            gerenciarComunicacao();

        } catch (IOException e) {
            System.err.println("Não foi possível conectar ao servidor: " + e.getMessage());
        } finally {
            stopConnection();
        }
    }

    private void gerenciarComunicacao() throws IOException {
        try (Scanner scanner = new Scanner(System.in)) {
            // 1. Gerar chaves RSA do cliente
            System.out.println("Gerando chaves RSA para o cliente...");
            RSA rsaCliente = new RSA();
            System.out.println("Chaves do Cliente geradas.");

            // 2. Receber chave pública do servidor
            System.out.println("Aguardando chave pública do servidor...");
            BigInteger nServidor = new BigInteger(in.readLine());
            BigInteger eServidor = new BigInteger(in.readLine());
            System.out.println("Chave pública do servidor recebida.");

            // 3. Enviar chave pública do cliente para o servidor
            System.out.println("Enviando chave pública (N e E) para o servidor...");
            out.println(rsaCliente.getN().toString());
            out.println(rsaCliente.getE().toString());

            while (true) {
                System.out.print("\nDigite sua mensagem para o servidor (ou 'sair' para terminar): ");
                String mensagem = scanner.nextLine();
                String msgCifrada = RSA.criptografar(mensagem, eServidor, nServidor);
                System.out.println("Enviando mensagem cifrada: " + msgCifrada);
                out.println(msgCifrada);

                if ("sair".equalsIgnoreCase(mensagem)) {
                    break;
                }

                System.out.println("Aguardando resposta do servidor...");
                String respostaCifrada = in.readLine();
                if (respostaCifrada == null) {
                    System.out.println("Servidor encerrou a conexão.");
                    break;
                }
                System.out.println("Resposta cifrada recebida: " + respostaCifrada);

                String respostaDecifrada = rsaCliente.decifrar(respostaCifrada);
                System.out.println("Resposta decifrada: " + respostaDecifrada);
                if ("sair".equalsIgnoreCase(respostaDecifrada)) {
                    System.out.println("Servidor solicitou o encerramento.");
                    break;
                }
            }
        }
    }

    public void stopConnection() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (clientSocket != null) clientSocket.close();
            System.out.println("Conexão encerrada.");
        } catch (IOException e) {
            System.err.println("Erro ao fechar a conexão: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.startConnection("127.0.0.1", 9600);
    }
}