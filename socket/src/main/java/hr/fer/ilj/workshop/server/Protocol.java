package hr.fer.ilj.workshop.server;

public class Protocol {

  public String handleRequest(String request) {
    StringBuilder sb = new StringBuilder();
    sb.append("HTTP/1.1 200 OK\r\n");
    sb.append("Content-Type: text/html\r\n");
    String content = getContent(request);
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

}
