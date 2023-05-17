package hr.tvz.ntovernic.connect4;

import hr.tvz.ntovernic.connect4.client.ClientInfo;
import hr.tvz.ntovernic.connect4.rmi.lobby.LobbyClient;
import hr.tvz.ntovernic.connect4.rmi.lobby.LobbyClientImpl;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.ResourceBundle;

public class LobbyController implements Initializable {

    private LobbyClient lobbyClient;

    @FXML
    private Pane rootPane;

    @FXML
    private ListView<String> playerListView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            lobbyClient = new LobbyClientImpl(this);
        } catch (RemoteException | MalformedURLException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void selectPlayer() throws IOException {
        lobbyClient.selectPlayer(ClientInfo.playerName, playerListView.getSelectionModel().getSelectedItem());
    }

    public void updatePlayerList(final List<String> playerList) {
        final List<String> otherPlayersList = playerList.stream()
                .filter(playerName -> !playerName.equals(ClientInfo.playerName))
                .toList();
        Platform.runLater(() -> playerListView.setItems(FXCollections.observableList(otherPlayersList)));
    }

    public void changeToGameScene(final String gameId) throws IOException {
        ClientInfo.gameId = gameId;
        final Parent game = FXMLLoader.load(Connect4Application.class.getResource("connect4-screen.fxml"));
        final Scene gameScene = new Scene(game);
        final Stage window = (Stage) rootPane.getScene().getWindow();

        Platform.runLater(() -> {
            window.setScene(gameScene);
            window.show();
        });
    }
}
