package hr.tvz.ntovernic.connect4.common;

import java.io.Serializable;

public class Message implements Serializable {

  private MessageType type;
  private Object content;

  public Message(MessageType type, Object content) {
    this.type = type;
    this.content = content;
  }

  public MessageType getType() {
    return type;
  }

  public Object getContent() {
    return content;
  }

  @Override
  public String toString() {
    return String.format("Message{type=%s, content=%s}", type, content);
  }
}
