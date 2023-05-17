package hr.tvz.ntovernic.connect4.client;

import hr.tvz.ntovernic.connect4.Connect4Controller;
import hr.tvz.ntovernic.connect4.common.Message;
import hr.tvz.ntovernic.connect4.common.MessageType;
import hr.tvz.ntovernic.connect4.rmi.chat.ChatClient;
import hr.tvz.ntovernic.connect4.rmi.chat.ChatClientImpl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Client {

    private final Socket socket;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;
    private final Connect4Controller controller;
    private final ChatClient chatClient;

    public Client(final String host, final int port, final Connect4Controller controller) throws IOException, NotBoundException {
        socket = new Socket(host, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        this.controller = controller;

        sendNewGameMessage();
        new ClientToServerThread(this).start();
        chatClient = new ChatClientImpl(this);
    }

    public ObjectInputStream getInputStream() {
        return in;
    }

    public Connect4Controller getController() {
        return controller;
    }

    public void sendChatMessage(final String message) throws RemoteException {
        chatClient.sendMessage(message);
    }

    public void sendNewGameMessage() throws IOException {
        out.writeObject(new Message(MessageType.START_GAME, ClientInfo.gameId));
        out.flush();
    }

    public void sendDropRequest(final Integer column) throws IOException {
        out.writeObject(newDropMessage(column));
        out.flush();
    }

    public void saveGame() throws IOException {
        out.writeObject(new Message(MessageType.SAVE_GAME, null));
        out.flush();
    }

    public void loadGame(final String gameSave) throws IOException {
        out.writeObject(new Message(MessageType.LOAD_GAME, gameSave));
        out.flush();
    }

    public void startReplay() throws IOException {
        out.writeObject(new Message(MessageType.REPLAY, null));
        out.flush();
    }

    public void close() throws IOException {
        out.close();
        in.close();
        socket.close();
    }

    private Message newDropMessage(final Integer column) {
        return new Message(MessageType.DROP, column);
    }
}
