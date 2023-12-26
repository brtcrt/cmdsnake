/**
 * Command Line Snake Game
 * @author brtcrt
 */
package snake;
import java.util.Scanner;
public class App {
    public static void main(String[] args) {
        // CONSTANTS
        final int x_bound = 11;
        final int y_bound = 11;

        Scanner in = new Scanner(System.in); // Create Scanner.
        Game game = new Game(x_bound, y_bound, in); // Create the game object.
        game.run(); // Run the game via the manager object.
        in.close(); // Close the scanner of course.
    }
}
