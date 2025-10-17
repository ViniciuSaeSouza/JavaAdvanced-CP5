package br.com.fiap.suna;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Connection {

    // Envia uma mensagem de texto através do socket.
    public static void enviar(Socket socket, String mensagem) throws IOException {
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        out.println(mensagem);
    }

    // Recebe uma mensagem de texto através do socket.
    public static String receber(Socket socket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        return in.readLine();
    }
}
