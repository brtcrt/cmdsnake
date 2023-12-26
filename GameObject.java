package snake;
public class GameObject {
    public int x;
    public int y;
    private int type;
    private String ch;
    // If the GameObject is the head or a body.
    // Using a seperate class would be best but
    // we can't use inheritance yet :(
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

    private GameObject(int x, int y, int type, String ch, GameObject ahead, GameObject behind) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.ch = ch;
        this.ahead = ahead;
        this.behind = behind;
    }

    public GameObject(int x, int y) {
        this(x, y, 0, " ", null, null);
    }

    public GameObject(GameObject ahead) {
        this(ahead.x, ahead.y, 3, "0", ahead, null);
        ahead.setBehind(this);
    }

    public GameObject(int x, int y, int type, String ch) {
        this(x, y, type, ch, null, null);
    }

    public void move(int x, int y) {
        this.x = x;
        this.y = y;
    }

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

    public void setBehind(GameObject behind) {
        this.behind = behind;
    }

}
