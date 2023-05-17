package hr.tvz.ntovernic.connect4;

import hr.tvz.ntovernic.connect4.client.ClientInfo;
import hr.tvz.ntovernic.connect4.client.ConfigurationProperties;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Connect4Application extends Application {

    @Override
    public void start(final Stage stage) throws IOException {
        final FXMLLoader fxmlLoader = new FXMLLoader(Connect4Application.class.getResource("lobby-screen.fxml"));
        final Scene scene = new Scene(fxmlLoader.load(), 920, 582);
        stage.setTitle("Connect4");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setOnCloseRequest(onCloseEventHandler());
        stage.show();
    }

    public static void main(final String[] args) {
        if (args.length > 0) {
            ClientInfo.playerName = args[0];
        } else {
            System.err.println("Player name is mandatory!");
            System.exit(-1);
        }
        loadProperties();
        launch();
    }

    private static void loadProperties() {
        try {
            final Properties properties = new Properties();
            properties.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.fscontext.RefFSContextFactory");
            properties.put(Context.PROVIDER_URL, "file:C:/Users/Nikola/IdeaProjects/demo2/config/");
            final Context context = new InitialContext(properties);

            final Object object = context.lookup("configuration.properties");
            final Properties configurationProperties = new Properties();
            configurationProperties.load(new FileReader(object.toString()));

            ConfigurationProperties.gameServerHost = configurationProperties.getProperty("game.server.host");
            ConfigurationProperties.gameServerPort = Integer.parseInt(configurationProperties.getProperty("game.server.port"));
            ConfigurationProperties.rmiServerAddress = configurationProperties.getProperty("rmi.server.address");
        } catch (final NamingException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private EventHandler<WindowEvent> onCloseEventHandler() {
        return event ->  {
            Platform.exit();
            System.exit(0);
        };
    }
}