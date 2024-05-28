import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class Server {
    public static void main(String[] args) {
        final int PORT = 4321;
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        ObjectInputStream input = null;
        ObjectOutputStream output = null;
        String[] opcoes = {"Pedra", "Papel", "Tesoura"};

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

        while (true) {
            // Accept = esperar uma conexão de um cliente
            try {
                System.out.println("Aguardando um cliente...");
                clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado.");

                // Troca de dados
                input = new ObjectInputStream(clientSocket.getInputStream());
                output = new ObjectOutputStream(clientSocket.getOutputStream());

                int modalidade = input.readInt();
                if (modalidade == 1) {
                    String jogadaCliente = (String) input.readObject();
                    String jogadaCPU = opcoes[new Random().nextInt(opcoes.length)];
                    System.out.println("Jogada da CPU: " + jogadaCPU);

                    String resultado = verificarResultado(jogadaCliente, jogadaCPU);
                    System.out.println("Resultado: " + resultado);
                    output.writeObject("Resultado: " + resultado);

                } else if (modalidade == 2) {
                    // Lógica para Jogador vs Jogador (não implementada aqui)
                    System.out.println("Modo Jogador vs Jogador ainda não implementado.");
                } else {
                    System.out.println("Modalidade inválida recebida.");
                }
            } catch (Exception e) {
                System.out.println("Erro ao receber os dados");
                e.printStackTrace();
            } finally {
                // Fechar recursos
                try {
                    if (input != null) input.close();
                    if (output != null) output.close();
                    if (clientSocket != null) clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String verificarResultado(String jogada1, String jogada2) {
        if (jogada1.equalsIgnoreCase(jogada2)) {
            return "Empate";
        } else if ((jogada1.equalsIgnoreCase("Pedra") && jogada2.equalsIgnoreCase("Tesoura")) ||
                    (jogada1.equalsIgnoreCase("Papel") && jogada2.equalsIgnoreCase("Pedra")) ||
                    (jogada1.equalsIgnoreCase("Tesoura") && jogada2.equalsIgnoreCase("Papel"))) {
            return "Jogador vence do computador!!!!";
        } else {
            return "Computador venceu!!!";
        }
    }
}
