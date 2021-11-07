package hr.fer.ilj.workshop.server;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hr.fer.ilj.workshop.files.FileLoader;

public class Protocol {
  private Logger LOG = LoggerFactory.getLogger(this.getClass());

  private FileLoader loader;

  public Protocol(FileLoader loader) {
    this.loader = loader;
  }

  public String handleRequest(String request) {
    StringBuilder sb = new StringBuilder();
    String path = parseRequestLine(request);
    String content;
    try {
      content = getContent(loader.loadFiles(path).stream()
          .map(fi -> fi.name())
          .toList().toString()
          );

      sb.append("HTTP/1.1 200 OK\r\n");
      sb.append("Content-Type: text/html\r\n");

    } catch (IOException e) {
      LOG.info("Can not load directory", e);
      sb.append("HTTP/1.1 500 Internal Server Error\r\n");
      sb.append("Content-Type: text/html\r\n");
      content = "Can not load directory";
    }
    sb.append("Content-Length: " + content.length() + "\r\n");
    sb.append("\r\n");
    sb.append(content);
    return sb.toString();
  }

  private String getContent(String body) {
    String content = """
        <html><body>
        %s
        </body></html>
        """;

    return String.format(content, body);
  }

  public static String parseRequestLine(String requestLine) {
    return requestLine.split(" ")[1];
  }

}
