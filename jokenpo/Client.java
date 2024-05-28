import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        final int PORT = 4321;
        Socket socketClient = null;
        ObjectOutputStream output = null;
        ObjectInputStream input = null;
        Scanner scanner = new Scanner(System.in);
        String[] opcoes = {"Pedra", "Papel", "Tesoura"};
        int modalidade;

        // Conexão = solicitação de conexão com o host
        try {
            socketClient = new Socket("localhost", PORT);
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

            System.out.println("""
                **Bem vindo ao Jogo Jokenpo** 
                Digite uma das modalidades que deseja jogar: 
                1 : Jogador vs CPU
                2 : Jogador vs Jogador
                """);

            modalidade = scanner.nextInt();
            scanner.nextLine(); 

            if (modalidade == 1) {
                System.out.println("""
                        Escolha qual jogada quer fazer:
                        Pedra | Papel | Tesoura
                        """);
                String jogada = scanner.nextLine();
                
                // Enviar modalidade e jogada para o servidor
                output.writeInt(modalidade);
                output.writeObject(jogada);

                // Receber o resultado do servidor
                String resultado = (String) input.readObject();
                System.out.println(resultado);
                
            } else if (modalidade == 2) {
                // Lógica para Jogador vs Jogador (não implementada aqui)
                System.out.println("Modo Jogador vs Jogador ainda não implementado.");
            } else {
                System.out.println("Modalidade inválida. Por favor, escolha 1 ou 2.");
            }
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
