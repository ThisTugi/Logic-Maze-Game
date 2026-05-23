import enigma.console.Console;
import enigma.console.TextAttributes;
import java.awt.Color;
import java.util.Random;

public class TableScreen {

    public boolean isCalculated = false;
    public boolean[][] tableResults;
    public boolean[][] isQuestion;
    public int[][] userAnswers;

    public int rowCount = 16;
    public int colCount = 0;
    public String[] terms = new String[32];
    public boolean isTableCompleted = false;

    public int bonusScore = 0;

    //This function converts postfix to terms,Like A^B or A>B.
    //It is important for our truth table.
    public void initTable(String postfix) {
        Stack s1 = new Stack(32);
        Stack s2 = new Stack(32);
        String[] array = postfix.split(" ");

        for(int i = 0; i < array.length; i++) {
            if (!array[i].trim().isEmpty()) s2.push(array[i]);
        }

        int size = s2.size();
        for(int i = 0; i < size; i++) s1.push(s2.pop());

        Stack processStack = new Stack(32);
        int index = 0;

        while(!s1.isEmpty()){
            String character = (String)s1.pop();
            if(!isOperator(character)){
                processStack.push(character);
            } else {
                String right = (String)processStack.pop();
                String term = "";
                if (character.equals("~")) {
                    term = "(~" + right + ")";
                } else {
                    String left = (String)processStack.pop();
                    term = "(" + left + character + right + ")";
                }
                processStack.push(term);
                terms[index] = term;
                index++;
            }
        }
        colCount = index;

        tableResults = new boolean[16][colCount];
        isQuestion = new boolean[16][colCount];
        userAnswers = new int[16][colCount];

        //This part is for draw Truth table's left side. ABCD
        for (int i = 0; i < 16; i++) {
            boolean[] currentValues = new boolean[256];
            currentValues['A'] = ((i / 8) % 2) == 1;
            currentValues['B'] = ((i / 4) % 2) == 1;
            currentValues['C'] = ((i / 2) % 2) == 1;
            currentValues['D'] = ((i / 1) % 2) == 1;

            currentValues['a'] = !currentValues['A'];
            currentValues['b'] = !currentValues['B'];
            currentValues['c'] = !currentValues['C'];
            currentValues['d'] = !currentValues['D'];

            Stack stack = new Stack(32);
            int currentColIndex = 0;

            for(int j = 0; j < array.length; j++){
                String word = array[j];
                if(word.trim().isEmpty()) continue;

                if(!isOperator(word)){
                    stack.push(currentValues[word.charAt(0)]);
                } else {
                    boolean result;
                    if (word.equals("~")) {
                        boolean val = (boolean)stack.pop();
                        result = !val;
                    } else {
                        boolean rightVal = (boolean)stack.pop();
                        boolean leftVal = (boolean)stack.pop();
                        result = calculate(leftVal, rightVal, word.charAt(0));
                    }
                    stack.push(result);
                    tableResults[i][currentColIndex] = result;
                    currentColIndex++;
                }
            }
        }

        Random rnd = new Random();
        for (int c = 0; c < colCount; c++) {
            for (int r = 0; r < 16; r++) userAnswers[r][c] = -1;
            int randomRow = rnd.nextInt(16);
            isQuestion[randomRow][c] = true;
        }

        isCalculated = true;
    }


