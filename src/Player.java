public class Player {
    public String name;
    public int fireballCount;
    public int x;
    public int y;
    public int itemCount;
    public int HP;
    public int storageMode;
    public int score;

    Player(int x,int y){
        this.name = "";
        this.x = x;
        this.y = y;
        this.HP = 100;
        this.itemCount = 0;
        this.fireballCount = 0;
        this.storageMode = -1;
        this.score = 0;
    }

    Player(String name, int score) {
        this.name = name;
        this.score = score;
    }
}
