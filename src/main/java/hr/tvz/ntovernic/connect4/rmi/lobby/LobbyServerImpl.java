package hr.tvz.ntovernic.connect4.rmi.lobby;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LobbyServerImpl extends UnicastRemoteObject implements LobbyServer {

    private final List<LobbyPlayerInfo> playerInfos;

    public LobbyServerImpl() throws RemoteException {
        super();
        playerInfos = new ArrayList<>();
    }

    @Override
    public void joinLobby(final LobbyPlayerInfo lobbyPlayerInfo) throws RemoteException {
        playerInfos.add(lobbyPlayerInfo);
        broadcastPlayerList();
    }

    @Override
    public void broadcastPlayerList() throws RemoteException {
        for (final LobbyClient client : getLobbyClients()) {
            client.getPlayerList(clientsToPlayerList());
        }
    }

    @Override
    public void createNewGame(final String player1, final String player2) throws RemoteException {
        final String gameId = UUID.randomUUID().toString();
        final LobbyPlayerInfo player1Info = getLobbyPlayerInfoByPlayerName(player1);
        final LobbyPlayerInfo player2Info = getLobbyPlayerInfoByPlayerName(player2);
        final LobbyClient player1LobbyClient = player1Info.getLobbyClient();
        final LobbyClient player2LobbyClient = player2Info.getLobbyClient();

        sendStartGameCall(player1LobbyClient, gameId);
        sendStartGameCall(player2LobbyClient, gameId);

        playerInfos.remove(player1Info);
        playerInfos.remove(player2Info);
        broadcastPlayerList();
    }

    private void sendStartGameCall(final LobbyClient lobbyClient, final String gameId) {
        new Thread(() -> {
            try {
                lobbyClient.startGame(gameId);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    private List<LobbyClient> getLobbyClients() {
        return playerInfos.stream()
                .map(LobbyPlayerInfo::getLobbyClient)
                .toList();
    }

    private List<String> clientsToPlayerList() {
        return playerInfos.stream()
                .map(LobbyPlayerInfo::getPlayerName)
                .toList();
    }

    private LobbyPlayerInfo getLobbyPlayerInfoByPlayerName(final String playerName) {
        return playerInfos.stream()
                .filter(playerInfo -> playerInfo.getPlayerName().equals(playerName))
                .findFirst()
                .orElse(null);
    }
}
