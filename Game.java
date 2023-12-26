package snake;
import java.util.Scanner;

public class Game {
    private final int x_max;
    private final int y_max;
    private Scanner in;
    private boolean hasFood;
    private int score;
    private boolean isDead;
    private boolean addBody;
    private GameObject player;
    private GameObject food;
    private GameObject[][] board;

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

    public void run() {
        while (!this.isDead) {
            System.out.println(this.stringifyGame());
            this.nextStep();
        }
        System.out.println("Game Over!");
        System.out.println("Score: " + this.score);
    }

    private void startup() {
        this.generateAllEmpty();
        this.generateWalls();
        this.generatePlayer();
        this.addFood();
    }

    private void refreshBoard() {
        this.generateAllEmpty();
        this.generateWalls();
        GameObject nextObj = this.player;
        while (nextObj != null) {
            this.board[nextObj.y][nextObj.x] = nextObj;
            nextObj = nextObj.getBehind();
        }
        if (!this.hasFood) {
            addFood();
        } else {
            this.board[food.y][food.x] = this.food;
        }
        
    }

    private void generatePlayer() {
        int startX = this.x_max / 5;
        int startY = this.y_max / 2;
        this.player = new GameObject(startX, startY, 2, "O");
        this.board[startY][startX] = this.player;
    }

    private void addFood() {
        while (!this.hasFood) {
            int randX = (int)(x_max * Math.random()); 
            int randY = (int)(y_max * Math.random()); 
            if (this.board[randY][randX].getType() == 0) {
                this.food = new GameObject(randX, randY, 4, "@");
                this.board[randY][randX] = this.food;
                this.hasFood = true;
            }
        }
    }

    private void generateAllEmpty() {
        for (int i = 0; i < this.y_max; i++) {
            for (int j = 0; j < this.x_max; j++) {
                this.board[i][j] = new GameObject(j, i);
            }
        }
    }

    private void generateWalls() {
        for (int i = 0; i < this.y_max; i++) {
            for (int j = 0; j < this.x_max; j++) {
                if ((i == 0 || i == y_max - 1) && (j == 0 || j == x_max - 1)) {
                    this.board[i][j] = new GameObject(j, i, 1, "+");
                } else if (i == 0 || i == y_max - 1) {
                    this.board[i][j] = new GameObject(j, i, 1, "-");                  
                } else if (j == 0 || j == x_max - 1) {
                    this.board[i][j] = new GameObject(j, i, 1, "|");
                }
            }
        }
    }

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

    private GameObject getUp() {
        return this.board[this.player.y - 1][this.player.x];
    }

    private GameObject getDown() {
        return this.board[this.player.y + 1][this.player.x];
    }

    private GameObject getLeft() {
        return this.board[this.player.y][this.player.x - 1];
    }

    private GameObject getRight() {
        return this.board[this.player.y][this.player.x + 1];
    }

    private void movePlayer(String dir) {
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

    private void moveBody(int oldX, int oldY) {
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
        if (this.addBody) {
            GameObject newObj = new GameObject(last);
            newObj.move(oldX, oldY);
            this.addBody = false;
        }
    }

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

    private void nextStep() {
        boolean validDir = false;
        String dir = "";
        while (!validDir) {
            System.out.print("Move: ");
            dir = in.nextLine().trim().toLowerCase();
            if (dir.equals("w") || dir.equals("a") || dir.equals("s") || dir.equals("d")) {
                validDir = true;
            } else {
                //System.out.println("Please give a valid move!");
            }
        }

        this.isDead = this.calculateDeath(dir);
        if (this.isDead) return;

        if (this.eatFood(dir)) {
            this.hasFood = false;
            this.addBody = true;
            this.score++;

        }

        this.movePlayer(dir);

        this.refreshBoard();
    }
}
