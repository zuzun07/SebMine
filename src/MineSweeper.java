import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MineSweeper extends JFrame {
    private JButton[][] buttons;
    private GameBoard gameBoard;
    private int size, mineCount, currentFlags, secondsElapsed;
    private boolean gameOver = false;
    private Timer timer;

    private JLabel timerLabel = new JLabel("Time: 0");
    private JLabel flagLabel = new JLabel();
    private JLabel statusLabel = new JLabel(" ");
    private JButton restartButton = new JButton("New Game");

    public MineSweeper(int size, int mineCount) {
        this.size = size;
        this.mineCount = mineCount;
        this.currentFlags = 0;

        ImageManager.loadImages();
        gameBoard = new GameBoard(size, mineCount);
        buttons = new JButton[size][size];

        setupUI();
        startTimer();

        setTitle("ŞebMine");
        if (ImageManager.appIcon != null) setIconImage(ImageManager.appIcon.getImage());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);

        MusicManager.playBackground("SebnemFerah-MayinTarlasi.wav");
    }

    private void setupUI() {
        getContentPane().removeAll();
        setLayout(new BorderLayout());

        JPanel topPanel = UIManager.createTopPanel(timerLabel, flagLabel, statusLabel, ImageManager.sebnemIconSmall);
        add(topPanel, BorderLayout.NORTH);

        int cellSize = 35;
        JPanel gridPanel = new JPanel(new GridLayout(size, size, 1, 1));
        gridPanel.setBorder(BorderFactory.createEtchedBorder());

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setPreferredSize(new Dimension(cellSize, cellSize));
                buttons[i][j].setIcon(ImageManager.sebnemIcon);
                buttons[i][j].setMargin(new Insets(0, 0, 0, 0));
                buttons[i][j].setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
                buttons[i][j].setBackground(Color.LIGHT_GRAY);

                final int x = i, y = j;
                buttons[i][j].addMouseListener(new MouseAdapter() {
                    public void mousePressed(MouseEvent e) {
                        if (gameOver) return;
                        MusicManager.playSound("click.wav");
                        if (SwingUtilities.isLeftMouseButton(e)) revealCell(x, y);
                        else if (SwingUtilities.isRightMouseButton(e)) toggleFlag(x, y);
                    }
                });
                gridPanel.add(buttons[i][j]);
            }
        }

        add(gridPanel, BorderLayout.CENTER);

        restartButton.addActionListener(e -> showRestartMenu());
        JPanel bottomPanel = UIManager.createBottomPanel(restartButton, ImageManager.sebnemIconSmall);
        add(bottomPanel, BorderLayout.SOUTH);

        pack();
    }

    private void showRestartMenu() {
        JPopupMenu menu = new JPopupMenu();
        JMenuItem same = new JMenuItem("Same Board");
        JMenuItem newB = new JMenuItem("New Board");

        if (ImageManager.sebnemIconSmall != null) {
            same.setIcon(ImageManager.sebnemIconSmall);
            newB.setIcon(ImageManager.sebnemIconSmall);
        }

        same.addActionListener(ev -> {
            dispose();
            new MineSweeper(size, mineCount); // NEW board each time
        });

        newB.addActionListener(ev -> {
            dispose();
            showSetupScreen();
        });

        menu.add(same);
        menu.add(newB);
        menu.show(restartButton, 0, -menu.getPreferredSize().height);
    }

    private void revealCell(int x, int y) {
        if (gameBoard.revealed[x][y] || gameBoard.flagged[x][y]) return;
        gameBoard.revealed[x][y] = true;
        buttons[x][y].setEnabled(false);
        buttons[x][y].setIcon(null);

        if (gameBoard.board[x][y] == -1) {
            buttons[x][y].setText("X");
            buttons[x][y].setBackground(Color.RED);
            buttons[x][y].setForeground(Color.WHITE);
            endGame(false);
            return;
        }

        if (gameBoard.board[x][y] > 0) {
            buttons[x][y].setText(Integer.toString(gameBoard.board[x][y]));
            buttons[x][y].setBackground(Color.WHITE);
            Color[] colors = {
                Color.BLUE, Color.GREEN, Color.RED, Color.GRAY,
                Color.ORANGE, Color.PINK, Color.CYAN, Color.MAGENTA
            };
            if (gameBoard.board[x][y] <= colors.length)
                buttons[x][y].setForeground(colors[gameBoard.board[x][y] - 1]);
        } else {
            buttons[x][y].setBackground(Color.WHITE);
            for (int dx = -1; dx <= 1; dx++)
                for (int dy = -1; dy <= 1; dy++) {
                    int nx = x + dx, ny = y + dy;
                    if (nx >= 0 && nx < size && ny >= 0 && ny < size)
                        revealCell(nx, ny);
                }
        }

        checkWin();
    }

    private void toggleFlag(int x, int y) {
        if (gameBoard.revealed[x][y]) return;

        if (gameBoard.flagged[x][y]) {
            gameBoard.flagged[x][y] = false;
            buttons[x][y].setText("");
            buttons[x][y].setIcon(ImageManager.sebnemIcon);
            buttons[x][y].setBackground(Color.LIGHT_GRAY);
            currentFlags--;
        } else if (currentFlags < mineCount) {
            gameBoard.flagged[x][y] = true;
            buttons[x][y].setText("F");
            buttons[x][y].setForeground(Color.RED);
            buttons[x][y].setBackground(Color.YELLOW);
            currentFlags++;
        }

        flagLabel.setText("Flags: " + currentFlags + " / " + mineCount);
        checkWin();
    }

    private void checkWin() {
        int revealedCount = 0;
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                if (gameBoard.revealed[i][j]) revealedCount++;

        if (revealedCount == size * size - mineCount)
            endGame(true);
    }

    private void endGame(boolean win) {
        gameOver = true;
        stopTimer();
        statusLabel.setText(win ? "YOU WON!" : "YOU LOST");
        statusLabel.setForeground(win ? new Color(0, 128, 0) : Color.RED);
        if (!win) revealAllMines();
    }

    private void revealAllMines() {
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                if (gameBoard.board[i][j] == -1 && !gameBoard.flagged[i][j]) {
                    buttons[i][j].setIcon(null);
                    buttons[i][j].setText("X");
                    buttons[i][j].setBackground(Color.RED);
                    buttons[i][j].setForeground(Color.WHITE);
                }
    }

    private void startTimer() {
        secondsElapsed = 0;
        timer = new Timer(1000, e -> timerLabel.setText("Time: " + (++secondsElapsed)));
        timer.start();
    }

    private void stopTimer() {
        if (timer != null) timer.stop();
    }

    private void showSetupScreen() {
        JFrame inputFrame = new JFrame("Minesweeper Setup");
        inputFrame.setSize(300, 280);
        inputFrame.setLayout(new GridLayout(7, 1));
        inputFrame.setLocationRelativeTo(null);
        inputFrame.setResizable(false);
        if (ImageManager.appIcon != null)
            inputFrame.setIconImage(ImageManager.appIcon.getImage());

        if (ImageManager.sebnemIcon != null) {
            JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            imagePanel.add(new JLabel(ImageManager.sebnemIcon));
            inputFrame.add(imagePanel);
        }

        JPanel sizePanel = new JPanel(new FlowLayout());
        JLabel sizeLabel = new JLabel("Board size:");
        JTextField sizeField = new JTextField(10);
        JLabel sizeError = new JLabel(" ");
        sizeError.setForeground(Color.RED);
        sizePanel.add(sizeLabel);
        sizePanel.add(sizeField);

        JPanel minePanel = new JPanel(new FlowLayout());
        JLabel mineLabel = new JLabel("Mine count:");
        JTextField mineField = new JTextField(10);
        JLabel mineError = new JLabel(" ");
        mineError.setForeground(Color.RED);
        minePanel.add(mineLabel);
        minePanel.add(mineField);

        JButton startButton = new JButton("Start Game");
        if (ImageManager.sebnemIconSmall != null)
            startButton.setIcon(ImageManager.sebnemIconSmall);

        Runnable validateInputs = () -> {
            boolean valid = true;
            sizeError.setText(" ");
            mineError.setText(" ");
            int s = -1, m = -1;
            try {
                s = Integer.parseInt(sizeField.getText().trim());
                if (s < 2 || s > 20) {
                    sizeError.setText("Size must be 2–20");
                    valid = false;
                }
            } catch (NumberFormatException e) {
                sizeError.setText("Invalid number");
                valid = false;
            }
            try {
                m = Integer.parseInt(mineField.getText().trim());
                if (m <= 0 || (s != -1 && m >= s * s)) {
                    mineError.setText("Too many mines");
                    valid = false;
                }
            } catch (NumberFormatException e) {
                mineError.setText("Invalid number");
                valid = false;
            }
            startButton.setEnabled(valid);
        };

        sizeField.getDocument().addDocumentListener(new SimpleDocListener(validateInputs));
        mineField.getDocument().addDocumentListener(new SimpleDocListener(validateInputs));

        startButton.setEnabled(false);
        startButton.addActionListener(e -> {
            int newSize = Integer.parseInt(sizeField.getText().trim());
            int newMines = Integer.parseInt(mineField.getText().trim());
            inputFrame.dispose();
            new MineSweeper(newSize, newMines);
        });

        inputFrame.add(sizePanel);
        inputFrame.add(sizeError);
        inputFrame.add(minePanel);
        inputFrame.add(mineError);
        inputFrame.add(new JLabel(" ", SwingConstants.CENTER));
        inputFrame.add(startButton);
        inputFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Runtime.getRuntime().addShutdownHook(new Thread(MusicManager::stopBackground));
            MineSweeper dummy = new MineSweeper(2, 1);
            dummy.dispose();
            dummy.showSetupScreen();
        });
    }

    private static class SimpleDocListener implements javax.swing.event.DocumentListener {
        private final Runnable callback;
        SimpleDocListener(Runnable callback) { this.callback = callback; }
        public void insertUpdate(javax.swing.event.DocumentEvent e) { callback.run(); }
        public void removeUpdate(javax.swing.event.DocumentEvent e) { callback.run(); }
        public void changedUpdate(javax.swing.event.DocumentEvent e) { callback.run(); }
    }
}
