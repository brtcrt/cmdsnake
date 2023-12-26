/**
 * @author brtcrt
 */
package snake;
/** 
 * GameObject class that will be used in the command line snake game.
 */
public class GameObject {
    // UD probobly wouldn't like these to be public
    // but honestly I can't be bothered to write a
    // function movement in each direction.
    public int x;
    public int y;
    private int type;
    private String ch;
    // If the GameObject is the head or a body.
    // Using a seperate class would be best but
    // we haven't learnt inheritance yet :(
    private GameObject ahead;
    private GameObject behind;
    /*
    * TYPES
    * Empty -> 0
    * Wall -> 1
    * Head -> 2
    * Body -> 3
    * Food -> 4
    */

    /*
    * CHARS
    * Empty -> " "
    * Wall -> "-" or "|"
    * Head -> "O"
    * Body -> "o"
    * Food -> "@"
    */

    /**
     * Main constructor. Never used publicly for object-orientation purposes.
     * @param x Initial X.
     * @param y Initial Y.
     * @param type Type of the game object from the following: 
     * Empty -> 0
     * Wall -> 1
     * Head -> 2
     * Body -> 3
     * Food -> 4
     * @param ch The character associated with the type. Type is String not Char.
     * @param ahead The {@link GameObject} in front of this one. Used for the body of the snake. 
     * @param behind The {@link GameObject} behind this one. Used for the body of the snake.
     */
    private GameObject(int x, int y, int type, String ch, GameObject ahead, GameObject behind) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.ch = ch;
        this.ahead = ahead;
        this.behind = behind;
    }

    /**
     * Generate an empty {@link GameObject} at the coordinates given.
     * @param x int
     * @param y int
     */
    public GameObject(int x, int y) {
        this(x, y, 0, " ", null, null);
    }

    /**
     * Generate a {@link GameObject} that is placed behind the provided one.
     * @param ahead {@link GameObject}
     */
    public GameObject(GameObject ahead) {
        this(ahead.x, ahead.y, 3, "0", ahead, null);
        ahead.setBehind(this);
    }

    /**
     * Create a new {@link GameObject} with the type and character provided at the given coordinates.
     * @param x int
     * @param y int
     * @param type int
     * @param ch String
     */
    public GameObject(int x, int y, int type, String ch) {
        this(x, y, type, ch, null, null);
    }
    
    // Getters
    public String getCh() {
        return this.ch;
    }

    public int getType() {
        return this.type;
    }
    
    public GameObject getAhead() {
        return this.ahead;
    }
    
    public GameObject getBehind() {
        return this.behind;
    }

    //Setters
    public void setBehind(GameObject behind) {
        this.behind = behind;
    }
    
    /**
     * Move the game object to the given coordinates.
     * @param x int
     * @param y int
     */
    public void move(int x, int y) {
        this.x = x;
        this.y = y;
    }

}
