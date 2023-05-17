package hr.tvz.ntovernic.connect4.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketInfo {

  private final Socket socket;
  private final ObjectOutputStream out;
  private final ObjectInputStream in;

  public SocketInfo(final Socket socket) throws IOException {
    this.socket = socket;
    this.out = new ObjectOutputStream(socket.getOutputStream());
    this.in = new ObjectInputStream(socket.getInputStream());
  }

  public Socket getSocket() {
    return socket;
  }

  public ObjectOutputStream getOut() {
    return out;
  }

  public ObjectInputStream getIn() {
    return in;
  }
}
