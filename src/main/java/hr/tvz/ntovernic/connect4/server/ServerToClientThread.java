package hr.tvz.ntovernic.connect4.server;

import hr.tvz.ntovernic.connect4.common.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ServerToClientThread extends Thread {

    private final Socket socket;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;
    private final Game game;
    private final Paint player;
    private final GameMoveFileService gameMoveFileService;
    private final GameMoveXmlService gameMoveXmlService;

    public ServerToClientThread(
            final SocketInfo socketInfo,
            final Game game,
            final Paint player,
            final GameMoveFileService gameMoveFileService,
            final GameMoveXmlService gameMoveXmlService) {

        this.socket = socketInfo.getSocket();
        this.out = socketInfo.getOut();
        this.in = socketInfo.getIn();
        this.game = game;
        this.player = player;
        this.gameMoveFileService = gameMoveFileService;
        this.gameMoveXmlService = gameMoveXmlService;
    }

    @Override
    public void run() {
        try {
            Message message;
            while ((message = (Message) in.readObject()) != null) {
                if (MessageType.SAVE_GAME.equals(message.getType())) {
                    saveGame();
                } else if (MessageType.LOAD_GAME.equals(message.getType())) {
                    loadGame((String) message.getContent());
                } else if (MessageType.REPLAY.equals(message.getType())) {
                    replayGame();
                } else {
                    if (game.getGameState().isPlayerOnTurn(player)) {
                        makeAMove((Integer) message.getContent());
                    }
                }

                Thread.sleep(100);
            }

            out.close();
            in.close();
            socket.close();
        } catch (final Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("Client has disconnected!");
        }
    }

    private void makeAMove(final Integer column) throws IOException, InterruptedException {
        if (game.getGameState().canPlace(column)) {
            game.getGameState().place(column, player);
            game.broadcastBoardState();

            if (game.getGameState().isWinner(Color.RED)) {
                game.broadcastGameOver(Color.RED);
                game.getGameState().setGameOver();
            } else if (game.getGameState().isWinner(Color.YELLOW)) {
                game.broadcastGameOver(Color.YELLOW);
                game.getGameState().setGameOver();
            }

            gameMoveFileService.saveMoveToFile(String.format("%s %s", player, column));
        }
    }

    private void saveGame() throws IOException, InterruptedException {
        final GameSave gameSave = game.getGameState().toGameSave();
        game.saveGameToFile(gameSave);
        game.broadcastGameSaveList();
    }

    private void loadGame(final String gameSaveFile) throws Exception {
        final GameSave gameSave = game.loadGameFromFile(gameSaveFile);
        game.getGameState().loadGame(gameSave);
        game.broadcastBoardState();
        game.broadcastLastMove("");
    }

    private void replayGame() throws Exception {
        game.getGameState().startReplay();
        game.broadcastBoardState();

        final List<GameMove> gameMoves = gameMoveXmlService.getGameMovesFromXml();
        gameMoves.forEach(move -> {
            try {
                Thread.sleep(1000);
                game.getGameState().place(move.getColumn(), Color.valueOf(move.getColor()));
                game.broadcastBoardState();
            } catch (final InterruptedException e) {
                throw new RuntimeException();
            }
        });
    }
}
