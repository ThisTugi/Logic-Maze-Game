import enigma.console.Console;
import enigma.console.TextAttributes;
import java.awt.Color;
import java.awt.event.KeyEvent;

public class TreeScreen {
    public char[] tree = new char[32];
    public boolean[] isCreated = new boolean[32];

    public int cursorIndex; // Cursor index
    public boolean showMaxDepthWarning = false;
    public boolean showInvalidTreeWarning = false;

    public TreeScreen() {
        for (int i = 1; i < 32; i++) {
            tree[i] = ' '; // Every root is empty
            isCreated[i] = false;
        }
        cursorIndex = 1; // Root
        isCreated[1] = true; // Root
    }

    public void drawTree(Console cn) throws InterruptedException {

        //Clear screen
        for (int y = 0; y < 25; y++) {
            cn.getTextWindow().setCursorPosition(0, y);
            for (int x = 0; x < 48; x++) System.out.print(" ");
        }

        int[] xPos = new int[32];
        int[] yPos = new int[32];
        int[] offsets = new int[32];
        int[] depths = new int[32];

        //Set root Node's coordinate
        xPos[1] = 30;
        yPos[1] = 2;
        offsets[1] = 12;
        depths[1] = 1;

        //For loop from begin to end
        for (int i = 1; i <= 31; i++) {
            if (!isCreated[i]) continue; //If this node not created skip

            //Save coordinates
            int x = xPos[i];
            int y = yPos[i];
            int offset = offsets[i];
            int depth = depths[i];

            //If there is no operator/variable draw 0 ,else draw char
            char displayChar = (tree[i] == ' ') ? 'O' : tree[i];
            cn.getTextWindow().setCursorPosition(x - 1, y);

            if (i == cursorIndex) {
                cn.setTextAttributes(new TextAttributes(Color.GREEN, Color.BLACK));
                System.out.print("[" + displayChar + "]");
                cn.setTextAttributes(new TextAttributes(Color.WHITE, Color.BLACK));
            } else {
                System.out.print(" " + displayChar + " ");
            }

            // If depth < 5 draw childs
            if (depth < 5) {
                int leftIndex = i * 2;
                int rightIndex = (i * 2) + 1;

                // Left child control
                if (leftIndex <= 31 && isCreated[leftIndex]) {
                    int childX = x - offset;
                    int childY = y + 3;

                    // Take childs coordinate
                    xPos[leftIndex] = childX;
                    yPos[leftIndex] = childY;
                    offsets[leftIndex] = offset / 2;
                    depths[leftIndex] = depth + 1;

                    // Draw connections char
                    for (int j = childX + 1; j < x - 1; j++) {
                        cn.getTextWindow().setCursorPosition(j, y + 1);
                        System.out.print("-");
                    }
                    cn.getTextWindow().setCursorPosition(childX, y + 1);
                    System.out.print("+");
                    cn.getTextWindow().setCursorPosition(childX, y + 2);
                    System.out.print("|");
                }

                //Right child control
                if (rightIndex <= 31 && isCreated[rightIndex]) {
                    int childX = x + offset;
                    int childY = y + 3;

                    // Take childs coordinate,and save for another node
                    xPos[rightIndex] = childX;
                    yPos[rightIndex] = childY;
                    offsets[rightIndex] = offset / 2;
                    depths[rightIndex] = depth + 1;

                    // Draw right child connections
                    for (int j = x + 2; j < childX; j++) {
                        cn.getTextWindow().setCursorPosition(j, y + 1);
                        System.out.print("-");
                    }
                    cn.getTextWindow().setCursorPosition(childX, y + 1);
                    System.out.print("+");
                    cn.getTextWindow().setCursorPosition(childX, y + 2);
                    System.out.print("|");
                }
            }
        }

        // Max Depth Warning
        if (showMaxDepthWarning) {

            cn.setTextAttributes(new TextAttributes(Color.RED, Color.BLACK));
            cn.getTextWindow().setCursorPosition(70, 22);
            System.out.print("!!!!!!!!!!!!!!!!!!!!!!!!!!");
            cn.getTextWindow().setCursorPosition(70, 23);
            System.out.print("!  MAX DEPTH REACHED: 5  !");
            cn.getTextWindow().setCursorPosition(70, 24);
            System.out.print("!!!!!!!!!!!!!!!!!!!!!!!!!!");
            cn.setTextAttributes(new TextAttributes(Color.WHITE, Color.BLACK));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            cn.getTextWindow().setCursorPosition(70,22);
            System.out.print("                          ");
            cn.getTextWindow().setCursorPosition(70,23);
            System.out.print("                          ");
            cn.getTextWindow().setCursorPosition(70,24);
            System.out.print("                          ");

            showMaxDepthWarning = false;
        }

        if (showInvalidTreeWarning) {
            cn.setTextAttributes(new TextAttributes(Color.RED, Color.BLACK));
            cn.getTextWindow().setCursorPosition(70, 22);
            System.out.print("!!!!!!!!!!!!!!!!!!!!!!!!!!");
            cn.getTextWindow().setCursorPosition(70, 23);
            System.out.print("! INVALID TREE STRUCTURE !");
            cn.getTextWindow().setCursorPosition(70, 24);
            System.out.print("!!!!!!!!!!!!!!!!!!!!!!!!!!");
            cn.setTextAttributes(new TextAttributes(Color.WHITE, Color.BLACK));

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            cn.getTextWindow().setCursorPosition(70, 22);
            System.out.print("                          ");
            cn.getTextWindow().setCursorPosition(70, 23);
            System.out.print("                          ");
            cn.getTextWindow().setCursorPosition(70, 24);
            System.out.print("                          ");

            showInvalidTreeWarning = false;
        }

        cn.setTextAttributes(new TextAttributes(Color.CYAN, Color.BLACK));
        int guideX = 85;

        cn.getTextWindow().setCursorPosition(guideX, 2);
        System.out.print("--- TREE RULES & RULES ---");

        cn.setTextAttributes(new TextAttributes(Color.WHITE, Color.BLACK));
        cn.getTextWindow().setCursorPosition(guideX, 4);
        System.out.print("1. Min Tree Depth : >= 3");

        cn.getTextWindow().setCursorPosition(guideX, 5);
        System.out.print("2. Min Variables  : >= 3");

        cn.getTextWindow().setCursorPosition(guideX, 7);
        cn.setTextAttributes(new TextAttributes(Color.YELLOW, Color.BLACK));
        System.out.print("[NODE STRUCTURE]");

        cn.setTextAttributes(new TextAttributes(Color.WHITE, Color.BLACK));
        cn.getTextWindow().setCursorPosition(guideX, 9);
        System.out.print("- PARENT Nodes (with kids):");
        cn.getTextWindow().setCursorPosition(guideX + 2, 10);
        System.out.print("MUST be an Operator (~, ^, v, +, >, =)");

        cn.getTextWindow().setCursorPosition(guideX, 12);
        System.out.print("- LEAF Nodes (no kids):");
        cn.getTextWindow().setCursorPosition(guideX + 2, 13);
        System.out.print("MUST be a Variable (A, B, C, D...)");

        cn.getTextWindow().setCursorPosition(guideX, 16);
        cn.setTextAttributes(new TextAttributes(Color.GREEN, Color.BLACK));
        System.out.print("Press [F] to Validate & Submit!");
        cn.setTextAttributes(new TextAttributes(Color.WHITE, Color.BLACK));

        // Infix,postfix
        String infix = getInfix(1);
        String postfix = getPostfix(1);


        cn.getTextWindow().setCursorPosition(0, 25);
        System.out.print("Infix   : " + infix);

        cn.getTextWindow().setCursorPosition(0, 26);
        System.out.print("Postfix : " + postfix);
    }

