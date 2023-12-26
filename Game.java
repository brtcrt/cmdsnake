/**
 * @author brtcrt
 */
package snake;
import java.util.Scanner;
/**
 * Game manager class for the command line snake game.
 */
public class Game {
    // Non-static variables
    private final int x_max;
    private final int y_max;
    private Scanner in;
    private boolean hasFood;
    private int score;
    private boolean isDead;
    private boolean addBody;
    private GameObject player; // The player GameObject
    private GameObject food;
    private GameObject[][] board; // A matrix of GameObjects to keep track of the game.

    /**
     * Creates the Game object.
     * @param x_bound int
     * @param y_bound int
     * @param in Scanner
     */
    public Game(int x_bound, int y_bound, Scanner in) {
        this.x_max = x_bound;
        this.y_max = y_bound;
        this.in = in;
        this.hasFood = false;
        this.score = 0;
        this.isDead = false;
        this.addBody = false;
        this.board = new GameObject[y_bound][x_bound];
        this.startup();
        
    }
    
    /**
     * Sets up the game board.
     */
    private void startup() {
        this.generateAllEmpty();
        this.generateWalls();
        this.generatePlayer();
        this.addFood();
    }


    /**
     * Main game loop. Runs until the player is dead.
     */
    public void run() {
        while (!this.isDead) {
            System.out.println(this.stringifyGame());
            this.nextStep();
        }
        System.out.println("Game Over!");
        System.out.println("Score: " + this.score);
    }
    
    /**
     * What goes inside the game loop itself.
     * Get input
     * Calculate if we die from that input
     * If not make the move
     * Refresh the board
     */
    private void nextStep() {
        boolean validDir = false;
        String dir = "";
        while (!validDir) {
            // Take a valid direction ("w", "a", "s", "d")
            System.out.print("Move: ");
            dir = in.nextLine().trim().toLowerCase(); // Remove whitespace and make case insensitive because why not.
            if (dir.equals("w") || dir.equals("a") || dir.equals("s") || dir.equals("d")) {
                validDir = true;
            } else {
                System.out.println("Please give a valid move!");
            }
        }

        this.isDead = this.calculateDeath(dir);
        if (this.isDead) return; // No need to move the player if we die.

        if (this.eatFood(dir)) {
            this.hasFood = false;
            this.addBody = true;
            this.score++;

        }

        this.movePlayer(dir);

        this.refreshBoard();
    }

    /**
     * After every move of the player, the board is updated accordingly.
     * The food {@link GameObject} and the player {@link GameObject} is placed first.
     * After that, the body parts of the player are inserted by getting the GameObject
     * that is linked to the player and the one linked to that and so on.
     */
    private void refreshBoard() {
        this.generateAllEmpty();
        this.generateWalls();
        /*
         * HOW THE LINKED STRUCTURE WORKS:
         * Player has several body parts, but only know the one right before it (or after it depending on the perspective)
         * This works as every other body GameObject also know the next GameObject that comes before and after it.
         * Player-Body1-Body2-Body3-Body4-...
         * Player is connected to Body1
         * Body1 is connected to Body2
         * and so on.
         */
        GameObject nextObj = this.player;
        while (nextObj != null) { // Until there are no more body parts.
            this.board[nextObj.y][nextObj.x] = nextObj;
            nextObj = nextObj.getBehind();
        }
        if (!this.hasFood) {
            addFood();
        } else {
            this.board[food.y][food.x] = this.food;
        }
        
    }

    /**
     * First, fill the board with empty GameObjects.
     */
    private void generateAllEmpty() {
        for (int i = 0; i < this.y_max; i++) {
            for (int j = 0; j < this.x_max; j++) {
                this.board[i][j] = new GameObject(j, i);
            }
        }
    }

    /**
     * Generate the walls.
     */
    private void generateWalls() {
        for (int i = 0; i < this.y_max; i++) {
            for (int j = 0; j < this.x_max; j++) {
                if ((i == 0 || i == y_max - 1) && (j == 0 || j == x_max - 1)) {
                    // Corner
                    this.board[i][j] = new GameObject(j, i, 1, "+");
                } else if (i == 0 || i == y_max - 1) {
                    // Up and Down
                    this.board[i][j] = new GameObject(j, i, 1, "-");                  
                } else if (j == 0 || j == x_max - 1) {
                    // Left and Right
                    this.board[i][j] = new GameObject(j, i, 1, "|");
                }
            }
        }
    }
    
    /**
     * Generate the player initially.
     */
    private void generatePlayer() {
        int startX = this.x_max / 5;
        int startY = this.y_max / 2;
        this.player = new GameObject(startX, startY, 2, "O");
        this.board[startY][startX] = this.player;
    }

