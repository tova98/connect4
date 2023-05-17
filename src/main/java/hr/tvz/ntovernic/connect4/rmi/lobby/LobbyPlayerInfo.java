package hr.tvz.ntovernic.connect4.rmi.lobby;

import java.io.Serializable;

public class LobbyPlayerInfo implements Serializable {

  private String playerName;
  private LobbyClient lobbyClient;

  public LobbyPlayerInfo(final String playerName, final LobbyClient lobbyClient) {
    this.playerName = playerName;
    this.lobbyClient = lobbyClient;
  }

  public String getPlayerName() {
    return playerName;
  }

  public LobbyClient getLobbyClient() {
    return lobbyClient;
  }
}
