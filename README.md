# Dots and Boxes

A Java implementation of the classic **Dots and Boxes** game, featuring both a text-based Console interface and a fully interactive Graphical User Interface (GUI).

## Features

### Console Version

* Simple text-based interface for terminal play.
* Supports board sizes from 2x2 to 10x10.
* Input validation for coordinates.

### GUI Version (Java Swing)

* **Interactive Gameplay:** Click on the spaces between dots to draw lines.
* **Visual Aids:** Hover effects show exactly where a move will be placed.
* **Dynamic UI:**
* Player names highlight in **Bold Blue** or **Bold Red** to indicate the current turn.
* Completed boxes are automatically filled with the owner's color.


* **Custom Setup:** Launch window allows setting board dimensions up to 10x10.
* **Game Over Screen:** distinct popup announcing the winner or tie with the final score.

## How to Run

1. **Prerequisites:** Java Development Kit (JDK) 17 or higher.
2. **Compile & Run:**
Navigate to the `src` folder and run the `Main` class.
```bash
cd src/main/java
javac it/units/sdm/dotsandboxes/Main.java
java  it.units.sdm.dotsandboxes.Main

```


3. **Launcher:**
When prompted, enter:
* `1` for **Console Mode**
* `2` for **GUI Mode**



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
