# Dots and Boxes

A Java implementation of the classic **Dots and Boxes** game, featuring both a text-based Console interface and a fully interactive Graphical User Interface (GUI).

## Features

### Console Version

* Simple text-based interface for terminal play.
* Supports board sizes from 2x2 to 10x10.
* Clear input instructions and validation
* Colored output for players and scores

### GUI Version (Java Swing)

* **Interactive Gameplay:** Click on the spaces between dots to draw lines.
* **Visual Aids:** Hover effects show exactly where a move will be placed.
* **Dynamic UI:**
* Player names highlight in **Bold Blue** or **Bold Red** to indicate the current turn.
* Completed boxes are automatically filled with the owner's color.


* **Custom Setup:** Launch window allows setting board dimensions up to 10x10.
* **Game Over Screen:** distinct popup announcing the winner or tie with the final score.

## How to Run

1. **Prerequisites:** Java (JDK) 17 or higher.
* Gradle Wrapper included (no local Gradle installation required)
2. **Run the Project**
* 2.1 From the project root directory, run:

```bash
./gradlew clean build
./gradlew run

```
Or (recommended for console clarity):

```bash
./gradlew run --console=plain

```
**What this does**
* Compiles the project
* Runs all automated tests
* Launches the game launcher

* 2.2 Navigate to the `src/main/java` folder and run the `Main` class.

```bash
javac it/units/sdm/dotsandboxes/Main.java
java it.units.sdm.dotsandboxes.Main

```
3. **Launcher:**
When prompted, enter:
* `1` for **Console Mode**
* `2` for **GUI Mode**
* `3` for **Exit**

**Console Controls**
When playing in Console Mode:
* Enter moves using: row col H/V
* Type help to reprint the move instructions
* Type quit or exit to stop the game at any time

## How to Play

1. The game is played on a grid of dots.
2. **Player 1 (Blue)** and **Player 2 (Red)** take turns drawing a single horizontal or vertical line between two unjoined adjacent dots.
3. If a player completes the fourth side of a 1x1 box, they **earn one point** and **must take another turn**.
4. The game ends when all possible lines are drawn.
5. The player with the most boxes wins.

## Project Structure

```text
src/main/java/it/units/sdm/dotsandboxes/
├── Main.java           # Launcher and menu logic
├── logic/
│   ├── GameSession.java # Handles game flow, turns, and scores
│   └── Board.java       # Handles grid data and line validation
├── model/
│   ├── Line.java
│   ├── Point.java
│   ├── Player.java
│   ├── Direction.java
│   └── Move.java
└── view/
    ├── ConsoleUI.java   # Text-based interface
    └── SwingUI.java     # Graphical interface

```
