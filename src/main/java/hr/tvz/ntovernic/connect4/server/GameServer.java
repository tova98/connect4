package hr.tvz.ntovernic.connect4.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameServer {

  protected static final Map<String, List<SocketInfo>> gameList = new HashMap<>();

  public void start() {
    try (ServerSocket serverSocket = new ServerSocket(8888)) {
      System.out.println("Server started on port 8888!");

      Socket socket;
      while ((socket = serverSocket.accept()) != null) {
        final SocketInfo socketInfo = new SocketInfo(socket);
        new ServerPlayerMatchThread(this, socketInfo).start();

        System.out.println("Client connected from: " + socket.getInetAddress());

        Thread.sleep(100);
      }
    } catch (final IOException e) {
      System.err.println("Could not start server!");
      System.exit(-1);
    } catch (final InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public synchronized void joinGame(final String gameId, final SocketInfo socketInfo) throws IOException, InterruptedException {
    if (!gameList.containsKey(gameId)) {
      gameList.put(gameId, new ArrayList<>());
    }

    gameList.get(gameId).add(socketInfo);

    if (gameList.get(gameId).size() == 2) {
      new Game(gameList.get(gameId).get(0), gameList.get(gameId).get(1), gameId).start();
    }
  }
}
