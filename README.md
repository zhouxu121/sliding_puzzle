# Sliding Puzzle

[中文](README.zh-CN.md) | **English** | [Deutsch](README.de.md)

A Java Swing implementation of the classic sliding-puzzle game. Play with numbered tiles or turn your own image into a puzzle.

## Current status

**v1.2.0 — multilingual interface and improved image handling**

The project provides a playable number-puzzle mode and an image-puzzle mode. The interface can be switched between Chinese, English, and German without resetting the current game. Image-loading errors, including invalid image files, are reported with localized messages.

## Features

- 3 × 3, 4 × 4, and 5 × 5 boards
- Solvable random puzzle generation using valid moves
- Number-puzzle and image-puzzle modes
- Upload JPG, JPEG, and PNG images
- Automatically scaled 600 × 600 image board with seamless tiles
- Switch between image and number display without losing the current board state
- Chinese, English, and German interface options
- Tile movement validation and step counter
- Difficulty selection and restart current game
- Win detection and completion dialog
- Java Swing desktop interface with separate game model and GUI classes

## Requirements

- Java Development Kit (JDK) 8 or newer; JDK 9 or newer is recommended for UTF-8 translations
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
2. Click a tile directly next to the empty field.
3. The tile moves into the empty field and the step counter increases.
4. Arrange the tiles in ascending order from left to right and top to bottom.

To play with an image, choose **Load Image** from the menu and select a JPG, JPEG, or PNG file. The current arrangement and step count remain unchanged. Choose **Show Numbers** to switch back to the number-puzzle display. Use the **Language** menu to change the interface language.

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
│   ├── Messages*.properties # Localized user-interface text
│   └── package.bluej      # BlueJ project configuration
├── .gitignore
├── README.md              # English
├── README.zh-CN.md        # Chinese
└── README.de.md           # German
```

## Main classes

### `GameModel`

Contains the game logic. It creates a solved board, shuffles it through valid moves, validates moves, stores the board state, and checks whether the puzzle is solved.

### `GameJFrame`

Contains the Swing interface. It displays numbered tiles or image tiles, handles image loading and display-mode and language changes, provides the game menu, and shows the completion dialog.

### `Main`

Starts the Swing application on the Event Dispatch Thread.

## License

Created for educational purposes.
