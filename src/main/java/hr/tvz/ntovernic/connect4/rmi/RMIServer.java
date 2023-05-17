package hr.tvz.ntovernic.connect4.rmi;

import hr.tvz.ntovernic.connect4.rmi.chat.ChatServer;
import hr.tvz.ntovernic.connect4.rmi.chat.ChatServerImpl;
import hr.tvz.ntovernic.connect4.rmi.lobby.LobbyServer;
import hr.tvz.ntovernic.connect4.rmi.lobby.LobbyServerImpl;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer {

    public static void main(final String[] args) throws RemoteException {
        final Registry registry = LocateRegistry.createRegistry(1111);
        final LobbyServer lobbyServer = new LobbyServerImpl();
        final ChatServer chatServer = new ChatServerImpl();
        registry.rebind("LobbyServer", lobbyServer);
        registry.rebind("ChatServer", chatServer);

        System.out.println("RMI server started.");
    }
}
