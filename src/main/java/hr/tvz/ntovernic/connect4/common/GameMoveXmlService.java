package hr.tvz.ntovernic.connect4.common;

import hr.tvz.ntovernic.connect4.server.GameMove;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class GameMoveXmlService {

  private static final String XML_FILE_PATH = "C:/Users/Nikola/IdeaProjects/demo2/";

  private Boolean fileBusy;
  private final String fileName;

  public GameMoveXmlService(final String gameId) throws IOException {
    this.fileBusy = false;
    this.fileName = XML_FILE_PATH + gameId + ".xml";

    Files.createFile(Path.of(fileName));
  }

  public synchronized void createGameMovesXml(final List<GameMove> gameMoves) throws InterruptedException {
    while (fileBusy) {
      wait();
    }

    fileBusy = true;

    final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    final DocumentBuilder builder;
    try {
      builder = factory.newDocumentBuilder();
      final Document document = builder.newDocument();
      final Element root = document.createElement("gameMoves");
      document.appendChild(root);

      for (final GameMove gameMove : gameMoves) {
        final Element gameMoveElement = document.createElement("gameMove");
        root.appendChild(gameMoveElement);

        final Element colorElement = document.createElement("color");
        colorElement.appendChild(document.createTextNode(gameMove.getColor()));
        gameMoveElement.appendChild(colorElement);

        final Element columnElement = document.createElement("column");
        columnElement.appendChild(document.createTextNode(String.valueOf(gameMove.getColumn())));
        gameMoveElement.appendChild(columnElement);
      }

      final TransformerFactory transformerFactory = TransformerFactory.newInstance();
      final Transformer transformer = transformerFactory.newTransformer();
      final DOMSource source = new DOMSource(document);
      final StreamResult result = new StreamResult(new File(fileName));
      transformer.transform(source, result);
    } catch (final ParserConfigurationException | TransformerException e) {
      throw new RuntimeException(e);
    }

    fileBusy = false;
    notify();
  }

  public synchronized List<GameMove> getGameMovesFromXml() throws Exception {
    while (fileBusy) {
      wait();
    }

    fileBusy = true;

    final List<GameMove> gameMoves = new ArrayList<>();

    final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setValidating(false);
    final DocumentBuilder builder = factory.newDocumentBuilder();
    final Document document = builder.parse(new File(fileName));

    final NodeList gameMoveNodes = document.getElementsByTagName("gameMove");

    for (int i = 0; i < gameMoveNodes.getLength(); i++) {
      final Node gameMoveNode = gameMoveNodes.item(i);
      if (gameMoveNode.getNodeType() == Node.ELEMENT_NODE) {
        final Element gameMoveElement = (Element) gameMoveNode;

        final String color = gameMoveElement.getElementsByTagName("color").item(0).getTextContent();
        final Integer column = Integer.parseInt(gameMoveElement.getElementsByTagName("column").item(0).getTextContent());

        GameMove gameMove = new GameMove(column, color);
        gameMoves.add(gameMove);
      }
    }

    fileBusy = false;
    notify();

    return gameMoves;
  }
}
