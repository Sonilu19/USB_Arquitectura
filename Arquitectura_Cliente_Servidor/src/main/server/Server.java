package main.server;

import java.io.*;
import java.net.*;

public class Server {
    private ServerSocket serverSocket;

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        while (true) {
            new ClientHandler(serverSocket.accept()).start();
        }
    }

    public void stop() throws IOException {
        serverSocket.close();
    }

    private static class ClientHandler extends Thread {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    if (inputLine.startsWith("pago:")) {
                        String[] parts = inputLine.split(":");
                        String cuenta = parts[1];
                        String monto = parts[2];
                        out.println("Pago recibido para la cuenta " + cuenta + " por el monto de " + monto);
                    } else if ("bye".equalsIgnoreCase(inputLine)) {
                        out.println("bye");
                        break;
                    } else {
                        out.println("mensaje no reconocido");
                    }
                }

                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
