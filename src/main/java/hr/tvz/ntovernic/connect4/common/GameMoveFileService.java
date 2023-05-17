package hr.tvz.ntovernic.connect4.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class GameMoveFileService {

    private static final String MOVE_FILE_PATH = "C:/Users/Nikola/IdeaProjects/demo2/";

    private Boolean fileBusy;
    private final String fileName;

    public GameMoveFileService(final String gameId) throws IOException {
        this.fileBusy = false;
        this.fileName = MOVE_FILE_PATH + gameId + ".txt";

        Files.createFile(Path.of(fileName));
    }

    public synchronized void saveMoveToFile(final String move) throws InterruptedException, IOException {
        while (fileBusy) {
            wait();
        }

        fileBusy = true;

        Files.write(Path.of(fileName), (move + "\n").getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);

        fileBusy = false;
        notify();
    }

    public synchronized String getLastMoveFromFile() throws InterruptedException, IOException {
        while (fileBusy) {
            wait();
        }

        fileBusy = true;

        final List<String> moves = Files.exists(Path.of(fileName)) ? Files.readAllLines(Path.of(fileName)) : List.of();
        final String lastMove = moves.size() > 0 ? moves.get(moves.size() - 1) : "";

        fileBusy = false;
        notify();

        return lastMove;
    }
}