    // Input İşlemleri
    public int takeInput(int rkey, Stack backpack,Console cn,int currentScreen) {
        int scoreChange = 0;

        if (rkey == KeyEvent.VK_A || rkey == KeyEvent.VK_LEFT) {
            int leftIndex = cursorIndex * 2;
            if (leftIndex > 31) { // Maksimum derinlik kontrol
                showMaxDepthWarning = true;
                return 0;
            }
            showMaxDepthWarning = false;

            // Eğer o yol henüz oluşturulmadıysa oluştur , -1 skor
            if (!isCreated[leftIndex]) {
                isCreated[leftIndex] = true;
                scoreChange = -1;
            }
            cursorIndex = leftIndex; // İmleci sola taşı

        } else if (rkey == KeyEvent.VK_D || rkey == KeyEvent.VK_RIGHT) {
            int rightIndex = (cursorIndex * 2) + 1;
            if (rightIndex > 31) {
                showMaxDepthWarning = true;
                return 0;
            }
            showMaxDepthWarning = false;

            if (!isCreated[rightIndex]) {
                isCreated[rightIndex] = true;
                scoreChange = -1;
            }
            cursorIndex = rightIndex;

        } else if (rkey == KeyEvent.VK_W || rkey == KeyEvent.VK_UP) {
            int parentIndex = cursorIndex / 2;
            if (parentIndex >= 1) {
                cursorIndex = parentIndex;
                scoreChange = -1;
                showMaxDepthWarning = false;
            }

        } else if (rkey == KeyEvent.VK_R) {
            if (tree[cursorIndex] != ' ') {
                backpack.push(tree[cursorIndex]);
                tree[cursorIndex] = ' ';
                scoreChange = -2;
            }

        }
        else if (rkey == KeyEvent.VK_T) {
            if (!backpack.isEmpty()) {
                if (tree[cursorIndex] == ' ') {
                    tree[cursorIndex] = (char) backpack.pop();
                }
            }
        }

        return scoreChange;
    }

