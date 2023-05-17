package hr.tvz.ntovernic.connect4.rmi.lobby;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface LobbyClient extends Remote {

    void getPlayerList(List<String> playerList) throws RemoteException;
    void selectPlayer(String player1, String player2) throws IOException;
    void startGame(String gameId) throws IOException;
}
