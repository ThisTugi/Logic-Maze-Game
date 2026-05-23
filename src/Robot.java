import java.util.Random;

public class Robot {
    public boolean isAlive;
    public  int targetMode;
    public int x;
    public int y;

    Robot(int x,int y){
        this.x = x;
        this.y = y;
        Random rnd = new Random();
        this.targetMode = rnd.nextInt(0,2);
        isAlive = true;
    }

    //Robot movement procedure
    public void movement(char[][] area) {
        if (!this.isAlive) return;

        int nextX = x;
        int nextY = y;
        boolean moveDecided = false;

        if (targetMode != 0) {
            int targetX = -1;
            int targetY = -1;
            double minDistance = Double.MAX_VALUE;

            // Find the closest target
            for (int i = 0; i < 21; i++) {
                for (int j = 0; j < 45; j++) {
                    if ("ABCDabcd~^v+>=".indexOf(area[i][j]) != -1) {
                        double distance = Math.abs(i - x) + Math.abs(j - y);
                        if (distance < minDistance) {
                            minDistance = distance;
                            targetX = i;
                            targetY = j;
                        }
                    }
                }
            }

            // Attempt to move towards the target
            if (targetX != -1) {
                // Check collisions
                if (x < targetX && area[x + 1][y] != '#' && area[x + 1][y] != 'X') nextX++;
                else if (x > targetX && area[x - 1][y] != '#' && area[x - 1][y] != 'X') nextX--;
                else if (y < targetY && area[x][y + 1] != '#' && area[x][y + 1] != 'X') nextY++;
                else if (y > targetY && area[x][y - 1] != '#' && area[x][y - 1] != 'X') nextY--;

                // If the coordinates have changed it means a successful step has been taken.
                if (nextX != x || nextY != y) {
                    moveDecided = true;
                }
            }
        }

        // If the robot is in random mode (targetMode == 0) OR
        // If the target-seeking robot is stuck against a wall/cannot find a target and therefore cannot take a step (!moveDecided):
        if (targetMode == 0 || !moveDecided) {
            Random rnd = new Random();
            int direction = rnd.nextInt(1, 5);

            //Reset coordinates
            nextX = x;
            nextY = y;

            if (direction == 1) nextY--;
            else if (direction == 2) nextY++;
            else if (direction == 3) nextX--;
            else if (direction == 4) nextX++;
        }

        // Collision control
        if (nextX >= 0 && nextX < 21 && nextY >= 0 && nextY < 45) {
            char targetCell = area[nextX][nextY];

            if (targetCell != '#' && targetCell != 'P' && targetCell != 'X' && targetCell != '@') {
                x = nextX;
                y = nextY;
            }
        }
    }


}
