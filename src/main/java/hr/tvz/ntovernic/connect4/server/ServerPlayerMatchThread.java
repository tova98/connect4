package hr.tvz.ntovernic.connect4.server;

import hr.tvz.ntovernic.connect4.common.Message;

import java.io.IOException;

public class ServerPlayerMatchThread extends Thread {

  private final GameServer gameServer;
  private final SocketInfo socketInfo;

  public ServerPlayerMatchThread(final GameServer gameServer, final SocketInfo socketInfo) {
    this.gameServer = gameServer;
    this.socketInfo = socketInfo;
  }

  @Override
  public void run() {
    try {
      final Message message = (Message) socketInfo.getIn().readObject();
      final String gameId = (String) message.getContent();
      gameServer.joinGame(gameId, socketInfo);
    } catch (final IOException | ClassNotFoundException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