    public boolean addOnTreeMode(char element, Console cn) throws InterruptedException {
        // Is tree full control
        boolean isTreeCompletelyFull = true;
        for (int i = 1; i <= 31; i++) {
            if (isCreated[i] && tree[i] == ' ') {
                isTreeCompletelyFull = false;
                break;
            }
        }

        // Error message when tree is full
        if (tree[cursorIndex] != ' ' && isTreeCompletelyFull) {
            cn.setTextAttributes(new TextAttributes(Color.RED, Color.BLACK));
            cn.getTextWindow().setCursorPosition(75, 25);
            System.out.print("!!!!!!!!!!!!!!!!!!!!!!!!!!");
            cn.getTextWindow().setCursorPosition(75, 26);
            System.out.print("!  TREE IS FULL! NO PLACE!");
            cn.getTextWindow().setCursorPosition(75, 27);
            System.out.print("!!!!!!!!!!!!!!!!!!!!!!!!!!");
            Thread.sleep(200);

            cn.getTextWindow().setCursorPosition(75, 25);
            System.out.print("                           ");
            cn.getTextWindow().setCursorPosition(75, 26);
            System.out.print("                           ");
            cn.getTextWindow().setCursorPosition(75, 27);
            System.out.print("                           ");
            cn.setTextAttributes(new TextAttributes(Color.WHITE, Color.BLACK));

            return false;
        }

        //Data added to cursor
        tree[cursorIndex] = element;

        // Swaping cursor
        boolean foundEmpty = false;

        //Looking to at the end of array(cursor to end)
        for (int i = cursorIndex + 1; i <= 31; i++) {
            if (isCreated[i] && tree[i] == ' ') {
                cursorIndex = i;
                foundEmpty = true;
                break;
            }
        }

        //If there is no empty place(for the first condition) look for begin to cursor
        if (!foundEmpty) {
            for (int i = 1; i < cursorIndex; i++) {
                if (isCreated[i] && tree[i] == ' ') {
                    cursorIndex = i;
                    break;
                }
            }
        }

        return true;
    }