    /**
     * Insert a food GameObject at an empty spot.
     */
    private void addFood() {
        while (!this.hasFood) { // Until we have successfuly added the food.
            int randX = (int)(x_max * Math.random()); 
            int randY = (int)(y_max * Math.random()); 
            if (this.board[randY][randX].getType() == 0) { // If the spot is an empty GameObject
                this.food = new GameObject(randX, randY, 4, "@");
                this.board[randY][randX] = this.food;
                this.hasFood = true;
            }
        }
    }

    /**
     * Simple Matrix to String converter.
     * @return String version of the board matrix.
     */
    public String stringifyGame() {
        String str = "";
        for (int i = 0; i < this.y_max; i++) {
            for (int j = 0; j < this.x_max; j++) {
                str += " " + this.board[i][j].getCh() + " "; 
            }
            str += "\n";
        }
        return str;
    }

    private boolean calculateDeath(String dir) {
        // Unless the GameObject at the direction of the player is NOT empty or food, the game should end.
        if (dir.equals("w")) {
            GameObject up = this.getUp();
            if (up.getType() != 0 && up.getType() != 4) return true; 
        } else if (dir.equals("a")) {
            GameObject left = this.getLeft();
            if (left.getType() != 0 && left.getType() != 4) return true;
        } else if (dir.equals("s")) {
            GameObject down = this.getDown();
            if (down.getType() != 0 && down.getType() != 4) return true;
        } else {
            GameObject right = this.getRight();
            if (right.getType() != 0 && right.getType() != 4) return true;
        }
        return false;
    }

    /**
     * @return The GameObject that is above the player.
     */
    private GameObject getUp() {
        return this.board[this.player.y - 1][this.player.x];
    }

    /**
     * @return The GameObject that is below the player.
     */
    private GameObject getDown() {
        return this.board[this.player.y + 1][this.player.x];
    }

    /**
     * @return The GameObject that is to the left of the player.
     */
    private GameObject getLeft() {
        return this.board[this.player.y][this.player.x - 1];
    }

    /**
     * @return The GameObject that is to the right of the player.
     */
    private GameObject getRight() {
        return this.board[this.player.y][this.player.x + 1];
    }

    /**
     * Moves the player using the direction provided.
     * @param dir String ("w", "s", "a", "d")
     */
    private void movePlayer(String dir) {
        // We need to store the initial coords of the player, as the body part
        // that comes after it will need to be placed there.
        int oldX = this.player.x;
        int oldY = this.player.y;
        if (dir.equals("w")) {
            this.player.y--;
        } else if (dir.equals("a")) {
            this.player.x--;
        } else if (dir.equals("s")) {
            this.player.y++;
        } else {
            this.player.x++;
        }
        this.moveBody(oldX, oldY);
    }

    /**
     * Moves the body of the player. Kill me.
     * @param oldX int
     * @param oldY int
     */
    private void moveBody(int oldX, int oldY) {
        /*
         * HOW THIS CLUSTERFUCK WORKS
         * 1) Get the initial coords of the player.
         * 2) Also get the initial coords of the body player behind it.
         * 3) Move the body part(1) to the coords of the player.
         * 4) Get the coords of the next body part(2).
         * 5) Move the body part(2) to body part(1)'s coords.
         * 6) Repeat the same thing until out of body parts.
         * Pain
         */
        GameObject obj = this.player.getBehind();
        GameObject last = this.player;
        int olderX = 0; // in case obj is null,
        int olderY = 0; // it won't have x or y
        while (obj != null) {
            olderX = obj.x;
            olderY = obj.y;
            obj.move(oldX, oldY);
            oldX = olderX;
            oldY = olderY;
            if (obj.getBehind() == null) {
                last = obj;
            }
            obj = obj.getBehind();
        }
        // Add a body part if we need to.
        if (this.addBody) {
            GameObject newObj = new GameObject(last);
            newObj.move(oldX, oldY);
            this.addBody = false;
        }
    }

    /**
     * Returns true if we have eaten the food. False otherwise.
     * @param dir String
     * @return boolean
     */
    private boolean eatFood(String dir) {
        if (dir.equals("w")) {
            GameObject up = this.getUp();
            if (up.getType() == 4) return true;
        } else if (dir.equals("a")) {
            GameObject left = this.getLeft();
            if (left.getType() == 4) return true;
        } else if (dir.equals("s")) {
            GameObject down = this.getDown();
            if (down.getType() == 4) return true;
        } else {
            GameObject right = this.getRight();
            if (right.getType() == 4) return true;
        }
        return false;
    }

}
