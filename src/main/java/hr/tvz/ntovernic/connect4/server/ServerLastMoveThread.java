package hr.tvz.ntovernic.connect4.server;

import hr.tvz.ntovernic.connect4.common.GameMoveFileService;

import java.io.IOException;

public class ServerLastMoveThread extends Thread {

    private final Game game;
    private final GameMoveFileService gameMoveFileService;

    public ServerLastMoveThread(final Game game, final GameMoveFileService gameMoveFileService) {
        this.game = game;
        this.gameMoveFileService = gameMoveFileService;
    }

    @Override
    public void run() {
        while (true) {
            try {
                final String move = gameMoveFileService.getLastMoveFromFile();
                game.broadcastLastMove(move);
                Thread.sleep(100);
            } catch (final InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
