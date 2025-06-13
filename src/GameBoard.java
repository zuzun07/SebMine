
public class GameBoard {
	    public int[][] board;
	    public boolean[][] revealed;
	    public boolean[][] flagged;

	    private final int size;
	    private final int mineCount;

	    public GameBoard(int size, int mineCount) {
	        this.size = size;
	        this.mineCount = mineCount;
	        this.board = new int[size][size];
	        this.revealed = new boolean[size][size];
	        this.flagged = new boolean[size][size];
	        initializeBoard();
	    }

	    public void initializeBoard() {
	        int count = 0;
	        while (count < mineCount) {
	            int x = (int) (Math.random() * size);
	            int y = (int) (Math.random() * size);
	            if (board[x][y] != -1) {
	                board[x][y] = -1;
	                count++;
	            }
	        }

	        for (int i = 0; i < size; i++)
	            for (int j = 0; j < size; j++)
	                if (board[i][j] != -1) {
	                    int c = 0;
	                    for (int dx = -1; dx <= 1; dx++)
	                        for (int dy = -1; dy <= 1; dy++) {
	                            int nx = i + dx, ny = j + dy;
	                            if (nx >= 0 && nx < size && ny >= 0 && ny < size && board[nx][ny] == -1) c++;
	                        }
	                    board[i][j] = c;
	                }
	    }


}
