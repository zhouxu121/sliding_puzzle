# Sliding Puzzle

A Java Swing implementation of the classic sliding-puzzle game.

This project was developed as a Java programming assignment. It keeps the game rules separate from the graphical interface and currently provides a playable number-puzzle mode.

## Current status

**v0.1.0 — first playable version**

The number-puzzle mode is ready to play. The image menu can select an image file, but image-based puzzles have not been implemented yet.

## Features

- 3 × 3, 4 × 4, and 5 × 5 boards
- Solvable random puzzle generation using valid moves
- Tile movement validation
- Step counter
- Difficulty selection
- Restart current game
- Win detection and completion dialog
- Java Swing desktop interface
- Separate game model and GUI classes

## Planned features

- Split a selected image into puzzle tiles
- Image-puzzle mode
- Improved visual design
- Executable releases and native installers

## Requirements

- Java Development Kit (JDK) 8 or newer
- BlueJ (optional)

Check your Java installation:

```bash
java --version
javac --version
```

## Run from source

Clone the repository:

```bash
git clone https://github.com/zhouxu121/sliding_puzzle.git
cd sliding_puzzle/SlidingPuzzle
```

Compile and run:

```bash
javac *.java
java Main
```

## Run with BlueJ

1. Open BlueJ.
2. Choose **Open Project**.
3. Open the `SlidingPuzzle` directory.
4. Compile all classes.
5. Right-click `Main` and run `void main(String[] args)`.

## How to play

1. Choose a board size from the difficulty menu.
2. Click a numbered tile directly next to the empty field.
3. The tile moves into the empty field and the step counter increases.
4. Arrange the tiles in ascending order from left to right and top to bottom.

Solved 3 × 3 board:

```text
0  1  2
3  4  5
6  7  [ ]
```

Only tiles immediately above, below, left, or right of the empty field can move.

## Project structure

```text
sliding_puzzle/
├── SlidingPuzzle/
│   ├── Main.java          # Application entry point
│   ├── GameJFrame.java    # Swing user interface
│   ├── GameModel.java     # Puzzle rules and board state
│   └── package.bluej      # BlueJ project configuration
├── .gitignore
├── README.md
└── README.zh-CN.md
```

## Main classes

### `GameModel`

Contains the game logic. It creates a solved board, shuffles it through valid moves, validates moves, stores the board state, and checks whether the puzzle is solved.

### `GameJFrame`

Contains the Swing interface. It displays the board and step count, handles tile clicks, provides the difficulty and restart menu, and shows the completion dialog.

### `Main`

Starts the Swing application on the Event Dispatch Thread.

## License

Created for educational purposes.

[中文说明](README.zh-CN.md)
