package hr.fer.ilj.workshop.files;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;

class HtmlGeneratorTest {

  @Test
  void generateHtmlForEmptyList() {
    HtmlGenerator generator = new HtmlGenerator();
    assertThat(generator.generate(List.of())).isEqualTo("""
            <p>File list:</p>
            <p>empty</p>
            """);
  }

  @Test
  void generateHtmlForOneElement() {
    HtmlGenerator generator = new HtmlGenerator();
    List<FileInfo> list = List.of(new FileInfo("ime", Path.of("dir/ime"), 0, FileType.FILE));
    assertThat(generator.generate(list)).isEqualTo("""
            <p>File list:</p>
            <ul>
              <li>ime</li>
            </ul>
            """);
  }

}
