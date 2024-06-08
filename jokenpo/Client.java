import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        final String HOST = "127.0.0.1";
        final int PORT = 4321;
        Socket socketClient = null;
        ObjectOutputStream output = null;
        ObjectInputStream input = null;
        Scanner scanner = new Scanner(System.in);

        // Conexão = solicitação de conexão com o host
        try {
            socketClient = new Socket(HOST, PORT);
            System.out.println("Conectado com o servidor");
        } catch (UnknownHostException e) {
            System.out.println("Host não encontrado");
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Troca de dados
        try {
            output = new ObjectOutputStream(socketClient.getOutputStream());
            input = new ObjectInputStream(socketClient.getInputStream());
            String serverMessage;
            String inputClientMessage;

            while (true) {
                serverMessage = (String) input.readObject();
                System.out.println(serverMessage);
                if (serverMessage.contains("Placar: ")) {
                    break;
                }
                if (serverMessage.contains(">")) {
                    inputClientMessage = scanner.next();
                    scanner.nextLine(); 
                    output.writeObject(inputClientMessage);
                }
            };
            
        } catch (Exception e) {
            System.out.println("Erro ao trocar dados com o servidor");
            e.printStackTrace();
        } finally {
            // Fechar recursos
            try {
                if (input != null) input.close();
                if (output != null) output.close();
                if (socketClient != null) socketClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        scanner.close(); // Fechar o scanner
        System.out.println("Cliente encerrado");
    }
}
