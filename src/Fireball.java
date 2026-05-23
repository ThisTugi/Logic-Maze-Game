public class Fireball {
    public int x, y;          // x: Yatay sütun (45), y: Dikey satır (21)
    public int direction;     // 1: Sol, 2: Sağ, 3: Yukarı, 4: Aşağı
    public boolean isAlive;

    public Fireball(int x, int y, int direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.isAlive = true;
    }

    // Merminin eski izini silmesi
    public void clear(char[][] area) {
        if (area[y][x] == 'o') {
            area[y][x] = ' ';
        }
    }

    // Merminin yeni yerine kendini çizmesi
    public void draw(char[][] area) {
        if (isAlive) {
            area[y][x] = 'o';
        }
    }

    // Merminin bir kare ilerlemesi
    public void move() {
        if (!isAlive) return;

        if (direction == 1) x--;      // Sol
        else if (direction == 2) x++; // Sağ
        else if (direction == 3) y--; // Yukarı
        else if (direction == 4) y++; // Aşağı
    }

    // Çarpışma Kontrolü
    public void checkCollision(char[][] area, Robot[] robots, int robotCount, Player player) {
        // 1. Harita sınırları dışına çıktı mı?
        if (y < 0 || y >= 21 || x < 0 || x >= 45) {
            this.isAlive = false;
            return;
        }

        char currentCell = area[y][x];
        String targets = "ABCDabcd~^v+>=@";

        if (currentCell == '#') {
            this.isAlive = false;
            return; //
        }

        if (currentCell == 'X') {
            boolean robotFound = false;

            for (int i = 0; i < robotCount; i++) {
                if (robots[i] != null && robots[i].isAlive) {
                    if (robots[i].x == this.y && robots[i].y == this.x) {
                        robots[i].isAlive = false;
                        area[robots[i].x][robots[i].y] = ' ';
                        player.score += 50;
                        robotFound = true;
                        break;
                    }
                }
            }

            if (!robotFound) {
                area[this.y][this.x] = ' ';
            }

            this.isAlive = false;
        }

        if(targets.indexOf(currentCell) != -1){
            this.isAlive = false;
        }
    }
}