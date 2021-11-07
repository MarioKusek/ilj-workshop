package hr.fer.ilj.workshop.server;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ProtocolTest {

  @Test
  void parsingRootPath() {
    assertThat(Protocol.parseRequestLine("GET / HTTP/1.1")).isEqualTo("/");
  }

  @Test
  void parsingPath() {
    assertThat(Protocol.parseRequestLine("GET /path1 HTTP/1.1")).isEqualTo("/path1");
  }

}
