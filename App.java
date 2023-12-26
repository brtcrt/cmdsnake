package snake;
import java.util.Scanner;
public class App {
    public static void main(String[] args) {
        final int x_bound = 11;
        final int y_bound = 11;
        Scanner in = new Scanner(System.in);
        Game game = new Game(x_bound, y_bound, in);
        game.run();
        in.close();
    }
}
