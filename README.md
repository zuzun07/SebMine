# ŞebMine

ŞebMine is a modern twist on the classic Minesweeper game, developed with Java Swing.  
It includes a clean interface, optional background music, and fully customizable board size and mine count.

This project was created as a fun and practical way to strengthen object-oriented programming, UI design, and modular architecture skills in Java.

---

## Features

- Create your own board (size: 2x2 to 20x20)
- Randomly placed mines
- Flag cells with right click
- Timer and flag counter
- "Same board" or "New board" restart options
- Background music support
- Optional image icons (with fallback colors)
- Fixed-size, non-resizable layout — designed to avoid overflow

---

## Technologies Used

- Java (JDK 8+)
- Java Swing (UI)
- Java Sound API
- Modular design with 5 classes:
  - `MineSweeper.java` – Main class and setup screen
  - `GameBoard.java` – Game logic and mine placement
  - `UIManager.java` – Top and bottom UI panel setup
  - `ImageManager.java` – Loads and scales icons or creates fallback images
  - `MusicManager.java` – Handles background music and sound effects

---

## How to Run

1. Clone this repository or download the ZIP:

   ```bash
   git clone https://github.com/zuzun07/SebMine.git
