package hr.fer.ilj.workshop.server;

import java.io.IOException;
import java.nio.file.NotDirectoryException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hr.fer.ilj.workshop.files.FileLoader;
import hr.fer.ilj.workshop.files.HtmlGenerator;

public class Protocol {
  private Logger LOG = LoggerFactory.getLogger(this.getClass());

  private FileLoader loader;
  private HtmlGenerator generator;

  public Protocol(FileLoader loader, HtmlGenerator generator) {
    this.loader = loader;
    this.generator = generator;
  }

  public String handleRequest(String request) {
    StringBuilder sb = new StringBuilder();
    String path = parseRequestLine(request);
    String content;
    try {
      content = getContent(generator.generate(loader.loadFiles(path)));

      sb.append("HTTP/1.1 200 OK\r\n");
      sb.append("Content-Type: text/html\r\n");
    } catch (NotDirectoryException e) {
      LOG.info("Can not load file", e);
      sb.append("HTTP/1.1 404 File Not Found\r\n");
      sb.append("Content-Type: text/html\r\n");
      content = "Can not load file as directory";
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
