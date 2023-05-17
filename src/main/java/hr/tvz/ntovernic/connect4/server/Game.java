package hr.tvz.ntovernic.connect4.server;

import hr.tvz.ntovernic.connect4.common.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Game {

    private static final String SAVE_GAME_PATH = "C:/Users/Nikola/IdeaProjects/demo2/";

    private final SocketInfo p1SocketInfo;
    private final SocketInfo p2SocketInfo;
    private final String gameId;

    private GameState gameState;
    private List<ObjectOutputStream> clientOutputStreams;

    private GameMoveFileService gameMoveFileService;
    private GameMoveXmlService gameMoveXmlService;

    private String saveGameFile;

    public Game(final SocketInfo p1SocketInfo, final SocketInfo p2SocketInfo, final String gameId) {
        this.p1SocketInfo = p1SocketInfo;
        this.p2SocketInfo = p2SocketInfo;
        this.gameId = gameId;
        this.saveGameFile = SAVE_GAME_PATH + gameId + ".bin";
    }

    public GameState getGameState() {
        return gameState;
    }

    public void start() throws IOException, InterruptedException {
        gameState = new GameState();
        clientOutputStreams = List.of(p1SocketInfo.getOut(), p2SocketInfo.getOut());

        gameMoveFileService = new GameMoveFileService(gameId);
        gameMoveXmlService = new GameMoveXmlService(gameId);

        new ServerToClientThread(p1SocketInfo, this, Color.RED, gameMoveFileService, gameMoveXmlService).start();
        new ServerToClientThread(p2SocketInfo, this, Color.YELLOW, gameMoveFileService, gameMoveXmlService).start();

        new ServerLastMoveThread(this, gameMoveFileService).start();

        broadcastGameSaveList();
    }

    public void broadcastBoardState() {
        final Message message = new Message(MessageType.BOARD, gameState.getBoardState());
        clientOutputStreams.forEach(out -> sendMessage(out, message));
    }

    public void broadcastGameOver(final Paint winner) throws InterruptedException {
        final Message message = new Message(MessageType.GAME_OVER, winner.toString());
        clientOutputStreams.forEach(out -> sendMessage(out, message));

        gameMoveXmlService.createGameMovesXml(gameState.getGameMoves());
    }

    public void broadcastLastMove(final String move) {
        final Message message = new Message(MessageType.LAST_MOVE, move);
        clientOutputStreams.forEach(out -> sendMessage(out, message));
    }

    public void broadcastGameSaveList() throws IOException, InterruptedException {
        Thread.sleep(1000);
        final List<String> gameSaveList = getSaveGameFileList();
        final Message message = new Message(MessageType.GAME_SAVE_LIST, gameSaveList);
        clientOutputStreams.forEach(out -> sendMessage(out, message));
    }

    public void saveGameToFile(final GameSave gameSave) throws IOException {
        final ObjectOutputStream saveOut = new ObjectOutputStream(new FileOutputStream(saveGameFile));
        saveOut.writeObject(gameSave);
        saveOut.flush();
        saveOut.close();
    }

    public GameSave loadGameFromFile(final String gameSave) throws Exception {
        final ObjectInputStream saveIn = new ObjectInputStream(new FileInputStream(SAVE_GAME_PATH + gameSave));
        return (GameSave) saveIn.readObject();
    }

    private List<String> getSaveGameFileList() throws IOException {
        return Files.list(Path.of(SAVE_GAME_PATH))
                .map(path -> path.getFileName().toString())
                .filter(fileName -> fileName.endsWith(".bin"))
                .toList();
    }

    private void sendMessage(final ObjectOutputStream out, final Message message) {
        try {
            out.writeObject(message);
            out.flush();
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
