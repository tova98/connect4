package hr.tvz.ntovernic.connect4.common;

import hr.tvz.ntovernic.connect4.server.GameMove;

import java.io.Serializable;
import java.util.List;

public class GameSave implements Serializable {

    private String[][] board;
    private String playerOnTurn;
    private Boolean gameOver;
    private Boolean replayInProgress;
    private List<GameMove> gameMoves;

    public GameSave(final String[][] board, final String playerOnTurn, final Boolean gameOver, final Boolean replayInProgress, final List<GameMove> gameMoves) {
        this.board = board;
        this.playerOnTurn = playerOnTurn;
        this.gameOver = gameOver;
        this.replayInProgress = replayInProgress;
        this.gameMoves = gameMoves;
    }

    public String[][] getBoard() {
        return board;
    }

    public String getPlayerOnTurn() {
        return playerOnTurn;
    }

    public Boolean getGameOver() {
        return gameOver;
    }

    public Boolean getReplayInProgress() {
        return replayInProgress;
    }

    public List<GameMove> getGameMoves() {
        return gameMoves;
    }
}
