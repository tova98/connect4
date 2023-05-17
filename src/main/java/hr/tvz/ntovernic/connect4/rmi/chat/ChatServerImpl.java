package hr.tvz.ntovernic.connect4.rmi.chat;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class ChatServerImpl extends UnicastRemoteObject implements ChatServer {

    private final Map<String, List<ChatClient>> chatRooms;

    public ChatServerImpl() throws RemoteException {
        super();
        chatRooms = new HashMap<>();
    }

    @Override
    public String createChatRoom() throws RemoteException {
        final String chatRoomId = UUID.randomUUID().toString();
        chatRooms.put(chatRoomId, new ArrayList<>());

        return chatRoomId;
    }

    @Override
    public void joinChatRoom(final ChatClient chatClient, final String chatRoomId) throws RemoteException {
        if (!chatRooms.containsKey(chatRoomId)) {
            chatRooms.put(chatRoomId, new ArrayList<>());
        }
        chatRooms.get(chatRoomId).add(chatClient);
    }

    @Override
    public void sendMessage(final String chatRoomId, final String message) throws RemoteException {
        for (final ChatClient client : chatRooms.get(chatRoomId)) {
            client.receiveMessage(message);
        }
    }
}