    public int getMaxTreeDepth(int index, int currentDepth) {
        if (index > 31 || !isCreated[index]) return 0;

        int leftDepth = getMaxTreeDepth(index * 2, currentDepth + 1);
        int rightDepth = getMaxTreeDepth((index * 2) + 1, currentDepth + 1);

        return Math.max(currentDepth, Math.max(leftDepth, rightDepth));
    }

    public int countVariables(int index) {
        int count = 0;

        //Travel everynode
        for (int i = index; i <= 31; i++) {
            //Node control
            if (isCreated[i] && tree[i] != ' ') {



                int temp = i;
                boolean isUnderIndex = false;
                while (temp > 0) {
                    if (temp == index) {
                        isUnderIndex = true;
                        break;
                    }
                    temp /= 2; //Child
                }

                //If node is our Tree count++
                if (isUnderIndex) {
                    if ("~^v+>=".indexOf(tree[i]) == -1) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    public int countNodes(int index) {
        int count = 0;

        //Travel every node
        for (int i = index; i <= 31; i++) {

            //Node control
            if (isCreated[i] && tree[i] != ' ') {

                int temp = i;
                boolean isUnderIndex = false;
                while (temp > 0) {
                    if (temp == index) {
                        isUnderIndex = true;
                        break;
                    }
                    temp /= 2; // Go to parent
                }


                //If everything right plus count
                if (isUnderIndex) {
                    count++;
                }
            }
        }
        return count;
    }

    public boolean isTreeValid(int index) {
        for (int i = index; i <= 31; i++) {
            if (isCreated[i] && tree[i] != ' ') {
                int left = i * 2;
                int right = (i * 2) + 1;

                //Çocuk kontrolu
                boolean hasChild = (left <= 31 && isCreated[left] && tree[left] != ' ') ||
                        (right <= 31 && isCreated[right] && tree[right] != ' ');

                if (hasChild) {
                    // Çocuğu varsa operator olma şartı
                    if ("~^v+>=".indexOf(tree[i]) == -1) return false;
                } else {
                    //Leaf kontrolu
                    if ("ABCDabcd".indexOf(tree[i]) == -1) return false;
                }
            }
        }
        return true;
    }

    public String getInfix(int index) {
        if (index > 31 || !isCreated[index] || tree[index] == ' ') return "";

        String[] tempTrees = new String[32];
        for (int i = 0; i < 32; i++) {
            tempTrees[i] = "";
        }

        // Loop end to start
        for (int i = 31; i >= index; i--) {
            if (isCreated[i] && tree[i] != ' ') {
                int left = i * 2;
                int right = (i * 2) + 1;

                boolean hasLeft = (left <= 31 && isCreated[left] && tree[left] != ' ');
                boolean hasRight = (right <= 31 && isCreated[right] && tree[right] != ' ');

                // If this index doesn't have any kids take this value
                if (!hasLeft && !hasRight) {
                    tempTrees[i] = String.valueOf(tree[i]);
                } else {
                    // If have kids take kids and this value
                    tempTrees[i] = "(" + tempTrees[left] + tree[i] + tempTrees[right] + ")";
                }
            }
        }
        return tempTrees[index];
    }

    public String getPostfix(int index) {
        if (index > 31 || !isCreated[index] || tree[index] == ' ') return "";

        String[] tempTrees = new String[32];
        for (int i = 0; i < 32; i++) {
            tempTrees[i] = "";
        }

        // loop from end to start
        for (int i = 31; i >= index; i--) {
            if (isCreated[i] && tree[i] != ' ') {
                int left = i * 2; //Left
                int right = (i * 2) + 1; // Right


                //Looking for child,if it is craeted take postfix
                String leftStr = (left <= 31 && isCreated[left]) ? tempTrees[left] : "";
                String rightStr = (right <= 31 && isCreated[right]) ? tempTrees[right] : "";

                //Postfix
                tempTrees[i] = leftStr + rightStr + tree[i] + " ";
            }
        }
        return tempTrees[index];
    }
}