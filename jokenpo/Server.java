import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
    public static void main(String[] args) throws IOException {
        final int PORT = 4321;
        ServerSocket serverSocket = null;
        Socket clientSocket = null;

        // Binding = obter uma porta do Sistema Operacional
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor disponível na porta " + PORT);
        } catch (BindException e) {
            System.out.println("Erro. A porta " + PORT + " já está em uso. Altere a configuração em...");
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Distributor distribuidor = new Distributor();
        distribuidor.start();

        while (true) {
            // Accept = esperar uma conexão de um cliente
            try {
                System.out.println("Aguardando um cliente...");
                clientSocket = serverSocket.accept();
                Player player = new Player(clientSocket);
                player.start();
                distribuidor.addPlayer(player);
            } catch (Exception e) {
                clientSocket.close();
                System.out.println("Erro ao receber os dados");
                e.printStackTrace();
            }
        }
    }
}
