import enigma.console.TextAttributes;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MazeScreen {
    public char[][] area = new char[21][45];
    TextAttributes red = new TextAttributes(Color.RED);
    TextAttributes blue = new TextAttributes(Color.CYAN);
    TextAttributes green = new TextAttributes(Color.GREEN);
    TextAttributes white = new TextAttributes(Color.WHITE);
    TextAttributes yellow = new TextAttributes(Color.YELLOW);

    public void loadMaze() {
        try {
            File myObj = new File("Maze.txt");
            Scanner myReader = new Scanner(myObj);
            int row = 0;
            while (myReader.hasNextLine() && row < 21) {
                String file = myReader.nextLine();
                for (int col = 0; col < file.length() && col < 45; col++) {
                    area[row][col] = file.charAt(col);
                }
                row++;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Dosya bulunamadı: maze.txt");
            e.printStackTrace();
        }
    }

    public void printMaze(enigma.console.Console cn,Robot[] robots, int robotCount) {
        for (int i = 0; i < 21; i++) { //Vertical
            for (int j = 0; j < 45; j++) { // Horizontal
                char item = area[i][j];
                TextAttributes color;

                //Select color for every items
                if (item == 'P') color = blue;
                else if (item == 'X') {
                    color = green;
                    for (int r = 0; r < robotCount; r++) {
                        if (robots[r] != null && robots[r].isAlive && robots[r].x == i && robots[r].y == j) {
                            if (robots[r].targetMode != 0) {
                                color = red;
                            }else{
                                color = green;
                            }
                            break;
                        }
                    }
                }
                else if (item == '@' || item == 'o') color = blue;
                else if ("ABCDabcd~^v+>=".indexOf(item) != -1) color = red;
                else if (item == '#') color = white;
                else color = white;

                //Write elements on the screen with color
                cn.setTextAttributes(color);
                cn.getTextWindow().setCursorPosition(j, i);
                System.out.print(item);
            }
        }
        cn.getTextWindow().setCursorPosition(0, 0);
    }

    // Parametrelere char[] queueElements eklendi
    public void printHUD(enigma.console.Console cn, int time, int score, int fireballs, int life, String storage, Stack backpack, char[] queueElements) {
        // Yazı rengini varsayılan yap
        cn.setTextAttributes(new TextAttributes(Color.WHITE));

        int startX = 60;

        // --- YENİ EKLENEN INPUT QUEUE KISMI ---
        cn.getTextWindow().setCursorPosition(startX, 1);
        System.out.print("Input Queue");

        cn.getTextWindow().setCursorPosition(startX, 2);
        System.out.print("<<<<<<<<<<"); // Kuyruğun yönünü gösteren oklar

        cn.getTextWindow().setCursorPosition(startX, 3);
        // Kuyruktaki 10 elemanı yan yana yazdır
        for(int i = 0; i < 10; i++) {
            System.out.print(queueElements[i]);
        }
        cn.getTextWindow().setCursorPosition(startX, 4);
        System.out.print("<<<<<<<<<<");
        // --------------------------------------

        // İstatistiklerin başlangıç satırını 2'den 6'ya kaydırdık ki üst üste binmesin
        int statsY = 6;

        cn.getTextWindow().setCursorPosition(startX, statsY);
        System.out.printf("Time     : %4d", time);

        cn.getTextWindow().setCursorPosition(startX, statsY + 1);
        System.out.printf("Score    : %4d", score);

        cn.getTextWindow().setCursorPosition(startX, statsY + 2);
        System.out.printf("Fireball : %4d", fireballs);

        cn.getTextWindow().setCursorPosition(startX, statsY + 3);
        System.out.printf("Life     : %4d", life);

        cn.getTextWindow().setCursorPosition(startX, statsY + 4);
        System.out.printf("Storage  : %-8s", storage);

        // Çantanın Y koordinatını da biraz aşağı kaydırdık
        int bpX = 64;
        int bpY = statsY + 11;

        for (int i = 0; i < 8; i++) {
            cn.getTextWindow().setCursorPosition(bpX, bpY + i);
            System.out.print("|   |");
        }
        cn.getTextWindow().setCursorPosition(bpX, bpY + 8);
        System.out.print("+---+");
        cn.getTextWindow().setCursorPosition(bpX - 2, bpY + 9);
        System.out.print(" Backpack ");

        Stack tempStack = new Stack(8);

        while (!backpack.isEmpty()) {
            tempStack.push(backpack.pop());
        }

        int count = 0;
        while (!tempStack.isEmpty()) {
            char item = (char) tempStack.pop();
            cn.getTextWindow().setCursorPosition(bpX + 2, (bpY + 7) - count);
            System.out.print(item);

            backpack.push(item);
            count++;
        }

        for (int i = count; i < 8; i++) {
            cn.getTextWindow().setCursorPosition(bpX + 2, (bpY + 7) - i);
            System.out.print(" ");
        }

        cn.getTextWindow().setCursorPosition(0, 0);
    }


}