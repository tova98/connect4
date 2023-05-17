package hr.tvz.ntovernic.connect4.client;

import hr.tvz.ntovernic.connect4.common.Message;
import hr.tvz.ntovernic.connect4.common.MessageType;

import java.io.IOException;
import java.util.List;

public class ClientToServerThread extends Thread {

    private final Client client;

    public ClientToServerThread(final Client client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            Message message;
            while ((message = (Message) client.getInputStream().readObject()) != null) {
                if (MessageType.GAME_OVER.equals(message.getType())) {
                    client.getController().showGameOver((String) message.getContent());
                } else if (MessageType.LAST_MOVE.equals(message.getType())) {
                    client.getController().updateLastMoveLabel((String) message.getContent());
                } else if (MessageType.GAME_SAVE_LIST.equals(message.getType())) {
                    client.getController().updateLoadGameList((List<String>) message.getContent());
                } else {
                    client.getController().updateBoard((String[][]) message.getContent());
                }

                Thread.sleep(100);
            }
        } catch (final IOException | InterruptedException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
