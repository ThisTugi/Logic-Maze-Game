import enigma.console.Console;
import enigma.console.TextAttributes;
import java.awt.Color;
import java.io.*;
import java.util.Scanner;

public class HighScoreTable {
    private DoubleLinkedList dll;
    private final String FILENAME = "highscore.txt";

    public HighScoreTable() {
        dll = new DoubleLinkedList();
        loadFromFile();
    }

    private void loadFromFile() {
        try {
            File file = new File(FILENAME);
            if (!file.exists()) {
                dll.insertSorted(new Player("Tarkan Bulut", 728));
                dll.insertSorted(new Player("Irmak Yol", 412));
                dll.insertSorted(new Player("Deniz Toprak", 190));
                dll.insertSorted(new Player("Ali Deniz", 56));
                saveToFile();
                return;
            }

            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                int lastSpace = line.lastIndexOf(" ");
                if (lastSpace != -1) {
                    String name = line.substring(0, lastSpace).trim();
                    int score = Integer.parseInt(line.substring(lastSpace + 1).trim());
                    dll.insertSorted(new Player(name, score));
                }
            }
            scanner.close();
        } catch (Exception e) {
            System.out.println("Skorlar yuklenirken hata oluştu.");
        }
    }

    public void addScore(Player player) {
        dll.insertSorted(player);
        saveToFile();
    }

    private void saveToFile() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILENAME));
            Node current = dll.getHead();
            while (current != null) {
                writer.write(current.getPlayer().name + " " + current.getPlayer().score);
                writer.newLine();
                current = current.getNext();
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Skorlar kaydedilirken hata olustu.");
        }
    }

    public void drawTable(Console cn, int startX, int startY) {
        cn.setTextAttributes(new TextAttributes(Color.CYAN, Color.BLACK));
        cn.getTextWindow().setCursorPosition(startX, startY);
        cn.getTextWindow().output("================ HIGH SCORE TABLE ================");

        cn.setTextAttributes(new TextAttributes(Color.WHITE, Color.BLACK));
        Node current = dll.getHead();
        int i = 1;
        while (current != null && i <= 10) { //Shows first ten player
            cn.getTextWindow().setCursorPosition(startX + 2, startY + 1 + i);
            cn.getTextWindow().output(String.format("%2d. %-20s %5d", i, current.getPlayer().name, current.getPlayer().score));
            current = current.getNext();
            i++;
        }

        cn.getTextWindow().setCursorPosition(startX, startY + i + 2);
        cn.setTextAttributes(new TextAttributes(Color.YELLOW, Color.BLACK));
        cn.getTextWindow().output("Press any key to exit...");
    }
}