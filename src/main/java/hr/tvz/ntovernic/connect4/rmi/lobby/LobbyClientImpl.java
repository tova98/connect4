package hr.tvz.ntovernic.connect4.rmi.lobby;

import hr.tvz.ntovernic.connect4.LobbyController;
import hr.tvz.ntovernic.connect4.client.ClientInfo;
import hr.tvz.ntovernic.connect4.client.ConfigurationProperties;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class LobbyClientImpl extends UnicastRemoteObject implements LobbyClient {

    private final LobbyServer lobbyServer;
    private final LobbyController lobbyController;

    public LobbyClientImpl(final LobbyController lobbyController) throws RemoteException, MalformedURLException, NotBoundException {
        super();
        this.lobbyController = lobbyController;
        lobbyServer = (LobbyServer)  Naming.lookup("rmi://" + ConfigurationProperties.rmiServerAddress + "/LobbyServer");
        lobbyServer.joinLobby(new LobbyPlayerInfo(ClientInfo.playerName, this));
    }

    @Override
    public void getPlayerList(final List<String> playerList) throws RemoteException {
        lobbyController.updatePlayerList(playerList);
    }

    @Override
    public void selectPlayer(final String player1, final String player2) throws IOException {
        lobbyServer.createNewGame(player1, player2);
    }

    @Override
    public void startGame(final String gameId) throws IOException {
        lobbyController.changeToGameScene(gameId);
    }
}
