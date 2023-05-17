package hr.tvz.ntovernic.connect4.rmi.chat;

import hr.tvz.ntovernic.connect4.client.Client;
import hr.tvz.ntovernic.connect4.client.ClientInfo;
import hr.tvz.ntovernic.connect4.client.ConfigurationProperties;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ChatClientImpl extends UnicastRemoteObject implements ChatClient {

    private final ChatServer chatServer;
    private final Client client;

    public ChatClientImpl(final Client client) throws RemoteException, MalformedURLException, NotBoundException {
        super();
        this.client = client;
        chatServer = (ChatServer) Naming.lookup("rmi://" + ConfigurationProperties.rmiServerAddress + "/ChatServer");
        chatServer.joinChatRoom(this, ClientInfo.gameId);
    }

    @Override
    public void sendMessage(final String message) throws RemoteException {
        chatServer.sendMessage(ClientInfo.gameId, message);
    }

    @Override
    public void receiveMessage(final String message) throws RemoteException {
       client.getController().receiveChatMessage(message);
    }
}
