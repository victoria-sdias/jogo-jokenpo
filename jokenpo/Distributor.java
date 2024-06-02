import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class Distributor extends Thread {
    private ArrayList<Player> players = new ArrayList<>();
    private Player tempPlayer = null;

    public void addPlayer(Player player) {
        this.players.add(player);
        System.out.println("Adicionando um novo player ao distribuidor");
    }

    @Override
    public void run() {
        while (true) {
            Iterator<Player> iterator = this.players.iterator();
            while (iterator.hasNext()) {
                Player player = iterator.next();
                if (player.getModality().equals("1")) {
                    Game game = new Game(player);
                    iterator.remove();
                    game.start();
                } else if (player.getModality().equals("2")) {
                    if (this.tempPlayer == null) {
                        this.tempPlayer = player;
                        iterator.remove();
                        try {
                            this.tempPlayer.sendMessage("Aguardando um segundo jogador...");
                        } catch (IOException ex) {
                        }
                    } else {
                        Game game = new Game(this.tempPlayer, player);
                        iterator.remove();
                        this.tempPlayer = null;
                        game.start();
                    }
                }
            }
            try {
                Thread.sleep(1000); // Pausa de 1 segundo entre as iterações
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
