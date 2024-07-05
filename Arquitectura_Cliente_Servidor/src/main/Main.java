package main;

import main.server.Server;
import main.client.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        try {
        // Start the server
        new Thread(() -> {
            try {
                Server server = new Server();
                server.start(8880);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

         // Obtener datos del pago del cliente
         System.out.print("Ingrese el número de cuenta: ");
         String cuenta = reader.readLine();
         
         System.out.print("Ingrese el monto del pago: ");
         String monto = reader.readLine();

         // Iniciar el cliente
         Client client = new Client();
         client.startConnection("127.0.0.1", 8880);

         String response = client.sendMessage("pago:" + cuenta + ":" + monto);
         System.out.println("Respuesta del servidor: " + response);

         response = client.sendMessage("bye");
         System.out.println("Respuesta del servidor: " + response);


        client.stopConnection();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}

