import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Game extends Thread {
    Player player1 = null;
    Player player2 = null;
    String modality;
    List<String> options = Arrays.asList("Pedra", "Papel", "Tesoura");
    String optionsMessage = "> Escolha qual jogada quer fazer:\nPedra | Papel | Tesoura";
    int scorePlayer1; //placar
    int scorePlayer2;
    int scoreEmpates;
    int game;
    String exit = "";
    

    public Game(Player player) {
        this.player1 = player;
        this.modality = "1";
    }

    public Game(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.modality = "2";
    }

    @Override
    public void run() {
        try {
            String player1ContinueVerifyValue;
            String player2ContinueVerifyValue;
            do {
                switch (this.modality) {
                    case "1": {
                        this.game++;
                        String optionPlayer1 = this.requestOption(this.player1);
                        String optionPlayer2 = this.options.get(new Random().nextInt(this.options.size()));
                        System.out.println("Jogada da CPU: " + optionPlayer2);
                        this.sendGameResult(optionPlayer1, optionPlayer2);
                        break;
                    }
                    case "2": {
                        this.player1.sendMessage(this.player1.getPlayerName() + ", você irá jogar com: " + this.player2.getPlayerName());
                        this.player2.sendMessage(this.player2.getPlayerName() + ", você irá jogar com: " + this.player1.getPlayerName());
                        this.player2.sendMessage("O jogador " + this.player1.getPlayerName()+ " está escolhendo...");
                        String optionPlayer1 = this.requestOption(this.player1);
                        this.player1.sendMessage("O jogador " + this.player2.getPlayerName()+ " está escolhendo...");
                        String optionPlayer2 = this.requestOption(this.player2);
                        this.sendGameResult(optionPlayer1, optionPlayer2);
                        break;
                    }
                    default: System.out.println("Modalidade inválida!");
                }
                this.player1.sendContinueVerifyMessage();
                if (this.player2 != null) {
                    this.player2.sendContinueVerifyMessage();
                }
                player1ContinueVerifyValue = "";
                player2ContinueVerifyValue = "";
                while (true) {
                    if (player1ContinueVerifyValue == "") {
                        player1ContinueVerifyValue = this.player1.getContinueVerifyValue();
                        if (player1ContinueVerifyValue != "" && this.player2 != null && player2ContinueVerifyValue == ""){
                            this.player1.sendMessage("Aguardando resposta do(a) segundo jogador(a)!");
                        }
                    } 
                    if (this.player2 != null && player2ContinueVerifyValue == "") {
                        player2ContinueVerifyValue = this.player2.getContinueVerifyValue();
                        if (player2ContinueVerifyValue != "" && player1ContinueVerifyValue == ""){
                            this.player2.sendMessage("Aguardando resposta do(a) segundo jogador(a)!");
                        }
                    } else {
                        player2ContinueVerifyValue = "sim";
                    }
                    if (!player1ContinueVerifyValue.equals("") && !player2ContinueVerifyValue.equals("")) {
                        break;
                    }
                    try {
                        Thread.sleep(1000); // Pausa de 1 segundo entre as iterações
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } while (player1ContinueVerifyValue.equalsIgnoreCase("sim") && player2ContinueVerifyValue.equalsIgnoreCase("sim"));
            sendScoreMessage();
        } catch (Exception e) {
            System.out.println("Erro na comunicação");
            System.out.println(e.getMessage());
        }
    }

    private String requestOption(Player player) throws IOException, ClassNotFoundException {
        player.sendMessage(this.optionsMessage);
        String option = player.listen();
        while (!(option.equalsIgnoreCase("Pedra") ||
                option.equalsIgnoreCase("Papel") ||
                option.equalsIgnoreCase("Tesoura"))) {
            player.sendMessage("Opção inválida!");
            player.sendMessage(this.optionsMessage);
            option = player.listen();
        }
        return option;
    }

    private void sendGameResult(String jogada1, String jogada2) {
        int resultado = this.verifyGameResult(jogada1, jogada2);
        String namePlayer2 = (this.player2 != null) ? this.player2.getPlayerName() : "Computador"; //if ternário
        try {
            switch (resultado) {
                case 0:
                    this.player1.sendMessage("Resultado: Empate, pois o(a) " + namePlayer2 + " também escolheu " + jogada2 + "\n");
                    if (this.player2 != null) {
                        this.player2.sendMessage("Resultado: Empate, pois o " + this.player1.getPlayerName() + " também escolheu " + jogada1 + "\n");
                    }
                    this.scoreEmpates++;
                    break;
                case 1:
                    this.player1.sendMessage("Resultado: " + this.player1.getPlayerName() + ", você ganhou, pois o(a) " + namePlayer2 + " escolheu " + jogada2 + "\n");
                    if (this.player2 != null) {
                        this.player2.sendMessage("Resultado: " + namePlayer2 + ", você perdeu, pois o " + this.player1.getPlayerName() + " escolheu " + jogada1 + "\n");
                    }
                    this.scorePlayer1++;
                    break;
                case 2:
                    this.player1.sendMessage("Resultado: " + this.player1.getPlayerName() + ", você perdeu, pois o(a) " + namePlayer2 + " escolheu " + jogada2 + "\n");
                    if (this.player2 != null) {
                        this.player2.sendMessage("Resultado: " + namePlayer2 + ", você ganhou, pois o " + this.player1.getPlayerName() + " escolheu " + jogada1 + "\n");
                    }
                    this.scorePlayer2++;
                    break;
                default:
                    throw new AssertionError();
            }
        } catch (Exception e) {
            System.out.println("Erro na comunicação");
            System.out.println(e.getMessage());
        }
    }

    private void sendScoreMessage() {
        try {
            this.player1.sendMessage("Placar: " + this.scorePlayer1 + " vitória(s), " + this.scorePlayer2 + " derrota(s), " + this.scoreEmpates + " empate(s).");
            if (this.player2 != null) {
                this.player2.sendMessage("Placar: " + this.scorePlayer2 + " vitória(s), " + this.scorePlayer1 + " derrota(s), " + this.scoreEmpates + " empate(s).");
            }
        } catch (Exception e) {
            System.out.println("Erro na comunicação");
            System.out.println(e.getMessage());
        }
    }

    private int verifyGameResult(String jogada1, String jogada2) {
        if (jogada1.equalsIgnoreCase(jogada2)) {
            return 0;
        } else if ((jogada1.equalsIgnoreCase("Pedra") && jogada2.equalsIgnoreCase("Tesoura")) ||
                    (jogada1.equalsIgnoreCase("Papel") && jogada2.equalsIgnoreCase("Pedra")) ||
                    (jogada1.equalsIgnoreCase("Tesoura") && jogada2.equalsIgnoreCase("Papel"))) {
            return 1;
        } else {
            return 2;
        }
    }
}