    public void drawCombinedScreen(Console cn, boolean isKmapActive, String userInput, boolean isChecked, int playerScore) {

        cn.getTextWindow().setCursorPosition(0, 0);

        cn.setTextAttributes(new TextAttributes(Color.YELLOW, Color.BLACK));
        System.out.println("==========================================================================================================");
        System.out.printf("  CURRENT SCORE: %-4d                                                                                   \n", playerScore);
        System.out.println("==========================================================================================================");
        cn.setTextAttributes(new TextAttributes(Color.WHITE, Color.BLACK));

        //Writes our all terms on the truth table
        System.out.print("ABCD | ");
        for (int i = 0; i < colCount; i++) {
            System.out.print(terms[i]);
            if (i < colCount - 1) System.out.print(" | ");
        }
        System.out.println("\n---------------------------------------------------------");

        for (int i = 0; i < 16; i++) {
            int a = (i / 8) % 2;
            int b = (i / 4) % 2;
            int c = (i / 2) % 2;
            int d = (i / 1) % 2;
            System.out.print("" + a + b + c + d + " | ");

            for (int j = 0; j < colCount; j++) {
                int totalSpace = terms[j].length() - 1;
                int leftSpace = totalSpace / 2;
                int rightSpace = totalSpace - leftSpace;

                for(int k = 0; k < leftSpace; k++) System.out.print(" ");

                if (isQuestion[i][j]) {
                    if (userAnswers[i][j] == -1) {
                        cn.setTextAttributes(new TextAttributes(Color.YELLOW, Color.BLACK));
                        System.out.print("?");
                    } else {
                        boolean expected = tableResults[i][j];
                        boolean answered = (userAnswers[i][j] == 1);
                        if (answered == expected) cn.setTextAttributes(new TextAttributes(Color.GREEN, Color.BLACK));
                        else cn.setTextAttributes(new TextAttributes(Color.RED, Color.BLACK));
                        System.out.print(userAnswers[i][j]);
                    }
                } else {
                    cn.setTextAttributes(new TextAttributes(Color.WHITE, Color.BLACK));
                    System.out.print(tableResults[i][j] ? "1" : "0");
                }

                cn.setTextAttributes(new TextAttributes(Color.WHITE, Color.BLACK));
                for(int k = 0; k < rightSpace; k++) System.out.print(" ");
                System.out.print(" | ");
            }
            System.out.println();
        }


        cn.getTextWindow().setCursorPosition(0, 23);
        if (!isTableCompleted) {
            cn.setTextAttributes(new TextAttributes(Color.YELLOW, Color.BLACK));
            System.out.print("Status: Fill the yellow '?' slots using '0' or '1' keys.          ");
        } else if (!isKmapActive) {
            cn.setTextAttributes(new TextAttributes(Color.GREEN, Color.BLACK));
            System.out.print("Status: Table Completed! (Press ENTER to generate Karnaugh Map)   ");
        } else {
            cn.setTextAttributes(new TextAttributes(Color.GREEN, Color.BLACK));
            System.out.print("Status: Table Completed!                                          ");
        }


        if (isKmapActive) {
            int kmapX = 65;
            int kmapY = 4;

            cn.setTextAttributes(new TextAttributes(Color.CYAN, Color.BLACK));
            cn.getTextWindow().setCursorPosition(kmapX, kmapY++);
            System.out.print("--- KARNAUGH MAP ---");
            cn.setTextAttributes(new TextAttributes(Color.WHITE, Color.BLACK));
            kmapY++;

            boolean[] isCovered = new boolean[16];
            int[][] chosenMasks = new int[16][4];
            int chosenCount = 0;

            //Firstly looking for the most big group,2^4 or 2^3 it contiunes like that...
            for(int numTwos = 4; numTwos >= 0; numTwos--) {
                for(int a=0; a<=2; a++) {
                    for(int b=0; b<=2; b++) {
                        for(int c=0; c<=2; c++) {
                            for(int d=0; d<=2; d++) {
                                int twos = (a==2?1:0) + (b==2?1:0) + (c==2?1:0) + (d==2?1:0);
                                if(twos != numTwos) continue;

                                boolean valid = true;
                                boolean coversNew = false;

                                //If any square is zero on the table this loop return false
                                for(int i=0; i<16; i++) {
                                    int valA = (i/8)%2; int valB = (i/4)%2;
                                    int valC = (i/2)%2; int valD = (i/1)%2;

                                    boolean match = (a==2 || a==valA) && (b==2 || b==valB) &&
                                            (c==2 || c==valC) && (d==2 || d==valD);
                                    if(match) {
                                        if(!tableResults[i][colCount-1]) { valid = false; break; }
                                        if(!isCovered[i]) coversNew = true;
                                    }
                                }

                                // Save this masks to our array
                                if(valid && coversNew) {
                                    chosenMasks[chosenCount][0] = a;
                                    chosenMasks[chosenCount][1] = b;
                                    chosenMasks[chosenCount][2] = c;
                                    chosenMasks[chosenCount][3] = d;
                                    chosenCount++;

                                    for(int i=0; i<16; i++) {
                                        int valA = (i/8)%2; int valB = (i/4)%2;
                                        int valC = (i/2)%2; int valD = (i/1)%2;
                                        if((a==2 || a==valA) && (b==2 || b==valB) &&
                                                (c==2 || c==valC) && (d==2 || d==valD)) {
                                            isCovered[i] = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            //00,01,11,10
            Color[] groupColors = {Color.RED, Color.GREEN, Color.MAGENTA, Color.YELLOW, Color.CYAN};
            int[] gray = {0, 1, 3, 2};

            cn.getTextWindow().setCursorPosition(kmapX, kmapY++);
            System.out.print("       CD");
            cn.getTextWindow().setCursorPosition(kmapX, kmapY++);
            System.out.print("       00   01   11   10");
            cn.getTextWindow().setCursorPosition(kmapX, kmapY++);
            System.out.print("AB   +----+----+----+----+");

            for (int r = 0; r < 4; r++) {
                cn.getTextWindow().setCursorPosition(kmapX, kmapY++);
                int A = gray[r] / 2;
                int B = gray[r] % 2;
                cn.setTextAttributes(new TextAttributes(Color.WHITE, Color.BLACK));
                System.out.print(A + "" + B + "   |");

                //This loop for K-Map drawing on the screen
                for (int c = 0; c < 4; c++) {
                    int C = gray[c] / 2;
                    int D = gray[c] % 2;

                    int index = (A * 8) + (B * 4) + (C * 2) + (D * 1);
                    boolean val = tableResults[index][colCount - 1];

                    System.out.print("  ");
                    if(val) {
                        Color cellColor = Color.WHITE;
                        for(int g=0; g<chosenCount; g++) {
                            int ga = chosenMasks[g][0], gb = chosenMasks[g][1];
                            int gc = chosenMasks[g][2], gd = chosenMasks[g][3];
                            if((ga==2 || ga==A) && (gb==2 || gb==B) && (gc==2 || gc==C) && (gd==2 || gd==D)) {
                                cellColor = groupColors[g % groupColors.length];
                                break;
                            }
                        }
                        cn.setTextAttributes(new TextAttributes(cellColor, Color.BLACK));
                        System.out.print("1");
                    } else {
                        cn.setTextAttributes(new TextAttributes(Color.WHITE, Color.BLACK));
                        System.out.print("0");
                    }
                    cn.setTextAttributes(new TextAttributes(Color.WHITE, Color.BLACK));
                    System.out.print(" |");
                }
                cn.getTextWindow().setCursorPosition(kmapX, kmapY++);
                System.out.print("     +----+----+----+----+");
            }


            cn.getTextWindow().setCursorPosition(0, 25);
            System.out.print("Simplified expression:                                      ");
            cn.getTextWindow().setCursorPosition(0, 26);
            if (!isChecked) {
                System.out.print("> " + userInput + "_                                     ");
            } else {
                System.out.print("> " + userInput);
                if (bonusScore > 0) {
                    cn.setTextAttributes(new TextAttributes(Color.GREEN, Color.BLACK));
                    System.out.print("  [CORRECT! +50 Points]                             ");
                } else {
                    cn.setTextAttributes(new TextAttributes(Color.RED, Color.BLACK));
                    System.out.print("  [WRONG! -20 Points]                               ");
                }
                cn.setTextAttributes(new TextAttributes(Color.YELLOW, Color.BLACK));
                cn.getTextWindow().setCursorPosition(0, 27);
                System.out.print("Press ENTER to continue to High Scores...                   ");
            }
        }
        cn.setTextAttributes(new TextAttributes(Color.WHITE, Color.BLACK));
    }

    public int answerNextQuestion(int answer) {
        int scoreModifier = 0;
        outerLoop:
        for (int c = 0; c < colCount; c++) {
            for (int r = 0; r < 16; r++) {
                if (isQuestion[r][c] && userAnswers[r][c] == -1) {
                    userAnswers[r][c] = answer;
                    boolean expected = tableResults[r][c];
                    boolean answered = (answer == 1);
                    if (answered == expected) scoreModifier = 3;
                    else scoreModifier = -2;
                    break outerLoop;
                }
            }
        }

        isTableCompleted = true;
        for (int c = 0; c < colCount; c++) {
            for (int r = 0; r < 16; r++) {
                if (isQuestion[r][c] && userAnswers[r][c] == -1) {
                    isTableCompleted = false;
                    break;
                }
            }
        }
        return scoreModifier;
    }

    //This function calculate the K-Map's answer
    public int checkSimplifiedExpression(String input) {
        if (input == null || input.trim().isEmpty()) {
            bonusScore = -20;
            return bonusScore;
        }

        input = addImplicitAnd(input);

        String userPostfix = convertInfixToPostfix(input);
        if (userPostfix == null) {
            bonusScore = -20;
            return bonusScore;
        }

        for (int i = 0; i < 16; i++) {
            //Assing variables to their value(0 or 1)
            boolean[] currentValues = new boolean[256];
            currentValues['A'] = ((i / 8) % 2) == 1;
            currentValues['B'] = ((i / 4) % 2) == 1;
            currentValues['C'] = ((i / 2) % 2) == 1;
            currentValues['D'] = ((i / 1) % 2) == 1;
            currentValues['a'] = !currentValues['A'];
            currentValues['b'] = !currentValues['B'];
            currentValues['c'] = !currentValues['C'];
            currentValues['d'] = !currentValues['D'];

            //Postfix stack
            Stack stack = new Stack(32);
            String[] array = userPostfix.split(" ");

            //Loop from every word in array
            try {
                for (String word : array) {
                    if (word.trim().isEmpty()) continue;
                    if (!isOperator(word)) {
                        stack.push(currentValues[word.charAt(0)]);
                    } else {
                        boolean result;
                        if (word.equals("~")) {
                            boolean val = (boolean)stack.pop();
                            result = !val;
                        } else {
                            boolean rightVal = (boolean)stack.pop();
                            boolean leftVal = (boolean)stack.pop();
                            result = calculate(leftVal, rightVal, word.charAt(0));
                        }
                        stack.push(result);
                    }
                }
                boolean finalResult = (boolean)stack.pop();
                boolean expectedResult = tableResults[i][colCount - 1];

                if (finalResult != expectedResult) {
                    bonusScore = -20;
                    return bonusScore;
                }
            } catch (Exception e) {
                bonusScore = -20;
                return bonusScore;
            }
        }

        bonusScore = 50;
        return bonusScore;
    }

    private String convertInfixToPostfix(String infix) {
        StringBuilder postfix = new StringBuilder();
        Stack stack = new Stack(32);

        for (int i = 0; i < infix.length(); i++) {
            char c = infix.charAt(i);
            if (c == ' ') continue;

            if (isVariable(c)) {
                postfix.append(c).append(" ");
            } else if (c == '(') {
                stack.push(String.valueOf(c));
            } else if (c == ')') {
                while (!stack.isEmpty() && !stack.peek().toString().equals("(")) {
                    postfix.append(stack.pop()).append(" ");
                }
                stack.pop();
            } else if (isOperator(c)) {
                while (!stack.isEmpty() && getPrecedence(stack.peek().toString().charAt(0)) >= getPrecedence(c)) {
                    postfix.append(stack.pop()).append(" ");
                }
                stack.push(String.valueOf(c));
            }
        }
        while (!stack.isEmpty()) {
            postfix.append(stack.pop()).append(" ");
        }
        return postfix.toString();
    }

    private int getPrecedence(char c) {
        if (c == '~') return 3;
        if (c == '^' || c == '&') return 2;
        if (c == 'v' || c == '+') return 1;
        if (c == '>' || c == '=') return 0;
        return -1;
    }

    private boolean isOperator(String word) {
        if (word.length() != 1) return false;
        return isOperator(word.charAt(0));
    }

    private boolean isOperator(char c) {
        return c == 'v' || c == '^' || c == '>' || c == '~' || c == '+' || c == '&' || c == '=';
    }

    //This function calculates of terms results
    private boolean calculate(boolean p, boolean q, char op) {
        if (op == 'v' || op == '+') return p || q;
        if (op == '^' || op == '&') return p && q;
        if (op == '>') return !p || q;
        if (op == '=') return p == q;
        return false;
    }

    private String addImplicitAnd(String input) {
        StringBuilder result = new StringBuilder();
        input = input.replace(" ", "");

        for (int i = 0; i < input.length(); i++) {
            char current = input.charAt(i);
            result.append(current);

            if (i < input.length() - 1) {
                char next = input.charAt(i + 1);

                boolean currentIsVarOrClose = isVariable(current) || current == ')';
                boolean nextIsVarOrOpenOrNot = isVariable(next) || next == '(' || next == '~';

                if (currentIsVarOrClose && nextIsVarOrOpenOrNot) {
                    result.append('^');
                }
            }
        }
        return result.toString();
    }

    private boolean isVariable(char c) {
        return (c >= 'A' && c <= 'D') || (c >= 'a' && c <= 'd');
    }
}