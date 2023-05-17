package hr.tvz.ntovernic.connect4.rmi.lobby;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface LobbyServer extends Remote {

    void joinLobby(LobbyPlayerInfo lobbyPlayerInfo) throws RemoteException;
    void broadcastPlayerList() throws RemoteException;
    void createNewGame(String player1, String player2) throws RemoteException;
}
