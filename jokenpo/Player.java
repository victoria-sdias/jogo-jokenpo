import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Player extends Thread {
    private String playerName;
    private final Socket socketClient;
    ObjectInputStream input = null;
    ObjectOutputStream output = null;
    private String modality = "";
    private String lastServerMessage = null;
    String menu = """ 
        Digite uma das modalidades que deseja jogar: 
        1 : Jogador vs CPU
        2 : Jogador vs Jogador
        >
        """;

    public Player(Socket socketClient) {
        this.socketClient = socketClient;
    }

    @Override
    public void run() {
        try {
            output = new ObjectOutputStream(this.socketClient.getOutputStream());
            input = new ObjectInputStream(this.socketClient.getInputStream());
            this.sendMessage("""
                **Bem vindo ao Jogo Jokenpo**
                Digite seu nome:
                >
                """);
            this.playerName = listen();
            do {
                this.sendMessage(this.menu);
                modality = listen();
                if (!modality.equals("1") && !modality.equals("2")) {
                    this.sendMessage("Modalidade inválida!");
                }
            } while (!modality.equals("1") && !modality.equals("2"));
        } catch (IOException e) {
            System.out.println("Erro na comunicação");
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException ex) {
        }
    }

    public void sendMessage(String msg) throws IOException {
        if (!msg.equals("Não utilize o caracter de 'maior que' em suas respostas!")) {
            this.lastServerMessage = msg;
        }
        output.writeObject(msg);
    }

    public String listen() throws IOException, ClassNotFoundException{
            String msg = null;
            do {
                msg = (String) this.input.readObject();
                if (msg.contains(">")) {
                    this.sendMessage("Não utilize o caracter de 'maior que' em suas respostas!");
                    this.sendMessage(this.lastServerMessage);
                    msg = null;
                }
            } while (msg == null);
            return(msg);
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public String getModality() {
        return this.modality;
    }
}
