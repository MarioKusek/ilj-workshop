package hr.fer.ilj.workshop.server;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import hr.fer.ilj.workshop.files.FileLoader;
import hr.fer.ilj.workshop.files.HtmlGenerator;

@ExtendWith(MockitoExtension.class)
class ProtocolTest {

  @Test
  void parsingRootPath() {
    assertThat(Protocol.parseRequestLine("GET / HTTP/1.1")).isEqualTo("/");
  }

  @Test
  void parsingPath() {
    assertThat(Protocol.parseRequestLine("GET /path1 HTTP/1.1")).isEqualTo("/path1");
  }

  @Test
  void loadingFile(@Mock FileLoader loader) throws Exception {
    Protocol protocol = new Protocol(loader, null);
    NotDirectoryException ex = new NotDirectoryException("/file");
    when(loader.loadFiles("/file")).thenThrow(ex);

    String response = protocol.handleRequest("GET /file HTTP/1.1");

    assertThat(response).startsWith("HTTP/1.1 404 File Not Found\r\n");
  }

  @Test
  void loadingRootParentDirectory() throws Exception {
    FileLoader loader = new FileLoader(Path.of("."));
    Protocol protocol = new Protocol(loader, null);

    String response = protocol.handleRequest("GET /.. HTTP/1.1");

    assertThat(response).startsWith("HTTP/1.1 400 Bad Request\r\n");
  }

  @Test
  void loadingSomeParentDirectory() throws Exception {
    FileLoader loader = new FileLoader(Path.of("."));
    Protocol protocol = new Protocol(loader, null);

    String response = protocol.handleRequest("GET /d/../dd HTTP/1.1");

    assertThat(response).startsWith("HTTP/1.1 400 Bad Request\r\n");
  }

  @Test
  void loadingSomeParentDirectory(@Mock FileLoader loader) throws Exception {
    when(loader.loadFiles(Mockito.anyString())).thenReturn(List.of());
    Protocol protocol = new Protocol(loader, new HtmlGenerator());

    String response = protocol.handleRequest("GET /first+second HTTP/1.1");

    verify(loader).loadFiles("/first second");
  }

}
