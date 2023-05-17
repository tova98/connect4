package hr.tvz.ntovernic.connect4.server;

import java.io.Serializable;

public class GameMove implements Serializable {

  private Integer column;
  private String color;

  public GameMove(final Integer column, final String color) {
    this.column = column;
    this.color = color;
  }

  public Integer getColumn() {
    return column;
  }

  public String getColor() {
    return color;
  }
}
