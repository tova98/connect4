package hr.tvz.ntovernic.connect4.server;

import hr.tvz.ntovernic.connect4.common.GameSave;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.List;

public class GameState {

    private static final Integer ROW_COUNT = 6;
    private static final Integer COLUMN_COUNT = 7;

    private Paint[][] board;
    private Paint playerOnTurn;
    private Boolean gameOver;
    private Boolean replayInProgress;
    private List<GameMove> gameMoves;

    public GameState() {
        this.board = newBoard();
        this.playerOnTurn = Color.RED;
        this.gameOver = false;
        this.replayInProgress = false;
        this.gameMoves = new ArrayList<>();
    }

    public GameSave toGameSave() {
        return new GameSave(getBoardState(), playerOnTurn.toString(), gameOver, false, gameMoves);
    }

    public String[][] getBoardState() {
        final String[][] boardState = new String[ROW_COUNT][COLUMN_COUNT];
        for (int i = 0; i < ROW_COUNT; i++) {
            for (int j = 0; j < COLUMN_COUNT; j++) {
                boardState[i][j] = board[i][j].toString();
            }
        }

        return boardState;
    }

    public void loadGame(final GameSave gameSave) {
        board = parseBoard(gameSave.getBoard());
        playerOnTurn = Color.valueOf(gameSave.getPlayerOnTurn());
        gameOver = gameSave.getGameOver();
        replayInProgress = gameSave.getReplayInProgress();
        gameMoves = gameSave.getGameMoves();
    }

    public List<GameMove> getGameMoves() {
        return gameMoves;
    }

    public boolean isPlayerOnTurn(final Paint player) {
        return player.equals(playerOnTurn);
    }

    public boolean canPlace(final Integer column) {
        if (Boolean.FALSE.equals(gameOver) && Boolean.FALSE.equals(replayInProgress)) {
            for (int i = 0; i < ROW_COUNT; i++) {
                if (canPlace(i, column)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean canPlace(final Integer row, final Integer column) {
        return board[row][column].equals(Color.WHITE) && (row == ROW_COUNT - 1 || !board[row + 1][column].equals(Color.WHITE));
    }

    public void place(final Integer column, final Paint color) {
        for (int i = 0; i < ROW_COUNT; i++) {
            if (canPlace(i, column)) {
                board[i][column] = color;
                gameMoves.add(new GameMove(column, color.toString()));
                nextPlayer();
            }
        }
    }

    public void setGameOver() {
        gameOver = true;
    }

    public boolean isWinner(final Paint player) {
        for (int i = 0; i < ROW_COUNT; i++) {
            for (int j = 0; j < COLUMN_COUNT - 3; j++) {
                if (board[i][j].equals(player) && board[i][j+1].equals(player) && board[i][j+2].equals(player) && board[i][j+3].equals(player)) {
                    return true;
                }
            }
        }

        for (int j = 0; j < COLUMN_COUNT; j++) {
            for (int i = 0; i < ROW_COUNT - 3; i++) {
                if (board[i][j].equals(player) && board[i+1][j].equals(player) && board[i+2][j].equals(player) && board[i+3][j].equals(player)) {
                    return true;
                }
            }
        }

        for (int i = 0; i < ROW_COUNT - 3; i++) {
            for (int j = 0; j < COLUMN_COUNT - 3; j++) {
                if (board[i][j].equals(player) && board[i+1][j+1].equals(player) && board[i+2][j+2].equals(player) && board[i+3][j+3].equals(player)) {
                    return true;
                }
            }
        }

        for (int i = 0; i < ROW_COUNT - 3; i++) {
            for (int j = 3; j < COLUMN_COUNT; j++) {
                if (board[i][j].equals(player) && board[i+1][j-1].equals(player) && board[i+2][j-2].equals(player) && board[i+3][j-3].equals(player)) {
                    return true;
                }
            }
        }

        return false;
    }

    public void startReplay() {
        board = newBoard();
        replayInProgress = true;
    }

    private Paint[][] newBoard() {
        final Paint[][] newBoard = new Paint[ROW_COUNT][COLUMN_COUNT];
        for (int i = 0; i < ROW_COUNT; i++) {
            for (int j = 0; j < COLUMN_COUNT; j++) {
                newBoard[i][j] = Color.WHITE;
            }
        }

        return newBoard;
    }

    private Paint[][] parseBoard(final String[][] savedBoard) {
        final Paint[][] newBoard = new Paint[ROW_COUNT][COLUMN_COUNT];
        for (int i = 0; i < ROW_COUNT; i++) {
            for (int j = 0; j < COLUMN_COUNT; j++) {
                newBoard[i][j] = Color.valueOf(savedBoard[i][j]);
            }
        }

        return newBoard;
    }

    private void nextPlayer() {
        playerOnTurn = playerOnTurn.equals(Color.RED) ? Color.YELLOW : Color.RED;
    }
}
