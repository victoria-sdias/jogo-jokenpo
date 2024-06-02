import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Game extends Thread {
    Player player1 = null;
    Player player2 = null;
    String modality;
    List<String> options = Arrays.asList("Pedra", "Papel", "Tesoura");
    String optionsMessage = """
        Escolha qual jogada quer fazer:
        Pedra | Papel | Tesoura
        >
        """;
    

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
            switch (this.modality) {
                case "1" -> {
                    String optionPlayer1 = this.requestOption(this.player1);
                    String optionCPU = this.options.get(new Random().nextInt(this.options.size()));
                    System.out.println("Jogada da CPU: " + optionCPU);
                    int resultado = verificarResultado(optionPlayer1, optionCPU);
                    switch (resultado) {
                        case 0:
                            this.player1.sendMessage("Resultado: Empate, pois o computador também escolheu " + optionCPU);
                            break;
                        case 1:
                            this.player1.sendMessage("Resultado: " + this.player1.getPlayerName() + ", você ganhou, pois o computador escolheu " + optionCPU);
                            break;
                        case 2:
                            this.player1.sendMessage("Resultado: " + this.player1.getPlayerName() + ", você perdeu, pois o computador escolheu " + optionCPU);
                            break;
                        default:
                            throw new AssertionError();
                    }
                    break;
                }
                case "2" -> {
                    this.player1.sendMessage(this.player1.getPlayerName() + ", você irá jogar com: " + this.player2.getPlayerName());
                    this.player2.sendMessage(this.player2.getPlayerName() + ", você irá jogar com: " + this.player1.getPlayerName());
                    this.player2.sendMessage("O jogador " + this.player1.getPlayerName()+ " está escolhendo...");
                    String optionPlayer1 = this.requestOption(this.player1);
                    this.player1.sendMessage("O jogador " + this.player2.getPlayerName()+ " está escolhendo...");
                    String optionPlayer2 = this.requestOption(this.player2);
                    int resultado = verificarResultado(optionPlayer1, optionPlayer2);
                    switch (resultado) {
                        case 0:
                            this.player1.sendMessage("Resultado: Empate, pois o " + this.player2.getPlayerName() +" também escolheu " + optionPlayer2);
                            this.player2.sendMessage("Resultado: Empate, pois o " + this.player1.getPlayerName() +" também escolheu " + optionPlayer1);
                            break;
                        case 1:
                            this.player1.sendMessage("Resultado: " + this.player1.getPlayerName() + ", você ganhou, pois o " + this.player2.getPlayerName() + " escolheu " + optionPlayer2);
                            this.player2.sendMessage("Resultado: " + this.player2.getPlayerName() + ", você perdeu, pois o " + this.player1.getPlayerName() + " escolheu " + optionPlayer1);
                            break;
                        case 2:
                            this.player2.sendMessage("Resultado: " + this.player2.getPlayerName() + ", você ganhou, pois o " + this.player1.getPlayerName() + " escolheu " + optionPlayer1);
                            this.player1.sendMessage("Resultado: " + this.player1.getPlayerName() + ", você perdeu, pois o " + this.player2.getPlayerName() + " escolheu " + optionPlayer2);
                            break;
                        default:
                            throw new AssertionError();
                    }
                    break;
                }
                default -> System.out.println("Modalidade inválida!");
            }
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

    private static int verificarResultado(String jogada1, String jogada2) {
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
