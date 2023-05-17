package hr.tvz.ntovernic.connect4.rmi.chat;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatClient extends Remote {

    void sendMessage(String message) throws RemoteException;
    void receiveMessage(String message) throws RemoteException;
}
