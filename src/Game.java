import enigma.core.Enigma;
import enigma.event.TextMouseEvent;
import enigma.event.TextMouseListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import enigma.console.TextAttributes;
import java.awt.Color;
import java.util.Random;

public class Game {
    public enigma.console.Console cn = Enigma.getConsole("Logic Maze", 140, 40, 20);
    public TextMouseListener tmlis;
    public KeyListener klis;

    public int mousepr;
    public int mousex, mousey;
    public int keypr;
    public int rkey;
    Random rnd = new Random();
    Robot[] robots = new Robot[100];
    int robotCount = 0;
    int spawnCounter = 0;
    InputQueue inputQueue = new InputQueue();
    int lastDir = 0;
    Fireball[] fireballs = new Fireball[100];
    Stack backpack = new Stack(8);
    boolean isTablePrinted = false;
    boolean isKmapActive = false;
    public boolean isNamingPhase = false;
    public String playerName = "";

    public String userInput = "";
    public boolean isSimplifiedChecked = false;

    public int currentScreen = 1;


    TextAttributes red = new TextAttributes(Color.RED);
    TextAttributes blue = new TextAttributes(Color.CYAN);
    TextAttributes green = new TextAttributes(Color.GREEN);
    TextAttributes white = new TextAttributes(Color.WHITE);
    TextAttributes yellow = new TextAttributes(Color.YELLOW);

