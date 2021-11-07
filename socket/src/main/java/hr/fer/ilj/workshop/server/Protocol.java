package hr.fer.ilj.workshop.server;

import hr.fer.ilj.workshop.files.FileLoader;

public class Protocol {
  private FileLoader loader;

  public Protocol(FileLoader loader) {
    this.loader = loader;
  }

  public String handleRequest(String request) {
    StringBuilder sb = new StringBuilder();
    sb.append("HTTP/1.1 200 OK\r\n");
    sb.append("Content-Type: text/html\r\n");

    String path = parseRequestLine(request);
    String content = getContent(path);
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
