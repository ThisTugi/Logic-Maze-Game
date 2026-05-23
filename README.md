# Logic Maze: Tree & Table Game

Welcome to Logic Maze, a terminal-based game developed from scratch in Java for the CME1252 Data Structures and Algorithms course. The main idea here was to take abstract concepts like digital logic design, expression trees, and truth tables, and turn them into an actual arcade maze game.

---

## How the Game Works

The game is split into three main sections. The maze itself runs in real-time, but whenever you switch views to check your progress, the game automatically pauses so you don't get destroyed by enemies while thinking.

### 1. The Maze (Key 1)
* **Movement & Combat:** You control the character 'P' using the arrow keys. You start with 100 Life Points. If you get cornered by enemies, you can press Space to shoot a fireball in the direction you are facing to clear the path.
* **Storage Modes (Key M):** You have a backpack that holds up to 8 items. By pressing M, you can switch your storage mode between Tree and Backpack. When set to Backpack, collected symbols stay in your inventory. If your backpack fills up, the game automatically routes any new symbols straight into your logic tree.
* **The Robots ('X'):** The enemies spawn with two different AI behaviors:
  * 50% of them move completely randomly just to add chaos.
  * The other 50% are targeted, meaning they actively track your coordinates and hunt you down.

### 2. The Tree Builder (Key 2)
This is where the mathematical side of the game takes place. All the variables (A, B, C, D) and operators (+, *, ') you collect in the maze are sent here. The game takes your real-time formula (Infix format), converts it into Postfix behind the scenes using stacks, and builds a Binary Expression Tree in memory.

### 3. The Truth Table & K-Map (Key F from Tree Screen)
Unlike the first two screens, you don't access this one with a number key. Once you completely finish building your Expression Tree on Screen 2, you press F to generate and enter the Table Screen.
* **Truth Table:** This section dynamically evaluates every single true/false combination for your collected variables and prints out the results in a clean table format.
* **Karnaugh Map (K-Map):** It maps out the truth table results and runs a Boolean simplification algorithm to give you the most optimized, shortest version of your logic expression.

---

## Core Data Structures Used

Instead of relying on built-in high-level libraries, everything in this project was built using core computer science structures:

* **Binary Expression Trees:** Used to parse, store, and evaluate the hierarchical structure of the logical formulas.
* **Doubly Linked Lists:** Manages the High Score Table. When you lose all your life points, the game reads highscore.txt, inserts your score in descending order using a doubly linked list, and updates the file.
* **Stacks & Queues:** Used for converting Infix expressions to Postfix, managing the movement queues of the AI robots, and processing fireball inputs.
* **Enigma Console Library:** Used to handle the retro ASCII rendering, custom colors for the robot states, and smooth screen updates in the terminal.

---

## How to Run It

### Prerequisites
* Java JDK 8 or higher installed on your machine.
* The Enigma console library (.jar dependency) must be linked in your project's classpath.

### Quick Start
1. Clone the repository:
   ```bash
   git clone [https://github.com/YOUR_USERNAME/YOUR_REPO_NAME.git](https://github.com/YOUR_USERNAME/YOUR_REPO_NAME.git)

Open the project in your preferred IDE (like IntelliJ, Eclipse, or VS Code), make sure the Enigma jar is in your build path, and run the main Game class.

Maximize your terminal window for the best experience.