    Game() throws Exception {
        tmlis=new TextMouseListener() {
            public void mouseClicked(TextMouseEvent arg0) {}
            public void mousePressed(TextMouseEvent arg0) {
                if(mousepr==0) {
                    mousepr=1;
                    mousex=arg0.getX();
                    mousey=arg0.getY();
                }
            }
            public void mouseReleased(TextMouseEvent arg0) {}
        };
        cn.getTextWindow().addTextMouseListener(tmlis);

        klis=new KeyListener() {
            public void keyTyped(KeyEvent e) {
                if (currentScreen == 3 && isKmapActive && !isSimplifiedChecked) {
                    char c = e.getKeyChar();
                    if ("ABCDabcd~^v+>=&01".indexOf(c) != -1 || c == ' ') {
                        userInput += c;
                    }
                }else if(isNamingPhase){
                    char c = e.getKeyChar();

                    if(Character.isLetter(c) || c == ' '){
                        playerName += c;
                    }
                }
            }
            public void keyPressed(KeyEvent e) {
                if(keypr==0) {
                    keypr=1;
                    rkey=e.getKeyCode();
                }
            }
            public void keyReleased(KeyEvent e) {}
        };
        cn.getTextWindow().addKeyListener(klis);

        MazeScreen mazeScreen = new MazeScreen();
        mazeScreen.loadMaze();
        TreeScreen treeScreen = new TreeScreen();
        TableScreen tableScreen = new TableScreen();
        cn.getTextWindow();

        for (int i = 0; i < 10; i++) {
            char element = inputQueue.dequeue();
            int ry, rx;
            do {
                ry = rnd.nextInt(21);
                rx = rnd.nextInt(45);
            } while (mazeScreen.area[ry][rx] != ' ');

            if (element == 'X') {
                if (robotCount < robots.length) {
                    robots[robotCount] = new Robot(ry, rx);
                    robotCount++;
                    mazeScreen.area[ry][rx] = 'X';
                }
            } else {
                mazeScreen.area[ry][rx] = element;
            }
        }

        int playerX; int playerY;
        do{
            playerY = rnd.nextInt(1,21);
            playerX = rnd.nextInt(1,45);
        }while(mazeScreen.area[playerY][playerX] != ' ');

        int px = playerX;
        int py = playerY;
        Player player = new Player(px,py);
        mazeScreen.area[py][px] = 'P';


        int maxCols = 138;
        int maxRows = 38;

        for (int y = 0; y < 45; y++) {
            for (int x = 0; x < 140; x++) {
                cn.getTextWindow().setCursorPosition(x, y);
                cn.getTextWindow().output(' ');
            }
        }
        cn.getTextWindow().setCursorPosition(0, 0);

        cn.setTextAttributes(new TextAttributes(Color.GREEN));

        String[] logoArt = {
                " /$$                               /$$                 /$$       /$$                      ",
                "| $$                              |__/                | $$$     /$$$                      ",
                "| $$        /$$$$$$$   /$$$$$$     /$$  /$$$$$$$      | $$$$   /$$$$  /$$$$$$$  /$$$$$$$$  /$$$$$$$",
                "| $$       /$$__  $$  /$$__  $$   | $$ /$$_____/      | $$ $$ /$$ $$ |____  $$ |____ /$$/ /$$__  $$",
                "| $$      | $$  \\ $$ | $$  \\ $$   | $$| $$            | $$  $$$| $$   /$$$$$$$     /$$$$/ | $$$$$$$$",
                "| $$      | $$  | $$ | $$  | $$   | $$| $$            | $$\\  $ | $$  /$$__  $$    /$$__/  | $$_____/",
                "| $$$$$$$$|  $$$$$$/ |  $$$$$$$   | $$|  $$$$$$$      | $$ \\/  | $$ |  $$$$$$$  /$$$$$$$$ |  $$$$$$$",
                "|________/ \\______/   \\____  $$   |__/ \\_______/      |__/     |__/  \\_______/ |________/  \\_______/",
                "                      /$$  \\ $$                                                                   ",
                "                     |  $$$$$$/                                                                   ",
                "                      \\______/                                                                    "
        };

        int logoWidth = logoArt[0].length();
        int logoHeight = logoArt.length;
        int logoStartX = (140 - logoWidth) / 2;
        int logoStartY = 4;

        char[] operators = {'A', 'B', 'C', 'D', 'a', 'b', 'c', 'd', '~', '^', 'v', '+', '>', '='};

        int[] dropY = new int[maxCols];
        for (int i = 0; i < maxCols; i++) {
            dropY[i] = rnd.nextInt(maxRows);
        }

        TextAttributes brightGreen = new TextAttributes(Color.GREEN);
        TextAttributes matrixColor = new TextAttributes(new Color(0, 100, 0));
        TextAttributes whiteText = new TextAttributes(Color.WHITE);

        try { Thread.sleep(300); } catch (InterruptedException e) {}
        keypr = 0; rkey = 0;

        while (true) {
            for (int x = 0; x < maxCols; x += 2) {

                int tailY = dropY[x] - 1;
                if (tailY >= 0 && tailY < maxRows) {
                    cn.getTextWindow().setCursorPosition(x, tailY);
                    cn.getTextWindow().output(' ');
                } else if (tailY < 0) {
                    cn.getTextWindow().setCursorPosition(x, maxRows - 1);
                    cn.getTextWindow().output(' ');
                }

                boolean insideLogo = (x >= logoStartX && x < logoStartX + logoWidth &&
                        dropY[x] >= logoStartY && dropY[x] < logoStartY + logoHeight);

                // Damla ASLA maxRows (38) sınırını geçemez! Aşağı kayma imkansız hale gelir.
                if (!insideLogo && dropY[x] < maxRows - 1 && dropY[x] >= 0) {
                    cn.setTextAttributes(matrixColor);
                    cn.getTextWindow().setCursorPosition(x, dropY[x]);
                    System.out.print(operators[rnd.nextInt(operators.length)]);
                }

                dropY[x]++;

                if (dropY[x] >= maxRows || rnd.nextInt(100) > 95) {
                    if (dropY[x] > 0 && dropY[x] <= maxRows) {
                        cn.getTextWindow().setCursorPosition(x, dropY[x] - 1);
                        cn.getTextWindow().output(' ');
                    }
                    dropY[x] = 0;
                }
            }

            cn.setTextAttributes(brightGreen);
            for (int i = 0; i < logoHeight; i++) {
                cn.getTextWindow().setCursorPosition(logoStartX, logoStartY + i);
                System.out.print(logoArt[i]);
            }

            cn.setTextAttributes(whiteText);
            String startText = "Press ENTER to Start the Game";
            int textStartX = (140 - startText.length()) / 2;
            cn.getTextWindow().setCursorPosition(textStartX, logoStartY + logoHeight + 4);
            System.out.print(startText);

            if (keypr == 1) {
                if (rkey == KeyEvent.VK_ENTER) {
                    keypr = 0;
                    break;
                }
                keypr = 0;
            }

            try {
                Thread.sleep(80);
            } catch (InterruptedException e) {

            }

            cn.getTextWindow().setCursorPosition(0, 0);
        }

        for (int y = 0; y < maxRows; y++) {
            for (int x = 0; x < maxCols; x++) {
                cn.getTextWindow().setCursorPosition(x, y);
                cn.getTextWindow().output(' ');
            }
        }
        cn.getTextWindow().setCursorPosition(0, 0);


        currentScreen = 1;
        int lastScreen = 1;

        while(true) {
            if(currentScreen != lastScreen){
                clearScreen(cn);
            }

            lastScreen = currentScreen;

            if(currentScreen == 2) {
                treeScreen.drawTree(cn);
            }

            if(currentScreen == 1){
                spawnCounter++;

                if (spawnCounter % 20 == 0) {
                    char element = inputQueue.dequeue();
                    int ry, rx;
                    do {
                        ry = rnd.nextInt(21);
                        rx = rnd.nextInt(45);
                    } while (mazeScreen.area[ry][rx] != ' ');

                    if (element == 'X') {
                        if (robotCount < robots.length) {
                            Robot newRobot = new Robot(ry, rx);
                            robots[robotCount] = newRobot;
                            robotCount++;
                            mazeScreen.area[ry][rx] = 'X';
                        }
                    } else {
                        mazeScreen.area[ry][rx] = element;
                    }
                }

                if (spawnCounter % 4 == 0) {
                    for (int i = 0; i < robotCount; i++) {
                        if (robots[i] != null && robots[i].isAlive) {
                            mazeScreen.area[robots[i].x][robots[i].y] = ' ';
                            robots[i].movement(mazeScreen.area);
                            mazeScreen.area[robots[i].x][robots[i].y] = 'X';
                        }
                    }
                }

                for (int i = 0; i < robotCount; i++) {
                    if (robots[i] != null && robots[i].isAlive) {
                        int diffRow = Math.abs(robots[i].x - py);
                        int diffCol = Math.abs(robots[i].y - px);
                        if ((diffRow == 1 && diffCol == 0) || (diffRow == 0 && diffCol == 1)) {
                            player.HP -= 5;
                        }
                    }
                }

                if(player.HP <= 0) {
                    cn.setTextAttributes(white);
                    for (int i = 0; i < 35; i++) {
                        cn.getTextWindow().setCursorPosition(0, i);
                        for (int j = 0; j < 100; j++) {
                            cn.getTextWindow().output(" ");
                        }
                    }

                    int startY = 10;

                    if (isSimplifiedChecked) {
                        cn.setTextAttributes(green);
                        String[] victoryArt = {
                                " __      _______ _____ _______ ____  _______     __ ",
                                " \\ \\    / /_   _/ ____|__   __/ __ \\|  __ \\ \\   / / ",
                                "  \\ \\  / /  | || |       | | | |  | | |__) \\ \\_/ /  ",
                                "   \\ \\/ /   | || |       | | | |  | |  _  / \\   /   ",
                                "    \\  /   _| || |____   | | | |__| | | \\ \\  | |    ",
                                "     \\/   |_____\\_____|  |_|  \\____/|_|  \\_\\ |_|    "
                        };
                        for (int i = 0; i < victoryArt.length; i++) {
                            cn.getTextWindow().setCursorPosition(18, startY + i);
                            cn.getTextWindow().output(victoryArt[i]);
                        }
                    } else {
                        cn.setTextAttributes(red);
                        String[] gameOverArt = {
                                "  _____          __  __ ______    ______      ________ _____  ",
                                " / ____|   /\\   |  \\/  |  ____|  / __ \\ \\    / /  ____|  __ \\ ",
                                "| |  __   /  \\  | \\  / | |__    | |  | \\ \\  / /| |__  | |__) |",
                                "| | |_ | / /\\ \\ | |\\/| |  __|   | |  | |\\ \\/ / |  __| |  _  / ",
                                "| |__| |/ ____ \\| |  | | |____  | |__| | \\  /  | |____| | \\ \\ ",
                                " \\_____/_/    \\_\\_|  |_|______|  \\____/   \\/   |______|_|  \\_\\"
                        };
                        for (int i = 0; i < gameOverArt.length; i++) {
                            cn.getTextWindow().setCursorPosition(18, startY + i);
                            cn.getTextWindow().output(gameOverArt[i]);
                        }
                    }

                    cn.setTextAttributes(white);
                    cn.getTextWindow().setCursorPosition(40, startY + 8);
                    cn.getTextWindow().output(String.format("FINAL SCORE: %d", player.score));

                    cn.getTextWindow().setCursorPosition(36, startY + 11);
                    cn.getTextWindow().output("Enter your name: ");

                    isNamingPhase = true;
                    keypr = 0;


                    while (true) {
                        cn.getTextWindow().setCursorPosition(53, startY + 11);
                        cn.getTextWindow().output(playerName + "          ");

                        if (keypr == 1) {
                            if (rkey == KeyEvent.VK_ENTER && !playerName.trim().isEmpty()) {
                                keypr = 0;
                                break;
                            } else if (rkey == KeyEvent.VK_BACK_SPACE && playerName.length() > 0) {
                                this.playerName = this.playerName.substring(0, this.playerName.length() - 1);
                            }

                            keypr = 0;
                        }
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    isNamingPhase = false;
                    player.name = this.playerName;

                    HighScoreTable highScoreTable = new HighScoreTable();
                    highScoreTable.addScore(player);

                    clearScreen(cn);
                    highScoreTable.drawTable(cn, 35, 10);

                    keypr = 0;

                    while(keypr == 0) {
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    System.exit(0);
                }

                for (int i = 0; i < fireballs.length; i++) {
                    if (fireballs[i] != null && fireballs[i].isAlive) {
                        fireballs[i].clear(mazeScreen.area);
                        fireballs[i].move();
                        fireballs[i].checkCollision(mazeScreen.area, robots, robotCount, player);
                        fireballs[i].draw(mazeScreen.area);
                    }
                }

                if(keypr == 1) {
                    mazeScreen.area[py][px] = ' ';
                    int nextX = px;
                    int nextY = py;

                    if(rkey == KeyEvent.VK_LEFT){ nextX--; lastDir = 1; }
                    if(rkey == KeyEvent.VK_RIGHT){ nextX++; lastDir = 2; }
                    if(rkey == KeyEvent.VK_UP){ nextY--; lastDir = 3; }
                    if(rkey == KeyEvent.VK_DOWN){ nextY++; lastDir = 4; }
                    if(rkey == KeyEvent.VK_2){ currentScreen = 2; }

                    if(rkey == KeyEvent.VK_SPACE && player.fireballCount > 0) {
                        int fbX = px; int fbY = py;
                        if(lastDir == 1) fbX--;
                        else if(lastDir == 2) fbX++;
                        else if(lastDir == 3) fbY--;
                        else if(lastDir == 4) fbY++;

                        if(fbY >= 0 && fbY < 21 && fbX >= 0 && fbX < 45) {
                            if(mazeScreen.area[fbY][fbX] != '#' && mazeScreen.area[fbY][fbX] != 'X') {
                                for (int i = 0; i < fireballs.length; i++) {
                                    if (fireballs[i] == null || !fireballs[i].isAlive) {
                                        player.fireballCount--;
                                        fireballs[i] = new Fireball(fbX, fbY, lastDir);
                                        mazeScreen.area[fbY][fbX] = 'o';
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if(rkey == KeyEvent.VK_M){ player.storageMode *= -1; }

                    if (nextY >= 0 && nextY < 21 && nextX >= 0 && nextX < 45) {
                        boolean canMove = true;
                        if (mazeScreen.area[nextY][nextX] != '#' && mazeScreen.area[nextY][nextX] != 'X') {
                            if ("ABCDabcd~^v+>=".indexOf(mazeScreen.area[nextY][nextX]) != -1) {
                                if (player.storageMode == -1) {
                                    if(backpack.size() < 8){
                                        backpack.push(mazeScreen.area[nextY][nextX]);
                                    }else{
                                        canMove = false;
                                    }
                                } else {
                                    if(treeScreen.addOnTreeMode(mazeScreen.area[nextY][nextX], cn)){
                                        canMove = true;
                                    }else{
                                        canMove = false;
                                    }
                                }
                            }else if (mazeScreen.area[nextY][nextX] == '@') {
                                player.fireballCount++;
                            }

                            if(canMove){
                                px = nextX;
                                py = nextY;
                            }
                        }
                    }

                    mazeScreen.area[py][px] = 'P';
                    keypr = 0;
                }



                mazeScreen.printMaze(cn,robots,robotCount);
                int displayTime = spawnCounter / 10;
                String storageStr = (player.storageMode == -1) ? "Backpack" : "Tree";
                mazeScreen.printHUD(cn, displayTime, player.score, player.fireballCount, player.HP, storageStr, backpack, inputQueue.getQueue());

            }else if(currentScreen == 2){
                int displayTime = spawnCounter / 10;
                String storageStr = (player.storageMode == -1) ? "Backpack" : "Tree";
                mazeScreen.printHUD(cn, displayTime, player.score, player.fireballCount, player.HP, storageStr, backpack, inputQueue.getQueue());

                if(keypr == 1) {
                    if(rkey == KeyEvent.VK_1) {
                        currentScreen = 1;
                        clearScreen(cn);
                    }else if(rkey == KeyEvent.VK_M){
                        player.storageMode *= -1;
                        storageStr = (player.storageMode == -1) ? "Backpack" : "Tree";
                        mazeScreen.printHUD(cn, displayTime, player.score, player.fireballCount, player.HP, storageStr, backpack, inputQueue.getQueue());
                    }else if(rkey == KeyEvent.VK_F){
                        int totalDepth = treeScreen.getMaxTreeDepth(1, 1);
                        int varCount = treeScreen.countVariables(1);
                        boolean isValid = treeScreen.isTreeValid(1);

                        if (totalDepth < 3 || varCount < 3 || !isValid) {
                            player.score -= 10;
                            treeScreen.showInvalidTreeWarning = true;
                            treeScreen.drawTree(cn);
                        } else {
                            int nodeCount = treeScreen.countNodes(1);
                            player.score += (10 * nodeCount);
                            currentScreen = 3;
                            clearScreen(cn);
                        }
                    }
                    else {
                        player.score += treeScreen.takeInput(rkey, backpack,cn,currentScreen);
                        treeScreen.drawTree(cn);
                    }
                    keypr = 0;
                }
            }else if(currentScreen == 3){
                if (!tableScreen.isCalculated) {
                    clearScreen(cn); // Ekranı bir kez temizle
                    String postfix = treeScreen.getPostfix(1);
                    tableScreen.initTable(postfix);
                    isKmapActive = false;
                    isSimplifiedChecked = false;
                    userInput = "";
                }

                if (keypr == 1) {
                    if (!tableScreen.isTableCompleted) {
                        if (rkey == KeyEvent.VK_0 || rkey == KeyEvent.VK_NUMPAD0) {
                            player.score += tableScreen.answerNextQuestion(0);
                        }
                        else if (rkey == KeyEvent.VK_1 || rkey == KeyEvent.VK_NUMPAD1) {
                            player.score += tableScreen.answerNextQuestion(1);
                        }
                    }
                    else if (!isKmapActive) {
                        if (rkey == KeyEvent.VK_ENTER) {
                            isKmapActive = true;
                        }
                    }
                    else if (isKmapActive && !isSimplifiedChecked) {
                        if (rkey == KeyEvent.VK_BACK_SPACE && userInput.length() > 0) {
                            userInput = userInput.substring(0, userInput.length() - 1);
                        }
                        else if (rkey == KeyEvent.VK_ENTER) {
                            int bonus = tableScreen.checkSimplifiedExpression(userInput);
                            player.score += bonus;
                            isSimplifiedChecked = true;
                        }
                    } else if (isSimplifiedChecked) {
                        if (rkey == KeyEvent.VK_ENTER) {
                            player.HP = 0;
                            currentScreen = 1;
                            clearScreen(cn);
                        }
                    }
                    keypr = 0;
                }

                tableScreen.drawCombinedScreen(cn, isKmapActive, userInput, isSimplifiedChecked, player.score);
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void clearScreen(enigma.console.Console cn) {
        for (int y = 0; y < 45; y++) {
            cn.getTextWindow().setCursorPosition(0, y);
            for (int x = 0; x < 140; x++) {
                System.out.print(" ");
            }
        }
        cn.getTextWindow().setCursorPosition(0, 0);
    }
}