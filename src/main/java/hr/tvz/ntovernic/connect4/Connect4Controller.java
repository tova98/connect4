package hr.tvz.ntovernic.connect4;

import hr.tvz.ntovernic.connect4.client.Client;
import hr.tvz.ntovernic.connect4.client.ClientInfo;
import hr.tvz.ntovernic.connect4.client.ConfigurationProperties;
import hr.tvz.ntovernic.connect4.client.DocumentationGenerator;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.ResourceBundle;

public class Connect4Controller implements Initializable {

    private static final Integer ROW_COUNT = 6;
    private static final Integer COLUMN_COUNT = 7;

    private Client client;
    private Circle[][] board;

    @FXML
    private Pane rootPane;

    @FXML
    private TextArea chatTextArea;

    @FXML
    private TextField chatTextField;

    @FXML
    private Label playerNameLabel;

    @FXML
    private Label lastMoveLabel;

    @FXML
    private Menu loadGameMenu;

    @Override
    public void initialize(final URL url, final ResourceBundle resourceBundle) {
        board = new Circle[ROW_COUNT][COLUMN_COUNT];

        for (int i = 0; i < ROW_COUNT; i++) {
            for (int j = 0; j < COLUMN_COUNT; j++) {
                final Circle circle = (Circle) rootPane.lookup(String.format("#c%d%d", i, j));
                circle.setFill(Color.WHITE);
                board[i][j] = circle;
            }
        }

        playerNameLabel.setText(ClientInfo.playerName);

        client = newClient();
    }

    @FXML
    protected void drop(final ActionEvent event) throws IOException {
        if (event.getSource() instanceof Button dropButton) {
            final Integer column = getColumn(dropButton.getId());
            client.sendDropRequest(column);
        }
    }

    @FXML
    protected void sendChatMessage() throws RemoteException {
        client.sendChatMessage(String.format("%s: %s \n", ClientInfo.playerName, chatTextField.getText()));
        chatTextField.setText("");
    }

    @FXML
    protected void saveGame() throws IOException {
        client.saveGame();
    }

    @FXML
    protected void startReplay() throws IOException {
        client.startReplay();
    }

    @FXML
    protected void generateDocumentation() throws IOException, ClassNotFoundException {
        DocumentationGenerator.generateDocumentation();
    }

    public void receiveChatMessage(final String message) {
        chatTextArea.appendText(message);
    }

    public void updateBoard(final String[][] boardState) {
        for (int i = 0; i < ROW_COUNT; i++) {
            for (int j = 0; j < COLUMN_COUNT; j++) {
                board[i][j].setFill(Color.valueOf(boardState[i][j]));
            }
        }
    }

    public void showGameOver(final String player) {
        final HBox messageBox = new HBox();
        final Label message = new Label("The winner is: ");
        final Label winner = getPlayerLabel(Color.valueOf(player));
        message.setStyle("-fx-font-size: 18;");
        messageBox.getChildren().addAll(message, winner);

        Platform.runLater(() -> {
            final Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Congratulations!");
            alert.setHeaderText("We have a winner!");
            alert.getDialogPane().setContent(messageBox);
            alert.showAndWait();
        });
    }

    public void updateLastMoveLabel(final String move) {
        if (move.isBlank()) return;

        final String player = getPlayerFromMove(move);
        final String column = move.split(" ")[1];

        Platform.runLater(() -> lastMoveLabel.setText(String.format("%s %s", player, column)));
    }

    public void updateLoadGameList(final List<String> gameSaveList) {
        loadGameMenu.getItems().clear();
        Platform.runLater(() -> gameSaveList.forEach(gameSave -> {
            final MenuItem menuItem = new MenuItem(gameSave);
            menuItem.setOnAction(event -> {
                try {
                    client.loadGame(gameSave);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            loadGameMenu.getItems().add(menuItem);
        }));
    }

    private Label getPlayerLabel(final Paint paint) {
        final Label label;
        if (paint.equals(Color.RED)) {
            label = new Label("RED");
            label.setStyle("-fx-text-fill: red; -fx-font-size: 18;");
        } else {
            label = new Label("YELLOW");
            label.setStyle("-fx-text-fill: yellow; -fx-font-size: 18;");
        }

        return label;
    }

    private Client newClient() {
        try {
            return new Client(ConfigurationProperties.gameServerHost, ConfigurationProperties.gameServerPort, this);
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        } catch (final NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    private Integer getColumn(final String objectId) {
        return switch(objectId) {
            case "drop0" -> 0;
            case "drop1" -> 1;
            case "drop2" -> 2;
            case "drop3" -> 3;
            case "drop4" -> 4;
            case "drop5" -> 5;
            case "drop6" -> 6;
            default -> throw new RuntimeException("Illegal action!");
        };
    }


    private String getPlayerFromMove(final String move) {
        final String playerColorHex = move.split(" ")[0];
        final Color player = Color.valueOf(playerColorHex);

        return Color.RED.equals(player) ? "RED" : "YELLOW";
    }
}