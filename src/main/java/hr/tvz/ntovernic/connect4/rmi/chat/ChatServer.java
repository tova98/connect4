package hr.tvz.ntovernic.connect4.rmi.chat;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatServer extends Remote {

    String createChatRoom() throws RemoteException;
    void joinChatRoom(ChatClient chatClient, String chatRoomId) throws RemoteException;
    void sendMessage(String chatRoomId, String message) throws RemoteException;
}
